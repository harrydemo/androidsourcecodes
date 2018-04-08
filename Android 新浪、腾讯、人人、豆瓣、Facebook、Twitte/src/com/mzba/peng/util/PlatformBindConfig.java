package com.mzba.peng.util;
/**
 * 第三方绑定平台相关接口
 * @author 06peng
 *
 */
public class PlatformBindConfig {
	
	public static final String Twitter = "twitter";
	public static final String Facebook = "facebook";
	public static final String Renren = "renren";
	public static final String Sina = "sina";
	public static final String Tencent = "tencent";
	public static final String Douban = "douban";
	public static final String QQ = "qq";
	
	public static final String Twitter_AppName = "";
	public static final String Twitter_ConsumerKey = "";
	public static final String Twitter_ConsumerSecret = "";
	public static final String Twitter_Request_Token = "https://api.twitter.com/oauth/request_token";
	public static final String Twitter_Authorize = "https://api.twitter.com/oauth/authorize";
	public static final String Twitter_Access_Token = "https://api.twitter.com/oauth/access_token";
	public static final String Callback = "http://06peng.com";
	
	public static final String Facebook_AppName = "";
	public static final String Facebook_AppID = "";
	public static final String Facebook_AppSecret = "";
	public static final String Fackbook_Authorize = "https://www.facebook.com/dialog/oauth?client_id="+Facebook_AppID+"" +
			"&redirect_uri=https://www.facebook.com/connect/login_success.html&response_type=token";
	
	public static final String Renren_Id = "";
	public static final String Renren_Callback = "http://graph.renren.com/oauth/login_success.html";
	public static final String Renren_AppKey = "";
	public static final String Renren_AppSecret = "";
	public static final String Renren_Authorize = "https://graph.renren.com/oauth/authorize?client_id="+Renren_Id+"" +
								"&response_type=code&redirect_uri=" + Renren_Callback;
	public static final String Renren_Token = "https://graph.renren.com/oauth/token?";		
	
	public static final String Sina_AppKey = "";
	public static final String Sina_AppSecret = "";
	public static final String Sina_Request_Token = "http://api.t.sina.com.cn/oauth/request_token";
	public static final String Sina_Authorize = "http://api.t.sina.com.cn/oauth/authorize";
	public static final String Sina_Access_Token = "http://api.t.sina.com.cn/oauth/access_token";
	public static final String Sina_Authorize2 = "https://api.weibo.com/oauth2/authorize?client_id="+Sina_AppKey+"" +
				"&response_type=code&redirect_uri=" + Callback;
	public static final String Sina_Access_Token2 = "https://api.weibo.com/oauth2/access_token?client_id="+Sina_AppKey+"&client_secret="+Sina_AppSecret+"" +
				"&grant_type=authorization_code&redirect_uri="+Callback+"&code=";
	
	
	public static final String Tencent_AppKey = "";
	public static final String Tencent_AppSecret = "";
	public static final String Tencent_Authorize_2 = "https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id="+Tencent_AppKey+"" +
								"&response_type=code&redirect_uri=" + Callback;
	public static final String Tencent_Access_Token_2 = "https://open.t.qq.com/cgi-bin/oauth2/access_token?client_id="+Tencent_AppKey+"" +
								"&client_secret="+Tencent_AppSecret+"&redirect_uri="+Callback+"&grant_type=authorization_code&code=";
	
	public static final String Douban_AppKey = "";
	public static final String Douban_AppSecret = "";
	public static final String Douban_Request_Token = "http://www.douban.com/service/auth/request_token";
	public static final String Douban_Authorize = "http://www.douban.com/service/auth/authorize";
	public static final String Douban_Access_Token = "http://www.douban.com/service/auth/access_token";
	
	
	public static final String QQ_AppKey = "";
	public static final String QQ_AppSecret = "";
	public static final String QQ_Request_Token = "http://openapi.qzone.qq.com/oauth/qzoneoauth_request_token";
//	public static final String QQ_Authorize = "http://openapi.qzone.qq.com/oauth/qzoneoauth_authorize";
//	public static final String QQ_Access_Token = "http://openapi.qzone.qq.com/oauth/qzoneoauth_access_token";
	public static final String QQ_Authorize = "https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id="+QQ_AppKey+"&display=mobile&redirect_uri="+Callback;
	
	public static int getOpenType(String platform) {
		if (platform.equals(Sina)) {
			return 1;
		} else if (platform.equals(QQ)) {
			return 0;
		} else if (platform.equals(Douban)) {
			return 4;
		} else if (platform.equals(Renren)) {
			return 3;
		} else if (platform.equals(Tencent)) {
			return 2;
		} else if (platform.equals(Twitter)) {
			return 5;
		} else {
			return 6;
		}
	}
}
