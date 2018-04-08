package com.nanosheep.bikeroute.parser;

import android.util.Log;
import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.utility.route.Route;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Parse a google elevations json object to an xy data series.
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
 * @version Jun 25, 2010
 */
public class GoogleElevationParser extends XMLParser implements Parser {
	/** Distance covered. **/
	private double distance;
	/** Route to work on. **/
	private final Route route;
	
	public GoogleElevationParser(final String feedUrl, final Route route) {
		super(feedUrl);
		this.route = route;
	}
	
	/**
	 * Parses a url pointing to a Google JSON object to a Route object.
	 * @return a Route object based on the JSON object.
	 */
	
	@Override
	public Route parse() {
		// turn the stream into a string
		final String result = convertStreamToString(this.getInputStream());
		try {
			//Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			//Get the results object
			final JSONArray jsonResult = json.getJSONArray("results");
			JSONObject location = jsonResult.getJSONObject(0).getJSONObject("location");
			//Get the first point, use it as a basis for calculating the distance
			//Store incremented distance with retrieved elevation.
			double lastLat = location.getDouble("lat");
			double lastLng = location.getDouble("lng");
			for (int i = 0; i < jsonResult.length(); i++) {
				location = jsonResult.getJSONObject(i).getJSONObject("location");
				final double lat = location.getDouble("lat");
				final double lng = location.getDouble("lng");
				final double elevation = jsonResult.getJSONObject(i).getDouble("elevation");
				distance += pointDiff(lat, lng, lastLat, lastLng);
				lastLat = lat;
				lastLng = lng;
				route.addElevation(elevation, distance);
			}
		} catch (JSONException e) {
			Log.e(e.getMessage(), "Google Elevation Parser - " + feedUrl);
		}
		return route;
	}

	/**
	 * Convert an inputstream to a string.
	 * @param input inputstream to convert.
	 * @return a String of the inputstream.
	 */
	
	private static String convertStreamToString(final InputStream input) {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        final StringBuilder sBuf = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sBuf.append(line);
            }
        } catch (IOException e) {
        	Log.e(e.getMessage(), "Google parser, stream2string");
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            	Log.e(e.getMessage(), "Google parser, stream2string");
            }
        }
        return sBuf.toString();
    }
	
	/**
	 * Calculate the distance between two points of the earth using
	 * the haversine formula.
	 * @param lat Starting latitude.
	 * @param lng Starting longitude.
	 * @param latA End latitude,
	 * @param lngA End longitude.
	 * @return The distance between the two points in m.
	 */
	
	private double pointDiff(final double lat, final double lng, final double latA, final double lngA) {
		final double dLat = Math.toRadians(latA - lat);
		final double dLon = Math.toRadians(lngA - lng); 
		final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
		        Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(latA)) * 
		        Math.sin(dLon / 2) * Math.sin(dLon / 2); 
		final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); 
		return BikeRouteConsts.EARTH_RADIUS * c * 1000;
	}
}
