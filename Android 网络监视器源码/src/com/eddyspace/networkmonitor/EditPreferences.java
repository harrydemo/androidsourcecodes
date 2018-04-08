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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * Class used to edit preferences in NetworkMonitor
 * 
 * @author davo
 *
 */
public class EditPreferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	private static final String LOG_TAG = "NetworkMonitor.EditPreferences";
	private final static boolean localLOGV = false;
	
	/**
	 * Expand the xml preferences view
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if(localLOGV) Log.v(LOG_TAG, "Generating preferences dialog");       
		addPreferencesFromResource(R.xml.preferences);
	}
	
	/**
	 * Enable an onchange listener
	 */
    @Override
    protected void onResume() {
        super.onResume();
        if(localLOGV) Log.v(LOG_TAG, "On Resume");
        
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    
    /**
     * Disable the onchange listener
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    
    /**
     * OnChange listener
     * @param settings	shared preferences object
     * @param key		which item was chose
     */	
	public void onSharedPreferenceChanged(SharedPreferences settings, String key) {
		if(localLOGV) Log.v(LOG_TAG, "SharedPreferenceChanged,  Key: " + key);		
       
		if( key.equals("preference_monitor") ) {
			if(localLOGV) Log.v(LOG_TAG, "  Matched preference_monitor");
			PackageManager pm = getPackageManager();
			ComponentName cn = new ComponentName(getApplicationContext(), NetworkMonitor.class);
			if( settings.getBoolean(key,true)) {
				if(localLOGV) Log.v(LOG_TAG, "  Key is true");
				pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);				
			} else {
				if(localLOGV) Log.v(LOG_TAG, "  Key is false");
				pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);				
			}			
		}
		if( key.equals("preference_monitor_type")) {
			if(localLOGV) Log.v(LOG_TAG, "  Matched preference_monitor_type: " + settings.getString(key, "default"));			
		}
		if( key.equals("preference_log")) {
			if(localLOGV) Log.v(LOG_TAG, "  Matched preference_log: " + settings.getBoolean(key, true));
		}
		if( key.equals("preference_log_name")) {
			if(localLOGV) Log.v(LOG_TAG, "  Matched preference_log_name" + settings.getString(key, "default"));
			LogAdapter la = new LogAdapter(getBaseContext());
			la.getWriteableFile();
		}
		if( key.equals("preference_ping_host")) {
			if(localLOGV) Log.v(LOG_TAG, "  Matched preference_ping_host" + settings.getString(key, "default"));
		}				
	}
}
