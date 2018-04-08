// Created by plusminus on 17:45:56 - 25.09.2008
package org.andnav.osm.views;

import java.util.ArrayList;
import java.util.List;

import net.wigle.wigleandroid.ZoomButtonsController;
import net.wigle.wigleandroid.ZoomButtonsController.OnZoomListener;

import org.andnav.osm.DefaultResourceProxyImpl;
import org.andnav.osm.ResourceProxy;
import org.andnav.osm.events.MapListener;
import org.andnav.osm.events.ScrollEvent;
import org.andnav.osm.events.ZoomEvent;
import org.andnav.osm.tileprovider.IRegisterReceiver;
import org.andnav.osm.tileprovider.OpenStreetMapTile;
import org.andnav.osm.tileprovider.util.CloudmadeUtil;
import org.andnav.osm.util.BoundingBoxE6;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.overlay.OpenStreetMapTilesOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay.Snappable;
import org.andnav.osm.views.util.IOpenStreetMapRendererInfo;
import org.andnav.osm.views.util.Mercator;
import org.andnav.osm.views.util.OpenStreetMapRendererFactory;
import org.andnav.osm.views.util.OpenStreetMapTileProvider;
import org.andnav.osm.views.util.OpenStreetMapTileProviderDirect;
import org.andnav.osm.views.util.constants.OpenStreetMapViewConstants;
import org.metalev.multitouch.controller.MultiTouchController;
import org.metalev.multitouch.controller.MultiTouchController.MultiTouchObjectCanvas;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;
import org.metalev.multitouch.controller.MultiTouchController.PositionAndScale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class OpenStreetMapView extends View implements OpenStreetMapViewConstants, MultiTouchObjectCanvas<Object> {

	// ===========================================================
	// Constants
	// ===========================================================

	private static final Logger logger = LoggerFactory.getLogger(OpenStreetMapView.class);

   	final static String BUNDLE_RENDERER = "org.andnav.osm.views.OpenStreetMapView.RENDERER";
	final static String BUNDLE_SCROLL_X = "org.andnav.osm.views.OpenStreetMapView.SCROLL_X";
	final static String BUNDLE_SCROLL_Y = "org.andnav.osm.views.OpenStreetMapView.SCROLL_Y";
	final static String BUNDLE_ZOOM_LEVEL = "org.andnav.osm.views.OpenStreetMapView.ZOOM";

	private static final double ZOOM_SENSITIVITY = 1.3;
	private static final double ZOOM_LOG_BASE_INV = 1.0 / Math.log(2.0 / ZOOM_SENSITIVITY);

	// ===========================================================
	// Fields
	// ===========================================================

	/** Current zoom level for map tiles. */
	private int mZoomLevel = 0;

	private final ArrayList<OpenStreetMapViewOverlay> mOverlays = new ArrayList<OpenStreetMapViewOverlay>();

	private final Paint mPaint = new Paint();
	private OpenStreetMapViewProjection mProjection;

	private OpenStreetMapView mMiniMap, mMaxiMap;
	private final OpenStreetMapTilesOverlay mMapOverlay;

	private final GestureDetector mGestureDetector;

	/** Handles map scrolling */
	private final Scroller mScroller;

	private final ScaleAnimation mZoomInAnimation;
	private final ScaleAnimation mZoomOutAnimation;
	private final MyAnimationListener mAnimationListener = new MyAnimationListener();

	private OpenStreetMapViewController mController;
	private int mMiniMapOverriddenVisibility = NOT_SET;
	private int mMiniMapZoomDiff = NOT_SET;

	// XXX we can use android.widget.ZoomButtonsController if we upgrade the dependency to Android 1.6
	private ZoomButtonsController mZoomController;
	private boolean mEnableZoomController = false;

	private ResourceProxy mResourceProxy;

	private MultiTouchController<Object> mMultiTouchController;
	private float mMultiTouchScale = 1.0f;

	protected MapListener mListener;

	// for speed (avoiding allocations)
	private Matrix mMatrix = new Matrix();
	private BoundingBoxE6 mBoundingBox = new BoundingBoxE6(0,0,0,0);
	private int[] mIntArray = new int[2];

	// ===========================================================
	// Constructors
	// ===========================================================

	private OpenStreetMapView(
			final Context context,
			final AttributeSet attrs,
			final IOpenStreetMapRendererInfo rendererInfo,
			OpenStreetMapTileProvider tileProvider) {
		super(context, attrs);
		mResourceProxy = new DefaultResourceProxyImpl(context);
		this.mController = new OpenStreetMapViewController(this);
		this.mScroller = new Scroller(context);

		if(tileProvider == null) {
			final Context applicationContext = context.getApplicationContext();
			final String cloudmadeKey = getCloudmadeKey(applicationContext);
			final IRegisterReceiver registerReceiver = new IRegisterReceiver() {
				@Override
				public Intent registerReceiver(final BroadcastReceiver aReceiver, final IntentFilter aFilter) {
					return applicationContext.registerReceiver(aReceiver, aFilter);
				}
				@Override
				public void unregisterReceiver(final BroadcastReceiver aReceiver) {
					applicationContext.unregisterReceiver(aReceiver);
				}
			};
			tileProvider = new OpenStreetMapTileProviderDirect(new SimpleInvalidationHandler(), cloudmadeKey, registerReceiver);
		}

		this.mMapOverlay = new OpenStreetMapTilesOverlay(this, OpenStreetMapRendererFactory.getRenderer(rendererInfo, attrs), tileProvider, mResourceProxy);
		mOverlays.add(this.mMapOverlay);
		this.mZoomController = new ZoomButtonsController(this);
		this.mZoomController.setOnZoomListener(new OpenStreetMapViewZoomListener());

		mZoomInAnimation = new ScaleAnimation(1, 2, 1, 2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mZoomOutAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mZoomInAnimation.setDuration(ANIMATION_DURATION_SHORT);
		mZoomOutAnimation.setDuration(ANIMATION_DURATION_SHORT);
		mZoomInAnimation.setAnimationListener(mAnimationListener);
		mZoomOutAnimation.setAnimationListener(mAnimationListener);

		mGestureDetector = new GestureDetector(context, new OpenStreetMapViewGestureDetectorListener());
		mGestureDetector.setOnDoubleTapListener(new OpenStreetMapViewDoubleClickListener());
	}

	public void detach() {
		mMapOverlay.detach();
	}

	/**
	 * Constructor used by XML layout resource (uses default renderer).
	 */
	public OpenStreetMapView(Context context, AttributeSet attrs) {
		this(context, attrs, null, null);
	}

	/**
	 * Standard Constructor.
	 */
	public OpenStreetMapView(final Context context, final IOpenStreetMapRendererInfo aRendererInfo) {
		this(context, null, aRendererInfo, null);
	}

	/**
	 * Standard Constructor (uses default renderer).
	 */
	public OpenStreetMapView(final Context context) {
		this(context, null, null, null);
	}

	/**
	 * Standard Constructor.
	 */
	public OpenStreetMapView(
			final Context context,
			final IOpenStreetMapRendererInfo aRendererInfo,
			final OpenStreetMapTileProvider aTileProvider) {
		this(context, null, aRendererInfo, aTileProvider);
	}

	/**
	 *
	 * @param context
	 * @param aRendererInfo
	 *            pass a {@link IOpenStreetMapRendererInfo} you like.
	 * @param osmv
	 *            another {@link OpenStreetMapView}, to share the TileProvider
	 *            with.<br/>
	 *            May significantly improve the render speed, when using the
	 *            same {@link IOpenStreetMapRendererInfo}.
	 */
	public OpenStreetMapView(final Context context,
			final IOpenStreetMapRendererInfo aRendererInfo,
			final OpenStreetMapView aMapToShareTheTileProviderWith) {
		this(context, null, aRendererInfo, /* TODO aMapToShareTheTileProviderWith.mTileProvider */ null);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * This MapView takes control of the {@link OpenStreetMapView} passed as
	 * parameter.<br />
	 * I.e. it zooms it to x levels less than itself and centers it the same
	 * coords.<br />
	 * Its pretty useful when the MiniMap uses the same TileProvider.
	 *
	 * @see OpenStreetMapView.OpenStreetMapView(
	 * @param aOsmvMinimap
	 * @param aZoomDiff
	 *            3 is a good Value. Pass {@link OpenStreetMapViewConstants}
	 *            .NOT_SET to disable autozooming of the minimap.
	 */
	public void setMiniMap(final OpenStreetMapView aOsmvMinimap, final int aZoomDiff) {
		this.mMiniMapZoomDiff = aZoomDiff;
		this.mMiniMap = aOsmvMinimap;
		aOsmvMinimap.setMaxiMap(this);

		// make sure that the zoom level of the minimap is set correctly. this is done when
		// setting the zoom level of the main map
		this.setZoomLevel(this.getZoomLevel());

		// Set identical map renderer
		this.mMiniMap.setRenderer(this.getRenderer());

		// Note the "false" parameter at the end - do NOT pass it further to other maps here
		// this.mMiniMap.setMapCenter(this.getMapCenterLatitudeE6(), this.getMapCenterLongitudeE6(), false);
		this.mMiniMap.getController().setCenter(this.getMapCenter());
	}

	public boolean hasMiniMap() {
		return this.mMiniMap != null;
	}

	/**
	 * @return {@link View}.GONE or {@link View}.VISIBLE or {@link View}
	 *         .INVISIBLE or {@link OpenStreetMapViewConstants}.NOT_SET
	 * */
	public int getOverrideMiniMapVisiblity() {
		return this.mMiniMapOverriddenVisibility;
	}

	/**
	 * Use this method if you want to make the MiniMap visible i.e.: always or
	 * never. Use {@link View}.GONE , {@link View}.VISIBLE, {@link View}
	 * .INVISIBLE. Use {@link OpenStreetMapViewConstants}.NOT_SET to reset this
	 * feature.
	 *
	 * @param aVisiblity
	 */
	public void setOverrideMiniMapVisiblity(final int aVisiblity) {
		switch (aVisiblity) {
			case View.GONE:
			case View.VISIBLE:
			case View.INVISIBLE:
				if (this.mMiniMap != null)
					this.mMiniMap.setVisibility(aVisiblity);
			case NOT_SET:
				this.setZoomLevel(this.mZoomLevel);
				break;
			default:
				throw new IllegalArgumentException("See javadoc of this method !!!");
		}
		this.mMiniMapOverriddenVisibility = aVisiblity;
	}

	private void setMaxiMap(final OpenStreetMapView aOsmvMaxiMap) {
		this.mMaxiMap = aOsmvMaxiMap;
	}

	public OpenStreetMapViewController getController() {
		return this.mController;
	}

	/**
	 * You can add/remove/reorder your Overlays using the List of
	 * {@link OpenStreetMapViewOverlay}. The first (index 0) Overlay gets drawn
	 * first, the one with the highest as the last one.
	 */
	public List<OpenStreetMapViewOverlay> getOverlays() {
		return this.mOverlays;
	}

	public Scroller getScroller() {
		return mScroller;
	}

	public double getLatitudeSpan() {
		return this.getDrawnBoundingBoxE6().getLongitudeSpanE6() / 1E6;
	}

	public int getLatitudeSpanE6() {
		return this.getDrawnBoundingBoxE6().getLatitudeSpanE6();
	}

	public double getLongitudeSpan() {
		return this.getDrawnBoundingBoxE6().getLongitudeSpanE6() / 1E6;
	}

	public int getLongitudeSpanE6() {
		return this.getDrawnBoundingBoxE6().getLatitudeSpanE6();
	}

	public BoundingBoxE6 getDrawnBoundingBoxE6() {
		return getBoundingBox(this.getWidth(), this.getHeight());
	}

	public BoundingBoxE6 getVisibleBoundingBoxE6() {
		return getBoundingBox(this.getWidth(), this.getHeight());
	}

	private BoundingBoxE6 getBoundingBox(final int pViewWidth, final int pViewHeight){
		final int mapTileZoom = mMapOverlay.getRendererInfo().maptileZoom();
		final int world_2 = (1 << mZoomLevel + mapTileZoom - 1);
		final int north = world_2 + getScrollY() - getHeight()/2;
		final int south = world_2 + getScrollY() + getHeight()/2;
		final int west = world_2 + getScrollX() - getWidth()/2;
		final int east = world_2 + getScrollX() + getWidth()/2;

		return Mercator.getBoundingBoxFromCoords(west, north, east, south, mZoomLevel + mapTileZoom);
	}

	/**
	 * This class is only meant to be used during on call of onDraw(). Otherwise
	 * it may produce strange results.
	 *
	 * @return
	 */
	public OpenStreetMapViewProjection getProjection() {
		return mProjection;
	}

	void setMapCenter(final GeoPoint aCenter) {
		this.setMapCenter(aCenter.getLatitudeE6(), aCenter.getLongitudeE6());
	}

	void setMapCenter(final int aLatitudeE6, final int aLongitudeE6) {
		this.setMapCenter(aLatitudeE6, aLongitudeE6, true);
	}

	void setMapCenter(final int aLatitudeE6, final int aLongitudeE6,
			final boolean doPassFurther) {
		if (doPassFurther && this.mMiniMap != null)
			this.mMiniMap.setMapCenter(aLatitudeE6, aLongitudeE6, false);
		else if (doPassFurther && this.mMaxiMap != null)
			this.mMaxiMap.setMapCenter(aLatitudeE6, aLongitudeE6, false);

		final int[] coords = Mercator.projectGeoPoint(aLatitudeE6, aLongitudeE6, getPixelZoomLevel(), null);
		final int worldSize_2 = getWorldSizePx()/2;
		if (getAnimation() == null || getAnimation().hasEnded()) {
			logger.debug("StartScroll");
			mScroller.startScroll(getScrollX(), getScrollY(),
					coords[MAPTILE_LONGITUDE_INDEX] - worldSize_2 - getScrollX(),
					coords[MAPTILE_LATITUDE_INDEX] - worldSize_2 - getScrollY(), 500);
			postInvalidate();
		}
	}

	public IOpenStreetMapRendererInfo getRenderer() {
		return this.mMapOverlay.getRendererInfo();
	}

	public void setRenderer(final IOpenStreetMapRendererInfo aRenderer) {
		this.mMapOverlay.setRendererInfo(aRenderer);
		if (this.mMiniMap != null)
			this.mMiniMap.setRenderer(aRenderer);
		this.checkZoomButtons();
		this.setZoomLevel(mZoomLevel); // revalidate zoom level
		postInvalidate();
	}

	/**
	 * @param aZoomLevel
	 *            between 0 (equator) and 18/19(closest), depending on the
	 *            Renderer chosen.
	 */
	int setZoomLevel(final int aZoomLevel) {
		final int minZoomLevel = this.mMapOverlay.getRendererInfo().zoomMinLevel();
		final int maxZoomLevel = this.mMapOverlay.getRendererInfo().zoomMaxLevel();
		final int newZoomLevel = Math.max(minZoomLevel, Math.min(maxZoomLevel, aZoomLevel));
		final int curZoomLevel = this.mZoomLevel;

		if (this.mMiniMap != null) {
			if (this.mZoomLevel < this.mMiniMapZoomDiff) {
				if (this.mMiniMapOverriddenVisibility == NOT_SET)
					this.mMiniMap.setVisibility(View.INVISIBLE);
			} else {
				if (this.mMiniMapOverriddenVisibility == NOT_SET
						&& this.mMiniMap.getVisibility() != View.VISIBLE) {
					this.mMiniMap.setVisibility(View.VISIBLE);
				}
				if (this.mMiniMapZoomDiff != NOT_SET)
					this.mMiniMap.setZoomLevel(this.mZoomLevel - this.mMiniMapZoomDiff);
			}
		}

		this.mZoomLevel = newZoomLevel;
		this.checkZoomButtons();

		if(newZoomLevel > curZoomLevel)
			scrollTo(getScrollX()<<(newZoomLevel-curZoomLevel), getScrollY()<<(newZoomLevel-curZoomLevel));
		else if(newZoomLevel < curZoomLevel)
			scrollTo(getScrollX()>>(curZoomLevel-newZoomLevel), getScrollY()>>(curZoomLevel-newZoomLevel));

		// snap for all snappables
		final Point snapPoint = new Point();
		mProjection = new OpenStreetMapViewProjection(); // XXX why do we need a new projection here?
		for (OpenStreetMapViewOverlay osmvo : this.mOverlays) {
			if (osmvo instanceof Snappable &&
					((Snappable)osmvo).onSnapToItem(getScrollX(), getScrollY(), snapPoint, this)) {
				scrollTo(snapPoint.x, snapPoint.y);
			}
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
	 * @return the current ZoomLevel between 0 (equator) and 18/19(closest),
	 *         depending on the Renderer chosen.
	 */
	public int getZoomLevel() {
		return getZoomLevel(true);
	}

	/**
	 * Get the current ZoomLevel for the map tiles.
	 * @param aPending if true and we're animating then return the zoom level
	 *        that we're animating towards, otherwise return the current
	 *        zoom level
	 * @return the current ZoomLevel between 0 (equator) and 18/19(closest),
	 *         depending on the Renderer chosen.
	 */
	public int getZoomLevel(final boolean aPending) {
		if (aPending && mAnimationListener.animating) {
			return mAnimationListener.targetZoomLevel;
		} else {
			return mZoomLevel;
		}
	}

	/*
	 * Returns the maximum zoom level for the point currently at the center.
	 * @return The maximum zoom level for the map's current center.
	 */
	public int getMaxZoomLevel() {
		return getRenderer().zoomMaxLevel();
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
		if (mZoomLevel <= 0) {
			return false;
		}
		if (mAnimationListener.animating && mAnimationListener.targetZoomLevel <= 0) {
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

	public GeoPoint getMapCenter() {
		return new GeoPoint(getMapCenterLatitudeE6(), getMapCenterLongitudeE6());
	}

	public int getMapCenterLatitudeE6() {
		return (int)(Mercator.tile2lat(getScrollY() + getWorldSizePx()/2, getPixelZoomLevel()) * 1E6);
	}

	public int getMapCenterLongitudeE6() {
		return (int)(Mercator.tile2lon(getScrollX() + getWorldSizePx()/2, getPixelZoomLevel()) * 1E6);
	}

	public void setResourceProxy(final ResourceProxy pResourceProxy) {
		mResourceProxy = pResourceProxy;
	}

	public void onSaveInstanceState(Bundle state) {
    	state.putString(BUNDLE_RENDERER, getRenderer().name());
    	state.putInt(BUNDLE_SCROLL_X, getScrollX());
    	state.putInt(BUNDLE_SCROLL_Y, getScrollY());
    	state.putInt(BUNDLE_ZOOM_LEVEL, getZoomLevel());
	}

	public void onRestoreInstanceState(Bundle state) {

		final String rendererName = state.containsKey(BUNDLE_RENDERER) ?
				state.getString(BUNDLE_RENDERER) :
					OpenStreetMapRendererFactory.DEFAULT_RENDERER.name();
		final IOpenStreetMapRendererInfo renderer = OpenStreetMapRendererFactory.getRenderer(rendererName);
		setRenderer(renderer);

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
	 * @param aMode
	 * if true use the network connection if it's available.
	 * if false don't use the network connection even if it's available.
	 */
	public void setUseDataConnection(boolean aMode) {
		mMapOverlay.setUseDataConnection(aMode);
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	public void onLongPress(MotionEvent e) {
		for (OpenStreetMapViewOverlay osmvo : this.mOverlays)
			if (osmvo.onLongPress(e, this))
				return;
	}

	public boolean onSingleTapUp(MotionEvent e) {
		for (OpenStreetMapViewOverlay osmvo : this.mOverlays)
			if (osmvo.onSingleTapUp(e, this)) {
				postInvalidate();
				return true;
			}

		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		for (OpenStreetMapViewOverlay osmvo : this.mOverlays)
			if (osmvo.onKeyDown(keyCode, event, this))
				return true;

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		for (OpenStreetMapViewOverlay osmvo : this.mOverlays)
			if (osmvo.onKeyUp(keyCode, event, this))
				return true;

		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		for (OpenStreetMapViewOverlay osmvo : this.mOverlays)
			if (osmvo.onTrackballEvent(event, this))
				return true;

		scrollBy((int)(event.getX() * 25), (int)(event.getY() * 25));

		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {

		if (DEBUGMODE)
			logger.debug("onTouchEvent(" + event + ")");

		for (OpenStreetMapViewOverlay osmvo : this.mOverlays)
			if (osmvo.onTouchEvent(event, this))
			{
				if (DEBUGMODE)
					logger.debug("overlay handled onTouchEvent");
				return true;
			}

		if (mMultiTouchController != null && mMultiTouchController.onTouchEvent(event)) {
			if (DEBUGMODE)
				logger.debug("mMultiTouchController handled onTouchEvent");
			return true;
		}

		if (mGestureDetector.onTouchEvent(event)) {
			if (DEBUGMODE)
				logger.debug("mGestureDetector handled onTouchEvent");
			return true;
		}

		boolean r = super.onTouchEvent(event);
		if (r) {
			if (DEBUGMODE)
				logger.debug("super handled onTouchEvent");
		} else {
			if (DEBUGMODE)
				logger.debug("no-one handled onTouchEvent");
		}
		return r;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScroller.isFinished())
				setZoomLevel(mZoomLevel);
			else
				scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();	// Keep on drawing until the animation has finished.
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

		mProjection = new OpenStreetMapViewProjection();

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
//		This is to slow:
//		final Rect r = c.getClipBounds();
//		mPaint.setColor(Color.GRAY);
//		mPaint.setPathEffect(new DashPathEffect(new float[] {1, 1}, 0));
//		for (int x = r.left; x < r.right; x += 20)
//			c.drawLine(x, r.top, x, r.bottom, mPaint);
//		for (int y = r.top; y < r.bottom; y += 20)
//			c.drawLine(r.left, y, r.right, y, mPaint);

		/* Draw all Overlays. Avoid allocation by not doing enhanced loop. */
		for (int i = 0; i < mOverlays.size(); i++) {
			mOverlays.get(i).onManagedDraw(c, this);
		}

		if (this.mMaxiMap != null) { // If this is a MiniMap
			this.mPaint.setColor(Color.RED);
			this.mPaint.setStyle(Style.STROKE);
			final int viewWidth = this.getWidth();
			final int viewHeight = this.getHeight();
			c.drawRect(0, 0, viewWidth, viewHeight, this.mPaint);
		}

		final long endMs = System.currentTimeMillis();
		if (DEBUGMODE)
			logger.debug("Rendering overall: " + (endMs - startMs) + "ms");
	}

	@Override
	protected void onDetachedFromWindow() {
		this.mZoomController.setVisible(false);
		this.mMapOverlay.detach();
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
			float scaleDiffFloat = (float) (Math.log(mMultiTouchScale) * ZOOM_LOG_BASE_INV);
			int scaleDiffInt = (int) Math.round(scaleDiffFloat);
			setZoomLevel(mZoomLevel + scaleDiffInt);
			// XXX maybe zoom in/out instead of zooming direct to zoom level
			//     - probably not a good idea because you'll repeat the animation
		}

		// reset scale
		mMultiTouchScale = 1.0f;
	}

	@Override
	public boolean setPositionAndScale(final Object obj, final PositionAndScale aNewObjPosAndScale, final PointInfo aTouchPoint) {
		mMultiTouchScale = aNewObjPosAndScale.getScale();
		invalidate(); // redraw
		return true;
	}

	/*
	 * Set the MapListener for this view
	 */
	public void setMapListener(MapListener ml) {
		mListener = ml;
	}

	// ===========================================================
	// Package Methods
	// ===========================================================

	/**
	 * Get the world size in pixels.
	 */
	int getWorldSizePx() {
		return (1 << getPixelZoomLevel());
	}

	/**
	 * Get the equivalent zoom level on pixel scale
	 */
	int getPixelZoomLevel() {
		return this.mZoomLevel + this.mMapOverlay.getRendererInfo().maptileZoom();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// NB: this method will be called even if we don't use Cloudmade
	//     because we only have the context in the constructor
	//     the alternative would be to only get it when needed,
	//     but that would mean keeping a handle on the context
	private String getCloudmadeKey(final Context aContext) {
		return CloudmadeUtil.getCloudmadeKey(aContext);
	}

	private void checkZoomButtons() {
		this.mZoomController.setZoomInEnabled(canZoomIn());
		this.mZoomController.setZoomOutEnabled(canZoomOut());
	}

	private int[] getCenterMapTileCoords() {
		final int mapTileZoom = this.mMapOverlay.getRendererInfo().maptileZoom();
		final int worldTiles_2 = 1 << (mZoomLevel-1);
		// convert to tile coordinate and make positive
		return new int[] {  (getScrollY() >> mapTileZoom) + worldTiles_2,
							(getScrollX() >> mapTileZoom) + worldTiles_2 };
	}

	/**
	 * @param centerMapTileCoords
	 * @param tileSizePx
	 * @param reuse
	 *            just pass null if you do not have a Point to be 'recycled'.
	 */
	private Point getUpperLeftCornerOfCenterMapTileInScreen(final int[] centerMapTileCoords,
			final int tileSizePx, final Point reuse) {
		final Point out = (reuse != null) ? reuse : new Point();

		final int worldTiles_2 = 1 << (mZoomLevel-1);
		final int centerMapTileScreenLeft = (centerMapTileCoords[MAPTILE_LONGITUDE_INDEX] - worldTiles_2) * tileSizePx - tileSizePx/2;
		final int centerMapTileScreenTop = (centerMapTileCoords[MAPTILE_LATITUDE_INDEX] - worldTiles_2) * tileSizePx - tileSizePx/2;

		out.set(centerMapTileScreenLeft, centerMapTileScreenTop);
		return out;
	}

	public void setBuiltInZoomControls(boolean on) {
		this.mEnableZoomController = on;
		this.checkZoomButtons();
	}

	public void setMultiTouchControls(boolean on) {
		mMultiTouchController = on ? new MultiTouchController<Object>(this, false) : null;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	/**
	 * This class may return valid results until the underlying
	 * {@link OpenStreetMapView} gets modified in any way (i.e. new center).
	 *
	 * @author Nicolas Gramlich
	 * @author Manuel Stahl
	 */
	public class OpenStreetMapViewProjection {

		private final int viewWidth_2 = getWidth() / 2;
		private final int viewHeight_2 = getHeight() / 2;
		private final int worldSize_2 = getWorldSizePx()/2;
		private final int offsetX = - worldSize_2;
		private final int offsetY = - worldSize_2;

		private final BoundingBoxE6 bb;
		private final int zoomLevel;
		private final int tileSizePx;
		private final int[] centerMapTileCoords;
		private final Point upperLeftCornerOfCenterMapTile;

		private final int[] reuseInt2 = new int[2];

		private OpenStreetMapViewProjection() {

			/*
			 * Do some calculations and drag attributes to local variables to
			 * save some performance.
			 */
			zoomLevel = OpenStreetMapView.this.mZoomLevel; // TODO Draw to
															// attributes and so
															// make it only
															// 'valid' for a
															// short time.
			tileSizePx = getRenderer().maptileSizePx();

			/*
			 * Get the center MapTile which is above this.mLatitudeE6 and
			 * this.mLongitudeE6 .
			 */
			centerMapTileCoords = getCenterMapTileCoords();
			upperLeftCornerOfCenterMapTile = getUpperLeftCornerOfCenterMapTileInScreen(
					centerMapTileCoords, tileSizePx, null);

			bb = OpenStreetMapView.this.getDrawnBoundingBoxE6();
		}

		/**
		 * Converts x/y ScreenCoordinates to the underlying GeoPoint.
		 *
		 * @param x
		 * @param y
		 * @return GeoPoint under x/y.
		 */
		public GeoPoint fromPixels(float x, float y) {
			return bb.getGeoPointOfRelativePositionWithLinearInterpolation(x / getWidth(), y / getHeight());
		}

		public Point fromMapPixels(int x, int y, Point reuse) {
			final Point out = (reuse != null) ? reuse : new Point();
			out.set(x - viewWidth_2, y - viewHeight_2);
			out.offset(getScrollX(), getScrollY());
			return out;
		}

		private static final int EQUATORCIRCUMFENCE = 40075004;

		public float metersToEquatorPixels(final float aMeters) {
			return aMeters / EQUATORCIRCUMFENCE * getWorldSizePx();
		}

		/**
		 * Converts a GeoPoint to its ScreenCoordinates. <br/>
		 * <br/>
		 * <b>CAUTION</b> ! Conversion currently has a large error on
		 * <code>zoomLevels <= 7</code>.<br/>
		 * The Error on ZoomLevels higher than 7, the error is below
		 * <code>1px</code>.<br/>
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
		 *            just pass null if you do not have a Point to be
		 *            'recycled'.
		 * @return the Point containing the approximated ScreenCoordinates of
		 *         the GeoPoint passed.
		 */
		public Point toMapPixels(final GeoPoint in, final Point reuse) {
			final Point out = (reuse != null) ? reuse : new Point();

			final int[] coords = Mercator.projectGeoPoint(in.getLatitudeE6(), in.getLongitudeE6(), getPixelZoomLevel(), null);
			out.set(coords[MAPTILE_LONGITUDE_INDEX], coords[MAPTILE_LATITUDE_INDEX]);
			out.offset(offsetX, offsetY);
			return out;
		}

		/**
		 * Performs only the first computationally heavy part of the projection, needToCall toMapPixelsTranslated to get final position.
		 * @param latituteE6
		 * 			 the latitute of the point
		 * @param longitudeE6
		 * 			 the longitude of the point
		 * @param reuse
		 *            just pass null if you do not have a Point to be
		 *            'recycled'.
		 * @return intermediate value to be stored and passed to toMapPixelsTranslated on paint.
		 */
		public Point toMapPixelsProjected(final int latituteE6, final int longitudeE6, final Point reuse) {
			final Point out = (reuse != null) ? reuse : new Point();

			//26 is the biggest zoomlevel we can project
			final int[] coords = Mercator.projectGeoPoint(latituteE6, longitudeE6, 28, this.reuseInt2);
			out.set(coords[MAPTILE_LONGITUDE_INDEX], coords[MAPTILE_LATITUDE_INDEX]);
			return out;
		}

		/**
		 * Performs the second computationally light part of the projection.
		 * @param in
		 * 			 the Point calculated by the toMapPixelsProjected
		 * @param reuse
		 *            just pass null if you do not have a Point to be
		 *            'recycled'.
		 * @return the Point containing the approximated ScreenCoordinates of
		 *         the initial GeoPoint passed to the toMapPixelsProjected.
		 */
		public Point toMapPixelsTranslated(final Point in, final Point reuse) {
			final Point out = (reuse != null) ? reuse : new Point();

			//26 is the biggest zoomlevel we can project
			int zoomDifference = 28 - getPixelZoomLevel();
			out.set((in.x >> zoomDifference) + offsetX , (in.y >> zoomDifference) + offsetY );
			return out;
		}


		/**
		 * Translates a rectangle from screen coordinates to intermediate coordinates.
		 * @param in the rectangle in screen coordinates
		 * @return a rectangle in intermediate coords.
		 */
		public Rect fromPixelsToProjected(final Rect in)
		{
			Rect result = new Rect();

			//26 is the biggest zoomlevel we can project
			int zoomDifference = 28 - getPixelZoomLevel();

			int x0 = (in.left - offsetX) << zoomDifference;
			int x1 = (in.right - offsetX) << zoomDifference;
			int y0 = (in.bottom - offsetX) << zoomDifference;
			int y1 = (in.top - offsetX) << zoomDifference;

			result.set(Math.min(x0,x1), Math.min(y0,y1), Math.max(x0,x1), Math.max(y0,y1));
			return result;
		}

		public Point toPixels(final int[] tileCoords, final Point reuse) {
			return toPixels(tileCoords[MAPTILE_LONGITUDE_INDEX], tileCoords[MAPTILE_LATITUDE_INDEX], reuse);
		}

		public Point toPixels(int tileX, int tileY, final Point reuse) {
			final Point out = (reuse != null) ? reuse : new Point();

			out.set(tileX * tileSizePx, tileY * tileSizePx);
			out.offset(offsetX, offsetY);

			return out;
		}

		public Path toPixels(final List<? extends GeoPoint> in, final Path reuse) {
			return toPixels(in, reuse, true);
		}

		protected Path toPixels(final List<? extends GeoPoint> in, final Path reuse, final boolean doGudermann)
				throws IllegalArgumentException {
			if (in.size() < 2)
				throw new IllegalArgumentException("List of GeoPoints needs to be at least 2.");

			final Path out = (reuse != null) ? reuse : new Path();
			out.incReserve(in.size());

			boolean first = true;
			for (GeoPoint gp : in) {
				final int[] underGeopointTileCoords = Mercator.projectGeoPoint(gp
						.getLatitudeE6(), gp.getLongitudeE6(), zoomLevel, null);

				/*
				 * Calculate the Latitude/Longitude on the left-upper
				 * ScreenCoords of the MapTile.
				 */
				final BoundingBoxE6 bb = Mercator.getBoundingBoxFromMapTile(underGeopointTileCoords,
						zoomLevel);

				final float[] relativePositionInCenterMapTile;
				if (doGudermann && zoomLevel < 7)
					relativePositionInCenterMapTile = bb
							.getRelativePositionOfGeoPointInBoundingBoxWithExactGudermannInterpolation(
									gp.getLatitudeE6(), gp.getLongitudeE6(), null);
				else
					relativePositionInCenterMapTile = bb
							.getRelativePositionOfGeoPointInBoundingBoxWithLinearInterpolation(gp
									.getLatitudeE6(), gp.getLongitudeE6(), null);

				final int tileDiffX = centerMapTileCoords[MAPTILE_LONGITUDE_INDEX]
						- underGeopointTileCoords[MAPTILE_LONGITUDE_INDEX];
				final int tileDiffY = centerMapTileCoords[MAPTILE_LATITUDE_INDEX]
						- underGeopointTileCoords[MAPTILE_LATITUDE_INDEX];
				final int underGeopointTileScreenLeft = upperLeftCornerOfCenterMapTile.x
						- (tileSizePx * tileDiffX);
				final int underGeopointTileScreenTop = upperLeftCornerOfCenterMapTile.y
						- (tileSizePx * tileDiffY);

				final int x = underGeopointTileScreenLeft
						+ (int) (relativePositionInCenterMapTile[MAPTILE_LONGITUDE_INDEX] * tileSizePx);
				final int y = underGeopointTileScreenTop
						+ (int) (relativePositionInCenterMapTile[MAPTILE_LATITUDE_INDEX] * tileSizePx);

				/* Add up the offset caused by touch. */
				if (first)
					out.moveTo(x, y);
//				out.moveTo(x + OpenStreetMapView.this.mTouchMapOffsetX, y
//						+ OpenStreetMapView.this.mTouchMapOffsetY);
				else
					out.lineTo(x, y);
				first = false;
			}

			return out;
		}
	}

	private class OpenStreetMapViewGestureDetectorListener implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			mZoomController.setVisible(mEnableZoomController);
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			final int worldSize = getWorldSizePx();
			mScroller.fling(getScrollX(), getScrollY(), (int)-velocityX, (int)-velocityY, -worldSize, worldSize, -worldSize, worldSize);
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			OpenStreetMapView.this.onLongPress(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			scrollBy((int)distanceX, (int)distanceY);
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return OpenStreetMapView.this.onSingleTapUp(e);
		}

	}

	private class OpenStreetMapViewDoubleClickListener implements GestureDetector.OnDoubleTapListener {
		@Override
		public boolean onDoubleTap(final MotionEvent e) {
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

	private class OpenStreetMapViewZoomListener implements OnZoomListener {
		@Override
		public void onZoom(boolean zoomIn) {
			if(zoomIn)
				getController().zoomIn();
			else
				getController().zoomOut();
		}
		@Override
		public void onVisibilityChanged(boolean visible) {}
	}

	private class MyAnimationListener implements AnimationListener {
		private int targetZoomLevel;
		private boolean animating;

		@Override
		public void onAnimationEnd(Animation aAnimation) {
			animating = false;
			setZoomLevel(targetZoomLevel);
		}

		@Override
		public void onAnimationRepeat(Animation aAnimation) {
		}

		@Override
		public void onAnimationStart(Animation aAnimation) {
			animating = true;
		}

	}

	private class SimpleInvalidationHandler extends Handler {

		@Override
		public void handleMessage(final Message msg) {
			switch (msg.what) {
				case OpenStreetMapTile.MAPTILE_SUCCESS_ID:
					invalidate();
					break;
			}
		}
	}
}
