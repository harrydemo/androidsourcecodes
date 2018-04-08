/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {
	
	public static boolean CheckNetwork(Context context){
		NetworkInfo info = null;
	 info = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
	  if(info!=null && info.isConnected()) return true;
	  else return false;
	}
	
	
}
