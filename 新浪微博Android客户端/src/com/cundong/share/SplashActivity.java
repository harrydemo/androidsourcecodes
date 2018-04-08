package com.cundong.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

/** 
 * 类说明：   乐分享 启动界面
 * @author  @Cundong
 * @weibo   http://weibo.com/liucundong
 * @blog    http://www.liucundong.com
 * @date    Aug 26, 2012 2:50:48 PM
 * @version 1.0
 */
public class SplashActivity extends Activity 
{
	private Context mContext = null;
	private boolean active = true;
	private int splashTime = 1500;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		this.mContext = this.getApplicationContext();
		
		new Thread() 
		{
			@Override
			public void run() 
			{
				try
				{
					int waited = 0;
					while (active && (waited < splashTime)) 
					{
						sleep(100);
						
						if (active) 
						{
							waited += 100;
						}
					}
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				} 
				finally 
				{
					finish();
					
					Intent intent = new Intent();
					intent.setClass(mContext, ShareMainActivity.class);
					startActivity(intent);
				}
			}
		}.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}