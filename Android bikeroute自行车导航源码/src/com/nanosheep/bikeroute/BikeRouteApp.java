/**
 * 
 */
package com.nanosheep.bikeroute;

import android.app.Application;
import com.nanosheep.bikeroute.utility.AddressDatabase;
import com.nanosheep.bikeroute.utility.RouteDatabase;
import com.nanosheep.bikeroute.utility.route.Route;
import com.nanosheep.bikeroute.utility.route.Segment;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

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
 * @author jono@nanosheep.net
 * @version Jul 2, 2010
 */
@ReportsCrashes(formKey = "dGF2OV9Cd3VsOWtLMjRnWG9FRG5mZEE6MQ",
	mode = ReportingInteractionMode.NOTIFICATION,
    resNotifTickerText = R.string.crash_notif_ticker_text,
    resNotifTitle = R.string.crash_notif_title,
    resNotifText = R.string.crash_notif_text,
    resDialogText = R.string.crash_dialog_text,
    resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
    resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
    resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
    resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast message when the user accepts to send a report.
    )
public class BikeRouteApp extends Application {
	/** Route object. **/
	private Route route;
	/** The current segment. **/
	private Segment segment;
	/** Previous addresses db. **/
	private AddressDatabase addressDB;
	/** Favourite routes db. **/
	private RouteDatabase routeDB;

	public BikeRouteApp () {
		super();
	}
	
	@Override
	public void onCreate() {
		Thread t = new Thread() {
			@Override
			public void run () {
				addressDB = new AddressDatabase(BikeRouteApp.this);
				setRouteDB(new RouteDatabase(BikeRouteApp.this));
			}
		};
		t.start();
		ACRA.init(this);
		super.onCreate();
	}
	
	/**
	 * @param route the route to set
	 */
	public void setRoute(final Route route) {
		this.route = route;
		segment = (route != null) && !route.getSegments().isEmpty() ? route.getSegments().get(0) : null;
	}

	/**
	 * @return the route
	 */
	public Route getRoute() {
		return route;
	}

	/**
	 * @param segment the segment to set.
	 */
	public void setSegment(final Segment segment) {
		this.segment = segment;
	}

	/**
	 * @return the current segment
	 */
	public Segment getSegment() {
		return segment;
	}

	/**
	 * @return the db
	 */
	public AddressDatabase getDb() {
		return addressDB;
	}

	/**
	 * @param routeDB the routeDB to set
	 */
	public void setRouteDB(RouteDatabase routeDB) {
		this.routeDB = routeDB;
	}

	/**
	 * @return the routeDB
	 */
	public RouteDatabase getRouteDB() {
		return routeDB;
	}
}
