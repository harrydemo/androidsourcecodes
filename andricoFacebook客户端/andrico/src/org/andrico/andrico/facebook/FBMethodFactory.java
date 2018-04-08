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

import org.andrico.andrico.facebook.FBBase.Session;

import java.util.HashMap;
import java.util.TreeMap;

public class FBMethodFactory {

    private String mApiKey;
    private String mApiSecret;
    private Session mSession;
    private Session mFacebookSession;

    /**
     * @param cookieDomain
     */
    public FBMethodFactory(String cookieDomain, String apiKey, String secret) {
        mApiKey = apiKey;
        mApiSecret = secret;
    }

    public FBMethod create(String method, TreeMap<String, String> parameters) {
        return create(method, parameters, false);
    }

    public FBMethod create(String method, TreeMap<String, String> parameters, Boolean session) {
        if (session && mSession == null) 
        {
            throw new RuntimeException("Unable to associate an established session with new facebook method.");
        }
        String secretParam = (session) ? mSession.getSecret() : mApiSecret;
        String sessionParam = (session) ? mSession.getSession() : null;
       
        return new FBMethod(method, mApiKey, secretParam, sessionParam, parameters);
    }

    public FBMethod FBMethod(String method) {
        return this.create(method, null);
    }

    /**
     * @return
     */
    public Session getSession() {
        if (mSession == null) {
            return null;
        } else {
            return mFacebookSession;
        }
    }

    public Boolean hasSession() {
        return mSession == null ? false : true;
    }

    /**
     * Using the string returned from the http sesion request, cast it to a JSON
     * object and store it.
     * 
     * @param session
     */
    public void setSession(Session session) {
        mSession = session;
    }

    /**
     * 
     */
    public void unsetSession() {
        mSession = null;
    }

}
