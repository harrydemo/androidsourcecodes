/**
 * 
 */
package com.tilltheendwjx.airplus.receiver;

import com.tilltheendwjx.airplus.Airs;
import com.tilltheendwjx.airplus.utils.AirAlertWakeLock;
import com.tilltheendwjx.airplus.utils.AsyncHandler;
import com.tilltheendwjx.airplus.utils.Log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager.WakeLock;

/**
 * @author wjx
 * 
 */
public class AirInitReceiver extends BroadcastReceiver {

	/**
	 * Sets alarm on ACTION_BOOT_COMPLETED. Resets alarm on TIME_SET,
	 * TIMEZONE_CHANGED
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {
		final String action = intent.getAction();
		if (Log.LOGV)
			Log.v("AlarmInitReceiver" + action);
		// final PendingResult result = goAsync();
		final WakeLock wl = AirAlertWakeLock.createPartialWakeLock(context);
		wl.acquire();
		AsyncHandler.post(new Runnable() {
			@Override
			public void run() {
				// Remove the snooze alarm after a boot.
				if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
					Airs.saveSnoozeAlert(context, Airs.INVALID_AIR_ID, -1);
				}
				Airs.disableExpiredAirs(context);
				Airs.setNextAlert(context);
				// result.finish();
				Log.v("AlarmInitReceiver finished");
				wl.release();
			}
		});
	}

}
