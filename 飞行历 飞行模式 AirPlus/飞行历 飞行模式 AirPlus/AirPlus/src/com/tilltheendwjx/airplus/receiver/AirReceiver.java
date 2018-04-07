/**
 * 
 */
package com.tilltheendwjx.airplus.receiver;

import com.tilltheendwjx.airplus.Air;
import com.tilltheendwjx.airplus.AirAlert;
import com.tilltheendwjx.airplus.AirAlertFullScreen;
import com.tilltheendwjx.airplus.Airs;
import com.tilltheendwjx.airplus.R;
import com.tilltheendwjx.airplus.SetAir;
import com.tilltheendwjx.airplus.utils.AirAlertWakeLock;
import com.tilltheendwjx.airplus.utils.AsyncHandler;
import com.tilltheendwjx.airplus.utils.Log;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.PowerManager.WakeLock;

/**
 * @author wjx
 * 
 */
public class AirReceiver extends BroadcastReceiver {
	/**
	 * If the alarm is older than STALE_WINDOW, ignore. It is probably the
	 * result of a time or timezone change
	 */
	private final static int STALE_WINDOW = 1 * 60 * 1000;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		// final PendingResult result = goAsync();
		final WakeLock wl = AirAlertWakeLock.createPartialWakeLock(context);
		wl.acquire();
		AsyncHandler.post(new Runnable() {
			@Override
			public void run() {
				handleIntent(context, intent);
				// result.finish();
				wl.release();
			}
		});
	}

	private void handleIntent(Context context, Intent intent) {
		if (Airs.AIR_KILLED.equals(intent.getAction())) {
			// The alarm has been killed, update the notification
			updateNotification(context,
					(Air) intent.getParcelableExtra(Airs.AIR_INTENT_EXTRA),
					intent.getIntExtra(Airs.AIR_KILLED_TIMEOUT, -1));
			return;
		} else if (Airs.CANCEL_SNOOZE.equals(intent.getAction())) {
			Air air = null;
			if (intent.hasExtra(Airs.AIR_INTENT_EXTRA)) {
				// Get the alarm out of the Intent
				air = intent.getParcelableExtra(Airs.AIR_INTENT_EXTRA);
			}
			if (air != null) {
				Airs.disableSnoozeAlert(context, air.id);
				Airs.setNextAlert(context);
			} else {
				// Don't know what snoozed alarm to cancel, so cancel them all.
				// This
				// shouldn't happen
				Log.wtf("Unable to parse Alarm from intent.");
				Airs.saveSnoozeAlert(context, Airs.INVALID_AIR_ID, -1);
			}
			return;
		} else if (!Airs.AIR_ALERT_ACTION.equals(intent.getAction())) {
			// Unknown intent, bail.
			return;
		}
		Air air = null;
		// Grab the alarm from the intent. Since the remote AlarmManagerService
		// fills in the Intent to add some extra data, it must unparcel the
		// Alarm object. It throws a ClassNotFoundException when unparcelling.
		// To avoid this, do the marshalling ourselves.
		final byte[] data = intent.getByteArrayExtra(Airs.AIR_RAW_DATA);
		if (data != null) {
			Parcel in = Parcel.obtain();
			in.unmarshall(data, 0, data.length);
			in.setDataPosition(0);
			air = Air.CREATOR.createFromParcel(in);
		}

		if (air == null) {
			Log.wtf("Failed to parse the alarm from the intent");
			// Make sure we set the next alert if needed.
			Airs.setNextAlert(context);
			return;
		}

		// Disable the snooze alert if this alarm is the snooze.
		Airs.disableSnoozeAlert(context, air.id);
		// Disable this alarm if it does not repeat.
		if (!air.daysOfWeek.isRepeatSet()) {
			Airs.enableAir(context, air.id, false);
		} else {
			// Enable the next alert if there is one. The above call to
			// enableAlarm will call setNextAlert so avoid calling it twice.
			Airs.setNextAlert(context);
		}

		// Intentionally verbose: always log the alarm time to provide useful
		// information in bug reports.
		long now = System.currentTimeMillis();
		Log.v("Recevied alarm set for " + Log.formatTime(air.start_time));

		// Always verbose to track down time change problems.
		if (now > air.start_time + STALE_WINDOW) {
			Log.v("Ignoring stale alarm");
			return;
		}

		AirAlertWakeLock.acquireCpuWakeLock(context);

		// 关闭所有的对话框
		Intent closeDialogs = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		context.sendBroadcast(closeDialogs);

		// Decide which activity to start based on the state of the keyguard.
		Class c = AirAlert.class;
		KeyguardManager km = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		if (km.inKeyguardRestrictedInputMode()) {
			// Use the full screen activity for security.
			c = AirAlertFullScreen.class;
		}

		// Play the alarm alert and vibrate the device.
		Intent playAir = new Intent(Airs.AIR_ALERT_ACTION);
		playAir.putExtra(Airs.AIR_INTENT_EXTRA, air);
		context.startService(playAir);

		// Trigger a notification that, when clicked, will show the alarm alert
		// dialog. No need to check for fullscreen since this will always be
		// launched from a user action.
		Intent notify = new Intent(context, AirAlert.class);
		notify.putExtra(Airs.AIR_INTENT_EXTRA, air);
		PendingIntent pendingNotify = PendingIntent.getActivity(context,
				air.id, notify, 0);

		// Use the alarm's label or the default label as the ticker text and
		// main text of the notification.
		String label = air.getLabelOrDefault(context);
		Notification n = new Notification(R.drawable.air, label, air.start_time);
		n.setLatestEventInfo(context, label,
				context.getString(R.string.air_notify_text), pendingNotify);
		n.flags |= Notification.FLAG_SHOW_LIGHTS
				| Notification.FLAG_ONGOING_EVENT;
		n.defaults |= Notification.DEFAULT_LIGHTS;

		// NEW: Embed the full-screen UI here. The notification manager will
		// take care of displaying it if it's OK to do so.
		Intent airAlert = new Intent(context, c);
		airAlert.putExtra(Airs.AIR_INTENT_EXTRA, air);
		airAlert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		n.fullScreenIntent = PendingIntent.getActivity(context, air.id,
				airAlert, 0);

		// Send the notification using the alarm id to easily identify the
		// correct notification.
		NotificationManager nm = getNotificationManager(context);
		nm.notify(air.id, n);
	}

	private NotificationManager getNotificationManager(Context context) {
		return (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	private void updateNotification(Context context, Air air, int timeout) {
		NotificationManager nm = getNotificationManager(context);

		// If the alarm is null, just cancel the notification.
		if (air == null) {
			if (Log.LOGV) {
				Log.v("Cannot update notification for killer callback");
			}
			return;
		}
		// Launch SetAlarm when clicked.
		Intent viewAir = new Intent(context, SetAir.class);
		viewAir.putExtra(Airs.AIR_INTENT_EXTRA, air);
		PendingIntent intent = PendingIntent.getActivity(context, air.id,
				viewAir, 0);

		// Update the notification to indicate that the alert has been
		// silenced.
		String label = air.getLabelOrDefault(context);
		Notification n = new Notification(R.drawable.air, label, air.start_time);
		n.setLatestEventInfo(
				context,
				label,
				context.getString(R.string.air_alert_alert_silenced, timeout),
				intent);
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		// We have to cancel the original notification since it is in the
		// ongoing section and we want the "killed" notification to be a plain
		// notification.
		nm.cancel(air.id);
		nm.notify(air.id, n);
	}

}
