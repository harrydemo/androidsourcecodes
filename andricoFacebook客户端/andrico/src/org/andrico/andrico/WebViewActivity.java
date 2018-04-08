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

import org.andrico.andrico.R;
import org.andrico.andrico.facebook.AuthorizationActivity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;


public abstract class WebViewActivity extends Activity {
    private class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            WebViewActivity.this.setProgress(newProgress * 100);
        }
    }
   
    
    private class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(LOG, "onPageStarted: " + url);
            WebViewActivity.this.onPageStarted(url);   
        }
        
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(LOG, "onPageStarted: " + url);
            if ((url.indexOf("http://www.facebook.com/login.php") > -1 ))
            {
            	if (WebViewActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            	{
            		mWebView.scrollTo(200, 270);
            		((WebView)findViewById(R.id.web_view)).zoomIn();
            	}
            	else
            	{
            		mWebView.scrollTo(270, 350);
            	}
            	
            	((WebView)findViewById(R.id.web_view)).zoomIn();
            	
            }
            WebViewActivity.this.onPageFinished(url);
        }
    }
    
    // ----------------------------------------------------------------------------
    
    
    private static final String LOG = "WebViewActivity";

    private Uri mLastUrl = null;
    private WebView mWebView;

    public void loadUri(Uri url) {
        Log.d(LOG, "loadUrl: " + url);
        mLastUrl = url;
        mWebView.loadUrl(url.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        Log.e(LOG, "onCreate");

        CookieSyncManager.createInstance(this);

        setContentView(R.layout.facebook_auth_activity);
        mWebView = (WebView)findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    abstract public void onPageStarted(String url);
    abstract public void onPageFinished(String url);

    @Override
    protected void onStart() {
        super.onStart();
        if (mLastUrl != null) {
            mWebView.loadUrl(mLastUrl.toString());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebView.stopLoading();
    }
}
