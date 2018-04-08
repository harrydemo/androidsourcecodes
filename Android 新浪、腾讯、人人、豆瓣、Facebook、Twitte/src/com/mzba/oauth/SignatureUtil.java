package com.mzba.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/**
 * 
 * @author 06peng
 *
 */
public class SignatureUtil {
	
	public static final String ENCODING = "UTF-8";
	
	/**
	 * äº§ç?ç­¾å???
	 * @param method
	 * @param url
	 * @param params
	 * @param consumerSecret
	 * @param tokenSecret
	 * @return
	 * @throws Exception
	 */
	public static String generateSignature(String method, String url,
			List<Parameter> params, String consumerSecret, String tokenSecret)
			throws Exception {
		
		// ?·å?æº?¸²
		String signatureBase = generateSignatureBase(method, url, params);
		// ???å¯??
		String oauthKey = "";
		if (null == tokenSecret || tokenSecret.equals("")) {
			oauthKey = encode(consumerSecret) + "&";
		} else {
			oauthKey = encode(consumerSecret) + "&" + encode(tokenSecret);
		}
		// ???ç­¾å???
		byte[] encryptBytes = HMAC_SHA1
				.HmacSHA1Encrypt(signatureBase, oauthKey);
		return Base64.encode(encryptBytes);
	}
	
	/**
	 * ???æº?¸²ï¼?TTPè¯·æ??¹å? & urlencode(url) & urlencode(a=x&b=y&...)
	 * @param method
	 * @param url
	 * @param params
	 * @return
	 */
	public static String generateSignatureBase(String method, String url, List<Parameter> params) {
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(method.toUpperCase() + "&");
		url = encode(url.toLowerCase());
		urlBuilder.append(url + "&");
		// ????????eyè¿??å­????????
		Collections.sort(params);
		for (Parameter param : params) {
			String name = encode(param.getName());
			String value = encode(param.getValue());
			urlBuilder.append(name);
			urlBuilder.append("%3D"); // å­?? =
			urlBuilder.append(value);
			urlBuilder.append("%26"); // å­?? &
		}
		// ?????°¾??26
		urlBuilder.delete(urlBuilder.length() - 3, urlBuilder.length());
		return urlBuilder.toString();
	}

	/**
	 * ??????
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		if (s == null) {
			return "";
		}
		String encoded = "";
		try {
			encoded = URLEncoder.encode(s, ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < encoded.length(); i++) {
			char c = encoded.charAt(i);
			if (c == '+') {
				sBuilder.append("%20");
			} else if (c == '*') {
				sBuilder.append("%2A");
			}
			// URLEncoder.encode()ä¼??????½¿?¨â?%7E??¡¨ç¤ºï?????¨è????ä»??è¦?????~??
			else if ((c == '%') && ((i + 1) < encoded.length())
					&& ((i + 2) < encoded.length())
					& (encoded.charAt(i + 1) == '7')
					&& (encoded.charAt(i + 2) == 'E')) {
				sBuilder.append("~");
				i += 2;
			} else {
				sBuilder.append(c);
			}
		}
		return sBuilder.toString();
	}
	
	/**
	 * å°??å¯??????°æ???
	 * @param value
	 * @return
	 */
	public static String decode(String value) {
		int nCount = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '%') {
				i += 2;
			}
			nCount++;
		}

		byte[] sb = new byte[nCount];

		for (int i = 0, index = 0; i < value.length(); i++) {
			if (value.charAt(i) != '%') {
				sb[index++] = (byte) value.charAt(i);
			} else {
				StringBuilder sChar = new StringBuilder();
				sChar.append(value.charAt(i + 1));
				sChar.append(value.charAt(i + 2));
				sb[index++] = Integer.valueOf(sChar.toString(), 16).byteValue();
				i += 2;
			}
		}
		String decode = "";
		try {
			decode = new String(sb, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decode;
	}
	
	/**
	 * äº§ç??¶é???
	 * @return
	 */
	public static String generateTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * äº§ç??????
	 * @param is32 äº§ç?å­??ä¸²é?åº????¸º32ä½?
	 * @return
	 */
	public static String generateNonce(boolean is32) {
		Random random = new Random();
		// äº§ç?123400??999999?????
		String result = String.valueOf(random.nextInt(9876599) + 123400);
		if (is32) {
			// è¿??MD5???
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(result.getBytes());
				byte b[] = md.digest();
				int i;

				StringBuffer buf = new StringBuffer("");
				for (int offset = 0; offset < b.length; offset++) {
					i = b[offset];
					if (i < 0)
						i += 256;
					if (i < 16)
						buf.append("0");
					buf.append(Integer.toHexString(i));
				}
				result = buf.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static List<Parameter> getQueryParameters(String queryString) {
		if (queryString.startsWith("?")) {
			queryString = queryString.substring(1);
		}
		List<Parameter> result = new ArrayList<Parameter>();
		if (queryString != null && !queryString.equals("")) {
			String[] p = queryString.split("&");
			for (String s : p) {
				if (s != null && !s.equals("")) {
					if (s.indexOf('=') > -1) {
						String[] temp = s.split("=");
						result.add(new Parameter(temp[0], temp[1]));
					}
				}
			}
		}
		return result;
	}
}
