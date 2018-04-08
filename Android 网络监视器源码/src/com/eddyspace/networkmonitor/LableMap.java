package com.eddyspace.networkmonitor;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class LableMap {
	
	// A mapping of lable identifiers to lable strings
	private static Map<String,String> lableMap = new HashMap<String, String>();	
	
	// A list of lable identifiers in the order that they should be displayed
	private static String[] lableList = {
		"date",
		"time",
		"state", 
 		"type", 
		"netID", 
		"speed",
		"roaming",
		"bgdata",
		"interface",
		"ip",
		"data_activity",
		"cell_type",
		"cell_location",
		"phone_type",
		// "gateway", 
		// "dns",    	            
	};
	
	/**
	 * Constructor.  Generates a HashMap used by the class to return information
	 * about the network.
	 * 
	 */	
	public LableMap (Context context) {	
		// Setup the label mapping
		lableMap.put("date", context.getString(R.string.key_date_name));
		lableMap.put("time", context.getString(R.string.key_time_name));
		lableMap.put("state", context.getString(R.string.key_state_name));
		lableMap.put("interface", context.getString(R.string.key_interface_name));
		lableMap.put("type", context.getString(R.string.key_type_name));
		lableMap.put("netID", context.getString(R.string.key_netid_name));
		lableMap.put("roaming", context.getString(R.string.key_roaming_name));
		lableMap.put("bgdata", context.getString(R.string.key_bgdata_name));
		lableMap.put("ip", context.getString(R.string.key_ip_name));
		lableMap.put("gateway", context.getString(R.string.key_gateway_name));
		lableMap.put("speed", context.getString(R.string.key_speed_name));
		lableMap.put("dns", context.getString(R.string.key_dns_name));
		lableMap.put("data_activity", context.getString(R.string.key_data_activity_name));
		lableMap.put("cell_type", context.getString(R.string.key_cell_type_name));
		lableMap.put("cell_location", context.getString(R.string.key_cell_location_name));
		lableMap.put("phone_type", context.getString(R.string.key_phone_type_name));
	}
	
	/**
	 * Return the lable for a field
	 * 
	 * @param key	string key for the field
	 * @return		string lable for the key
	 */
    public String getLable(String key) {
    	return lableMap.get(key);
    }
    
    /**
     * Return a list of lable identifiers in the order they should be displayed
     * 
     * @return	string array of lable identifiers
     */
    public String[] getLableList() {
    	return lableList;
    }
}
