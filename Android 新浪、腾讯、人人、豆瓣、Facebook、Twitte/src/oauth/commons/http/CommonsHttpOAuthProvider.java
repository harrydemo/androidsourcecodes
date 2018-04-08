/*
 * Copyright (c) 2009 Matthias Kaeppler Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package oauth.commons.http;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oauth.signpost.AbstractOAuthProvider;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.SSLSocketFactoryEx;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * This implementation uses the Apache Commons {@link HttpClient} 4.x HTTP
 * implementation to fetch OAuth tokens from a service provider. Android users
 * should use this provider implementation in favor of the default one, since
 * the latter is known to cause problems with Android's Apache Harmony
 * underpinnings.
 * 
 * @author Matthias Kaeppler
 */
public class CommonsHttpOAuthProvider extends AbstractOAuthProvider {

    private static final long serialVersionUID = 1L;

    private transient HttpClient httpClient;

    public CommonsHttpOAuthProvider(String requestTokenEndpointUrl, String accessTokenEndpointUrl,
            String authorizationWebsiteUrl) {
        super(requestTokenEndpointUrl, accessTokenEndpointUrl, authorizationWebsiteUrl);
        this.httpClient = new DefaultHttpClient();
    }

    public CommonsHttpOAuthProvider(String requestTokenEndpointUrl, String accessTokenEndpointUrl,
            String authorizationWebsiteUrl, HttpClient httpClient) {
        super(requestTokenEndpointUrl, accessTokenEndpointUrl, authorizationWebsiteUrl);
        this.httpClient = httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    protected HttpRequest createRequest(String endpointUrl) throws Exception {
        HttpPost request = new HttpPost(endpointUrl);
        return new HttpRequestAdapter(request);
    }
    
    @Override
	protected HttpRequest createRequestForTencent(String endpointUrl)
			throws Exception {
    	 HttpPost request = new HttpPost(endpointUrl);
    	 request.addHeader("Content-Type", "application/x-www-form-urlencoded");
         return new HttpRequestAdapter(request);
	}

    @Override
    protected oauth.signpost.http.HttpResponse sendRequest(HttpRequest request) throws Exception {
        HttpResponse response = httpClient.execute((HttpUriRequest) request.unwrap());
        return new HttpResponseAdapter(response);
    }
    
    @Override
	protected oauth.signpost.http.HttpResponse sendRequestForTencent(
			HttpRequest request) throws Exception {
		Map<String, String> headers = request.getAllHeaders();
		String oauthHeader = (String) headers.get("Authorization");
		HttpParameters httpParams = OAuth.oauthHeaderToParamsMap(oauthHeader);
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		Set<String> keys = httpParams.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = httpParams.getFirst(key);
			formParams.add(new BasicNameValuePair(key, URLDecoder.decode(value,
					"UTF-8")));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams,
				"UTF-8");
		HttpPost postRequest = (HttpPost) request.unwrap();
		postRequest.setEntity(entity);
		org.apache.http.HttpResponse response = getNewHttpClient().execute(postRequest);
		return new HttpResponseAdapter(response);
	}
    
    public HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}

    @Override
    protected void closeConnection(HttpRequest request, oauth.signpost.http.HttpResponse response)
            throws Exception {
        if (response != null) {
            HttpEntity entity = ((HttpResponse) response.unwrap()).getEntity();
            if (entity != null) {
                try {
                    // free the connection
                    entity.consumeContent();
                } catch (IOException e) {
                    // this means HTTP keep-alive is not possible
                    e.printStackTrace();
                }
            }
        }
    }

}
