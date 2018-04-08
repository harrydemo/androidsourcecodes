package com.tiger.activitygroupdome;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 用来创建管理此模块的界面 .
 * 每个模块,都需要这样创建一个管理的界面,其余的模块就不掩饰了,!
 * @author HuYang
 * 
 */
public class GotoMianActicity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 此模块的第一个主界面,跳转到那个界面,下面第一个参数就要写那个界面,
		// 你可以去看此方法,会把这个界面添加的集合.
		startChildActivity("Activity01_A", new Intent(GotoMianActicity.this,
				Activity01_A.class));
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchKeyEvent(event);
	}
}
