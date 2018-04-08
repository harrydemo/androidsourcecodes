package com.lqf.shezhi;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.lqf.gerenriji.R;

public class GongJuXiang extends Activity {
	//定义控件
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置布局标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gongjuxiang);
		// 获取自定义标题布局
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.gongjubiaoti);
		//获取控件
	
	}

}
