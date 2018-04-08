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

package com.renren.api.connect.android;

import android.content.Context;
import android.os.Bundle;

import com.renren.api.connect.android.exception.RenrenError;

/**
 * 将对人人的请求封装成异步
 * 
 * @see Renren
 * @see RequestListener
 * 
 * @author yong.li@opi-corp.com
 * 
 */
public class AsyncRenren {
	private Renren renren;

	public AsyncRenren(Renren renren) {
		this.renren = renren;
	}

	/**
	 * 退出登录
	 * 
	 * @param context
	 * @param listener
	 */
	public void logout(final Context context, final RequestListener listener) {
		new Thread() {
			@Override
			public void run() {
				try {
					String resp = renren.logout(context);
					RenrenError rrError = Util.parseRenrenError(resp,
							Renren.RESPONSE_FORMAT_JSON);
					if (rrError != null)
						listener.onRenrenError(rrError);
					else
						listener.onComplete(resp);
				} catch (Throwable e) {
					listener.onFault(e);
				}
			}
		}.start();
	}

	/**
	 * 调用 REST API，服务器的响应是JSON串。
	 * 
	 * REST API 详细信息见 http://wiki.dev.renren.com/wiki/API
	 * 
	 * @param parameters
	 * @param listener
	 */
	public void requestJSON(Bundle parameters, RequestListener listener) {
		request(parameters, listener, Renren.RESPONSE_FORMAT_JSON);
	}

	/**
	 * 调用 REST API 服务器的响应是XML串。
	 * 
	 * REST API 详细信息见 http://wiki.dev.renren.com/wiki/API
	 * 
	 * @param parameters
	 * @param listener
	 */
	public void requestXML(Bundle parameters, RequestListener listener) {
		request(parameters, listener, Renren.RESPONSE_FORMAT_XML);
	}

	/**
	 * 调用 REST API。
	 * 
	 * REST API 详细信息见 http://wiki.dev.renren.com/wiki/API
	 * 
	 * @param parameters
	 * @param listener
	 * @param format
	 *            return data format (json or xml)
	 */
	public void request(final Bundle parameters,
			final RequestListener listener, final String format) {
		new Thread() {
			@Override
			public void run() {
				try {
					String resp = renren.request(parameters, format);
					RenrenError rrError = Util.parseRenrenError(resp, format);
					if (rrError != null)
						listener.onRenrenError(rrError);
					else
						listener.onComplete(resp);
				} catch (Throwable e) {
					listener.onFault(e);
				}
			}
		}.start();
	}
}
