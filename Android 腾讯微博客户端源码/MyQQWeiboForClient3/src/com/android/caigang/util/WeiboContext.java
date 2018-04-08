package com.android.caigang.util;

import com.mime.qweibo.examples.MyWeiboSync;

public class WeiboContext {
	
	private static MyWeiboSync weibo;
	
	private static Object INSTANCE_LOCK = new Object();
	
	public static MyWeiboSync getInstance() {
	       synchronized (INSTANCE_LOCK) {
	           if (weibo == null) {
	        	   weibo = new MyWeiboSync();
	           }
	           return weibo;
	       }
	   }
}
