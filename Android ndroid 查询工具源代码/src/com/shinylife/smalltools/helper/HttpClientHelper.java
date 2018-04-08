package com.shinylife.smalltools.helper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientHelper {

	private static final long serialVersionUID = 808018030183407996L;

	private static final int OK = 200;// OK: Success!
	private static final int NOT_MODIFIED = 304;
	private static final int BAD_REQUEST = 400;
	private static final int NOT_AUTHORIZED = 401;
	private static final int FORBIDDEN = 403;
	private static final int NOT_FOUND = 404;
	private static final int NOT_ACCEPTABLE = 406;
	private static final int INTERNAL_SERVER_ERROR = 500;
	private static final int BAD_GATEWAY = 502;
	private static final int SERVICE_UNAVAILABLE = 503;

	private final static boolean DEBUG = Constants.DEBUG;
	private int retryCount = Constants.HTTP_REQUEST_RETRY_COUNT;
	private int retryIntervalMillis = Constants.HTTP_REQUEST_RETRY_INTERVAL_SECONDS * 1000;
	private int connectionTimeout = Constants.HTTP_REQUEST_CONNECTION_TIMEOUT * 1000;
	private int readTimeout = Constants.HTTP_REQUEST_READ_TIMEOUT * 1000;

	private String reqestCharset = "utf-8";
	private String responseCharset = "utf-8";

	private Map<String, String> requestHeaders = new HashMap<String, String>();

	public void setRequestCharset(String charset) {
		reqestCharset = charset;
	}

	public String getRequestCharset() {
		return reqestCharset == null ? "ISO-8859-1" : reqestCharset;
	}

	public void setResponseCharset(String charset) {
		responseCharset = charset;
	}

	public String getResponseCharset() {
		return responseCharset == null ? "ISO-8859-1" : responseCharset;
	}

	public HttpResponse delete(String url) throws HttpException {
		return delete(url, true);
	}

	public HttpResponse delete(String url, boolean isEncodeParameters)
			throws HttpException {
		return delete(url, null, isEncodeParameters);
	}

	public HttpResponse delete(String url, HttpPostParameter[] postParams,
			boolean isEncodeParameters) throws HttpException {
		return httpRequest(url, "DELETE", postParams, isEncodeParameters);
	}

	public HttpResponse post(String url) throws HttpException {
		return post(url, true);
	}

	public HttpResponse post(String url, boolean isEncodeParameters)
			throws HttpException {
		return post(url, null, isEncodeParameters);
	}

	public HttpResponse post(String url, HttpPostParameter[] postParams,
			boolean isEncodeParameters) throws HttpException {
		return httpRequest(url, "POST", postParams, isEncodeParameters);
	}

	public HttpResponse get(String url) throws HttpException {
		return get(url, true);
	}

	public HttpResponse get(String url, boolean isEncodeParameters)
			throws HttpException {
		return get(url, null, isEncodeParameters);
	}

	public HttpResponse get(String url, HttpPostParameter[] postParams,
			boolean isEncodeParameters) throws HttpException {
		return httpRequest(url, "GET", postParams, isEncodeParameters);
	}

	public HttpResponse httpRequest(String url, String httpMethod,
			HttpPostParameter[] postParams, boolean isEncodeParameters)
			throws HttpException {
		int retriedCount;
		int retry = retryCount;
		HttpResponse res = null;
		for (retriedCount = 0; retriedCount < retry; retriedCount++) {
			int responseCode = -1;
			log("发起请求：" + url);
			try {
				HttpURLConnection con = null;
				OutputStream osw = null;
				try {
					con = getConnection(url);
					setHeaders(con);
					if ("POST".equals(httpMethod)) {
						con.setRequestMethod("POST");
						con.setRequestProperty("Content-Type",
								"application/x-www-form-urlencoded");
						con.setDoOutput(true);
						con.setDoInput(true);
						String postParam = "";
						if (postParams != null) {
							postParam = encodeParameters(postParams,
									isEncodeParameters);
						}

						byte[] bytes = postParam.getBytes(getRequestCharset());
						log("请求长度: ", Integer.toString(bytes.length) + ",请求参数："
								+ postParam);
						// con.setRequestProperty("Content-Length",
						// Integer.toString(bytes.length));
						osw = con.getOutputStream();
						osw.write(bytes);
						osw.flush();
						osw.close();
					} else if ("DELETE".equals(httpMethod)) {
						con.setRequestMethod("DELETE");
					} else {
						con.setRequestMethod("GET");
					}
					res = new HttpResponse(con, getResponseCharset());
					responseCode = con.getResponseCode();
					if (DEBUG) {
						log("Response: ");
						Map<String, List<String>> responseHeaders = con
								.getHeaderFields();
						for (String key : responseHeaders.keySet()) {
							List<String> values = responseHeaders.get(key);
							for (String value : values) {
								if (null != key) {
									log(key + ": " + value);
								} else {
									log(value);
								}
							}
						}
					}
					if (responseCode != OK) {
						if (responseCode < INTERNAL_SERVER_ERROR
								|| retriedCount >= retryCount) {
							throw new HttpException(getCause(responseCode)
									+ "\n" + res.asString(), responseCode);
						}
					} else {
						break;
					}
				} finally {
					try {
						osw.close();
					} catch (Exception ignore) {
					}
				}
			} catch (IOException ioe) {
				if (retriedCount >= retryCount) {
					throw new HttpException(ioe.getMessage(), ioe, responseCode);
				}
			}
			try {
				if (DEBUG && null != res) {
					res.asString();
				}
				log("Sleeping " + retryIntervalMillis
						+ " millisecs for next retry.");
				Thread.sleep(retryIntervalMillis);
			} catch (InterruptedException ignore) {

			}
		}
		return res;
	}

	public String encodeParameters(HttpPostParameter[] postParams,
			boolean isEncodeParameters) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < postParams.length; j++) {
			if (j != 0) {
				buf.append("&");
			}
			if (isEncodeParameters) {
				buf.append(encode(postParams[j].name)).append("=")
						.append(encode(postParams[j].value));
			} else {
				buf.append(postParams[j].name).append("=")
						.append(postParams[j].value);
			}
		}
		return buf.toString();
	}

	/**
	 * rfc3986
	 * 
	 * @param value
	 * @return
	 */
	public String encode(String value) {
		String encoded = null;
		try {
			encoded = URLEncoder.encode(value, getRequestCharset());
		} catch (UnsupportedEncodingException ignore) {
		}
		StringBuffer buf = new StringBuffer(encoded.length());
		char focus;
		for (int i = 0; i < encoded.length(); i++) {
			focus = encoded.charAt(i);
			if (focus == '*') {
				buf.append("%2A");
			} else if (focus == '+') {
				buf.append("%20");
			} else if (focus == '%' && (i + 1) < encoded.length()
					&& encoded.charAt(i + 1) == '7'
					&& encoded.charAt(i + 2) == 'E') {
				buf.append('~');
				i += 2;
			} else {
				buf.append(focus);
			}
		}
		return buf.toString();
	}

	private void setHeaders(HttpURLConnection connection) {
		for (String key : requestHeaders.keySet()) {
			connection.addRequestProperty(key, requestHeaders.get(key));
			log(key + ": " + requestHeaders.get(key));
		}
	}

	public void setRequestHeader(String name, String value) {
		requestHeaders.put(name, value);
	}

	public String getRequestHeader(String name) {
		return requestHeaders.get(name);
	}

	private HttpURLConnection getConnection(String url)
			throws MalformedURLException, IOException {
		HttpURLConnection con = (HttpURLConnection) new URL(url)
				.openConnection();
		if (connectionTimeout > 0) {
			con.setConnectTimeout(connectionTimeout);
		}
		if (readTimeout > 0) {
			con.setReadTimeout(readTimeout);
		}
		return con;
	}

	private static void log(String message) {
		if (DEBUG) {
			System.out.println("[" + new java.util.Date() + "]" + message);
		}
	}

	private static void log(String message, String message2) {
		if (DEBUG) {
			log(message + message2);
		}
	}

	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}
}
