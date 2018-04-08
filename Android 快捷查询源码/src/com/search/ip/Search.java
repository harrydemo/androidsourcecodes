package com.search.ip;

import com.search.RequestListener;
import com.search.common.HttpUtils;

import android.os.Bundle;

public class Search {
	
	private static final String HTTP_URL = "http://www.youdao.com/smartresult-xml/search.s";
	
	private static final String TYPE = "ip";	
	
	public String request(String ip){
		
		Bundle params = new Bundle();
		params.putString("type", TYPE);
		params.putString("q", ip);
		
		return HttpUtils.openUrl(HTTP_URL, "GET", params,"gbk");
		//return HttpUtils.getUrl(HTTP_URL, params);
		
	}
	
	public void asyncRequest(final String ip, final RequestListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					String responseXml = request(ip);
					responseXml = new String(responseXml.getBytes(),"UTF-8");
					listener.onComplete(responseXml);
				} catch (Exception e) {
					listener.onException(e);
				}
				
			}
		}).start();
	}

}
