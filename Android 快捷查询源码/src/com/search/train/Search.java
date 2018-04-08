package com.search.train;

import com.search.RequestListener;
import com.search.common.HttpUtils;

import android.os.Bundle;
import android.util.Log;



public class Search{

	private static final String HTTP_URL = "http://assistant.mail.yeah.net/assistant/train.do";
	
	private static final String EVENT_VALUE = "fTrainS2SCallBack";
	
	private static final String OP_VALUE = "searchs2s";
	
	private static final String START_VALUE = "0";
	
	private static final String SIZE_VALUE = "100";
	
	private static final String METHOD = "GET";
	
	
	
	public String request(String queryFrom, String queryTo){
		
		Bundle bundle = new Bundle();
		bundle.putString("event", EVENT_VALUE);
		bundle.putString("op", OP_VALUE);
		bundle.putString("start", START_VALUE);
		bundle.putString("size", SIZE_VALUE);
		bundle.putString("queryFrom", queryFrom);
		bundle.putString("queryTo", queryTo);
		//bundle.putString("extend", this.getExtend(queryFrom, queryTo));
		
		return HttpUtils.openUrl(HTTP_URL, METHOD, bundle,"gbk");
		
	}

	public void asyncRequest(final String queryFrom, final String queryTo, final RequestListener listener){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					String returnJSON = request(queryFrom, queryTo);
					listener.onComplete(returnJSON);
				} catch (Exception e) {
					Log.v(LOG_TAG, e.getMessage());
					listener.onException(e);
				}
				
			}
		}).start();
		
	}

	private String getExtend(String queryFrom, String queryTo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static final String LOG_TAG = "com.search.train.Search";
	
	
}
