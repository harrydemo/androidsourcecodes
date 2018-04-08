package com.lqf.riji;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.lqf.gerenriji.R;
import com.lqf.riji.duanxin.Tab1;
import com.lqf.riji.duanxin.Tab2;
import com.lqf.riji.duanxin.Tab3;
import com.lqf.riji.duanxin.Tab4;

public class DuanXin extends ActivityGroup {
	// 定义所需的控件
	private TabHost tabHost;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duanxin);
		tabHost = (TabHost) findViewById(R.id.tabhost);
		// 获取选项卡布局
		tabHost.setup(this.getLocalActivityManager());

		// 创建选项卡对象
		TabSpec tab1 = tabHost.newTabSpec("TAB1");
		// 选项卡标题
		tab1.setIndicator("日常");
		// 跳转
		tab1.setContent(new Intent(this, Tab1.class));
		// 增加
		tabHost.addTab(tab1);

		// 创建选项卡对象
		TabSpec tab2 = tabHost.newTabSpec("TAB2");
		// 选项卡标题
		tab2.setIndicator("节日");
		// 跳转
		tab2.setContent(new Intent(this, Tab2.class));
		// 增加
		tabHost.addTab(tab2);

		// 创建选项卡对象
		TabSpec tab3 = tabHost.newTabSpec("TAB3");
		// 选项卡标题
		tab3.setIndicator("贺词");
		// 跳转
		tab3.setContent(new Intent(this, Tab3.class));
		// 增加
		tabHost.addTab(tab3);

		// 创建选项卡对象
		TabSpec tab4 = tabHost.newTabSpec("TAB4");
		// 选项卡标题
		tab4.setIndicator("其他");
		// 跳转
		tab4.setContent(new Intent(this, Tab4.class));
		// 增加
		tabHost.addTab(tab4);
		// 第一个显示的Tab界面
		tabHost.setCurrentTab(0);

	}
}
