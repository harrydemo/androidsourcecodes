package com.mime.qweibo.examples;

import android.util.Log;

import com.mime.qweibo.examples.QWeiboType.PageFlag;
import com.mime.qweibo.examples.QWeiboType.ResultType;

public class MyWeiboSync {
	private QWeiboSyncApi api = new QWeiboSyncApi();
	private static String customKey = "bfd047f9434640bcb009031e59fb9fef";
	private static String customSecret = "1c2397bee7b595c2d0f18c11fa83b4dd";
	private String tokenKey = null;
	private String tokenSecrect = null;
	private String verify = null;
	private String accessTokenKey = null;
	private String accessTokenSecrect = null;
	public static String CALLBACK_URL = "weibo://accountActivity";
	
	
	public String getAccessTokenKey() {
		return accessTokenKey;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public String getTokenSecrect() {
		return tokenSecrect;
	}

	public String getVerify() {
		return verify;
	}

	public String getAccessTokenSecrect() {
		return accessTokenSecrect;
	}


	public void setAccessTokenKey(String accessTokenKey) {
		this.accessTokenKey = accessTokenKey;
	}

	public void setAccessTokenSecrect(String accessTokenSecrect) {
		this.accessTokenSecrect = accessTokenSecrect;
	}

	public void getRequestToken(){
		String response =  api.getRequestToken(customKey, customSecret);
		 if (!parseToken(response,false)) {
	        	return;
	     }
	}
	
	public String getAuthorizeUrl(){
		return "http://open.t.qq.com/cgi-bin/authorize?oauth_token=" + tokenKey;
	}
	
	public void getAccessToken(String tokenKey,String tokenSecrect,String verify){
		String response = api.getAccessToken(customKey, customSecret, tokenKey, tokenSecrect, verify);
		 if (!parseToken(response,true)) {
	        	return;
	     }
		 Log.d("MyWeiboSync", response);
	}
	
	public String publishMsg(String accessTokenKey,String accessTokenSecrect,String content){
		 String response = api.publishMsg(customKey, customSecret, accessTokenKey, accessTokenSecrect, content, null, ResultType.ResultType_Json);
		 return response;
	}
	
	public String getUserInfo(String requestToken, String requestTokenSecrect){
		 String response = api.getUserInfo(customKey, customSecret, requestToken, requestTokenSecrect, ResultType.ResultType_Json);
		 return response;
	}
	
	public String getHomeMsg(String requestToken, String requestTokenSecrect,PageFlag pageFlag, int nReqNum) {
		 String response = api.getHomeMsg(customKey, customSecret, requestToken, requestTokenSecrect, ResultType.ResultType_Json, pageFlag, nReqNum);
		 return response;
	}
	
	public String getWeiboDetail(String requestToken, String requestTokenSecrect,String id) {
		 String response = api.getWeiboDetail(customKey, customSecret, requestToken, requestTokenSecrect, id, ResultType.ResultType_Json);
		 return response;
	}
	
	public String getUserInfoByName(String requestToken, String requestTokenSecrect,String name) {
		 String response = api.getUserInfoByName(customKey, customSecret, requestToken, requestTokenSecrect, name, ResultType.ResultType_Json);
		 return response;
	}
	
	public String getRefers(String requestToken, String requestTokenSecrect,PageFlag pageflag,int pagetime,int reqnum,int lastid) {
		 String response = api.getRefers(customKey, customSecret, requestToken, requestTokenSecrect, pageflag, pagetime, reqnum, lastid,ResultType.ResultType_Json);
		 return response;
	}
	
	public String getTweets(String requestToken, String requestTokenSecrect,PageFlag pageflag,int pagetime,int reqnum,int lastid,String name) {
		 String response = api.getTweets(customKey, customSecret, requestToken, requestTokenSecrect, pageflag, pagetime, reqnum, lastid, name, ResultType.ResultType_Json);
		 return response;
	}
	
	public String getFans(String requestToken, String requestTokenSecrect,int reqnum,int startindex,String name) {
		 String response = api.getFans(customKey, customSecret, requestToken, requestTokenSecrect, reqnum, startindex, name, ResultType.ResultType_Json);
		 return response;
	}
	
	public String getIdols(String requestToken, String requestTokenSecrect,int reqnum,int startindex,String name) {
		 String response = api.getIdols(customKey, customSecret, requestToken, requestTokenSecrect, reqnum, startindex, name, ResultType.ResultType_Json);
		 return response;
	}
	
	//转播 
	public String reBroad(String requestToken, String requestTokenSecrect,String content,String reid) {
		 String response = api.reBroad(customKey, customSecret, requestToken, requestTokenSecrect, content, reid, ResultType.ResultType_Json);
		 return response;
	}
	//私信，对话
	public String addPrivate(String requestToken, String requestTokenSecrect,String content,String name) {
		 String response = api.addPrivate(customKey, customSecret, requestToken, requestTokenSecrect, content,name, ResultType.ResultType_Json);
		 return response;
	}
	//评论一条微博
	public String addComment(String requestToken, String requestTokenSecrect,String content,String reid) {
		 String response = api.addComment(customKey, customSecret, requestToken, requestTokenSecrect, content,reid, ResultType.ResultType_Json);
		 return response;
	}
	//删除
	public String delete(String requestToken, String requestTokenSecrect,String id) {
		 String response = api.delete(customKey, customSecret, requestToken, requestTokenSecrect, id, ResultType.ResultType_Json);
		 return response;
	}
	//收藏
	public String addFav(String requestToken, String requestTokenSecrect,String id) {
		 String response = api.addFav(customKey, customSecret, requestToken, requestTokenSecrect, id, ResultType.ResultType_Json);
		 return response;
	}
	
	public boolean parseToken(String response,Boolean isLastToken) {
		if (response == null || response.equals("")) {
			return false;
		}
		String[] tokenArray = response.split("&");

		if (tokenArray.length < 2) {
			return false;
		}

		String strTokenKey = tokenArray[0];
		String strTokenSecrect = tokenArray[1];

		String[] token1 = strTokenKey.split("=");
		if (token1.length < 2) {
			return false;
		}
		if(isLastToken){
			this.accessTokenKey = token1[1];
		}else{
			this.tokenKey = token1[1];
		}

		String[] token2 = strTokenSecrect.split("=");
		if (token2.length < 2) {
			return false;
		}
		if(isLastToken){
			this.accessTokenSecrect = token2[1];
		}else{
			this.tokenSecrect = token2[1];
		}
		return true;
	}
}
