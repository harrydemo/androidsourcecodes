package com.mzba.oauth;

import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.http.HttpParameters;
/**
 * 
 * @author 06peng
 *
 */
public class DoubanOAuthConsumer extends DefaultOAuthConsumer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoubanOAuthConsumer(String consumerKey, String consumerSecret) {
		super(consumerKey, consumerSecret);
		HttpParameters parameters = new HttpParameters();
		parameters.put("realm", "");
		setAdditionalParameters(parameters);
	}
	
}
