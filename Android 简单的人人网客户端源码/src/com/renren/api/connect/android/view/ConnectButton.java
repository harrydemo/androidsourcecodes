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

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.renren.api.connect.android.example.R;
import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.RequestListener;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.view.ConnectButtonListenerHelper.DefaultConnectButtonListener;

/**
 * 自定义的View组件，封装了对登录、登出等的处理。
 * 
 * @author yong.li@opi-corp.com
 * 
 */
public class ConnectButton extends ImageButton implements OnClickListener {
	private Renren renren;
	private ConnectButtonListener connectButtonListener = new DefaultConnectButtonListener();

	private int loginResourceId;
	private int logoutResourceId;

	public ConnectButton(Context context) {
		super(context);
	}

	public ConnectButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initImageResourceId(attrs);
	}

	public ConnectButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initImageResourceId(attrs);
	}

	private void initImageResourceId(AttributeSet attrs) {
		this.logoutResourceId = attrs.getAttributeResourceValue(null,
				"renren_logout_resource", R.drawable.renren_logout_button);
		this.loginResourceId = attrs.getAttributeResourceValue(null,
				"renren_login_resource", R.drawable.renren_login_button);
		setImageResource(this.loginResourceId);
	}

	/**
	 * 必须初始化后才能使用
	 * 
	 * @param renren
	 */
	public void init(final Renren renren) {
		this.renren = renren;
		this.updateButtonImage();
		setBackgroundColor(Color.TRANSPARENT);
		setAdjustViewBounds(true);
		drawableStateChanged();
		setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		if (renren.isSessionKeyValid()) {
			AsyncRenren asyncRunner = new AsyncRenren(renren);
			asyncRunner.logout(getContext(), new LogoutRequestListener());
		} else {
			renren.authorize(new LoginDialogListener());
		}
	}

	public void setConnectButtonListener(
			ConnectButtonListener connectButtonListener) {
		this.connectButtonListener = connectButtonListener;
	}

	/**
	 * 必须在UI线程中调用
	 */
	public void updateButtonImage() {
		setImageResource(renren.isSessionKeyValid() ? this.logoutResourceId
				: this.loginResourceId);
	}

	private void uiThreadUpdateButtonImage() {
		this.post(new Runnable() {

			@Override
			public void run() {
				updateButtonImage();
			}
		});
	}

	private final class LoginDialogListener implements RenrenDialogListener {

		@Override
		public void onComplete(Bundle values) {
			uiThreadUpdateButtonImage();
			connectButtonListener.onLogined(values);
		}

		@Override
		public void onRenrenError(RenrenError error) {
			connectButtonListener.onRenrenError(error);
		}

		@Override
		public void onHttpError(int errorCode, String description,
				String failingUrl) {
			String msg = "errorCode: " + errorCode + "\nerrorMsg:"
					+ description + "\nfailurl:" + failingUrl;
			Exception error = new Exception(msg);
			connectButtonListener.onException(error);
		}

		@Override
		public void onCancel(Bundle values) {
			Log.i(Util.LOG_TAG, "User cancel!");
		}
	}

	private class LogoutRequestListener implements RequestListener {
		@Override
		public void onComplete(String response) {
			uiThreadUpdateButtonImage();
			Bundle values = new Bundle();
			values.putString("success", "true");
			connectButtonListener.onLogouted(values);
		}

		@Override
		public void onFault(Throwable fault) {
			connectButtonListener.onException(new Exception(fault));
		}

		@Override
		public void onRenrenError(RenrenError e) {
			connectButtonListener.onRenrenError(e);
		}
	}
}
