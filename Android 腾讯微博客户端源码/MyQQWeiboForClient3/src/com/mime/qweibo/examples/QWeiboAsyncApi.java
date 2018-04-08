package com.mime.qweibo.examples;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.mime.qweibo.OauthKey;
import com.mime.qweibo.QAsyncHandler;
import com.mime.qweibo.QParameter;
import com.mime.qweibo.QWeiboRequest;
import com.mime.qweibo.examples.QWeiboType.PageFlag;
import com.mime.qweibo.examples.QWeiboType.ResultType;
import com.mime.qweibo.utils.QHttpUtil;

public class QWeiboAsyncApi implements QAsyncHandler {
	
	private MyWeiboASync weibo;
	private Context context;

	public QWeiboAsyncApi(MyWeiboASync weibo,Context context) {
		this.weibo = weibo;
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public MyWeiboASync getWeibo() {
		return weibo;
	}

	public void setWeibo(MyWeiboASync weibo) {
		this.weibo = weibo;
	}

	@Override
	public void onThrowable(Throwable t, Object cookie) {
		System.err.println(cookie.toString() + ":" + t.getLocalizedMessage());

	}

	@Override
	public void onCompleted(int statusCode, String content, Object cookie) {
		System.out.println("success:" + cookie.toString());
		System.out.println("code:" + statusCode);
		System.out.println("content:" + content);
		Map<String, String> map = QHttpUtil.splitResponse(content);
		if(content.indexOf("oauth_token")!=-1){
			if(content.indexOf("oauth_callback_confirmed")!=-1){//request token
				weibo.tokenKey = map.get("oauth_token");
				weibo.tokenSecrect = map.get("oauth_token_secret");
				weibo.authorizeUrl = "http://open.t.qq.com/cgi-bin/authorize?oauth_token=" + weibo.tokenKey;
				System.out.println(weibo.authorizeUrl);
				Uri uri = Uri.parse(weibo.authorizeUrl);
		        context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
			}else{															//处理access token
				weibo.accessTokenKey = map.get("oauth_token");
				weibo.accessTokenSecrect = map.get("oauth_token_secret");
			}
		}else{
			
		}
	}

	/**
	 * Asynchronously get request token.
	 * 
	 * @param customKey
	 *            Your AppKey.
	 * @param customSecret
	 *            Your AppSecret.
	 * @return Whether request has started.
	 */
	public boolean getRequestToken(String customKey, String customSecret) {
		String url = "https://open.t.qq.com/cgi-bin/request_token";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		//The OAuth Call back URL(You should encode this url if it
		//contains some unreserved characters).
		//oauthKey.callbackUrl = "http://www.qq.com";
		//oauthKey.callbackUrl = "null";
		oauthKey.callbackUrl = "testapp://authorizeActivity";
		

		QWeiboRequest request = new QWeiboRequest();

		return request.asyncRequest(url, "GET", oauthKey, parameters, null,
				this, "getRequestToken");
	}

	/**
	 * Asynchronously get access token.
	 * 
	 * @param customKey
	 *            Your AppKey.
	 * @param customSecret
	 *            Your AppSecret
	 * @param requestToken
	 *            The request token.
	 * @param requestTokenSecret
	 *            The request token Secret
	 * @param verify
	 *            The verification code.
	 * @return Whether request has started.
	 */
	public boolean getAccessToken(String customKey, String customSecret,
			String requestToken, String requestTokenSecret, String verify) {

		String url = "https://open.t.qq.com/cgi-bin/access_token";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecret;
		oauthKey.callbackUrl = "testapp://authorizeActivity";
		oauthKey.verify = verify;

		QWeiboRequest request = new QWeiboRequest();
		return request.asyncRequest(url, "GET", oauthKey, parameters, null,
				this, "getAccessToken");
	}

	/**
	 * Asynchronously get home page messages.
	 * 
	 * @param customKey
	 *            Your AppKey
	 * @param customSecret
	 *            Your AppSecret
	 * @param requestToken
	 *            The access token
	 * @param requestTokenSecret
	 *            The access token secret
	 * @param format
	 *            Response format, xml or json
	 * @param pageFlag
	 *            Page number.
	 * @param nReqNum
	 *            Number of messages you want.
	 * @return Whether request has started.
	 */
	public boolean getHomeMsg(String customKey, String customSecret,
			String requestToken, String requestTokenSecret, ResultType format,
			PageFlag pageFlag, int nReqNum) {

		String url = "http://open.t.qq.com/api/statuses/home_timeline";
		List<QParameter> parameters = new ArrayList<QParameter>();
		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecret;

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			strFormat = "json";
		}

		parameters.add(new QParameter("format", strFormat));
		parameters.add(new QParameter("pageflag", String.valueOf(pageFlag
				.ordinal())));
		parameters.add(new QParameter("reqnum", String.valueOf(nReqNum)));

		QWeiboRequest request = new QWeiboRequest();
		return request.asyncRequest(url, "GET", oauthKey, parameters, null,
				this, "getHomeMsg");
	}

	/**
	 * Asynchronously publish a Weibo message.
	 * 
	 * @param customKey
	 *            Your AppKey
	 * @param customSecret
	 *            Your AppSecret
	 * @param requestToken
	 *            The access token
	 * @param requestTokenSecrect
	 *            The access token secret
	 * @param content
	 *            The content of your message
	 * @param pic
	 *            The files of your images.
	 * @param format
	 *            Response format, xml or json(Default).
	 * @return Whether request has started.
	 */
	public boolean publishMsg(String customKey, String customSecret,
			String requestToken, String requestTokenSecrect, String content,
			String pic, ResultType format) {

		List<QParameter> files = new ArrayList<QParameter>();
		String url = null;
		String httpMethod = "POST";

		if (pic == null || pic.trim().equals("")) {
			url = "http://open.t.qq.com/api/t/add";
		} else {
			url = "http://open.t.qq.com/api/t/add_pic";
			files.add(new QParameter("pic", pic));
		}

		OauthKey oauthKey = new OauthKey();
		oauthKey.customKey = customKey;
		oauthKey.customSecrect = customSecret;
		oauthKey.tokenKey = requestToken;
		oauthKey.tokenSecrect = requestTokenSecrect;

		List<QParameter> parameters = new ArrayList<QParameter>();

		String strFormat = null;
		if (format == ResultType.ResultType_Xml) {
			strFormat = "xml";
		} else if (format == ResultType.ResultType_Json) {
			strFormat = "json";
		} else {
			strFormat = "json";
		}

		parameters.add(new QParameter("format", strFormat));
		try {
			parameters.add(new QParameter("content", new String(content
					.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return false;
		}
		parameters.add(new QParameter("clientip", "10.0.2.2"));

		QWeiboRequest request = new QWeiboRequest();
		return request.asyncRequest(url, httpMethod, oauthKey, parameters,
				files, this, "publishMsg");
	}

}
