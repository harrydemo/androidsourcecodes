package com.orange.gpstest;

/**
 * This class represents a dynamic test.
 * @author Alan Sowamber
 *
 */
public class TrackingGPSTest extends GPSTest {
	
	/**
	 * Create a dynamic test
	 * @param mainApp Reference to the main application
	 */
	public TrackingGPSTest(MainApp mainApp){
		super(mainApp);
		
	}
	
	/**
	 * Start static test when chronometer starts 
	 */
	public void onChronoStart(){
		Uninitialize();
		Initialize();
		
	}
	
	/**
	 * Stop static test when chronometer stop 
	 */
	public  void onChronoStop(){
		Uninitialize();
	}
	
	public  void onChronoReached(int timeElapsed)
	{
		super.onChronoReached( timeElapsed);
		if(location!=null){
			String gga=buildNMEA(this);
			writeData(gga);
		}
		
	}
	

}
