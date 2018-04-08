// Created by plusminus on 12:29:23 - 21.09.2008
package org.andnav.osm.contributor.util;

import org.andnav.osm.contributor.util.constants.OpenStreetMapContributorConstants;
import org.andnav.osm.util.GeoPoint;

/**
 * Extends the {@link GeoPoint} with a timeStamp.
 * @author Nicolas Gramlich
 */
public class RecordedGeoPoint extends GeoPoint implements OpenStreetMapContributorConstants {
	
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final long mTimeStamp;
	protected final int mNumSatellites;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RecordedGeoPoint(final int latitudeE6, final int longitudeE6) {
		this(latitudeE6, longitudeE6, System.currentTimeMillis(), NOT_SET);
	}
	
	public RecordedGeoPoint(final int latitudeE6, final int longitudeE6, final long aTimeStamp, final int aNumSatellites) {
		super(latitudeE6, longitudeE6);
		this.mTimeStamp = aTimeStamp;
		this.mNumSatellites = aNumSatellites;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public long getTimeStamp() {
		return this.mTimeStamp;
	}
	
	public double getLatitudeAsDouble(){
		return this.getLatitudeE6() / 1E6;
	}
	
	public double getLongitudeAsDouble(){
		return this.getLongitudeE6() / 1E6;
	}
	
	public int getNumSatellites(){
		return this.mNumSatellites;
	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
