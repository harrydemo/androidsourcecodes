/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cn.daming.deskclock;

import android.media.AudioManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;

/**
 * Settings for the Alarm Clock.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private static final int ALARM_STREAM_TYPE_BIT =
            1 << AudioManager.STREAM_ALARM;

    private static final String KEY_ALARM_IN_SILENT_MODE =
            "alarm_in_silent_mode";
    static final String KEY_ALARM_SNOOZE =
            "snooze_duration";
    static final String KEY_VOLUME_BEHAVIOR =
            "volume_button_setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (KEY_ALARM_IN_SILENT_MODE.equals(preference.getKey())) {
            CheckBoxPreference pref = (CheckBoxPreference) preference;
            int ringerModeStreamTypes = Settings.System.getInt(
                    getContentResolver(),
                    Settings.System.MODE_RINGER_STREAMS_AFFECTED, 0);

            if (pref.isChecked()) {
                ringerModeStreamTypes &= ~ALARM_STREAM_TYPE_BIT;
            } else {
                ringerModeStreamTypes |= ALARM_STREAM_TYPE_BIT;
            }

            Settings.System.putInt(getContentResolver(),
                    Settings.System.MODE_RINGER_STREAMS_AFFECTED,
                    ringerModeStreamTypes);

            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference pref, Object newValue) {
        final ListPreference listPref = (ListPreference) pref;
        final int idx = listPref.findIndexOfValue((String) newValue);
        listPref.setSummary(listPref.getEntries()[idx]);
        return true;
    }

    private void refresh() {
        final CheckBoxPreference alarmInSilentModePref =
                (CheckBoxPreference) findPreference(KEY_ALARM_IN_SILENT_MODE);
        final int silentModeStreams =
                Settings.System.getInt(getContentResolver(),
                        Settings.System.MODE_RINGER_STREAMS_AFFECTED, 0);
        alarmInSilentModePref.setChecked(
                (silentModeStreams & ALARM_STREAM_TYPE_BIT) == 0);

        final ListPreference snooze =
                (ListPreference) findPreference(KEY_ALARM_SNOOZE);
        snooze.setSummary(snooze.getEntry());
        snooze.setOnPreferenceChangeListener(this);
    }

}
