package com.mime.qweibo.examples;

import com.mime.qweibo.examples.QWeiboType.ResultType;

public class MyWeiboASync {
	public static QWeiboAsyncApi api;
	public static String customKey = "bfd047f9434640bcb009031e59fb9fef";
	public static String customSecrect = "1c2397bee7b595c2d0f18c11fa83b4dd";
	public String tokenKey = null;
	public String tokenSecrect = null;
	public String verify = null;
	public String accessTokenKey = null;
	public String accessTokenSecrect = null;
	public String authorizeUrl = null;
	
	
	
	public boolean getRequestToken(){
		Boolean isSucess =  api.getRequestToken(customKey, customSecrect);
		return isSucess;
	}
	
	public String getAuthorizeUrl(){
		return "http://open.t.qq.com/cgi-bin/authorize?oauth_token=" + tokenKey;
	}
	
	public boolean getAccessToken(String tokenKey,String tokenSecrect,String verify){
		boolean isSucess = api.getAccessToken(customKey, customSecrect, tokenKey, tokenSecrect, verify);
		return isSucess;
	}
	
	public boolean publishMsg(String accessTokenKey,String accessTokenSecrect,String content){
		 boolean isSucess = api.publishMsg(customKey, customSecrect, accessTokenKey, accessTokenSecrect, content, null, ResultType.ResultType_Json);
		 return isSucess;
	}

}
