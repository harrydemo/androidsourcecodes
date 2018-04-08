package com.nanosheep.bikeroute.parser;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;
import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.utility.Convert;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.Route;
import com.nanosheep.bikeroute.utility.route.Segment;
import org.xml.sax.Attributes;

/**
 * An xml parser for the CycleStreets.net journey planner API.
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
 * @version Jun 21, 2010
 */

public class CycleStreetsParser extends XMLParser implements Parser {
	/** Distance covered. **/
	private double distance;

	public CycleStreetsParser(final String feedUrl) {
		super(feedUrl);
	}

	/**
	 * Parse the query URL to a list of GeoPoints.
	 * @return a list of geopoints corresponding to the routeplan.
	 */
	
	@Override
	public final Route parse() {
		final Segment segment = new Segment();
		final Route route = new Route();
		route.setRouter(BikeRouteConsts.CS);
		route.setCopyright("Route planning by <a href=\"http://cyclestreets.net\">CycleStreets.net</a>");
		final RootElement root = new RootElement(MARKERS);
		final Element marker = root.getChild(MARKER);
		// Listen for start of tag, get attributes and set them
		// on current marker.
		marker.setStartElementListener(new StartElementListener() {
			@Override
			public void start(final Attributes attributes) {
				segment.clearPoints();
				PGeoPoint p;
				
				final String pointString = attributes.getValue("points");
				final String nameString = attributes.getValue("name");
				String turnString = attributes.getValue("turn");
				final String type = attributes.getValue("type");
				final String walk = attributes.getValue("walk");
				final String length = attributes.getValue("distance");
				final String totalDistance = attributes.getValue("length");
				final String elev = attributes.getValue("elevations");
				final String distances = attributes.getValue("distances");
				final String id = attributes.getValue("itinerary");
				
				/** Parse segment. **/
				if ("segment".equals(type)) {
					 StringBuffer sBuf = new StringBuffer();
					  if (!"unknown".equals(turnString)) {

                          sBuf.append(Character.toUpperCase(
                                  turnString.charAt(0))).append(turnString.substring(1));
						  sBuf.append(" at ");
					  }
					  sBuf.append(nameString);
					  sBuf.append(' ');
					  if ("1".equals(walk)) {
						  sBuf.append("(dismount)");
					  }
					  segment.setName(nameString);
					  segment.setInstruction(sBuf.toString());
									
					final String[] pointsArray = pointString.split(" ", -1);
					final String[] elevations = elev.split(",", -1);
					final String[] dists = distances.split(",", -1);
					
					//Add elevations to the elevation/distance series
					for (int i = 0; i < dists.length; i++) {
						int len = Integer.parseInt(dists[i]);
						int elevation = Integer.parseInt(elevations[i]);
						distance += len;
						route.addElevation(elevation, distance);
					}
					
					final int len = pointsArray.length;
					for (int i = 0; i < len; i++) {
						final String[] point = pointsArray[i].split(",", -1);
						p = new PGeoPoint(Convert.asMicroDegrees(Double.parseDouble(point[1])), 
								Convert.asMicroDegrees(Double.parseDouble(point[0])));
						route.addPoint(p);
						segment.addPoint(p);
					}
					segment.setDistance(distance/1000);
					segment.setLength(Integer.parseInt(length));
					
					
				} else {
					/** Parse route details. **/
					route.setName(nameString);
					route.setLength(Integer.parseInt(totalDistance));
					route.setItineraryId(Integer.parseInt(id));
				}
			}
		});
		marker.setEndElementListener(new EndElementListener() {
			@Override
			public void end() {
				if (segment.getInstruction() != null) {
					route.addSegment(segment.copy());
				}
			}
		});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root
					.getContentHandler());
		} catch (Exception e) {
			Log.e(e.getMessage(), "CycleStreets parser - " + feedUrl);
			return null;
		}
		//route.buildTree();
		return route;
	}
}