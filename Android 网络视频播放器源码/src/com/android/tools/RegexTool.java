
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
import android.content.Context;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool{

	private RegexCallback callback;
	private Context context;

	public RegexTool(Context context1, RegexCallback regexcallback){
		callback = regexcallback;
		context = context1;
	}

	private String Find_a_content(String s, String s1, String s2, String s3)
	{
		String s4 = "";
		Matcher matcher = Pattern.compile(s1).matcher(s);
		do
		{
			if (!matcher.find())
			{
				if (s4 != null)
					s4 = s4.replace(s2, "").replace(s3, "");
				return s4;
			}
			s4 = matcher.group();
		} while (true);
	}

	private String ReplaceAllSpace(String s)
	{
		return Pattern.compile("\\s*", 8).matcher(s).replaceAll("");
	}

	//对视频详情页数据解析
	public void RegexAnalysisItemDetail(String s){
		Matcher matcher = Pattern.compile("<a.*?/a>").matcher(s);
		
		while(matcher.find()){
			String s1 = matcher.group();
			String s2 = ReplaceAllSpace(s1);
			String s3 = Find_a_content(s2, "href=\".*?\"", "href=\"", "\"");
			String s4 = Find_a_content(s2, "title=\".*?\"", "title=\"", "\"");
			String s5 = Find_a_content(s2, "detail=\".*?\"", "detail=\"", "\"");
			String s6 = Find_a_content(s2, "src=\".*?\"", "src=\"", "\"");
			if (!s3.contains("get_") && s3.contains("&site")){
				callback.RegexRespone(s3, s4, s5, s6);
			}
				
			else{
				if (s3.contains("get_")){
					callback.RegexRespone(s3, s5);
				}
					
			}
		}
		callback.NotifyDataAnalysisFinished();
		return;
	}

	//对返回的数据进行解析
	public void RegexAnalysisListItme(String s){
		Matcher matcher = Pattern.compile("<a.*?/a>").matcher(s);
		
		while(matcher.find()){
			String s1 = matcher.group();
			String s2 = ReplaceAllSpace(s1);
			String s3 = Find_a_content(s2, "href=\".*?\"", "href=\"", "\"");
			String s4 = Find_a_content(s2, "title=\".*?\"", "title=\"", "\"");
			String s5 = Find_a_content(s2, "detail=\".*?\"", "detail=\"", "\"");
			String s6 = Find_a_content(s2, "src=\".*?\"", "src=\"", "\"");
			if (!s3.contains("index") && s3.contains("&site")){
				callback.RegexRespone(s3, s4, s5, s6);
			}
			else{
				if (s3.contains("index") && s3.contains("&o=1"))
					callback.RegexRespone(s3, s4);
			}
		}
		callback.NotifyDataAnalysisFinished();
		return;
	}

	public void RegexAnalysisSearchKeywords(String s)
	{
		Matcher matcher = Pattern.compile("<span.*?/span>").matcher(s);
		do
		{
			String s3;
			do
			{
				if (!matcher.find())
				{
					callback.NotifyDataAnalysisFinished();
					return;
				}
				String s1 = matcher.group();
				String s2 = ReplaceAllSpace(s1);
				s3 = Find_a_content(s2, "key\">.*?</span", "key\">", "</span");
			} while (s3 == null || s3.length() <= 0);
			callback.RegexRespone(s3, s3);
		} while (true);
	}
}
