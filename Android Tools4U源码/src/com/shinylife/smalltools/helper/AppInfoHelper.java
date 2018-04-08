package com.shinylife.smalltools.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppInfoHelper {
	private Context mContext;
	private final String pName = "com.shinylife.baibaoxian";

	public AppInfoHelper(Context context) {
		mContext = context;
	}

	public int getVersionCode() {
		try {
			PackageInfo pinfo = mContext.getPackageManager().getPackageInfo(
					pName, PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionCode;
		} catch (NameNotFoundException e) {
		}
		return 0;
	}

	public String getVersionName() {
		try {
			PackageInfo pinfo = mContext.getPackageManager().getPackageInfo(
					pName, PackageManager.GET_CONFIGURATIONS);
			return pinfo.versionName;
		} catch (NameNotFoundException e) {
		}
		return null;
	}
}
