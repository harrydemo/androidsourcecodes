package com.cundong.share.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/** 
 * 类说明：    网络监测类
 * @author  @Cundong
 * @weibo   http://weibo.com/liucundong
 * @blog    http://www.liucundong.com
 * @date    Aug 26, 2012 2:50:48 PM
 * @version 1.0
 */
public class NetworkUtils {
	public final static int NONE = 0; 	// 无网络
	public final static int WIFI = 1; 	// Wi-Fi
	public final static int MOBILE = 2; // 3G,GPRS

	/**
	 * 获取当前网络状态
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetworkState(Context context) 
	{
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// 手机网络判断
		State state = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING)
		{
			return MOBILE;
		}

		// Wifi网络判断
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING)
		{
			return WIFI;
		}
		return NONE;
	}
}