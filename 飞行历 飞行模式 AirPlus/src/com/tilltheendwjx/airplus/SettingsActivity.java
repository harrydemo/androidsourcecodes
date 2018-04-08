/**
 * 
 */
package com.tilltheendwjx.airplus;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;

/**
 * @author wjx
 * 
 */
public class SettingsActivity extends PreferenceActivity implements
		OnPreferenceChangeListener {
	static final String KEY_AUTO_AIR = "auto_air";
	static final String KEY_AIR_SNOOZE = "snooze_duration";
	static final String KEY_VOLUME_BEHAVIOR = "volume_button_setting";

	/**
	 * 
	 */
	public SettingsActivity() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.Preference.OnPreferenceChangeListener#onPreferenceChange
	 * (android.preference.Preference, java.lang.Object)
	 */
	@Override
	public boolean onPreferenceChange(Preference pref, Object newValue) {
		if (KEY_AIR_SNOOZE.equals(pref.getKey())) {
			final ListPreference listPref = (ListPreference) pref;
			final int idx = listPref.findIndexOfValue((String) newValue);
			listPref.setSummary(listPref.getEntries()[idx]);
		} else if (KEY_AUTO_AIR.equals(pref.getKey())) {
			final ListPreference listPref = (ListPreference) pref;
			String delay = (String) newValue;
			updateAutoAirSummary(listPref, delay);
		} else if (KEY_VOLUME_BEHAVIOR.equals(pref.getKey())) {
			final ListPreference listPref = (ListPreference) pref;
			final int idx = listPref.findIndexOfValue((String) newValue);
			listPref.setSummary(listPref.getEntries()[idx]);
		}
		return true;
	}

	private void updateAutoAirSummary(ListPreference listPref, String delay) {
		int i = Integer.parseInt(delay);
		if (i == -1) {
			listPref.setSummary(R.string.auto_air_never);
		} else {
			listPref.setSummary(listPref.getEntries()[listPref
					.findIndexOfValue(delay)]
					+ getString(R.string.auto_air_summary));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onPreferenceTreeClick(android.
	 * preference.PreferenceScreen, android.preference.Preference)
	 */
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		ListPreference listPref = (ListPreference) findPreference(KEY_AIR_SNOOZE);
		listPref.setSummary(listPref.getEntry());
		listPref.setOnPreferenceChangeListener(this);

		listPref = (ListPreference) findPreference(KEY_AUTO_AIR);
		String delay = listPref.getValue();
		updateAutoAirSummary(listPref, delay);
		listPref.setOnPreferenceChangeListener(this);

		listPref = (ListPreference) findPreference(KEY_VOLUME_BEHAVIOR);
		listPref.setSummary(listPref.getEntry());
		listPref.setOnPreferenceChangeListener(this);
	}

}
