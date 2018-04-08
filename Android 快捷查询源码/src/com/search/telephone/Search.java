package com.search.telephone;

import android.os.Bundle;
import android.util.Log;

import com.search.RequestListener;
import com.search.common.HttpUtils;

public class Search {
	
	private static final String  HTTP_URL = "http://api.showji.com/Locating/default.aspx";
	
	private static final String METHOD = "GET";
	
	private static final String OUTPUT = "json";
	
	private static final String CALLBACK = "querycallback";
	
	private static final String LOG_TAG = "com.search.telephone.Search";
	
	/**
	 * �����װ
	 * @param telephone
	 * @return
	 */
	private String request(String telephone){
		
    	Bundle data = new Bundle();
    	data.putString("m", telephone);
    	data.putString("output", OUTPUT);
    	data.putString("callback", CALLBACK);
    	
    	return HttpUtils.openUrl(HTTP_URL, METHOD, data,null);		
	}
	
	//����������첽��װ
	public void asyncRequest(final String telephone, final RequestListener listener){
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					String response = request(telephone);
					listener.onComplete(response); //��listener4������
				} catch (Exception e) {
					Log.e(LOG_TAG, e.getMessage());
					listener.onException(e);
				}
			}
			
		}).start();
	}

}
