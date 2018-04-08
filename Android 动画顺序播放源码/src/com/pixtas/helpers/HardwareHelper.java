package com.pixtas.helpers;

import com.pixtas.yogapowervinyasa.Config;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class HardwareHelper {
	private static final String TAG = "HardwareHelper";
	/*检查网络*/
	public static boolean connectedNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		for(NetworkInfo info: cm.getAllNetworkInfo()){
			if(info.isConnected()){
				return true;
			}
		}
		return false;
	}
	
	/*获取手机唯一性标识*/
	public static String getToken(Application app) {
		TelephonyManager mgr = (TelephonyManager)app.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = mgr.getDeviceId();
		if(deviceId == null || deviceId.length() < 1){
			return null;
		}
		
		int phoneType = mgr.getPhoneType();
		if(phoneType == TelephonyManager.PHONE_TYPE_NONE)
			return null;
		if(Config.debug){
			Log.i(TAG,deviceId + "@a." + (phoneType == TelephonyManager.PHONE_TYPE_GSM ? "g" : "c"));
		}
		return deviceId + "@a." + (phoneType == TelephonyManager.PHONE_TYPE_GSM ? "g" : "c");
	}
	
	/*获取imei*/
	public static String getImei(Application app){
		TelephonyManager tm = (TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE);
    	return tm.getDeviceId();
	}
	
}
