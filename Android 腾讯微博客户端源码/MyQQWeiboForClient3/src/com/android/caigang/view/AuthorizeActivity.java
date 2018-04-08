package com.android.caigang.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.caigang.R;
import com.mime.qweibo.OAuth;
import com.mime.qweibo.examples.MyWeiboSync;

public class AuthorizeActivity extends Activity {
	private static final String TAG = "AuthorizeActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		WebView webView = (WebView) findViewById(R.id.web);
		webView.setWebViewClient(new MyWebViewClient());
		Intent intent = this.getIntent();
		if (!intent.equals(null)) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (bundle.containsKey("url")) {
					String url = bundle.getString("url");
					WebSettings webSettings = webView.getSettings();
					webSettings.setJavaScriptEnabled(true);
					webSettings.setSupportZoom(true);
					webView.requestFocus();
					webView.loadUrl(url);
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			WebView webView = (WebView) findViewById(R.id.web);
			if (webView.canGoBack()) {
				webView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Pattern p = Pattern.compile("^" + MyWeiboSync.CALLBACK_URL
					+ ".*oauth_verifier=(\\d+)");
			Matcher m = p.matcher(url);
			if (m.find()) {
				Intent intent = new Intent();
				intent.setAction("com.weibo.caigang.getverifier");
				String veryfier = m.group(1);
				intent.putExtra("veryfier", veryfier);
				sendBroadcast(intent);
				finish();
			}
		}
	}
}
