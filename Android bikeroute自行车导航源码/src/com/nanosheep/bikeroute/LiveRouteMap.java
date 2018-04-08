/**
 * 
 */
package com.nanosheep.bikeroute;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.service.NavigationService;
import com.nanosheep.bikeroute.service.RouteListener;
import com.nanosheep.bikeroute.service.RoutePlannerTask;
import com.nanosheep.bikeroute.utility.dialog.DialogFactory;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.Route;
import com.nanosheep.bikeroute.utility.route.Segment;

import java.util.ListIterator;
import java.util.Random;

/**
 * Extends RouteMap providing live/satnav features - turn guidance advancing with location,
 * route replanning.
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
 * @version Oct 4, 2010
 */
public class LiveRouteMap extends SpeechRouteMap implements RouteListener {
	/** Intent for replanning searches. **/
	protected Intent searchIntent;
	/** Planning dialog tracker. **/
	protected boolean mShownDialog;
	
	/** Search task for replanning. **/
	private RoutePlannerTask search;
	
	/** Live navigation toggle. **/
	private boolean liveNavigation;
	
	/** Last segment visited. **/
	private Segment lastSegment;
	/** Spoken for this segment. **/
	private boolean spoken;
	/** Arrived at destination. **/
	private boolean arrived;
	/** Navigation service. **/
	private NavigationService mBoundService;
	/** Receiver for navigation updates. **/
	private NavigationReceiver mBroadcastReceiver = new NavigationReceiver();
	/** Connection to navigation service. **/
	private ServiceConnection mConnection = new ServiceConnection() {
	    @Override
		public void onServiceConnected(ComponentName className, IBinder service) {
	        mBoundService = ((NavigationService.LocalBinder)service).getService();
	    }

	    @Override
		public void onServiceDisconnected(ComponentName className) {
	        mBoundService = null;
	    }
	};
	/** Are we bound to navigation service? **/
	private boolean mIsBound;
	
	@Override
	public void onCreate(final Bundle savedState) {
		super.onCreate(savedState);
		
		loadRouteFile();
		
		//Handle rotations
		final Object[] data = (Object[]) getLastNonConfigurationInstance();
		arrived = false;
		if (data != null) {
			isSearching = (Boolean) data[2];
			search = (RoutePlannerTask) data[3];
			if(search != null) {
				search.setListener(this);
			}
			spoken = (Boolean) data[4];
			arrived = (Boolean) data[1];
		}
		registerReceiver(mBroadcastReceiver, 
				new IntentFilter(getString(R.string.navigation_intent)));
	}
	
	@Override
	public void onNewIntent(final Intent intent) {
		super.onNewIntent(intent);
		loadRouteFile();
	}
	
	/**
	 * Load a route from a file if given one in launching intent.
	 */
	
	private void loadRouteFile() {
		Uri uri = getIntent().getData();
		if (uri != null) {
			searchIntent = new Intent();
			searchIntent.putExtra(RoutePlannerTask.ROUTE_ID, (new Random()).nextInt(2147483647));
			searchIntent.putExtra(RoutePlannerTask.PLAN_TYPE, RoutePlannerTask.FILE_PLAN);
			searchIntent.putExtra(RoutePlannerTask.FILE, uri.getPath());
			LiveRouteMap.this.search = new RoutePlannerTask(LiveRouteMap.this, searchIntent);
			LiveRouteMap.this.search.execute();
			//Set the launching intent to one without file data now route is loaded.
			setIntent(getIntent().setData(null));
		}
	}
	
	/**
	 * Retain any state if the screen is rotated.
	 */
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		Object[] objs = new Object[5];
		objs[0] = directionsVisible;
		objs[1] = arrived;
		objs[2] = isSearching;
		objs[3] = search;
		objs[4] = spoken;
	    return objs;
	}
	
	@Override
	public final boolean onPrepareOptionsMenu(final Menu menu) {
		final MenuItem replan = menu.findItem(R.id.replan);
		final MenuItem stopService = menu.findItem(R.id.stop_nav);
		if (app.getRoute() != null) {
			replan.setVisible(true);
		}
		if (mIsBound) {
			stopService.setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void showStep() {
		super.showStep();
		if(mSettings.getBoolean("gps", false)) {
			mLocationOverlay.enableFollowLocation();
		} else {
			mLocationOverlay.disableFollowLocation();
		}
	}
	
	@Override
	public void hideStep() {
		super.hideStep();
		mLocationOverlay.disableFollowLocation();
	}
	
	/**
	 * Fire a replanning request (current location -> last point of route.)
	 * to the routing service, display a dialog while in progress.
	 */
	
	private void replan() {
		isSearching = true;
		try {
			dismissDialog(R.id.plan_fail);
		} catch (Exception e) {
			Log.e("Replanner", "Fail dialog not shown!");
		}
		showDialog(R.id.plan);

		Location self = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if (self == null) {
			self = mLocationOverlay.getLastFix();
		}
		if (self == null) {
			self = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (self != null) {
			searchIntent = new Intent();
			searchIntent.putExtra(RoutePlannerTask.ROUTE_ID, app.getRoute().getRouteId());
			searchIntent.putExtra(RoutePlannerTask.PLAN_TYPE, RoutePlannerTask.REPLAN_PLAN);
			searchIntent.putExtra(RoutePlannerTask.START_LOCATION, self);
			searchIntent.putExtra(RoutePlannerTask.END_POINT,
					(Parcelable) app.getRoute().getPoints().get(app.getRoute().getPoints().size() - 1));
			LiveRouteMap.this.search = new RoutePlannerTask(LiveRouteMap.this, searchIntent);
			LiveRouteMap.this.search.execute();
		} else {
			dismissDialog(R.id.plan);
			showDialog(R.id.plan_fail);
		}
			
	}
	

	@Override
	public Dialog onCreateDialog(final int id) {
		Dialog dialog;
		AlertDialog.Builder builder;
		switch(id) {
		case R.id.gps:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.gps_msg);
			builder.setCancelable(false);
			builder.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int id) {
						showGpsOptions();
					}
				});
			builder.setTitle(R.string.gps_msg_title);
			dialog = builder.create();
			break;
		case R.id.plan:
			ProgressDialog pDialog = new ProgressDialog(this);
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage(getText(R.string.plan_msg));
			pDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(final DialogInterface arg0) {
					removeDialog(R.id.plan);
				}
				});
			pDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(final DialogInterface arg0) {
					if (search != null) {
						search.cancel(true);
					}
					isSearching = false;
				}
			
			});
			dialog = pDialog;
			break;
		case R.id.plan_fail:
			dialog = DialogFactory.getDialog(id, this);
			break;
		case R.id.network_error:
			dialog = DialogFactory.getDialog(id, this);
			break;
		default:
			dialog = super.onCreateDialog(id);
		}
		return dialog;
	}
	
	/**
	 * Handle option selection.
	 * @return true if option selected.
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.replan:
			if (!mIsBound && liveNavigation) {
				doBindService();
			}
			replan();
			break;
		case R.id.stop_nav:
			doUnbindService();
			finishActivity(R.id.trace);
			setResult(1);
			this.finish();
			app.setRoute(null);
			break;
		case R.id.turnbyturn:
			spoken = true;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	/**
	 * Bind to navigation service.
	 */
	
	private void doBindService() {
		if (!mIsBound) {
			bindService(new Intent(LiveRouteMap.this, NavigationService.class), mConnection, Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}
	}

	/**
	 * Unbind from navigation service.
	 */
	
	private void doUnbindService() {
	    if (mIsBound) {
	        // Detach our existing connection.
	        unbindService(mConnection);
	        mIsBound = false;
	    }
	}

	/**
	 * Unregister navigation receiver and unbind from service.
	 */
	
	@Override
	public void onDestroy() {
		doUnbindService();
	    unregisterReceiver(mBroadcastReceiver);
	    super.onDestroy();
	}
	
	/**
	 * Update settings for gps, bind nav service if appropriate
	 * and speak segment if osd enabled.
	 */
	
	@Override
	public void onResume() {
		super.onResume();
		startNavigation();
	}
	
	/**
	 * Unbind & rebind nav service if needed, start up navigation if we have
	 * a route.
	 */
	
	private void startNavigation() {
		doUnbindService();
		liveNavigation = mSettings.getBoolean("gps", false);
		if (app.getRoute() != null) {
			//Disable live navigation for Google routes to comply with Google tos
			liveNavigation = app.getRoute().getRouter().equals(BikeRouteConsts.G) ? false : liveNavigation;
			if (tts && directionsVisible && !isSearching) {
				speak(app.getSegment());
				lastSegment = app.getSegment();
			}
			if (liveNavigation) {
				if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					showDialog(R.id.gps);
				}
				doBindService();
			}
		}
	}
	
	
	/**
	 * Receiver for updates from the live navigation service.
	 * @author jono@nanosheep.net
	 * @version Nov 4, 2010
	 */
	
	private class NavigationReceiver extends BroadcastReceiver {

		/* (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			if (liveNavigation && directionsVisible && !arrived && !isSearching) {
				if (intent.getBooleanExtra(getString(R.string.replan), false)) {
					isSearching = true;
					replan();
				} else if (intent.getBooleanExtra(getString(R.string.arrived), false)) {
					arrive();
					spoken = true;
				} else if (app.getRoute() != null) {
					PGeoPoint current = (PGeoPoint) intent.getExtras().get(getString(R.string.point));
					if (!app.getSegment().equals(lastSegment)) {
						lastSegment = app.getSegment();
						spoken = false;
					}
					
					//Get next point
					ListIterator<PGeoPoint> it = app.getRoute().getPoints().listIterator(
							app.getRoute().getPoints().indexOf(current) + 1);
					PGeoPoint next = it.hasNext() ? it.next() : current;
					
					//Speak directions if the next point is a new segment
					//and have not spoken already
					if (!spoken && !app.getSegment().equals(app.getRoute().getSegment(next)) && tts) {
							speak(app.getRoute().getSegment(next));
							spoken = true;
					}
					showStep();
					traverse(current);
				}
			}
		}
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

	/* (non-Javadoc)
	 * @see com.nanosheep.bikeroute.service.RouteListener#searchComplete(java.lang.Integer, com.nanosheep.bikeroute.utility.Route)
	 */
	@Override
	public void searchComplete(Integer msg, Route route) {
		try {
			dismissDialog(R.id.plan);
		} catch (Exception e)  {
			
		}
		
		isSearching = false;
		if (msg != null) {
			
			if (msg == R.id.result_ok) {
				doUnbindService();
				app.setRoute(route);
				app.setSegment(app.getRoute().getSegments().get(0));
				//viewRoute();
				mOsmv.getController().setCenter(app.getSegment().startPoint());
				traverse(app.getSegment().startPoint());
				arrived = false;
				if (directionsVisible) {
					showStep();
					if (tts) {
						speak(app.getSegment());
						spoken = true;
					}
				}
				if(liveNavigation) {
					doBindService();
				}
			} else {
				showDialog(msg);
			}
		}
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
	
	/**
	 * Arrive at a destination.
	 */
	
	private void arrive() {
		doUnbindService();
		arrived = true;
		app.setSegment(app.getRoute().getSegment(app.getRoute().getEndPoint()));
		traverse(app.getRoute().getEndPoint());
		showStep();
		if (tts) {
			directionsTts.speak(getString(R.string.arrived_speech), TextToSpeech.QUEUE_ADD, null);
		}
	}
	
	
	/** Show GPS options if GPS provider is disabled.
	 * 
	 */
	
	private void showGpsOptions() { 
        startActivity(new Intent(  
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)); 
	}
}
