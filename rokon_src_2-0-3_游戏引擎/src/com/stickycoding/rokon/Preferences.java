package com.stickycoding.rokon;

import android.content.SharedPreferences;

/**
 * Preferences.java
 * Just a helper for SharedPreferences, cuts down on a bit of in-game code 
 * 
 * @author Richard
 */

public class Preferences {

	/**
	 * This is the file to which SharedPreferences are saved through here. 
	 */
	public static final String file = "rokon_prefs";
	
	/**
	 * The SharedPreferences object can be access directly from your game
	 */
	public static SharedPreferences object = Rokon.getActivity().getSharedPreferences(file, 0);
	
}
