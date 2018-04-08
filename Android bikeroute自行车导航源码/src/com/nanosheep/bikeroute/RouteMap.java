package com.nanosheep.bikeroute;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.service.RoutePlannerTask;
import com.nanosheep.bikeroute.utility.BikeAlert;
import com.nanosheep.bikeroute.utility.Convert;
import com.nanosheep.bikeroute.utility.Parking;
import com.nanosheep.bikeroute.utility.TurnByTurnGestureListener;
import com.nanosheep.bikeroute.utility.dialog.DialogFactory;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.Segment;
import com.nanosheep.bikeroute.view.overlay.LiveMarkers;
import com.nanosheep.bikeroute.view.overlay.RouteOverlay;
import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.acra.ErrorReporter;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.PathOverlay;

import java.io.FileOutputStream;
import java.util.ListIterator;

/**
 * A class for displaying a route map with overlays for directions and
 * nearby bicycle stands.
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
 * 
 */

public class RouteMap extends OpenStreetMapActivity {

	/** Stand markers overlay. */
	private LiveMarkers stands;
	/** Route overlay. **/
	protected PathOverlay routeOverlay;
	/** Travelled route overlay. **/
	protected PathOverlay travelledRouteOverlay;
	/** Location manager. **/
	protected LocationManager mLocationManager;
	
	/* Constants. */
	/** Initial zoom level. */
	private static final int ZOOM = 15;
	protected boolean isSearching = false;

	/** Parking manager. */
	private Parking prk;

	/** Bike alert manager. **/
	private BikeAlert bikeAlert;
	
	/** Dialog display. **/
	private Dialog dialog;
	
	/** Application reference. **/
	protected BikeRouteApp app;
	
	/** Onscreen directions shown. **/
	protected boolean directionsVisible;
	
	/** Gesture detection for the onscreen directions. **/
    private GestureDetector gestureDetector;
	private OnTouchListener gestureListener;
	
	/** Units for directions. **/
	protected String unit;
	
	/** Preferences manager. **/
	protected SharedPreferences mSettings;
	/** Wakelock. **/
	private PowerManager.WakeLock wl;

	@Override
	public void onCreate(final Bundle savedState) {
		super.onCreate(savedState);
		
		/* Get Preferences. */
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		/* Get location manager. */
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		//Set OSD invisible
		directionsVisible = false;
		
		//Get wakelock
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Navigating");
		
		// Initialize map, view & controller
		setContentView(R.layout.main);
		this.mOsmv = (MapView) findViewById(R.id.mapview);
		//this.mOsmv.setResourceProxy(mResourceProxy);
        mOsmv.setTileSource(TileSourceFactory.MAPNIK);
        this.mLocationOverlay = new MyLocationOverlay(this.getApplicationContext(), this.mOsmv, mResourceProxy);
        this.mLocationOverlay.enableCompass();
        this.mOsmv.setBuiltInZoomControls(true);
        this.mOsmv.setMultiTouchControls(true);
        this.mOsmv.getOverlays().add(this.mLocationOverlay);
        this.mOsmv.getOverlays().add(new OSDOverlay(this));
        
        /* Route paths. **/
		routeOverlay = new RouteOverlay(Color.BLUE,this);
		travelledRouteOverlay = new RouteOverlay(Color.GREEN,this);
		mOsmv.getOverlays().add(routeOverlay);
		mOsmv.getOverlays().add(travelledRouteOverlay);
        

        mOsmv.getController().setZoom(mPrefs.getInt(getString(R.string.prefs_zoomlevel), 1));
        mOsmv.scrollTo(mPrefs.getInt(getString(R.string.prefs_scrollx), 0), 
        		mPrefs.getInt(getString(R.string.prefs_scrolly), 0));
		mOsmv.setBuiltInZoomControls(true);
		
		mOsmv.getController().setZoom(ZOOM);
		
		//Directions overlay
		final View overlay = findViewById(R.id.directions_overlay);
		overlay.setVisibility(View.INVISIBLE);
		
		//Get application reference
		app = (BikeRouteApp)getApplication();


		// Initialize stands overlay
		stands = new LiveMarkers(mOsmv, this);

		// Initialize parking manager
		prk = new Parking(this);
		// Initialize bike alert manager
		bikeAlert = new BikeAlert(this);
				
		//Handle rotations
		final Object[] data = (Object[]) getLastNonConfigurationInstance();
		if ((data != null) && ((Boolean) data[0])) {
			mOsmv.getController().setZoom(16);
			showStep();
		}
		
		if (getIntent().getIntExtra(RoutePlannerTask.PLAN_TYPE, RoutePlannerTask.ADDRESS_PLAN) == RoutePlannerTask.BIKE_PLAN) {
			bikeAlert.setBikeAlert(prk.getLocation());
		}
	}
	
	/**
	 * Handle jump intents from directionsview.
	 */
	
	@Override
	public void onNewIntent(final Intent intent) {
		if (intent.getBooleanExtra(getString(R.string.jump_intent), false) && app.getRoute() != null) {
			showStep();
			traverse(app.getSegment().startPoint());
			mOsmv.getController().setCenter(app.getSegment().startPoint());
		}
		if (intent.getIntExtra(RoutePlannerTask.PLAN_TYPE, RoutePlannerTask.ADDRESS_PLAN) == RoutePlannerTask.BIKE_PLAN) {
			bikeAlert.setBikeAlert(prk.getLocation());
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		/* Units preferences. */
		unit = mSettings.getString("unitsPref", "km");
		this.mLocationOverlay.enableMyLocation();
        this.mLocationOverlay.disableFollowLocation();
        this.mLocationOverlay.enableCompass();
        mOsmv.setTileSource(TileSourceFactory.getTileSource(mSettings.getString("tilePref", "Mapnik")));
        
        if(app.getRoute() != null) {
        	ErrorReporter.getInstance().putCustomData("Route", app.getRoute().getName());
        	ErrorReporter.getInstance().putCustomData("Router", app.getRoute().getRouter());
        	ErrorReporter.getInstance().putCustomData("Route Length", app.getRoute().getSegments().toString());
        	traverse(app.getSegment().startPoint());
    		mOsmv.getController().setCenter(app.getSegment().startPoint());
    		if(mSettings.getBoolean("keepAwake", false)) {
    			wl.acquire();
    		}
        }
	}
	
	@Override
	public void onPause() {
		super.onPause();
		this.mLocationOverlay.disableMyLocation();
		this.mLocationOverlay.disableCompass();
		if (wl.isHeld()) {
			wl.release();
		}
	}
	
	/**
	 * Draw the route to the map.
	 */
	@Deprecated
	protected void viewRoute() {
		if (routeOverlay == null) {
			routeOverlay = new RouteOverlay(Color.BLUE,this);
			travelledRouteOverlay = new RouteOverlay(Color.GREEN,this);
			for(PGeoPoint pt : app.getRoute().getPoints()) {
				routeOverlay.addPoint(pt);
			}
			mOsmv.getOverlays().add(routeOverlay);
			mOsmv.getOverlays().add(travelledRouteOverlay);
		}
		mOsmv.invalidate();
	}
	
	/**
	 * Creates dialogs for loading, on errors, alerts.
	 * Available dialogs:
	 * Planning progress, planning error, unpark.
	 * @return the appropriate Dialog object
	 */
	
	@Override
	public Dialog onCreateDialog(final int id) {
		AlertDialog.Builder builder;
		ProgressDialog pDialog;
		switch(id) {
		case R.id.unpark:
			builder = new AlertDialog.Builder(this);
			builder.setMessage("Reached bike. Unpark?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									prk.unPark();
									RouteMap.this.hideStep();
									bikeAlert.unsetAlert();
									dialog.dismiss();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									dialog.cancel();
								}
							});
			dialog = builder.create();
			break;
		case R.id.awaiting_fix:
			pDialog = new ProgressDialog(this);
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage(getText(R.string.fix_msg));
			pDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface arg0) {
					RouteMap.this.removeDialog(R.id.awaiting_fix);
				}
			});
			dialog = pDialog;
			break;
		case R.id.about:
			dialog = DialogFactory.getAboutDialog(this);
			break;
		case R.id.save:
			dialog = DialogFactory.getSaveDialog(this, app.getRoute());
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
		inflater.inflate(R.menu.map_menu, menu);
		return true;
	}

	/**
	 * Prepare the menu. Set parking related menus to reflect parked or unparked
	 * state, set directions menu & turnbyturn visible only if a route has been planned.
	 * @return a boolean indicating super's state.
	 */

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		final MenuItem park = menu.findItem(R.id.park);
		final MenuItem unPark = menu.findItem(R.id.unpark);
		final MenuItem steps = menu.findItem(R.id.directions);
		final MenuItem turnByTurn = menu.findItem(R.id.turnbyturn);
		final MenuItem map = menu.findItem(R.id.map);
		final MenuItem elev = menu.findItem(R.id.elevation);
		final MenuItem share = menu.findItem(R.id.sharing);
		final MenuItem csShare = menu.findItem(R.id.share);
		final MenuItem save = menu.findItem(R.id.save);
		if (prk.isParked()) {
			park.setVisible(false);
			unPark.setVisible(true);
		} else {
			park.setVisible(true);
			unPark.setVisible(false);
		}
		if (app.getRoute() != null) {
			save.setVisible(true);
			steps.setVisible(true);
			elev.setVisible(true);
			share.setVisible(true);
			if (directionsVisible) {
				turnByTurn.setVisible(false);
				map.setVisible(true);
			} else {
				turnByTurn.setVisible(true);
				map.setVisible(false);
			}
			if (app.getRoute().getRouter().equals(BikeRouteConsts.CS)) {
				csShare.setVisible(true);
				menu.setGroupVisible(R.id.cyclestreets, true);
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
		switch (item.getItemId()) {
		case R.id.share:
			Intent target = new Intent(Intent.ACTION_SEND);
			target.putExtra(Intent.EXTRA_TEXT, getString(R.string.cs_jump) + app.getRoute().getItineraryId());
			target.setType("text/plain");
			intent = Intent.createChooser(target, getString(R.string.share_chooser_title));
			startActivity(intent);
			break;
		case R.id.feedback:
			intent = new Intent(this, Feedback.class);
			startActivity(intent);
			break;
		case R.id.prefs:
			intent = new Intent(this, Preferences.class);
			startActivityForResult(intent, R.id.trace);
			break;
		case R.id.unpark:
			prk.unPark();
			break;
		case R.id.center:
			RouteMap.this.mLocationOverlay.followLocation(true);
			break;
		case R.id.showstands:
			Toast.makeText(this, "Getting stands from OpenStreetMap..",
					Toast.LENGTH_LONG).show();
			stands.refresh(mOsmv.getMapCenter());
			return true;
		case R.id.export:
			String xml = app.getRoute().toXml();
			export(xml, R.string.filename);
			break;
		case R.id.export_gpx:
			String gpx = app.getRoute().toGPX();
			export(gpx, R.string.filename_gpx);
			break;
		case R.id.park:
			showDialog(R.id.awaiting_fix);
			RouteMap.this.mLocationOverlay.runOnFirstFix(new Runnable() {
				@Override
				public void run() {
					if (RouteMap.this.dialog.isShowing()) {
							RouteMap.this.dismissDialog(R.id.awaiting_fix);
							Location self = mLocationOverlay.getLastFix();
							
							if (self == null) {
								self = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							}
							if (self == null) {
								self = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							}
							if (self != null) {
								prk.park(
									new PGeoPoint(self.getLatitude(), self.getLongitude()));
							}
					}
				}
			});
			break;
		case R.id.directions:
			intent = new Intent(this, DirectionsView.class);
			startActivityForResult(intent, R.id.trace);
			break;
		case R.id.turnbyturn:
			showStep();
			break;
		case R.id.map:
			hideStep();
			break;
		case R.id.navigate:
			intent = new Intent(this, Navigate.class);
			startActivityForResult(intent, R.id.trace);
			break;
		case R.id.elevation:
			XYMultipleSeriesDataset elevation = app.getRoute().getElevations();
			final XYMultipleSeriesRenderer renderer = app.getRoute().getChartRenderer();
			if (!"km".equals(unit)) {
				elevation = Convert.asImperial(elevation);
				renderer.setYTitle("ft");
			    renderer.setXTitle("m");
			}
			intent = ChartFactory.getLineChartIntent(this, elevation, renderer);
			startActivityForResult(intent, R.id.trace);
			break;
		case R.id.about:
			showDialog(R.id.about);
			break;
		case R.id.save:
			showDialog(R.id.save);
			break;
		default:
			return false;

		}
		return true;
	}
	
	/**
	 * Helper for sharing xml'd route files.
	 * @param content
	 * @param fileNameId
	 */
	
	private void export(final String content, int fileNameId) {
		Intent t;
		FileOutputStream fos;
		try {
			fos = openFileOutput(getString(fileNameId), Context.MODE_WORLD_READABLE);
			fos.write(content.getBytes());
			fos.close();
		} catch (Exception e) {
			Log.e("File export", e.getMessage());
		}
		t = new Intent(Intent.ACTION_SEND);
		t.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(getFileStreamPath(getString(fileNameId))));
		t.setType("text/xml");
		startActivity(Intent.createChooser(t, getString(R.string.share_chooser_title)));
	}
	
	/**
	 * Retain any route data if the screen is rotated.
	 */
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		Object[] objs = new Object[1];
		objs[0] = directionsVisible;
	    return objs;
	}
	
	/**
	 * Traverse the route up to the point given, overlay a different coloured route
	 * up to there.
	 * @param point
	 */
	
	protected void traverse(final PGeoPoint point) {
		ErrorReporter.getInstance().putCustomData("CrashPoint", point.toString());
		travelledRouteOverlay.clearPath();
		routeOverlay.clearPath();
		int index = app.getRoute().getPoints().indexOf(point);
		int i = 0;
		for (PGeoPoint lPoint : app.getRoute().getPoints()) {
			if (i <= index) {
				travelledRouteOverlay.addPoint(lPoint);
			} 
			if (i >= index){
				routeOverlay.addPoint(lPoint);
			}
			i++;
		}
		mOsmv.invalidate();
	}
	
	/**
	 * Go to the next step of the directions and show it.
	 */
	
	public void nextStep() {
		final int index = app.getRoute().getSegments().indexOf(app.getSegment());
		final ListIterator<Segment> it = app.getRoute().getSegments().listIterator(index + 1);
		if (it.hasNext()) {
			app.setSegment(it.next());
			traverse(app.getSegment().startPoint());
			mOsmv.getController().setCenter(app.getSegment().startPoint());
		}	else {
			traverse(app.getRoute().getEndPoint());
			mOsmv.getController().setCenter(app.getRoute().getEndPoint());
		}
		showStep();
	}
	
	/**
	 * Go to the previous step of the directions and show it.
	 */
	
	public void lastStep() {
		final int index = app.getRoute().getSegments().indexOf(app.getSegment());
		final ListIterator<Segment> it = app.getRoute().getSegments().listIterator(index);
		if (it.hasPrevious()) {
			app.setSegment(it.previous());
		}
		showStep();
		traverse(app.getSegment().startPoint());
		mOsmv.getController().setCenter(app.getSegment().startPoint());
 	}
	
	/**
	 * Hide the onscreen directions.
	 */
	
	public void hideStep() {
		final View overlay = findViewById(R.id.directions_overlay);
		overlay.setVisibility(View.INVISIBLE);
		mOsmv.setClickable(true);
		directionsVisible = false;
		this.mOsmv.setBuiltInZoomControls(true);
        this.mOsmv.setMultiTouchControls(true);
	}
	
	/**
	 * Show the currently selected step of directions onscreen, focus
	 * the map at the start of that section.
	 */
	
	public void showStep() {
		directionsVisible = true;
		this.mOsmv.setBuiltInZoomControls(false);
        this.mOsmv.setMultiTouchControls(false);
        mOsmv.getController().setZoom(16);
		
		final View overlay = findViewById(R.id.directions_overlay);
		this.mOsmv.setClickable(false);
		
		//Setup buttons
		final Button back = (Button) overlay.findViewById(R.id.back_button);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lastStep();
			}
			
		});
		
		final Button next = (Button) overlay.findViewById(R.id.next_button);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextStep();
			}
			
		});
		
		overlay.setOnTouchListener(gestureListener);
		
		final TextView turn = (TextView) overlay.findViewById(R.id.turn);
		final TextView distance = (TextView) overlay.findViewById(R.id.distance);
		final TextView num = (TextView) overlay.findViewById(R.id.step_no);
		turn.setText(Html.fromHtml(app.getSegment().getInstruction()), TextView.BufferType.SPANNABLE);
		
		final String distanceString = "km".equals(unit) ? Convert.asMeterString(app.getSegment().getLength()) + " ("
				+ Convert.asKilometerString(app.getSegment().getDistance()) + ")" : 
					Convert.asFeetString(app.getSegment().getLength()) + " ("
					+ Convert.asMilesString(app.getSegment().getDistance()) + ")";
		
		distance.setText(distanceString);
		num.setText(app.getRoute().getSegments().indexOf(app.getSegment()) 
				+ 1 + "/" + app.getRoute().getSegments().size());
		overlay.setVisibility(View.VISIBLE);
	}

	/** Overlay to handle swipe events when showing directions.
	 * 
	 */
	
	private class OSDOverlay extends Overlay {

		/**
		 * @param ctx
		 */
		public OSDOverlay(final Context ctx) {
			super(ctx);
			//Detect swipes (left & right, taps.)
	        gestureDetector = new GestureDetector(RouteMap.this, new TurnByTurnGestureListener(RouteMap.this));
	        gestureListener = new View.OnTouchListener() {
	            @Override
				public boolean onTouch(final View v, final MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
	                }
	        };
		}
		
		/**
		 * If the onscreen display is enabled, capture motion events to control it.
		 */
		
		@Override
		public boolean onTouchEvent(final MotionEvent event, final MapView mv) {
			if (RouteMap.this.directionsVisible) {
				RouteMap.this.gestureDetector.onTouchEvent(event);
				return true;
			} else {
		        return false;
			}
		}


		/* (non-Javadoc)
		 * @see org.osmdroid.views.overlay.Overlay#draw(android.graphics.Canvas, org.osmdroid.views.MapView, boolean)
		 */
		@Override
		protected void draw(Canvas arg0, MapView arg1, boolean arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
