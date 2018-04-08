/**
 * 
 */
package com.nanosheep.bikeroute.utility;

import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.Route;
import com.nanosheep.bikeroute.utility.route.Segment;


/**
 * Class for exporting route objects to GPX files.
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
 * @version Jan 8, 2011
 */
public class GPXExporter {
	
	private static final String XMLVER = "<?xml version=\"1.0\"?>";
	private static final String SCHEMA = "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.1\" creator=\"BikeRoute\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">";
	private static final String NAME = "<name>";
	private static final String NAME_CLOSE = "</name>";
	
	private GPXExporter() { }
	
	public void export(Route route) {
		
	}
	
	/**
	 * Generate a GPX string from a route, ready to be output to a file.
	 * @param route the route to GPXify.
	 * @return
	 */
	
	private String makeGPX(Route route) {
		StringBuffer sb = new StringBuffer(XMLVER);
		sb.append(SCHEMA);
		sb.append("<metadata>");
		sb.append(NAME);
		sb.append(route.getName());
		sb.append(NAME_CLOSE);
		sb.append("</metadata>");
		
		sb.append("<rte>");
		
		for (Segment s : route.getSegments()) {
			for (PGeoPoint p : s.getPoints()) {
				sb.append("<rtept lon=\"");
				sb.append(Convert.asDegrees(p.getLongitudeE6()));
				sb.append("\" lat=\"");
				sb.append(Convert.asDegrees(p.getLatitudeE6()));
				sb.append("\">");
				sb.append(NAME);
				sb.append(s.getName());
				sb.append(NAME_CLOSE);
				sb.append("</rtept>");
			}
		}
		sb.append("</rte></gpx>");
		return sb.toString();
	}

}
