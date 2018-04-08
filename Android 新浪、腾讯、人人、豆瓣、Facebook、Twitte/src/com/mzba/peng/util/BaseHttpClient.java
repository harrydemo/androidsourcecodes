package com.mzba.peng.util;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


/**
 * 
 * @author 06peng
 *
 */
public final class BaseHttpClient implements HttpClient {
        
    // Gzip of data shorter than this probably won't be worthwhile
    public static long DEFAULT_SYNC_MIN_GZIP_BYTES = 256;

    private static final String TAG = "AndroidHttpClient";
    
    /** Set if HTTP requests are blocked from being executed on this thread */
    private static final ThreadLocal<Boolean> sThreadBlocked =
            new ThreadLocal<Boolean>();

    /** Interceptor throws an exception if the executing thread is blocked */
    private static final HttpRequestInterceptor sThreadCheckInterceptor =
            new HttpRequestInterceptor() {
        public void process(HttpRequest request, HttpContext context) {
            if (sThreadBlocked.get() != null && sThreadBlocked.get()) {
                throw new RuntimeException("This thread forbids HTTP requests");
            }
        }
    };

    /**
     * Create a new HttpClient with reasonable defaults (which you can update).
     *
     * @param userAgent to report in your HTTP requests.
     * @param sessionCache persistent session cache
     * @return AndroidHttpClient for you to use for all your requests.
     */
    public static BaseHttpClient newInstance(Context mContext, String userAgent) {
        HttpParams params = new BasicHttpParams();

        // Turn off stale checking.  Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        // Default connection and socket timeout of 20 seconds.  Tweak to taste.
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        // Don't handle redirects -- return them to the caller.  Our code
        // often wants to re-POST after a redirect, which we must do ourselves.
        HttpClientParams.setRedirecting(params, false);

        // Set the specified user agent and register standard protocols.
        HttpProtocolParams.setUserAgent(params, userAgent);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager manager = 
                new ThreadSafeClientConnManager(params, schemeRegistry);
        

        // We use a factory method to modify superclass initialization
        // parameters without the funny call-a-static-method dance.
        return new BaseHttpClient(mContext, manager, params);
    }

    //private final HttpClient delegate;
    private final DefaultHttpClient delegate;

    private RuntimeException mLeakedException = new IllegalStateException(
            "AndroidHttpClient created and never closed");

    private BaseHttpClient(Context mContext, ClientConnectionManager ccm, HttpParams params) {
        this.delegate = new DefaultHttpClient(ccm, params) {
        	@Override
            protected BasicHttpProcessor createHttpProcessor() {
                // Add interceptor to prevent making requests from main thread.
                BasicHttpProcessor processor = super.createHttpProcessor();
                processor.addRequestInterceptor(sThreadCheckInterceptor);
                processor.addRequestInterceptor(new CurlLogger());

                return processor;
            }

            @Override
            protected HttpContext createHttpContext() {
                // Same as DefaultHttpClient.createHttpContext() minus the
                // cookie store.
                HttpContext context = new BasicHttpContext();
                context.setAttribute(ClientContext.AUTHSCHEME_REGISTRY, getAuthSchemes());
                context.setAttribute(ClientContext.COOKIESPEC_REGISTRY, getCookieSpecs());
                context.setAttribute(ClientContext.CREDS_PROVIDER, getCredentialsProvider());
                return context;
            }
            
        };

        /*设置代理*/
        setProxy(mContext);
    }

    
    /**
     * 
     * @param mContext
     */
	private void setProxy(Context mContext) {
		try {
			ContentValues values = new ContentValues();
			Cursor cur = mContext.getContentResolver().query(
					Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
			if (cur != null && cur.getCount() > 0) {
				if (cur.moveToFirst()) {
					int colCount = cur.getColumnCount();
					for (int i = 0; i < colCount; i++) {
						values.put(cur.getColumnName(i), cur.getString(i));
					}
				}
				cur.close();
			}
			String proxyHost = (String) values.get("proxy");
			if (!TextUtils.isEmpty(proxyHost) && !isWiFiConnected(mContext)) {
				int prot = Integer.parseInt(String.valueOf(values.get("port")));
				delegate.getCredentialsProvider().setCredentials(
						new AuthScope(proxyHost, prot),
						new UsernamePasswordCredentials((String) values.get("user"), (String) values.get("password")));
				HttpHost proxy = new HttpHost(proxyHost, prot);
				delegate.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.w(TAG, "set proxy error+++++++++++++++++");
		}
	}


	/**
	 * 当前连接是否WiFi连接
	 * @param mContext
	 * @return
	 */
	private boolean isWiFiConnected(Context mContext) {
		boolean isWifiEnable = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo!=null&&activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			isWifiEnable = true;
		}
		return isWifiEnable;
	}

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mLeakedException != null) {
            Log.e(TAG, "Leak found", mLeakedException);
            mLeakedException = null;
        }
    }

    /**
     * Block this thread from executing HTTP requests.
     * Used to guard against HTTP requests blocking the main application thread.
     * @param blocked if HTTP requests run on this thread should be denied
     */
    public static void setThreadBlocked(boolean blocked) {
        sThreadBlocked.set(blocked);
    }

    /**
     * Modifies a request to indicate to the server that we would like a
     * gzipped response.  (Uses the "Accept-Encoding" HTTP header.)
     * @param request the request to modify
     * @see #getUngzippedContent
     */
    public static void modifyRequestToAcceptGzipResponse(HttpRequest request) {
        request.addHeader("Accept-Encoding", "gzip");
    }

    /**
     * Gets the input stream from a response entity.  If the entity is gzipped
     * then this will get a stream over the uncompressed data.
     *
     * @param entity the entity whose content should be read
     * @return the input stream to read from
     * @throws IOException
     */
    public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
		InputStream responseStream = entity.getContent();
		if (responseStream == null)
			return responseStream;
		Header header = entity.getContentEncoding();
		if (header == null)
			return responseStream;
		String contentEncoding = header.getValue();
		if (contentEncoding == null)
			return responseStream;
		if (contentEncoding.contains("gzip"))
			responseStream = new GZIPInputStream(responseStream);
		return responseStream;
    }

    /**
     * Release resources associated with this client.  You must call this,
     * or significant resources (sockets and memory) may be leaked.
     */
    public void close() {
        if (mLeakedException != null) {
            getConnectionManager().shutdown();
            mLeakedException = null;
        }
    }

    public HttpParams getParams() {
        return delegate.getParams();
    }

    public ClientConnectionManager getConnectionManager() {
        return delegate.getConnectionManager();
    }

    public HttpResponse execute(HttpUriRequest request) throws IOException {
        return delegate.execute(request);
    }

    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
        return delegate.execute(request, context);
    }

    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
        return delegate.execute(target, request);
    }

    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
        return delegate.execute(target, request, context);
    }

    public <T> T execute(HttpUriRequest request,  ResponseHandler<? extends T> responseHandler)
            throws IOException, ClientProtocolException {
        return delegate.execute(request, responseHandler);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
            throws IOException, ClientProtocolException {
        return delegate.execute(request, responseHandler, context);
    }

    public <T> T execute(HttpHost target, HttpRequest request,
            ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return delegate.execute(target, request, responseHandler);
    }

    public <T> T execute(HttpHost target, HttpRequest request,
            ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return delegate.execute(target, request, responseHandler, context);
    }

    /* cURL logging support. */

    /**
     * Logging tag and level.
     */
    private static class LoggingConfiguration {

        private final String tag;
        private final int level;

        private LoggingConfiguration(String tag, int level) {
            this.tag = tag;
            this.level = level;
        }

        /**
         * Returns true if logging is turned on for this configuration.
         */
        private boolean isLoggable() {
            return Log.isLoggable(tag, level);
        }

        /**
         * Returns true if auth logging is turned on for this configuration.  Can only be set on
         * insecure devices.
         */
        private boolean isAuthLoggable() {
//            String secure = SystemProperties.get("ro.secure");
//            return "0".equals(secure) && Log.isLoggable(tag + "-auth", level);
        	return false;
        }

        /**
         * Prints a message using this configuration.
         */
        private void println(String message) {
            Log.println(level, tag, message);
        }
    }

    /** cURL logging configuration. */
    private volatile LoggingConfiguration curlConfiguration;

    /**
     * Enables cURL request logging for this client.
     *
     * @param name to log messages with
     * @param level at which to log messages (see {@link android.util.Log})
     */
    public void enableCurlLogging(String name, int level) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (level < Log.VERBOSE || level > Log.ASSERT) {
            throw new IllegalArgumentException("Level is out of range ["
                + Log.VERBOSE + ".." + Log.ASSERT + "]");    
        }

        curlConfiguration = new LoggingConfiguration(name, level);
    }

    /**
     * Disables cURL logging for this client.
     */
    public void disableCurlLogging() {
        curlConfiguration = null;
    }

    /**
     * Logs cURL commands equivalent to requests.
     */
    private class CurlLogger implements HttpRequestInterceptor {
        public void process(HttpRequest request, HttpContext context)  throws HttpException, IOException {
            LoggingConfiguration configuration = curlConfiguration;
            if (configuration != null && configuration.isLoggable() && request instanceof HttpUriRequest) {
                configuration.println(toCurl((HttpUriRequest) request, configuration.isAuthLoggable()));
            }
        }
    }

    /**
     * Generates a cURL command equivalent to the given request.
     */
    private static String toCurl(HttpUriRequest request, boolean logAuthToken) throws IOException {
        StringBuilder builder = new StringBuilder();

        builder.append("curl ");

        for (Header header: request.getAllHeaders()) {
            if (!logAuthToken && (header.getName().equals("Authorization") || header.getName().equals("Cookie"))) {
                continue;
            }
            builder.append("--header \"");
            builder.append(header.toString().trim());
            builder.append("\" ");
        }

        URI uri = request.getURI();

        // If this is a wrapped request, use the URI from the original
        // request instead. getURI() on the wrapper seems to return a
        // relative URI. We want an absolute URI.
        if (request instanceof RequestWrapper) {
            HttpRequest original = ((RequestWrapper) request).getOriginal();
            if (original instanceof HttpUriRequest) {
                uri = ((HttpUriRequest) original).getURI();
            }
        }

        builder.append("\"");
        builder.append(uri);
        builder.append("\"");

        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            HttpEntity entity = entityRequest.getEntity();
            if (entity != null && entity.isRepeatable()) {
                if (entity.getContentLength() < 1024) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    entity.writeTo(stream);
                    String entityString = stream.toString();

                    // TODO: Check the content type, too.
                    builder.append(" --data-ascii \"").append(entityString).append("\"");
                } else {
                    builder.append(" [TOO MUCH DATA TO INCLUDE]");
                }
            }
        }

        return builder.toString();
    }
}
