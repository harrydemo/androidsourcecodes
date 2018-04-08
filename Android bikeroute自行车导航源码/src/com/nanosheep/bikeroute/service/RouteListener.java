/**
 * 
 */
package com.nanosheep.bikeroute.service;

import android.content.Context;
import com.nanosheep.bikeroute.utility.route.Route;


/**
 * Interface for classes wishing to listen for route plans.
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
 * @version Oct 11, 2010
 */
public interface RouteListener {
	/**
	 * Called when a route search completes.
	 * @param msg Response code
	 * @param route Route computed or null
	 */
	public void searchComplete(Integer msg, Route route);
	/**
	 * Called when a search is cancelled.
	 */
	public void searchCancelled();
	/**
	 * @return the context of the listening activity.
	 */
	public Context getContext();
}
