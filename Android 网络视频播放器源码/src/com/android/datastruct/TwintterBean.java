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
package com.android.datastruct;


public class TwintterBean
{

	private String token;
	private String token_secret;

	public TwintterBean()
	{
		token = "";
		token_secret = "";
	}

	public String GetToken()
	{
		return token;
	}

	public String GetTokenSecret()
	{
		return token_secret;
	}

	public void SetToken(String s)
	{
		token = s;
	}

	public void SetTokenSecret(String s)
	{
		token_secret = s;
	}
}
