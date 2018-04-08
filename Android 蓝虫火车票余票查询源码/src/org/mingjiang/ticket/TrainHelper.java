package org.mingjiang.ticket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TrainHelper {
	private TrainHelper() {
	}

	public static String parseNumStr(String num) {
		if (Integer.parseInt(num) >= 1 && Integer.parseInt(num) < 10) {
			num = "0" + num;
		}
		return num;
	}
	
	public static String Train_AllType = "全部";
	public static String Train_DCType_Text = "动车";
	public static String Train_ZType_Text = "直快";
	public static String Train_TType_Text = "特快";
	public static String Train_KType_Text = "快速";
	public static String Train_PKType_Text = "普快";
	public static String Train_PKEType_Text = "普客";
	public static String Train_LKType_Text = "临客";
	
	

}
