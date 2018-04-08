package com.test;

import android.app.Activity;
import android.os.Bundle;

public class ThemeActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// 应用透明背景的主题
		setTheme(R.style.Theme_Transparent);

		// 应用布景主题1
		setTheme(R.style.Theme_Translucent);

		// 应用布景主题2
		setContentView(R.layout.main);
	}
}