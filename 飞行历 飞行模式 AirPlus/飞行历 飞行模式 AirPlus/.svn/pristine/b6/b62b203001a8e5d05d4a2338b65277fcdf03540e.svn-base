/**
 * 
 */
package com.tilltheendwjx.airplus;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import com.tilltheendwjx.airplus.R;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * @author wjx
 * 
 */
public class Air implements Parcelable {

	// ////////////////////////////
	// Parcelable apis
	// ////////////////////////////
	public static final Parcelable.Creator<Air> CREATOR = new Parcelable.Creator<Air>() {
		public Air createFromParcel(Parcel p) {
			return new Air(p);
		}

		public Air[] newArray(int size) {
			return new Air[size];
		}
	};

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel p, int flags) {
		p.writeInt(id);
		p.writeInt(enabled ? 1 : 0);
		p.writeInt(start_hour);
		p.writeInt(start_minutes);
		p.writeInt(end_hour);
		p.writeInt(end_minutes);
		p.writeInt(daysOfWeek.getCoded());
		p.writeLong(start_time);
		p.writeLong(end_time);
		p.writeInt(vibrate ? 1 : 0);
		p.writeString(label);
		p.writeString(air_mode_radios.getEnableAirModeRadios());
		// p.writeParcelable(alert, flags);
		// p.writeInt(silent ? 1 : 0);
	}

	// ////////////////////////////
	// end Parcelable apis
	// ////////////////////////////

	// ////////////////////////////
	// Column definitions
	// ////////////////////////////
	public static class Columns implements BaseColumns {
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri
				.parse("content://com.tilltheendwjx.airplus/air");

		/**
		 * Hour in 24-hour localtime 0 - 23.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String START_HOUR = "start_hour";

		/**
		 * Minutes in localtime 0 - 59
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String START_MINUTES = "start_minutes";

		/**
		 * Days of week coded as integer
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		/**
		 * Hour in 24-hour localtime 0 - 23.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String END_HOUR = "end_hour";

		/**
		 * Minutes in localtime 0 - 59
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String END_MINUTES = "end_minutes";

		/**
		 * Days of week coded as integer
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String DAYS_OF_WEEK = "daysofweek";

		/**
		 * Air time in UTC milliseconds from the epoch.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String START_AIR_TIME = "start_Airtime";

		/**
		 * Air time in UTC milliseconds from the epoch.
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
		public static final String END_AIR_TIME = "end_Airtime";

		/**
		 * True if Air is active
		 * <P>
		 * Type: BOOLEAN
		 * </P>
		 */
		public static final String ENABLED = "enabled";

		/**
		 * True if Air should vibrate
		 * <P>
		 * Type: BOOLEAN
		 * </P>
		 */
		public static final String VIBRATE = "vibrate";

		/**
		 * Message to show when Air triggers Note: not currently used
		 * <P>
		 * Type: STRING
		 * </P>
		 */
		public static final String MESSAGE = "message";
		public static final String AIR_MODE_RADIOS = "airModeRadios";

		/**
		 * Audio alert to play when Air triggers
		 * <P>
		 * Type: STRING
		 * </P>
		 */
		// public static final String ALERT = "alert";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = START_HOUR + ", "
				+ START_MINUTES + " ASC";

		// Used when filtering enabled Airs.
		public static final String WHERE_ENABLED = ENABLED + "=1";

		static final String[] AIR_QUERY_COLUMNS = { _ID, START_HOUR,
				START_MINUTES, END_HOUR, END_MINUTES, DAYS_OF_WEEK,
				START_AIR_TIME, END_AIR_TIME, ENABLED, VIBRATE, MESSAGE,
				AIR_MODE_RADIOS, };

		/**
		 * These save calls to cursor.getColumnIndexOrThrow() THEY MUST BE KEPT
		 * IN SYNC WITH ABOVE QUERY COLUMNS
		 */
		public static final int AIR_ID_INDEX = 0;
		public static final int AIR_START_HOUR_INDEX = 1;
		public static final int AIR_START_MINUTES_INDEX = 2;
		public static final int AIR_END_HOUR_INDEX = 3;
		public static final int AIR_END_MINUTES_INDEX = 4;
		public static final int AIR_DAYS_OF_WEEK_INDEX = 5;
		public static final int AIR_START_TIME_INDEX = 6;
		public static final int AIR_END_TIME_INDEX = 7;
		public static final int AIR_ENABLED_INDEX = 8;
		public static final int AIR_VIBRATE_INDEX = 9;
		public static final int AIR_MESSAGE_INDEX = 10;
		public static final int AIR_MODE_RADIOS_INDEX = 11;
		// public static final int Air_ALERT_INDEX = 8;
	}

	// ////////////////////////////
	// End column definitions
	// ////////////////////////////

	// Public fields
	public int id;
	public boolean enabled;
	public int start_hour;
	public int start_minutes;
	public int end_hour;
	public int end_minutes;
	public DaysOfWeek daysOfWeek;
	public long start_time;
	public long end_time;
	public boolean vibrate;
	public String label;
	public AirModeRadio air_mode_radios;

	// public Uri alert;
	// public boolean silent;

	public Air(Cursor c) {
		id = c.getInt(Columns.AIR_ID_INDEX);
		enabled = c.getInt(Columns.AIR_ENABLED_INDEX) == 1;
		start_hour = c.getInt(Columns.AIR_START_HOUR_INDEX);
		start_minutes = c.getInt(Columns.AIR_START_MINUTES_INDEX);
		end_hour = c.getInt(Columns.AIR_END_HOUR_INDEX);
		end_minutes = c.getInt(Columns.AIR_END_MINUTES_INDEX);
		daysOfWeek = new DaysOfWeek(c.getInt(Columns.AIR_DAYS_OF_WEEK_INDEX));
		start_time = c.getLong(Columns.AIR_START_TIME_INDEX);
		end_time = c.getLong(Columns.AIR_END_TIME_INDEX);
		vibrate = c.getInt(Columns.AIR_VIBRATE_INDEX) == 1;
		label = c.getString(Columns.AIR_MESSAGE_INDEX);
		air_mode_radios = new AirModeRadio(
				c.getString(Columns.AIR_MODE_RADIOS_INDEX));
		// String alertString = c.getString(Columns.Air_ALERT_INDEX);
		// if (Airs.Air_ALERT_SILENT.equals(alertString)) {
		// if (Log.LOGV) {
		// Log.v("Air is marked as silent");
		// }
		// silent = true;
		// } else {
		// if (alertString != null && alertString.length() != 0) {
		// alert = Uri.parse(alertString);
		// }
		//
		// // If the database alert is null or it failed to parse, use the
		// // default alert.
		// if (alert == null) {
		// alert = RingtoneManager.getDefaultUri(
		// RingtoneManager.TYPE_Air);
		// }
		// }
	}

	public Air(Parcel p) {
		id = p.readInt();
		enabled = p.readInt() == 1;
		start_hour = p.readInt();
		start_minutes = p.readInt();
		end_hour = p.readInt();
		end_minutes = p.readInt();
		daysOfWeek = new DaysOfWeek(p.readInt());
		start_time = p.readLong();
		end_time = p.readLong();
		vibrate = p.readInt() == 1;
		label = p.readString();
		air_mode_radios = new AirModeRadio(p.readString());
		// alert = (Uri) p.readParcelable(null);
		// silent = p.readInt() == 1;
	}

	// Creates a default Air at the current time.
	public Air() {
		id = -1;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		start_hour = c.get(Calendar.HOUR_OF_DAY);
		start_minutes = c.get(Calendar.MINUTE);
		end_hour = c.get(Calendar.HOUR_OF_DAY);
		end_minutes = c.get(Calendar.MINUTE);
		vibrate = true;
		daysOfWeek = new DaysOfWeek(0);
		air_mode_radios = new AirModeRadio("");
		// alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_Air);
	}

	public String getLabelOrDefault(Context context) {
		if (label == null || label.length() == 0) {
			return context.getString(R.string.default_label);
		}
		return label;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Air))
			return false;
		final Air other = (Air) o;
		return id == other.id;
	}

	public static final class AirModeRadio {
		private static String[] RADIO_MAP = new String[] { "cell", "bluetooth",
				"wifi", };
		private boolean[] RET_MAP = new boolean[] { false, false, false, };
		private final static String reg = ",";

		AirModeRadio(String Radios) {
			set(Radios);
		}

		public boolean[] getBooleanArray() {
			return RET_MAP;
		}

		public void set(int which, boolean set) {
			RET_MAP[which] = set;
		}

		public String getEnableAirModeRadios() {
			String mRadios = "";
			if (RET_MAP[0] == false) {
				mRadios += RADIO_MAP[0];
				mRadios += reg;
			}
			if (RET_MAP[1] == false) {
				mRadios += RADIO_MAP[1];
				mRadios += reg;
			}
			if (RET_MAP[2] == false) {
				mRadios += RADIO_MAP[2];
				mRadios += reg;
			}
			if (mRadios.endsWith(reg)) {
				mRadios = mRadios.substring(0, mRadios.length() - 1);
			}
			return mRadios;
		}

		public String getDisabeAirModeRadios() {
			String mRadios = "";
			if (RET_MAP[0] == true) {
				mRadios += RADIO_MAP[0];
				mRadios += reg;
			}
			if (RET_MAP[1] == true) {
				mRadios += RADIO_MAP[1];
				mRadios += reg;
			}
			if (RET_MAP[2] == true) {
				mRadios += RADIO_MAP[2];
				mRadios += reg;
			}
			if (mRadios.endsWith(reg)) {
				mRadios = mRadios.substring(0, mRadios.length() - 1);
			}
			return mRadios;
		}

		public void set(String radios) {
			if (!radios.isEmpty()) {
				RET_MAP[0] = radios.contains(RADIO_MAP[0]) ? true : false;
				RET_MAP[1] = radios.contains(RADIO_MAP[1]) ? true : false;
				RET_MAP[2] = radios.contains(RADIO_MAP[2]) ? true : false;
			} else {
				RET_MAP[0] = false;
				RET_MAP[1] = false;
				RET_MAP[2] = false;
			}
		}

		public void set(AirModeRadio newRadio) {
			RET_MAP[0] = newRadio.RET_MAP[0];
			RET_MAP[1] = newRadio.RET_MAP[1];
			RET_MAP[2] = newRadio.RET_MAP[2];
		}

		public String toString(Context context, boolean showNever) {
			if (showNever) {
				if (getDisabeAirModeRadios().isEmpty()) {
					return context.getString(R.string.none).toString();
				}
			} else {

			}
			return showNever ? getDisabeAirModeRadios() : "";
		}

	}

	/*
	 * Days of week code as a single int. 0x00: no day 0x01: Monday 0x02:
	 * Tuesday 0x04: Wednesday 0x08: Thursday 0x10: Friday 0x20: Saturday 0x40:
	 * Sunday
	 */
	public static final class DaysOfWeek {

		private static int[] DAY_MAP = new int[] { Calendar.MONDAY,
				Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY,
				Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY, };

		// Bitmask of all repeating days
		private int mDays;

		DaysOfWeek(int days) {
			mDays = days;
		}

		public String toString(Context context, boolean showNever) {
			StringBuilder ret = new StringBuilder();

			// no days
			if (mDays == 0) {
				return showNever ? context.getText(R.string.never).toString()
						: "";
			}

			// every day
			if (mDays == 0x7f) {
				return context.getText(R.string.every_day).toString();
			}

			// count selected days
			int dayCount = 0, days = mDays;
			while (days > 0) {
				if ((days & 1) == 1)
					dayCount++;
				days >>= 1;
			}

			// short or long form?
			DateFormatSymbols dfs = new DateFormatSymbols();
			String[] dayList = (dayCount > 1) ? dfs.getShortWeekdays() : dfs
					.getWeekdays();

			// selected days
			for (int i = 0; i < 7; i++) {
				if ((mDays & (1 << i)) != 0) {
					ret.append(dayList[DAY_MAP[i]]);
					dayCount -= 1;
					if (dayCount > 0)
						ret.append(context.getText(R.string.day_concat));
				}
			}
			return ret.toString();
		}

		private boolean isSet(int day) {
			return ((mDays & (1 << day)) > 0);
		}

		public void set(int day, boolean set) {
			if (set) {
				mDays |= (1 << day);
			} else {
				mDays &= ~(1 << day);
			}
		}

		public void set(DaysOfWeek dow) {
			mDays = dow.mDays;
		}

		public int getCoded() {
			return mDays;
		}

		// Returns days of week encoded in an array of booleans.
		public boolean[] getBooleanArray() {
			boolean[] ret = new boolean[7];
			for (int i = 0; i < 7; i++) {
				ret[i] = isSet(i);
			}
			return ret;
		}

		public boolean isRepeatSet() {
			return mDays != 0;
		}

		/**
		 * returns number of days from today until next Air
		 * 
		 * @param c
		 *            must be set to today
		 */
		public int getNextAir(Calendar c) {
			if (mDays == 0) {
				return -1;
			}

			int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;

			int day = 0;
			int dayCount = 0;
			for (; dayCount < 7; dayCount++) {
				day = (today + dayCount) % 7;
				if (isSet(day)) {
					break;
				}
			}
			return dayCount;
		}
	}
}
