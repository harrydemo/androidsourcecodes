package org.app.music;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class LocalSettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
 
}
