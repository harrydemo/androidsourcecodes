package com.genius.demo;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;

public class SettingActivity extends PreferenceActivity{

	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
        
 
        addPreferencesFromResource(R.xml.setting_preference);

    }
}
