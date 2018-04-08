/**
 * TorProxyLib - Anonymous data communication for Android devices
 * 			   - Tools for application developers
 * Copyright (C) 2009 Connell Gauld
 * 
 *  Thanks to University of Cambridge,
 * 		Alastair Beresford and Andrew Rice
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package uk.ac.cam.cl.dtg.android.tor.TorProxyLib;

public class TorProxyLib {
	
	public static final String CONTROL_SERVICE_PACKAGE = "uk.ac.cam.cl.dtg.android.tor.TorProxy";
	public static final String CONTROL_SERVICE_CLASS = "uk.ac.cam.cl.dtg.android.tor.TorProxy.TorProxyControlService";

	public static final String SETTINGS_ACTIVITY_PACKAGE = "uk.ac.cam.cl.dtg.android.tor.TorProxy";
	public static final String SETTINGS_ACTIVITY_CLASS = "uk.ac.cam.cl.dtg.android.tor.TorProxy.TorProxySettings";
	
	// Intents
	public static final String STATUS_CHANGE_INTENT = "uk.ac.cam.cl.dtg.android.tor.TOR_STATUS_CHANGE";
	public static final String PROFILE_CHANGE_INTENT = "uk.ac.cam.cl.dtg.android.tor.TOR_PROFILE_CHANGE";
	public static final String SERVICE_CHANGE_INTENT = "uk.ac.cam.cl.dtg.android.tor.HIDDEN_SERVICE_CHANGE";
	
	// Permission
	public static final String CONTROL_PERMISSION = "uk.ac.cam.cl.dtg.android.tor.CONTROL_PERMISSION";
	
	// Profile stuff
	public static final int PROFILE_OFF = 0;
	public static final int PROFILE_ONDEMAND = 1;
	public static final int PROFILE_ON = 2;
	
	// Status stuff
	public static final int STATUS_UNAVAILABLE = 0;
	public static final int STATUS_REQUIRES_DEMAND = 1;
	public static final int STATUS_CONNECTING = 2;
	public static final int STATUS_ON = 3;
}
