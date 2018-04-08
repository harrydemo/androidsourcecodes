package com.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class Demo extends Activity {
	
	public static final String SETTING_INFOS = "SETTING_Infos";

	public static final String NAME = "NAME";

	public static final String PASSWORD = "PASSWORD";

	private EditText field_name; // �����û��������
	private EditText filed_pass; // ������������

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		field_name = (EditText) findViewById(R.id.name); // ���Ȼ�ȡ���������û��������
		filed_pass = (EditText) findViewById(R.id.password); // ͬʱҲ��Ҫ��ȡ������������
		// Restore preferences
		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0); // ��ȡһ��SharedPreferences����

		String name = settings.getString(NAME, ""); // ȡ�������NAME
		String password = settings.getString(PASSWORD, ""); // ȡ�������PASSWORD

		// Set value
		field_name.setText(name); // ��ȡ�������û�������field_name
		filed_pass.setText(password); // ��ȡ���������븳��filed_pass

	}

	protected void onStop() {

		super.onStop();
		// ���Ȼ�ȡһ��SharedPreferences����
		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0); 
		// ���û��������뱣���ȥ
		SharedPreferences.Editor editor = settings.edit();

		editor.putString( NAME, field_name.getText().toString() );
		editor.putString( PASSWORD, filed_pass.getText().toString() );
		editor.commit();
	} 

}