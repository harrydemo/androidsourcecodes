package com.lqf.riji;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.lqf.gerenriji.R;
import com.lqf.gerenriji.ZhuJieMian;
import com.lqf.rili.RiLi;
import com.lqf.sqlite.DBhelper;

public class RiJi extends Activity {
	private ListView listView;
	// 定义一个游标
	private Cursor cursor;
	// 定义DBHelper类
	private DBhelper helper;

	@SuppressWarnings("static-access")
	protected void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置布局标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.riji);
		// 获取自定义标题布局
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.biaoti1);

		// 获取数据库实列化对象
		helper = new DBhelper(this);
		// 获取所需要的控件
		listView = (ListView) findViewById(R.id.listview);
		// 调用查询方法
		cursor = helper.rijiselect();
		// 创建数据库适配器
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.riji, cursor, new String[] { helper.TITLE,
						helper.MARCH, helper.NEIRONG }, new int[] {
						R.id.biaoti, R.id.riqi, R.id.neirong });
		//绑定适配器
		listView.setAdapter(adapter);
		//绑定上下文菜单
		listView.setOnCreateContextMenuListener(contextMenuListener);
	}

	// 创建Menu菜单
	@SuppressWarnings("unused")
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu menua = menu.addSubMenu(Menu.NONE, 0, 1, "写日记");
		SubMenu menub = menu.addSubMenu(Menu.NONE, 1, 1, "纪念日");
		SubMenu menuc = menu.addSubMenu(Menu.NONE, 2, 1, "个人账本");
		SubMenu menud = menu.addSubMenu(Menu.NONE, 3, 1, "幽默笑话");
		SubMenu menue = menu.addSubMenu(Menu.NONE, 4, 1, "短信大全");

		return super.onCreateOptionsMenu(menu);
	}

	// 点击Menu菜单事件
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(RiJi.this, XieRiJi.class);
			startActivity(intent);
			break;
		case 1:
			Intent intent2 = new Intent(RiJi.this, JiNianRi.class);
			startActivity(intent2);
			break;
		case 2:
			
			break;
		case 3:
			Intent intent4 = new Intent(RiJi.this, XiaoHua.class);
			startActivity(intent4);
			break;
		case 4:
			Intent intent5 = new Intent(RiJi.this, DuanXin.class);
			startActivity(intent5);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	 //该方法用于显示上下文菜单
	OnCreateContextMenuListener contextMenuListener = new OnCreateContextMenuListener() {
		
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			//添加上下文菜单
			menu.add(Menu.NONE, 0, 2, "新增");
			menu.add(Menu.NONE, 1, 2, "修改");
			menu.add(Menu.NONE, 2, 2, "删除");
			menu.add(Menu.NONE, 3, 2, "全部删除");
			//添加上下文菜单标题
			menu.setHeaderTitle("菜单选项");
			//添加上下文菜单标题图片
			menu.setHeaderIcon(android.R.drawable.ic_popup_sync);
			
		}
	};
	//点击上下文菜单所触发的事件
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(RiJi.this, XieRiJi.class);
			startActivity(intent);
			break;
		case 1:
			break;
		case 2:
			
			break;
		case 3:
			break;
		}
		return super.onContextItemSelected(item);
	}
	//返回键动画效果
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				Intent intent = new Intent(RiJi.this, ZhuJieMian.class);
				setResult(RESULT_OK, intent);
				finish();
				overridePendingTransition(R.anim.my_up, R.anim.my_down);
				return false;
			}
			return false;
		}
}
