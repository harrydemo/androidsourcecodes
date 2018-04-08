package com.orange.gpstest;

import android.location.GpsStatus;
import android.location.Location;

/**
 * This class represents a static test.
 * @author Alan Sowamber
 *
 */
public class StaticGPSTest extends GPSTest {

	/**
	 * Create a static test
	 * @param mainApp Reference to the main application
	 */
	public StaticGPSTest(MainApp mainApp) {
		super(mainApp);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Start static test when chronometer starts 
	 */
	public void onChronoStart(){
		Uninitialize();
		Initialize();
		
	}
	
	/**
	 * stop static test when chronometer stop 
	 */
	public  void onChronoStop(){
		Uninitialize();
	}
	
	/**
	 * Call inherited method
	 */
	public  void onChronoReached(int timeElapsed){
		super.onChronoReached( timeElapsed);
		
	}
	
	/**
	 * 
	 */
	public void onGpsStatusChanged(int event) {
		super.onGpsStatusChanged(event);
	
		if(event==GpsStatus.GPS_EVENT_FIRST_FIX)
		{
			if(location!=null){
				String gga=buildNMEA(this);
				writeData(gga);
				mainApp.stopTest();
			}
			
		}
	}
	
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);
		
		if(super.gpsStatus!=null)
		{
			if(location!=null){
				String gga=buildNMEA(this);
				writeData(gga);
				mainApp.stopTest();
			}
			
			
		}
	}

}
