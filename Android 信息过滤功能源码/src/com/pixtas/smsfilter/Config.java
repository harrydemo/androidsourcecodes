package com.pixtas.smsfilter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Config {
//	’˝ Ω’æ
	public static final String point = "http://www.pixtas.com/";
	
//	≤‚ ‘’æ
//	public static final String point = "http://pixtas.haojiubujian.com/";
	
	public static final String check_version_url = point + "ver4/4/album/smsFilter/check/version/";
	
	public static final String appName = "smsFilter";
	public static final String version = "1.0.0";
	
	public static final String SMS_RECEIVE_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static final String PHONE_STATE = "android.intent.action.PHONE_STATE";
	public static boolean debug = true;
	
	/*ºÏ≤ÈÕ¯¬Á*/
	public static boolean connectedNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		for(NetworkInfo info: cm.getAllNetworkInfo()){
			if(info.isConnected()){
				return true;
			}
		}
		return false;
	}
}
