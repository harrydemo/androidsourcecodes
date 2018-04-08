package com.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class Demo extends Activity {
	
	public static final String SETTING_INFOS = "SETTING_Infos";

	public static final String NAME = "NAME";

	public static final String PASSWORD = "PASSWORD";

	private EditText field_name; // 接收用户名的组件
	private EditText filed_pass; // 接收密码的组件

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		field_name = (EditText) findViewById(R.id.name); // 首先获取用来输入用户名的组件
		filed_pass = (EditText) findViewById(R.id.password); // 同时也需要获取输入密码的组件
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0); // 获取一个SharedPreferences对象

		String name = settings.getString(NAME, ""); // 取出保存的NAME
		String password = settings.getString(PASSWORD, ""); // 取出保存的PASSWORD

		// Set value
		field_name.setText(name); // 将取出来的用户名赋予field_name
		filed_pass.setText(password); // 将取出来的密码赋予filed_pass

	}

	protected void onStop() {

		super.onStop();
		// 首先获取一个SharedPreferences对象
		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0); 
		// 将用户名和密码保存进去
		SharedPreferences.Editor editor = settings.edit();

		editor.putString( NAME, field_name.getText().toString() );
		editor.putString( PASSWORD, filed_pass.getText().toString() );
		editor.commit();
	} 

}