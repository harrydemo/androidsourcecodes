/**
 * 
 */
package com.tilltheendwjx.airplus;

import com.tilltheendwjx.airplus.utils.Log;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author wjx
 * 
 */
public class AirAlert extends AirAlertFullScreen {
	// If we try to check the keyguard more than 5 times, just launch the full
	// screen activity.
	private int mKeyguardRetryCount;
	private final int MAX_KEYGUARD_CHECKS = 5;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			handleScreenOff((KeyguardManager) msg.obj);
		}
	};

	private final BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			KeyguardManager km = (KeyguardManager) context
					.getSystemService(Context.KEYGUARD_SERVICE);
			handleScreenOff(km);
		}
	};

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Listen for the screen turning off so that when the screen comes back
		// on, the user does not need to unlock the phone to dismiss the alarm.
		registerReceiver(mScreenOffReceiver, new IntentFilter(
				Intent.ACTION_SCREEN_OFF));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mScreenOffReceiver);
		// Remove any of the keyguard messages just in case
		mHandler.removeMessages(0);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.air_alert;
	}

	private boolean checkRetryCount() {
		if (mKeyguardRetryCount++ >= MAX_KEYGUARD_CHECKS) {
			Log.e("Tried to read keyguard status too many times, bailing...");
			return false;
		}
		return true;
	}

	private void handleScreenOff(final KeyguardManager km) {
		if (!km.inKeyguardRestrictedInputMode() && checkRetryCount()) {
			if (checkRetryCount()) {
				mHandler.sendMessageDelayed(mHandler.obtainMessage(0, km), 500);
			}
		} else {
			// Launch the full screen activity but do not turn the screen on.
			Intent i = new Intent(this, AirAlertFullScreen.class);
			i.putExtra(Airs.AIR_INTENT_EXTRA, mAir);
			i.putExtra(SCREEN_OFF, true);
			startActivity(i);
			finish();
		}
	}

}
