package cn.com.hh.view;

import cn.com.hh.domian.UserInfo;
import cn.com.hh.oauth.OAuth;
import cn.com.hh.sqlite.DataHelper;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AuthorizeActivity extends Activity {
	private Dialog dialog;
	private OAuth auth;
	private static final String CallBackUrl = "myapp://AuthorizeActivity";
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorize);
		
		
		View diaView=View.inflate(this, R.layout.dialog, null);
		dialog = new Dialog(AuthorizeActivity.this,R.style.dialog);
		dialog.setContentView(diaView);
		dialog.show();
		
		ImageButton stratBtn=(ImageButton)diaView.findViewById(R.id.btn_start);
        stratBtn.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0) {
                auth=new OAuth("30632531","f539cb169860ed99cf8c1861c5da34f6");
                auth.RequestAccessToken(AuthorizeActivity.this, CallBackUrl);
            }
            
        });

	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	        super.onNewIntent(intent);
	        //在这里处理获取返回的oauth_verifier参数
	        UserInfo user= auth.GetAccessToken(intent);
	        if(user!=null){
	                    DataHelper helper=new DataHelper(this);
	                    String uid=user.getUserId();
	                    if(helper.HaveUserInfo(uid))
	                    {
	                        helper.UpdateUserInfo(user);
	                        Log.e("UserInfo", "update");
	                    }else
	                    {
	                        helper.SaveUserInfo(user);
	                        Log.e("UserInfo", "add");
	                    }
	                }

	}

}
