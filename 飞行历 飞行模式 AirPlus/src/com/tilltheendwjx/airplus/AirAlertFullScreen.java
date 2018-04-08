/**
 * 
 */
package com.tilltheendwjx.airplus;

import java.util.Calendar;

import com.tilltheendwjx.airplus.receiver.AirReceiver;
import com.tilltheendwjx.airplus.utils.Log;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author wjx
 * 
 */
public class AirAlertFullScreen extends Activity {
	// These defaults must match the values in res/xml/settings.xml
	private static final String DEFAULT_SNOOZE = "10";
	private static final String DEFAULT_VOLUME_BEHAVIOR = "2";
	protected static final String SCREEN_OFF = "screen_off";

	protected Air mAir;
	private int mVolumeBehavior;
	boolean mFullscreenStyle;

	// Receives the ALARM_KILLED action from the AlarmKlaxon,
	// and also ALARM_SNOOZE_ACTION / ALARM_DISMISS_ACTION from other
	// applications
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Airs.AIR_SNOOZE_ACTION)) {
				snooze();
			} else if (action.equals(Airs.AIR_DISMISS_ACTION)) {
				dismiss(false);
			} else {
				Air air = intent.getParcelableExtra(Airs.AIR_INTENT_EXTRA);
				if (air != null && mAir.id == air.id) {
					dismiss(true);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mAir = getIntent().getParcelableExtra(Airs.AIR_INTENT_EXTRA);

		// Get the volume/camera button behavior setting
		final String vol = PreferenceManager.getDefaultSharedPreferences(this)
				.getString(SettingsActivity.KEY_VOLUME_BEHAVIOR,
						DEFAULT_VOLUME_BEHAVIOR);
		mVolumeBehavior = Integer.parseInt(vol);

		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		// Turn on the screen unless we are being launched from the AlarmAlert
		// subclass as a result of the screen turning off.
		if (!getIntent().getBooleanExtra(SCREEN_OFF, false)) {
			win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
		}
		updateLayout();

		// Register to get the alarm killed/snooze/dismiss intent.
		IntentFilter filter = new IntentFilter(Airs.AIR_KILLED);
		filter.addAction(Airs.AIR_SNOOZE_ACTION);
		filter.addAction(Airs.AIR_DISMISS_ACTION);
		registerReceiver(mReceiver, filter);
	}

	private void setTitle() {
		final String titleText = mAir.getLabelOrDefault(this);

		setTitle(titleText);
	}

	protected int getLayoutResId() {
		return R.layout.air_alert_fullscreen;
	}

	private void updateLayout() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View v = inflater.inflate(getLayoutResId(), null);
		setContentView(v);

		/*
		 * snooze behavior: pop a snooze confirmation view, kick alarm manager.
		 */
		Button snooze = (Button) findViewById(R.id.snooze);
		snooze.requestFocus();
		snooze.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				snooze();
			}
		});
		/* dismiss button: close notification */
		findViewById(R.id.dismiss).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						dismiss(false);
					}
				});
		findViewById(R.id.done).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						done();
					}
				});
		/* Set the title from the passed in alarm */
		setTitle();
	}

	private void done() {
		AirModeController.setAirplaneMode(AirAlertFullScreen.this, true);
		Intent endAir = new Intent(Airs.AIR_END);
		endAir.putExtra(Airs.AIR_INTENT_EXTRA, mAir);
		sendBroadcast(endAir);
		dismiss(false);
	}

	// Attempt to snooze this alert.
	private void snooze() {
		// Do not snooze if the snooze button is disabled.
		if (!findViewById(R.id.snooze).isEnabled()) {
			dismiss(false);
			return;
		}
		final String snooze = PreferenceManager.getDefaultSharedPreferences(
				this)
				.getString(SettingsActivity.KEY_AIR_SNOOZE, DEFAULT_SNOOZE);
		int snoozeMinutes = Integer.parseInt(snooze);

		final long snoozeTime = System.currentTimeMillis()
				+ (1000 * 60 * snoozeMinutes);
		Airs.saveSnoozeAlert(AirAlertFullScreen.this, mAir.id, snoozeTime);

		// Get the display time for the snooze and update the notification.
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(snoozeTime);

		// Append (snoozed) to the label.
		String label = mAir.getLabelOrDefault(this);
		label = getString(R.string.air_notify_snooze_label, label);

		// Notify the user that the alarm has been snoozed.
		Intent cancelSnooze = new Intent(this, AirReceiver.class);
		cancelSnooze.setAction(Airs.CANCEL_SNOOZE);
		cancelSnooze.putExtra(Airs.AIR_INTENT_EXTRA, mAir);
		PendingIntent broadcast = PendingIntent.getBroadcast(this, mAir.id,
				cancelSnooze, 0);
		NotificationManager nm = getNotificationManager();
		Notification n = new Notification(R.drawable.air, label, 0);
		n.setLatestEventInfo(
				this,
				label,
				getString(R.string.air_notify_snooze_text,
						Airs.formatTime(this, c)), broadcast);
		n.flags |= Notification.FLAG_AUTO_CANCEL
				| Notification.FLAG_ONGOING_EVENT;
		nm.notify(mAir.id, n);

		String displayTime = getString(R.string.air_alert_snooze_text,
				snoozeMinutes);
		// Intentionally log the snooze time for debugging.
		Log.v(displayTime);

		// Display the snooze minutes in a toast.
		Toast.makeText(AirAlertFullScreen.this, displayTime, Toast.LENGTH_LONG)
				.show();
		stopService(new Intent(Airs.AIR_ALERT_ACTION));
		finish();
	}

	private NotificationManager getNotificationManager() {
		return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	// Dismiss the alarm.
	private void dismiss(boolean killed) {
		Log.i(killed ? "Alarm killed" : "Alarm dismissed by user");
		// The service told us that the alarm has been killed, do not modify
		// the notification or stop the service.
		if (!killed) {
			// Cancel the notification and stop playing the alarm
			NotificationManager nm = getNotificationManager();
			nm.cancel(mAir.id);
			stopService(new Intent(Airs.AIR_ALERT_ACTION));

		}
		finish();
	}

	/**
	 * this is called when a second alarm is triggered while a previous alert
	 * window is still active.
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (Log.LOGV)
			Log.v("AlarmAlert.OnNewIntent()");

		mAir = intent.getParcelableExtra(Airs.AIR_INTENT_EXTRA);

		setTitle();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// If the alarm was deleted at some point, disable snooze.
		if (Airs.getAir(getContentResolver(), mAir.id) == null) {
			Button snooze = (Button) findViewById(R.id.snooze);
			snooze.setEnabled(false);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (Log.LOGV)
			Log.v("AlarmAlert.onDestroy()");
		// No longer care about the alarm being killed.
		unregisterReceiver(mReceiver);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// Do this on key down to handle a few of the system keys.
		boolean up = event.getAction() == KeyEvent.ACTION_UP;
		switch (event.getKeyCode()) {
		// Volume keys and camera keys dismiss the alarm
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_CAMERA:
		case KeyEvent.KEYCODE_FOCUS:
			if (up) {
				switch (mVolumeBehavior) {
				case 1:
					snooze();
					break;

				case 2:
					dismiss(false);
					break;

				default:
					break;
				}
			}
			return true;
		default:
			break;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onBackPressed() {
		// Don't allow back to dismiss. This method is overriden by AlarmAlert
		// so that the dialog is dismissed.
		return;
	}

}
