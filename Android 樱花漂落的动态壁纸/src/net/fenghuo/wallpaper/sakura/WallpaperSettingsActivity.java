package net.fenghuo.wallpaper.sakura;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class WallpaperSettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.wallpaper_settings);
	}

}
