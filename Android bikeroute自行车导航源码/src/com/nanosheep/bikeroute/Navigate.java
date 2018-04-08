package com.nanosheep.bikeroute;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.nanosheep.bikeroute.adapter.FindPlaceAdapter;
import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.service.RouteListener;
import com.nanosheep.bikeroute.service.RoutePlannerTask;
import com.nanosheep.bikeroute.utility.AddressDatabase;
import com.nanosheep.bikeroute.utility.Parking;
import com.nanosheep.bikeroute.utility.StringAddress;
import com.nanosheep.bikeroute.utility.contacts.AbstractContactAccessor;
import com.nanosheep.bikeroute.utility.dialog.DialogFactory;
import com.nanosheep.bikeroute.utility.route.Route;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * A class for performing A to B navigation and planning routes.
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
 * @version Oct 3, 2010
 */
public class Navigate extends Activity implements RouteListener {	
	
	/** Start Address box. **/
	private transient AutoCompleteTextView startAddressField;
	/** End address box. **/
	private transient AutoCompleteTextView endAddressField;
	/** Waypoint address box. **/
	private transient AutoCompleteTextView wayAddressField;

	/** Parking manager. */
	private Parking prk;
	
	/** Address db. **/
	private AddressDatabase db;

	/** Is a search running. **/
	public boolean isSearching;

	/** Contact accessor to navigate to contact. **/
	protected AbstractContactAccessor mContactAccessor;
	private Intent searchIntent;
	protected BroadcastReceiver routeReceiver;
	protected BikeRouteApp app;
	private RoutePlannerTask search;

	/**Is planning dialog showing. **/
	private static boolean mShownDialog;
	
	/** Route id generator. **/
	Random random;
	
	/** Layout row for destination. **/
	private View destRow;
	/** Add via button. **/
	private Button addViaBtn;
	/** Remove via button. **/
	private Button remViaBtn;
	
	
	@Override
	public final void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	random = new Random();
	app = ((BikeRouteApp) getApplication());
	mContactAccessor = AbstractContactAccessor.getInstance();
	requestWindowFeature(Window.FEATURE_RIGHT_ICON);
	setContentView(R.layout.findplace);
	setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.ic_bar_bikeroute);
	
	searchIntent = new Intent();
	
	isSearching = false;
	mShownDialog = false;
	
	//DB
	db = app.getDb();
	
	//Parking manager
	prk = new Parking(this);
	
	//Initialise fields
	startAddressField = (AutoCompleteTextView) findViewById(R.id.start_address_input);
	wayAddressField = (AutoCompleteTextView) findViewById(R.id.way_address_input);
	endAddressField = (AutoCompleteTextView) findViewById(R.id.end_address_input);	
	destRow = findViewById(R.id.destRow);
	final Button searchButton = (Button) findViewById(R.id.search_button);
	addViaBtn = (Button) findViewById(R.id.add_button);
	remViaBtn = (Button) findViewById(R.id.remove_button);
	
	//Initialise adapter
	FindPlaceAdapter adapter = new FindPlaceAdapter(this,
			android.R.layout.simple_dropdown_item_1line);
	startAddressField.setAdapter(adapter);
	endAddressField.setAdapter(adapter);
	
	//Initialise buttons
	searchButton.setOnClickListener(new SearchClickListener());
	WaypointClickListener wayListn = new WaypointClickListener();
	addViaBtn.setOnClickListener(wayListn);
	remViaBtn.setOnClickListener(wayListn);
	
	/* Autofill starting location by reverse geocoding current
	 * lat & lng
	 */
	
	//Handle rotations
	final Object[] data = (Object[]) getLastNonConfigurationInstance();
	if (data != null) {
		isSearching = (Boolean) data[2];
		startAddressField.setText((String) data[3]);
		endAddressField.setText((String) data[4]);
		wayAddressField.setText((String) data[6]);
		destRow.setVisibility((Integer) data[7]);
		search = (RoutePlannerTask) data[5];
		if(search != null) {
			search.setListener(this);
		}
	}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		String defaultRouter = Locale.getDefault().equals(Locale.UK) ? BikeRouteConsts.CS : BikeRouteConsts.MQ;
		String router = PreferenceManager.getDefaultSharedPreferences(this).
				getString("router", defaultRouter);
		
		if (!BikeRouteConsts.CS.equals(router)) {
			destRow.setVisibility(View.GONE);
			addViaBtn.setVisibility(View.GONE);
		}
		Thread t = new Thread() {
			@Override
			public void run() {
				//Initialise geocoder
				final Geocoder geocoder = new Geocoder(Navigate.this);
				/* Get current lat & lng if available. */
				final LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				try {
				Location self = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				Location selfNet= lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				
				//Only use GPS if more recent fix 
				if (self != null) {
					self =  (selfNet == null) || (selfNet.getTime() < self.getTime() + 600000) ? self : selfNet;
				}
				
				/* Autofill starting location by reverse geocoding current
				 * lat & lng
				 */
				
				if (self != null) {
					try {
						final Address startAddress = geocoder.getFromLocation(self.getLatitude(),
								self.getLongitude(), 1).get(0);
						startAddressField.setText(StringAddress.asString(startAddress));
					} catch (Exception e) {
						Log.e(e.getMessage(), "FindPlace - location: " + self);
					}
				}
				} catch (IllegalArgumentException e) {
					Log.e("Location Service", "No Location provider.");
				}
			}
		};
		t.run();
	}

	
	/**
	 * Handler for navigation requests from the text boxes.
	 * Initiates a geocode on the addresses, passes that to
	 * the route planner and moves to the routemap once planning is
	 * completed.
	 * @author jono@nanosheep.net
	 * @version Jun 28, 2010
	 */
	
	private class SearchClickListener implements OnClickListener {

		@Override
		public void onClick(final View view) {
			ArrayList<String> addresses = new ArrayList<String>();
			addresses.add(startAddressField.getText().toString());
			addresses.add(wayAddressField.getText().toString());
			if(destRow.getVisibility() == View.VISIBLE && 
					!"".equals(endAddressField.getText().toString())) {
				addresses.add(endAddressField.getText().toString());
			}
			searchIntent.putExtra(RoutePlannerTask.PLAN_TYPE, RoutePlannerTask.ADDRESS_PLAN);
			searchIntent.putStringArrayListExtra(RoutePlannerTask.ADDRESSES, addresses);
			requestRoute();
		}
	}
	
	private class WaypointClickListener implements OnClickListener {

		@Override
		public void onClick(final View view) {
			if(destRow.getVisibility() == View.GONE) {
				destRow.setVisibility(View.VISIBLE);
				addViaBtn.setVisibility(View.INVISIBLE);
			} else {
				destRow.setVisibility(View.GONE);
				addViaBtn.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**
	 * Overridden to deal with rotations which require tracking
	 * displayed dialog to ensure it is not duplicated.
	 */
	
	@Override
	protected void onPrepareDialog(final int id, final Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		if (id == R.id.plan) {
			mShownDialog = true;
		}
	}
	
	/**
	 * Request a route from the planning service, register a receiver to handle it.
	 */
	private void requestRoute() {
		searchIntent.putExtra(RoutePlannerTask.ROUTE_ID, random.nextInt(2147483647));
		showDialog(R.id.plan);
		isSearching = true;
		search = new RoutePlannerTask(this, searchIntent);
		search.execute();
	}

	/**
	 * Creates dialogs for loading, on errors, alerts.
	 * Available dialogs:
	 * Planning progress, planning error.
	 * @return the approriate Dialog object
	 */
	
	@Override
	public Dialog onCreateDialog(final int id) {
		ProgressDialog pDialog;
		Dialog dialog;
		switch(id) {
		case R.id.plan:
			pDialog = new ProgressDialog(this);
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage(getText(R.string.plan_msg));
			pDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(final DialogInterface arg0) {
					removeDialog(R.id.plan);
					if (!Navigate.this.isSearching) {
						mShownDialog = false;
					}
				}
			});
			pDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(final DialogInterface arg0) {
						search.cancel(true);
				}
				
			});
			dialog = pDialog;
			break;
		case R.id.plan_fail:
		case R.id.network_error:
		case R.id.ioerror:
		case R.id.argerror:
		case R.id.reserror:
		case R.id.geocodeerror:
		case R.id.geocodeconnecterror:
			dialog = DialogFactory.getDialog(id, this);
			break;
		case R.id.about:
			dialog = DialogFactory.getAboutDialog(this);
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
	
	/**
	 * Create the options menu.
	 * @return true if menu created.
	 */

	@Override
	public final boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.navigate_menu, menu);
		return true;
	}
	
	@Override
	public final boolean onPrepareOptionsMenu(final Menu menu) {
		final MenuItem steps = menu.findItem(R.id.directions);
		final MenuItem back = menu.findItem(R.id.bike);
		final MenuItem stand  = menu.findItem(R.id.stand);
		steps.setVisible(false);
		if (app.getRoute() != null) {
			steps.setVisible(true);
		}
		back.setVisible(false);
		stand.setVisible(false);
		if (startAddressField.getText().length() > 0) {
			stand.setVisible(true);
			if (prk.isParked()) {
				back.setVisible(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 * Handle option selection.
	 * @return true if option selected.
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case R.id.prefs:
			intent = new Intent(this, Preferences.class);
			startActivityForResult(intent, R.id.trace);
			break;
		case R.id.directions:
			intent = new Intent(this, DirectionsView.class);
			startActivityForResult(intent, R.id.trace);
			break;
		case R.id.map:
			intent = new Intent(this, LiveRouteMap.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivityForResult(intent, R.id.trace);
			break;
		case R.id.bike:
			searchIntent.putExtra(RoutePlannerTask.PLAN_TYPE, RoutePlannerTask.BIKE_PLAN);
			searchIntent.putExtra(RoutePlannerTask.START_ADDRESS, startAddressField.getText().toString());
			requestRoute();
			break;
		case R.id.stand:
			searchIntent.putExtra(RoutePlannerTask.PLAN_TYPE, RoutePlannerTask.STANDS_PLAN);
			searchIntent.putExtra(RoutePlannerTask.START_ADDRESS, startAddressField.getText().toString());
			requestRoute();
			break;
		case R.id.stop_nav:
			finishActivity(R.id.trace);
			setResult(1);
			this.finish();
			app.setRoute(null);
			break;
		case R.id.about:
			showDialog(R.id.about);
			break;
		case R.id.favourites:
			intent = new Intent(this, SavedRoutes.class);
			startActivityForResult(intent, R.id.trace);
			break;
		case R.id.contacts:
			startActivityForResult(mContactAccessor.getPickContactIntent(), 0);
		}
		return true;
	}
	
	/**
	 * Handles callbacks from searchtasks.
	 * Dismisses any shown planning dialog, loads the routemap
	 * if the search was successful, displays an error if not.
	 * @param msg Result status message from search task.
	 * @param route Route returned by the planner, or null.
	 */
	
	@Override
	public void searchComplete(final Integer msg, final Route route) {
		if (msg != null) {
			try {
				dismissDialog(R.id.plan);
			} catch (Exception e) {
				Log.e("Navigate", e.getMessage());
			}
			if (msg == R.id.result_ok) {
				try {
					db.insert(startAddressField.getText().toString());
					if (!"".equals(endAddressField.getText().toString())) {
						db.insert(endAddressField.getText().toString());
					}
					if (!"".equals(wayAddressField.getText().toString())) {
						db.insert(wayAddressField.getText().toString());
					}
				} catch (Exception e) {
					Log.e("ADB", "DB hiccup.");
				}
				final Intent map = new Intent(this, LiveRouteMap.class);
				map.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				map.putExtras(searchIntent);
				app.setRoute(route);
				startActivityForResult(map, R.id.trace);
			} else {
				showDialog(msg);
			}
		}
	}
	
	/**
   	 * Callback for contact picker activity - requests a string address for the contact
   	 * chosen.
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if ((resultCode == RESULT_OK) && (requestCode == 0)) {
            loadContactAddress(data.getData());
        } else if ((requestCode == R.id.trace) && (resultCode == 1)) {
        	setResult(1);
        	finish();
        }
    }
    
    /**
     * Load contact address on a background thread and set as destination.
     */
    private void loadContactAddress(final Uri contactUri) {
        final AsyncTask<Uri, Void, String> task = new AsyncTask<Uri, Void, String>() {

            @Override
            protected String doInBackground(Uri... uris) {
                return mContactAccessor.loadAddress(getContentResolver(), uris[0]);
            }

            @Override
            protected void onPostExecute(final String result) {
            	endAddressField.setText(result);
            }
        };

        task.execute(contactUri);
    }
	
	/**
	 * Overridden to preserve searchtask, dialog & textfields on rotation.
	 */
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		Object[] objs = new Object[8];
		objs[1] = mShownDialog;
		objs[2] = isSearching;
		objs[3] = startAddressField.getText().toString();
		objs[4] = endAddressField.getText().toString();
		objs[5] = search;
		objs[6] = wayAddressField.getText().toString();
		objs[7] = destRow.getVisibility();
	    return objs;
	}

	/* (non-Javadoc)
	 * @see com.nanosheep.bikeroute.service.RouteListener#searchCancelled()
	 */
	@Override
	public void searchCancelled() {
		isSearching = false;
		search = null;
	}

	/* (non-Javadoc)
	 * @see com.nanosheep.bikeroute.service.RouteListener#getContext()
	 */
	@Override
	public Context getContext() {
		return this;
	}

}
