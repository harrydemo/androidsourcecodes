package com.nanosheep.bikeroute.view.overlay;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.nanosheep.bikeroute.utility.Stands;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of BikeRoute.
 * 
 * Copyright (C) 2011  Jonathan Gray
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * A class to display markers on a map and update them from a remote
 * feed.
 * @author jono@nanosheep.net
 * @version Jun 21, 2010
 */

public class LiveMarkers implements OnItemGestureListener<OverlayItem> {
	/** Reference to map view to draw markers over. **/
	private final MapView mv;
	/** Markers list for use by thread. **/
	private List<OverlayItem> markers;
	private final Context context;
	/** Radius to return markers within. **/
	protected static final double RADIUS = 0.5;
	/** List of overlay items. **/
	private final List<OverlayItem> mOverlays;
	/** Itemized Overlay. **/
	private ItemizedOverlay<OverlayItem> iOverlay;

	public LiveMarkers(final MapView mOsmv, final Context ctxt) {
		mv = mOsmv;
		context = ctxt.getApplicationContext();
		mOverlays = new ArrayList<OverlayItem>(1);
		iOverlay = new ItemizedIconOverlay<OverlayItem>(mOverlays,
				this, mv.getResourceProxy());
	}

	/**
	 * Update markers around given point.
	 * @param p the Geopoint to gather markers around.
	 */

	public void refresh(final GeoPoint p) {
		Thread update = new Thread() {
			private static final int MSG = 0;
			@Override
			public void run() {
				markers = Stands.getMarkers(p, RADIUS, context);
				LiveMarkers.this.messageHandler.sendEmptyMessage(MSG);
			}
		};
		update.start();
	}
	
	/**
	 * Handler for stands thread.
	 * Remove the existing stands overlay if it exists and
	 * replace it with the new one from the thread.
	 */
	
	private final Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			if (mv.getOverlays().contains(iOverlay)) {
				mv.getOverlays().remove(iOverlay);
			}
			mOverlays.clear();
			mOverlays.addAll(markers);
			iOverlay = new ItemizedIconOverlay<OverlayItem>(mOverlays, LiveMarkers.this, mv.getResourceProxy());
			mv.getOverlays().add(iOverlay);
			mv.postInvalidate();
		}
	};

	/* (non-Javadoc)
	 * @see org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener#onItemLongPress(int, java.lang.Object)
	 */
	@Override
	public boolean onItemLongPress(int index, OverlayItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener#onItemSingleTapUp(int, java.lang.Object)
	 */
	@Override
	public boolean onItemSingleTapUp(int index,
			OverlayItem item) {
		// TODO Auto-generated method stub
		return false;
	}

}
