package com.lqf.rili;

import com.lqf.gerenriji.R;
import com.lqf.gerenriji.ZhuJieMian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RiLi extends Activity {
	// 定义CalendarView对象
	private CalendarView calendarView;

	protected void onCreate(Bundle savedInstanceState) {
		// 获取日历绘制布局
		LinearLayout mainLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.rili, null);
		super.onCreate(savedInstanceState);
		setContentView(mainLayout);
		// 获取实例化对象
		calendarView = new CalendarView(this);
		// 显示网格
		mainLayout.addView(calendarView);
	}

	// 创建Menu功能菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		// 定义Menu主菜单
		SubMenu menua = menu.addSubMenu(Menu.NONE, 0, 1, "");
		menua.setIcon(R.drawable.rilimenua);// 设置背景
		SubMenu menub = menu.addSubMenu(Menu.NONE, 1, 1, "");
		menub.setIcon(R.drawable.rilimenub);// 设置背景
		SubMenu menuc = menu.addSubMenu(Menu.NONE, 2, 1, "");
		menuc.setIcon(R.drawable.rilimenuc);// 设置背景
		SubMenu menud = menu.addSubMenu(Menu.NONE, 3, 1, "");
		menud.setIcon(R.drawable.rilimenud);// 设置背景
		SubMenu menue = menu.addSubMenu(Menu.NONE, 4, 1, "");
		menue.setIcon(R.drawable.rilimenue);// 设置背景
		SubMenu menuf = menu.addSubMenu(Menu.NONE, 5, 1, "");
		menuf.setIcon(R.drawable.rilimenuf);// 设置背景
		return super.onCreateOptionsMenu(menu);
	}

	// 点击Menu菜单所触发的事件
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Toast.makeText(RiLi.this, "您点击了今天", 100).show();
			break;
		case 1:
			Toast.makeText(RiLi.this, "您点击了日期", 100).show();
			break;
		case 2:
			Toast.makeText(RiLi.this, "您点击了提醒", 100).show();
			break;
		case 3:
			Toast.makeText(RiLi.this, "您点击了天气", 100).show();
			break;
		case 4:
			Toast.makeText(RiLi.this, "您点击了信息", 100).show();
			break;
		case 5:
			Toast.makeText(RiLi.this, "您点击了关于", 100).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	//返回键动画效果
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(RiLi.this, ZhuJieMian.class);
			setResult(RESULT_OK, intent);
			finish();
			overridePendingTransition(R.anim.my_up, R.anim.my_down);
			return false;
		}
		return false;
	}
}
