/**
 * 
 */
package com.nanosheep.bikeroute.service;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import com.nanosheep.bikeroute.R;
import com.nanosheep.bikeroute.utility.Parking;
import com.nanosheep.bikeroute.utility.Stands;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.RouteManager;
import com.nanosheep.bikeroute.utility.route.RouteManager.GeocodeConnectException;
import com.nanosheep.bikeroute.utility.route.RouteManager.GeocodeException;

/**
 * Search task, 
 * Displays a planning dialog, searches, then transitions to a map displaying the
 * located route if one is found and adds start & destination to a db of recently used
 * addresses, displays an error if planning failed.
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

public class RoutePlannerTask extends AsyncTask<Void, Void, Integer> {
	/** Route planner service consts. **/
	/** Request name string. **/
	public static final String PLAN_TYPE = "plan_type";
	/** Bike plan. **/
	public static final int BIKE_PLAN = 0;
	/** PGeoPoint plan. **/
	public static final int PGeoPoint_PLAN = 1;
	/** Replanning request. **/
	public static final int REPLAN_PLAN = 2;
	/** Stand plan. **/
	public static final int STANDS_PLAN = 3;
	/** Address plan. **/
	public static final int ADDRESS_PLAN = 4;
	/** File plan. **/
	public static final int FILE_PLAN = 5;
	public static final String START_ADDRESS = "start_address";
	public static final String END_ADDRESS = "end_address";
	public static final String START_LOCATION = "start_location";
	public static final String END_POINT = "end_point";
	public static final String ROUTE_ID = "route_id";
	public static final String FILE = "route_file";
	public static final String ADDRESSES = "address_array";
	private RouteManager planner;
    protected String startAddressInput;
    protected String endAddressInput;
	private RouteListener mAct;
	private Intent mIntent;
        
        
        public RoutePlannerTask(RouteListener act, Intent intent) {
                super();
                mIntent = intent;
                mAct = act;
                planner = new RouteManager(mAct.getContext());
        }
        
        public void setListener(final RouteListener listener) {
        	mAct = listener;
        }
        
        @Override
        protected void onPreExecute() {
                
        }
        @Override
        protected Integer doInBackground(Void... arg0) {
        	int msg = R.id.plan_fail; 
        	planner.clearRoute();
        	String routeFile = mIntent.getStringExtra(FILE);
        	planner.setRouteId(mIntent.getIntExtra(ROUTE_ID, 0));
    		final String startAddressInput = mIntent.getStringExtra(START_ADDRESS);
    		final String endAddressInput = mIntent.getStringExtra(END_ADDRESS);
    		final ArrayList<String> addresses = mIntent.getStringArrayListExtra(ADDRESSES);
                switch(mIntent.getIntExtra(PLAN_TYPE, ADDRESS_PLAN)) {
        		case ADDRESS_PLAN:
        			if ("".equals(addresses.get(0)) || "".equals(addresses.get(1))) {
        				msg = R.id.argerror;
        			} else {
        				msg = R.id.result_ok;
        				try {
        					planner.setAddresses(addresses);
        					//planner.setStart(startAddressInput);
        					//planner.setDest(endAddressInput);		
        				} catch (GeocodeException e) {
							msg = R.id.geocodeerror;
						} catch (GeocodeConnectException e) {
							msg = R.id.geocodeconnecterror;
						}
        			}
        			break;
        		case BIKE_PLAN:
        			final Parking prk = new Parking(mAct.getContext());
        			if ("".equals(startAddressInput)) {
        				msg = R.id.argerror;
        			} else {
        				try {
        					msg = R.id.result_ok;
        					planner.setStart(startAddressInput);
        					planner.setDest(prk.getLocation());	
        				} catch (GeocodeException e) {
							msg = R.id.geocodeerror;
        				}  catch (GeocodeConnectException e) {
							msg = R.id.geocodeconnecterror;
						}
        			}
        			break;
        		case STANDS_PLAN:
        			if ("".equals(startAddressInput)) {
        				msg = R.id.argerror;
        			} else {
        				msg = R.id.result_ok;
        				try {
        					planner.setStart(startAddressInput);
        					planner.setDest(Stands.getNearest(planner.getStart(), mAct.getContext()));	
        				} catch (GeocodeException e) {
							msg = R.id.geocodeerror;
        				}  catch (GeocodeConnectException e) {
							msg = R.id.geocodeconnecterror;
						}
        			}
        			break;
        		case REPLAN_PLAN:
        			final Location start = mIntent.getParcelableExtra(START_LOCATION);
        			final PGeoPoint dest = mIntent.getParcelableExtra(END_POINT);
        			msg = R.id.result_ok;
        			planner.setStart(start);
        			planner.setDest(dest);	
        			break;	
        		case FILE_PLAN:
        			msg = routeFile == null ?  R.id.plan_fail : R.id.result_ok;
        			break;
        		default:
        			msg = R.id.plan_fail;
        		}
              	if ((msg == R.id.result_ok)) {
                	msg = routeFile != null ? planner.showRoute(routeFile) : planner.showRoute();
                }
        		return msg;
        }
        @Override
        protected void onPostExecute(final Integer msg) {
        	mAct.searchComplete(msg,  planner.getRoute());
        	mAct = null;
        }
        
        @Override
        protected void onCancelled() {
        	mAct.searchCancelled();
        	mAct = null;
        	super.onCancelled();
        }
}
