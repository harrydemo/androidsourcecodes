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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkMonitor extends BroadcastReceiver {
    private static final String LOG_TAG = "NetworkMonitor.NetworkMonitor";
	private final static boolean localLOGV = true;
	public static final String PREFS_NAME = "com.eddyspace.networkmonitor_preferences";
    
	/** Called when the broadcast is received. */
	public void onReceive(Context context, Intent intent) {
        if(localLOGV) Log.v(LOG_TAG, "On Receive");
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE );

        // Get the network information
        NetInfoAdapter ni = new NetInfoAdapter(context);                  
        
        String tickerText = context.getString(R.string.not_connected);
        if( ni.isConnected()) {
        	tickerText = context.getString(R.string.connected) + " " + ni.getInfo("type");;
        	if( ni.exists("netID")) {
        		tickerText = tickerText + " " + ni.getInfo("netID");
        	}
        	if( ni.exists("speed")) {
        		tickerText = tickerText + " " + ni.getInfo("speed");
        	}
        }
        
        // for some reason, this gets called when the network state has
        // not really changed.  The log ends up with multiple "disconnect"
        // lines.  I think it's the phone going to sleep and waking up
        // but I can't be sure.  Anyway, the routine AddToLog below
        // calls appendUniqueLine in the log adaptor which returns
        // false if the "state" field in the new log line is the same as the 
        // "state" field in the last line already in the log.  We can use
        // this to tell if state really has changed.  This should remove
        // spurious notifications.
        
        // Start with an indicator that is true by default (because we might not
        // be logging at all) and negate it if the log line is not appended 
        // (ie state hasn't really changed)
        boolean stateChanged = true;
        if(prefs.getBoolean("preference_log", true)) {            
        	if(localLOGV) Log.v(LOG_TAG, "Log file name is: " + prefs.getString("preference_log_name", context.getString(R.string.preference_log_name_default)));
        	if( AddToLog(context, ni) ) {
        		if(localLOGV) Log.v(LOG_TAG, "Line added to log");
        	} else {
        		stateChanged = false;
        		if(localLOGV) Log.v(LOG_TAG, "Line not unique");
        	}
        } else {
        	if(localLOGV) Log.v(LOG_TAG, "Not logging");
        }
        
        //  Only notify if state really has changed
        if( prefs.getBoolean("preference_monitor", true) && stateChanged ) {
            String notifyType = prefs.getString("preference_monitor_type", "Status Bar");
            if(localLOGV) Log.v(LOG_TAG, "Monitoring is turned on");
            if( notifyType.equals("Status Bar") ) {
            	if(localLOGV) Log.v(LOG_TAG, "Notify using status bar");
            	showNotification(context, tickerText);
            } else {
            	if(localLOGV) Log.v(LOG_TAG, "Notify using toast");
        	    showToast(context, (String) tickerText);
            }
        }
	}

	/**
	* Show notification using Toast
 	* 
	* @param context	the context we're working with
	* @param message	the message to display
	* @return 			Nothing
	*/
	protected void showToast(Context context, String message) {
        // create the view
		if(localLOGV) Log.v(LOG_TAG, "Showing toast " + message);
		View view = inflateView(context, R.layout.netmon_toast);

        // set the text in the view
        TextView tv = (TextView)view.findViewById(R.id.message);
        tv.setText(message);

        // show the toast
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
	
	/**
	* Show notification using notifications
 	* 
	* @param context	the context we're working with
	* @param message	the message to display
	* @return 			Nothing
	*/
    protected void showNotification(Context context, CharSequence message) {    
        NotificationManager nm;
        final int NM_ID = 1;
        
        if(localLOGV) Log.v(LOG_TAG, "Showing notification " + message);
    	nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    	long when = System.currentTimeMillis();
    	Notification notifyDetails = new Notification(R.drawable.netmon_notify ,message, when);    
    	    	
    	Intent ShowInfoIntent  = new Intent(context, NetworkInfo.class);    	
    	PendingIntent myPendingIntent = PendingIntent.getActivity(context, 0, ShowInfoIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);    	

    	notifyDetails.setLatestEventInfo(context, context.getText(R.string.net_connectivity_changed), context.getText(R.string.more_info), myPendingIntent);
    	notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
    	nm.notify(NM_ID, notifyDetails);    	    		
    }
    
    // Helper to inflate the toast "view"
    private View inflateView(Context context, int resource) {
        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return vi.inflate(resource, null);
    }
    
    /**
	* Log the network change
	*
	* @param context	the context we're working with
	* @param ni			NetInfoAdapter to retrieve information from
	* @return 			boolean success or failure
	*/
    private boolean AddToLog(Context context, NetInfoAdapter ni) {
    	LogAdapter la = new LogAdapter(context);
    	
    	// Use LableMap to keep everything in the same order..    	
    	String[] lable = new LableMap(context).getLableList();
    	int i = 2;  // Lables start at 0 but we're only using 2 onwards..
    	return la.appendUniqueLine(Tools.join(",", new String[] { 
    			Tools.getTodaysDate(), 
    			Tools.getTodaysTime(), 
    			ni.getInfo(lable[i++]), 
    			ni.getInfo(lable[i++]), 
    			ni.getInfo(lable[i++]), 
    			ni.getInfo(lable[i++]),  
    			ni.getInfo(lable[i++]), 
    			ni.getInfo(lable[i++]), 
    			ni.getInfo(lable[i++]), 
    			ni.getInfo(lable[i++]),    			
    			ni.getInfo(lable[i++]), 
    			ni.getInfo(lable[i++])
    	}));    	            	  	
    }
}