package com.oauthTest.activity;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.oauthTest.R;
import com.oauthTest.data.OAuth;
import com.oauthTest.utils.ConfigUtil;

/**
 * 展示AccessToken
 * 
 * 		1. 获取授权后成功后回调的URL并匹配出验证码
 *  		（网易无验证码不处理）
 *  	2.获取OAuth对象并设置验证码
 *  	3.获取accessToken
 *  
 * @author bywyu
 *
 */
public class ShowAccessTokenAct extends Activity{
	
	private final String LOGTAG = "ShowAccessTokenAct";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.showaccesstoken_ui);
	    
	    String url = getIntent().getStringExtra(ConfigUtil.OAUTH_VERIFIER_URL);
    	Uri uri = Uri.parse(url);
    	//匹配验证码
		String oauth_verifier = uri.getQueryParameter("oauth_verifier");
		
		OAuth oAuth = OAuth.getInstance();
		oAuth.setOauthVerifier(oauth_verifier);
		String accessToken = oAuth.getAccessToken();	
		
		String oauth_token = oAuth.getOauth_token();
		String oauth_token_secret = oAuth.getOauth_token_secret();
		
	    TextView textView = (TextView)findViewById(R.id.accessTokenView);
	    textView.setTextColor(Color.RED);
	    textView.setTextSize(20);
		textView.setText( "oauth_verifier = "+oauth_verifier+" \noauth_token = "+oauth_token+"\noauth_token_secret = "+oauth_token_secret
				+"\naccessToken = "+accessToken);
		System.out.println("accessToken = "+accessToken);
    }
	
}
