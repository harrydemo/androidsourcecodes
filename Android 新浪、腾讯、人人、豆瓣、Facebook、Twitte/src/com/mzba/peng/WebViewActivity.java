package com.mzba.peng;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mzba.peng.util.PlatformBindConfig;
import com.mzba.peng.util.ServiceUtils;

/**
 * 
 * @author 06peng
 *
 */
public class WebViewActivity extends Activity {
	
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);    //设置Activity显示进度条
		setContentView(R.layout.oauth_webview);
		webView = (WebView)this.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				WebViewActivity.this.setProgress(newProgress * 100);
			}
		});
		
		Bundle extras = getIntent().getExtras();
		String url = "";
		if (extras != null) {
			if (extras.containsKey("url")) {
				url = extras.getString("url");
				if (ServiceUtils.isConnectInternet(this)) {
					webView.loadUrl(url);
				} else {
					Toast.makeText(this, "网络连接失败", Toast.LENGTH_LONG).show();
					return;
				}
			}
		}
		webView.addJavascriptInterface(new JavaScriptInterface(), "Methods");
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				System.out.println("url:" + url);
				/**豆瓣*/
				if (url.indexOf(PlatformBindConfig.Callback + "/?oauth_token=") != -1) {
//					String oauthToken = url.substring(url.indexOf("/?oauth_token=") + 14, url.length());
					Intent intent = new Intent();
					intent.setAction("oauth_verifier");
					Bundle extras = new Bundle();
					intent.putExtras(extras);
					sendBroadcast(intent);
					WebViewActivity.this.finish();
					return;
				} 
				
				/**腾讯*/
				if (url.contains("code=") && url.contains("&openid=") && url.contains("&openkey=")) {
					String code = url.substring(url.indexOf("code=") + 5, url.indexOf("&openid="));
					String openid = url.substring(url.indexOf("&openid=") + 8, url.indexOf("&openkey="));
					String openkey = url.substring(url.indexOf("&openkey=") + 9, url.length());
					if (!code.equals("")) {
						Intent intent = new Intent();
						intent.setAction("oauth_verifier");
						Bundle extras = new Bundle();
						extras.putString("code", code);
						extras.putString("openid", openid);
						extras.putString("openkey", openkey);
						intent.putExtras(extras);
						sendBroadcast(intent);
						WebViewActivity.this.finish();
						return;
					}
				}
				
				/**人人或新浪*/
				if (url.contains("code=")) {
					String code = url.substring(url.indexOf("code=") + 5, url.length());
					if (!code.equals("")) {
						Intent intent = new Intent();
						intent.setAction("oauth_verifier");
						Bundle extras = new Bundle();
						extras.putString("code", code);
						intent.putExtras(extras);
						sendBroadcast(intent);
						WebViewActivity.this.finish();
						return;
					}
				}
				
				/**QQ或Facebook*/
				if (url.contains("#access_token=") || url.contains("&expires_in=")) {
					String accessToken = url.substring(url.indexOf("#access_token=") + 14, url.indexOf("&expires_in="));
					String expires = url.substring(url.indexOf("&expires_in=") + 12, url.length());
					
					System.out.println(accessToken);
					System.out.println(expires);
					if (!accessToken.equals("")) {
						Intent intent = new Intent();
						intent.setAction("oauth_verifier");
						Bundle extras = new Bundle();
						extras.putString("accessToken", accessToken);
						extras.putString("expires", expires);
						intent.putExtras(extras);
						sendBroadcast(intent);
						WebViewActivity.this.finish();
						return;
					}
				}
			}
			
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {   
				handler.proceed();
			}   

			
			@Override
			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:window.Methods.getHTML('<div>'+document.getElementById('oauth_pin').innerHTML+'</div>');"); 
				super.onPageFinished(view, url);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack(); 
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class JavaScriptInterface {
		public void getHTML(String html) {
			System.out.println(html);
			String pin = getPin(html);
			// 这里就获取到了我们想要的pin码
			// 这个pin码就是oauth_verifier值,用来进一步获取Access Token和Access Secret用
			System.out.println("pin:" + pin);
			Intent intent = new Intent();
			intent.setAction("oauth_verifier");
			Bundle extras = new Bundle();
			extras.putString("pin", pin);
			intent.putExtras(extras);
			sendBroadcast(intent);
			WebViewActivity.this.finish();
		}
	}
	
	public String getPin(String html) {
		String ret = "";
		String regEx = "[0-9]{7}";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		boolean result = m.find();
		if (result) {
			ret = m.group(0);
		}
		return ret;
	}
}
