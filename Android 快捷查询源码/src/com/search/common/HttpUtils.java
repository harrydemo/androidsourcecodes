package com.search.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.util.Log;

public final class HttpUtils {
	
	private static final String LOG_TAG = "HttpUtils Connect Tag";
	
	/**
	 * http����
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 */
	public static String openUrl(String url, String method, Bundle params, String enc){
		
		String response = null;
		
		if(method.equals("GET")){
			url = url + "?" + encodeUrl(params);
		}
		
		try {
			Log.d(LOG_TAG, "Url:"+url);
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestProperty("User-Agent", System.getProperties()
					.getProperty("http.agent")
					);
			conn.setReadTimeout(10000); //设置超时时间
			if(method.equals("POST")){
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.getOutputStream().write(encodeUrl(params).getBytes("UTF-8"));
			}
			response = read(conn.getInputStream(),enc);
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			throw new RuntimeException(e.getMessage(),e);
		}
		return response;
	}
	

	
	/**
	 * 
	 * @param in
	 * @param enc
	 * @return
	 * @throws IOException
	 */
	private static String read(InputStream in, String enc) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		BufferedReader r = null;
		if(enc != null){
			//按指定的编码读入流
			r = new BufferedReader(new InputStreamReader(in,enc), 1000);
		}else{
			//按默认的编码读入
			r = new BufferedReader(new InputStreamReader(in), 1000);
		}

		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	/**
	 * ��Key-valueת������&��t�ӵ�URL��ѯ������ʽ��
	 * 
	 * @param parameters
	 * @return
	 */
	public static String encodeUrl(Bundle parameters) {
		if (parameters == null)
			return "";
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : parameters.keySet()) {
			if (first)
				first = false;
			else
				sb.append("&");
			sb.append(key + "=" + parameters.getString(key));
		}
		return sb.toString();
	}
	
	
}
