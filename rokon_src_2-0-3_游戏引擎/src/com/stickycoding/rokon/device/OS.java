package com.stickycoding.rokon.device;

import android.os.Build;

import com.stickycoding.rokon.Debug;

public class OS {
	
	/**
	 * The API level. This shouldn't be changed unless you are sure what the consequences are.
	 */
	public static int API_LEVEL  = 0;
	
	/**
	 * Determines the API level, cross compatible with all SDK's.
	 * This shouldn't need to be called twice
	 */
	public static void determineAPI() {
		API_LEVEL = Integer.parseInt(Build.VERSION.SDK);
		Debug.print("API LEVEL " + Build.VERSION.SDK + " int=" + API_LEVEL);
		hackBrokenDevices();
	}
	
	public static void hackBrokenDevices() {
       if (Build.PRODUCT.contains("morrison") || Build.PRODUCT.contains("voles") || Build.PRODUCT.contains("umts_sholes") || Build.PRODUCT.contains("sdk")) {
    	   Graphics.setSupportsVBO(false);
       }
   }

}
