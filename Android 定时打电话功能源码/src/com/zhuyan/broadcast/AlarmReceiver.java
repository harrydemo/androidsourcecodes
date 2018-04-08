package com.zhuyan.broadcast;

import com.zhuyan.TimingDialActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	private final static String TAG = "AlarmReceiver";
	private Context context;

	private String telNumber = "10086";
	private long time = 0;

	public void onReceive(Context context, Intent intent) {
		this.context = context;
		SharedPreferences preference = context.getSharedPreferences("zhuyan",
				Context.MODE_PRIVATE);
		telNumber = preference.getString("tel_phone", "10086");
		time = preference.getLong("time", 0);
		if (time != 0) {
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, intent, 0);
			AlarmManager am;
			am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
			Editor edit = context.getSharedPreferences(
					"zhuyan", Context.MODE_PRIVATE).edit();
			edit.putLong("time", 0);
			edit.commit();
		}

		PhoneCall();
	}

	private void PhoneCall() {
		Log.i(TAG, "PhoneCall");
		Uri localUri = Uri.parse("tel:" + telNumber);
		Intent call = new Intent(Intent.ACTION_CALL, localUri);
		call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(call);
		// android.util.Log.v("TeleListener", "start to call");
	}

}
