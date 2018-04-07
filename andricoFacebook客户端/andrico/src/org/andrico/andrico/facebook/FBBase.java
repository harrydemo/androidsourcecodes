 /*************************************************************************** 
 *              Copyright (C) 2009 Andrico Team                             * 
 *              http://code.google.com/p/andrico/                           *
 *                             												*
 * Licensed under the Apache License, Version 2.0 (the "License");			*
 * you may not use this file except in compliance with the License.			*
 * 																			*	
 * You may obtain a copy of the License at 									*
 * http://www.apache.org/licenses/LICENSE-2.0								*
 *																			*
 * Unless required by applicable law or agreed to in writing, software		*
 * distributed under the License is distributed on an "AS IS" BASIS,		*
 *																			*
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	*
 * See the License for the specific language governing permissions and		*
 * limitations under the License.											*
 ****************************************************************************/

package org.andrico.andrico.facebook;

import org.andrico.andjax.http.ByteArrayBody;
import org.andrico.andjax.http.HttpClientService;
import org.andrico.andjax.http.IHttpResponseRunnable;
import org.andrico.andjax.http.ByteArrayBody.WriteToProgressHandler;

import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.TreeMap;


class FBBase {
    private static final String LOG = "FacebookBase";

    public class Session {
        private String mSecret;
        private String mSession;
        private String mUid;

        protected Session(String session, String secret, String uid) {
            if (session == null || secret == null || uid == null) {
                throw new IllegalArgumentException();
            }
            mSession = session;
            mSecret = secret;
            mUid = uid;
        }

        synchronized public String getSecret() {
            return mSecret;
        }

        synchronized public String getSession() {
            return mSession;
        }

        synchronized public String getUid() {
            return mUid;
        }
    }

    public static final Uri WWW_URI = Uri.parse("http://www.facebook.com");
    public static final Uri MOBILE_URI = Uri.parse("http://m.facebook.com");
    public static final Uri X_URI = Uri.parse("http://x.facebook.com");
    public static final Uri REST_URI = Uri.parse("http://api.facebook.com/restserver.php?");
    //public static final Uri REST_URI = Uri.parse("http://192.168.1.1:8000/test?");

    private String mApiKey;
    private String mApiSecret;
    private FBMethodFactory mFacebookMethodFactory;
    private HttpClientService mHttpClientService;
    private Session mSession;

    public FBBase(String api, String secret) {
        mApiKey = api;
        mApiSecret = secret;
        mHttpClientService = new HttpClientService(2, null);
        mFacebookMethodFactory = new FBMethodFactory("facebook.com", api, secret);
    }

    /**
     * Add a method to the execution stack, the provided callback will be run
     * after having mResult set, which should be accessed with
     * FacebookMethodCallback.getResult()
     * 
     * @param method The method to run
     * @param c The callback to be run upon competion of the method.
     */
    public void authenticate(FBMethod method, IHttpResponseRunnable c) {
        try {
            mHttpClientService.submit(method.getRequestUrl(), c);
        } catch (URISyntaxException e) {
            Log.d(LOG, "authenticate failed", e);
        }
    }

    /**
     * Generate a URL to load in the browser for a user to see.
     * 
     * @param authToken Used as the auth_token parameter in the generated url.
     * @return String
     */
    public String authorizationParameters(String authToken) {
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("api_key", mApiKey);
        parameters.put("auth_token", authToken);
        parameters.put("popup", "1");
        parameters.put("v", "1.0");

        return FBMethod.urlParameters(parameters);
    }

    /**
     * Generate a URL authorizing a requested extended preference.
     * 
     * @param Which preference to generate the request URL for.
     * @return
     */
    public Uri authorizeExtendedPreferenceUrl(String extPerm, String next, String nextCancel) {
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("api_key", mApiKey);
        parameters.put("ext_perm", extPerm);
        parameters.put("popup", "1");
        parameters.put("v", "1.0");
        if (next != null) {
            parameters.put("next", next);
        }
        if (nextCancel != null) {
            parameters.put("next_cancel", nextCancel);
        }

        Uri url = Uri.withAppendedPath(WWW_URI, "authorize.php?"
                + FBMethod.urlParameters(parameters));
        Log.e(LOG, "Generated setStatus authorization URL: " + url);
        return url;
    }

    /**
     * Create a URL that can be used in a web browser to allow a user to
     * authorize an app permanently. Success redirects the user to:
     * https://login.facebook.com/code_gen.php See:
     * http://wiki.developers.facebook
     * .com/index.php/Authentication_guide#Creating_Infinite_Sessions
     * 
     * @return
     */
    public Uri authorizeInfiniteSessionUrl() {
        // Its really hard to track the mobile url.
        // return "http://m.facebook.com/code_gen.php?v=1.0&api_key=" + mApiKey;
        return Uri.withAppendedPath(WWW_URI, "code_gen.php?v=1.0&api_key=" + mApiKey);
    }

    public Uri authorizeLoginUrl(String authToken) {
        return Uri.withAppendedPath(WWW_URI, "login.php?"
                + authorizationParameters(authToken));
    }

    /**
     * Load up the extended perms activity so that we can authorized an extended
     * permission.
     */
    public Intent createExtendedPreferenceActivityIntent(Context context, String extPerm) {
        return ExtendedPreferenceActivity.createActivityIntent(context, mApiKey, mApiSecret,
                mSession.getSession(), mSession.getSecret(), mSession.getUid(), extPerm);
    }

    /**
     * Load up the login activity so that we can get a session.
     */
    public Intent createLoginActivityIntent(Context context) {
        return LoginActivity.createActivityIntent(context, mApiKey, mApiSecret);
    }

    public HttpResponse execute(FBMethod method, WriteToProgressHandler progressHandler) {
        method.mParameters.put("call_id", Long.toString(System.nanoTime()));
        try {
            Log.d(LOG, "Executing: " + method.getRequestUrl());
            if (method.hasData()) {
                MultipartEntity multipartEntity = makeMultipartEntityFromParameters(method, progressHandler);
                return mHttpClientService.execute(method.getRequestUrl(), multipartEntity);
            } else {
                return mHttpClientService.execute(method.getRequestUrl());
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public HttpResponse execute(FBMethod method) {
        return execute(method, null);
    }

    public FBMethodFactory getMethodFactory() {
        return mFacebookMethodFactory;
    }

    public synchronized Session getSession() {
        return mSession;
    }

    /**
     * When a LoginActivity activity returns a LOGIN_REQUEST, parse its
     * response.
     * 
     * @param resultCode
     * @param data
     */
    public Boolean handleLoginActivityResult(Context context, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            setSession(data.getStringExtra(LoginActivity.SESSION_KEY_EXTRA), data
                    .getStringExtra(LoginActivity.SECRET_EXTRA), data
                    .getStringExtra(LoginActivity.UID_EXTRA));
            return true;
        } else {
            return false;
        }
    }
    
    public synchronized boolean isRunning() {
        return mHttpClientService.isRunning();
    }

    /**
     * Using the string returned from the http sesion request, cast it to a JSON
     * object and store it.
     * 
     * @param session
     */
    public synchronized void setSession(String session, String secret, String uid) {
        mSession = new Session(session, secret, uid);
        mFacebookMethodFactory.setSession(mSession);
    }
    
    public synchronized void stop() {
        mHttpClientService.stop();
    }
    
    public synchronized void start() {
        mHttpClientService.start();
    }

    /**
     * Add a method to the execution stack, the provided callback will be run
     * after having mResult set, which should be accessed with
     * FacebookMethodCallback.getResult(). This method will properly sequence
     * and add needed parameters before executing the call.
     * 
     * @param method The method to run
     * @param runnable The callback to be run upon completion of the method.
     */
    public void submit(FBMethod method, IHttpResponseRunnable runnable, WriteToProgressHandler progressHandler) {
        method.mParameters.put("call_id", Long.toString(System.nanoTime()));
        try {
            Log.d(LOG, "Executing: " + method.getRequestUrl());
            if (method.hasData()) {
                MultipartEntity multipartEntity = makeMultipartEntityFromParameters(method, progressHandler);

                mHttpClientService.submit(method.getRequestUrl(), multipartEntity, runnable);
            } else {
                mHttpClientService.submit(method.getRequestUrl(), runnable);
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void submit(FBMethod method, IHttpResponseRunnable runnable) {
        submit(method, runnable, null);
    }

    /**
     * Unset the facebook session.
     */
    public void unsetSession() {
        mSession = null;
        mFacebookMethodFactory.unsetSession();
    }
    
    private MultipartEntity makeMultipartEntityFromParameters(FBMethod method, ByteArrayBody.WriteToProgressHandler runnable)
            throws UnsupportedEncodingException {
        MultipartEntity multipartEntity = new MultipartEntity();
        for (String key : method.mParameters.keySet()) {
            multipartEntity.addPart(key, new StringBody(method.mParameters.get(key)));
        }
        multipartEntity.addPart("data", new ByteArrayBody(method.mData, method.mDataFilename, runnable));
        return multipartEntity;
    }
}
