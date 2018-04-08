package com.nanosheep.bikeroute.utility.route;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import com.nanosheep.bikeroute.utility.Convert;
import com.savarese.spatial.GenericPoint;
import com.savarese.spatial.KDTree;
import com.savarese.spatial.NearestNeighbors;
import com.savarese.spatial.NearestNeighbors.Entry;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

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
 * @version Jun 22, 2010
 */
public class Route {
	private String name;
	private final List<PGeoPoint> points;
	private final List<Segment> segments;
	private PGeoPoint waypoint;
	private String copyright;
	private String warning;
	private String country;
	private int length;
	private XYSeries elevations;
	private String polyline;
	private Bundle segmentMap;
	private KDTree<?, GenericPoint<Integer>, PGeoPoint> kd;
	private int id;
	private int itineraryId;
	/** Routing engine that originally produced this route. **/
	private String router;
	
	private static final String XMLVER = "<?xml version=\"1.0\"?>";
	private static final String SCHEMA = "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"1.1\" creator=\"BikeRoute\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">";
	private static final String NAME = "<name>";
	private static final String NAME_CLOSE = "</name>";
	
	public Route() {
		points = new ArrayList<PGeoPoint>();
		segments = new ArrayList<Segment>();
		elevations = new XYSeries("Elevation");
		segmentMap = new Bundle(Segment.class.getClassLoader());
		country = "";
		warning = "";
		polyline = "";
		//kd = new KDTree<PGeoPoint>(2);
		kd = new KDTree();
	}
    
	public String toXml() {
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<markers><marker type=\"route\" copyright=\"");
		sb.append(getCopyright());
		sb.append("\" name=\"");
		sb.append(getName());
		sb.append("\" length=\"");
		sb.append(getLength());
		sb.append("\" warning=\"");
		sb.append(getWarning());
		sb.append("\" polyline=\"");
		sb.append(polyline);
		sb.append("\" country=\"");
		sb.append(country);
		sb.append("\" router=\"");
		sb.append(router);
		sb.append("\" elevations=\"");
		for (int i = 0; i < elevations.getItemCount(); i++) {
			sb.append(elevations.getX(i));
			sb.append(",");
			sb.append(elevations.getY(i));
			if (i < elevations.getItemCount() - 1) {
				sb.append(" ");
			}
		}
		sb.append("\" itineraryid=\"");
		sb.append(itineraryId);
		sb.append("\" />");
		for (Segment s : segments) {
			sb.append(s.toXml());
		}
		sb.append("</markers>");
		return sb.toString();
	}
	
    public void buildTree() {
    	for (PGeoPoint p : points) {
    		try {
    			kd.put(new GenericPoint(p.getLatitudeE6(), p.getLongitudeE6()), p);
			} catch (Exception e) {
				Log.e("KD", e.getMessage());
			}
    	}
    }

	public void addPoint(final PGeoPoint p) {
		points.add(p);
	}
	
	public void addPoints(final List<PGeoPoint> points) {
		this.points.addAll(points);
	}
	
	public List<PGeoPoint> getPoints() {
		return points;
	}
	
	/**
	 * Get the nearest neighbouring point on the route to the point given.
	 * @param p
	 * @return
	 */
	
	public PGeoPoint nearest(final PGeoPoint p) {
		NearestNeighbors nn = new NearestNeighbors();
		return (PGeoPoint) nn.get(kd, new GenericPoint(p.getLatitudeE6(), p.getLongitudeE6()), 1)[0].getNeighbor().getValue();
	}
	
	/**
	 * Get the k nearest neighbouring points on the route.
	 * @param p
	 * @param k
	 * @return
	 */
	
	public List<PGeoPoint> nearest(final PGeoPoint p, final int k) {
		NearestNeighbors nn = new NearestNeighbors();
		Entry [] neighbours = nn.get(kd, new GenericPoint(p.getLatitudeE6(), p.getLongitudeE6()), k);
		List<PGeoPoint> neighbourList = new ArrayList<PGeoPoint>(neighbours.length);
		
		for (int i = 0; i < neighbours.length; i++) {
			neighbourList.add((PGeoPoint) neighbours[i].getNeighbor().getValue());
		}
		
		return neighbourList;
	}
	
	public void addSegment(final Segment s) {
		segments.add(s);
		for (PGeoPoint p : s.getPoints()) {
			segmentMap.putParcelable(p.toString(), s);
		}
	}
	
	public List<Segment> getSegments() {
		return segments;
	}
	
	/**
	 * Get the segment this point belongs to.
	 * @param point
	 * @return a Segment
	 */
	
	public Segment getSegment(final PGeoPoint point) {
		return segmentMap.getParcelable(point.toString());
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param string the copyright to set
	 */
	public void setCopyright(String string) {
		this.copyright = string;
	}

	/**
	 * @return the copyright string id
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}

	/**
	 * @return the warning
	 */
	public String getWarning() {
		return warning;
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
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Get the last point in the route.
	 * @return  Destination as a PGeoPoint
	 */
	
	public PGeoPoint getEndPoint() {
		return points.get(points.size() - 1);
	}
	
	/**
	 * Get the elevations as a set of series' that can be displayed by the
	 * achart lib.
	 * @return an XYMultipleSeriesDataset that contains the elevation/distance series.
	 */
	
	public XYMultipleSeriesDataset getElevations() {
		XYMultipleSeriesDataset elevationSet = new XYMultipleSeriesDataset();
		elevationSet.addSeries(elevations);
	    return elevationSet;
	}
	
	
	/**
	 * Get the xyseries that holds the elevation/distance series.
	 * @return
	 */
	public XYSeries getElevationSeries() {
		return elevations;
	}
	
	/**
	 * An an elevation and distance (in metres) to the elevation series for
	 * this route.
	 * @param elevation in metres.
	 * @param dist in metres.
	 */
	
	public void addElevation(final double elevation, final double dist) {
		elevations.add(dist / 1000, elevation);
	}
	
	/**
	 * Get a renderer for drawing the elevation chart.
	 * @return an XYMultipleSeriesRenderer configured for metric.
	 */
	
	public XYMultipleSeriesRenderer getChartRenderer() {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    XYSeriesRenderer r = new XYSeriesRenderer();
	    r.setColor(Color.BLUE);
	    r.setPointStyle(PointStyle.POINT);
	    r.setFillBelowLine(true);
	    r.setFillBelowLineColor(Color.GREEN);
	    r.setFillPoints(true);
	    renderer.addSeriesRenderer(r);
	    renderer.setAxesColor(Color.DKGRAY);
	    renderer.setLabelsColor(Color.LTGRAY);
	    renderer.setYTitle("m");
	    renderer.setXTitle("km");
	    renderer.setYAxisMax(elevations.getMaxY() + 200);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
	    return renderer;
	  }

	/**
	 * @param polyline the polyline to set
	 */
	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}

	/**
	 * @return the polyline
	 */
	public String getPolyline() {
		return polyline;
	}

	/**
	 * @param intExtra
	 */
	public void setRouteId(int routeId) {
		id = routeId;
	}
	
	public int getRouteId() {
		return id;
	}

	/**
	 * @param itineraryId the itineraryId to set
	 */
	public void setItineraryId(int itineraryId) {
		this.itineraryId = itineraryId;
	}

	/**
	 * @return the itineraryId
	 */
	public int getItineraryId() {
		return itineraryId;
	}
	
	/**
	 * Generate a GPX string from a route, ready to be output to a file.
	 * @param route the route to GPXify.
	 * @return
	 */
	
	public String toGPX() {
		StringBuffer sb = new StringBuffer(XMLVER);
		sb.append(SCHEMA);
		sb.append("<metadata>");
		sb.append(NAME);
		sb.append(name);
		sb.append(NAME_CLOSE);
		sb.append("</metadata>\n");
		
		sb.append("<rte>\n");
		
		for (Segment s :segments) {
			for (PGeoPoint p : s.getPoints()) {
				sb.append("<rtept lon=\"");
				sb.append(Convert.asDegrees(p.getLongitudeE6()));
				sb.append("\" lat=\"");
				sb.append(Convert.asDegrees(p.getLatitudeE6()));
				sb.append("\">");
				sb.append(NAME);
				sb.append(s.getName());
				sb.append(NAME_CLOSE);
				sb.append("</rtept>\n");
			}
		}
		sb.append("</rte></gpx>");
		return sb.toString();
	}

	/**
	 * @param router the router to set
	 */
	public void setRouter(String router) {
		this.router = router;
	}

	/**
	 * @return the router
	 */
	public String getRouter() {
		return router;
	}

	public void setWaypoint(PGeoPoint waypoint) {
		this.waypoint = waypoint;
	}

	public PGeoPoint getWaypoint() {
		return waypoint;
	}
}
