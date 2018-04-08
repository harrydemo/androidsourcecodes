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

/*
 * 数据接口
 * */
public class ListItemBean
{

	private String VideoHref;
	private String VideoImgSrc;
	private String VideoLength;
	private String VideoTitle;

	public ListItemBean()
	{
	}

	public String getVideoHref()
	{
		return VideoHref;
	}

	public String getVideoImgSrc()
	{
		return VideoImgSrc;
	}

	public String getVideoLength()
	{
		return VideoLength;
	}

	public String getVideoTitle()
	{
		return VideoTitle;
	}

	public void setVideoHref(String s)
	{
		VideoHref = s;
	}

	public void setVideoImgSrc(String s)
	{
		VideoImgSrc = s;
	}

	public void setVideoLength(String s)
	{
		VideoLength = s;
	}

	public void setVideoTitle(String s)
	{
		VideoTitle = s;
	}
}
