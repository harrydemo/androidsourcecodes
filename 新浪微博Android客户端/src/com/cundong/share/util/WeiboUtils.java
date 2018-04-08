package com.cundong.share.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

/** 
 * 类说明：    Weibo工具类
 * @author  @Cundong
 * @weibo   http://weibo.com/liucundong
 * @blog    http://www.liucundong.com
 * @date    Aug 26, 2012 2:50:48 PM
 * @version 1.0
 */
public class WeiboUtils {
	
	/**
	 * 判断存储于本地的Token是否过期
	 * @param accessToken
	 * @param expiresIn
	 * @return
	 */
	public static boolean isSessionValid(String accessToken, long expiresIn) {
		return (!TextUtils.isEmpty(accessToken) && (expiresIn == 0 || (System
				.currentTimeMillis() < expiresIn)));
	}
	
	/**
	 * 更新本地Token信息
	 * @param data
	 * @param uid
	 * @param localToken
	 * @param localExpiresIn
	 */
	public static void updateLocalToken(SharedPreferences data, String uid, String localToken, long localExpiresIn){
		SharedPreferences.Editor editor = data.edit();
		editor.putString("uid", uid);
		editor.putString("localToken", localToken);
		editor.putLong("localExpiresIn", localExpiresIn);
		editor.commit();
	}
	
	public static String upload(Context context, Weibo weibo, String source, String file,
			String status, String lon, String lat) throws WeiboException {
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("pic", file);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/upload.json";
		try {
			rlt = weibo.request(context, url, bundle, Utility.HTTPMETHOD_POST,
					weibo.getAccessToken());
		} catch (WeiboException e) {
			throw new WeiboException(e);
		}
		return rlt;
	}

	public static String update(Context context, Weibo weibo, String source, String status,
			String lon, String lat) throws WeiboException {

		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		String rlt = "";
		String url = Weibo.SERVER + "statuses/update.json";
		try {
			rlt = weibo.request(context, url, bundle, Utility.HTTPMETHOD_POST,
					weibo.getAccessToken());
		} catch (WeiboException e) {
			throw new WeiboException(e);
		}

		return rlt;
	}
}
