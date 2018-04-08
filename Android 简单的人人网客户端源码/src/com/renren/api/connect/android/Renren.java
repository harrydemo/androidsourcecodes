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

import java.util.Set;
import java.util.TreeSet;

import org.json.JSONObject;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.renren.api.connect.android.bean.FeedParam;
import com.renren.api.connect.android.view.RenrenDialog;
import com.renren.api.connect.android.view.RenrenDialogListener;
import com.renren.api.connect.android.view.RenrenDialogListenerHelper;

/**
 * 封装对人人的请求，如：显示登录界面、退出登录、请求REST APIs等。
 * 
 * @author yong.li@opi-corp.com
 */
public class Renren {

    public static final String REDIRECT_URI = "rrconnect://success";

    public static final String CANCEL_URI = "rrconnect://cancel";

    public static final String DISPLAY_VALUE = "android";

    static final String RRCONNECT_NUMBER = "2";

    static final String RESPONSE_FORMAT_JSON = "json";

    static final String RESPONSE_FORMAT_XML = "xml";

    static final String TOKEN = "auth_token";

    static final String SESSION_KEY = "session_key";

    static final String AUTO_LOGIN = "autoLogin";

    static final String RENREN_CONFIG = "renren_connect_config";

    static final String RENREN_SESSION_KEY = "renren_connect_session_key";

    static final String RENREN_SESSION_KEY_CREATE_TIME = "renren_connect_session_key_create_time";

    private String authToken = null;

    private String sessionKey = null;

    private String apiKey;

    private String apiSecret;

    private Context context;

    public static final String LOGIN_URL = "http://login.api.renren.com/connect/touch_login.do";

    static final String POST_FEED_URL = "http://www.connect.renren.com/feed/iphone/feedPrompt";

    static final String RESTSERVER_URL = "http://api.renren.com/restserver.do";

    private static final long ONE_HOUR = 1000 * 60 * 60;

    public Renren(Context context, String apiKey, String apiSecret) {
        if (context == null || apiKey == null || apiSecret == null) throw new RuntimeException(
                "context, apiKey,apiSecret必须提供");
        this.context = context;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;

        this.restoreSessionKey();
        if (this.sessionKey != null) Log.i(Util.LOG_TAG, "restore success sessionKey:"
                + this.sessionKey);
    }

    /**
     * 获取登录authToken，并获取sessionkey。如果用户已经登录监听器将不会被调用。
     * 
     * @param context
     * @param listener
     */
    public void authorize(final RenrenDialogListener listener) {
        if (this.sessionKey != null) return;
        // 调用CookieManager.getInstance之前
        // 必须先调用CookieSyncManager.createInstance
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().setCookie("http://login.api.renren.com/connect/",
                "mobile-connect=beta");

        Bundle params = new Bundle();
        params.putString("api_key", apiKey);
        params.putString("rrconnect", RRCONNECT_NUMBER);
        params.putString("display", DISPLAY_VALUE);
        params.putString("next", REDIRECT_URI);

        String url = LOGIN_URL + "?" + Util.encodeUrl(params);
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Util.showAlert(context, "没有权限", "应用需要访问互联网的权限");
        } else {
            RenrenDialogListenerHelper helper = new RenrenDialogListenerHelper();
            helper.addDialogListener(new RenrenDialogListenerHelper.DefaultRenrenDialogListener() {// 耗时的流氓监听器

                        @Override
                        public void onComplete(Bundle values) {
                            CookieSyncManager.getInstance().sync();
                            authToken = values.getString(TOKEN);
                            if (authToken != null) {
                                Log.d(Util.LOG_TAG, "Success obtain auth_token=" + authToken);
                                boolean autoLogin = values.getBoolean(AUTO_LOGIN, false);
                                if (!autoLogin) {
                                    if (values.getString("url").indexOf("autoLogintrue") > -1) autoLogin = true;
                                }
                                initSessionKey(autoLogin);
                            }
                        }
                    });
            helper.addDialogListener(listener);
            new RenrenDialog(context, url, helper).show();
        }
    }

    private void initSessionKey(boolean autoLogin) {
        Bundle params = new Bundle();
        params.putString("method", "auth.getSession");
        params.putString("auth_token", authToken);
        String sk = this.request(params, RESPONSE_FORMAT_JSON);
        try {
            JSONObject obj = new JSONObject(sk);
            this.sessionKey = obj.getString("session_key");
            Log.i(Util.LOG_TAG, "---login success sessionKey:" + this.sessionKey);
            if (autoLogin) this.storeSessionKey(this.sessionKey);
            else this.storeSessionKey(null);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void storeSessionKey(String sessionKey) {
        Editor editor = context.getSharedPreferences(RENREN_CONFIG, Context.MODE_PRIVATE).edit();
        if (sessionKey != null) {
            editor.putString(RENREN_SESSION_KEY, sessionKey);
            editor.putLong(RENREN_SESSION_KEY_CREATE_TIME, System.currentTimeMillis());
        } else {
            this.clearPersistSession();
        }
        editor.commit();
    }

    private void restoreSessionKey() {
        SharedPreferences sp = context.getSharedPreferences(RENREN_CONFIG, Context.MODE_PRIVATE);
        String sk = sp.getString(RENREN_SESSION_KEY, null);
        if (sk == null) return;
        long createTime = sp.getLong(RENREN_SESSION_KEY_CREATE_TIME, 0);
        long life = Long.parseLong(sk.split("\\.")[2]) * 1000;
        long currenct = System.currentTimeMillis();
        if ((createTime + life) < (currenct - ONE_HOUR)) this.clearPersistSession();
        else this.sessionKey = sk;
    }

    private void clearPersistSession() {
        Editor editor = context.getSharedPreferences(RENREN_CONFIG, Context.MODE_PRIVATE).edit();
        editor.remove(RENREN_SESSION_KEY);
        editor.remove(RENREN_SESSION_KEY_CREATE_TIME);
        editor.commit();
    }

    /**
     * 退出登录
     * 
     * @param context
     * @return
     */
    public String logout(Context context) {
        Util.clearCookies(context);
        this.clearPersistSession();
        authToken = null;
        sessionKey = null;
        return "true";
    }

    /**
     * 发新鲜事
     * 
     * @param feedParam
     * @param listener
     */
    public void feed(FeedParam feedParam, final RenrenDialogListener listener) {
        Bundle params = new Bundle();
        params.putString("api_key", apiKey);
        params.putString("session_key", sessionKey);
        params.putString("preview", "true");
        params.putString("callback", REDIRECT_URI);
        params.putString("cancel_url", CANCEL_URI);
        params.putString("feed_target_type", "self_feed");
        params.putString("in_canvas", "0");
        params.putString("size", "2");
        params.putString("display", DISPLAY_VALUE);

        params.putString("feed_info", feedParam.getFeedInfo());
        if (feedParam.getUserMessage() != null) params.putString("user_message", feedParam
                .getUserMessage());
        if (feedParam.getUserMessagePrompt() != null) params.putString("user_message_prompt",
                feedParam.getUserMessagePrompt());

        String url = POST_FEED_URL + "?" + Util.encodeUrl(params);
        //byte[] formParams = Util.encodeUrl(params).getBytes();
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Util.showAlert(context, "没有权限", "应用需要访问互联网的权限");
        } else {
            //new RenrenDialog(context, url, listener, formParams).show();
            new RenrenDialog(context, url, listener).show();
        }
    }

    /**
     * 调用 REST APIs
     * 
     * @param parameters
     * @param format json or xml
     * @return
     */
    public String request(Bundle parameters, String format) {
        parameters.putString("format", format);
        if (isSessionKeyValid()) {
            parameters.putString(SESSION_KEY, sessionKey);
        }
        this.prepareParams(parameters);
        return Util.openUrl(RESTSERVER_URL, "POST", parameters);
    }

    private void prepareParams(Bundle params) {
        params.putString("api_key", apiKey);
        params.putString("v", "1.0");
        params.putString("call_id", String.valueOf(System.currentTimeMillis()));

        StringBuffer sb = new StringBuffer();
        Set<String> keys = new TreeSet<String>(params.keySet());
        for (String key : keys) {
            sb.append(key);
            sb.append("=");
            sb.append(params.getString(key));
        }
        sb.append(apiSecret);
        params.putString("sig", Util.md5(sb.toString()));
    }

    /**
     * 判断sessionKey是否有效。
     * 
     * @return boolean
     */
    public boolean isSessionKeyValid() {
        return (sessionKey != null);
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
