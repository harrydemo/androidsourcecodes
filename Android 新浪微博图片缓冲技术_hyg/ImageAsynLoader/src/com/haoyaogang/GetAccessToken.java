package com.haoyaogang;

import weibo4android.Weibo;

public class GetAccessToken {

	
	public static void main(String args[])
	{
		//首先填写 你得 app key and app secret
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	
		//创建一个对象
		Weibo weibo = new Weibo();
		
		
	}
}
