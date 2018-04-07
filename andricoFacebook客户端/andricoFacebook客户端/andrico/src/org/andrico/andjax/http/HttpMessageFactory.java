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

import org.apache.http.HttpMessage;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.util.Log;
import android.webkit.CookieManager;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Generate http request objects that can be executed by the http client
 * service.
 * 
 * 
 */
class HttpMessageFactory {

    public static final String TAG = "HttpMessageFactory";

    private final String mCookieDomain;

    /**
     * Creates an httpMessageFactory controlling the specified domain.
     * 
     * @param cookieDomain
     */
    public HttpMessageFactory(String cookieDomain) {
        this.mCookieDomain = cookieDomain;
    }

    /**
     * Create a new http uri request with more standard datatypes.
     * 
     * @param url The url to push the request to.
     * @param params String parameters to pass in the post.
     * @throws URISyntaxException
     */
    public HttpMessage create(String url, Map<String, String> params) throws URISyntaxException {
        MultipartEntity multipartEntity = null;
        if (params != null) {
            multipartEntity = new MultipartEntity();
            for (final String key : params.keySet()) {
                try {
                    multipartEntity.addPart(key, new StringBody(params.get(key)));
                } catch (final UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return this.createFromParts(url, multipartEntity);
    }

    /**
     * Create a new http uri request and submit for execution. Promote this to
     * public when I need to actually instantiate an HttpMessage with these
     * parameters.
     * 
     * @param url The url that this request will access.
     * @param multipartEntity The MultipartEntity to send with the request.
     * @return
     * @throws URISyntaxException
     */
    public HttpMessage createFromParts(String url, MultipartEntity multipartEntity)
            throws URISyntaxException {
        HttpMessage method;
        if (multipartEntity == null) {
            Log.d("HttpMessageFactory", "Using HttpGet");
            method = new HttpGet(url);
        } else {
            Log.d("HttpMessageFactory", "Using HttpPost");
            method = new HttpPost(url);
            ((HttpPost)method).setEntity(multipartEntity);
        }

        if (mCookieDomain != null) {
            final CookieManager cookieManager = CookieManager.getInstance();
            method.addHeader("Cookie", cookieManager.getCookie(this.mCookieDomain));
        }
        ;
        return method;
    }
}
