package com.android.weibo;


import com.sph.player.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;

/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
public class OAuthActivity extends Activity
{

	public OAuthActivity(){
		
	}

	public void onCreate(Bundle bundle){
		super.onCreate(bundle);

		setContentView(R.layout.timeline);
		Uri uri = getIntent().getData();
		
		try {
			RequestToken requesttoken = OAuthConstant.getInstance()
					.getRequestToken();
			String s = uri.getQueryParameter("oauth_verifier");
			AccessToken accesstoken = requesttoken.getAccessToken(s);
			OAuthConstant.getInstance().setAccessToken(accesstoken);
			TextView textview = (TextView) findViewById(R.id.TextView01);
			StringBuilder stringbuilder = new StringBuilder(
					"得到AccessToken的key和Secret,可以使用这两个参数进行授权登录了.\n Access token:\n");
			String s1 = accesstoken.getToken();
			StringBuilder stringbuilder1 = stringbuilder.append(s1).append(
					"\n Access token secret:\n");
			String s2 = accesstoken.getTokenSecret();
			String s3 = stringbuilder1.append(s2).toString();
			textview.setText(s3);
			Button button = (Button) findViewById(R.id.Button01);
			button.setText("我来发条微博");
			button.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					Weibo weibo;

					try {
						weibo = OAuthConstant.getInstance().getWeibo();
						String s = OAuthConstant.getInstance().getToken();
						String s1 = OAuthConstant.getInstance()
								.getTokenSecret();
						weibo.setToken(s, s1);
						weibo4android.Status status = weibo
								.updateStatus("这个消息是用调用新浪微博API通过手机发送出来的");
					} catch (Exception e) {
						e.printStackTrace();
						return;
						// TODO: handle exception
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return;
			// TODO: handle exception
		}

	}

}
