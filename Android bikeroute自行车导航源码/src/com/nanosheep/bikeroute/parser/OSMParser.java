/**
 * 
 */
package com.nanosheep.bikeroute.parser;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * @version Jun 26, 2010
 */
public class OSMParser extends XMLParser {

	/**
	 * @param feedUrl
	 */
	public OSMParser(final String feedUrl) {
		super(feedUrl);
	}


	public List<PGeoPoint> parse() {
			final RootElement root = new RootElement("osm");
			final List<PGeoPoint> marks = new ArrayList<PGeoPoint>();
			final Element node = root.getChild("node");
			// Listen for start of tag, get attributes and set them
			// on current marker.
			node.setStartElementListener(new StartElementListener() {
				@Override
				public void start(final Attributes attributes) {
					marks.add(new PGeoPoint(
							Double.parseDouble(attributes.getValue("lat")),
							Double.parseDouble(attributes.getValue("lon"))));
				}

			});
			node.setEndElementListener(new EndElementListener() {
				@Override
				public void end() {
				}
			});
			try {
				Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root
						.getContentHandler());
			} catch (IOException e) {
				Log.e(e.getMessage(), "OSMParser - " + feedUrl);
			} catch (SAXException e) {
				Log.e(e.getMessage(), "OSMParser - " + feedUrl);
			}
			return marks;
	}

}
