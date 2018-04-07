/**
 * 
 */
package com.tilltheendwjx.airplus;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * @author wjx
 * 
 */
public class AirModeRadioPreference extends ListPreference {

	private Air.AirModeRadio mAirModeRadio = new Air.AirModeRadio("");
	// New value that will be set if a positive result comes back from the
	// dialog.
	private Air.AirModeRadio mNewAirModeRadio = new Air.AirModeRadio("");

	public AirModeRadioPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		String[] entries = new String[] { "cell", "bluetooth", "wifi" };
		String[] values = entries;
		this.setEntries(entries);
		this.setEntryValues(values);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.ListPreference#onDialogClosed(boolean)
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			mAirModeRadio.set(mNewAirModeRadio);
			setSummary(mAirModeRadio.toString(getContext(), true));
			callChangeListener(mAirModeRadio);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.preference.ListPreference#onPrepareDialogBuilder(android.app.
	 * AlertDialog.Builder)
	 */
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		CharSequence[] entries = getEntries();
		// CharSequence[] entryValues = getEntryValues();
		builder.setMultiChoiceItems(entries, mAirModeRadio.getBooleanArray(),
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						mNewAirModeRadio.set(which, isChecked);
					}
				});
	}

	public void setAirModeRadio(Air.AirModeRadio dow) {
		mAirModeRadio.set(dow);
		mNewAirModeRadio.set(dow);
		setSummary(dow.toString(getContext(), true));
	}

	public Air.AirModeRadio getAirModeRadio() {
		return mAirModeRadio;
	}

}
