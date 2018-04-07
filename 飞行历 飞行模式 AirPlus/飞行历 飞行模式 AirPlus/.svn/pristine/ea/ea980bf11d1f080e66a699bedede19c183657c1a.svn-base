/**
 * 
 */
package com.tilltheendwjx.airplus;

import com.tilltheendwjx.airplus.utils.AirAlertWakeLock;
import com.tilltheendwjx.airplus.utils.Log;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * @author wjx
 * 
 */
public class AirService extends Service {
	private static final String DEFAULT_AIR_TIMEOUT = "10";
	//private static final long[] sVibratePattern = new long[] { 500, 500 };
	private Vibrator mVibrator;
	private Air mCurrentAir;
	private long mStartTime;
	private TelephonyManager mTelephonyManager;
	private int mInitialCallState;

	// Internal messages
	private static final int KILLER = 1000;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case KILLER:
				if (Log.LOGV) {
					Log.v("*********** Alarm killer triggered ***********");
				}
				sendKillBroadcast((Air) msg.obj);
				stopSelf();
				break;
			}
		}
	};

	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String ignored) {
			// The user might already be in a call when the alarm fires. When
			// we register onCallStateChanged, we get the initial in-call state
			// which kills the alarm. Check against the initial call state so
			// we don't kill the alarm during a call.
			if (state != TelephonyManager.CALL_STATE_IDLE
					&& state != mInitialCallState) {
				sendKillBroadcast(mCurrentAir);
				stopSelf();
			}
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Listen for incoming calls to kill the air.
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		AirAlertWakeLock.acquireCpuWakeLock(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		stop();
		// Stop listening for incoming calls.
		mTelephonyManager.listen(mPhoneStateListener, 0);
		AirAlertWakeLock.releaseCpuLock();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// No intent, tell the system not to restart us.
		if (intent == null) {
			stopSelf();
			return START_NOT_STICKY;
		}

		final Air air = intent.getParcelableExtra(Airs.AIR_INTENT_EXTRA);

		if (air == null) {
			Log.v("AlarmKlaxon failed to parse the alarm from the intent");
			stopSelf();
			return START_NOT_STICKY;
		}

		if (mCurrentAir != null) {
			sendKillBroadcast(mCurrentAir);
		}

		play(air);
		mCurrentAir = air;
		// Record the initial call state here so that the new alarm has the
		// newest state.
		mInitialCallState = mTelephonyManager.getCallState();

		return START_STICKY;
	}

	private void sendKillBroadcast(Air air) {
		long millis = System.currentTimeMillis() - mStartTime;
		int minutes = (int) Math.round(millis / 60000.0);
		Intent airKilled = new Intent(Airs.AIR_KILLED);
		airKilled.putExtra(Airs.AIR_INTENT_EXTRA, air);
		airKilled.putExtra(Airs.AIR_KILLED_TIMEOUT, minutes);
		sendBroadcast(airKilled);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private void play(Air air) {
		// stop() checks to see if we are already playing.
		stop();
		/* Start the vibrator after everything is ok with the media player */
		if (air.vibrate) {
			mVibrator.vibrate(1000);
		} else {
			//mVibrator.cancel();
		}
		enableKiller(air);
		mStartTime = System.currentTimeMillis();
	}

	/**
	 * Stops alarm audio and disables alarm if it not snoozed and not repeating
	 */
	public void stop() {
		if (Log.LOGV)
			Log.v("AlarmKlaxon.stop()");
		Intent alarmDone = new Intent(Airs.AIR_DONE_ACTION);
		sendBroadcast(alarmDone);
		// Stop vibrator
		//mVibrator.cancel();
		disableKiller();
	}

	/**
	 * Kills alarm audio after ALARM_TIMEOUT_SECONDS, so the alarm won't run all
	 * day.
	 * 
	 * This just cancels the audio, but leaves the notification popped, so the
	 * user will know that the alarm tripped.
	 */
	private void enableKiller(Air air) {
		final String autoSnooze = PreferenceManager
				.getDefaultSharedPreferences(this).getString(
						SettingsActivity.KEY_AUTO_AIR, DEFAULT_AIR_TIMEOUT);
		int autoSnoozeMinutes = Integer.parseInt(autoSnooze);
		if (autoSnoozeMinutes != -1) {
			mHandler.sendMessageDelayed(mHandler.obtainMessage(KILLER, air),
					1000 * autoSnoozeMinutes);
		}
	}

	private void disableKiller() {
		mHandler.removeMessages(KILLER);
	}

}
