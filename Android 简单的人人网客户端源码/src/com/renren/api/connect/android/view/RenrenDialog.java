/*
 * Copyright 2010 Renren, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.renren.api.connect.android.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renren.api.connect.android.example.R;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;

/**
 * 把人人请求结果用Dialog形式显示给用户（如登录界面）。
 * 
 * @author yong.li@opi-corp.com
 * 
 */
public class RenrenDialog extends Dialog {
	private static final int RENREN_BLUE = 0xFF005EAC;

	private static final float[] DIMENSIONS_LANDSCAPE = { 460, 260 };
	private static final float[] DIMENSIONS_PORTRAIT = { 280, 420 };

	private static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);
	private static final int MARGIN = 4;
	private static final int PADDING = 2;

	private String mUrl;
	private String method = "get";
	//private byte[] params;

	private RenrenDialogListener mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private LinearLayout mContent;
	private TextView mTitle;

	public RenrenDialog(Context context, String url,
			RenrenDialogListener listener) {
		super(context);
		mUrl = url;
		mListener = listener;
		this.method = "get";
	}

	public RenrenDialog(Context context, String url,
			RenrenDialogListener listener, byte[] params) {
		super(context);
		mUrl = url;
		mListener = listener;
		this.method = "post";
		//this.params = params;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");

		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		setUpTitle();
		setUpWebView();
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		float scale = getContext().getResources().getDisplayMetrics().density;
		float[] dimensions = display.getWidth() < display.getHeight() ? DIMENSIONS_PORTRAIT
				: DIMENSIONS_LANDSCAPE;
		addContentView(mContent, new FrameLayout.LayoutParams(
				(int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
						* scale + 0.5f)));
	}

	private void setUpTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Drawable icon = getContext().getResources().getDrawable(
				R.drawable.renren_android_title_logo);
		mTitle = new TextView(getContext());
		mTitle.setText("与人人连接");
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);
		mTitle.setBackgroundColor(RENREN_BLUE);
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
		mTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		mContent.addView(mTitle);
	}

	private void setUpWebView() {
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new RenrenDialog.RenrenWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		if ("post".equalsIgnoreCase(this.method))
			//mWebView.postUrl(mUrl, params);
		    ;
		else
			mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
	}

	private class RenrenWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(Util.LOG_TAG, "Redirect URL: " + url);
			if (url.startsWith(Renren.REDIRECT_URI)) {
				Bundle values = Util.parseUrl(url);
				String error = values.getString("error_reason");
				if (error == null) {
					mListener.onComplete(values);
				} else {
					mListener.onRenrenError(new RenrenError(error));
				}
				RenrenDialog.this.dismiss();
				return true;
			} else if (url.startsWith(Renren.CANCEL_URI)) {
				Bundle values = Util.parseUrl(url);
				String error = values.getString("error_reason");
				if (error == null)
					error = values.getString("errMsg");
				if (error == null) {
					mListener.onCancel(values);
				} else {
					mListener.onRenrenError(new RenrenError(error));
				}
				RenrenDialog.this.dismiss();
				return true;
			} else if (url.contains(Renren.DISPLAY_VALUE)) {
				return false;
			}
			getContext().startActivity(
					new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			mListener.onHttpError(errorCode, description, failingUrl);
			mSpinner.dismiss();
			RenrenDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(Util.LOG_TAG, "Webview loading URL: " + url);
			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			String title = mWebView.getTitle();
			if (title != null && title.length() > 0) {
				mTitle.setText(title);
			}
			mSpinner.dismiss();
		}
	}
}
