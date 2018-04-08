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

 /* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.andrico.andjax.http;

import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Handle http connections.
 * 
 * Make asynchronous http requests with Future actions. We support two threads because historically, thats all browsers
 * are allowed to pull from per domain. Since we're only touching one domain, this matches that expectation
 * 
 * @author jlapenna@gmail.com (Joe LaPenna)
 * 
 */
public class HttpClientService {
    /**
     * Encapsulates the http request in a runnable to be executed. When called,
     * the passed httpRequest will be executed by the HttpClientService
     * instance's executor.
     * 
     * @author jlapenna
     */
    private class HttpMessageCallable implements Callable<HttpResponse> {
        private static final String LOG = "HttpMessageCallable";
        private final HttpMessage mHttpRequest;

        /**
         * @param httpRequest The http request that will be executed when this
         *            is called.
         */
        public HttpMessageCallable(HttpMessage httpRequest) {
            this.mHttpRequest = httpRequest;
        }

        /*
         * When called, the http message will be executed by the
         * HttpClientService's http client.
         * @return HttpResponse The result of the http request.
         */
        public HttpResponse call() throws Exception {
            try {
                return HttpClientService.this.mHttpClient
                        .execute((HttpUriRequest)this.mHttpRequest);
            } catch (final Exception e) {
                Log.d(LOG, "call caused an exception.", e);
                throw e;
            }
        }
    }

    public static final String TAG = "HttpClientService";

    /**
     * The thread executor.
     */
    private ExecutorService mExecutorService;

    /**
     * Shard http client.
     */
    private HttpClient mHttpClient;

    /**
     * The factory used to generate http requests for this service.
     */
    private final HttpMessageFactory mHttpMessageFactory;

    private final String mCookieDomain;

    private final int mPoolSize;

    /**
     * Construct the HttpClientService
     * 
     * @param poolSize The number of threads allowed to exist at once.
     * @param cookieDomain I'm not quite sure what I'm doing with this. figure
     *            it out.
     */
    public HttpClientService(int poolSize, String cookieDomain) {
        mPoolSize = poolSize;
        mCookieDomain = cookieDomain;

        // Handles generating HttpRequests.
        this.mHttpMessageFactory = new HttpMessageFactory(mCookieDomain);

        start();
    }

    public synchronized void stop() {
        assertState();
        if (isRunning()) {
            this.mHttpClient.getConnectionManager().shutdown();
            this.mHttpClient = null;

            this.mExecutorService.shutdown();
            this.mExecutorService = null;
        }
    }

    public synchronized void start() {
        assertState();
        if (!isRunning()) {
            // Use the client defaults and create a client.
            this.mHttpClient = this.createHttpClient();

            // Sets up the thread pool part of the service.
            this.mExecutorService = Executors.newFixedThreadPool(mPoolSize);
        }
    }

    public synchronized boolean isRunning() {
        assertState();
        return this.mHttpClient != null && this.mExecutorService != null;
    }

    private void assertState() {
        assert (this.mHttpClient != null && this.mExecutorService != null)
                || (this.mHttpClient == null && this.mExecutorService == null);
    }

    /**
     * Create a thread-safe client.
     * 
     * @return HttpClient
     */
    private final HttpClient createHttpClient() {
        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));

        // Set some client http client parameter defaults.
        final HttpParams httpParams = this.createHttpParams();

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
                supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private final HttpParams createHttpParams() {
        // prepare parameters
        final HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(params, true);
        return params;
    }

    private HttpResponse execute(final HttpMessage httpMessage) {
        try {
            return this.mHttpClient.execute((HttpUriRequest)httpMessage);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Execute an http request, calling the runnable after. Right now this is
     * limited in that you can only pass one value per key in the http post.
     * (The Map is the limiter).
     * 
     * @param url The url to access
     * @param responseRunnable The runnable to execute upon http request
     *            completion.
     * @throws URISyntaxException
     */
    public HttpResponse execute(String url) throws URISyntaxException {
        final HttpMessage httpMessage = this.mHttpMessageFactory.create(url, null);
        return execute(httpMessage);
    }

    /**
     * Execute an http request, calling the runnable after. Right now this is
     * limited in that you can only pass one value per key in the http post.
     * (The Map is the limiter).
     * 
     * @param url The url to access
     * @param params The POST parameters to pass to this request.
     * @throws URISyntaxException
     */
    public HttpResponse execute(String url, Map<String, String> params) throws URISyntaxException {
        Log.d(TAG, "Executing");
        final HttpMessage httpMessage = this.mHttpMessageFactory.create(url, params);
        return execute(httpMessage);
    }

    /**
     * Execute an http request, calling the runnable after. Right now this is
     * limited in that you can only pass one value per key in the http post.
     * (The Map is the limiter).
     * 
     * @param url The url to access
     * @param multipartEntity The POST parameters to pass to this request.
     * @throws URISyntaxException
     */
    public HttpResponse execute(String url, MultipartEntity multipartEntity)
            throws URISyntaxException {
        HttpMessage httpMessage = this.mHttpMessageFactory.createFromParts(url, multipartEntity);
        return this.execute(httpMessage);
    }

    @Override
    public void finalize() {
        this.mExecutorService.shutdownNow();
    }

    /**
     * Put this HttpChainingRunnable that is composed of a HttpMessageCallable
     * and HttpChainingRunnable on the queue to be executed. Upon completion of
     * the http request, call the runnable. Right now this is limited in that
     * you can only pass one value per key in the http post. (The Map is the
     * limiter).
     * 
     * @param chainingRunnable The runnable that will cause a runnable to be
     *            called when an http request completes.
     */
    private void submit(HttpChainingRunnable chainingRunnable) {
        this.mExecutorService.submit(chainingRunnable);
    }

    /**
     * Put an http request on the queue to be executed. Upon completion of the
     * http request, a runnable will be called. Right now this is limited in
     * that you can only pass one value per key in the http post. (The Map is
     * the limiter).
     * 
     * @param url The url to access.
     * @param responseRunnable The runnable to execute upon http request
     *            completion.
     * @throws URISyntaxException
     */
    public void submit(String url, IHttpResponseRunnable responseRunnable)
            throws URISyntaxException {
        final HttpMessage httpMessage = this.mHttpMessageFactory.create(url, null);
        this
                .submit(new HttpChainingRunnable(new HttpMessageCallable(httpMessage),
                        responseRunnable));
    }

    /**
     * Put an http request on the queue to be executed. Upon completion of the
     * http request, a runnable will be called. Right now this is limited in
     * that you can only pass one value per key in the http post. (The Map is
     * the limiter).
     * 
     * @param url The url to access
     * @param params The POST parameters to pass to this request.
     * @param responseRunnable The runnable to execute upon http request
     *            completion.
     * @throws URISyntaxException
     */
    public void submit(String url, Map<String, String> params,
            IHttpResponseRunnable responseRunnable) throws URISyntaxException {
        final HttpMessage httpMessage = this.mHttpMessageFactory.create(url, params);

        this
                .submit(new HttpChainingRunnable(new HttpMessageCallable(httpMessage),
                        responseRunnable));
    }

    /**
     * Put an http request on the queue to be executed. Upon completion of the
     * http request, a runnable will be called. Right now this is limited in
     * that you can only pass one value per key in the http post. (The Map is
     * the limiter).
     * 
     * @param url The url to access.
     * @param multipartEntity The POST parameters to pass to this request.
     * @param responseRunnable The runnable to execute upon http request
     *            completion.
     * @throws URISyntaxException
     */
    public void submit(String url, MultipartEntity multipartEntity,
            IHttpResponseRunnable responseRunnable) throws URISyntaxException {
        Log.d(TAG, "submitting multipartentity dodad");
        HttpMessage httpMessage;

        httpMessage = this.mHttpMessageFactory.createFromParts(url, multipartEntity);
        Log.d(TAG, "Posting multipartEntity" + multipartEntity.toString());
        Log.d(TAG, String.valueOf(multipartEntity.getContentLength()));
        Log.d(TAG, multipartEntity.toString());
        this
                .submit(new HttpChainingRunnable(new HttpMessageCallable(httpMessage),
                        responseRunnable));
    }

}
