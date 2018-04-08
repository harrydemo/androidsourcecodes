package com.orange.gpstest;




import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.*;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.io.File;

/**
 * This class represents the application
 * @see Application
 * @author Alan Sowamber
 *
 */
public class MainApp  extends Application implements IChronoEventListener {

	public MainActivity mainActivity=null;
	public SatelitteView satView=null;
	public ChronoTimer chronoTimer;
	public Settings settings;
	public GPSTest gpsTest=null;
	
	boolean testRunning;
	DisplayMetrics metric;
	
	static final int OK=0;
	static final int CANTCREATEFILE=1;
	static final int FILECREATED=2;
	static final int CANTOPENFILE=3;
	
	int versioncode;
	String versionName;
	String appName;
	
	/**
	 * Implements method from Application class
	 * Create the application
	 */
	@Override
	public void  onCreate  ()
	{
		super.onCreate();
		
		settings=new Settings(this);
		settings.TestFile="\\testfile";
		
		mainActivity=null;
		chronoTimer=new ChronoTimer(1);
		
		testRunning=false;
		WindowManager windowManager=(WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display display=windowManager.getDefaultDisplay();
		metric=new DisplayMetrics();	
		display.getMetrics(metric);
		try {
			PackageInfo packageInfo=this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			versioncode=packageInfo.versionCode;
			versionName=packageInfo.versionName;
			appName=this.getResources().getString(packageInfo.applicationInfo.labelRes);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			versioncode=0;
		}
	}
	
	/**
	 * Get the font size depending the screen size
	 * @return Font size
	 */
	int GetFontSize()
	{
		int size=0;
		
		size=(int)((float)20*metric.widthPixels/320);
	
		return size; 
	}
	
	/**
	 * Get location manager system service
	 * @return LocationManager
	 */
	public LocationManager GetLocationManager(){
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}
	
	/**
	 * Update UI data from test
	 * @param test Current test
	 * @param event GPS status event
	 * @see GpsStatus.Listener
	 */
	public void onGpsStatusChanged(GPSTest test,int event) {
		
		switch(event)
		{
		case GpsStatus.GPS_EVENT_FIRST_FIX:
			Log.d("onGpsStatusChanged", "GPS_EVENT_FIRST_FIX");
			break;
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			Log.d("onGpsStatusChanged", "GPS_EVENT_SATELLITE_STATUS");
			break;
		case GpsStatus.GPS_EVENT_STARTED:
			Log.d("onGpsStatusChanged", "GPS_EVENT_STARTED");
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			Log.d("onGpsStatusChanged", "GPS_EVENT_STOPPED");
			break;
		}
		
		int iNbSatellites=test.iNbSatellites;
		Log.d("isatellites",String.valueOf(iNbSatellites));
		
		if(mainActivity!=null)
		{
			mainActivity.setNumberOfSatellites(iNbSatellites);
			
			if(test.gpsStatus!=null)
				mainActivity.setTimeToFirstFix(test.gpsStatus.getTimeToFirstFix());
		}
		
		if(satView!=null)
		{
			satView.UpdateTable(test.gpsStatus);
			
		}
		
	}
	
	/*
	 * Quit application
	 */
	void Exit()
	{
		
		chronoTimer.stopChrono();
		System.exit(0);
	}
	
	
	/**
	 * Check or create the output test file
	 * @param testFileStr Output test file filename
	 * @return Error code. Possible values are OK, CANTCREATEFILE, FILECREATED or CANTOPENFILE
	 */
	int checkTestFile(String testFileStr){
		File file =new File(testFileStr);
		ArrayList<File> parentArray=new ArrayList<File>();
		ArrayList<File> newFiles=new ArrayList<File>();
		
		
		File parentFile=file;
		int iret=OK;
		
		int lastIndex=0;
		
		try
		{
			
		if(file.isDirectory()&& file.exists())
		{
			if(!file.canWrite())
			{
				iret=CANTOPENFILE;
				throw new Exception();
			}
			
			return OK;
		}
		
		
		while(parentFile!=null){
			parentArray.add(parentFile);
			parentFile=parentFile.getParentFile();
		}
		
			for(lastIndex=parentArray.size()-1;lastIndex>=0;lastIndex--)
			{
				parentFile=parentArray.get(lastIndex);
				if((parentFile.exists()==false)||(parentFile.isDirectory()==false))
				{
					if(parentFile.mkdirs()==false)
						throw new Exception();
					newFiles.add(parentFile);
				}
				
			}
			iret=OK;
		}
		catch(Exception e)
		{
			iret=CANTCREATEFILE;
			
		}
		
		if(iret!=OK)
		{
			if(newFiles.size()>0)
			{
				for(lastIndex=newFiles.size()-1;lastIndex>=0;lastIndex--)
				{
					parentFile=newFiles.get(lastIndex);
					
					try
					{
						parentFile.delete();
					}
					catch(Exception e){}
				}
			}
			
			return iret;
		}
		
	
		
		if(newFiles.size()>0)
			return FILECREATED;
		
		return OK;
		
		
	}
	
	/**
	 * Update UI data from test
	 * @param test Current test
	 * @param location New location
	 * @see LocationListener
	 */
	public void onLocationChanged(GPSTest test,Location location) 
	{
		// TODO Auto-generated method stub
		
		if(mainActivity!=null)
		{
			mainActivity.setLatitude(location.getLatitude());
			mainActivity.setLongitude(location.getLongitude());
			mainActivity.setAccuracy(location.getAccuracy());
		}
	}

	/**
	 * Update UI data from test
	 * @param test Current test
	 * @param provider Provider name
	 * @see LocationListener
	 */
	public void onProviderDisabled(GPSTest test,String provider) {
		// TODO Auto-generated method stub
		if(mainActivity!=null)
		{
			mainActivity.setAvailability(false);
		}
		
	}

	/**
	 * Update UI data from test
	 * @param test Current test
	 * @param provider Provider name
	 * @see LocationListener
	 */
	public void onProviderEnabled(GPSTest test,String provider) {
		// TODO Auto-generated method stub
		if(mainActivity!=null)
		{
			mainActivity.setAvailability(true);
			mainActivity.setNetworkRequired(test.locationProvider.requiresNetwork());
			mainActivity.setSatelliteRequired(test.locationProvider.requiresSatellite());
		}
		
		
	}

	/**
	 * Update UI data from test
	 * @param test Current test
	 * @param provider Provider name
	 * @see LocationListener
	 */
	public void onStatusChanged(GPSTest test,String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
		
		
		
	}
	
	/**
	 * Start a static test
	 */
	public void startStaticTest()
	{
		File testFile=new File(settings.TestDirectory);
		if(testFile.exists()==false){
			if(testFile.mkdirs()==false)
			{
				mainActivity.showDialog(MainActivity.dialogMsgDirectoryErrorId);
				return;
			}
		}
		
		if(gpsTest!=null)
			gpsTest.Uninitialize();
			
		gpsTest=new StaticGPSTest(this);
		chronoTimer.listener=gpsTest;
		chronoTimer.startChrono();

		if(mainActivity!=null)
		{
			mainActivity.setSessionDuration(0);
			mainActivity.progressBar.setVisibility(View.VISIBLE);
			mainActivity.progressText.setVisibility(View.VISIBLE);
		}
		
		testRunning=true;
		
		
	}
	
	/**
	 * Start a dynamic test
	 */
	public void startTrackingTest()
	{
		File testFile=new File(settings.TestDirectory);
		if(testFile.exists()==false){
			if(mainActivity!=null)
				mainActivity.showDialog(MainActivity.dialogMsgDirectoryErrorId);
			return;
		}
		
		if(gpsTest!=null)
			gpsTest.Uninitialize();
		
		gpsTest=new TrackingGPSTest(this);
		chronoTimer.listener=gpsTest;
		chronoTimer.startChrono();
		
		if(mainActivity!=null)
		{
			mainActivity.setSessionDuration(0);
			mainActivity.progressBar.setVisibility(View.VISIBLE);
			mainActivity.progressText.setVisibility(View.VISIBLE);
		}
		
		testRunning=true;
		
		
		
		
	}
	
	/**
	 * Stop the current test
	 */
	public void stopTest()
	{
		chronoTimer.stopChrono();
		if(mainActivity!=null)
		{
			
			mainActivity.progressBar.setVisibility(View.INVISIBLE);
			mainActivity.progressText.setVisibility(View.INVISIBLE);
		}
		testRunning=false;
	}

	/**
	 * Update the UI on timer change
	 * @param timeElapsed new timer value
	 */
	public void onChronoReached(int timeElapsed) {
		// TODO Auto-generated method stub
		if(mainActivity!=null)
		{
			this.mainActivity.setSessionDuration(timeElapsed);
		}
		
	}

	/**
	 * Update the UI on timer start
	 */
	public void onChronoStart() {
		// TODO Auto-generated method stub
		if(mainActivity!=null)
		{
			this.mainActivity.setSessionDuration(0);
		}
		
	}

	/**
	 * Update the UI on timer stop
	 */
	public void onChronoStop() {
		// TODO Auto-generated method stub
		
	}

	
}
