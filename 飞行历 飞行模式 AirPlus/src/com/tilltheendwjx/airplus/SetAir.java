/**
 * 
 */
package com.tilltheendwjx.airplus;

import com.tilltheendwjx.airplus.utils.Log;
import com.tilltheendwjx.airplus.utils.ToastMaster;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * @author wjx
 * 
 */
public class SetAir extends PreferenceActivity implements
		OnPreferenceChangeListener, OnTimeSetListener {

	private static final String KEY_CURRENT_AIR = "currentAir";
	private static final String KEY_ORIGINAL_AIR = "originalAir";
	private static final String KEY_START_TIME_PICKER_BUNDLE = "startTimePickerBundle";
	private static final String KEY_END_TIME_PICKER_BUNDLE = "endTimePickerBundle";

	private CheckBoxPreference mEnabledPref;
	private static final Handler sHandler = new Handler();
	private EditText mLabel;

	private Preference mStartTimePref;
	private Preference mEndTimePref;
	private CheckBoxPreference mVibratePref;
	private RepeatPreference mRepeatPref;
	private AirModeRadioPreference mAirModeRadioPref;

	private int mId;
	private int mStartHour;
	private int mStartMinute;
	private int mEndHour;
	private int mEndMinute;
	private TimePickerDialog mStartTimePickerDialog;
	private TimePickerDialog mEndTimePickerDialog;

	private int StartOrEnd = 0;

	private Air mOriginalAir;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_air);
		EditText label = (EditText) getLayoutInflater().inflate(
				R.layout.air_label, null);
		ListView list = (ListView) findViewById(android.R.id.list);
		list.addFooterView(label);
		addPreferencesFromResource(R.xml.air_prefs);

		mEnabledPref = (CheckBoxPreference) findPreference("enabled");
		mEnabledPref.setOnPreferenceChangeListener(this);
		mLabel = label;
		mStartTimePref = findPreference("start_time");
		mEndTimePref = findPreference("end_time");
		mVibratePref = (CheckBoxPreference) findPreference("vibrate");
		mVibratePref.setOnPreferenceChangeListener(this);
		mRepeatPref = (RepeatPreference) findPreference("setRepeat");
		mRepeatPref.setOnPreferenceChangeListener(this);
		mAirModeRadioPref = (AirModeRadioPreference) findPreference("airModeRadio");
		mAirModeRadioPref.setOnPreferenceChangeListener(this);

		mStartHour = mStartMinute = 0;
		mEndHour = mEndMinute = 0;

		Intent i = getIntent();
		Air air = i.getParcelableExtra(Airs.AIR_INTENT_EXTRA);

		if (air == null) {
			air = new Air();
		}
		mOriginalAir = air;

		updatePrefs(mOriginalAir);
		// We have to do this to get the save/cancel buttons to highlight on
		// their own.
		getListView().setItemsCanFocus(true);
		// Attach actions to each button.
		Button b = (Button) findViewById(R.id.air_save);
		b.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				long time = saveAir(null);
				if (mEnabledPref.isChecked()) {
					popAirSetToast(SetAir.this, time);
				}
				finish();
			}
		});
		Button revert = (Button) findViewById(R.id.air_revert);
		revert.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				revert();
				finish();
			}
		});
		b = (Button) findViewById(R.id.air_delete);
		if (mId == -1) {
			b.setEnabled(false);
			b.setVisibility(View.GONE);
		} else {
			b.setVisibility(View.VISIBLE);
			b.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					deleteAir();
				}
			});
		}

	}

	private void deleteAir() {
		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.delete_air))
				.setMessage(getString(R.string.delete_air_confirm))
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface d, int w) {
								Airs.deleteAir(SetAir.this, mId);
								finish();
							}
						}).setNegativeButton(android.R.string.cancel, null)
				.show();
	}

	private void revert() {
		int newId = mId;
		// "Revert" on a newly created alarm should delete it.
		if (mOriginalAir.id == -1) {
			Airs.deleteAir(SetAir.this, newId);
		} else {
			saveAir(mOriginalAir);
		}
	}

	static void popAirSetToast(Context context, int hour, int minute,
			Air.DaysOfWeek daysOfWeek) {
		popAirSetToast(context, Airs.calculateAir(hour, minute, daysOfWeek)
				.getTimeInMillis());
	}

	static void popAirSetToast(Context context, long timeInMillis) {
		String toastText = formatToast(context, timeInMillis);
		Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_LONG);
		ToastMaster.setToast(toast);
		toast.show();
	}

	/**
	 * format "Alarm set for 2 days 7 hours and 53 minutes from now"
	 */
	static String formatToast(Context context, long timeInMillis) {
		long delta = timeInMillis - System.currentTimeMillis();
		long hours = delta / (1000 * 60 * 60);
		long minutes = delta / (1000 * 60) % 60;
		long days = hours / 24;
		hours = hours % 24;

		String daySeq = (days == 0) ? "" : (days == 1) ? context
				.getString(R.string.day) : context.getString(R.string.days,
				Long.toString(days));

		String minSeq = (minutes == 0) ? "" : (minutes == 1) ? context
				.getString(R.string.minute) : context.getString(
				R.string.minutes, Long.toString(minutes));

		String hourSeq = (hours == 0) ? "" : (hours == 1) ? context
				.getString(R.string.hour) : context.getString(R.string.hours,
				Long.toString(hours));

		boolean dispDays = days > 0;
		boolean dispHour = hours > 0;
		boolean dispMinute = minutes > 0;

		int index = (dispDays ? 1 : 0) | (dispHour ? 2 : 0)
				| (dispMinute ? 4 : 0);

		String[] formats = context.getResources().getStringArray(
				R.array.air_set);
		return String.format(formats[index], daySeq, hourSeq, minSeq);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.PreferenceActivity#onRestoreInstanceState(android.
	 * os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		Air airFromBundle = state.getParcelable(KEY_ORIGINAL_AIR);
		if (airFromBundle != null) {
			mOriginalAir = airFromBundle;
		}
		airFromBundle = state.getParcelable(KEY_CURRENT_AIR);
		if (airFromBundle != null) {
			updatePrefs(airFromBundle);
		}
		Bundle startBundle = state.getParcelable(KEY_START_TIME_PICKER_BUNDLE);
		if (startBundle != null) {
			mStartTimePickerDialog.onRestoreInstanceState(startBundle);
		}
		Bundle endBundle = state.getParcelable(KEY_END_TIME_PICKER_BUNDLE);
		if (endBundle != null) {
			mStartTimePickerDialog.onRestoreInstanceState(endBundle);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.PreferenceActivity#onSaveInstanceState(android.os.
	 * Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(KEY_ORIGINAL_AIR, mOriginalAir);
		outState.putParcelable(KEY_CURRENT_AIR, buildAirFromUi());
		if (mStartTimePickerDialog != null) {
			if (mStartTimePickerDialog.isShowing()) {
				outState.putParcelable(KEY_START_TIME_PICKER_BUNDLE,
						mStartTimePickerDialog.onSaveInstanceState());
				mStartTimePickerDialog.dismiss();
			}
			mStartTimePickerDialog = null;
		}
		if (mEndTimePickerDialog != null) {
			if (mEndTimePickerDialog.isShowing()) {
				outState.putParcelable(KEY_END_TIME_PICKER_BUNDLE,
						mEndTimePickerDialog.onSaveInstanceState());
				mEndTimePickerDialog.dismiss();
			}
			mEndTimePickerDialog = null;
		}
	}

	private void updatePrefs(Air air) {
		mId = air.id;
		mEnabledPref.setChecked(air.enabled);
		mLabel.setText(air.label);
		mStartHour = air.start_hour;
		mStartMinute = air.start_minutes;
		mEndHour = air.end_hour;
		mEndMinute = air.end_minutes;
		mRepeatPref.setDaysOfWeek(air.daysOfWeek);
		mAirModeRadioPref.setAirModeRadio(air.air_mode_radios);
		mVibratePref.setChecked(air.vibrate);
		// Give the alert uri to the preference.
		// mAirPref.setAlert(Air.alert);
		updateTime();
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
		if (preference == mStartTimePref) {
			this.StartOrEnd = 1;
			showStartTimePicker();

		}
		if (preference == mEndTimePref) {
			this.StartOrEnd = 2;
			showEndTimePicker();

		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private void showStartTimePicker() {
		if (mStartTimePickerDialog != null) {
			if (mStartTimePickerDialog.isShowing()) {
				Log.e("mStartTimePickerDialog is already showing.");
				mStartTimePickerDialog.dismiss();
			} else {
				Log.e("mStartTimePickerDialog is not null");
			}
			mStartTimePickerDialog.dismiss();
		}

		mStartTimePickerDialog = new TimePickerDialog(this, this, mStartHour,
				mStartMinute, DateFormat.is24HourFormat(this));
		mStartTimePickerDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						mStartTimePickerDialog = null;

					}
				});
		mStartTimePickerDialog.show();
	}

	private void showEndTimePicker() {
		if (mEndTimePickerDialog != null) {
			if (mEndTimePickerDialog.isShowing()) {
				Log.e("mEndTimePickerDialog is already showing.");
				mEndTimePickerDialog.dismiss();
			} else {
				Log.e("mEndTimePickerDialog is not null");
			}
			mEndTimePickerDialog.dismiss();
		}

		mEndTimePickerDialog = new TimePickerDialog(this, this, mEndHour,
				mEndMinute, DateFormat.is24HourFormat(this));
		mEndTimePickerDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						mEndTimePickerDialog = null;

					}
				});
		mEndTimePickerDialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.app.TimePickerDialog.OnTimeSetListener#onTimeSet(android.widget
	 * .TimePicker, int, int)
	 */
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		if (StartOrEnd == 1) {
			mStartTimePickerDialog = null;
			mStartHour = hourOfDay;
			mStartMinute = minute;
		} else if (StartOrEnd == 2) {
			mEndTimePickerDialog = null;
			mEndHour = hourOfDay;
			mEndMinute = minute;
		} else {
			return;
		}
		updateTime();
		mEnabledPref.setChecked(true);
		StartOrEnd = 0;
	}

	private void updateTime() {
		if (StartOrEnd == 1) {
			mStartTimePref.setSummary(Airs.formatTime(this, mStartHour,
					mStartMinute, mRepeatPref.getDaysOfWeek()));
		} else if (StartOrEnd == 2) {
			mEndTimePref.setSummary(Airs.formatTime(this, mEndHour, mEndMinute,
					mRepeatPref.getDaysOfWeek()));
		} else if (StartOrEnd == 0) {
			mStartTimePref.setSummary(Airs.formatTime(this, mStartHour,
					mStartMinute, mRepeatPref.getDaysOfWeek()));
			mEndTimePref.setSummary(Airs.formatTime(this, mEndHour, mEndMinute,
					mRepeatPref.getDaysOfWeek()));
		} else {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.Preference.OnPreferenceChangeListener#onPreferenceChange
	 * (android.preference.Preference, java.lang.Object)
	 */
	@Override
	public boolean onPreferenceChange(final Preference p, Object newValue) {

		sHandler.post(new Runnable() {
			public void run() {
				// Editing any preference (except enable) enables the Air.
				if (p != mEnabledPref) {
					mEnabledPref.setChecked(true);
				}
				saveAir(null);
			}
		});
		return true;
	}

	private Air buildAirFromUi() {
		Air air = new Air();
		air.id = mId;
		air.enabled = mEnabledPref.isChecked();
		air.start_hour = mStartHour;
		air.start_minutes = mStartMinute;
		air.end_hour = mEndHour;
		air.end_minutes = mEndMinute;
		air.daysOfWeek = mRepeatPref.getDaysOfWeek();
		air.vibrate = mVibratePref.isChecked();
		air.label = mLabel.getText().toString();
		air.air_mode_radios = mAirModeRadioPref.getAirModeRadio();
		// air.alert = mairPref.getAlert();
		return air;
	}

	private long saveAir(Air Air) {
		if (Air == null) {
			Air = buildAirFromUi();
		}

		long time = 1;
		if (Air.id == -1) {
			time = Airs.addAir(this, Air);
			// addAir populates the Air with the new id. Update mId so that
			// changes to other preferences update the new Air.
			mId = Air.id;
		} else {
			time = Airs.setAir(this, Air);
		}
		return time;
	}

}
