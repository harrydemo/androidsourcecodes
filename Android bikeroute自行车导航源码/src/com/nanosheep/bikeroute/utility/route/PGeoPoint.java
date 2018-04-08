/**
 * 
 */
package com.nanosheep.bikeroute.utility.route;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import org.osmdroid.util.GeoPoint;

/**
 * Wrapper for GeoPoint to make parcelable
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
 * @version Jan 18, 2011
 */
public class PGeoPoint extends GeoPoint implements Parcelable {

	/**
	 * @param aLocation
	 */
	public PGeoPoint(Location aLocation) {
		super(aLocation);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aGeopoint
	 */
	public PGeoPoint(GeoPoint aGeopoint) {
		super(aGeopoint);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aLatitudeE6
	 * @param aLongitudeE6
	 */
	public PGeoPoint(int aLatitudeE6, int aLongitudeE6) {
		super(aLatitudeE6, aLongitudeE6);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aLatitude
	 * @param aLongitude
	 */
	public PGeoPoint(double aLatitude, double aLongitude) {
		super(aLatitude, aLongitude);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aLatitudeE6
	 * @param aLongitudeE6
	 * @param aAltitude
	 */
	public PGeoPoint(int aLatitudeE6, int aLongitudeE6, int aAltitude) {
		super(aLatitudeE6, aLongitudeE6, aAltitude);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aLatitude
	 * @param aLongitude
	 * @param aAltitude
	 */
	public PGeoPoint(double aLatitude, double aLongitude, double aAltitude) {
		super(aLatitude, aLongitude, aAltitude);
		// TODO Auto-generated constructor stub
	}
	
	// ===========================================================
    // Parcelable
    // ===========================================================
    private PGeoPoint(final Parcel in) {
    	super(in.readInt(), in.readInt(), in.readInt());
    }

    @Override
    public int describeContents() {
            return 0;
    }

    @Override
    public void writeToParcel(final Parcel out, final int flags) {
            out.writeInt(getLatitudeE6());
            out.writeInt(getLongitudeE6());
            out.writeInt(getAltitude());
    }

    public static final Parcelable.Creator<PGeoPoint> CREATOR = new Parcelable.Creator<PGeoPoint>() {
            @Override
            public PGeoPoint createFromParcel(final Parcel in) {
                    return new PGeoPoint(in);
            }

            @Override
            public PGeoPoint[] newArray(final int size) {
                    return new PGeoPoint[size];
            }
    };

}
