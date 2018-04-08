package com.updateapp;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
public class CurrentVersion {
	private static final String TAG = "Config";
	public static final String appPackName = "com.updateapp";
	public static int getVerCode(Context context)throws NameNotFoundException{
		int verCode = -1;
		try{
			verCode = context.getPackageManager().getPackageInfo(appPackName, 0).versionCode;	
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}
	public static String getVerName(Context context){
		String verName = "";
		try{
			verName = context.getPackageManager().getPackageInfo(appPackName, 0).versionName;
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
		}
		return verName;
	}
	public static String getAppName(Context context){
		String appName = context.getResources().getText(R.string.app_name).toString();
		return appName;
		}
}
