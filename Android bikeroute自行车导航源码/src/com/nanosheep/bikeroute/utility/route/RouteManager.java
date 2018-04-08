package com.nanosheep.bikeroute.utility.route;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import com.nanosheep.bikeroute.R;
import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.parser.*;
import com.nanosheep.bikeroute.utility.Convert;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Plans routes and displays them as overlays on the provided mapview.
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
 * @version Oct 3, 2010
 */
public class RouteManager {
	/** Owning activity. **/
	private final Context ctxt;
	/** Route planned switch. **/
	private boolean planned;
	/** Route. **/
	private Route route;
	/** Start point. **/
	private GeoPoint start;
	/** Destination point. **/
	private GeoPoint dest;
	/** Country the route is in. **/
	private String country;
	/** Geocoder. **/
	private final Geocoder geocoder;
	private int id;
	/** Route points. **/
	private List<PGeoPoint> points;
	
	public RouteManager(final Context context) {
		super();
		ctxt = context.getApplicationContext();
		planned = false;
		geocoder = new Geocoder(ctxt);
		points = new ArrayList<PGeoPoint>();
	}
	
	public int showRoute(final String routeFile) {
		clearRoute();
		Parser parser = new BikeRouteParser(routeFile);
		route = parser.parse();
		if ((route == null) || RouteManager.this.route.getPoints().isEmpty()) {
			return R.id.plan_fail;
		}
		//Build KD tree for the route
		route.buildTree();
		return R.id.result_ok;
	}
	
	/**
	 * Plan a route between the points given and show it on the map. Displays an
	 * alert if the planning failed for some reason.
	 * Executes planning process in a separate thread, displays a progress
	 * dialog while planning.
	 */

	public int showRoute() {
		clearRoute();
		try {
			country = geocoder.getFromLocation(Convert.asDegrees(
					points.get(points.size()-1).getLatitudeE6()),Convert.asDegrees(
							points.get(points.size()-1).getLongitudeE6()), 1).get(0).
							getCountryCode();
			//route = plan(start, dest);
			route = plan(points);
			route.setCountry(country);
			if (RouteManager.this.route.getPoints().isEmpty()) {
				throw new PlanException("Route is empty.");
			}
			//Build KD tree for the route
			route.buildTree();
		} catch (IOException e) {
			Log.e(e.getMessage(), "Planner");
			return R.id.plan_fail;
		} catch (PlanException e) {
			return R.id.network_error;
		} catch (Exception e) {
			return R.id.plan_fail;
		}
		return R.id.result_ok;
	}

	private Route plan(List<PGeoPoint> pointsList) {
		Parser parser;
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctxt);
		//Default router is cyclestreets in UK, MapQuest elsewhere.
		String defaultRouter = Locale.getDefault().equals(Locale.UK) ? BikeRouteConsts.CS : BikeRouteConsts.MQ;
		String router = settings.getString("router", defaultRouter);
		
		if (BikeRouteConsts.CS.equals(router)) {
			String routeType = settings.getString("cyclestreetsJourneyPref", "balanced");
			final StringBuffer sBuf = new StringBuffer(ctxt.getString(R.string.cs_api));
			sBuf.append("start_lat=");
			sBuf.append(Convert.asDegrees(pointsList.get(0).getLatitudeE6()));
			sBuf.append("&start_lng=");
			sBuf.append(Convert.asDegrees(pointsList.get(0).getLongitudeE6()));
			sBuf.append("&way_lat=");
			sBuf.append(Convert.asDegrees(pointsList.get(1).getLatitudeE6()));
			sBuf.append("&way_lng=");
			sBuf.append(Convert.asDegrees(pointsList.get(1).getLongitudeE6()));
			if(pointsList.size() == 3) {
				sBuf.append("&dest_lat=");
				sBuf.append(Convert.asDegrees(pointsList.get(2).getLatitudeE6()));
				sBuf.append("&dest_lng=");
				sBuf.append(Convert.asDegrees(pointsList.get(2).getLongitudeE6()));
			}
			sBuf.append("&plan=");
			sBuf.append(routeType);
			sBuf.append("&route_id=");
			sBuf.append(id);

			parser = new CycleStreetsParser(sBuf
				.toString());
		} else if (BikeRouteConsts.G.equals(router)) {
			final StringBuffer sBuf = new StringBuffer(ctxt.getString(R.string.us_api));
			sBuf.append("origin=");
			sBuf.append(Convert.asDegrees(pointsList.get(0).getLatitudeE6()));
			sBuf.append(',');
			sBuf.append(Convert.asDegrees(pointsList.get(0).getLongitudeE6()));
			sBuf.append("&destination=");
			sBuf.append(Convert.asDegrees(pointsList.get(1).getLatitudeE6()));
			sBuf.append(',');
			sBuf.append(Convert.asDegrees(pointsList.get(1).getLongitudeE6()));
			if ("US".equals(country)) {
				sBuf.append("&sensor=true&mode=bicycling");
			} else {
				sBuf.append("&sensor=true&mode=driving");
			}
		parser = new GoogleDirectionsParser(sBuf.toString());
		} else {
			final StringBuffer sBuf = new StringBuffer(ctxt.getString(R.string.mq_api));
			sBuf.append(pointsList.get(0).getLatitudeE6()/1E6);
			sBuf.append(',');
			sBuf.append(pointsList.get(0).getLongitudeE6()/1E6);
			sBuf.append("&to=");
			sBuf.append(pointsList.get(1).getLatitudeE6()/1E6);
			sBuf.append(',');
			sBuf.append(pointsList.get(1).getLongitudeE6()/1E6);
			sBuf.append("&generalize=0.1&shapeFormat=cmp");
			parser = new MapQuestParser(sBuf.toString());
		}
		Route r =  parser.parse();
		//Untidy.
		//If not using cyclestreets, need to query elevations api for
		//this route.
		if (!BikeRouteConsts.CS.equals(router)) {
			final StringBuffer elev = new StringBuffer(ctxt.getString(R.string.elev_api));
			elev.append(URLEncoder.encode(r.getPolyline()));
			parser = new GoogleElevationParser(elev.toString(), r);
			r = parser.parse();
		}
		r.setCountry(country);
		r.setRouteId(id);
		return r;
	}

	public void setRouteId(int routeId) {
		id = routeId;
	}
	
	/**
	 * Plan a route from the start point to a destination.
	 * 
	 * @param start Start point.
	 * @param dest Destination.
	 * @return a list of segments for the route.
	 * @deprecated
	 */

	private Route plan(final GeoPoint start, final GeoPoint dest) {
		Parser parser;
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ctxt);
		//Default router is cyclestreets in UK, MapQuest elsewhere.
		String defaultRouter = Locale.getDefault().equals(Locale.UK) ? BikeRouteConsts.CS : BikeRouteConsts.MQ;
		String router = settings.getString("router", defaultRouter);
		
		if (BikeRouteConsts.CS.equals(router)) {
			String routeType = settings.getString("cyclestreetsJourneyPref", "balanced");
			final StringBuffer sBuf = new StringBuffer(ctxt.getString(R.string.cs_api));
			sBuf.append("start_lat=");
			sBuf.append(Convert.asDegrees(start.getLatitudeE6()));
			sBuf.append("&start_lng=");
			sBuf.append(Convert.asDegrees(start.getLongitudeE6()));
			sBuf.append("&dest_lat=");
			sBuf.append(Convert.asDegrees(dest.getLatitudeE6()));
			sBuf.append("&dest_lng=");
			sBuf.append(Convert.asDegrees(dest.getLongitudeE6()));
			sBuf.append("&plan=");
			sBuf.append(routeType);
			sBuf.append("&route_id=");
			sBuf.append(id);

			parser = new CycleStreetsParser(sBuf
				.toString());
		} else if (BikeRouteConsts.G.equals(router)) {
			final StringBuffer sBuf = new StringBuffer(ctxt.getString(R.string.us_api));
			sBuf.append("origin=");
			sBuf.append(Convert.asDegrees(start.getLatitudeE6()));
			sBuf.append(',');
			sBuf.append(Convert.asDegrees(start.getLongitudeE6()));
			sBuf.append("&destination=");
			sBuf.append(Convert.asDegrees(dest.getLatitudeE6()));
			sBuf.append(',');
			sBuf.append(Convert.asDegrees(dest.getLongitudeE6()));
			if ("US".equals(country)) {
				sBuf.append("&sensor=true&mode=bicycling");
			} else {
				sBuf.append("&sensor=true&mode=driving");
			}
		parser = new GoogleDirectionsParser(sBuf.toString());
		} else {
			final StringBuffer sBuf = new StringBuffer(ctxt.getString(R.string.mq_api));
			sBuf.append(start.getLatitudeE6()/1E6);
			sBuf.append(',');
			sBuf.append(start.getLongitudeE6()/1E6);
			sBuf.append("&to=");
			sBuf.append(dest.getLatitudeE6()/1E6);
			sBuf.append(',');
			sBuf.append(dest.getLongitudeE6()/1E6);
			sBuf.append("&generalize=0.1&shapeFormat=cmp");
			parser = new MapQuestParser(sBuf.toString());
		}
		Route r =  parser.parse();
		//Untidy.
		//If not using cyclestreets, need to query elevations api for
		//this route.
		if (!BikeRouteConsts.CS.equals(router)) {
			final StringBuffer elev = new StringBuffer(ctxt.getString(R.string.elev_api));
			elev.append(URLEncoder.encode(r.getPolyline()));
			parser = new GoogleElevationParser(elev.toString(), r);
			r = parser.parse();
		}
		r.setCountry(country);
		r.setRouteId(id);
		return r;
	}
	
	/**
	 * Clear the current route.
	 */

	public void clearRoute() {
	//	routeOverlay = null;
		planned = false;
		route = null;
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(final Route route) {
		this.route = route;
		//routeOverlay = new RouteOverlay(route, Color.BLUE);
		planned = true;
	}

	/**
	 * @return the route
	 */
	public Route getRoute() {
		return route;
	}

	/**
	 * @param isPlanned the isPlanned to set
	 */
	public void setPlanned(final boolean isPlanned) {
		this.planned = isPlanned;
	}

	/**
	 * @return the isPlanned
	 */
	public boolean isPlanned() {
		return planned;
	}
	
	/**
	 * @return the starting geopoint
	 */
	
	public GeoPoint getStart() {
		return start;
	}
	
	/**
	 * @return the destination geopoint.
	 */
	
	public GeoPoint getDest() {
		return dest;
	}
	
	/**
	 * Set the start point for the route.
	 * @param start start point.
	 */
	
	public void setStart(final GeoPoint start) {
		this.start = start;
	}
	
	/**
	 * Set a start point which is an address.
	 * @param address
	 */
	
	public void setStart(final Address address) {
		GeoPoint p = new GeoPoint(Convert.asMicroDegrees(address.getLatitude()),
				Convert.asMicroDegrees(address.getLongitude()));
		setStart(p);
	}
	
	/**
	 * Set a destination point which is an address
	 * @param dest2
	 */
	
	public void setDest(final Address dest2) {
		GeoPoint p = new GeoPoint(Convert.asMicroDegrees(dest2.getLatitude()),
				Convert.asMicroDegrees(dest2.getLongitude()));
		setDest(p);
	}
	
	/**
	 * Set a start point which is a location
	 * @param location
	 */
	
	public void setStart(final Location location) {
		GeoPoint p = new GeoPoint(Convert.asMicroDegrees(location.getLatitude()),
				Convert.asMicroDegrees(location.getLongitude()));
		setStart(p);
	}
	
	/**
	 * Set a destination which is a location.
	 * @param location
	 */
	
	public void setDest(final Location location) {
		GeoPoint p = new GeoPoint(Convert.asMicroDegrees(location.getLatitude()),
				Convert.asMicroDegrees(location.getLongitude()));
		setDest(p);
	}
	
	/**
	 * Set the destination point for the route.
	 * @param end point.
	 */
	
	public void setDest(final GeoPoint end) {
		this.dest = end;
	}
	
	public void setDest(final String name) throws GeocodeException, GeocodeConnectException {
		setDest(addressFromName(name));
	}
	
	/**
	 * Get an address from a name string.
	 * @param name
	 * @return
	 * @throws GeocodeException
	 * @throws GeocodeConnectException
	 */
	
	private Address addressFromName(final String name) throws GeocodeException, GeocodeConnectException {
		try {
			List<Address> addresses = geocoder.getFromLocationName(
					name, 1);
			if(addresses == null || addresses.size() == 0) {
				throw new GeocodeException();
			}
			return addresses.get(0);
		} catch (IOException e) {
			throw new GeocodeConnectException(e.getMessage());
		}
	}
	
	public void setStart(final String name) throws GeocodeException, GeocodeConnectException {
		setStart(addressFromName(name));
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * Exception type for route planning exceptions.
	 * 
	 * @author jono@nanosheep.net
	 * @version Jun 27, 2010
	 */
	private class PlanException extends Exception {

		/**
		 * Empty planexception.
		 */
		public PlanException() {
		}

		/**
		 * PlanException with specified message.
		 * @param detailMessage
		 */
		public PlanException(String detailMessage) {
			super(detailMessage);
		}

	}
	
	/**
	 * Exception for failed (i.e. null result) geocodes.
	 * @author jono@nanosheep.net
	 * @version Apr 7, 2011
	 */
	
	public class GeocodeException extends Exception {
		public GeocodeException() {}
		public GeocodeException(String detailMessage) {
			super(detailMessage);
		}
	}
	/**
	 * Exception for geocode service connection failures.
	 * @author jono@nanosheep.net
	 * @version Apr 7, 2011
	 */
	public class GeocodeConnectException extends Exception {
		public GeocodeConnectException() {}
		public GeocodeConnectException(String detailMessage) {
			super(detailMessage);
		}
	}
	public void setAddresses(ArrayList<String> addresses) throws GeocodeException, GeocodeConnectException {
		points.clear();
		for(String str : addresses) {
			Address address = addressFromName(str);
			PGeoPoint p = new PGeoPoint(Convert.asMicroDegrees(address.getLatitude()),
					Convert.asMicroDegrees(address.getLongitude()));
			points.add(p);
		}
	}

}
