/*
 * This file is part of NetworkMonitor copyright Dave Edwards (eddyspace.com)
 * 
 * Firstly thanks to all and sundry for the tutorials and forum threads I've used
 * in putting this together.
 *
 * Where I've copied whole chunks of stuff I've tried to attribute it properly.
 * Otherwise, I've at least typed it in and munged it to suit which makes it 
 * mine I guess.
 *
 * As far as it goes: Network Monitor is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation, either version 3 of the License, 
 * or (at your option) any later version.
 * 
 * Network Monitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * Please see  <a href='http://www.gnu.org/licenses/'>www.gnu.org/licenses</a> 
 * for a copy of the GNU General Public License.
 */

package com.eddyspace.networkmonitor;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;

/**
 * Android activity to display network information 
 * 
 * @author davo 
 * 
 */
public class NetworkInfo extends Activity {
	private static final String LOG_TAG = "NetworkMonitor.MetmonInfo";	
	private final static boolean localLOGV = false;
	public static final String PREFS_NAME = "com.eddyspace.networkmonitor_preferences";
	
	private LogAdapter la = null;
	
	/**
	 * Generates the network information view
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Network Information");
        
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE );
        if( prefs.getBoolean("preference_first_run", true) ) {
            Dialog detail = new Dialog(this);
            detail.setTitle(R.string.first_run_title);
            detail.setContentView(R.layout.first_run);
            // TextView textView = (TextView) detail.findViewById(R.id.about_text); 		  	    		  	
            detail.show();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("preference_first_run", false);
            editor.commit();
        }
        
        if(localLOGV) Log.v(LOG_TAG, "onCreate NetmonInfo");        
        NetInfoAdapter ni = new NetInfoAdapter(this);
    	la = new LogAdapter(this);
    	Map<String,String> infoMap = new HashMap<String,String>();
    	
        if( ni.isConnected() ) {        	
        	if(localLOGV) Log.v(LOG_TAG, "Seems we're connected..");
        } else {
        	if(localLOGV) Log.v(LOG_TAG, "Seems we're not connected.."); 
        }        

        infoMap.put("date", Tools.getTodaysDate());
        infoMap.put("time", Tools.getTodaysTime());
	  	infoMap.put("state", ni.getInfo("state"));            
	  	infoMap.put("type", ni.getInfo("type"));
	  	infoMap.put("netID", ni.getInfo("netID"));
	  	infoMap.put("speed", ni.getInfo("speed"));
	  	infoMap.put("interface", ni.getInfo("interface"));
	  	infoMap.put("ip", ni.getInfo("ip"));
	  	infoMap.put("gateway", ni.getInfo("gateway"));
	  	infoMap.put("dns", ni.getInfo("dns"));
	  	infoMap.put("roaming", ni.getInfo("roaming"));
	  	infoMap.put("bgdata", ni.getInfo("bgdata"));
	  	infoMap.put("cell_type", ni.getInfo("cell_type"));
	  	infoMap.put("cell_location", ni.getInfo("cell_location"));
	  	infoMap.put("data_activty", ni.getInfo("data_activity"));
	  	infoMap.put("phone_type", ni.getInfo("phone_type"));
	  	
	  	
	  	setContentView(R.layout.netmon_info);
	    TableLayout tl = (TableLayout) findViewById(R.id.maintable);
	    Tools.MakeTableLayout(this, infoMap, tl, "net_info");
    }
    
    /**
     * Create the menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.netinfo_menu, menu); 
        return true;
    }
    
    /**
     * Handle menu selections
     */    
    public boolean onOptionsItemSelected(MenuItem item) {
    	  switch (item.getItemId()) {
    	  case R.id.netmon_tools:
  		    	Intent ntActivity = new Intent(getBaseContext(), NetworkTools.class);
  		    	startActivity(ntActivity);		    
  		    	return true;
    	  case R.id.preferences:
    		  	Intent prefActivity = new Intent(getBaseContext(), EditPreferences.class);
  		    	startActivity(prefActivity);		    
  		    	return true;
    	  case R.id.show_log:
      			Intent logActivity = new Intent(getBaseContext(), ShowLog.class);
    		    startActivity(logActivity);		    
        	    return true;
    	  case R.id.clear_log:
    		  	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		  	builder.setMessage(getString(R.string.clear_log_alert_dialog_text))
    		         .setCancelable(false)
    		         .setPositiveButton(getString(R.string.clear_log_alert_dialog_ok), new DialogInterface.OnClickListener() {
    		             public void onClick(DialogInterface dialog, int id) {
    		            	 if(localLOGV) Log.v(LOG_TAG, "Chose ok in clear_alert_dialog");
    	    		       	 la.clearLog();
    		             }
    		         })
    		         .setNegativeButton(getString(R.string.clear_log_alert_dialog_cancel), new DialogInterface.OnClickListener() {
    		             public void onClick(DialogInterface dialog, int id) {
    		            	 if(localLOGV) Log.v(LOG_TAG, "Chose cancel in clear_alert_dialog");
    		                  dialog.cancel();
    		             }
    		         });
    		  		 AlertDialog alert = builder.create(); 
    		  		 alert.show();
          	    return true;
    	  case R.id.about_network_monitor:
    		    Dialog detail = new Dialog(this);
    		    detail.setTitle(R.string.about_network_monitor_title);
    		  	detail.setContentView(R.layout.about);
    		    // TextView textView = (TextView) detail.findViewById(R.id.about_text); 		  	    		  	
    		  	detail.show();		  
    	  default: 
    	    return super.onOptionsItemSelected(item);
    	  }
    }
}