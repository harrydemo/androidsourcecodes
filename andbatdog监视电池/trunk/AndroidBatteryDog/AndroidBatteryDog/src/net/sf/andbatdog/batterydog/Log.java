package net.sf.andbatdog.batterydog;

public class Log {

	private final static boolean logEnabled = false;
	
	public static void i(String tag, String msg) {
		if (logEnabled) {
			android.util.Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		android.util.Log.e(tag, msg, tr);
	}

	public static void e(String tag, String msg) {
		if (logEnabled) {
			android.util.Log.e(tag, msg);
		}
	}

}
