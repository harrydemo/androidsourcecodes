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

package org.andrico.andrico;


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
import android.net.Uri;
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


public class WebActivity extends WebViewActivity {
    public static final String LOG = "LoginActivity";
    
    
    public static final String UID_EXTRA = "uid";
    
    
    /**
     * Load up the login activity so that we can get a session.
     */
    public static Intent createActivityIntent(Context context, String apiKey, String apiSecret) {
        Intent loginIntent = new Intent(context, org.andrico.andrico.WebActivity.class);
        return loginIntent;
    }


    /**
     * Execute a getSession facebook method and on result exit the activity
     * returning a result containing session information.
     * 
     * @param authToken
     */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "onCreate");

        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.createInstance(this);


        Intent intent = getIntent();

    
        Log.d(LOG, "Recieved Intent:" + intent.toString());
        
        String path = intent.getStringExtra("url");
        Uri url = Uri.parse(path);
        
        loadUri(url);
    }

    
    /**
     * @param url
     * @return
     */
    @Override
    public void onPageStarted(String url) 
    {    
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
