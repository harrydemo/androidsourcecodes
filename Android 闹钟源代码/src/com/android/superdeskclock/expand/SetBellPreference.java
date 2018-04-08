package com.android.superdeskclock.expand;

import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.util.AttributeSet;
import android.util.Log;

import com.android.superdeskclock.Alarm;
import com.android.superdeskclock.Alarms;
import com.android.superdeskclock.R;

public class SetBellPreference extends ListPreference{
	private Uri mAlert;
	private PreferenceActivity preferenceActivity;
	private int mId;
	public SetBellPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.preferenceActivity=(PreferenceActivity)context;
        String[] values = context.getResources().getStringArray(R.array.choose_bell);
        mId=preferenceActivity.getIntent().getIntExtra(Alarms.ALARM_ID, -1);
        setEntries(values);
        setEntryValues(values);
	}
	
	
	@Override
    protected void onPrepareDialogBuilder(Builder builder) {
        CharSequence[] entries = getEntries();
        
        builder.setSingleChoiceItems(entries, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						break;
					case 1:
						Intent intent = new Intent(preferenceActivity, ChooseBellActivity.class);
						intent.putExtra(Alarms.ALARM_ID, mId);
						intent.putExtra("TYPE", 1);
						preferenceActivity.startActivity(intent);
						preferenceActivity.finish();
						break;
					case 2:
						Intent intent2 = new Intent(preferenceActivity, ChooseBellActivity.class);
						intent2.putExtra(Alarms.ALARM_ID, mId);
						intent2.putExtra("TYPE", 2);
						preferenceActivity.startActivity(intent2);
						preferenceActivity.finish();
						break;
					default:
						break;
				}
			}
		});
    }
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			Alarm alarm=com.android.superdeskclock.Alarms.getAlarm(preferenceActivity.getContentResolver(),mId);
			alarm.silent=true;
			alarm.alert=Uri.parse("silent");
			mAlert=alarm.alert;
			ContentValues values = com.android.superdeskclock.Alarms.createContentValues(alarm);
			ContentResolver resolver = preferenceActivity.getContentResolver();
			resolver.update(ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, alarm.id),values, null, null);
			
			setSummary(R.string.silent_alarm_summary);
        }
	}
	
	public void setAlert(Uri alert) {
        mAlert = alert;
        if (alert != null) {
            final Ringtone r = RingtoneManager.getRingtone(getContext(), alert);
            if (r != null) {
                setSummary(r.getTitle(getContext()));
            }
        } else {
            setSummary(R.string.silent_alarm_summary);
        }
    }
	
	public Uri getAlert() {
        return mAlert;
    }
}
