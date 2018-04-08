/**
 * 
 */
package com.nanosheep.bikeroute.utility;

import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import com.nanosheep.bikeroute.RouteMap;

/**
 * Detect gestures to control the onscreen directions display in a routemap.
 * 
 * Swipe right to advance, left to retreat. Tap to focus, double tap to close.
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
 * @author jono@nanosheep.net
 * @version Jun 29, 2010
 */
public class TurnByTurnGestureListener extends SimpleOnGestureListener implements
		OnGestureListener {
	
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final RouteMap map;
    
    public TurnByTurnGestureListener(final RouteMap map) {
    	this.map = map;
    }
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
               map.lastStep(); 
               return true;
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            	map.nextStep();
            	return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
    
    @Override
    public boolean onSingleTapConfirmed(final MotionEvent evt) {
    	map.showStep();
    	return true;
    }
    
    @Override 
    public boolean onDoubleTap(final MotionEvent evt) {
    	map.hideStep();
    	return true;
    }

}
