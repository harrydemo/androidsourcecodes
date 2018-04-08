package com.mzba.oauth;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.basic.HttpURLConnectionResponseAdapter;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpResponse;
/**
 * 
 * @author 06peng
 *
 */
public class DoubanOAuthProvider extends DefaultOAuthProvider {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MAC_NAME = "HmacSHA1";

	public DoubanOAuthProvider(String requestTokenEndpointUrl,
			String accessTokenEndpointUrl, String authorizationWebsiteUrl) {
		super(requestTokenEndpointUrl, accessTokenEndpointUrl, authorizationWebsiteUrl);
	}

	@Override
	protected void retrieveToken(OAuthConsumer consumer, String endpointUrl,
			String... additionalParameters) throws OAuthMessageSignerException,
			OAuthCommunicationException, OAuthNotAuthorizedException,
			OAuthExpectationFailedException {
		if (consumer.getConsumerKey() == null || consumer.getConsumerSecret() == null) {
            throw new OAuthExpectationFailedException("Consumer key or secret not set");
        }
		
		HttpURLConnection connection = null;
        HttpResponse response = null;
        String requestMethod = "GET";
        try {
        	String tokenSecet = consumer.getTokenSecret();
        	if(tokenSecet == null) {
        		tokenSecet = "";
        	}
        	HashMap<String, String> query = new HashMap<String, String>();
        	query.put(oauth.signpost.OAuth.OAUTH_CONSUMER_KEY, consumer.getConsumerKey());
        	query.put(oauth.signpost.OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA1");
        	query.put(oauth.signpost.OAuth.OAUTH_TIMESTAMP, Long.toString(System.currentTimeMillis() / 1000L));
        	query.put(oauth.signpost.OAuth.OAUTH_NONCE, Long.toString(new Random().nextLong()));
        	if(additionalParameters != null 
        			&& additionalParameters.length > 0 
        			&& additionalParameters[0].equals(oauth.signpost.OAuth.OAUTH_CALLBACK)) {
        		// no-op
        	} else {
        		query.put(oauth.signpost.OAuth.OAUTH_TOKEN, consumer.getToken());
        	}
        	query.put(oauth.signpost.OAuth.OAUTH_SIGNATURE, getSignature(consumer.getConsumerKey(),
        			consumer.getConsumerSecret(), tokenSecet, requestMethod, endpointUrl, query ));
        	
        	StringBuilder sb = new StringBuilder(endpointUrl);
        	sb.append("?");
        	boolean first = true;
        	for(Entry<String, String> entry: query.entrySet()) {
        		if(!first) {
        			sb.append("&");
        		}
        		first = false;
        		sb.append(oauth.signpost.OAuth.percentEncode(entry.getKey()))
        			.append("=").append(oauth.signpost.OAuth.percentEncode(entry.getValue()));
        	}
        	connection = (HttpURLConnection) new URL(sb.toString()).openConnection();
        	connection.setRequestMethod(requestMethod);
            connection.setAllowUserInteraction(false);
            //connection.setRequestProperty("Content-Length", "0");
            connection.connect();
            response = new HttpURLConnectionResponseAdapter(connection);
            int statusCode = response.getStatusCode();

            boolean requestHandled = false;
            if (requestHandled) {
                return;
            }

            if (statusCode >= 300) {
                handleUnexpectedResponse(statusCode, response);
            }

            HttpParameters responseParams = oauth.signpost.OAuth.decodeForm(response.getContent());

            String token = responseParams.getFirst(oauth.signpost.OAuth.OAUTH_TOKEN);
            String secret = responseParams.getFirst(oauth.signpost.OAuth.OAUTH_TOKEN_SECRET);
            responseParams.remove(oauth.signpost.OAuth.OAUTH_TOKEN);
            responseParams.remove(oauth.signpost.OAuth.OAUTH_TOKEN_SECRET);
            setResponseParameters(responseParams);
            if (token == null || secret == null) {
                throw new OAuthExpectationFailedException(
                        "Request token or token secret not set in server reply. "
                                + "The service provider you use is probably buggy.");
            }
            consumer.setTokenWithSecret(token, secret);
        } catch (OAuthNotAuthorizedException e) {
            throw e;
        } catch (OAuthExpectationFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new OAuthCommunicationException(e);
        } finally {
            try {
            	if(connection != null) {
            		connection.disconnect();
            	}
            } catch (Exception e) {
            	throw new OAuthCommunicationException(e);
            }
        }
	}
	
	private String getSignature(String consumerKey, String consumerSecret,
			String tokenSecret, String method, String url,
			Map<String, String> query) throws OAuthMessageSignerException {
        try {
            String keyString = oauth.signpost.OAuth.percentEncode(consumerSecret) + '&'
                    + oauth.signpost.OAuth.percentEncode(tokenSecret);
            byte[] keyBytes = keyString.getBytes(oauth.signpost.OAuth.ENCODING);

            SecretKey key = new SecretKeySpec(keyBytes, MAC_NAME);
            Mac mac = Mac.getInstance(MAC_NAME);
            mac.init(key);

            String sbs = getSignatureBaseString(method, url, query);
            oauth.signpost.OAuth.debugOut("SBS", sbs);
            byte[] text = sbs.getBytes(oauth.signpost.OAuth.ENCODING);

            return new String(Base64.encode(mac.doFinal(text)));
        } catch (GeneralSecurityException e) {
            throw new OAuthMessageSignerException(e);
        } catch (UnsupportedEncodingException e) {
            throw new OAuthMessageSignerException(e);
        }
        
	}

	private String getSignatureBaseString(String method, String url,
			Map<String, String> query) throws OAuthMessageSignerException {
        try {
            String normalizedUrl = normalizeRequestUrl(url);
            String normalizedParams = normalizeRequestParameters(query);

            return method + '&' + oauth.signpost.OAuth.percentEncode(normalizedUrl) + '&'
                    + oauth.signpost.OAuth.percentEncode(normalizedParams);
        } catch (Exception e) {
            throw new OAuthMessageSignerException(e);
        }		
	}

	private String normalizeRequestParameters(Map<String, String> query) {
        if (query == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<String>(query.keySet());
        Collections.sort(keys);
        boolean first = true;
        for (String key: keys) {
            if (oauth.signpost.OAuth.OAUTH_SIGNATURE.equals(key) || "realm".equals(key)) {
                continue;
            }
            
            if(!first) {
            	sb.append("&");
            }
            first = false;
            sb.append(oauth.signpost.OAuth.percentEncode(key)).append('=').append(oauth.signpost.OAuth.percentEncode(query.get(key)));
        }
        return sb.toString();		
	}

	private String normalizeRequestUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String scheme = uri.getScheme().toLowerCase();
        String authority = uri.getAuthority().toLowerCase();
        boolean dropPort = (scheme.equals("http") && uri.getPort() == 80)
                || (scheme.equals("https") && uri.getPort() == 443);
        if (dropPort) {
            // find the last : in the authority
            int index = authority.lastIndexOf(":");
            if (index >= 0) {
                authority = authority.substring(0, index);
            }
        }
        String path = uri.getRawPath();
        if (path == null || path.length() <= 0) {
            path = "/"; // conforms to RFC 2616 section 3.2.2
        }
        // we know that there is no query and no fragment here.
        return scheme + "://" + authority + path;		
	}
}
