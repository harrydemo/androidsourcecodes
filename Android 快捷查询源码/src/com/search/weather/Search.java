package com.search.weather;

import com.search.RequestListener;
import com.search.common.HttpUtils;

import android.os.Bundle;
import android.util.Log;

/**
 * 查询类
 * @author Administrator
 *
 */
public class Search {
	
	//这里使用的是雅虎天气查询url
	private static final String HTTP_URL = "http://weather.yahooapis.com/forecastrss";
	private static final String METHOD = "GET";
	private static final String LOG_TAG = "com.search.weather.Search";
	
	
	public String request(String woeid){
		if(woeid != null){
			Bundle params = new Bundle();
			params.putString("w", woeid); //城市对应的id
			params.putString("u", "c"); //华氏温度是f，摄氏温度是c。这里采用摄氏度
			return HttpUtils.openUrl(HTTP_URL, METHOD, params,null);
		}else{
			return null;
		}
	}
	
	//异步封装
	public void asyncRequest(final String city, final RequestListener listener){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					String response = request(city);
					listener.onComplete(response);
				} catch (Exception e) {
					Log.e(LOG_TAG, e.getMessage());
					listener.onException(e);
				}
				
			}
		}).start();
	}

}
