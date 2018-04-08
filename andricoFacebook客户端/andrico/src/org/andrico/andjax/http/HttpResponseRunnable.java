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

package org.andrico.andjax.http;

/**
 * Handle response to http requests in a UI thread.
 */

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import java.io.IOException;

/**
 * Used to encapsulate actions after an http request completes. TODO(jlapenna):
 * Should we make this a callable?
 */
public class HttpResponseRunnable implements Runnable, IHttpResponseRunnable {
    private static final String TAG = "HttpResponseRunnable";

    /**
     * Retrieve the string contents of the Entity attached to an http response.
     * 
     * @param response The response to pull the (String)entity from.
     * @return The contents of the entity from a string or null if there were
     *         any errors.
     */
    public static String httpResponseToString(HttpResponse response) {
        String responseString = null;
        try {
            responseString = EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            Log.d(TAG, "ParseException, its likely there was a network issue.");
        } catch (IOException e) {
            Log.d(TAG, "IOException, its likely there was a network issue.");
        } catch (NullPointerException e) {
            Log.d(TAG, "NullPointerException, its likely there was a network issue.");
        }

        if (responseString != null) {
            Log.d(TAG, responseString);
        }
        return responseString;
    }

    protected HttpResponse mHttpResponse;
    protected String mResponseString;

    /*
     * As this is a primitive concrete class, we must implement fail(), but in
     * the general case this class will never be directly instantiated.
     */
    public void fail(Exception e) {
        // Don't do anything!
    }

    public HttpResponse getResponse() {
        return this.mHttpResponse;
    }

    /*
     * As this is a primitive concrete class, we must implement run(), but in
     * the general case this class will never be directly instantiated.
     * @see java.lang.Runnable#run()
     */
    public void run() {
        // Don't do anything!
    }

    /**
     * Set the response that this runnable acts on. Runs in the thread that
     * run() is called in. To ease use, mResponseString is set by calling
     * response.getEntity() and converting it to a string.
     * 
     * @param method The http response that triggers this runnable to execute.
     */
    public void setResponse(HttpResponse response) {
        this.mHttpResponse = response;
    }
};
