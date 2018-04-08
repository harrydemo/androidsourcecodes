package com.android.tools;
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

public interface RegexCallback
{

	public abstract void NotifyDataAnalysisFinished();

	public abstract void RegexRespone(String s, String s1);

	public abstract void RegexRespone(String s, String s1, String s2, String s3);
}
