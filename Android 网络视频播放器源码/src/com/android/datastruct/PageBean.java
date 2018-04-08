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

public class PageBean
{

	private String pageIndex;
	private String url;

	public PageBean()
	{
		url = "";
		pageIndex = "";
	}

	public String GetPageIndex()
	{
		return pageIndex;
	}

	public String GetUrl()
	{
		return url;
	}

	public void SetPageIndex(String s)
	{
		pageIndex = s;
	}

	public void SetUrl(String s)
	{
		url = s;
	}
}
