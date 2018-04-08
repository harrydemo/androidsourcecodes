package com.android.weibo;
/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import weibo4android.Weibo;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;

public class OAuthConstant
{

	private static OAuthConstant instance = null;
	private static Weibo weibo = null;
	private AccessToken accessToken;
	private RequestToken requestToken;
	private String token;
	private String tokenSecret;

	private OAuthConstant()
	{
	}

	/**
	 * @deprecated Method getInstance is deprecated
	 */

	public static OAuthConstant getInstance(){
		try {
			if (instance == null)
				instance = new OAuthConstant();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return instance;
	}

	public AccessToken getAccessToken()
	{
		return accessToken;
	}

	public RequestToken getRequestToken()
	{
		return requestToken;
	}

	public String getToken()
	{
		return token;
	}

	public String getTokenSecret()
	{
		return tokenSecret;
	}

	public Weibo getWeibo()
	{
		if (weibo == null)
			weibo = new Weibo();
		return weibo;
	}

	public void setAccessToken(AccessToken accesstoken)
	{
		accessToken = accesstoken;
		String s = accesstoken.getToken();
		token = s;
		String s1 = accesstoken.getTokenSecret();
		tokenSecret = s1;
	}

	public void setRequestToken(RequestToken requesttoken)
	{
		requestToken = requesttoken;
	}

	public void setToken(String s)
	{
		token = s;
	}

	public void setTokenSecret(String s)
	{
		tokenSecret = s;
	}

}
