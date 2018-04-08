package com.oauthTest.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oauthTest.R;
import com.oauthTest.data.OAuth;
import com.oauthTest.utils.ConfigUtil;

/**
 * 用户授权页面
 * 		1.初始化OAuth对象
 * 		2.获取用户授权页面并填充至webView
 * 		3.根据载入的url判断匹配规则的结果执行跳转
 * 
 * @author bywyu
 *
 */
public class AuthorizationAct extends Activity {
	
	private final String LOGTAG = "AuthorizationAct";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.authorization_ui);
	    
	    //init
	    OAuth  oAuth = OAuth.getInstance();
	    oAuth.clear();
	    
	    //获取被操作app的key、secret
	    String appKey = ConfigUtil.getInstance().getAppKey();
	    String appSecret = ConfigUtil.getInstance().getAppSecret();
	    oAuth.setKeyAndSecret(appKey, appSecret);
	    
	    String url = oAuth.getAuthorizUrl();
	    Log.d(LOGTAG, "onCreat() [Authoriz] url = "+url);
	   
	    initWebView(url);
    }
	
	private void initWebView(String url) {
		WebView authorizationView = (WebView) findViewById(R.id.authorizationView);
	    authorizationView.clearCache(true);
	    authorizationView.getSettings().setJavaScriptEnabled(true);
	    authorizationView.getSettings().setSupportZoom(true);
	    authorizationView.getSettings().setBuiltInZoomControls(true);
	    authorizationView.setWebViewClient(new WebViewC()); 
	    authorizationView.loadUrl(url);
    }

	class WebViewC extends WebViewClient{
		private int index = 0;
		@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;
        }
		
		/**
		 * 由于腾讯授权页面采用https协议
		 * 		执行此方法接受所有证书
		 */
		public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
			 handler.proceed() ;
		 }

		@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	        super.onPageStarted(view, url, favicon);
	        Log.d(LOGTAG, "onPageStarted url = "+url );
	        
	        /**
	         *  url.contains(ConfigUtil.callBackUrl)
	         *  如果授权成功url中包含之前设置的callbackurl
	         *  		包含：授权成功
	         *
	         *index == 0
	         *由于该方法onPageStarted可能被多次调用造成重复跳转
	         *		则添加此标示
	         */
	        
	        if( url.contains(ConfigUtil.callBackUrl) && index == 0){
	        	index ++;
            	Intent intent = new Intent(AuthorizationAct.this,ShowAccessTokenAct.class);
            	intent.putExtra(ConfigUtil.OAUTH_VERIFIER_URL, url);
            	AuthorizationAct.this.startActivity(intent);
            	AuthorizationAct.this.finish();
           }
        }

		@Override
        public void onPageFinished(WebView view, String url) {
	        // TODO Auto-generated method stub
	        super.onPageFinished(view, url);
        }
	}
}
