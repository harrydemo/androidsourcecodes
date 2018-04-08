/**
 * 
 */
package com.nanosheep.bikeroute;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.widget.ListView;
import android.widget.TextView;
import com.nanosheep.bikeroute.adapter.DirectionListAdapter;
import com.nanosheep.bikeroute.utility.Convert;
import com.nanosheep.bikeroute.utility.dialog.DialogFactory;
import com.nanosheep.bikeroute.utility.route.Route;
import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

/**
 * A class for displaying a list of directions.
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
 * @version Jun 24, 2010
 */
public class DirectionsView extends ListActivity {
	/** Route object. **/
	private Route route;
	/** Units. **/
	private String unit;
	private TextView header;
	private TextView footer;
	private BikeRouteApp app;
	
	@Override
	public void onCreate(final Bundle in) {
		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
		setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.ic_bar_bikeroute);
		super.onCreate(in);
		//Create a header to display route distance
		header = new TextView(this);
		getListView().addHeaderView(header, "", false);
		//Create a footer to display warnings & copyrights
		footer = new TextView(this);
		getListView().addFooterView(footer, "", false);
		//Add the list of directions and set it filterable
		setListAdapter(new DirectionListAdapter(this, R.layout.direction_item));
		getListView().setTextFilterEnabled(true);
		app = (BikeRouteApp) getApplication();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		unit = settings.getString(getString(R.string.prefs_units), getString(R.string.km));

		route = app.getRoute();
		
		setTitle(route.getName());
	  
		//Create a header for the list.
		StringBuffer sBuf = new StringBuffer("Total distance: ");
		if (getString(R.string.km).equals(unit)) {
			sBuf.append(Convert.asMeterString(route.getLength()));
			sBuf.append(" (");
			sBuf.append(Convert.asKilometerString(route.getLength()));
			sBuf.append(')');
		} else {
			sBuf.append(Convert.asFeetString(route.getLength()));
			sBuf.append(" (");
			sBuf.append(Convert.asMilesString(route.getLength()));
			sBuf.append(')');
		}
		header.setText(sBuf.toString());
		
		sBuf = new StringBuffer();
		if (route.getWarning() != null) {
			sBuf.append(route.getWarning());
			sBuf.append('\n');
		}
		if (route.getCopyright() != null) {
			sBuf.append(route.getCopyright());
		}
		footer.setText(Html.fromHtml(sBuf.toString()), TextView.BufferType.SPANNABLE);
		footer.setMovementMethod(LinkMovementMethod.getInstance());
		((DirectionListAdapter) getListAdapter()).populate();
	}
	
	/**
	 * Return to the navigation activity if a direction item is
	 * clicked, focus the map there.
	 */
	
	@Override
	protected void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		app.setSegment(route.getSegments().get(position -1));
		final Intent intent = new Intent(this, LiveRouteMap.class);

		intent.putExtra(getString(R.string.jump_intent), true);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivityForResult(intent, R.id.trace);
	}	
	
	/**
	 * Create the options menu.
	 * @return true if menu created.
	 */

	@Override
	public final boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.directions_menu, menu);
		return true;
	}
	
	/*@Override
	public final boolean onPrepareOptionsMenu(final Menu menu) {
		if (app.getRoute().getRouter().equals(BikeRouteConsts.CS)) {
			menu.setGroupVisible(R.id.cyclestreets, true);
		}
		return true;
	}*/
	
	/**
	 * Handle option selection.
	 * @return true if option selected.
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intentDir = null;
		switch (item.getItemId()) {
		case R.id.navigate:
			intentDir = new Intent(this, Navigate.class);
			break;
		case R.id.share:
			Intent target = new Intent(Intent.ACTION_SEND);
			target.putExtra(Intent.EXTRA_TEXT, getString(R.string.cs_jump) + app.getRoute().getItineraryId());
			target.setType("text/plain");
			intentDir = Intent.createChooser(target, getString(R.string.share_chooser_title));
			break;
		case R.id.feedback:
			intentDir = new Intent(this, Feedback.class);
			break;
		case R.id.map:
			intentDir = new Intent(this, LiveRouteMap.class);
			intentDir.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			break;
		case R.id.prefs:
			intentDir = new Intent(this, Preferences.class);
			break;
		case R.id.stop_nav:
			finishActivity(R.id.trace);
			setResult(1);
			this.finish();
			break;
		case R.id.about:
			showDialog(R.id.about);
			return true;
		case R.id.elevation:
			XYMultipleSeriesDataset elevation = route.getElevations();
			final XYMultipleSeriesRenderer renderer = route.getChartRenderer();
			if (!getString(R.string.km).equals(unit)) {
				elevation = Convert.asImperial(elevation);
				renderer.setYTitle(getString(R.string.ft));
			    renderer.setXTitle(getString(R.string.meters));
			}
			renderer.setYAxisMax(elevation.getSeriesAt(0).getMaxY() + 200);
			intentDir = ChartFactory.getLineChartIntent(this, elevation, renderer);
		}
		startActivityForResult(intentDir, R.id.trace);
		return true;
	}
	
	/**
	 * Creates dialogs for loading, on errors, alerts.
	 * Available dialogs:
	 * Planning progress, planning error.
	 * @return the approriate Dialog object
	 */
	
	@Override
	public Dialog onCreateDialog(final int id) {
		return DialogFactory.getAboutDialog(this);
	}
	
	/**
   	 * Finish cascade passer.
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if ((requestCode == R.id.trace) && (resultCode == 1)) {
        	setResult(1);
        	finish();
        }
    }

}
