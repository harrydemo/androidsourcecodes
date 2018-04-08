/**
 * 
 */
package com.nanosheep.bikeroute.view.overlay;

import android.content.Context;
import org.osmdroid.views.overlay.PathOverlay;

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
 * 
 * @author jono@nanosheep.net
 * @version Sep 29, 2010
 */
public class RouteOverlay extends PathOverlay {

	/**
	 * @param color
	 * @param ctx
	 */
	public RouteOverlay(int color, Context ctx) {
		super(color, ctx);
		mPaint.setStrokeWidth(5.0f);
		mPaint.setAlpha(175);
	}

}
