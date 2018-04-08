package com.mzba.oauth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import oauth.commons.http.CommonsHttpOAuthConsumer;
import oauth.commons.http.CommonsHttpOAuthProvider;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.signature.SSLSocketFactoryEx;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mzba.peng.MainActivity;
import com.mzba.peng.WebViewActivity;
import com.mzba.peng.util.PlatformAccount;
import com.mzba.peng.util.PlatformBindConfig;
import com.mzba.peng.util.ServiceUtils;

/**
 * 
 * @author 06peng
 * 
 */
public class OAuth {

	public static final String TAG = OAuth.class.getCanonicalName();
	public static final String SIGNATURE_METHOD = "HMAC-SHA1";
	public static OAuthConsumer consumer;
	public static OAuthProvider provider;
	public String consumerKey;
	public String consumerSecret;
	public String oauthTokenSecretForDouban;
	public String verifier;
	private String platform;
	public static VerfierRecivier reciver;
	private Activity activity;
	private Handler handler;
	
	private Twitter twitter;
	private RequestToken requestToken;
	private AccessToken accessTokenTwitter;
	private String authUrl;
	
	public OAuth(Activity activity, Handler handler, String platform) {
		this.activity = activity;
		this.handler = handler;
		this.platform = platform;
	}

	public OAuth(Activity activity, Handler handler, String platform,
			String consumerKey, String consumerSecret) {
		this.activity = activity;
		this.handler = handler;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.platform = platform;
	}

	public String requestAccessToken(String callbackurl,
			String requestToken, String accessToken, String authorization) {
		if (platform.equals(PlatformBindConfig.Douban)) {
			consumer = new DoubanOAuthConsumer(consumerKey, consumerSecret);
			provider = new DoubanOAuthProvider(requestToken, accessToken, authorization);
		} else {
			consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
			provider = new CommonsHttpOAuthProvider(requestToken, accessToken, authorization);
		}
		try {
			if (platform.equals(PlatformBindConfig.Tencent)) {
				authUrl = provider.retrieveRequestTokenForTencent(consumer, callbackurl);
			} else if (platform.equals(PlatformBindConfig.Douban)){
				authUrl = provider.retrieveRequestToken(consumer, PlatformBindConfig.Callback);
			} else {
				authUrl = provider.retrieveRequestToken(consumer, callbackurl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Intent intent = new Intent();
				Bundle extras = new Bundle();
				extras.putString("url", authUrl);
				extras.putString("platform", platform);
				intent.setClass(activity.getApplicationContext(), WebViewActivity.class);
				intent.putExtras(extras);
				activity.startActivity(intent);
				IntentFilter filter = new IntentFilter();
				filter.addAction("oauth_verifier");
				reciver = new VerfierRecivier();
				activity.registerReceiver(reciver, filter);
			}
		});
		return authUrl;
	}
	
	public void requestAcccessTokenForTwitter() {
		try {
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(consumerKey, consumerSecret);
			requestToken = twitter.getOAuthRequestToken();
			authUrl = requestToken.getAuthorizationURL();
			
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Intent intent = new Intent();
					Bundle extras = new Bundle();
					extras.putString("url", authUrl);
					extras.putString("platform", platform);
					intent.setClass(activity.getApplicationContext(), WebViewActivity.class);
					intent.putExtras(extras);
					activity.startActivity(intent);
					IntentFilter filter = new IntentFilter();
					filter.addAction("oauth_verifier");
					reciver = new VerfierRecivier();
					activity.registerReceiver(reciver, filter);
				}
			});
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	public void requestAccessTokenForOAuth2(final String authUrl) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Intent intent = new Intent();
				Bundle extras = new Bundle();
				extras.putString("url", authUrl);
				extras.putString("platform", platform);
				intent.setClass(activity.getApplicationContext(), WebViewActivity.class);
				intent.putExtras(extras);
				activity.startActivity(intent);
				IntentFilter filter = new IntentFilter();
				filter.addAction("oauth_verifier");
				reciver = new VerfierRecivier();
				activity.registerReceiver(reciver, filter);
			}
		});
	}

	public void requestToken(String requestTokenUrl, String authorization) {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("oauth_consumer_key", consumerKey));
		params.add(new Parameter("oauth_signature_method", SIGNATURE_METHOD));
		params.add(new Parameter("oauth_timestamp", SignatureUtil.generateTimeStamp()));
		params.add(new Parameter("oauth_nonce", SignatureUtil.generateNonce(false)));
		String signature;
		try {
			signature = SignatureUtil.generateSignature("GET", requestTokenUrl, params, consumerSecret, null);
			params.add(new Parameter("oauth_signature", signature));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		StringBuilder urlBuilder = new StringBuilder();
		for (Parameter param : params) {
			urlBuilder.append(param.getName());
			urlBuilder.append("=");
			urlBuilder.append(param.getValue());
			urlBuilder.append("&");
		}
		// 删除最后“&”字符
		urlBuilder.deleteCharAt(urlBuilder.length() - 1);
		try {
			String url = httpGet(requestTokenUrl, urlBuilder.toString());
			oauthTokenSecretForDouban = url.substring(19, url.indexOf("&oauth_token"));
			String oauthToken = url.substring(url.indexOf("&oauth_token") + 13, url.length());
			String oauthUrl = authorization + "?oauth_token=" + oauthToken + "&oauth_callback=" + PlatformBindConfig.Callback;
			IntentFilter filter = new IntentFilter();
			filter.addAction("oauth_verifier");
			reciver = new VerfierRecivier();
			activity.registerReceiver(reciver, filter);

			Intent intent = new Intent();
			Bundle extras = new Bundle();
			extras.putString("url", oauthUrl);
			intent.setClass(activity.getApplicationContext(), WebViewActivity.class);
			intent.putExtras(extras);
			activity.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取拼接的参数
	 * 
	 * @param httpMethod
	 * @param url
	 * @param token
	 * @param tokenSecret
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getPostParameter(String httpMethod, String url, String token,
			String tokenSecret, List<Parameter> params) throws Exception {

		String urlParams = null;
		// 添加参数
		params.add(new Parameter("oauth_consumer_key", consumerKey));
		params.add(new Parameter("oauth_signature_method", SIGNATURE_METHOD));
		params.add(new Parameter("oauth_timestamp", SignatureUtil.generateTimeStamp()));
		params.add(new Parameter("oauth_nonce", SignatureUtil.generateNonce(false)));
		params.add(new Parameter("oauth_version", "1.0"));
		if (token != null) {
			params.add(new Parameter("oauth_token", token));
		}
		String signature = SignatureUtil.generateSignature(httpMethod, url, params, consumerSecret, tokenSecret);
		params.add(new Parameter("oauth_signature", signature));
		// 构造请求参数字符串
		StringBuilder urlBuilder = new StringBuilder();
		for (Parameter param : params) {
			urlBuilder.append(param.getName());
			urlBuilder.append("=");
			urlBuilder.append(param.getValue());
			urlBuilder.append("&");
		}
		// 删除最后“&”字符
		urlBuilder.deleteCharAt(urlBuilder.length() - 1);
		urlParams = urlBuilder.toString();
		return urlParams;
	}

	public HttpURLConnection signRequest(String token, String tokenSecret, HttpURLConnection conn) {
		consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret);
		consumer.setTokenWithSecret(token, tokenSecret);
		try {
			consumer.sign(conn);
		} catch (OAuthMessageSignerException e1) {
			e1.printStackTrace();
		} catch (OAuthExpectationFailedException e1) {
			e1.printStackTrace();
		} catch (OAuthCommunicationException e1) {
			e1.printStackTrace();
		}
		try {
			conn.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public HttpResponse signRequest(String token, String tokenSecret,
			String url, ArrayList<BasicNameValuePair> params) {
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 关闭Expect:100-Continue握手
		// 100-Continue握手需谨慎使用，因为遇到不支持HTTP/1.1协议的服务器或者代理时会引起问题
		post.getParams().setBooleanParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		return signRequest(token, tokenSecret, post);
	}

	public HttpResponse signRequest(String token, String tokenSecret, HttpPost post) {
		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		consumer.setTokenWithSecret(token, tokenSecret);
		HttpResponse response = null;
		try {
			try {
				consumer.sign(post);
			} catch (OAuthMessageSignerException e) {
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				e.printStackTrace();
			}
			response = (HttpResponse) new DefaultHttpClient().execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * 通过GET方式发送请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String httpGet(String urlPath, String params) throws Exception {
		String result = null; // 返回信息
		if (null != params && !params.equals("")) {
			urlPath += "?" + params;
		}
		Log.i(TAG, "url:" + urlPath);
		// 构造HttpClient的实例
		URL url = new URL(urlPath);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setRequestMethod("GET");
		request.connect();
		System.out.println("response code:" + request.getResponseCode());
		if (200 == request.getResponseCode()) {
			InputStream inputStream = request.getInputStream();
			result = new String(ServiceUtils.readStream(inputStream));
		}
		return result;
	}

	public String getPostParameterForDouban(String httpMethod, String url,
			String token, String oauthTokenSecret, List<Parameter> params)
			throws Exception {
		String urlParams = null;
		// 添加参数
		params.add(new Parameter("oauth_consumer_key", consumerKey));
		params.add(new Parameter("oauth_signature_method", SIGNATURE_METHOD));
		params.add(new Parameter("oauth_timestamp", SignatureUtil.generateTimeStamp()));
		params.add(new Parameter("oauth_nonce", SignatureUtil.generateNonce(false)));
		if (token != null) {
			params.add(new Parameter("oauth_token", token));
		}
		String signature = SignatureUtil.generateSignature(httpMethod, url,
				params, consumerSecret, oauthTokenSecret);
		params.add(new Parameter("oauth_signature", signature));
		// 构造请求参数字符串
		StringBuilder urlBuilder = new StringBuilder();
		for (Parameter param : params) {
			urlBuilder.append(param.getName());
			urlBuilder.append("=");
			urlBuilder.append(param.getValue());
			urlBuilder.append("&");
		}
		// 删除最后“&”字符
		urlBuilder.deleteCharAt(urlBuilder.length() - 1);
		urlParams = urlBuilder.toString();
		return urlParams;
	}

	String accessToken = null;
	String tokenSecret = null;
	
	public class VerfierRecivier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final Bundle bundle = intent.getExtras();
			if (bundle != null) {
				((MainActivity) activity).mSpinner.show();
				new Thread() {
					public void run() {
						if (platform.equals(PlatformBindConfig.Renren)) {
							String code = bundle.getString("code");
							bindRenren(code);
						} else if (platform.equals(PlatformBindConfig.Tencent)) {
							String code = bundle.getString("code");
							String openid = bundle.getString("openid");
							String openkey = bundle.getString("openkey");
							bindTencent2(code, openid, openkey);
						} else if (platform.equals(PlatformBindConfig.Sina)) {
							String code = bundle.getString("code");
							bindSina(code);
						} else if (platform.equals(PlatformBindConfig.QQ)) {
							String expires = bundle.getString("expires");
							String access_token = bundle.getString("accessToken");
							bindQzone(access_token, expires);
						} else if (platform.equals(PlatformBindConfig.Douban)) {
							provider.setOAuth10a(true);
							try {
								provider.retrieveAccessToken(consumer, null);
							} catch (OAuthMessageSignerException e) {
								e.printStackTrace();
							} catch (OAuthNotAuthorizedException e) {
								e.printStackTrace();
							} catch (OAuthExpectationFailedException e) {
								e.printStackTrace();
							} catch (OAuthCommunicationException e) {
								e.printStackTrace();
							}
							accessToken = consumer.getToken();
							tokenSecret = consumer.getTokenSecret();
							bindDouban(accessToken, tokenSecret);
						} else if (platform.equals(PlatformBindConfig.Twitter)) {
							String pin = bundle.getString("pin");
							try {
								if (pin.length() > 0) {
									accessTokenTwitter = twitter.getOAuthAccessToken(requestToken, pin);
								} else {
									accessTokenTwitter = twitter.getOAuthAccessToken();
								}
								accessToken = accessTokenTwitter.getToken();
								tokenSecret = accessTokenTwitter.getTokenSecret();
								bindTwitter(accessToken, tokenSecret, accessTokenTwitter.getScreenName());
							} catch (TwitterException te) {
								if (401 == te.getStatusCode()) {
									System.out.println("Unable to get the access token.");
								} else {
									te.printStackTrace();
								}
							}
							
						} else if (platform.equals(PlatformBindConfig.Facebook)) {
							String expires = bundle.getString("expires");
							String access_token = bundle.getString("accessToken");
							bindFacebook(access_token, expires);
						}
					};
				}.start();
			}
		}
	}
	
	
	private void bindSina(String code) {
		PlatformAccount account = new PlatformAccount();
		String access_url = "https://api.weibo.com/oauth2/access_token";
		HttpClient request = getNewHttpClient();
		HttpPost httpPost;
		try {
			httpPost = new HttpPost(new URI(access_url));
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("client_id", PlatformBindConfig.Sina_AppKey));
			params.add(new BasicNameValuePair("client_secret", PlatformBindConfig.Sina_AppSecret));
			params.add(new BasicNameValuePair("grant_type", "authorization_code"));
			params.add(new BasicNameValuePair("redirect_uri", PlatformBindConfig.Callback));
			params.add(new BasicNameValuePair("code", code));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			
			HttpResponse resposne = request.execute(httpPost);
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ServiceUtils.readStream(inputStream));
				JSONObject obj = new JSONObject(result);
				String accessToken = obj.getString("access_token");
				String expires_in = obj.getString("expires_in");
				String uid = obj.getString("uid");
				String url = "https://api.weibo.com/2/users/show.json?access_token="+accessToken + "&uid="+uid;
				HttpGet httpGet = new HttpGet(new URI(url));
				resposne = request.execute(httpGet);
				if (200 == resposne.getStatusLine().getStatusCode()) {
					inputStream = resposne.getEntity().getContent();
					result = new String(ServiceUtils.readStream(inputStream));
					JSONObject object = new JSONObject(result);
					account.setAccessToken(accessToken);
					account.setNickName(object.getString("screen_name"));
					account.setOpenAvatar(object.getString("profile_image_url"));
					String sexStr = object.getString("gender");
					int sex = 0;
					if (sexStr != null) {
						if (sexStr.equals("m")) {
							sex = 1;
						} else if (sexStr.equals("f")) {
							sex = 2;
						} else {
							sex = 0;
						}
					}
					account.setOpenSex(sex);
					account.setOpenType(1);
					account.setOpenUid(object.getString("id"));
					account.setOpenExpire(expires_in);
					Message msg = Message.obtain(handler, MainActivity.oauth_sucess, account);
					handler.sendMessage(msg);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bindTencent2(String code, String openid, String openkey) {
		PlatformAccount account = new PlatformAccount();
		String urlPath = PlatformBindConfig.Tencent_Access_Token_2 + code;
		try {
			System.out.println("urlpath:" + urlPath);
			HttpClient request = getNewHttpClient();
			HttpGet httpGet = new HttpGet(new URI(urlPath));
			HttpResponse resposne = request.execute(httpGet);
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ServiceUtils.readStream(inputStream));
				System.out.println("------------" + result);
				String access_token = result.substring(13, result.indexOf("&expires_in="));
				String expires_in = result.substring(result.indexOf("&expires_in=") + 12, result.indexOf("&refresh_token="));
				account.setAccessToken(access_token);
				account.setOpenExpire(expires_in);
				account.setOpenType(2);
				String url = "https://open.t.qq.com/api/user/info?oauth_consumer_key="+PlatformBindConfig.Tencent_AppKey+"" +
						"&access_token="+access_token+"&openid="+openid+"&clientip=10.0.0.2&oauth_version=2.a&scope=all";
				httpGet = new HttpGet(new URI(url));
				resposne = request.execute(httpGet);
				System.out.println("------------code-------" + resposne.getStatusLine().getStatusCode());
				if (200 == resposne.getStatusLine().getStatusCode()) {
					inputStream = resposne.getEntity().getContent();
					result = new String(ServiceUtils.readStream(inputStream));
					JSONObject object = new JSONObject(result);
					JSONObject resultObj = object.getJSONObject("data");
					account.setNickName(resultObj.getString("nick"));
					account.setOpenAvatar(resultObj.getString("head"));
					account.setOpenSex(resultObj.getInt("sex"));
					account.setOpenUid(resultObj.getString("name"));
					Message msg = Message.obtain(handler, MainActivity.oauth_sucess, account);
					handler.sendMessage(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bindRenren(String code) {
		PlatformAccount account = new PlatformAccount();
		try {
			String urlPath = PlatformBindConfig.Renren_Token;
			URL url = new URL(urlPath);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setDoOutput(true);
			OutputStream out = request.getOutputStream();
			String grant_type = "grant_type=authorization_code";
			String client_id = "&client_id=" + PlatformBindConfig.Renren_AppKey;
			String client_secret = "&client_secret=" + PlatformBindConfig.Renren_AppSecret;
			String redirect_uri = "&redirect_uri=" + PlatformBindConfig.Renren_Callback;
			code = "&code=" + code;
			out.write((grant_type + code + client_id + client_secret + redirect_uri).getBytes());
			out.flush();
			request.connect();
			if (200 == request.getResponseCode()) {
				InputStream inputStream = request.getInputStream();
				String result = new String(ServiceUtils.readStream(inputStream));
				System.out.println("---------renren--------" + result);
  				JSONObject object = new JSONObject(result);
				String openExpire = object.getString("expires_in");
				account.setOpenExpire(openExpire);
				String refresh_token = object.getString("refresh_token");
				account.setTokenSecret(refresh_token);
				String access_token = object.getString("access_token");
				account.setAccessToken(access_token);
				JSONObject userObject = object.getJSONObject("user");
				account.setOpenUid(userObject.getString("id"));
				account.setNickName(userObject.getString("name"));
				account.setOpenType(3);
				if (userObject.has("avatar")) {
					JSONArray avatarArray = userObject.getJSONArray("avatar");
					for (int i = 0;i < avatarArray.length();i++) {
						JSONObject avatarObject = avatarArray.getJSONObject(i);
						if (avatarObject.getString("type").equals("avatar")) {
							account.setOpenAvatar(avatarObject.getString("url"));
							break;
						}
					}
				}
				Message msg = Message.obtain(handler, MainActivity.oauth_sucess, account);
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bindQzone(String accessToken, String expires) {
		PlatformAccount account = new PlatformAccount();
		String urlPath = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;
		try {
			HttpClient request = getNewHttpClient();
			HttpGet httpGet = new HttpGet(new URI(urlPath));
			HttpResponse resposne = request.execute(httpGet);
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ServiceUtils.readStream(inputStream));
				result = result.substring(result.indexOf("(") + 1, result.indexOf(")"));
				JSONObject object = new JSONObject(result);
				// String client_id = object.getString("client_id");
				String openid = object.getString("openid");
				account.setOpenUid(openid);
				account.setAccessToken(accessToken);
				account.setOpenType(0);
				String userUrl = "https://graph.qq.com/user/get_user_info?access_token="
						+ accessToken
						+ "&oauth_consumer_key="
						+ PlatformBindConfig.QQ_AppKey + "&openid=" + openid;
				httpGet = new HttpGet(new URI(userUrl));
				resposne = request.execute(httpGet);
				if (200 == resposne.getStatusLine().getStatusCode()) {
					inputStream = resposne.getEntity().getContent();
					result = new String(ServiceUtils.readStream(inputStream));
					object = new JSONObject(result);
					account.setNickName(object.getString("nickname"));
					account.setOpenAvatar(object.getString("figureurl"));
				}
				Message msg = Message.obtain(handler, MainActivity.oauth_sucess, account);
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bindDouban(String accessToken, String tokenSecret) {
		PlatformAccount account = null;
		String urlPath = "http://api.douban.com/people/%40me";
		consumer = new DoubanOAuthConsumer(consumerKey, consumerSecret);
		consumer.setTokenWithSecret(accessToken, tokenSecret);
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			consumer.sign(conn);
			account = parseXml(conn.getInputStream());
			account.setOpenType(4);
			account.setAccessToken(accessToken);
			account.setTokenSecret(tokenSecret);
			Message msg = Message.obtain(handler, MainActivity.oauth_sucess, account);
			handler.sendMessage(msg);
		} catch (OAuthMessageSignerException e1) {
			e1.printStackTrace();
		} catch (OAuthExpectationFailedException e1) {
			e1.printStackTrace();
		} catch (OAuthCommunicationException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void bindTwitter(String accessToken, String tokenSecret, String screenName) {
		PlatformAccount account = null;
		try {
			account = new PlatformAccount();
			User user = twitter.showUser(screenName);
			account.setAccessToken(accessToken);
			account.setTokenSecret(tokenSecret);
			account.setNickName(user.getScreenName());
			if (user.getProfileImageURL() != null) {
				account.setOpenAvatar(user.getProfileImageURL().toString().replace("_normal", "")); 
			}
			account.setOpenUid(String.valueOf(user.getId()));
			account.setOpenType(5);
			Message msg = Message.obtain(handler, MainActivity.oauth_sucess, account);
			handler.sendMessage(msg);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	private PlatformAccount bindFacebook(String accessToken, String expires) {
		PlatformAccount account = new PlatformAccount();
		String urlPath = "https://graph.facebook.com/me?fields=picture,id,name,username,gender&access_token=" + accessToken;
		try {
			HttpClient request = getNewHttpClient();
			HttpGet httpGet = new HttpGet(new URI(urlPath));
			HttpResponse resposne = request.execute(httpGet);
			System.out.println(resposne.getStatusLine().getStatusCode());
			if (200 == resposne.getStatusLine().getStatusCode()) {
				InputStream inputStream = resposne.getEntity().getContent();
				String result = new String(ServiceUtils.readStream(inputStream));
				System.out.println(result);
				JSONObject resultObj = new JSONObject(result);
				account.setAccessToken(accessToken);
				account.setNickName(resultObj.getString("username"));
				account.setOpenAvatar(resultObj.getString("picture"));
				account.setOpenExpire(expires);
				account.setOpenType(6);
				account.setOpenUid(resultObj.getString("id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	public HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * 解析xml
	 * 
	 * @param is
	 */
	private PlatformAccount parseXml(InputStream is) {
		PlatformAccount account = null;
		try {
			XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = xmlPullParserFactory.newPullParser();
			// 设置输入流以及编码方式
			parser.setInput(is, "UTF-8");
			// 获取当前的事件类型
			int eventType = parser.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				String nodeName = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:
					if (nodeName.equalsIgnoreCase("entry")) {
						account = new PlatformAccount();
					}
					if (nodeName.equalsIgnoreCase("title")) {
						account.setNickName(parser.nextText());
					}
					if (nodeName.equalsIgnoreCase("db:uid")) {
						account.setOpenUid(parser.nextText());
					}
					if (nodeName.equalsIgnoreCase("link")) {
						if (parser.getAttributeValue(1).equalsIgnoreCase("icon")) {
							account.setOpenAvatar(parser.getAttributeValue(0));	
						}
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
				// 手动的触发下一个事件
				eventType = parser.next();
				Log.i("OAuth", eventType + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}
}
