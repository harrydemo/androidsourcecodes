package com.nanosheep.bikeroute.parser;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.Route;
import com.nanosheep.bikeroute.utility.route.Segment;
import org.xml.sax.Attributes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * An xml parser for routes from other BikeRoutes.
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
 */

public class BikeRouteParser extends XMLParser implements Parser {

	private String path;
	
	public BikeRouteParser(final String routeFile) {
		path = routeFile;
	}

	/**
	 * Parse the query URL to a list of PGeoPoints.
	 * @return a list of PGeoPoints corresponding to the routeplan.
	 */
	
	@Override
	public final Route parse() {
		final Segment segment = new Segment();
		final Route route = new Route();
		//route.setCopyright("Route planning by CycleStreets.net");
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
				final String turnString = attributes.getValue("turn");
				final String type = attributes.getValue("type");
				final String copyright = attributes.getValue("copyright");
				final String distance = attributes.getValue("distance");
				final String length = attributes.getValue("length");
				final String elev = attributes.getValue("elevations");
				final String id = attributes.getValue("itineraryid");
				final String warning = attributes.getValue("warning");
				final String polyline = attributes.getValue("polyline");
				final String country = attributes.getValue("country");
				final String router = attributes.getValue("router");
				
				/** Parse segment. **/
				if ("segment".equals(type)) {
					  segment.setName(nameString);
					  segment.setInstruction(turnString);
									
					final String[] pointsArray = pointString.split(" ", -1);
					
					final int len = pointsArray.length;
					for (int i = 0; i < len; i++) {
						final String[] point = pointsArray[i].split(",", -1);
						p = new PGeoPoint(Integer.parseInt(point[0]), 
								Integer.parseInt(point[1]));
						route.addPoint(p);
						segment.addPoint(p);
					}
					segment.setDistance(Double.parseDouble(distance));
					segment.setLength(Integer.parseInt(length));
					
					
				} else {
					/** Parse route details. **/
					route.setName(nameString);
					route.setLength(Integer.parseInt(length));
					route.setItineraryId(Integer.parseInt(id));
					route.setCopyright(copyright);
					route.setCountry(country);
					route.setPolyline(polyline);
					route.setWarning(warning);
					route.setRouter(router);
					
					final String[] elevations = elev.split(" ", -1);
					for (int i = 0; i < elevations.length; i++) {
						final String[] xy = elevations[i].split(",", -1);
						route.addElevation(Double.parseDouble(xy[1]), Double.parseDouble(xy[0]) * 1000);
					}
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
			Log.e(e.getMessage(), "BikeRoute parser - " + feedUrl);
			return null;
		}
		//route.buildTree();
		return route;
	}
	
	@Override
	public InputStream getInputStream() {
			InputStream fIn = null;
			try {
				fIn = new FileInputStream(path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			return fIn;
	}
}