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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Represents a facebook method to remotely execute by way of REST.
 */
public class FBMethod {
    // Non-static because we customize it.
   
    /**
     * Create an md5sum of all a method's parameters per @link XXX
     * 
     * @param md5 A MessageDigest set to generate md5s
     * @param parameters The parameters used for the method that will be
     *            signaturized.
     * @param secret A session's secret token.
     * @return
     */
    protected static String signature(MessageDigest md5, TreeMap<String, String> parameters,
            String secret) {
        Vector<String> keys = new Vector<String>(parameters.keySet());
        Collections.sort(keys);
        StringBuffer concat = new StringBuffer();
        for (Object key : keys) {
            concat.append(key + "=" + parameters.get(key));
        }
        concat.append(secret);

        byte[] sig = md5.digest(concat.toString().getBytes());
        return md5HexDigest(sig);
    }

    private static String md5HexDigest(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * Given the parameters return a string applicable as the query parameters
     * of an http GET.
     * 
     * @param parameters The key,value pairs to turn into a query.
     * @return
     */
    protected static String urlParameters(TreeMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        for (Object key : parameters.keySet()) {
            sb.append((String)key + "=" + java.net.URLEncoder.encode(parameters.get(key)) + "&");
        }

        int sbLength = sb.length();
        if (sbLength > 0) {
            sb.deleteCharAt(sbLength - 1);
        }
        return sb.toString();
    }

    private String LOG_SPECIFIC = "FBMethod";

    protected String mApiKey;
    protected TreeMap<String, String> mParameters;
    protected String mMethod;
    protected String mSecret;
    protected String mSession;
    protected byte[] mData;
    protected String mDataContentType = "image/jpg";
    protected String mDataFilename;
    private MessageDigest mMd5;

    FBMethod(String method, String apiKey, String secret, String session,
            TreeMap<String, String> parameters) {
        // Help keep track of messages when there are multiple at once.
        LOG_SPECIFIC += "." + method;

        mMethod = method;
        mApiKey = apiKey;
        mSecret = secret;
        mSession = session;

        if (mSession != null) {           
        } else {            
        }

        mParameters = getRequestParameters(method);
        if (parameters != null) {
            mParameters.putAll(parameters);
        }

        // Build the md5 builder
        try {
            mMd5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // NO ERROR HANDLING!
        }
    }

    /**
     * Get the default set of parameters for a facebook method.
     * 
     * @param method
     * @return
     */
    private TreeMap<String, String> getRequestParameters(String method) {
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("api_key", mApiKey);
        parameters.put("format", "JSON");
        parameters.put("v", "1.0");
        parameters.put("method", method);
        if (mSession != null) {          
            parameters.put("session_key", mSession);
        }

        return parameters;
    }

    /**
     * Generate the full URL to be used to execute this method.
     * 
     * @return
     */
    public String getRequestUrl() {
        String stringParams = (mParameters.isEmpty()) ? "" : (urlParameters(mParameters) + "&");
        String requestUrl = FB.REST_URI + stringParams + "sig="
                + FBMethod.signature(mMd5, mParameters, mSecret);
        return requestUrl;
    }

    /**
     * Test if a method has data that needs to be be posted.
     * 
     * @return
     */
    protected boolean hasData() {
        return (mData != null);
    }

    /**
     * If data is to be posted as part of the method execution, set it with
     * this.
     * 
     * @param data The bytes to be transmitted to facebook.
     * @param filename A name associated with the data.
     * @param contentType The type this data composes, eg "image/jpg" for a jpg.
     */
    protected void setData(byte[] data, String filename, String contentType) {
        mData = data;
        mDataContentType = contentType;
        mDataFilename = filename;
    }

}
