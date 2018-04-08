package com.nanosheep.bikeroute;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;

import com.nanosheep.bikeroute.utility.ResourceProxyImpl;

/**
 * Based on osmdroid default map view activity.
 * 
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
 * 
 * Default map view activity.
 * @author Manuel Stahl
 *
 */
public class OpenStreetMapActivity extends Activity {

        // ===========================================================
        // Fields
        // ===========================================================

        protected SharedPreferences mPrefs;
        protected MapView mOsmv;
        protected MyLocationOverlay mLocationOverlay;
        protected ResourceProxy mResourceProxy;

        // ===========================================================
        // Constructors
        // ===========================================================
            
        
        /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResourceProxy = new ResourceProxyImpl(getApplicationContext());

        mPrefs = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE);

    }

    @Override
    protected void onPause() {
        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.putInt(getString(R.string.prefs_scrollx), mOsmv.getScrollX());
        edit.putInt(getString(R.string.prefs_scrolly), mOsmv.getScrollY());
        edit.putInt(getString(R.string.prefs_zoomlevel), mOsmv.getZoomLevel());
        edit.putBoolean(getString(R.string.prefs_showlocation), mLocationOverlay.isMyLocationEnabled());
        edit.putBoolean(getString(R.string.prefs_followlocation), mLocationOverlay.isFollowLocationEnabled());
        edit.commit();

        this.mLocationOverlay.disableMyLocation();
        System.gc();
        super.onPause();
    }

    @Override
    protected void onResume() {
    	System.gc();
        if(mPrefs.getBoolean(getString(R.string.prefs_showlocation), false)) {
                this.mLocationOverlay.enableMyLocation();
        }
        if(mPrefs.getBoolean(getString(R.string.prefs_followlocation), true)) {
        	this.mLocationOverlay.enableFollowLocation();
        } else {
        	this.mLocationOverlay.disableFollowLocation();
        }
        super.onResume();
    }


    @Override
    public boolean onTrackballEvent(final MotionEvent event) {
            return this.mOsmv.onTrackballEvent(event);
    }
    
    @Override
	public boolean onTouchEvent(final MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				this.mLocationOverlay.disableFollowLocation();
			}
	        return super.onTouchEvent(event);
	}
}