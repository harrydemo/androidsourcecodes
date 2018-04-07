package com.shinylife.smalltools.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class DeviceHelper {
	Activity mActivity;

	public DeviceHelper(Activity activity) {
		mActivity = activity;
	}

	public static String getMODEL() {
		return Build.MODEL;
	}

	public static int getSDKVersion() {
		return Integer.parseInt(Build.VERSION.SDK);
	}

	public static String getReleaseVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 读取手机号 need <uses-permission
	 * android:name="android.permission.READ_PHONE_STATE" />
	 * 
	 * @param activity
	 * @return
	 */
	public String getPhoneNo() {
		TelephonyManager tm = (TelephonyManager) mActivity
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNo = tm.getLine1Number();
		return phoneNo;
	}

	/**
	 * 获取尺寸
	 * 
	 * @return
	 */
	public DisplayMetrics getDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

}
