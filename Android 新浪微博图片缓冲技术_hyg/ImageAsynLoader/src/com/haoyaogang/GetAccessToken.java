package com.haoyaogang;

import weibo4android.Weibo;

public class GetAccessToken {

	
	public static void main(String args[])
	{
		//������д ��� app key and app secret
		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
		System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
	
		//����һ������
		Weibo weibo = new Weibo();
		
		
	}
}
