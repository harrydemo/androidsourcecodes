package com.xcontacts.utils;

import android.util.Log;

/**
 * 对Log进行简单的封装.不需要Log时,把boolean对象赋值为false即可
 * 
 * @author Lefter
 */
public final class MyLog {
	private static boolean openLog = true;
	private static final String TAG = "XContacts";

	public static void d(String msg) {
		if (openLog) {
			Log.d(TAG, msg);
		}
	}

	public static void v(String msg) {
		if (openLog) {
			Log.v(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (openLog) {
			Log.e(TAG, msg);
		}
	}

	public static void i(String msg) {
		if (openLog) {
			Log.i(TAG, msg);
		}
	}

	public static void w(String msg) {
		if (openLog) {
			Log.w(TAG, msg);
		}
	}
}