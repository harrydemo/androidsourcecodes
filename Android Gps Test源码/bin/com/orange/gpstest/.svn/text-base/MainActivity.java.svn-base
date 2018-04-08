package com.orange.gpstest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.location.*;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.util.TypedValue;

/**
 * This class represents the main window of the application
 * This window displays all GPS data updated during tests
 * @see Activity
 * @author Alan Sowamber
 */
public class MainActivity extends Activity{
	
	final static String TAG="MyActivity";
	final static String version="1.01";
	public LocationManager locationManager;
	public String locationProviderString;
    	
	public MainApp mainApplication;
	
	public TextView latitude;
	public TextView longitude;
	public TextView availability;
	public TextView sessionDuration;
	public TextView accuracy;
	public TextView positiongMethod;
	public TextView numberOfSatellites;
	public TextView timeToFirstFix;
	public TextView reqNetwork;
	public TextView reqSatellites;
	public ProgressBar progressBar;
	public TextView progressText;
	
	static final String dialogMsgArray[]={
		"Invalid destination directory!"
	};
	
	static final int dialogMsgDirectoryErrorId=0;
	static final int dialogMsgAboutId=1;
	
	/**
	 * Implements this method to display dialogs
	 * @see Activity
	 */
	@Override
	public Dialog onCreateDialog (int id){
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if(id==dialogMsgAboutId)
		{
			String msg=String.format("%s %s", mainApplication.appName,mainApplication.versionName);
			builder.setMessage(msg);
		}
		else
		{
			builder.setMessage(dialogMsgArray[id]);
		}
		
		builder.setCancelable(false);
		builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });
		AlertDialog alert = builder.create();
		return alert;
	}
	
	
	
	
	/** Called when the activity is first created. 
	 * Create the reference to each control and size its to fit screen
	 * @see Activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Create references
		mainApplication=(MainApp) getApplication();
		mainApplication.mainActivity=this;
		
		
		TextView latitudelabel=(TextView)findViewById(R.id.Latitude);
		latitudelabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		latitude=(TextView)findViewById(R.id.Latitude_value);
		latitude.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		TextView longitudelabel=(TextView)findViewById(R.id.Longitude);
		longitudelabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		longitude=(TextView)findViewById(R.id.Longitude_value);
		longitude.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		TextView availabilitylabel=(TextView)findViewById(R.id.Location_availability);
		availabilitylabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		availability=(TextView)findViewById(R.id.Location_availability_value);
		availability.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		TextView sessionDurationlabel=(TextView)findViewById(R.id.Session_duration);
		sessionDurationlabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		sessionDuration=(TextView)findViewById(R.id.Session_duration_value);
		sessionDuration.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		TextView accuracylabel=(TextView)findViewById(R.id.Horizontal_accuracy);
		accuracylabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		accuracy=(TextView)findViewById(R.id.Horizontal_accuracy_value);
		accuracy.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		TextView numberOfSatelliteslabel=(TextView)findViewById(R.id.Satellite_numbers);
		numberOfSatelliteslabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		numberOfSatellites=(TextView)findViewById(R.id.Satellite_numbers_value);
		numberOfSatellites.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		TextView timeToFirstFixlabel=(TextView)findViewById(R.id.TTFF);
		timeToFirstFixlabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		timeToFirstFix=(TextView)findViewById(R.id.TTFF_value);
		timeToFirstFix.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		TextView reqNetworklabel=(TextView)findViewById(R.id.NetworkReq);
		reqNetworklabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		reqNetwork=(TextView)findViewById(R.id.NetworkReq_value);
		reqNetwork.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		TextView reqSatelliteslabel=(TextView)findViewById(R.id.SatReq);
		reqSatelliteslabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		reqSatellites=(TextView)findViewById(R.id.SatReq_value);
		reqSatellites.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		progressBar=(ProgressBar)findViewById(R.id.ProgressBar01);
		
		progressText=(TextView)findViewById(R.id.Progressing);
		progressText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApplication.GetFontSize());
		
		
		// Set the default values
		if(mainApplication.testRunning)
		{
			progressBar.setVisibility(View.VISIBLE);
			progressText.setVisibility(View.VISIBLE);
		}	
		
		this.setSessionDuration(mainApplication.chronoTimer.chronoValue);
		
		if(mainApplication.gpsTest!=null)
		{
			GPSTest gpsTest=mainApplication.gpsTest;
			
			if(gpsTest.isLocationAvaibable())
			{
				Location location=gpsTest.location;
				this.setLatitude(location.getLatitude());
				this.setLongitude(location.getLongitude());
				this.setAccuracy(location.getAccuracy());
			}
			else
			{
				this.setLatitude(0);
				this.setLongitude(0);
				this.setAccuracy(0);
			}
			
			if(gpsTest.isLocationProviderAvaibable())
			{
				LocationProvider locationProvider=gpsTest.locationProvider;
				
				this.setNetworkRequired(locationProvider.requiresNetwork());
				this.setSatelliteRequired(locationProvider.requiresSatellite());
				
				
				
				if(gpsTest.isLocationManagerAvaibable()){
					LocationManager locationManager=gpsTest.locationManager;
					this.setAvailability(locationManager.isProviderEnabled(locationProvider.getName()));
				}
				else
				{
					this.setAvailability(false);
				}
			}
			else
			{
				
				this.setAvailability(false);
				this.setNetworkRequired(false);
				this.setSatelliteRequired(false);
			}
			
			if(gpsTest.isStatusAvaibable())
			{
				this.setNumberOfSatellites(gpsTest.iNbSatellites);
				
				this.setTimeToFirstFix(gpsTest.gpsStatus.getTimeToFirstFix());
			}
			else
			{
				this.setNumberOfSatellites(0);
				
				this.setTimeToFirstFix(0);
			}
		}
		else
		{
			this.setLatitude(0);
			this.setLongitude(0);
			this.setAccuracy(0);
			this.setAvailability(false);
			this.setNumberOfSatellites(0);
			this.setTimeToFirstFix(0);
			this.setNetworkRequired(false);
			this.setSatelliteRequired(false);
		}
		
		
	}
	
	/**
	 * Set the "Yes" or "No" text nested "Require network" TextView.
	 * @param req A true value set "Yes" and a false value set "No".
	 */
	public void setNetworkRequired(boolean req){
		
		if(req==true)
			this.reqNetwork.setText("Yes");
		else
			this.reqNetwork.setText("No");
		
	}
	
	/**
	 * Set the "Yes" or "No" text nested "Require satellite" TextView.
	 * @param req A true value set "Yes" and a false value set "No".
	 */
	public void setSatelliteRequired(boolean req){
		
		if(req==true)
			this.reqSatellites.setText("Yes");
		else
			this.reqSatellites.setText("No");
		
	}
	
	/**
	 * Set a number value nested "Latitude" TextView.
	 * @param req The number value to set.
	 */
	public void setLatitude(double latitude)
	{
		this.latitude.setText(String.valueOf(latitude));
	}
	
	/**
	 * Set a number value nested "Longitude" TextView.
	 * @param req The number value to set.
	 */
	void setLongitude(double longitude)
	{
		this.longitude.setText(String.valueOf(longitude));
		
	}
	
	/**
	 * Set "Enabled" or "Disabled" text value nested "AGPS availability" TextView.
	 * @param req A true value set "Enabled" text and a false value set "Disabled" text.
	 */
	void setAvailability(boolean availability)
	{
		if(availability==true)
			this.availability.setText("Enabled");
		else
			this.availability.setText("Disabled");
	}
	
	/**
	 * Convert a time value in seconds to a text in format "hh:mm:ss".
	 * @param seconds
	 * @return The String representation of the time value.
	 */
	static String convertTime(int seconds){
		int hour,min,sec;
		
		hour=seconds/3600;
		min=seconds/60-hour*60;
		sec=seconds-hour*3600-min*60;
		
		String strDate=String.format("%02d:%02d:%02d", hour,min,sec);
		
		return strDate;
		
	}
	
	/**
	 * Set a number value nested "Session duration" TextView.
	 * @param sessionDuration The number value to set.
	 */
	void setSessionDuration(int sessionDuration)
	{
		
		String strDate=convertTime(sessionDuration);
		this.sessionDuration.setText(strDate);
	}
	
	/**
	 * Set a number value nested "Accuracy" TextView.
	 * @param f The number value to set.
	 */
	void setAccuracy(float f){
		this.accuracy.setText(String.valueOf(f));
		
	}
	
	/**
	 * Set a number value nested "Satellites" TextView.
	 * @param nbSatellites The number value to set.
	 */
	void setNumberOfSatellites(int nbSatellites)
	{
		this.numberOfSatellites.setText(String.valueOf(nbSatellites));
		
	}
	
	/**
	 * Set a number value nested "TTFF" TextView.
	 * @param milli The number value to set.
	 */
	void setTimeToFirstFix(int milli)
	{
		milli=milli/1000;
		String strTime=convertTime(milli);
		this.timeToFirstFix.setText(strTime);
		
	}
	
	
	/**
	 * Implements this method from Activity class
	 * 
	 * @see Activity
	 */
	@Override
	protected void  onDestroy  ()
	{
		super.onDestroy();
		mainApplication.mainActivity=null;
		
	}
	
	/**
	 * Implements this method from Activity class
	 * Create the option menu on click on menu button
	 * @see Activity
	 */
	public boolean  onCreateOptionsMenu  (Menu menu)
	{
		
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.menu.option_menu, menu);
		 return true;

	}
	
	/**
	 * Implements this method from Activity class
	 * Handle the return value selected from the menu 
	 * @see Activity
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.staticmenu:
	    	//Start static test
	    	mainApplication.startStaticTest();
	        return true;
	    case R.id.trackingmenu:
	    	//Start dynamic test
	    	mainApplication.startTrackingTest();
	        return true;
	    case R.id.stop:
	    	//Stop the current test
	    	mainApplication.stopTest();
	        
	        return true;
	        
	    case R.id.settings:
	    {
	    	//Go to settings window
	    	Intent ident =new Intent(this,SettingsUI.class);
	    	this.startActivity(ident);
	    	return true;
	    }
	    
	    case R.id.snrItem:
	    {
	    	//Go to snr satellite window
	    	Intent ident =new Intent(this,SatelitteView.class);
	    	this.startActivity(ident);
	    	return true;
	    }
	    
	    case R.id.quit:
	    {
	    	//Quit application
	    	mainApplication.Exit();
	    	return true;
	    }
	    
	    case R.id.aboutitem:
	    {
	    	//Show "About" dialog
	    	showDialog(dialogMsgAboutId);
	    	return true;
	    }
	    
	    }
	    return false;
	}
	
	
	

}