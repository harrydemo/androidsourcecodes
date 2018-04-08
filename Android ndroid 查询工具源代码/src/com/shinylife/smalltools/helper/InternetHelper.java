package com.shinylife.smalltools.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetHelper {
	Activity mActivity;
	public InternetHelper(Activity activity){
		mActivity=activity;
	}
	/**
	 * need   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	 * @param activity
	 * @return
	 */
	public boolean getNetworkIsAvailable() {
		ConnectivityManager manager = (ConnectivityManager) mActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}
}
