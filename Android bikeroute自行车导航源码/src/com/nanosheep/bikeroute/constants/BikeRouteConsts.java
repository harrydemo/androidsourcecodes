/**
 * 
 */
package com.nanosheep.bikeroute.constants;

import android.view.Menu;

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
 * @version Sep 27, 2010
 */
public final class BikeRouteConsts {
	
	/**Version number.**/
	public static final int VERSION = 1136;
	
	
	/** Osmdroid consts. **/
	public static final int MENU_MY_LOCATION = Menu.FIRST;
    public static final int MENU_MAP_MODE = MENU_MY_LOCATION + 1;
    public static final int MENU_SAMPLES = MENU_MAP_MODE + 1;
    public static final int MENU_ABOUT = MENU_SAMPLES + 1;

    public static final int NOT_SET = Integer.MIN_VALUE;
	
	/** Pi/180 for converting degrees - radians. **/
	public static final double PI_180 = Math.PI / 180;
	/** Radius of the earth for degrees - miles calculations. **/
	public static final double EARTH_RADIUS = 3960.0;
	
	/** Router Consts. **/
	/** Google. **/
	public static final String G = "Google";
	/** MapQuest. **/
	public static final String MQ = "MapQuest";
	/** CycleStreets. **/
	public static final String CS = "CycleStreets";
	
	/** User Agent String. **/
	public static final String AGENT = "Apache-HttpClient/BikeRoute "+VERSION;
	
	private BikeRouteConsts() { }
}
