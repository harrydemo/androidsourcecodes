/**
 * 
 */
package com.tilltheendwjx.airplus;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * @author wjx
 * 
 */
public class RepeatPreference extends ListPreference {

	private Air.DaysOfWeek mDaysOfWeek = new Air.DaysOfWeek(0);
	// New value that will be set if a positive result comes back from the
	// dialog.
	private Air.DaysOfWeek mNewDaysOfWeek = new Air.DaysOfWeek(0);

	public RepeatPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		String[] weekdays = new DateFormatSymbols().getWeekdays();
		String[] values = new String[] { weekdays[Calendar.MONDAY],
				weekdays[Calendar.TUESDAY], weekdays[Calendar.WEDNESDAY],
				weekdays[Calendar.THURSDAY], weekdays[Calendar.FRIDAY],
				weekdays[Calendar.SATURDAY], weekdays[Calendar.SUNDAY], };
		setEntries(values);
		setEntryValues(values);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.ListPreference#onDialogClosed(boolean)
	 */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			mDaysOfWeek.set(mNewDaysOfWeek);
			setSummary(mDaysOfWeek.toString(getContext(), true));
			callChangeListener(mDaysOfWeek);
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
		builder.setMultiChoiceItems(entries, mDaysOfWeek.getBooleanArray(),
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						mNewDaysOfWeek.set(which, isChecked);
					}
				});
	}

	public void setDaysOfWeek(Air.DaysOfWeek dow) {
		mDaysOfWeek.set(dow);
		mNewDaysOfWeek.set(dow);
		setSummary(dow.toString(getContext(), true));
	}

	public Air.DaysOfWeek getDaysOfWeek() {
		return mDaysOfWeek;
	}

}
