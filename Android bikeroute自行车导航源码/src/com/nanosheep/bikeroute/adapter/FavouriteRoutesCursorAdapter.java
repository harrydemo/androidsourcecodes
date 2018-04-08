/**
 * 
 */
package com.nanosheep.bikeroute.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import com.nanosheep.bikeroute.R;

/**
 * An adapter for displaying saved route objects.
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
public class FavouriteRoutesCursorAdapter extends SimpleCursorAdapter {
	/** Layout inflater . **/
	private final transient LayoutInflater inflater;
	/** Units. **/
	private String unit;

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public FavouriteRoutesCursorAdapter(final Context context, final int textView) {
		super(context, textView, null, null, null);
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final View view = inflater.inflate(R.layout.direction_item, null);
		
		return view;
	}

}
