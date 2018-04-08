package com.oauthTest.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

import com.oauthTest.utils.Base64Encoder;
import com.oauthTest.utils.ConfigUtil;
import com.oauthTest.utils.HttpClientUtil;


public class OAuth {
	private Random random = new Random();
	private String encoding = "utf-8"; //URL编码方式
	private String oauth_request_method = "GET"; // 请求方法
	private String requst_token_url; // 请求url
	private String oauth_consumer_key; // —— 注册应用后由应用服务商提供
	private String consumer_secret; // —— 注册应用后由应用服务商提供
	private String oauth_token;
	private String oauth_token_secret;
	private String oauth_verifier; //验证码
	private String oauth_callback = ""; // —— 用户授权后的返回地址
	private String oauth_nonce; // —— 随机字符串，须保证每次都不同 （32个字符长度）
	private String oauth_timestamp; // —— 时间戳
	private String oauth_signature_method = "HMAC-SHA1"; // —— 签名base string 的方法，目前支持
	// HMAC-SHA1
	private String oauth_version = "1.0"; // —— Oauth协议版本
	private String oauth_signature; // oauth请求签 名
	
	private static OAuth instance;

	public void setOauthTokenAndOauthTokenSercet(String oauth_token,String oauth_token_secret,String oauth_verifier){
		this.oauth_token = oauth_token;
		this.oauth_token_secret = oauth_token_secret;
		this.oauth_verifier = oauth_verifier;
	}
	
	/**
	 * 初始化oauth_token、oauth_token_secret、oauth_verifier
	 */
	public void clear(){
		this.oauth_token = null;
		this.oauth_token_secret = null;
		this.oauth_verifier = null;
	}
	
	public static synchronized OAuth getInstance(){
		if(instance == null){
			instance = new OAuth();
		}
		return instance;
	} 
	
	public OAuth() {

	}
	
	public void setKeyAndSecret(String key,String secret){		
		this.oauth_consumer_key = key;
		this.consumer_secret = secret;	
	}
	
	/**
	 * 拼接请求的url
	 * 	
	 * @return Query String
	 * @throws Exception
	 */
	public String getOauthUrl() throws Exception{
		this.oauth_nonce = getAauth_nonce();
		this.oauth_timestamp = getAauth_timestamp();
		String baseUrlString = this.getBaseString();
		this.oauth_signature = this.getAauth_signature(baseUrlString);
		return requst_token_url + "?"+baseUrlString + "&oauth_signature=" + URLEncoder.encode(oauth_signature,encoding);
	}
	
	/**
	 * 获取随机的 oauth_nonce
	 * 
	 * @return
	 */
	private String getAauth_nonce() {
		return String.valueOf(random.nextInt(9876599) + 123400);
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	private String getAauth_timestamp() {
		return String.valueOf(System.currentTimeMillis()).substring(0, 10);
	}

	/**
	 * 拼接所有参数
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getBaseString() throws UnsupportedEncodingException {
		
		String bsss = "";
		String curWeibo = ConfigUtil.getInstance().getCurWeibo();
		
		if((oauth_verifier == null || oauth_verifier.length() == 0) && !curWeibo.equals(ConfigUtil.WANGYIW)){
			bsss +="oauth_callback=" + URLEncoder.encode(ConfigUtil.callBackUrl, encoding) + "&";
		}
		bsss += "oauth_consumer_key=" + URLEncoder.encode(oauth_consumer_key, encoding);
		bsss += "&oauth_nonce=" + URLEncoder.encode(oauth_nonce, encoding);
		bsss += "&oauth_signature_method=" + URLEncoder.encode(oauth_signature_method, encoding); 
		bsss += "&oauth_timestamp=" + URLEncoder.encode(oauth_timestamp, encoding);
		
		if(oauth_token != null && (oauth_token.length() > 0)){
			bsss += "&oauth_token=" + URLEncoder.encode(oauth_token, encoding); 
		}
		if(oauth_verifier != null && oauth_verifier.length() >0){
			bsss += "&oauth_verifier=" + URLEncoder.encode(oauth_verifier, encoding); 
		}		
				
		bsss += "&oauth_version=" + URLEncoder.encode(oauth_version, encoding);
		
		return bsss;
	}

	/**
	 * 获取 oauth_signature
	 * @param data
	 * @param key
	 * @return
	 */
	public String getAauth_signature(String data) throws Exception{
		byte[] byteHMAC = null;
		try {
			String bss = oauth_request_method + "&" + URLEncoder.encode(requst_token_url, encoding) + "&";
			String bsss = URLEncoder.encode(data, encoding);
			String urlString = bss +bsss;
			
			String oauthKey =  URLEncoder.encode(consumer_secret, encoding) + "&"
			+ ((oauth_token_secret == null || oauth_token_secret.equals("")) ? "" : URLEncoder.encode(oauth_token_secret,encoding));
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(oauthKey.getBytes("US-ASCII"), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal((urlString).getBytes("US-ASCII"));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ignore) {
			
		}
		
		String oauthSignature = new Base64Encoder().encode(byteHMAC);
		return oauthSignature;
	}

	
	
	public static void main(String[] args) throws Exception {
		OAuth oAuth = OAuth.getInstance();
		oAuth.setKeyAndSecret("3c22f8387ac8431596314e3c62d376ab","222079b842a62131b41729206d1e6c5c");

		String oauthToken = oAuth.getOauthToken();
		System.out.println("oauthToken="+oauthToken);	
		
//		if(oauthToken != null){
//			System.out.println("https://open.t.qq.com/cgi-bin/authorize?"+oauthToken);	
//		}
		
//		oAuth.setOauthTokenAndOauthTokenSercet("35d04d56e2cf480dbe36838742cd43d4","0de5f74323c7fc83e6dfa7ea92ec322b","952828");
//		String accessToken = oAuth.getAccessToken();
//		System.out.println("accessToken="+accessToken);	
	}
	
	/**
	 * 获取 oauth_token
	 * @return
	 */
	private String getOauthToken(){
		String oauthToken = null;
		this.requst_token_url = ConfigUtil.getInstance().getRequest_token_url();
		System.out.println("getOauthToken() [1] requst_token_url = "+requst_token_url);
		try {
			
			String url = this.getOauthUrl();
			System.out.println("getOauthToken() [2] getOauthUrl() = "+url);	
			oauthToken = HttpClientUtil.get(url,"utf-8");
			System.out.println("getOauthToken() [3] oauthToken  = "+oauthToken);	
		} catch (Exception e) {
            e.printStackTrace();
        }
        
        return oauthToken;
	}
	
	/**
	 * 获取AccessToken
	 * 
	 * @return
	 */
	public String getAccessToken(){
		String oauthToken = null;
		this.requst_token_url = ConfigUtil.getInstance().getAccess_token_url();
		
		try {
			String url = this.getOauthUrl();
			System.out.println("getAccessToken() url="+url);	
			oauthToken = HttpClientUtil.get(url,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getAccessToken() Exception");	
        }
        
        return oauthToken;
	}

	public void setOauth_callback(String oauthCallback) {
		oauth_callback = oauthCallback;
	}
	
	/**
	 * 获取授权URL
	 * 		用户登录页面地址
	 * @return 授权URL
	 */
	public String getAuthorizUrl(){
		
		String oauthToken =this. getOauthToken();
	    
	    try {
	    	String[] oauth = oauthToken.split("&");
			String[] oauthTokenTmp = oauth[0].split("=");
			oauth_token = oauthTokenTmp[1];
			String tmp = oauth[1];
			String[] tmps = tmp.split("=");
			oauth_token_secret = tmps[1];
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        String token = "oauth_token="+oauth_token;
		String url = ConfigUtil.getInstance().getAuthoriz_token_url()+"?"+token;
		String curWeibo = ConfigUtil.getInstance().getCurWeibo();
		try {
			if(curWeibo.equals(ConfigUtil.WANGYIW) || curWeibo.equals(ConfigUtil.SOHUW)){
		            url = ConfigUtil.getInstance().getAuthoriz_token_url()+"?"+token+"&"+
		            	"oauth_callback=" + URLEncoder.encode(ConfigUtil.callBackUrl, "utf-8") +
		            	"&client_type=mobile";
			}
		} catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return url;
	}
	
	/**
	 *  设置验证码
	 * @param oauthVerifier 验证码
	 */
	public void setOauthVerifier(String oauthVerifier) {
	    this.oauth_verifier = oauthVerifier;
    }
	

	public String getOauth_token() {
    	return oauth_token;
    }

	public void setOauth_token(String oauthToken) {
    	oauth_token = oauthToken;
    }

	public String getOauth_token_secret() {
    	return oauth_token_secret;
    }

	public void setOauth_token_secret(String oauthTokenSecret) {
    	oauth_token_secret = oauthTokenSecret;
    }
	
}
