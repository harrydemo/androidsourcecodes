package com.orange.gpstest;


import java.io.File;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * This class is the base class of a Test
 * @author Alan Sowamber
 */

public class GPSTest implements IChronoEventListener,android.location.GpsStatus.Listener,LocationListener{
	
	public LocationManager locationManager=null;
	public Location location=null;
	public GpsStatus gpsStatus=null;
	public LocationProvider locationProvider=null;
	public int iNbSatellites=0;
	public MainApp mainApp;
	public float averageSNR=0;
	public int satelliteUsed=0;
	PrintStream printStream;
	
	/**
	 * Create the test passing the reference to the MainApp class
	 * @param MainApp Main application class 
	 */
	public GPSTest(MainApp mainApp){
		this.mainApp=mainApp;
	}
	
	/**
	 * Initialize the test
	 * -Create the NMEA output test file for writing
	 * -Request GPS notifications
	 */
	public void Initialize(){
		try{
			
			Date date=Calendar.getInstance().getTime();
			CharSequence datestring=DateFormat.format("MMddyy_kkmmssaa", date);
			String filename;
			
			if(mainApp.settings.appendTime)
				filename=mainApp.settings.TestFile+"_"+datestring+".txt";
			else
				filename=mainApp.settings.TestFile+".txt";;
				
			File file=new File(mainApp.settings.TestDirectory,filename);
			printStream =new PrintStream(file);
			
			
		}
		catch(Exception e){
			
			Log.e("File error", "File error: "+e.toString());
			
			
		}
		
		locationManager=mainApp.GetLocationManager();
		locationProvider=locationManager.getProvider("gps");
		
		if(locationManager.isProviderEnabled("gps"))
			mainApp.onProviderEnabled(this, "gps");
		else
			mainApp.onProviderDisabled(this, "gps");
			
		locationManager.requestLocationUpdates("gps", 0, 0, this);
		locationManager.addGpsStatusListener(this);
	}
	
	/**
	 * Uninitialize the test
	 * - Remove GPS notifications
	 * - Close the NMEA output file
	 */
	public void Uninitialize(){
		if(locationManager!=null)
		{
			locationManager.removeUpdates(this);
			locationManager.removeGpsStatusListener(this);
			//locationManager=null;
			
		}
		
		if(printStream!=null)
		{
			
			printStream.close();
			
			
			printStream=null;
		}
		
	}
	
	/**
	 * Control if a location is currently available
	 * @return boolean The availability of the location 
	 */
	public boolean isLocationAvaibable()
	{
		return (this.location!=null);
	}
	
	/**
	 * Control if a GPS status is currently available
	 * @return boolean The availability of the GPS Status 
	 */
	public boolean isStatusAvaibable()
	{
		return (this.gpsStatus!=null);
	}
	
	/**
	 * Control if a LocationProvider is currently available
	 * @return boolean The availability of the LocationProvider 
	 */
	public boolean isLocationProviderAvaibable()
	{
		return (this.locationProvider!=null);
	}
	
	/**
	 * Control if a LocationManager is currently available
	 * @return boolean The availability of the LocationManager 
	 */
	public boolean isLocationManagerAvaibable()
	{
		return (this.locationManager!=null);
	}
	
	/**
	 * Implements the ChronoTimer methods to receive timer notifications
	 */
	@Override
	public void onChronoStart(){
		
	}
	
	/**
	 * Implements the ChronoTimer methods to receive timer notifications
	 */
	@Override
	public  void onChronoStop(){
		
	}
	
	/**
	 * Implements the ChronoTimer methods to receive timer notifications
	 */
	@Override
	public  void onChronoReached(int timeElapsed){
		mainApp.onChronoReached(timeElapsed);
		
	}
	
	/**
	 * Create a NMEA frame in a String from the current GPS location and status.
	 * @param GPSTest Reference to the test to build from.
	 * @return String The NMEA frame or null if no frame has been built
	 */
	public String buildNMEA(GPSTest test)
	{
		Location loc=test.location;
		
		/*
		try {
			mainApp.openFileOutput("tracelog.txt", Context.MODE_WORLD_READABLE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
		String gga = "$GPGGA";
		try{
			//hhmmss.ss
			String degre;
			String minutes;
			Date date = new Date(loc.getTime());
			Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("GMT"));
			cal.setTime(date);
			int hour = cal.get(Calendar.HOUR);
			int min = cal.get(Calendar.MINUTE);
			int sec = cal.get(Calendar.SECOND);
			int milliSec = cal.get(Calendar.MILLISECOND);
			String timeFormated = 	(hour<10? "0": "")+hour+
			(min<10? "0": "")+min+
			(sec<10? "0": "")+sec+"."+
			(milliSec<10? "0": "")+milliSec;

			//latitude
			double latitude = loc.getLatitude();
			String latitudeStr = Double.toString(latitude);
			String northsouth = "N";

			latitudeStr = Location.convert(latitude,Location.FORMAT_MINUTES);
			if(latitudeStr.startsWith("-")){
				latitudeStr = latitudeStr.substring(1);
				northsouth = "S";
			}
			
			//Get degrees
			int index=latitudeStr.indexOf(":");
			degre=latitudeStr.substring(0, index);
			for(int i=index;i<2;i++)
			{
				degre="0"+degre;
			}
			
			//Get minutes
			minutes=latitudeStr.substring(index+1);
			minutes=minutes.replace(',', '.');
			index=minutes.indexOf(".");
			for(int i=index;i<2;i++)
			{
				minutes="0"+minutes;				
			}
			
			Log.d("Degree", degre);
			Log.d("minutes", degre);
			
			latitudeStr=degre+minutes;
			


			//longitude
			double longitude = loc.getLongitude();
			String longitudeStr = Double.toString(longitude);
			String estwest = "E";
			
			longitudeStr = Location.convert(longitude,Location.FORMAT_MINUTES);
			
			if(longitudeStr.startsWith("-")){
				longitudeStr = longitudeStr.substring(1);
				estwest = "W";
			}
			//Get degrees
			index=longitudeStr.indexOf(":");
			degre=longitudeStr.substring(0, index);
			for(int i=index;i<3;i++)
			{
				degre="0"+degre;
			}
			
			//get minutes
			minutes=longitudeStr.substring(index+1);
			minutes=minutes.replace(',', '.');
			//add 0
			index=minutes.indexOf(".");
			for(int i=index;i<2;i++)
			{
				minutes="0"+minutes;				
			}
			
			longitudeStr=degre+minutes;
			

			//horizontal accuracy
			float hdilution = loc.getAccuracy();
			String hdilutionStr = "";
			if(Float.isNaN(hdilution)==false){
				hdilutionStr = Float.toString(hdilution);
				//hdilutionStr = formatEndingLongString(1,hdilutionStr);
			}

			String satelliteUsedStr=new String();
			
			satelliteUsedStr=String.format("%02d", test.satelliteUsed);

			//altitude
			double altitude = loc.getAltitude();
			String altitudeStr = "";
			if(Double.isNaN(altitude) == false){
				altitudeStr = Double.toString(altitude);
				//altitudeStr = formatEndingLongString(1,altitudeStr);
			}



			gga = gga+","+timeFormated+","+latitudeStr+","+northsouth+","+longitudeStr+","+estwest+",1,"+satelliteUsedStr+","+hdilutionStr+","+altitudeStr+",M,,,,*";
			gga = gga +(gga.length()-2)+"\r\n"; //-2 to subtract the first $ and the last *
			
			
			
		}catch(Exception e){
			gga = e.toString();
		
	}
		
		return gga;
	
	}
	
	/**
	 * Write string line into the current open test file
	 * @param str Data to write
	 */
	void writeData(String str)
	{
		printStream.println(str);
	}
	
	
	/**
	 * Implements methods to receive GPS status notifications
	 * @see GpsStatus.Listener
	 */
	@Override
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
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
			return;
		case GpsStatus.GPS_EVENT_STOPPED:
			Log.d("onGpsStatusChanged", "GPS_EVENT_STOPPED");
			return;
		}
		
		if(locationManager!=null)
			gpsStatus=locationManager.getGpsStatus(null);
		
		GpsStatus gpsStatus= locationManager.getGpsStatus(null);
		if(gpsStatus!=null)
		{
			this.gpsStatus=gpsStatus;
			
			int newNbSatellites=0;
			int newsatelliteUsed=0;
			Iterator<GpsSatellite> iterator=gpsStatus.getSatellites().iterator();
			
			while(iterator.hasNext())
			{
				
				GpsSatellite gpsSatellite=iterator.next();
				if(gpsSatellite.usedInFix())
					newsatelliteUsed++;
				
				newNbSatellites++;
			}
			satelliteUsed=newsatelliteUsed;
			iNbSatellites=newNbSatellites;
			Log.d("Sat used:",String.valueOf(satelliteUsed));
			mainApp.onGpsStatusChanged(this, event);
		}
		
		
		
	}
	
	/**
	 * Implements methods to receive GPS location notifications
	 * @see LocationListener
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		mainApp.onLocationChanged(this, location);
		this.location=location;
		
		
	}

	/**
	 * Implements methods to receive GPS provider notifications
	 * @see LocationListener
	 */
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		//mainActivity.sessionDuration.setText("disabled");
		mainApp.onProviderDisabled(this, provider);
		
		
	}

	/**
	 * Implements methods to receive GPS provider notifications
	 * @see LocationListener
	 */
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		//mainActivity.sessionDuration.setText("enabled");
		mainApp.onProviderEnabled(this, provider);
		
		
	}

	/**
	 * Implements methods to receive GPS status notifications
	 * @see LocationListener
	 */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
			
		
		mainApp.onStatusChanged(this,provider,  status,  extras);
		
	}

}
