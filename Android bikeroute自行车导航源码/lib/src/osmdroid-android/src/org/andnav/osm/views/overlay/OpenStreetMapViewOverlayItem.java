// Created by plusminus on 00:02:58 - 03.10.2008
package org.andnav.osm.views.overlay;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.util.GeoPoint;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * Immutable class describing a GeoPoint with a Title and a Description.
 * @author Nicolas Gramlich
 * @author Theodore Hong
 * @author Fred Eisele
 *
 */
public class OpenStreetMapViewOverlayItem {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final int	ITEM_STATE_FOCUSED_MASK	= 4;
	public static final int	ITEM_STATE_PRESSED_MASK	= 1;
	public static final int	ITEM_STATE_SELECTED_MASK = 2;
	
	protected static final Point DEFAULT_MARKER_SIZE = new Point(26, 94);
	
	public enum HotspotPlace {
		CUSTOM, // indicates the mMarker is set by the user
		CENTER, 
		BOTTOM_CENTER, // default
		TOP_CENTER,
		RIGHT_CENTER,
		LEFT_CENTER,
		UPPER_RIGHT_CORNER,
		LOWER_RIGHT_CORNER,
		UPPER_LEFT_CORNER,
		LOWER_LEFT_CORNER
	}
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	public final String mTitle;
	public final String mDescription;
	public final GeoPoint mGeoPoint;
	protected Drawable mMarker;
	protected Point mMarkerHotspot;
	protected HotspotPlace mStdHotspotPlace;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	/**
	 * @param aTitle this should be <b>singleLine</b> (no <code>'\n'</code> )
	 * @param aDescription a <b>multiLine</b> description ( <code>'\n'</code> possible)
	 * @param aGeoPoint
	 */
	public OpenStreetMapViewOverlayItem(final String aTitle, final String aDescription, final GeoPoint aGeoPoint) {
		this.mTitle = aTitle;
		this.mDescription = aDescription;
		this.mGeoPoint = aGeoPoint;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public String getTitle() {
		return mTitle;
	}

	public String getSnippet() {
		return mDescription;
	}

	public GeoPoint getPoint() {
		return mGeoPoint;
	}

	/*
	 * (copied from Google API docs)
	 * Returns the marker that should be used when drawing this item on the map.
	 * A null value means that the default marker should be drawn. Different markers
	 * can be returned for different states. The different markers can have different
	 * bounds. The default behavior is to call
	 * {@link setState(android.graphics.drawable.Drawable, int)} on the overlay item's marker,
	 * if it exists, and then return it.
	 * 
	 * @param stateBitset The current state.
	 * @return The marker for the current state, or null if the default marker for the overlay should be used.
	 * 
	 */
	public Drawable getMarker(int stateBitset) {
		// marker not specified
		if (mMarker == null) {
			return null;
		}
		
		// set marker state appropriately
		setState(mMarker, stateBitset);
		return mMarker;
	}

	public Point getMarkerHotspot(int stateBitset) {
		return mMarkerHotspot;
	}

	public void setMarker(Drawable marker) {
		this.mMarker = marker;
		this.deriveHotspot();
	}
	
	public void setMarkerHotspot(Point mMarkerHotspot) {
		this.mMarkerHotspot = mMarkerHotspot;
		this.mStdHotspotPlace = HotspotPlace.CUSTOM;
	}
	
	public Point setMarkerHotspotPlace(HotspotPlace place) {
		this.mStdHotspotPlace = (place == null) ? HotspotPlace.BOTTOM_CENTER : place;
		this.deriveHotspot();
		return this.mMarkerHotspot;
	}
	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
		/*
	 * (copied from the Google API docs)
	 * Sets the state of a drawable to match a given state bitset. This is done by converting
	 * the state bitset bits into a state set of R.attr.state_pressed, R.attr.state_selected
	 * and R.attr.state_focused attributes, and then calling {@link Drawable.setState(int[])}.
	 */
	public static void setState(Drawable drawable, int stateBitset) {
		int[] stateArray = new int[] {
-				stateBitset & ITEM_STATE_PRESSED_MASK,
				stateBitset & ITEM_STATE_SELECTED_MASK,
				stateBitset & ITEM_STATE_FOCUSED_MASK,
		};
		drawable.setState(stateArray);	
	}

	/**
	 * The interaction between the hot-spot-place and the hot-spot is somewhat ambiguous.
	 * Generally either one or the other will be specified and the other will be set appropriately.
	 * The ambiguity arises in the case where they are both specified.
	 * 
	 * @param pMarker
	 * @param pHotspot
	 * @param pHotspotPlace
	 * @param pResourceProxy
	 * @return
	 */
	public static OpenStreetMapViewOverlayItem getDefaultItem(Drawable pMarker, Point pHotspot, HotspotPlace pHotspotPlace, ResourceProxy pResourceProxy) {
		OpenStreetMapViewOverlayItem that = new OpenStreetMapViewOverlayItem("<default>", "used when no marker is specified", new GeoPoint(0.0,0.0));
		that.mMarker = (pMarker != null) ? pMarker : pResourceProxy.getDrawable(ResourceProxy.bitmap.marker_default);
		
		if (pHotspot == null) {
			if (pHotspotPlace == null) {
				that.deriveHotspot();
				return that;
			} else {
				that.mStdHotspotPlace = pHotspotPlace;
				that.deriveHotspot();
				return that;
			}
		} else {
			that.mMarkerHotspot = pHotspot;
			if (pHotspotPlace == null) {
				that.mStdHotspotPlace = HotspotPlace.CUSTOM;
				return that;
			} else {
				that.mStdHotspotPlace = pHotspotPlace;
				return that;
			}
		}
	}
	
	public Drawable getDrawable() { return this.mMarker; }
	public int getWidth() { return this.mMarker.getIntrinsicWidth(); }
	public int getHeight() { return this.mMarker.getIntrinsicHeight(); }
	
	/**
	 * Select one of several standard positions for the hot spot.
	 */
	protected void deriveHotspot() {
		if (this.mStdHotspotPlace == null) this.mStdHotspotPlace = HotspotPlace.CUSTOM;
		Point markerSize = new Point( this.mMarker.getIntrinsicWidth(), this.mMarker.getIntrinsicWidth());
		// there may be some case where markerSize = DEFAULT_MARKER_SIZE but I can't think of any right now.
		
		switch (this.mStdHotspotPlace){
		case CUSTOM: 
			if (this.mMarkerHotspot != null) return;
			this.mStdHotspotPlace = HotspotPlace.BOTTOM_CENTER;
			this.mMarkerHotspot = new Point(markerSize.x/2, markerSize.y/2);
			return;
		case CENTER: 
			this.mMarkerHotspot = new Point(markerSize.x/2, markerSize.y/2);
			return;
		case BOTTOM_CENTER: 
			this.mMarkerHotspot = new Point(markerSize.x/2, markerSize.y);
			return;
		case TOP_CENTER: 
			this.mMarkerHotspot = new Point(markerSize.x/2, 0);
			return;
		case RIGHT_CENTER: 
			this.mMarkerHotspot = new Point(markerSize.x, markerSize.y/2);
			return;
		case LEFT_CENTER: 
			this.mMarkerHotspot = new Point(0, markerSize.y/2);
			return;
		case UPPER_RIGHT_CORNER: 
			this.mMarkerHotspot = new Point(markerSize.x, 0);
			return;
		case LOWER_RIGHT_CORNER: 
			this.mMarkerHotspot = new Point(markerSize.x, markerSize.y);
			return;
		case UPPER_LEFT_CORNER: 
			this.mMarkerHotspot = new Point(0, 0);
			return;
		case LOWER_LEFT_CORNER: 
			this.mMarkerHotspot = new Point(0, markerSize.y);
			return;
		}
		
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}
