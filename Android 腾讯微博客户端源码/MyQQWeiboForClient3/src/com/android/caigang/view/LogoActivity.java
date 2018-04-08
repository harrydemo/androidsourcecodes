package com.android.caigang.view;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.android.caigang.R;
import com.android.caigang.db.DataHelper;
import com.android.caigang.model.UserInfo;
import com.android.caigang.util.DataBaseContext;
import com.android.caigang.util.WeiboContext;
import com.mime.qweibo.examples.MyWeiboSync;

public class LogoActivity extends Activity {
	private DataHelper dataHelper;
	private List<UserInfo> userList;
	private MyWeiboSync weibo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		
		dataHelper = DataBaseContext.getInstance(getApplicationContext());
		userList = dataHelper.GetUserList(false);
		
		SharedPreferences preferences = getSharedPreferences("default_user",Activity.MODE_PRIVATE);
		final String nick = preferences.getString("user_default_nick", "");
		
		ImageView imageView=(ImageView)this.findViewById(R.id.logo_bg);
		imageView.setImageResource(R.drawable.login_first);
		AlphaAnimation aa=new AlphaAnimation(0.1f,1.0f);
		aa.setDuration(5000);
		imageView.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener(){
			@Override
			public void onAnimationEnd(Animation arg0){
				if(userList.size()<1||userList==null){//如果数据库没有存在一个用户的话那么跳往授权界面，添加用户
					Intent it=new Intent(LogoActivity.this,AccountActivity.class);
					startActivity(it);
					finish();
				}else if("".equals(nick)){//如果数据库存在用户但是没有选择默认登录的用户的话，跳往选择默认登录用户界面
					Intent it=new Intent(LogoActivity.this,AccountActivity.class);
					startActivity(it);
					finish();
				}else{//如果数据库存在用户并且保存了默认的用户的话,那么跳往次微博用户的默认主界面
					Intent it=new Intent(LogoActivity.this,MainActivity.class);
					startActivity(it);
					finish();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
		}
		);
	}
}
