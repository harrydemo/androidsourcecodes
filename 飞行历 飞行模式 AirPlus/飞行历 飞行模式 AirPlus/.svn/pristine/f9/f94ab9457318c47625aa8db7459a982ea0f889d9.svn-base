/**
 * 
 */
package com.tilltheendwjx.airplus.receiver;

import com.tilltheendwjx.airplus.Air;
import com.tilltheendwjx.airplus.AirModeController;
import com.tilltheendwjx.airplus.Airs;
import com.tilltheendwjx.airplus.utils.AirAlertWakeLock;
import com.tilltheendwjx.airplus.utils.AsyncHandler;
import com.tilltheendwjx.airplus.utils.Log;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;

/**
 * @author wjx
 * 
 */
public class AirEndReceiver extends BroadcastReceiver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(final Context context, final Intent intent) {
		final WakeLock wl = AirAlertWakeLock.createPartialWakeLock(context);
		wl.acquire();
		AsyncHandler.post(new Runnable() {
			@Override
			public void run() {
				if (Airs.AIR_END.equals(intent.getAction())) {
					Air air = null;
					if (intent.hasExtra(Airs.AIR_INTENT_EXTRA)) {
						air = (Air) intent
								.getParcelableExtra(Airs.AIR_INTENT_EXTRA);
					}
					if (air != null) {
						Airs.enableEndAlert(context, air, air.end_time,
								air.air_mode_radios.getEnableAirModeRadios());
					}
				} else if (Airs.AIR_END_ALERT_ACTION.equals(intent.getAction())) {
					AirModeController.setAirplaneMode(context, false);
					Airs.disableExpiredAirs(context);
					Airs.setNextAlert(context);
				}
				wl.release();
			}
		});
	}
}
