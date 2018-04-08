package com.android.weibo;
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

import com.android.datastore.DatabaseManager;
import com.sph.player.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.*;
import weibo4android.Weibo;
import weibo4android.WeiboException;

public class WeiboActivitySend extends Activity
{

	private Button btnAt;
	private Button btnSharp;
	private Context ct;
	private Button my_send_btn;
	private ImageView returnBack;
	private EditText send_text_editor;
	String share_weibo_url;
	String twintterToken;
	String twintterTokenSecret;

	public WeiboActivitySend()
	{
		twintterToken = "";
		twintterTokenSecret = "";
		share_weibo_url = "";
		btnAt = null;
		btnSharp = null;
	}

	//初始化web页面
	private void InitWebPage(){
		ct = this;
		btnAt = (Button)findViewById(R.id.at_samples);
		btnAt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				String s = String.valueOf(send_text_editor.getText().toString());
				String s1 = (new StringBuilder(s)).append("@").toString();
				send_text_editor.setText(s1);
				
				EditText edittext = send_text_editor;
				int i = s1.length();
				edittext.setSelection(i);
			}
		});
		
		btnSharp = (Button)findViewById(R.id.sharp_samples);
		btnSharp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				String s = String.valueOf(send_text_editor.getText().toString());
				String s1 = (new StringBuilder(s)).append("#").toString();
				send_text_editor.setText(s1);
				EditText edittext = send_text_editor;
				int i = s1.length();
				edittext.setSelection(i);
			}
		});

		send_text_editor = (EditText)findViewById(R.id.send_text_editor);

		my_send_btn = (Button)findViewById(R.id.my_send_btn);
		my_send_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				SendToTwintter();
				setResult(3);
				finish();
			}
		});
		
		returnBack = (ImageView)findViewById(R.id.my_edit_button);
		returnBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view){
				DatabaseManager.getInstance(ct).removeTwintterToken();
				setResult(3);
				finish();
			}
		});
	}

	//发送到微博
	public void SendToTwintter(){
		
		try {
			Weibo weibo = OAuthConstant.getInstance().getWeibo();
			weibo.setToken(twintterToken, twintterTokenSecret);
			String s2 = String.valueOf(send_text_editor.getText().toString());
			StringBuilder stringbuilder = new StringBuilder(s2);
			StringBuilder stringbuilder1 = stringbuilder
					.append(share_weibo_url);
			String s4 = getResources().getString(R.string.success_message_3);
			String s5 = String.valueOf(stringbuilder1.append(s4).toString());
			StringBuilder stringbuilder2 = new StringBuilder(s5);
			String s6 = getResources().getString(R.string.success_message_2);
			String s7 = stringbuilder2.append(s6).toString();
			weibo4android.Status status = weibo.updateStatus(s7);
			Context context = ct;
			String s8 = getResources().getString(R.string.success_message_1);
			Toast.makeText(context, s8, 0).show();
		} catch (Exception e) {
			// TODO: handle exception
			Context context1 = ct;
			String s9 = getResources().getString(R.string.err_message_1);
			Toast.makeText(context1, s9, 0).show();
			e.printStackTrace();
		}

	}

	//activity的创建
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.activity_weibo_send);
		
		Bundle bundle1 = getIntent().getExtras();
		share_weibo_url = bundle1.getString("WEIBO_SHARE_URL");

		twintterToken = bundle1.getString("WEIBO_TOKEN");

		twintterTokenSecret = bundle1.getString("WEIBO_TOKENSECRET");

		//初始化web页面
		InitWebPage();
	}


}
