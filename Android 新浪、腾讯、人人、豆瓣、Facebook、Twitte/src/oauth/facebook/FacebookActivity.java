/*
 * Copyright 2010 Facebook, Inc.
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

package oauth.facebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mzba.peng.BasicActivity;

public class FacebookActivity extends BasicActivity {

    static final int FB_BLUE = 0xFF6D84B4;
    static final float[] DIMENSIONS_DIFF_LANDSCAPE = {20, 60};
    static final float[] DIMENSIONS_DIFF_PORTRAIT = {40, 60};
    static final FrameLayout.LayoutParams FILL =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                         ViewGroup.LayoutParams.MATCH_PARENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;
    static final String DISPLAY_STRING = "touch";
    static final String FB_ICON = "icon.png";

    private String mUrl;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private FrameLayout mContent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getStringExtra("url");
        mSpinner = new ProgressDialog(this);
        mSpinner.setMessage("Loading...");  
        mContent = new FrameLayout(this);
        mContent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        /* Create the 'x' image, but don't add to the mContent layout yet
         * at this point, we only need to know its drawable width and height 
         * to place the webview
         */
//        createCrossImage();
        
        /* Now we know 'x' drawable width and height, 
         * layout the webivew and add it the mContent layout
         */
//        int crossWidth = mCrossImage.getDrawable().getIntrinsicWidth();
        setUpWebView();
        
        /* Finally add the 'x' image to the mContent layout and
         * add mContent to the Dialog view
         */
//        mContent.addView(mCrossImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        setContentView(mContent);
    }
    
//    private void createCrossImage() {
//        mCrossImage = new ImageView(this);
//        // Dismiss the dialog when user click on the 'x'
//        mCrossImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Facebook.listener.onCancel();
//                finish();
//            }
//        });
//        Drawable crossDrawable = getResources().getDrawable(R.drawable.close);
//        mCrossImage.setImageDrawable(crossDrawable);
//        /* 'x' should not be visible while webview is loading
//         * make it visible only after webview has fully loaded
//        */
//        mCrossImage.setVisibility(View.INVISIBLE);
//    }

    private void setUpWebView() {
        LinearLayout webViewContainer = new LinearLayout(this);
        mWebView = new WebView(this);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new FacebookActivity.FbWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mWebView.setVisibility(View.INVISIBLE);
        mWebView.getSettings().setSavePassword(false);
        
 //       webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(mWebView);
        mContent.addView(webViewContainer);
    }

    private class FbWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Util.logd("Facebook-WebView", "Redirect URL: " + url);
            if (url.startsWith(Facebook.REDIRECT_URI)) {
                Bundle values = Util.parseUrl(url);

                String error = values.getString("error");
                if (error == null) {
                    error = values.getString("error_type");
                }

                if (error == null) {
                	Facebook.listener.onComplete(values);
                } else if (error.equals("access_denied") ||
                           error.equals("OAuthAccessDeniedException")) {
                	Facebook.listener.onCancel();
                } else {
                	Facebook.listener.onFacebookError(new FacebookError(error));
                }

                finish();
                return true;
            } else if (url.startsWith(Facebook.CANCEL_URI)) {
            	Facebook.listener.onCancel();
                finish();
                return true;
            } else if (url.contains(DISPLAY_STRING)) {
                return false;
            }
            // launch non-dialog URLs in a full browser
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Facebook.listener.onError(
                    new DialogError(description, errorCode, failingUrl));
            finish();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Util.logd("Facebook-WebView", "Webview loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            if (mSpinner != null || !mSpinner.isShowing()) {
            	 mSpinner.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSpinner.dismiss();
            /* 
             * Once webview is fully loaded, set the mContent background to be transparent
             * and make visible the 'x' image. 
             */
            mContent.setBackgroundColor(Color.TRANSPARENT);
            mWebView.setVisibility(View.VISIBLE);
        }
    }
}
