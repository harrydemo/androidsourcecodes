package com.orange.gpstest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.location.*;
import android.util.TypedValue;
import java.util.*;

/**
 * This class represents the satellite SNR window
 * This window displays all satellite GPS detailed data updated during tests
 * @see Activity
 * @author Alan Sowamber
 */
public class SatelitteView extends Activity {

	MainApp mainApp;
	TableLayout tableLayout;
	int numberOfRow;
	
	/** 
	 * Called when the activity is first created. 
	 * Create and update the window	 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.satelliteview);
	    
	    mainApp= (MainApp) this.getApplication();
	    mainApp.satView=this;
	    tableLayout=(TableLayout)this.findViewById(R.id.TableSatelitteLayout);
	    
	    if(mainApp.testRunning)
	    {
	    	GPSTest gpsTest=mainApp.gpsTest;
	    	if(gpsTest.gpsStatus!=null){
	    		UpdateTable(gpsTest.gpsStatus);
	    	}
	    }
	
	    // TODO Auto-generated method stub
	}
	
	/**
	 * Update the window from new GPSStatus object
	 * @param gpsStatus The new status
	 * @see GPSStatus
	 */
	public void UpdateTable(GpsStatus gpsStatus){
		
		Iterator<android.location.GpsSatellite> satList=null;
		satList=gpsStatus.getSatellites().iterator();
		
		if(satList!=null)
		{
			tableLayout.removeAllViews();
			
			while(satList.hasNext())
			{
				GpsSatellite gpsSat=satList.next();
				String satNameString="Satelitte PRN:"+String.format("%02d", gpsSat.getPrn());
				
				if(gpsSat.usedInFix())
				{
					satNameString=satNameString+" used";
				}
				
				TextView satName= new TextView(this);
				satName.setText(satNameString);
				satName.setPadding((int)5, 0, 0, 0);
				satName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApp.GetFontSize());
				
				
				TextView satSNRValue=new TextView(this);
				String satSNRValueString=String.valueOf(gpsSat.getSnr())+" dB";
				satSNRValue.setText(satSNRValueString);
				satSNRValue.setPadding((int)(5), 0, 0, 0);
				satSNRValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainApp.GetFontSize());
				   
				
				TableRow tableRow=new TableRow(this);
				tableRow.addView(satName);
				tableRow.addView(satSNRValue);
				
				tableLayout.addView(tableRow);
				
			}
			
		}
		
		
		
		
		
	}

}
