package com.chuguangming;

import com.chuguangming.KickFlybug.Until.ActivityUtil;
import com.chuguangming.KickFlybug.View.GameMainView;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class GameMainActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// 全屏并且没有Title
		ActivityUtil.fullLandScapeScreen(this);
		// 得到屏幕的相关信息并且保存在ActivityUtil中的静态变量中
		ActivityUtil.initScreenData(this);
		// debug为1是选择输出调试信息,为2是选择输出主界面
		int debug = 2;
		switch (debug)
		{
		case 1:
			setContentView(R.layout.main);
			// 得到屏幕分辨率
			GetScreenInfo();
			break;
		case 2:
			setContentView(new GameMainView(this));
			break;
		}

	}

	/**
	 * 得到屏幕分辨率
	 * */
	private void GetScreenInfo()
	{
		TextView textview1 = (TextView) findViewById(R.id.textview1);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		// 获得手机的宽带和高度像素单位为px
		String str = "手机屏幕分辨率为:" + dm.widthPixels + " * " + dm.heightPixels
				+ "--" + String.valueOf(ActivityUtil.SCREEN_WIDTH) + "*"
				+ String.valueOf(ActivityUtil.SCREEN_HEIGHT);
		textview1.setText(str);
	}
}