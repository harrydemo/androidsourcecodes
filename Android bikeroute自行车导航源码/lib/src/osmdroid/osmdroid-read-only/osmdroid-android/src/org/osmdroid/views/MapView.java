// Created by plusminus on 17:45:56 - 25.09.2008
package org.osmdroid.views;

import java.util.LinkedList;
import java.util.List;

import net.wigle.wigleandroid.ZoomButtonsController;
import net.wigle.wigleandroid.ZoomButtonsController.OnZoomListener;

import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.MultiTouchObjectCanvas;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;
import org.metalev.multitouch.controller.MultiTouchController.PositionAndScale;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.api.IProjection;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.IStyledTileSource;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.constants.GeoConstants;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Overlay.Snappable;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.util.Mercator;
import org.osmdroid.views.util.constants.MapViewConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Scroller;

public class MapView extends View implements IMapView, MapViewConstants,
		MultiTouchObjectCanvas<Object> {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final Logger logger = LoggerFactory.getLogger(MapView.class);

	final static String BUNDLE_TILE_SOURCE = "org.osmdroid.views.MapView.TILE_SOURCE";
	final static String BUNDLE_SCROLL_X = "org.osmdroid.views.MapView.SCROLL_X";
	final static String BUNDLE_SCROLL_Y = "org.osmdroid.views.MapView.SCROLL_Y";
	final static String BUNDLE_ZOOM_LEVEL = "org.osmdroid.views.MapView.ZOOM";

	private static final double ZOOM_SENSITIVITY = 1.3;
	private static final double ZOOM_LOG_BASE_INV = 1.0 / Math.log(2.0 / ZOOM_SENSITIVITY);

	// ===========================================================
	// Fields
	// ===========================================================

	/** Current zoom level for map tiles. */
	private int mZoomLevel = 0;

	private int mTileSizePixels = 0;

	private final LinkedList<Overlay> mOverlays = new LinkedList<Overlay>();

	private Projection mProjection;

	private final TilesOverlay mMapOverlay;

	private final GestureDetector mGestureDetector;

	/** Handles map scrolling */
	private final Scroller mScroller;

	private final ScaleAnimation mZoomInAnimation;
	private final ScaleAnimation mZoomOutAnimation;
	private final MyAnimationListener mAnimationListener = new MyAnimationListener();

	private final MapController mController;

	// XXX we can use android.widget.ZoomButtonsController if we upgrade the
	// dependency to Android 1.6
	private final ZoomButtonsController mZoomController;
	private boolean mEnableZoomController = false;

	private ResourceProxy mResourceProxy;

	private MultiTouchController<Object> mMultiTouchController;
	private float mMultiTouchScale = 1.0f;

	protected MapListener mListener;

	// for speed (avoiding allocations)
	private final Matrix mMatrix = new Matrix();
	private final MapTileProviderBase mTileProvider;

	private final Handler mTileRequestCompleteHandler;

	// ===========================================================
	// Constructors
	// ===========================================================

	private MapView(final Context context, final Handler tileRequestCompleteHandler,
			final AttributeSet attrs, final int tileSizePixels, MapTileProviderBase tileProvider) {
		super(context, attrs);
		mResourceProxy = new DefaultResourceProxyImpl(context);
		this.mController = new MapController(this);
		this.mScroller = new Scroller(context);
		this.mTileSizePixels = tileSizePixels;

		if (tileProvider == null) {
			final ITileSource tileSource = getTileSourceFromAttributes(attrs);
			tileProvider = new MapTileProviderBasic(context, tileSource);
		}

		mTileRequestCompleteHandler = tileRequestCompleteHandler == null ? new SimpleInvalidationHandler(
				this) : tileRequestCompleteHandler;
		mTileProvider = tileProvider;
		mTileProvider.setTileRequestCompleteHandler(mTileRequestCompleteHandler);

		this.mMapOverlay = new TilesOverlay(mTileProvider, mResourceProxy);
		mOverlays.add(this.mMapOverlay);
		this.mZoomController = new ZoomButtonsController(this);
		this.mZoomController.setOnZoomListener(new MapViewZoomListener());

		mZoomInAnimation = new ScaleAnimation(1, 2, 1, 2, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mZoomOutAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mZoomInAnimation.setDuration(ANIMATION_DURATION_SHORT);
		mZoomOutAnimation.setDuration(ANIMATION_DURATION_SHORT);
		mZoomInAnimation.setAnimationListener(mAnimationListener);
		mZoomOutAnimation.setAnimationListener(mAnimationListener);

		mGestureDetector = new GestureDetector(context, new MapViewGestureDetectorListener());
		mGestureDetector.setOnDoubleTapListener(new MapViewDoubleClickListener());
	}

	/**
	 * Constructor used by XML layout resource (uses default tile source).
	 */
	public MapView(final Context context, final AttributeSet attrs) {
		this(context, null, attrs, 256, null);
	}

	/**
	 * Standard Constructor.
	 */
	public MapView(final Context context, final int tileSizePixels,
			final MapTileProviderBase aTileProvider) {
		this(context, null, null, tileSizePixels, aTileProvider);
	}

	public MapView(final Context context, final int tileSizePixels) {
		this(context, null, null, tileSizePixels, null);
	}

	public MapView(final Context context, final Handler tileRequestCompleteHandler,
			final int tileSizePixels, final MapTileProviderBase aTileProvider) {
		this(context, tileRequestCompleteHandler, null, tileSizePixels, aTileProvider);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public MapController getController() {
		return this.mController;
	}

	/**
	 * You can add/remove/reorder your Overlays using the List of {@link Overlay}. The first (index
	 * 0) Overlay gets drawn first, the one with the highest as the last one.
	 */
	public List<Overlay> getOverlays() {
		return this.mOverlays;
	}

	public MapTileProviderBase getTileProvider() {
		return mTileProvider;
	}

	public Scroller getScroller() {
		return mScroller;
	}

	public Handler getTileRequestCompleteHandler() {
		return mTileRequestCompleteHandler;
	}

	@Override
	public int getLatitudeSpan() {
		return this.getBoundingBox().getLatitudeSpanE6();
	}

	@Override
	public int getLongitudeSpan() {
		return this.getBoundingBox().getLongitudeSpanE6();
	}

	public static int getMapTileZoom(final int tileSizePixels) {
		if (tileSizePixels <= 0) {
			return 0;
		}

		int pixels = tileSizePixels;
		int a = 0;
		while (pixels != 0) {
			pixels >>= 1;
			a++;
		}
		return a - 1;
	}

	public BoundingBoxE6 getBoundingBox() {
		return getBoundingBox(getWidth(), getHeight());
	}

	public BoundingBoxE6 getBoundingBox(final int pViewWidth, final int pViewHeight) {
		final int mapTileZoom = getMapTileZoom(mTileSizePixels);
		final int world_2 = 1 << mZoomLevel + mapTileZoom - 1;
		final int north = world_2 + getScrollY() - getHeight() / 2;
		final int south = world_2 + getScrollY() + getHeight() / 2;
		final int west = world_2 + getScrollX() - getWidth() / 2;
		final int east = world_2 + getScrollX() + getWidth() / 2;

		return Mercator
				.getBoundingBoxFromCoords(west, north, east, south, mZoomLevel + mapTileZoom);
	}

	/**
	 * This class is only meant to be used during on call of onDraw(). Otherwise it may produce
	 * strange results.
	 * 
	 * @return
	 */
	@Override
	public Projection getProjection() {
		if (mProjection == null) {
			mProjection = new Projection();
		}
		return mProjection;
	}

	void setMapCenter(final GeoPoint aCenter) {
		this.setMapCenter(aCenter.getLatitudeE6(), aCenter.getLongitudeE6());
	}

	void setMapCenter(final int aLatitudeE6, final int aLongitudeE6) {
		final Point coords = Mercator.projectGeoPoint(aLatitudeE6, aLongitudeE6,
				getPixelZoomLevel(), null);
		final int worldSize_2 = getWorldSizePx() / 2;
		if (getAnimation() == null || getAnimation().hasEnded()) {
			logger.debug("StartScroll");
			mScroller.startScroll(getScrollX(), getScrollY(),
					coords.x - worldSize_2 - getScrollX(), coords.y - worldSize_2 - getScrollY(),
					500);
			postInvalidate();
		}
	}

	public void setTileSource(final ITileSource aTileSource) {
		mTileProvider.setTileSource(aTileSource);
		mTileSizePixels = aTileSource.getTileSizePixels();
		this.checkZoomButtons();
		this.setZoomLevel(mZoomLevel); // revalidate zoom level
		postInvalidate();
	}

	/**
	 * @param aZoomLevel
	 *            the zoom level bound by the tile source
	 */
	int setZoomLevel(final int aZoomLevel) {
		final int minZoomLevel = getMinimumZoomLevel();
		final int maxZoomLevel = getMaximumZoomLevel();

		final int newZoomLevel = Math.max(minZoomLevel, Math.min(maxZoomLevel, aZoomLevel));
		final int curZoomLevel = this.mZoomLevel;

		this.mZoomLevel = newZoomLevel;
		this.checkZoomButtons();

		if (newZoomLevel > curZoomLevel) {
			scrollTo(getScrollX() << newZoomLevel - curZoomLevel, getScrollY() << newZoomLevel
					- curZoomLevel);
		} else if (newZoomLevel < curZoomLevel) {
			scrollTo(getScrollX() >> curZoomLevel - newZoomLevel, getScrollY() >> curZoomLevel
					- newZoomLevel);
		}

		// snap for all snappables
		final Point snapPoint = new Point();
		// XXX why do we need a new projection here?
		mProjection = new Projection();
		for (int i = mOverlays.size() - 1; i >= 0; i--)
			if (mOverlays.get(i) instanceof Snappable
					&& ((Snappable) mOverlays.get(i)).onSnapToItem(getScrollX(), getScrollY(),
							snapPoint, this)) {
				scrollTo(snapPoint.x, snapPoint.y);
			}

		// do callback on listener
		if (newZoomLevel != curZoomLevel && mListener != null) {
			final ZoomEvent event = new ZoomEvent(this, newZoomLevel);
			mListener.onZoom(event);
		}
		return this.mZoomLevel;
	}

	/**
	 * Get the current ZoomLevel for the map tiles.
	 * 
	 * @return the current ZoomLevel between 0 (equator) and 18/19(closest), depending on the tile
	 *         source chosen.
	 */
	@Override
	public int getZoomLevel() {
		return getZoomLevel(true);
	}

	/**
	 * Get the current ZoomLevel for the map tiles.
	 * 
	 * @param aPending
	 *            if true and we're animating then return the zoom level that we're animating
	 *            towards, otherwise return the current zoom level
	 * @return the zoom level
	 */
	public int getZoomLevel(final boolean aPending) {
		if (aPending && mAnimationListener.animating) {
			return mAnimationListener.targetZoomLevel;
		} else {
			return mZoomLevel;
		}
	}

	/**
	 * Returns the minimum zoom level for the point currently at the center.
	 * 
	 * @return The minimum zoom level for the map's current center.
	 */
	public int getMinimumZoomLevel() {
		return mMapOverlay.getMinimumZoomLevel();
	}

	/**
	 * @deprecated Replaced by {@link #getMaxZoomLevel()}
	 */
	@Deprecated
	public int getMaximumZoomLevel() {
		return mMapOverlay.getMaximumZoomLevel();
	}

	/**
	 * Returns the maximum zoom level for the point currently at the center.
	 * 
	 * @return The maximum zoom level for the map's current center.
	 */
	@Override
	public int getMaxZoomLevel() {
		return mMapOverlay.getMaximumZoomLevel();
	}

	public boolean canZoomIn() {
		final int maxZoomLevel = getMaxZoomLevel();
		if (mZoomLevel >= maxZoomLevel) {
			return false;
		}
		if (mAnimationListener.animating && mAnimationListener.targetZoomLevel >= maxZoomLevel) {
			return false;
		}
		return true;
	}

	public boolean canZoomOut() {
		final int minZoomLevel = getMinimumZoomLevel();
		if (mZoomLevel <= minZoomLevel) {
			return false;
		}
		if (mAnimationListener.animating && mAnimationListener.targetZoomLevel <= minZoomLevel) {
			return false;
		}
		return true;
	}

	/**
	 * Zoom in by one zoom level.
	 */
	boolean zoomIn() {

		if (canZoomIn()) {
			if (mAnimationListener.animating) {
				// TODO extend zoom (and return true)
				return false;
			} else {
				mAnimationListener.targetZoomLevel = mZoomLevel + 1;
				mAnimationListener.animating = true;
				startAnimation(mZoomInAnimation);
				return true;
			}
		} else {
			return false;
		}
	}

	boolean zoomInFixing(final GeoPoint point) {
		setMapCenter(point); // TODO should fix on point, not center on it
		return zoomIn();
	}

	boolean zoomInFixing(final int xPixel, final int yPixel) {
		setMapCenter(xPixel, yPixel); // TODO should fix on point, not center on it
		return zoomIn();
	}

	/**
	 * Zoom out by one zoom level.
	 */
	boolean zoomOut() {

		if (canZoomOut()) {
			if (mAnimationListener.animating) {
				// TODO extend zoom (and return true)
				return false;
			} else {
				mAnimationListener.targetZoomLevel = mZoomLevel - 1;
				mAnimationListener.animating = true;
				startAnimation(mZoomOutAnimation);
				return true;
			}
		} else {
			return false;
		}
	}

	boolean zoomOutFixing(final GeoPoint point) {
		setMapCenter(point); // TODO should fix on point, not center on it
		return zoomOut();
	}

	boolean zoomOutFixing(final int xPixel, final int yPixel) {
		setMapCenter(xPixel, yPixel); // TODO should fix on point, not center on it
		return zoomOut();
	}

	@Override
	public GeoPoint getMapCenter() {
		return new GeoPoint(getMapCenterLatitudeE6(), getMapCenterLongitudeE6());
	}

	public int getMapCenterLatitudeE6() {
		return (int) (Mercator.tile2lat(getScrollY() + getWorldSizePx() / 2, getPixelZoomLevel()) * 1E6);
	}

	public int getMapCenterLongitudeE6() {
		return (int) (Mercator.tile2lon(getScrollX() + getWorldSizePx() / 2, getPixelZoomLevel()) * 1E6);
	}

	public void setResourceProxy(final ResourceProxy pResourceProxy) {
		mResourceProxy = pResourceProxy;
	}

	public void onSaveInstanceState(final Bundle state) {
		state.putInt(BUNDLE_SCROLL_X, getScrollX());
		state.putInt(BUNDLE_SCROLL_Y, getScrollY());
		state.putInt(BUNDLE_ZOOM_LEVEL, getZoomLevel());
	}

	public void onRestoreInstanceState(final Bundle state) {

		setZoomLevel(state.getInt(BUNDLE_ZOOM_LEVEL, 1));
		scrollTo(state.getInt(BUNDLE_SCROLL_X, 0), state.getInt(BUNDLE_SCROLL_Y, 0));
	}

	/**
	 * Whether to use the network connection if it's available.
	 */
	public boolean useDataConnection() {
		return mMapOverlay.useDataConnection();
	}

	/**
	 * Set whether to use the network connection if it's available.
	 * 
	 * @param aMode
	 *            if true use the network connection if it's available. if false don't use the
	 *            network connection even if it's available.
	 */
	public void setUseDataConnection(final boolean aMode) {
		mMapOverlay.setUseDataConnection(aMode);
	}

	/**
	 * Check mAnimationListener.animating to determine if view is animating. Useful for overlays to
	 * avoid recalculating during an animation sequence.
	 * 
	 * @return boolean indicating whether view is animating.
	 */
	public boolean isAnimating() {
		return mAnimationListener.animating;
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	public void onDetach() {
		for (int i = mOverlays.size() - 1; i >= 0; i--)
			mOverlays.get(i).onDetach(this);
	}

	public void onLongPress(final MotionEvent e) {
		for (int i = mOverlays.size() - 1; i >= 0; i--)
			if (mOverlays.get(i).onLongPress(e, this))
				return;
	}

	public boolean onSingleTapUp(final MotionEvent e) {
		for (int i = mOverlays.size() - 1; i >= 0; i--)
			if (mOverlays.get(i).onSingleTapUp(e, this)) {
				postInvalidate();
				return true;
			}

		return false;
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		for (int i = mOverlays.size() - 1; i >= 0; i--)
			if (mOverlays.get(i).onKeyDown(keyCode, event, this))
				return true;

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(final int keyCode, final KeyEvent event) {
		for (int i = mOverlays.size() - 1; i >= 0; i--)
			if (mOverlays.get(i).onKeyUp(keyCode, event, this))
				return true;

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTrackballEvent(final MotionEvent event) {
		for (int i = mOverlays.size() - 1; i >= 0; i--)
			if (mOverlays.get(i).onTrackballEvent(event, this))
				return true;

		scrollBy((int) (event.getX() * 25), (int) (event.getY() * 25));

		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {

		if (DEBUGMODE) {
			logger.debug("onTouchEvent(" + event + ")");
		}

		for (int i = mOverlays.size() - 1; i >= 0; i--)
			if (mOverlays.get(i).onTouchEvent(event, this)) {
				if (DEBUGMODE) {
					logger.debug("overlay handled onTouchEvent");
				}
				return true;
			}

		if (mMultiTouchController != null && mMultiTouchController.onTouchEvent(event)) {
			if (DEBUGMODE) {
				logger.debug("mMultiTouchController handled onTouchEvent");
			}
			return true;
		}

		if (mGestureDetector.onTouchEvent(event)) {
			if (DEBUGMODE) {
				logger.debug("mGestureDetector handled onTouchEvent");
			}
			return true;
		}

		final boolean r = super.onTouchEvent(event);
		if (r) {
			if (DEBUGMODE) {
				logger.debug("super handled onTouchEvent");
			}
		} else {
			if (DEBUGMODE) {
				logger.debug("no-one handled onTouchEvent");
			}
		}
		return r;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScroller.isFinished()) {
				setZoomLevel(mZoomLevel);
			} else {
				scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			}
			postInvalidate(); // Keep on drawing until the animation has
			// finished.
		}
	}

	@Override
	public void scrollTo(int x, int y) {
		final int worldSize = getWorldSizePx();
		x %= worldSize;
		y %= worldSize;
		super.scrollTo(x, y);

		// do callback on listener
		if (mListener != null) {
			final ScrollEvent event = new ScrollEvent(this, x, y);
			mListener.onScroll(event);
		}
	}

	@Override
	public void onDraw(final Canvas c) {
		final long startMs = System.currentTimeMillis();

		mProjection = new Projection();

		if (mMultiTouchScale == 1.0f) {
			c.translate(getWidth() / 2, getHeight() / 2);
		} else {
			c.getMatrix(mMatrix);
			mMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
			mMatrix.preScale(mMultiTouchScale, mMultiTouchScale, getScrollX(), getScrollY());
			c.setMatrix(mMatrix);
		}

		/* Draw background */
		c.drawColor(Color.LTGRAY);
		// This is too slow:
		// final Rect r = c.getClipBounds();
		// mPaint.setColor(Color.GRAY);
		// mPaint.setPathEffect(new DashPathEffect(new float[] {1, 1}, 0));
		// for (int x = r.left; x < r.right; x += 20)
		// c.drawLine(x, r.top, x, r.bottom, mPaint);
		// for (int y = r.top; y < r.bottom; y += 20)
		// c.drawLine(r.left, y, r.right, y, mPaint);

		/* Draw all Overlays. Avoid allocation by not doing enhanced loop. */
		for (int i = 0; i < mOverlays.size(); i++) {
			mOverlays.get(i).onManagedDraw(c, this);
		}

		final long endMs = System.currentTimeMillis();
		if (DEBUGMODE) {
			logger.debug("Rendering overall: " + (endMs - startMs) + "ms");
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		this.mZoomController.setVisible(false);
		this.onDetach();
		super.onDetachedFromWindow();
	}

	// ===========================================================
	// Implementation of MultiTouchObjectCanvas
	// ===========================================================

	@Override
	public Object getDraggableObjectAtPoint(final PointInfo pt) {
		return this;
	}

	@Override
	public void getPositionAndScale(final Object obj, final PositionAndScale objPosAndScaleOut) {
		objPosAndScaleOut.set(0, 0, true, mMultiTouchScale, false, 0, 0, false, 0);
	}

	@Override
	public void selectObject(final Object obj, final PointInfo pt) {
		// if obj is null it means we released the pointers
		// if scale is not 1 it means we pinched
		if (obj == null && mMultiTouchScale != 1.0f) {
			final float scaleDiffFloat = (float) (Math.log(mMultiTouchScale) * ZOOM_LOG_BASE_INV);
			final int scaleDiffInt = Math.round(scaleDiffFloat);
			setZoomLevel(mZoomLevel + scaleDiffInt);
			// XXX maybe zoom in/out instead of zooming direct to zoom level
			// - probably not a good idea because you'll repeat the animation
		}

		// reset scale
		mMultiTouchScale = 1.0f;
	}

	@Override
	public boolean setPositionAndScale(final Object obj, final PositionAndScale aNewObjPosAndScale,
			final PointInfo aTouchPoint) {
		mMultiTouchScale = aNewObjPosAndScale.getScale();
		invalidate(); // redraw
		return true;
	}

	/*
	 * Set the MapListener for this view
	 */
	public void setMapListener(final MapListener ml) {
		mListener = ml;
	}

	// ===========================================================
	// Package Methods
	// ===========================================================

	/**
	 * Get the world size in pixels.
	 */
	int getWorldSizePx() {
		return 1 << getPixelZoomLevel();
	}

	/**
	 * Get the equivalent zoom level on pixel scale
	 */
	int getPixelZoomLevel() {
		return this.mZoomLevel + getMapTileZoom(mTileSizePixels);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void checkZoomButtons() {
		this.mZoomController.setZoomInEnabled(canZoomIn());
		this.mZoomController.setZoomOutEnabled(canZoomOut());
	}

	/**
	 * @param centerMapTileCoords
	 * @param tileSizePx
	 * @param reuse
	 *            just pass null if you do not have a Point to be 'recycled'.
	 */
	private Point getUpperLeftCornerOfCenterMapTileInScreen(final Point centerMapTileCoords,
			final int tileSizePx, final Point reuse) {
		final Point out = reuse != null ? reuse : new Point();

		final int worldTiles_2 = 1 << mZoomLevel - 1;
		final int centerMapTileScreenLeft = (centerMapTileCoords.x - worldTiles_2) * tileSizePx
				- tileSizePx / 2;
		final int centerMapTileScreenTop = (centerMapTileCoords.y - worldTiles_2) * tileSizePx
				- tileSizePx / 2;

		out.set(centerMapTileScreenLeft, centerMapTileScreenTop);
		return out;
	}

	public void setBuiltInZoomControls(final boolean on) {
		this.mEnableZoomController = on;
		this.checkZoomButtons();
	}

	public void setMultiTouchControls(final boolean on) {
		mMultiTouchController = on ? new MultiTouchController<Object>(this, false) : null;
	}

	private ITileSource getTileSourceFromAttributes(final AttributeSet aAttributeSet) {

		ITileSource tileSource = TileSourceFactory.DEFAULT_TILE_SOURCE;

		if (aAttributeSet != null) {
			final String tileSourceAttr = aAttributeSet.getAttributeValue(null, "tilesource");
			if (tileSourceAttr != null) {
				try {
					final ITileSource r = TileSourceFactory.getTileSource(tileSourceAttr);
					logger.info("Using tile source specified in layout attributes: " + r);
					tileSource = r;
				} catch (final IllegalArgumentException e) {
					logger.warn("Invalid tile souce specified in layout attributes: " + tileSource);
				}
			}
		}

		if (aAttributeSet != null && tileSource instanceof IStyledTileSource) {
			String style = aAttributeSet.getAttributeValue(null, "style");
			if (style == null) {
				// historic - old attribute name
				style = aAttributeSet.getAttributeValue(null, "cloudmadeStyle");
			}
			if (style == null) {
				logger.info("Using default style: 1");
			} else {
				logger.info("Using style specified in layout attributes: " + style);
				((IStyledTileSource<?>) tileSource).setStyle(style);
			}
		}

		logger.info("Using tile source: " + tileSource);
		return tileSource;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	/**
	 * This class may return valid results until the underlying {@link MapView} gets modified in any
	 * way (i.e. new center).
	 * 
	 * @author Nicolas Gramlich
	 * @author Manuel Stahl
	 */
	public class Projection implements IProjection, GeoConstants {

		private final int viewWidth_2 = getWidth() / 2;
		private final int viewHeight_2 = getHeight() / 2;
		private final int worldSize_2 = getWorldSizePx() / 2;
		private final int offsetX = -worldSize_2;
		private final int offsetY = -worldSize_2;

		private final BoundingBoxE6 mBoundingBoxProjection;
		private final int mZoomLevelProjection;
		private final int mTileSizePixelsProjection;
		private final int mTileMapZoomProjection;
		private final Point mCenterMapTileCoordsProjection;
		private final Point mUpperLeftCornerOfCenterMapTileProjection;

		private Projection() {

			/*
			 * Do some calculations and drag attributes to local variables to save some performance.
			 */
			mZoomLevelProjection = mZoomLevel;
			// TODO Draw to attributes and so make it only 'valid' for a short time.
			mTileSizePixelsProjection = mTileSizePixels;
			mTileMapZoomProjection = getMapTileZoom(getTileSizePixels());

			/*
			 * Get the center MapTile which is above this.mLatitudeE6 and this.mLongitudeE6 .
			 */
			mCenterMapTileCoordsProjection = calculateCenterMapTileCoords(getTileSizePixels(),
					getZoomLevel());
			mUpperLeftCornerOfCenterMapTileProjection = getUpperLeftCornerOfCenterMapTileInScreen(
					getCenterMapTileCoords(), getTileSizePixels(), null);

			mBoundingBoxProjection = MapView.this.getBoundingBox();
		}

		public int getTileSizePixels() {
			return mTileSizePixelsProjection;
		}

		public int getTileMapZoom() {
			return mTileMapZoomProjection;
		}

		public int getZoomLevel() {
			return mZoomLevelProjection;
		}

		public Point getCenterMapTileCoords() {
			return mCenterMapTileCoordsProjection;
		}

		public Point getUpperLeftCornerOfCenterMapTile() {
			return mUpperLeftCornerOfCenterMapTileProjection;
		}

		public BoundingBoxE6 getBoundingBox() {
			return mBoundingBoxProjection;
		}

		private Point calculateCenterMapTileCoords(final int tileSizePixels, final int zoomLevel) {
			final int mapTileZoom = getMapTileZoom(tileSizePixels);
			final int worldTiles_2 = 1 << zoomLevel - 1;
			// convert to tile coordinate and make positive
			return new Point((getScrollX() >> mapTileZoom) + worldTiles_2,
					(getScrollY() >> mapTileZoom) + worldTiles_2);
		}

		/**
		 * Converts x/y ScreenCoordinates to the underlying GeoPoint.
		 * 
		 * @param x
		 * @param y
		 * @return GeoPoint under x/y.
		 */
		public GeoPoint fromPixels(final float x, final float y) {
			return getBoundingBox().getGeoPointOfRelativePositionWithLinearInterpolation(
					x / getWidth(), y / getHeight());
		}

		public Point fromMapPixels(final int x, final int y, final Point reuse) {
			final Point out = reuse != null ? reuse : new Point();
			out.set(x - viewWidth_2, y - viewHeight_2);
			out.offset(getScrollX(), getScrollY());
			return out;
		}

		/**
		 * Converts a GeoPoint to its ScreenCoordinates. <br/>
		 * <br/>
		 * <b>CAUTION</b> ! Conversion currently has a large error on <code>zoomLevels <= 7</code>.<br/>
		 * The Error on ZoomLevels higher than 7, the error is below <code>1px</code>.<br/>
		 * TODO: Add a linear interpolation to minimize this error.
		 * 
		 * <PRE>
		 * Zoom 	Error(m) 	Error(px)
		 * 11 	6m 	1/12px
		 * 10 	24m 	1/6px
		 * 8 	384m 	1/2px
		 * 6 	6144m 	3px
		 * 4 	98304m 	10px
		 * </PRE>
		 * 
		 * @param in
		 *            the GeoPoint you want the onScreenCoordinates of.
		 * @param reuse
		 *            just pass null if you do not have a Point to be 'recycled'.
		 * @return the Point containing the approximated ScreenCoordinates of the GeoPoint passed.
		 */
		public Point toMapPixels(final GeoPoint in, final Point reuse) {
			final Point out = reuse != null ? reuse : new Point();

			final Point coords = Mercator.projectGeoPoint(in.getLatitudeE6(), in.getLongitudeE6(),
					getPixelZoomLevel(), null);
			out.set(coords.x, coords.y);
			out.offset(offsetX, offsetY);
			return out;
		}

		/**
		 * Performs only the first computationally heavy part of the projection, needToCall
		 * toMapPixelsTranslated to get final position.
		 * 
		 * @param latituteE6
		 *            the latitute of the point
		 * @param longitudeE6
		 *            the longitude of the point
		 * @param reuse
		 *            just pass null if you do not have a Point to be 'recycled'.
		 * @return intermediate value to be stored and passed to toMapPixelsTranslated on paint.
		 */
		public Point toMapPixelsProjected(final int latituteE6, final int longitudeE6,
				final Point reuse) {
			final Point out = reuse != null ? reuse : new Point();

			// 26 is the biggest zoomlevel we can project
			final Point coords = Mercator.projectGeoPoint(latituteE6, longitudeE6, 28, out);
			out.set(coords.x, coords.y);
			return out;
		}

		/**
		 * Performs the second computationally light part of the projection.
		 * 
		 * @param in
		 *            the Point calculated by the toMapPixelsProjected
		 * @param reuse
		 *            just pass null if you do not have a Point to be 'recycled'.
		 * @return the Point containing the approximated ScreenCoordinates of the initial GeoPoint
		 *         passed to the toMapPixelsProjected.
		 */
		public Point toMapPixelsTranslated(final Point in, final Point reuse) {
			final Point out = reuse != null ? reuse : new Point();

			// 26 is the biggest zoomlevel we can project
			final int zoomDifference = 28 - getPixelZoomLevel();
			out.set((in.x >> zoomDifference) + offsetX, (in.y >> zoomDifference) + offsetY);
			return out;
		}

		/**
		 * Translates a rectangle from screen coordinates to intermediate coordinates.
		 * 
		 * @param in
		 *            the rectangle in screen coordinates
		 * @return a rectangle in intermediate coords.
		 */
		public Rect fromPixelsToProjected(final Rect in) {
			final Rect result = new Rect();

			// 26 is the biggest zoomlevel we can project
			final int zoomDifference = 28 - getPixelZoomLevel();

			final int x0 = in.left - offsetX << zoomDifference;
			final int x1 = in.right - offsetX << zoomDifference;
			final int y0 = in.bottom - offsetX << zoomDifference;
			final int y1 = in.top - offsetX << zoomDifference;

			result.set(Math.min(x0, x1), Math.min(y0, y1), Math.max(x0, x1), Math.max(y0, y1));
			return result;
		}

		public Point toPixels(final Point tileCoords, final Point reuse) {
			return toPixels(tileCoords.x, tileCoords.y, reuse);
		}

		public Point toPixels(final int tileX, final int tileY, final Point reuse) {
			final Point out = reuse != null ? reuse : new Point();

			out.set(tileX * getTileSizePixels(), tileY * getTileSizePixels());
			out.offset(offsetX, offsetY);

			return out;
		}

		// not presently used
		public Rect toPixels(final BoundingBoxE6 pBoundingBoxE6) {
			final Rect rect = new Rect();

			final Point reuse = new Point();

			toMapPixels(
					new GeoPoint(pBoundingBoxE6.getLatNorthE6(), pBoundingBoxE6.getLonWestE6()),
					reuse);
			rect.left = reuse.x;
			rect.top = reuse.y;

			toMapPixels(
					new GeoPoint(pBoundingBoxE6.getLatSouthE6(), pBoundingBoxE6.getLonEastE6()),
					reuse);
			rect.right = reuse.x;
			rect.bottom = reuse.y;

			return rect;
		}

		@Override
		public float metersToEquatorPixels(final float meters) {
			return meters / EQUATORCIRCUMFENCE * getWorldSizePx();
		}

		@Override
		public Point toPixels(final GeoPoint in, final Point out) {
			return toMapPixels(in, out);
		}

		@Override
		public GeoPoint fromPixels(final int x, final int y) {
			return fromPixels((float) x, (float) y);
		}
	}

	private class MapViewGestureDetectorListener implements OnGestureListener {

		@Override
		public boolean onDown(final MotionEvent e) {
			mZoomController.setVisible(mEnableZoomController);
			return true;
		}

		@Override
		public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX,
				final float velocityY) {
			final int worldSize = getWorldSizePx();
			mScroller.fling(getScrollX(), getScrollY(), (int) -velocityX, (int) -velocityY,
					-worldSize, worldSize, -worldSize, worldSize);
			return true;
		}

		@Override
		public void onLongPress(final MotionEvent e) {
			MapView.this.onLongPress(e);
		}

		@Override
		public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX,
				final float distanceY) {
			scrollBy((int) distanceX, (int) distanceY);
			return true;
		}

		@Override
		public void onShowPress(final MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(final MotionEvent e) {
			return MapView.this.onSingleTapUp(e);
		}

	}

	private class MapViewDoubleClickListener implements GestureDetector.OnDoubleTapListener {
		@Override
		public boolean onDoubleTap(final MotionEvent e) {
			for (int i = mOverlays.size() - 1; i >= 0; i--)
				if (mOverlays.get(i).onDoubleTapUp(e, MapView.this))
					return true;

			final GeoPoint center = getProjection().fromPixels(e.getX(), e.getY());
			return zoomInFixing(center);
		}

		@Override
		public boolean onDoubleTapEvent(final MotionEvent e) {
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(final MotionEvent e) {
			return false;
		}
	}

	private class MapViewZoomListener implements OnZoomListener {
		@Override
		public void onZoom(final boolean zoomIn) {
			if (zoomIn) {
				getController().zoomIn();
			} else {
				getController().zoomOut();
			}
		}

		@Override
		public void onVisibilityChanged(final boolean visible) {
		}
	}

	private class MyAnimationListener implements AnimationListener {
		private int targetZoomLevel;
		private boolean animating;

		@Override
		public void onAnimationEnd(final Animation aAnimation) {
			animating = false;
			setZoomLevel(targetZoomLevel);
		}

		@Override
		public void onAnimationRepeat(final Animation aAnimation) {
		}

		@Override
		public void onAnimationStart(final Animation aAnimation) {
			animating = true;
		}

	}
}
