package cn.sharp.android.ncr;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
/**
 * …Ë÷√
 * @author shao chuanchao
 *
 */
public class PrefSettings extends PreferenceActivity implements
		OnPreferenceChangeListener {
	public final static String PREF_AUTO_FOCUS = "pref_auto_focus";
	public final static String PREF_AUTO_REC = "pref_auto_rec";
	public final static String PREF_AUTO_DETECT = "pref_auto_detect";
	public final static String PREF_SAVE_REC_IMAGE = "pref_save_rec_image";
	public final static String PREF_REC_PROGRESS_STYLE = "rec_progress_style";
	private Preference autoRecPref;
	private CheckBoxPreference autoDetectPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.pref_settings);
		autoRecPref = getPreferenceScreen().getPreference(1);
		autoDetectPref = (CheckBoxPreference) getPreferenceScreen()
				.getPreference(2);

		autoRecPref.setOnPreferenceChangeListener(this);

		boolean autoRecChecked = this.getPreferenceScreen()
				.getSharedPreferences().getBoolean(PREF_AUTO_REC, false);
		autoDetectPref.setEnabled(!autoRecChecked);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == autoRecPref) {
			if (newValue != null) {
				boolean value = Boolean.parseBoolean(newValue.toString());
				/**
				 * auto-rec-pref item checked
				 */
				if (value) {
					/**
					 * set auto-detect pref item checked
					 */
					autoDetectPref.setEnabled(false);
					autoDetectPref.setChecked(true);
				} else {
					autoDetectPref.setEnabled(true);
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

}
