package com.android.weibo;
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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.*;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.android.datastore.DatabaseManager;
import com.android.datastruct.TwintterBean;
import com.sph.player.R;

import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;


//登录微博activity
public class WeiboActivity extends Activity{
	class ClientNotification extends WebViewClient{

		private static final String TAG = "weibo_activity ClientNotification";
		final WeiboActivity this$0;

		public void onLoadResource(WebView webview, String s){
			super.onLoadResource(webview, s);
		}

		public void onPageFinished(WebView webview, String s)
		{
			super.onPageFinished(webview, s);
		}

		public void onPageStarted(WebView webview, String s, Bitmap bitmap)
		{
			super.onPageStarted(webview, s, bitmap);
		}

		public void onReceivedError(WebView webview, int i, String s, String s1)
		{
			super.onReceivedError(webview, i, s, s1);
		}

		public boolean shouldOverrideUrlLoading(WebView webview, String s)
		{
			return super.shouldOverrideUrlLoading(webview, s);
		}

		private ClientNotification()
		{
			super();
			this$0 = WeiboActivity.this;
		}
	}

	//JavaScript的接口类
	class JavaScriptInterface{

		final WeiboActivity this$0;

		//获取Html函数
		public void getHTML(String s){
			WeiboActivity weiboactivity = WeiboActivity.this;
			weiboactivity.pin = getPin(s);
			
			if (pin != null && pin.length() > 0){
				GotoWeiboSendPage();
			} else{
				Context context = ct;
				String s2 = ct.getResources().getString(R.string.err_message_4);
				Toast.makeText(context, s2, 0).show();
			}
		}

		//构造函数
		JavaScriptInterface(){
			super();
			this$0 = WeiboActivity.this;
		}
	}


	private Context ct;
	String pin;
	private ImageView returnBack;
	String share_weibo_url;
	private WebView webView;

	public WeiboActivity()
	{
		pin = "";
		share_weibo_url = "";
	}

	//初始化web页面
	private void InitWebPage(){
		//webview控件
		webView = (WebView)findViewById(R.id.web_page_control);
		webView.getSettings().setCacheMode(1);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setAppCacheEnabled(true);
		
		//返回按钮图片控件
		returnBack = (ImageView)findViewById(R.id.my_edit_button);
		returnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				finish();
			}

		});
		
		JavaScriptInterface javascriptinterface = new JavaScriptInterface();
		webView.addJavascriptInterface(javascriptinterface, "Methods");
		WebViewClientListenter  mWebViewClientListenter = new WebViewClientListenter();
		webView.setWebViewClient(mWebViewClientListenter);
	}

	//打开微博函数
	private void WeiboOpen(String s){
		if (webView != null){
			webView.getSettings().setSupportZoom(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.setAlwaysDrawnWithCacheEnabled(true);
			webView.getSettings().setBlockNetworkImage(true);
			webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setScrollContainer(true);
			webView.loadUrl(s);
		}
	}

	//微博发送页面
	public void GotoWeiboSendPage(){
		RequestToken requesttoken = OAuthConstant.getInstance().getRequestToken();
		AccessToken accesstoken = null;
		try{
			accesstoken = requesttoken.getAccessToken(pin);
			OAuthConstant.getInstance().setAccessToken(accesstoken);
		}catch (WeiboException weiboexception){
			Context context = ct;
			String s4 = getResources().getString(R.string.err_message_3);
			Toast.makeText(context, s4, 0).show();
			weiboexception.printStackTrace();
			
			return;
		}catch (Exception e){
			Context context = ct;
			String s4 = getResources().getString(R.string.err_message_3);
			Toast.makeText(context, s4, 0).show();
			e.printStackTrace();
			
			return;
		}
		
		if (accesstoken != null){
			String s1 = accesstoken.getToken();
			String s2 = accesstoken.getTokenSecret();
			TwintterBean twintterbean = new TwintterBean();
			twintterbean.SetToken(s1);
			twintterbean.SetTokenSecret(s2);
			DatabaseManager.getInstance(this).addTwintterToken(twintterbean);
			
			Intent intent = new Intent();
		    intent.setClass(this, WeiboActivitySend.class);
			
		    Bundle bundle = new Bundle();
			bundle.putString("WEIBO_SHARE_URL", share_weibo_url);
			bundle.putString("WEIBO_TOKEN", s1);
			bundle.putString("WEIBO_TOKENSECRET", s2);
			
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		}
	}

	public String getPin(String s)
	{
		String s1 = "";
		Matcher matcher = Pattern.compile("[0-9]{6}").matcher(s);
		if (matcher.find())
			s1 = matcher.group(0);
		return s1;
	}

	protected void onActivityResult(int i, int j, Intent intent){
		if (i == 1 && j == 3){
			finish();
		}
	}

	//activity的创建函数
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		
		//设置当前布局
		setContentView(R.layout.activity_weibo_login);
		ct = this;
		
		Bundle bundle1 = getIntent().getExtras();
		String WEIBO = bundle1.getString("WEIBO");
		share_weibo_url = bundle1.getString("WEIBO_SHARE_URL");
		if ( WEIBO== null || share_weibo_url == null){
			Context context = ct;
			String s2 = ct.getResources().getString(R.string.err_message_4);
			Toast.makeText(context, s2, 0).show();
			finish();
		}
		//初始化web页面
		InitWebPage();
		if (WEIBO != null && WEIBO.length() > 0){
			WeiboOpen(WEIBO);
		}
	}




	private class WebViewClientListenter extends WebViewClient{

		final WeiboActivity this$0;

		//页面关闭函数
		public void onPageFinished(WebView webview, String s){
			if (s.contains("http://api.t.sina.com.cn/oauth/authoriz")){
				webview.loadUrl("javascript:window.Methods.getHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			}
			super.onPageFinished(webview, s);
		}

		//页面开始函数
		public void onPageStarted(WebView webview, String s, Bitmap bitmap){
			if (s.contains("http://api.t.sina.com.cn/oauth/authoriz")){
				webView.setVisibility(8);
			}
			super.onPageStarted(webview, s, bitmap);
		}

		WebViewClientListenter(){
			super();
			this$0 = WeiboActivity.this;
		}
	}
}
