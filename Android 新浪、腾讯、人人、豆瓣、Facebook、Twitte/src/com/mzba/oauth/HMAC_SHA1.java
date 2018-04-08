package com.mzba.oauth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author 06peng
 *
 */
public class HMAC_SHA1 {
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "US-ASCII";

	/**
	 * ä½¿ç? HMAC-SHA1 ç­¾å??¹æ?å¯¹å?encryptTextè¿??ç­¾å?
	 * @param encryptText è¢?????å­??ä¸?
	 * @param encryptKey å¯??
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws InvalidKeyException
	 * @see <a href =
	 *      "http://tools.ietf.org/html/draft-hammer-oauth-10#section-3.4.2">HMAC-SHA1</a>
	 */
	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		Mac mac = Mac.getInstance(MAC_NAME);
		SecretKeySpec spec = new SecretKeySpec(encryptKey.getBytes("US-ASCII"), MAC_NAME);
		mac.init(spec);
		byte[] text = encryptText.getBytes(ENCODING);
		return mac.doFinal(text);
	}
}
