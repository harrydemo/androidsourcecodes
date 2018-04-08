/**
 * 
 */
package com.nanosheep.bikeroute.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.nanosheep.bikeroute.BikeRouteApp;
import com.nanosheep.bikeroute.R;
import com.nanosheep.bikeroute.utility.Convert;
import com.nanosheep.bikeroute.utility.route.Segment;

/**
 * An adapter for displaying Segment objects as a set of directions.
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
 * @version Jun 28, 2010
 */
public class DirectionListAdapter extends ArrayAdapter<Segment> {
	/** Layout inflater . **/
	private final transient LayoutInflater inflater;
	/** Units. **/
	private String unit;

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public DirectionListAdapter(final Context context, final int textView) {
		super(context, textView);
		inflater = LayoutInflater.from(context);
		this.populate();
	}
	
	/**
	 * Populate the adapter with the current segment list, clearing anything already there.
	 */
	public void populate() {
		clear();
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
		unit = settings.getString("unitsPref", "km");
		BikeRouteApp app = (BikeRouteApp) getContext().getApplicationContext();
		for (Segment s : app.getRoute().getSegments()) {
			add(s);
		}
	}

	/**
	 * Get a view which displays an instruction with road name, a distance
	 * for that segment and total distance covered below, with an icon on
	 * the righthand side.
	 */
	
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final Segment segment = getItem(position);
		final View view = inflater.inflate(R.layout.direction_item, null);
		final TextView turn = (TextView) view.findViewById(R.id.turn);
		final TextView distance = (TextView) view.findViewById(R.id.distance);
		final TextView length = (TextView) view.findViewById(R.id.length);
		
		turn.setText(Html.fromHtml(segment.getInstruction()), TextView.BufferType.SPANNABLE);
		if ("km".equals(unit)) {
			length.setText(Convert.asMeterString(segment.getLength()));
			distance.setText("(" + Convert.asKilometerString(segment.getLength()) + ")");
			
		} else {
			length.setText(Convert.asFeetString(segment.getLength()));
			distance.setText("(" + Convert.asMilesString(segment.getLength()) + ")");
		}
		
		return view;
	}

}
