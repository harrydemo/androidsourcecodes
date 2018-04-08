package com.orange.gpstest;

import android.content.SharedPreferences;

/**
 * This class represents the settings data
 * @author Alan Sowamber
 *
 */
public class Settings {
	
	public String TestFile;
	public String TestDirectory;
	MainApp mainApp;
	boolean appendTime;
	
	/**
	 * Create settings from last saved settings in preference file
	 * @param mainApp Reference to the application
	 */
	public Settings(MainApp mainApp){
		
		this.mainApp=mainApp;
		
		SharedPreferences fsettings = mainApp.getSharedPreferences("TestSettings", 0);
		
		TestFile=fsettings.getString("filename", "testfile");
		TestDirectory=fsettings.getString("directory", "\\sdcard\\gpstestfiles");
		
		appendTime=true;
		
	}
	
	/**
	 * Save current settings in preference file
	 */
	public void SaveData()
	{
		SharedPreferences fsettings = mainApp.getSharedPreferences("TestSettings", 0);
		SharedPreferences.Editor editor= fsettings.edit();
		editor.putString("filename", TestFile);
		editor.putString("directory", TestDirectory);
		
		editor.commit();
		
	}

}
