/*
 * Copyright 2010 Renren, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.renren.api.connect.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.renren.api.connect.android.exception.RenrenError;

/**
 *工具支持类
 * 
 * @author yong.li@opi-corp.com
 * 
 */
public final class Util {
	public static final String LOG_TAG = "Renren-Connect";

	/**
	 * 将Key-value转换成用&号链接的URL查询参数形式。
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

	/**
	 * 将用&号链接的URL参数转换成key-value形式。
	 * 
	 * @param s
	 * @return
	 */
	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			params.putString("url", s);
			String array[] = s.split("&");
			for (String parameter : array) {
				String v[] = parameter.split("=");
				if (v.length > 1)
					params.putString(v[0], URLDecoder.decode(v[1]));
			}
		}
		return params;
	}

	/**
	 * 解析URL中的查询串转换成key-value
	 * 
	 * @param url
	 * @return
	 */
	public static Bundle parseUrl(String url) {
		url = url.replace("rrconnect", "http");
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}

	/**
	 * 发送http请求
	 * 
	 * @param url
	 * @param method
	 *            GET 或 POST
	 * @param params
	 * @return
	 */
	public static String openUrl(String url, String method, Bundle params) {
		if (method.equals("GET")) {
			url = url + "?" + encodeUrl(params);
		}
		String response = "";
		try {
			Log.d(LOG_TAG, method + " URL: " + url);
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();
			conn.setRequestProperty("User-Agent", System.getProperties()
					.getProperty("http.agent")
					+ " RenrenAndroidSDK");
			if (!method.equals("GET")) {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.getOutputStream().write(
						encodeUrl(params).getBytes("UTF-8"));
			}

			response = read(conn.getInputStream());
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage());
			throw new RuntimeException(e.getMessage(), e);
		}
		return response;
	}

	private static String read(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	public static void clearCookies(Context context) {
		@SuppressWarnings("unused")
		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	/**
	 * 将服务器返回的错误JSON串，转化成RenrenError.
	 * 
	 * @param JSON串
	 * @return
	 */
	private static RenrenError parseJson(String jsonResponse) {
		try {
			JSONObject json = new JSONObject(jsonResponse);
			return new RenrenError(json.getInt("error_code"), json
					.getString("error_msg"), jsonResponse);
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 将服务器返回的错误XML串，转化成RenrenError.
	 * 
	 * @param JSON串
	 * @return
	 */
	private static RenrenError parseXml(String xmlResponse) {
		XmlPullParser parser = Xml.newPullParser();
		RenrenError error = null;
		try {
			parser.setInput(new StringReader(xmlResponse));
			int evtCode = parser.getEventType();
			int errorCode = -1;
			String errorMsg = null;
			while (evtCode != XmlPullParser.END_DOCUMENT) {
				switch (evtCode) {
				case XmlPullParser.START_TAG:
					if ("error_code".equals(parser.getName())) {
						errorCode = Integer.parseInt(parser.nextText());
					}
					if ("error_msg".equals(parser.getName())) {
						errorMsg = parser.nextText();
					}
					break;
				}
				if (errorCode > -1 && errorMsg != null) {
					error = new RenrenError(errorCode, errorMsg, xmlResponse);
					break;
				}
				evtCode = parser.next();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return error;
	}

	/**
	 * 响应的内容是错误信息时，被转化成RenrenError，否则返回NULL
	 * 
	 * @param response
	 * @param responseFormate
	 * @return
	 */
	public static RenrenError parseRenrenError(String response,
			String responseFormate) {
		if (response.indexOf("error_code") < 0)
			return null;
		if (Renren.RESPONSE_FORMAT_JSON.equalsIgnoreCase(responseFormate))
			return parseJson(response);
		return parseXml(response);
	}

	/**
	 * 显示一个简单的对话框；只能在UI线程中调用。
	 * 
	 * @param context
	 * @param title
	 * @param text
	 */
	public static void showAlert(Context context, String title, String text,
			boolean showOk) {
		Builder alertBuilder = new Builder(context);
		alertBuilder.setTitle(title);
		alertBuilder.setMessage(text);
		if (showOk)
			alertBuilder.setNeutralButton("确定", null);
		alertBuilder.create().show();
	}

	/**
	 * 显示一个简单的对话框；只能在UI线程中调用。
	 * 
	 * @param context
	 * @param title
	 * @param text
	 */
	public static void showAlert(Context context, String title, String text) {
		showAlert(context, title, text, true);
	}

	public static String md5(String string) {
		if (string == null || string.trim().length() < 1) {
			return null;
		}
		try {
			return getMD5(string.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static String getMD5(byte[] source) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			StringBuffer result = new StringBuffer();
			for (byte b : md5.digest(source)) {
				result.append(Integer.toHexString((b & 0xf0) >>> 4));
				result.append(Integer.toHexString(b & 0x0f));
			}
			return result.toString();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
