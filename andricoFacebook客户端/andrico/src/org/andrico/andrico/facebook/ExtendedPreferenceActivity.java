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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class ExtendedPreferenceActivity extends AuthorizationActivity {
    private static final String LOG = "ExtendedPreferenceActivity";

    public static final String SESSION_KEY_EXTRA = "session_key";
    public static final String SECRET_EXTRA = "secret";
    public static final String UID_EXTRA = "uid";
    public static final String API_KEY_EXTRA = "api_key";
    public static final String API_SECRET_EXTRA = "api_secret";
    public static final String EXT_PERM_EXTRA = "ext_perm_extra";

    /**
     * Load up the login activity so that we can get a session.
     */
    public static Intent createActivityIntent(Context context, String apiKey, String apiSecret,
            String sessionKey, String secret, String uid, String extPerm) {
        Intent extPermIntent = new Intent(context,
                org.andrico.andrico.facebook.ExtendedPreferenceActivity.class);
        extPermIntent.putExtra(API_KEY_EXTRA, apiKey);
        extPermIntent.putExtra(API_SECRET_EXTRA, apiSecret);
        extPermIntent.putExtra(SESSION_KEY_EXTRA, sessionKey);
        extPermIntent.putExtra(SECRET_EXTRA, secret);
        extPermIntent.putExtra(UID_EXTRA, uid);
        extPermIntent.putExtra(EXT_PERM_EXTRA, extPerm);
        return extPermIntent;
    }

    private FB mFacebook;
    private String mNextUrl;
    private String mNextCancelUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "onCreate");

        Intent intent = getIntent();

        String apiKey = intent.getStringExtra(API_KEY_EXTRA);
        String apiSecret = intent.getStringExtra(API_SECRET_EXTRA);
        mFacebook = new FB(apiKey, apiSecret);

        String session = intent.getStringExtra(SESSION_KEY_EXTRA);
        String secret = intent.getStringExtra(SECRET_EXTRA);
        String uid = intent.getStringExtra(UID_EXTRA);
        mFacebook.setSession(session, secret, uid);

        String extPerm = intent.getStringExtra(EXT_PERM_EXTRA);

        mNextUrl = "http://apps.facebook.com/statusinator/extPermOk";
        mNextCancelUrl = "http://apps.facebook.com/statusinator/extPermCancel";
        loadUri(mFacebook.authorizeExtendedPreferenceUrl(extPerm, mNextUrl, mNextCancelUrl));
        Toast.makeText(this, "Loading authorization page.", Toast.LENGTH_SHORT).show();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.googlecode.statusinator2.facebook.AuthorizationActivity#onPageStarted
     * (java.lang.String)
     */
    @Override
    public void onPageStarted(String url) {
        if (url.equals(mNextUrl)) {
            Intent result = new Intent();
            setResult(RESULT_OK, result);
        } else if (url.equals(mNextCancelUrl)) {
            Intent result = new Intent();
            setResult(RESULT_CANCELED, result);
        } else {
            // The "default" case.
            return;
        }
        finish();
    }

    public void successful() {
    }

    /* (non-Javadoc)
     * @see com.googlecode.statusinator2.facebook.AuthorizationActivity#onPageFinished(java.lang.String)
     */
    @Override
    public void onPageFinished(String url) {
        // TODO Auto-generated method stub
        
    }
}
