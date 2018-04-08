// Created by plusminus on 20:32:01 - 27.09.2008
package org.andnav.osm.views.overlay;

import org.andnav.osm.DefaultResourceProxyImpl;
import org.andnav.osm.ResourceProxy;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.util.constants.OpenStreetMapViewConstants;

import android.content.Context;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Base class representing an overlay which may be displayed on top of a
 * {@link OpenStreetMapView}. To add an overlay, subclass this class, create an
 * instance, and add it to the list obtained from getOverlays() of
 * {@link OpenStreetMapView}.
 * 
 * @author Nicolas Gramlich
 */
public abstract class OpenStreetMapViewOverlay implements OpenStreetMapViewConstants {
	
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final ResourceProxy mResourceProxy;

	// ===========================================================
	// Constructors
	// ===========================================================

	public OpenStreetMapViewOverlay(final Context ctx) {
		mResourceProxy = new DefaultResourceProxyImpl(ctx);
	}
	
	public OpenStreetMapViewOverlay(final ResourceProxy pResourceProxy) {
		mResourceProxy = pResourceProxy;
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for SuperClass/Interfaces
	// ===========================================================

	/**
	 * Managed Draw calls gives Overlays the possibility to first draw manually
	 * and after that do a final draw. This is very useful, i sth. to be drawn
	 * needs to be <b>topmost</b>.
	 */
	public void onManagedDraw(final Canvas c, final OpenStreetMapView osmv) {
		onDraw(c, osmv);
		onDrawFinished(c, osmv);
	}

	protected abstract void onDraw(final Canvas c, final OpenStreetMapView osmv);

	protected abstract void onDrawFinished(final Canvas c,
			final OpenStreetMapView osmv);

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * By default does nothing (<code>return false</code>). If you handled the
	 * Event, return <code>true</code>, otherwise return <code>false</code>. If
	 * you returned <code>true</code> none of the following Overlays or the
	 * underlying {@link OpenStreetMapView} has the chance to handle this event.
	 */
	public boolean onKeyDown(final int keyCode, KeyEvent event,
			final OpenStreetMapView mapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the
	 * Event, return <code>true</code>, otherwise return <code>false</code>. If
	 * you returned <code>true</code> none of the following Overlays or the
	 * underlying {@link OpenStreetMapView} has the chance to handle this event.
	 */
	public boolean onKeyUp(final int keyCode, KeyEvent event,
			final OpenStreetMapView mapView) {
		return false;
	}

	/**
	 * <b>You can prevent all(!) other Touch-related events from happening!</b><br />
	 * By default does nothing (<code>return false</code>). If you handled the
	 * Event, return <code>true</code>, otherwise return <code>false</code>. If
	 * you returned <code>true</code> none of the following Overlays or the
	 * underlying {@link OpenStreetMapView} has the chance to handle this event.
	 */
	public boolean onTouchEvent(final MotionEvent event,
			final OpenStreetMapView mapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the
	 * Event, return <code>true</code>, otherwise return <code>false</code>. If
	 * you returned <code>true</code> none of the following Overlays or the
	 * underlying {@link OpenStreetMapView} has the chance to handle this event.
	 */
	public boolean onTrackballEvent(final MotionEvent event,
			final OpenStreetMapView mapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the
	 * Event, return <code>true</code>, otherwise return <code>false</code>. If
	 * you returned <code>true</code> none of the following Overlays or the
	 * underlying {@link OpenStreetMapView} has the chance to handle this event.
	 */
	public boolean onSingleTapUp(MotionEvent e,
			OpenStreetMapView openStreetMapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the
	 * Event, return <code>true</code>, otherwise return <code>false</code>. If
	 * you returned <code>true</code> none of the following Overlays or the
	 * underlying {@link OpenStreetMapView} has the chance to handle this event.
	 */
	public boolean onLongPress(MotionEvent e,
			OpenStreetMapView openStreetMapView) {
		return false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	/**
	 * Interface definition for overlays that contain items that can be snapped
	 * to (for example, when the user invokes a zoom, this could be called
	 * allowing the user to snap the zoom to an interesting point.)
	 */
	public interface Snappable {
		
		/**
		 * Checks to see if the given x and y are close enough to an item resulting in snapping the current action (e.g. zoom) to the item. 
		 * @param x The x in screen coordinates.
		 * @param y The y in screen coordinates.
		 * @param snapPoint To be filled with the the interesting point (in screen coordinates) that is closest to the given x and y. Can be untouched if not snapping.
		 * @param mapView The OpenStreetMapView that is requesting the snap. Use MapView.getProjection() to convert between on-screen pixels and latitude/longitude pairs. 
		 * @return Whether or not to snap to the interesting point.
		 */
		boolean onSnapToItem(int x, int y, android.graphics.Point snapPoint,
				OpenStreetMapView mapView);
	}

}
