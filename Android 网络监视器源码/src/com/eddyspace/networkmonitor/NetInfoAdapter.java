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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.telephony.TelephonyManager;
import android.telephony.*;



/* I haven't found a way to get some of the values without resorting
 * to system tools.  Please let me know if you have found a different
 * way because I'd like to remove all use of mySystem below.
 * 
 * mySystem uses reflection to get access to the system binaries.
 */   

/** 
 * Helper class to provide network information to NetworkMonitor
 */
public class NetInfoAdapter {
	private static final String LOG_TAG = "NetworkMonitor.Tools";
	private final static boolean localLOGV = false;
	
	private static Map<String,String> netMap = new HashMap<String, String>();
	private static Map<Integer,String> phoneType = new HashMap<Integer, String>();
	private static Map<Integer,String> networkType = new HashMap<Integer, String>();
	private boolean netExists = false;
	
	/**
	 * Constructor.  Generates a HashMap used by the class to return information
	 * about the network.  
	 * 
	 * @param context	context we're working under
	 */
	NetInfoAdapter(Context context) {
		// Initialise some mappings
    	phoneType.put(0,"None");
    	phoneType.put(1,"GSM");
    	phoneType.put(2,"CDMA");
    	
    	networkType.put(0,"Unknown");
    	networkType.put(1,"GPRS");
    	networkType.put(2,"EDGE");
    	networkType.put(3,"UMTS");
    	networkType.put(4,"CDMA");
    	networkType.put(5,"EVDO_0");
    	networkType.put(6,"EVDO_A");
    	networkType.put(7,"1xRTT");
    	networkType.put(8,"HSDPA");
    	networkType.put(9,"HSUPA");
    	networkType.put(10,"HSPA");
    	networkType.put(11,"IDEN");

		// Initialise the network information mapping
		netMap.put("state","");
		netMap.put("interface", "");
		netMap.put("type", "");
		netMap.put("netID", "");
//		netMap.put("speed", "");
		netMap.put("roaming", "");
		netMap.put("ip", "");
//		netMap.put("gateway", "");
//		netMap.put("dns", "");    	
		netMap.put("bgdata", "");		
		netMap.put("data_activity", "n/a");
		netMap.put("cell_location", "n/a");	
		netMap.put("cell_type", "n/a");
		netMap.put("Phone_type", "n/a");

		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if( tm != null ) {
			netMap.put("data_activity", Integer.toString(tm.getDataActivity()));			
     		netMap.put("cell_location", tm.getCellLocation().toString());	
			netMap.put("cell_type", getNetworkType(tm.getNetworkType()));
			netMap.put("phone_type", getPhoneType(tm.getPhoneType()));
			
		}
    	// Find out if we're connected to a network
    	ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = (NetworkInfo) cm.getActiveNetworkInfo();         
    	if( ni != null && ni.isConnected() ) {
    		if(localLOGV) Log.v(LOG_TAG, "Network is connected");
    		
    		netExists = true;		
    		netMap.put("state", context.getString(R.string.connected));    	    		
    		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    		NetworkInterface intf = getInternetInterface();
    		netMap.put("interface", intf.getName());
    		netMap.put("ip", getIPAddress(intf));
    		String type = (String) ni.getTypeName();
    		if( wifi.isWifiEnabled() ) {
    			if(localLOGV) Log.v(LOG_TAG, "Wifi connected");
    			netMap.put("type", context.getString(R.string.net_type_wifi));    			
    			WifiInfo wi = wifi.getConnectionInfo();
    			netMap.put("netID", wi.getSSID());
    			netMap.put("speed", Integer.toString(wi.getLinkSpeed()) + "Mbps");    		
    			// netMap.put("gateway", Tools.mySystem("/system/bin/getprop", "dhcp." + intf.getName() + ".gateway", "").trim());
    		} else if(type.equals("MOBILE")) {
    			if(localLOGV) Log.v(LOG_TAG, "Mobile connected");
    			netMap.put("type", context.getString(R.string.net_type_mobile));    			
    			netMap.put("netID", ni.getExtraInfo());
    			netMap.put("bgdata", cm.getBackgroundDataSetting()? 
    	    			context.getString(R.string.permitted): 
    	    			context.getString(R.string.denied));
    			// netMap.put("gateway", Tools.mySystem("/system/bin/getprop", "net.rmnet0.gw", "").trim());
    			if( ni.isRoaming() ){
    				netMap.put("roaming", context.getString(R.string.roaming_yes));    				
    			} else {
    				netMap.put("roaming", context.getString(R.string.roaming_no));    
    			}    			
    		} else {
    			//Unsupported network type
    			if(localLOGV) Log.v(LOG_TAG, "Unknown/unsupported network type");
    			netMap.put("type", type + " " + context.getString(R.string.net_type_unsupported));
    		}
        	// netMap.put("dns", Tools.mySystem("/system/bin/getprop", "net.dns1", "").trim());
        } else {
        	netMap.put("state", context.getString(R.string.not_connected));
        	netMap.put("dns", "");
        }
	}

    /**
     * Return a string representation of the phone type given the
     * integer returned from TelephonyManager.getPhoneType()
     * 
     * @param key	key for the info required
     * @return		string information relating to the key
     */
    public String getPhoneType(Integer key) {    	
    	if( phoneType.containsKey(key) ) {
    		return phoneType.get(key);
    	} else {
    		return "unknown";
    	}
    }
    
    /**
     * Return a string representation of the network type type given the
     * integer returned from TelephonyManager.getNetworkType()
     * 
     * @param key	key for the info required
     * @return		string information relating to the key
     */
    public String getNetworkType(Integer key) {    	
    	if( networkType.containsKey(key) ) {
    		return networkType.get(key);
    	} else {
    		return "unknown";
    	}
    }


    /**
     * Return information relating to a key
     * 
     * @param key	key for the info required
     * @return		string information relating to the key
     */
    public String getInfo(String key) {    	
    	return exists(key)? netMap.get(key): "";
    }
    
    /**
     * Returns if this key exists in the HashMap
     * 
     * @param key	key to look for
     * @return		boolean exits or not
     */
    public boolean exists(String key) {
    	return netMap.containsKey(key);
    }
    
    /**
     * Returns the network state
     * 
     * @return	boolean connected or not connected
     */
    public boolean isConnected() {
    	return netExists; 
    }
    
    /**
     * Returns the IP address of the supplied network interface
     * 
     * @param intf	interface to check
     * @return		dotted quad IP address
     */
    private static String getIPAddress( NetworkInterface intf) {
    	String result = "";
    	for( Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
    		InetAddress inetAddress = enumIpAddr.nextElement();
    		result = inetAddress.getHostAddress();
    	}
    	return result;
    }
    
    /**
     * Return the first network interface found that isn't localhost
     * 
     * @return	first working internet interface found
     */
    private static NetworkInterface getInternetInterface() {
    	try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				if( ! intf.equals(NetworkInterface.getByName("lo"))) {
					return intf;	
				}				
			}
		} catch (SocketException ex) {			
			Log.w(LOG_TAG,  ex.toString());
		}
        return null;
    }
}
