package com.android.caigang.util;

import android.content.Context;

import com.android.caigang.db.DataHelper;
import com.mime.qweibo.examples.MyWeiboSync;

public class DataBaseContext {
	
	private static DataHelper dataHelper;
	
	private static Object INSTANCE_LOCK = new Object();
	
	public static DataHelper getInstance(Context context) {
	       synchronized (INSTANCE_LOCK) {
	           if (dataHelper == null) {
	        	   dataHelper = new DataHelper(context);
	           }
	           return dataHelper;
	       }
	   }
}
