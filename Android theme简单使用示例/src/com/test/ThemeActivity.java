package com.test;

import android.app.Activity;
import android.os.Bundle;

public class ThemeActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Ӧ��͸������������
		setTheme(R.style.Theme_Transparent);

		// Ӧ�ò�������1
		setTheme(R.style.Theme_Translucent);

		// Ӧ�ò�������2
		setContentView(R.layout.main);
	}
}