package com.zhuyan;

import java.util.Calendar;

import com.zhuyan.broadcast.AlarmReceiver;
import com.zhuyan.dialog.LongClickDialog;

import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimingDialActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String TAG = "TimingDialActivity";
	Button mButtonStart;
	Button mButtonStop;
	EditText interval;
	EditText telPhoneEditText;

	LongClickDialog dialog;

	TextView mTextView;

	Calendar calendar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		calendar = Calendar.getInstance();

		mTextView = (TextView) findViewById(R.id.phone_view);
		mButtonStart = (Button) findViewById(R.id.set_time);
		mButtonStop = (Button) findViewById(R.id.cancel_time);
		interval = (EditText) findViewById(R.id.interval);
		telPhoneEditText = (EditText) findViewById(R.id.tel_number);
		dialog = new LongClickDialog(this, telPhoneEditText);
		telPhoneEditText.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "");
				dialog.show();
				return true;
			}

		});

		mButtonStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				calendar.setTimeInMillis(System.currentTimeMillis());
				int mHour = calendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = calendar.get(Calendar.MINUTE);
				new TimePickerDialog(TimingDialActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								calendar.setTimeInMillis(System
										.currentTimeMillis());
								calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								calendar.set(Calendar.MINUTE, minute);
								calendar.set(Calendar.SECOND, 0);
								calendar.set(Calendar.MILLISECOND, 0);

								Intent intent = new Intent(
										TimingDialActivity.this,
										AlarmReceiver.class);
								PendingIntent pendingIntent = PendingIntent
										.getBroadcast(TimingDialActivity.this,
												0, intent, 0);
								AlarmManager am;
								am = (AlarmManager) getSystemService(ALARM_SERVICE);
								am.set(AlarmManager.RTC_WAKEUP,
										calendar.getTimeInMillis(),
										pendingIntent);
								String tmpS = telPhoneEditText.getText()
										.toString()
										+ "->"
										+ format(hourOfDay)
										+ ":" + format(minute);
								int tempInterval = 0;
								if (!interval.getText().toString().equals("")) {
									tempInterval = Integer.valueOf(interval
											.getText().toString());
								}

								SharedPreferences preference = getSharedPreferences(
										"zhuyan", Context.MODE_PRIVATE);
								Editor edit = preference.edit();
								edit.putString("tel_phone", telPhoneEditText
										.getText().toString());
								edit.putString("info", mTextView.getText()
										.toString());
								if (tempInterval != 0) {
									edit.putLong("time",
											calendar.getTimeInMillis()
													+ tempInterval * 60 * 1000);
									tmpS += "//interval time is "
											+ tempInterval + "minters";
								}else{
									edit.putLong("time",0);
								}
								edit.commit();
								mTextView.setText(tmpS);
							}
						}, mHour, mMinute, true).show();
			}
		});

		mButtonStop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(TimingDialActivity.this,
						AlarmReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						TimingDialActivity.this, 0, intent, 0);
				AlarmManager am;
				am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.cancel(pendingIntent);
				mTextView.setText("Cancelled");
			}
		});
	}

	/* ��ʽ���ַ�(7:3->07:03) */
	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		SharedPreferences preference = getSharedPreferences("zhuyan",
				Context.MODE_PRIVATE);
		telPhoneEditText.setText(preference.getString("tel_phone", "10086"));
		mTextView.setText(preference.getString("info", "demo"));
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferences preference = getSharedPreferences("zhuyan",
				Context.MODE_PRIVATE);
		Editor edit = preference.edit();
		edit.putString("tel_phone", telPhoneEditText.getText().toString());
		edit.putString("info", mTextView.getText().toString());
		edit.commit();
	}

}