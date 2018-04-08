package com.mzba.peng;

import oauth.facebook.AsyncFacebookRunner;
import oauth.facebook.BaseRequestListener;
import oauth.facebook.DialogError;
import oauth.facebook.Facebook;
import oauth.facebook.Facebook.DialogListener;
import oauth.facebook.FacebookError;
import oauth.facebook.SessionEvents;
import oauth.facebook.SessionEvents.AuthListener;
import oauth.facebook.Util;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mzba.oauth.OAuth;
import com.mzba.peng.util.AppContext;
import com.mzba.peng.util.PlatformAccount;
import com.mzba.peng.util.PlatformBindConfig;
/**
 * 
 * @author 06peng
 * @see http://06peng.com
 *
 */
public class MainActivity extends BasicActivity implements OnClickListener, Callback {
	
	private Button sinaBtn;
	private Button tencentBtn;
	private Button qqBtn;
	private Button renrenBtn;
	private Button doubanBtn;
	private Button facebookBtn;
	private Button twitterBtn;
	
	private TextView textView;
	
	private Handler handler;
	private String platform;
	public OAuth oauth;
	public static final String CALLBACKURL = "app:authorize";
	
	public static final int oauth_sucess = 1;
	
	private Facebook mFacebook;
	private AsyncFacebookRunner mAsyncRunner;
	private SessionListener mSessionListener = new SessionListener();
	
	public ProgressDialog mSpinner;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler(this);
        initWidget();
        mSpinner = new ProgressDialog(this);
        mSpinner.setMessage("Loading...");  
    }
    
    private void initWidget() {
    	sinaBtn = (Button) findViewById(R.id.bind_sina);
    	sinaBtn.setOnClickListener(this);
    	
    	tencentBtn = (Button) findViewById(R.id.bind_tencent);
    	tencentBtn.setOnClickListener(this);
    	
    	qqBtn = (Button) findViewById(R.id.bind_qq);
    	qqBtn.setOnClickListener(this);
    	
    	renrenBtn = (Button) findViewById(R.id.bind_renren);
    	renrenBtn.setOnClickListener(this);
    	
    	doubanBtn = (Button) findViewById(R.id.bind_douban);
    	doubanBtn.setOnClickListener(this);
    	
    	facebookBtn = (Button) findViewById(R.id.bind_facebook);
    	facebookBtn.setOnClickListener(this);
    	
    	twitterBtn = (Button) findViewById(R.id.bind_twitter);
    	twitterBtn.setOnClickListener(this);
    	
    	textView = (TextView) findViewById(R.id.bind_content);
    }
    

	@Override
	public void onClick(View v) {
		mSpinner.show();
		if (v == sinaBtn) {
			platform = PlatformBindConfig.Sina;
			oauth = new OAuth(this, handler, platform);
			oauth.requestAccessTokenForOAuth2(PlatformBindConfig.Sina_Authorize2);
		} else if (v == tencentBtn) {
			platform = PlatformBindConfig.Tencent;
			oauth = new OAuth(this, handler, platform);
			oauth.requestAccessTokenForOAuth2(PlatformBindConfig.Tencent_Authorize_2);
		} else if (v == qqBtn) {
			platform = PlatformBindConfig.QQ;
			oauth = new OAuth(this, handler, platform);
			oauth.requestAccessTokenForOAuth2(PlatformBindConfig.QQ_Authorize);
		} else if (v == renrenBtn) {
			platform = PlatformBindConfig.Renren;
			oauth = new OAuth(this, handler, platform);
			oauth.requestAccessTokenForOAuth2(PlatformBindConfig.Renren_Authorize);
		} else if (v == doubanBtn) {
			platform = PlatformBindConfig.Douban;
			oauth = new OAuth(this, handler, platform, PlatformBindConfig.Douban_AppKey,
					PlatformBindConfig.Douban_AppSecret);
			oauth.requestAccessToken(CALLBACKURL, PlatformBindConfig.Douban_Request_Token,
					PlatformBindConfig.Douban_Access_Token, PlatformBindConfig.Douban_Authorize);
		} else if (v == facebookBtn) {
			platform = PlatformBindConfig.Facebook;
			mFacebook = new Facebook(PlatformBindConfig.Facebook_AppID);
			mAsyncRunner = new AsyncFacebookRunner(mFacebook);
			SessionEvents.addAuthListener(new SampleAuthListener());
			SessionEvents.addAuthListener(mSessionListener);
			mFacebook.authorize((BasicActivity) AppContext.getContext(), new String[] {}, new LoginDialogListener());
		} else if (v == twitterBtn) {
			platform = PlatformBindConfig.Twitter;
			oauth = new OAuth(this, handler, PlatformBindConfig.Twitter,
					PlatformBindConfig.Twitter_ConsumerKey, PlatformBindConfig.Twitter_ConsumerSecret);
			oauth.requestAcccessTokenForTwitter();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case oauth_sucess:
			mSpinner.hide();
			PlatformAccount account = (PlatformAccount) msg.obj;
			if (account != null) {
				textView.setText("昵称：" + account.getNickName() + "；UID:" + account.getOpenUid() + 
						"；头像地址:" + account.getOpenAvatar() + ";accesstoken:" + account.getAccessToken());
			}
			break;

		default:
			break;
		}
		return false;
	}
	
	public class SampleAuthListener implements AuthListener {

		public void onAuthSucceed() {
			System.out.println("You have logged in! ");
		}

		public void onAuthFail(String error) {
			System.out.println("Login Failed: " + error);
		}
	}

	public class SampleRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			try {
				PlatformAccount account = new PlatformAccount();
				Log.d("Facebook-Example", "Response: " + response.toString());
				JSONObject json = Util.parseJson(response);
				account.setAccessToken(mFacebook.getAccessToken());
				if (json.has("name")) {
					account.setNickName(json.getString("name"));
				}
				account.setOpenAvatar("https://graph.facebook.com/"+json.getString("id")+"/picture?type=large");
				account.setOpenExpire(String.valueOf(mFacebook.getAccessExpires()));
				account.setOpenType(6);
				account.setOpenUid(json.getString("id"));
			} catch (JSONException e) {
				e.printStackTrace();
				Log.w("Facebook-Example", "JSON Error in response");
			} catch (FacebookError e) {
				Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
			}
		}
	}

	private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }
	
	private class SessionListener implements AuthListener {
        
        public void onAuthSucceed() {
            mAsyncRunner.request("me", new SampleRequestListener());
        }
        
        public void onAuthFail(String error) {
        	
        }
    }

}
