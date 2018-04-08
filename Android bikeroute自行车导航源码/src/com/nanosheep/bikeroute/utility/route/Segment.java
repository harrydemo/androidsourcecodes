package com.nanosheep.bikeroute.utility.route;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Holds a segment of a route - a road name, the points
 * that make it up and the turn to be taken to reach the
 * next segment.
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
public class Segment implements Parcelable{
	/** Points in this segment. **/
	private List<PGeoPoint> points;
	/** Turn instruction to reach next segment. **/
	private String instruction;
	/** Length of segment. **/
	private int length;
	/** Distance covered. **/
	private double distance;
	/** Name (e.g. street name) of the segment. **/
	private String name;

	
	public Segment() {
		points = new ArrayList<PGeoPoint>();
	}
	
	/**
     * Create a segment from a previously parcelled segment.
     * @param in
     */
    public Segment(final Parcel in) {
            readFromParcel(in);
    }
	
	/* (non-Javadoc)
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeString(instruction);
            dest.writeString(name);
            dest.writeInt(length);
            dest.writeTypedList(points);
            dest.writeDouble(distance);
    }
    
    /**
     * Rehydrate a segment from a parcel.
     * @param in The parcel to rehydrate.
     */
    
    public void readFromParcel(final Parcel in) {
            instruction = in.readString();
            name = in.readString();
            length = in.readInt();
            points = in.createTypedArrayList(PGeoPoint.CREATOR);
            distance = in.readDouble();
    }

	/**
	 * Set the turn instruction.
	 * @param turn Turn instruction string.
	 */
	
	public void setInstruction(final String turn) {
		this.instruction = turn;
	}
	
	/**
	 * Get the turn instruction to reach next segment.
	 * @return a String of the turn instruction.
	 */
	
	public String getInstruction() {
		return instruction;
	}
	
	/**
	 * Clear the points out of a segment.
	 */
	
	public void clearPoints() {
		points.clear();
	}
	
	/**
	 * Add a point to this segment.
	 * @param point PGeoPoint to add.
	 */
	
	public void addPoint(final PGeoPoint point) {
		points.add(point);
	}
	
	/** Add a list of points to this segment.
	 * 
	 */
	
	public void addPoints(final List<PGeoPoint> points) {
		this.points.addAll(points);
	}
	
	/** Get the starting point of this 
	 * segment.
	 * @return a PGeoPoint
	 */
	
	public PGeoPoint startPoint() {
		return points.get(0);
	}
	
	public List<PGeoPoint> getPoints() {
		return points;
	}
	
	/** Creates a segment which is a copy of this one.
	 * @return a Segment that is a copy of this one.
	 */
	
	public Segment copy() {
		final Segment copy = new Segment();
		copy.points = new ArrayList<PGeoPoint>(points);
		copy.instruction = instruction;
		copy.name = name;
		copy.length = length;
		copy.distance = distance;
		return copy;
	}
	
	/**
	 * @param length the length to set
	 */
	public void setLength(final int length) {
		this.length = length;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(final double distance) {
		this.distance = distance;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}
	
	@Override
	public boolean equals(Object o) {
        return (o instanceof Segment) && ((Segment) o).getInstruction().equals(instruction)
                && ((Segment) o).getDistance() == distance;
		}
	
	 public static final Parcelable.Creator CREATOR =
	        new Parcelable.Creator() {
	            @Override
				public Segment createFromParcel(final Parcel in) {
	                return new Segment(in);
	            }

	                        @Override
	                        public Segment[] newArray(final int size) {
	                                return new Segment[size];
	                        }
	        };


	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Set the name of this segment.
	 * 
	 * @param nameString
	 */
	public void setName(String nameString) {
		this.name = nameString;
	}
	
	/**
	 * Get the streetname of the segment.
	 * @return the name of the segment, or the instruction if this is a google route.
	 */
	public String getName() {
		return name;
	}
	
	public String toXml() {
		StringBuilder sb = new StringBuilder("<marker type=\"segment\" turn=\"");
		sb.append(getInstruction());
		sb.append("\" name=\"");
		sb.append(getName());
		sb.append("\" length=\"");
		sb.append(getLength());
		sb.append("\" distance=\"");
		sb.append(getDistance());
		sb.append("\" points=\"");
		Iterator<PGeoPoint> it = points.iterator();
		while (it.hasNext()) {
			PGeoPoint p = it.next();
			sb.append(p.getLatitudeE6());
			sb.append(",");
			sb.append(p.getLongitudeE6());
			if (it.hasNext()) {
				sb.append(" ");
			}
		}
		sb.append("\" />");
		return sb.toString();
	}

}
