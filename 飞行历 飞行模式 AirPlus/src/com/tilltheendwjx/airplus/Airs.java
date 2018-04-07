/**
 * 
 */
package com.tilltheendwjx.airplus;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Settings;
import android.text.format.DateFormat;

import com.tilltheendwjx.airplus.utils.Log;
import com.tilltheendwjx.airplus.utils.SharedPreferencesHandler;

/**
 * @author wjx
 * 
 */
public class Airs {

	// This action triggers the AirReceiver as well as the AirKlaxon. It
	// is a public action used in the manifest for receiving Air broadcasts
	// from the Air manager.
	public static final String AIR_ALERT_ACTION = "com.tilltheendwjx.airplus.AIR_ALERT";
	public static final String AIR_END_ALERT_ACTION = "com.tilltheendwjx.airplus.AIR_END_ALERT";

	// A public action sent by AirKlaxon when the Air has stopped sounding
	// for any reason (e.g. because it has been dismissed from
	// AirAlertFullScreen,
	// or killed due to an incoming phone call, etc).
	public static final String AIR_DONE_ACTION = "com.tilltheendwjx.airplus.AIR_DONE";

	// AirAlertFullScreen listens for this broadcast intent, so that other
	// applications
	// can snooze the Air (after Air_ALERT_ACTION and before Air_DONE_ACTION).
	public static final String AIR_SNOOZE_ACTION = "com.tilltheendwjx.airplus.AIR_SNOOZE";

	// AirAlertFullScreen listens for this broadcast intent, so that other
	// applications
	// can dismiss the Air (after Air_ALERT_ACTION and before Air_DONE_ACTION).
	public static final String AIR_DISMISS_ACTION = "com.tilltheendwjx.airplus.AIR_DISMISS";

	// This is a private action used by the AirKlaxon to update the UI to
	// show the Air has been killed.
	public static final String AIR_KILLED = "air_killed";
	public static final String AIR_END = "air_end";

	// Extra in the Air_KILLED intent to indicate to the user how long the
	// Air played before being killed.
	public static final String AIR_KILLED_TIMEOUT = "air_killed_timeout";

	// This string is used to indicate a silent Air in the db.
	public static final String AIR_ALERT_SILENT = "silent";

	// This intent is sent from the notification when the user cancels the
	// snooze alert.
	public static final String CANCEL_SNOOZE = "cancel_snooze";

	// This string is used when passing an Air object through an intent.
	public static final String AIR_INTENT_EXTRA = "intent.extra.air";

	// This extra is the raw Air object data. It is used in the
	// AirManagerService to avoid a ClassNotFoundException when filling in
	// the Intent extras.
	public static final String AIR_RAW_DATA = "intent.extra.air_raw";

	private static final String PREF_SNOOZE_IDS = "snooze_ids";
	private static final String PREF_SNOOZE_TIME = "snooze_time";

	private final static String DM12 = "E h:mm aa";
	private final static String DM24 = "E kk:mm";

	private final static String M12 = "h:mm aa";
	// Shared with DigitalClock
	final static String M24 = "kk:mm";

	public final static int INVALID_AIR_ID = -1;

	/**
	 * 向数据库中添加一个飞行段
	 */
	public static long addAir(Context context, Air Air) {
		ContentValues values = createContentValues(Air);
		Uri uri = context.getContentResolver().insert(
				com.tilltheendwjx.airplus.Air.Columns.CONTENT_URI, values);
		Air.id = (int) ContentUris.parseId(uri);

		long timeInMillis = calculateAir(Air);
		if (Air.enabled) {
			clearSnoozeIfNeeded(context, timeInMillis);
		}
		setNextAlert(context);
		return timeInMillis;
	}

	/**
	 * 从数据库中删除一个飞行段
	 */
	public static void deleteAir(Context context, int AirId) {
		if (AirId == INVALID_AIR_ID)
			return;

		ContentResolver contentResolver = context.getContentResolver();
		disableSnoozeAlert(context, AirId);

		Uri uri = ContentUris.withAppendedId(Air.Columns.CONTENT_URI, AirId);
		contentResolver.delete(uri, "", null);

		setNextAlert(context);
	}

	/**
	 * 从数据库中查询飞行段
	 */
	public static Cursor getAirsCursor(ContentResolver contentResolver) {
		return contentResolver.query(Air.Columns.CONTENT_URI,
				Air.Columns.AIR_QUERY_COLUMNS, null, null,
				Air.Columns.DEFAULT_SORT_ORDER);
	}

	/**
	 * 从数据库中查询指定条件的飞行段
	 */
	private static Cursor getFilteredAirsCursor(ContentResolver contentResolver) {
		return contentResolver.query(Air.Columns.CONTENT_URI,
				Air.Columns.AIR_QUERY_COLUMNS, Air.Columns.WHERE_ENABLED, null,
				null);
	}

	private static ContentValues createContentValues(Air Air) {
		ContentValues values = new ContentValues(11);
		long startTime = 0;
		long endTime = 0;
		// if (!Air.daysOfWeek.isRepeatSet()) {
		startTime = calculateAir(Air);
		endTime = calculateEndAir(Air);
		// }

		values.put(com.tilltheendwjx.airplus.Air.Columns.ENABLED,
				Air.enabled ? 1 : 0);
		values.put(com.tilltheendwjx.airplus.Air.Columns.START_HOUR,
				Air.start_hour);
		values.put(com.tilltheendwjx.airplus.Air.Columns.START_MINUTES,
				Air.start_minutes);
		values.put(com.tilltheendwjx.airplus.Air.Columns.END_HOUR, Air.end_hour);
		values.put(com.tilltheendwjx.airplus.Air.Columns.END_MINUTES,
				Air.end_minutes);
		values.put(com.tilltheendwjx.airplus.Air.Columns.START_AIR_TIME,
				startTime);
		values.put(com.tilltheendwjx.airplus.Air.Columns.END_AIR_TIME, endTime);
		values.put(com.tilltheendwjx.airplus.Air.Columns.DAYS_OF_WEEK,
				Air.daysOfWeek.getCoded());
		values.put(com.tilltheendwjx.airplus.Air.Columns.VIBRATE, Air.vibrate);
		values.put(com.tilltheendwjx.airplus.Air.Columns.MESSAGE, Air.label);
		values.put(com.tilltheendwjx.airplus.Air.Columns.AIR_MODE_RADIOS,
				Air.air_mode_radios.getEnableAirModeRadios());

		// A null alert Uri indicates a silent Air.
		// values.put(com.tilltheendwjx.airplus.Air.Columns.ALERT, Air.alert ==
		// null ? Air_ALERT_SILENT
		// : Air.alert.toString());
		return values;
	}

	private static void clearSnoozeIfNeeded(Context context, long AirTime) {
		// If this Air fires before the next snooze, clear the snooze to
		// enable this Air.
		SharedPreferences prefs = context.getSharedPreferences(
				AirPlusActivity.PREFERENCES, 0);

		// Get the list of snoozed Airs
		final Set<String> snoozedIds = SharedPreferencesHandler.getStringSet(
				prefs, PREF_SNOOZE_IDS, new HashSet<String>());
		// new HashSet<String>());
		for (String snoozedAir : snoozedIds) {
			final long snoozeTime = prefs.getLong(
					getAirPrefSnoozeTimeKey(snoozedAir), 0);
			if (AirTime < snoozeTime) {
				final int AirId = Integer.parseInt(snoozedAir);
				clearSnoozePreference(context, prefs, AirId);
			}
		}
	}

	/**
	 * 查询指定id的飞行段 不存在返回空
	 */
	public static Air getAir(ContentResolver contentResolver, int AirId) {
		Cursor cursor = contentResolver.query(
				ContentUris.withAppendedId(Air.Columns.CONTENT_URI, AirId),
				Air.Columns.AIR_QUERY_COLUMNS, null, null, null);
		Air Air = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				Air = new Air(cursor);
			}
			cursor.close();
		}
		return Air;
	}

	/**
	 * A convenience method to set an Air in the Airs content provider.
	 * 
	 * @return Time when the Air will fire.
	 */
	public static long setAir(Context context, Air Air) {
		ContentValues values = createContentValues(Air);
		ContentResolver resolver = context.getContentResolver();
		resolver.update(ContentUris.withAppendedId(
				com.tilltheendwjx.airplus.Air.Columns.CONTENT_URI, Air.id),
				values, null, null);

		long timeInMillis = calculateAir(Air);

		if (Air.enabled) {
			// Disable the snooze if we just changed the snoozed Air. This
			// only does work if the snoozed Air is the same as the given
			// Air.
			disableSnoozeAlert(context, Air.id);

			// Disable the snooze if this Air fires before the snoozed Air.
			// This works on every Air since the user most likely intends to
			// have the modified Air fire next.
			clearSnoozeIfNeeded(context, timeInMillis);
		}

		setNextAlert(context);

		return timeInMillis;
	}

	/**
	 * A convenience method to enable or disable an Air.
	 * 
	 * @param id
	 *            corresponds to the _id column
	 * @param enabled
	 *            corresponds to the ENABLED column
	 */

	public static void enableAir(final Context context, final int id,
			boolean enabled) {
		enableAirInternal(context, id, enabled);
		setNextAlert(context);
	}

	private static void enableAirInternal(final Context context, final int id,
			boolean enabled) {
		enableAirInternal(context, getAir(context.getContentResolver(), id),
				enabled);
	}

	private static void enableAirInternal(final Context context, final Air Air,
			boolean enabled) {
		if (Air == null) {
			return;
		}
		ContentResolver resolver = context.getContentResolver();

		ContentValues values = new ContentValues(2);
		values.put(com.tilltheendwjx.airplus.Air.Columns.ENABLED, enabled ? 1
				: 0);

		// If we are enabling the Air, calculate Air time since the time
		// value in Air may be old.
		if (enabled) {
			long time = 0;
			if (!Air.daysOfWeek.isRepeatSet()) {
				time = calculateAir(Air);
			}
			values.put(com.tilltheendwjx.airplus.Air.Columns.START_AIR_TIME,
					time);
		} else {
			// Clear the snooze if the id matches.
			disableSnoozeAlert(context, Air.id);
		}

		resolver.update(ContentUris.withAppendedId(
				com.tilltheendwjx.airplus.Air.Columns.CONTENT_URI, Air.id),
				values, null, null);
	}

	private static Air calculateNextAlert(final Context context) {
		long minTime = Long.MAX_VALUE;
		long now = System.currentTimeMillis();
		final SharedPreferences prefs = context.getSharedPreferences(
				AirPlusActivity.PREFERENCES, 0);

		Set<Air> Airs = new HashSet<Air>();

		// We need to to build the list of Airs from both the snoozed list and
		// the scheduled
		// list. For a non-repeating Air, when it goes of, it becomes disabled.
		// A snoozed
		// non-repeating Air is not in the active list in the database.

		// first go through the snoozed Airs
		final Set<String> snoozedIds = SharedPreferencesHandler.getStringSet(
				prefs, PREF_SNOOZE_IDS, new HashSet<String>());
		for (String snoozedAir : snoozedIds) {
			final int AirId = Integer.parseInt(snoozedAir);
			final Air a = getAir(context.getContentResolver(), AirId);
			Airs.add(a);
		}

		// Now add the scheduled Airs
		final Cursor cursor = getFilteredAirsCursor(context
				.getContentResolver());
		if (cursor != null) {
			try {
				if (cursor.moveToFirst()) {
					do {
						final Air a = new Air(cursor);
						Airs.add(a);
					} while (cursor.moveToNext());
				}
			} finally {
				cursor.close();
			}
		}

		Air Air = null;

		for (Air a : Airs) {
			// A time of 0 indicates this is a repeating Air, so
			// calculate the time to get the next alert.
			if (a.start_time == 0) {
				a.start_time = calculateAir(a);
			}

			// Update the Air if it has been snoozed
			updateAirTimeForSnooze(prefs, a);

			if (a.start_time < now) {
				Log.v("Disabling expired Air set for "
						+ Log.formatTime(a.start_time));
				// Expired Air, disable it and move along.
				enableAirInternal(context, a, false);
				continue;
			}
			if (a.start_time < minTime) {
				minTime = a.start_time;
				Air = a;
			}
		}

		return Air;
	}

	/**
	 * Disables non-repeating Airs that have passed. Called at boot.
	 */
	public static void disableExpiredAirs(final Context context) {
		Cursor cur = getFilteredAirsCursor(context.getContentResolver());
		long now = System.currentTimeMillis();

		try {
			if (cur.moveToFirst()) {
				do {
					Air Air = new Air(cur);
					// A time of 0 means this Air repeats. If the time is
					// non-zero, check if the time is before now.
					if (Air.start_time != 0 && Air.start_time < now) {
						Log.v("Disabling expired Air set for "
								+ Log.formatTime(Air.start_time));
						enableAirInternal(context, Air, false);
					}
				} while (cur.moveToNext());
			}
		} finally {
			cur.close();
		}
	}

	/**
	 * Called at system startup, on time/timezone change, and whenever the user
	 * changes Air settings. Activates snooze if set, otherwise loads all Airs,
	 * activates next alert.
	 */
	public static void setNextAlert(final Context context) {
		final Air air = calculateNextAlert(context);
		if (air != null) {
			enableAlert(context, air, air.start_time,
					air.air_mode_radios.getEnableAirModeRadios());
		} else {
			disableAlert(context, AIR_ALERT_ACTION);
		}
	}

	/**
	 * Sets alert in AirManger and StatusBar. This is what will actually launch
	 * the alert when the Air triggers.
	 * 
	 * @param Air
	 *            Air.
	 * @param atTimeInMillis
	 *            milliseconds since epoch
	 */
	private static void enableAlert(Context context, final Air air,
			final long atTimeInMillis, String air_mode_radios) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (Log.LOGV) {
			Log.v("** setAlert id " + air.id + " atTime " + atTimeInMillis);
		}
		Intent intent = new Intent(AIR_ALERT_ACTION);

		// AirManagerService process. The AirManager adds extra data to
		// this Intent which causes it to inflate. Since the remote process
		// does not know about the Air class, it throws a
		// ClassNotFoundException.
		//
		// To avoid this, we marshall the data ourselves and then parcel a plain
		// byte[] array. The AirReceiver class knows to build the Air
		// object from the byte[] array.
		Parcel out = Parcel.obtain();
		air.writeToParcel(out, 0);
		out.setDataPosition(0);
		intent.putExtra(AIR_RAW_DATA, out.marshall());

		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		am.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
		Settings.System.putString(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_RADIOS, air_mode_radios);
		setStatusBarIcon(context, true);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(atTimeInMillis);
		String timeString = formatDayAndTime(context, c);
		saveNextAir(context, timeString);
	}

	public static void enableEndAlert(Context context, final Air air,
			final long atEndTimeInMillis, String air_mode_radios) {
		AlarmManager endam = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(AIR_END_ALERT_ACTION);
		Parcel out = Parcel.obtain();
		air.writeToParcel(out, 0);
		out.setDataPosition(0);
		intent.putExtra(AIR_RAW_DATA, out.marshall());
		PendingIntent endsender = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		endam.set(AlarmManager.RTC_WAKEUP, atEndTimeInMillis, endsender);
		Settings.System.putString(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_RADIOS, air_mode_radios);
		setStatusBarIcon(context, true);
		Calendar endc = Calendar.getInstance();
		endc.setTimeInMillis(atEndTimeInMillis);
		String endTimeString = formatDayAndTime(context, endc);
		saveNextAir(context, endTimeString);
	}

	/**
	 * Disables alert in AirManger and StatusBar.
	 * 
	 * @param id
	 *            Air ID.
	 */
	public static void disableAlert(Context context, String action) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0,
				new Intent(action), PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
		setStatusBarIcon(context, false);
		saveNextAir(context, "");
	}

	public static void saveSnoozeAlert(final Context context, final int id,
			final long time) {
		SharedPreferences prefs = context.getSharedPreferences(
				AirPlusActivity.PREFERENCES, 0);
		if (id == INVALID_AIR_ID) {
			clearAllSnoozePreferences(context, prefs);
		} else {
			final Set<String> snoozedIds = SharedPreferencesHandler
					.getStringSet(prefs, PREF_SNOOZE_IDS, new HashSet<String>());
			// prefs.getStringSet(PREF_SNOOZE_IDS,
			// new HashSet<String>());
			snoozedIds.add(Integer.toString(id));
			final SharedPreferences.Editor ed = prefs.edit();
			SharedPreferencesHandler.putStringSet(ed, PREF_SNOOZE_IDS,
					snoozedIds);
			// ed.putStringSet(PREF_SNOOZE_IDS, snoozedIds);
			ed.putLong(getAirPrefSnoozeTimeKey(id), time);
			ed.apply();
		}
		// Set the next alert after updating the snooze.
		setNextAlert(context);
	}

	private static String getAirPrefSnoozeTimeKey(int id) {
		return getAirPrefSnoozeTimeKey(Integer.toString(id));
	}

	private static String getAirPrefSnoozeTimeKey(String id) {
		return PREF_SNOOZE_TIME + id;
	}

	/**
	 * Disable the snooze alert if the given id matches the snooze id.
	 */
	public static void disableSnoozeAlert(final Context context, final int id) {
		SharedPreferences prefs = context.getSharedPreferences(
				AirPlusActivity.PREFERENCES, 0);
		if (hasAirBeenSnoozed(prefs, id)) {
			// This is the same id so clear the shared prefs.
			clearSnoozePreference(context, prefs, id);
		}
	}

	// Helper to remove the snooze preference. Do not use clear because that
	// will erase the clock preferences. Also clear the snooze notification in
	// the window shade.
	private static void clearSnoozePreference(final Context context,
			final SharedPreferences prefs, final int id) {
		final String AirStr = Integer.toString(id);
		final Set<String> snoozedIds = SharedPreferencesHandler.getStringSet(
				prefs, PREF_SNOOZE_IDS, new HashSet<String>());
		if (snoozedIds.contains(AirStr)) {
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.cancel(id);
		}

		final SharedPreferences.Editor ed = prefs.edit();
		snoozedIds.remove(AirStr);
		SharedPreferencesHandler.putStringSet(ed, PREF_SNOOZE_IDS, snoozedIds);
		ed.remove(getAirPrefSnoozeTimeKey(AirStr));
		ed.apply();
	}

	private static void clearAllSnoozePreferences(final Context context,
			final SharedPreferences prefs) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		final Set<String> snoozedIds = SharedPreferencesHandler.getStringSet(
				prefs, PREF_SNOOZE_IDS, new HashSet<String>());
		final SharedPreferences.Editor ed = prefs.edit();
		for (String snoozeId : snoozedIds) {
			nm.cancel(Integer.parseInt(snoozeId));
			ed.remove(getAirPrefSnoozeTimeKey(snoozeId));
		}
		ed.remove(PREF_SNOOZE_IDS);
		ed.apply();
	}

	private static boolean hasAirBeenSnoozed(final SharedPreferences prefs,
			final int AirId) {
		final Set<String> snoozedIds = SharedPreferencesHandler.getStringSet(
				prefs, PREF_SNOOZE_IDS, null);
		// Return true if there a valid snoozed AirId was saved
		return snoozedIds != null
				&& snoozedIds.contains(Integer.toString(AirId));
	}

	/**
	 * Updates the specified Air with the additional snooze time. Returns a
	 * boolean indicating whether the Air was updated.
	 */
	private static boolean updateAirTimeForSnooze(
			final SharedPreferences prefs, final Air Air) {
		if (!hasAirBeenSnoozed(prefs, Air.id)) {
			// No need to modify the Air
			return false;
		}

		final long time = prefs.getLong(getAirPrefSnoozeTimeKey(Air.id), -1);
		// The time in the database is either 0 (repeating) or a specific time
		// for a non-repeating Air. Update this value so the AirReceiver
		// has the right time to compare.
		Air.start_time = time;

		return true;
	}

	/**
	 * Tells the StatusBar whether the Air is enabled or disabled
	 */
	public static void setStatusBarIcon(Context context, boolean enabled) {
		Intent AirChanged = new Intent("android.intent.action.ALARM_CHANGED");
		AirChanged.putExtra("alarmSet", enabled);
		context.sendBroadcast(AirChanged);
	}

	public static long calculateEndAir(Air Air) {
		return calculateAir(Air.end_hour, Air.end_minutes, Air.daysOfWeek)
				.getTimeInMillis();
	}

	public static long calculateAir(Air Air) {
		return calculateAir(Air.start_hour, Air.start_minutes, Air.daysOfWeek)
				.getTimeInMillis();
	}

	/**
	 * Given an Air in hours and minutes, return a time suitable for setting in
	 * AirManager.
	 */
	static Calendar calculateAir(int hour, int minute, Air.DaysOfWeek daysOfWeek) {

		// start with now
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		// if Air is behind current time, advance one day
		if (hour < nowHour || hour == nowHour && minute <= nowMinute) {
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		int addDays = daysOfWeek.getNextAir(c);
		if (addDays > 0)
			c.add(Calendar.DAY_OF_WEEK, addDays);
		return c;
	}

	static String formatTime(final Context context, int hour, int minute,
			Air.DaysOfWeek daysOfWeek) {
		Calendar c = calculateAir(hour, minute, daysOfWeek);
		return formatTime(context, c);
	}

	/* used by AirAlert */
	static String formatTime(final Context context, Calendar c) {
		String format = get24HourMode(context) ? M24 : M12;
		return (c == null) ? "" : (String) DateFormat.format(format, c);
	}

	/**
	 * Shows day and time -- used for lock screen
	 */
	private static String formatDayAndTime(final Context context, Calendar c) {
		String format = get24HourMode(context) ? DM24 : DM12;
		return (c == null) ? "" : (String) DateFormat.format(format, c);
	}

	/**
	 * Save time of the next Air, as a formatted string, into the system
	 * settings so those who care can make use of it.
	 */
	static void saveNextAir(final Context context, String timeString) {
		Settings.System.putString(context.getContentResolver(),
				Settings.System.NEXT_ALARM_FORMATTED, timeString);
	}

	/**
	 * @return true if clock is set to 24-hour mode
	 */
	static boolean get24HourMode(final Context context) {
		return android.text.format.DateFormat.is24HourFormat(context);
	}
}
