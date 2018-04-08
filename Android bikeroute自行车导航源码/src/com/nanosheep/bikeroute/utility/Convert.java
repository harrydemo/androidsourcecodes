/**
 * 
 */
package com.nanosheep.bikeroute.utility;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;

/**
 * Utility class for converting units.
 * Converts meters to feet, km to miles, distances to distance strings
 *  and degrees to/from microdegrees.
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
 * @version Jul 5, 2010
 */
public final class Convert {
	/** Multiplier for km to miles. **/
	private static final double MILES_CONVERT = 0.621371192;
	/** Multiplier for meters to feet. **/
	private static final double FEET_CONVERT = 3.2808399;
	/** Conversion factor for degrees/mdegrees. **/
	public static final double CNV = 1E6;
	
	private Convert() { }
	
	/**
	 * Convert a number of kilometers to miles.
	 * @param km number of km to convert
	 * @return number of km in miles
	 */
	
	public static double asMiles(final double km) {
		return round2(km * MILES_CONVERT);
	}
	
	public static double asMiles(final int meters) {
		return meters > 0 ? round2(asMiles(meters / 1000.0)) : 0;
	}
	
	/**
	 * Convert a number of meters to feet.
	 * @param meters Value to convert in meters.
	 * @return meters converted to feet.
	 */
	
	public static double asFeet(final double meters) {
		return meters > 0 ? round2(meters * FEET_CONVERT) : 0;
	}
	
	public static String asFeetString(final double meters) {
		return round2(meters * FEET_CONVERT) + "ft";
	}
	
	public static String asMilesString(final double km) {
		return asMiles(km) + "m";
	}
	
	public static String asMilesString(final int meters) {
		return asMiles(meters) + "m";
	}
	
	public static String asKilometerString(final double km) {
		return km + "km";
	}
	
	public static String asKilometerString(final int meters) {
		return meters > 0 ? meters/1000.00 + "km": "0km";
	}
	
	public static String asMeterString(final int meters) {
		return meters + "m";
	}
	
	public static XYMultipleSeriesDataset asImperial(final XYMultipleSeriesDataset input) {
		final XYMultipleSeriesDataset output = new XYMultipleSeriesDataset();
		final XYSeries metric = input.getSeriesAt(0);
		final XYSeries imperial = new XYSeries(metric.getTitle());
		for (int i = 0; i < metric.getItemCount(); i++) {
			imperial.add(asMiles(metric.getX(i)), asFeet(metric.getY(i)));
		}
		output.addSeries(imperial);
		return output;
	}
	
	/**
	 * Convert degrees to microdegrees.
	 * @param degrees
	 * @return integer microdegrees.
	 */
	
	public static int asMicroDegrees(final double degrees) {
		return (int) (degrees * CNV);
	}
	
	/**
	 * Convert microdegrees to degrees.
	 * @param mDegrees
	 * @return double type degrees.
	 */
	
	public static double asDegrees(final int mDegrees) {
		return mDegrees / CNV;
	}
	
	private static double round2(double num) {
		double result = num * 100;
		result = Math.round(result);
		result = result / 100;
		return result;
		}
	
	
}
