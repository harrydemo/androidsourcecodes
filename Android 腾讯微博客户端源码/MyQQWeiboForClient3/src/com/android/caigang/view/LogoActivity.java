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
				if(userList.size()<1||userList==null){//������ݿ�û�д���һ���û��Ļ���ô������Ȩ���棬����û�
					Intent it=new Intent(LogoActivity.this,AccountActivity.class);
					startActivity(it);
					finish();
				}else if("".equals(nick)){//������ݿ�����û�����û��ѡ��Ĭ�ϵ�¼���û��Ļ�������ѡ��Ĭ�ϵ�¼�û�����
					Intent it=new Intent(LogoActivity.this,AccountActivity.class);
					startActivity(it);
					finish();
				}else{//������ݿ�����û����ұ�����Ĭ�ϵ��û��Ļ�,��ô������΢���û���Ĭ��������
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
