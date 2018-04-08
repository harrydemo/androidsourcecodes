package com.rss.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity {
	private Button loginButton;
	private TextView loginText;
	private ImageView imagView;
	private static int i_alpha = 255;
	private Handler mHandler = new Handler();
	boolean isShow = false;
	private Thread thread;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		loginButton = (Button)findViewById(R.id.login_in);
		loginText = (TextView)findViewById(R.id.logining);
		
		isShow = true;	
		imagView = (ImageView)findViewById(R.id.login_rss);
		imagView.setAlpha(i_alpha);
				
		//������Ϣ֮�����
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {				
				super.handleMessage(msg);
				imagView.setAlpha(i_alpha);
				imagView.invalidate();
			}
			
		};
		
		//����һ���̣߳����ڸ���alphaֵ
		thread = new Thread(new Runnable() {
			
			public void run() {
				while(isShow) {
					try {
						Thread.sleep(100);
						updateAlpha();						
					}catch (InterruptedException  e) {
						e.printStackTrace();
					}
				}				
			}
			
		});
				
		loginButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(LoginActivity.this, ActivityMain.class);
				//�����߳�
				thread.start();
				
				loginButton.setVisibility(v.INVISIBLE);
				loginText.setVisibility(v.VISIBLE);				
			}
		});
	}
	
	protected void updateAlpha() {
		if((i_alpha-5) >= 0) {
			i_alpha = i_alpha - 5;		
			
		}else {
			System.out.println("i_alpha" + i_alpha);
			i_alpha = 0;
			isShow = false;
			startActivity(intent);	
		}
		
		mHandler.sendMessage(mHandler.obtainMessage()); 
	}
}
