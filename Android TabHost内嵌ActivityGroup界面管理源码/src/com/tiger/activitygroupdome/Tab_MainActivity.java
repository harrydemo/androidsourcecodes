package com.tiger.activitygroupdome;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * 底部选项卡选择界面
 * 
 * @author HuYang
 * 
 */
public class Tab_MainActivity extends TabActivity {
	/** 检查更新对象 */
	private TabHost tabHost;
	private TabHost.TabSpec spec;
	private RadioGroup mainbtGroup;
	private RadioButton tab_radio_Home;
	private RadioButton tab_radio_Category;
	private RadioButton tab_radio_Cart;
	private RadioButton tab_radio_Personal;
	private RadioButton tab_radio_More;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tab);
		init_tab();
		initView();
	}

	/**
	 * 初始化
	 */
	public void initView() {
		this.mainbtGroup = (RadioGroup) this.findViewById(R.id.main_radio);
		// 点击底部按钮,切换界面,!
		mainbtGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.menu_tab_home:
					tabHost.setCurrentTabByTag("home");
					break;
				case R.id.menu_tab_category:
					tabHost.setCurrentTabByTag("category");
					break;
				case R.id.menu_tab_cart:
					tabHost.setCurrentTabByTag("cart");
					break;
				case R.id.menu_tab_personal:
					tabHost.setCurrentTabByTag("personal");
				case R.id.menu_tab_more:
					tabHost.setCurrentTabByTag("more");
					break;
				}
			}
		});

	}

	/**
	 * 设置底部每个按钮点击后跳转的界面.
	 */
	private void init_tab() {
		// 获得tabHost对象,继承的TabActivity,直接取对象.!
		tabHost = getTabHost();
		TabSpec spec_home = tabHost
				.newTabSpec("home")
				.setIndicator("home")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								GotoMianActicity.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_home);
		TabSpec spec_category = tabHost
				.newTabSpec("category")
				.setIndicator("category")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								Activity02_A.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_category);

		TabSpec spec_cart = tabHost
				.newTabSpec("cart")
				.setIndicator("cart")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								Activity02_A.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_cart);
		TabSpec spec_personal = tabHost
				.newTabSpec("personal")
				.setIndicator("personal")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								Activity02_A.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_personal);
		TabSpec spec_more = tabHost
				.newTabSpec("more")
				.setIndicator("more")
				.setContent(
						new Intent().setClass(Tab_MainActivity.this,
								Activity02_A.class).addFlags(
								Intent.FLAG_ACTIVITY_CLEAR_TOP));
		tabHost.addTab(spec_more);
		// 得到底部每个按钮的对象,! 实例化.
		tab_radio_Home = (RadioButton) findViewById(R.id.menu_tab_more);
		tab_radio_Category = (RadioButton) findViewById(R.id.menu_tab_category);
		tab_radio_Cart = (RadioButton) findViewById(R.id.menu_tab_cart);
		tab_radio_Personal = (RadioButton) findViewById(R.id.menu_tab_personal);
		tab_radio_More = (RadioButton) findViewById(R.id.menu_tab_more);
	}

	public void promptExit(final Context con) {
		// 创建对话框
		AlertDialog.Builder ab = new AlertDialog.Builder(con);
		ab.setTitle("提示");
		ab.setMessage("确定要退出吗");
		ab.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Tab_MainActivity.this.finish();
			}
		});
		ab.setNegativeButton("取消", null);
		// 显示对话框
		ab.show();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			promptExit(Tab_MainActivity.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
