package com.search.ems;

import com.search.RequestListener;
import com.search.common.HttpUtils;

import android.os.Bundle;
import android.util.Log;

public class Search {
	
	private static final String HTTP_URL = "http://api.kuaidi100.com/apione";
	
	private static final String PARAM_NAME_COMPANY= "com"; //公司代码
	
	private static final String PARAM_NAME_ORDER = "nu"; //订单号
	
	private static final String PARAM_NAME_RETURN = "show"; //返回值的类型：0，json；1，xml；2
	
	private static final String METHOD = "GET";
	
	/**
	 * 
	 * @param companyCode：公司代码
	 * @param order:订单号
	 * @return
	 */
	public String request(String companyCode, String order){
		
		Bundle params = new Bundle();
		params.putString(PARAM_NAME_COMPANY, companyCode);
		params.putString(PARAM_NAME_ORDER, order);
		params.putString(PARAM_NAME_RETURN, "0"); //采用json返回值类型
		
		
		return HttpUtils.openUrl(HTTP_URL, METHOD, params, null);
	}
	
	public void asyncRequest(final String companyCode, final String order, final RequestListener listener){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					String returnJson = request(companyCode, order);
					listener.onComplete(returnJson);
				} catch (Exception e) {
					Log.e(LOG_TAG, e.getMessage());
					listener.onException(e);
				}
				
			}
		}).start();
	}
	
	private static final String LOG_TAG = "com.search.ems.Search";

}
