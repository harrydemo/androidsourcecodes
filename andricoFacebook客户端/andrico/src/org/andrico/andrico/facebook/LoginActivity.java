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


import org.andrico.andjax.http.HttpResponseByHandlerDecorator;
import org.andrico.andrico.Preferences;
import org.andrico.andrico.R;
import org.andrico.andrico.Synchronize;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AuthorizationActivity {
    public static final String LOG = "LoginActivity";
    
    private static final int GET_SESSION_RETRY_MAX_COUNT = 5;

    public static final String API_KEY_EXTRA = "api_key";
    public static final String API_SECRET_EXTRA = "api_secret";
    public static final String SECRET_EXTRA = "secret";
    public static final String SESSION_KEY_EXTRA = "session_key";
    public static final String UID_EXTRA = "uid";
    
    public static final int DIALOG_AUTH_TOKEN_REQUEST = 1;

    
    /**
     * Load up the login activity so that we can get a session.
     */
    public static Intent createActivityIntent(Context context, String apiKey, String apiSecret) {
        Intent loginIntent = new Intent(context,
                org.andrico.andrico.facebook.LoginActivity.class);
        loginIntent.putExtra(API_KEY_EXTRA, apiKey);
        loginIntent.putExtra(API_SECRET_EXTRA, apiSecret);
        return loginIntent;
    }

    private String mAuthToken;
    private FB mFacebook;
    private Handler mHandler;
    private int getSessionAttempts = 0;

    /**
     * Execute a getSession facebook method and on result exit the activity
     * returning a result containing session information.
     * 
     * @param authToken
     */
    protected void getSession(final String authToken) {
        Log.d(LOG, "Have authToken (" + authToken + "), requesting session");
        getSessionAttempts += 1;

        Toast t = Toast.makeText(this, "REQUESTING SESSION", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
    	t.show();
    	
        mFacebook.authenticate(mFacebook.auth_getSession(authToken),
                new HttpResponseByHandlerDecorator(mHandler, new FBMethodCallback() {
                    @Override
                    public void run() {
                        JSONObject session;
                        try {
                            session = new JSONObject(getResult());
                            String sessionKey = session.getString(LoginActivity.SESSION_KEY_EXTRA);
                            String secret = session.getString(LoginActivity.SECRET_EXTRA);
                            String uid = session.getString(LoginActivity.UID_EXTRA);

                            Log.d(LOG, "Fetched session: " + session.toString());
                            Intent result = new Intent();
                            result.putExtra(LoginActivity.SESSION_KEY_EXTRA, sessionKey);
                            result.putExtra(LoginActivity.SECRET_EXTRA, secret);
                            result.putExtra(LoginActivity.UID_EXTRA, uid);
                            Synchronize.tokenMessage = "ACQUIRED FACEBOOK SESSION";
                        	
                            setResult(RESULT_OK, result);
                            
                            
                            
                        } catch (JSONException e) {
                            Log.e(LOG, "Cannot Parse JSON result", e);
                            try {
                                session = new JSONObject(getResult());
                                String errorCode = session.getString("error_code");
                                if ((errorCode.equals("100") || errorCode.equals("104"))
                                        && getSessionAttempts < GET_SESSION_RETRY_MAX_COUNT) {
                                    Log.e(LOG, "Attempting to get the session again...");
                                    getSession(authToken);
                                    return;
                                }
                            } catch (JSONException e1) {
                                Log.e(LOG, "Stil cannot Parse JSON result", e1);
                            }
                            Synchronize.tokenMessage = "COULD NOT ACQUIRE SESSION";
                                                            	
                            setResult(RESULT_CANCELED);
                        }
                        finish();
                    }
                }));
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate");

        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.createInstance(this);

        mHandler = new Handler();

        Intent intent = getIntent();

        String apiKey = intent.getStringExtra(API_KEY_EXTRA);
        String apiSecret = intent.getStringExtra(API_SECRET_EXTRA);
        mFacebook = new FB(apiKey, apiSecret);

        Log.d(LOG, "Recieved Intent:" + intent.toString());
        // getAuthTokenAndLogin();
        loadUri(mFacebook.authorizeInfiniteSessionUrl());
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_AUTH_TOKEN_REQUEST:
                // This example shows how to add a custom layout to an
                // AlertDialog
                LayoutInflater factory = LayoutInflater.from(this);
                final View authDialog = factory.inflate(R.layout.facebook_auth_token_dialog, null);
                return new AlertDialog.Builder(this).setView(authDialog)
                				.setPositiveButton(android.R.string.ok, 
                					new DialogInterface.OnClickListener() 
                				{
                                    public void onClick(DialogInterface dialog, int whichButton)
                                    {
                                    	EditText authTokenEditText = (EditText)authDialog
                                    	.findViewById(R.id.auth_token);
                                    	
                                    	                                  	
                                    	
                                        getSession(authTokenEditText.getText().toString());
                                    }
                                })
                                /*.setNegativeButton(android.R.string.cancel, 
                                	new DialogInterface.OnClickListener() 
                                {
                                    public void onClick(DialogInterface dialog, int whichButton) 
                                    {
                                        Toast t = Toast.makeText(LoginActivity.this,
                                                "COULD NOT ACQUIRE SESSION", Toast.LENGTH_SHORT);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                    	t.show();
                                    	
                                        setResult(RESULT_CANCELED);
                                        finish();
                                    }
                                })*/.create();
        }
        return null;
    }

    /**
     * When a page is loaded in the AuthorizationActivity, check that it is the
     * correct Url, then suck down session information.
     * 
     * @param url
     * @return
     */
    @Override
    public void onPageStarted(String url) {
        // At this point the user has logged in successfully! Yay!
        // Bad URL: https://login.facebook.com/login.php?popup=1
        // Good URL: http://www.facebook.com/desktopapp.php?api_key=
        // c82e9b8b5c38e0573a1b39633beb0b6e&popup=
        if (url.indexOf("facebook.com/desktopapp.php?api_key=") > -1) {
            Log.d(LOG, "desktopapp.php?api_key was noticed, calling getSession");
            getSession(mAuthToken);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.googlecode.statusinator2.facebook.AuthorizationActivity#onPageFinished
     * (java.lang.String)
     */
    @Override
    public void onPageFinished(String url) 
    {
    	   	 
        if (url.indexOf("login.facebook.com/code_gen.php") > -1) 
        {
        	if (LoginActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        	{
        		((WebView)findViewById(R.id.web_view)).scrollTo(43, 0);
        	}
            Log.d(LOG, "code_gen.php was noticed, prompting user...");
            ((WebView)findViewById(R.id.web_view)).zoomIn();
            showDialog(DIALOG_AUTH_TOKEN_REQUEST);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onStop() {
        super.onStop();
        CookieSyncManager.getInstance().stopSync();
    }

}
