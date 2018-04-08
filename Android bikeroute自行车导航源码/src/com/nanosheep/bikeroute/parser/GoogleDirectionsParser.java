package com.nanosheep.bikeroute.parser;

import android.util.Log;
import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.utility.Convert;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.Route;
import com.nanosheep.bikeroute.utility.route.Segment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Parse a google directions json object to a route.
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
public class GoogleDirectionsParser extends XMLParser implements Parser {
	/** Distance covered. **/
	private int distance;
	
	public GoogleDirectionsParser(String feedUrl) {
		super(feedUrl);
	}
	
	/**
	 * Parses a url pointing to a Google JSON object to a Route object.
	 * @return a Route object based on the JSON object.
	 */
	
	@Override
	public Route parse() {
		// turn the stream into a string
		final String result = convertStreamToString(this.getInputStream());
		//Create an empty route
		final Route route = new Route();
		route.setRouter(BikeRouteConsts.G);
		//Create an empty segment
		final Segment segment = new Segment();
		try {
			//Tranform the string into a json object
			final JSONObject json = new JSONObject(result);
			//Get the route object
			final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
			//Get the leg, only one leg as we don't support waypoints
			final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
			//Get the steps for this leg
			final JSONArray steps = leg.getJSONArray("steps");
			//Number of steps for use in for loop
			final int numSteps = steps.length();
			//Set the name of this route using the start & end addresses
			route.setName(leg.getString("start_address") + " to " + leg.getString("end_address"));
			//Get google's copyright notice (tos requirement)
			route.setCopyright(jsonRoute.getString("copyrights"));
			//Get the total length of the route.
			route.setLength(leg.getJSONObject("distance").getInt("value"));
			//Get any warnings provided (tos requirement)
			if (!jsonRoute.getJSONArray("warnings").isNull(0)) {
				route.setWarning(jsonRoute.getJSONArray("warnings").getString(0));
			}
			/* Loop through the steps, creating a segment for each one and
			 * decoding any polylines found as we go to add to the route object's
			 * map array.
			 */
			for (int i = 0; i < numSteps; i++) {
				segment.clearPoints();
				//Get the individual step
				final JSONObject step = steps.getJSONObject(i);
				//Set the length of this segment in metres
				final int length = step.getJSONObject("distance").getInt("value");
				distance += length;
				segment.setLength(length);
				segment.setDistance(distance/1000);
				//Strip html from google directions and set as turn instruction
				segment.setInstruction(step.getString("html_instructions"));
				segment.setName(segment.getInstruction());
				//Retrieve & decode this segment's polyline and add it to the route & segment.
				List<PGeoPoint> points = decodePolyLine(step.getJSONObject("polyline").getString("points"));
				route.addPoints(points);
				segment.addPoints(points);
				//Push a copy of the segment to the route
				route.addSegment(segment.copy());
			}
			//Keep a copy of the overview polyline for the route for elevation service query.
			route.setPolyline(jsonRoute.getJSONObject("overview_polyline").getString("points"));
		} catch (JSONException e) {
			Log.e(e.getMessage(), "Google JSON Parser - " + feedUrl);
			return null;
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
 
        String line;
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
	 * Decode a polyline string into a list of PGeoPoints.
	 * @param poly polyline encoded string to decode.
	 * @return the list of PGeoPoints represented by this polystring.
	 */
	
	private List<PGeoPoint> decodePolyLine(final String poly) {
		int len = poly.length();
		int index = 0;
		List<PGeoPoint> decoded = new ArrayList<PGeoPoint>();
		int lat = 0;
		int lng = 0;

		while (index < len) {
		int b;
		int shift = 0;
		int result = 0;
		do {
			b = poly.charAt(index++) - 63;
			result |= (b & 0x1f) << shift;
			shift += 5;
		} while (b >= 0x20);
		int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		lat += dlat;

		shift = 0;
		result = 0;
		do {
			b = poly.charAt(index++) - 63;
			result |= (b & 0x1f) << shift;
			shift += 5;
		} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

		decoded.add(new PGeoPoint(
				Convert.asMicroDegrees(lat / 1E5), Convert.asMicroDegrees(lng / 1E5)));
		}

		return decoded;
		}
}
