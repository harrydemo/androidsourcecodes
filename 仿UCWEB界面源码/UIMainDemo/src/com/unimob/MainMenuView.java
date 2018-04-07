/*
 *Author:CoffeeCole
 *Email:longkefan@foxmail.com
 *Date:2010-8-6
 */
package com.unimob;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainMenuView extends Activity {
	AlertDialog menuDialog;// menu菜单Dialog
	ListView listView;
	GridView menuGrid, toolbarGrid;
	View menuView;
	private boolean isMore = false;// menu菜单翻页控制
	/*-- MENU菜单选项下标 --*/
	private final int ITEM_SEARCH = 0;// 搜索
	private final int ITEM_FILE_MANAGER = 1;// 文件管理
	private final int ITEM_DOWN_MANAGER = 2;// 下载管理
	private final int ITEM_FULLSCREEN = 3;// 全屏
	private final int ITEM_MORE = 11;// 菜单

	/*-- Toolbar底部菜单选项下标--*/
	private final int TOOLBAR_ITEM_PAGEHOME = 0;// 首页
	private final int TOOLBAR_ITEM_BACK = 1;// 退后
	private final int TOOLBAR_ITEM_FORWARD = 2;// 前进
	private final int TOOLBAR_ITEM_NEW = 3;// 创建
	private final int TOOLBAR_ITEM_MENU = 4;// 菜单
	/** 菜单图片 **/
	int[] menu_image_array = { R.drawable.menu_search,
			R.drawable.menu_filemanager, R.drawable.menu_downmanager,
			R.drawable.menu_fullscreen, R.drawable.menu_inputurl,
			R.drawable.menu_bookmark, R.drawable.menu_bookmark_sync_import,
			R.drawable.menu_sharepage, R.drawable.menu_quit,
			R.drawable.menu_nightmode, R.drawable.menu_refresh,
			R.drawable.menu_more };
	/** 菜单文字 **/
	String[] menu_name_array = { "搜索", "文件管理", "下载管理", "全屏", "网址", "书签",
			"加入书签", "分享页面", "退出", "夜间模式", "刷新", "更多" };
	/** 菜单图片2 **/
	int[] menu_image_array2 = { R.drawable.menu_auto_landscape,
			R.drawable.menu_penselectmodel, R.drawable.menu_page_attr,
			R.drawable.menu_novel_mode, R.drawable.menu_page_updown,
			R.drawable.menu_checkupdate, R.drawable.menu_checknet,
			R.drawable.menu_refreshtimer, R.drawable.menu_syssettings,
			R.drawable.menu_help, R.drawable.menu_about, R.drawable.menu_return };
	/** 菜单文字2 **/
	String[] menu_name_array2 = { "自动横屏", "笔选模式", "阅读模式", "浏览模式", "快捷翻页",
			"检查更新", "检查网络", "定时刷新", "设置", "帮助", "关于", "返回" };

	/** 底部菜单图片 **/
	int[] menu_toolbar_image_array = { R.drawable.controlbar_homepage,
			R.drawable.controlbar_backward_enable,
			R.drawable.controlbar_forward_enable, R.drawable.controlbar_window,
			R.drawable.controlbar_showtype_list };
	/** 底部菜单文字 **/
	String[] menu_toolbar_name_array = { "首页", "后退", "前进", "创建", "菜单" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 设置自定义menu菜单
		menuView = View.inflate(this, R.layout.gridview_menu, null);
		// 创建AlertDialog
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
					dialog.dismiss();
				return false;
			}
		});

		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** 监听menu选项 **/
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case ITEM_SEARCH:// 搜索

					break;
				case ITEM_FILE_MANAGER:// 文件管理

					break;
				case ITEM_DOWN_MANAGER:// 下载管理

					break;
				case ITEM_FULLSCREEN:// 全屏

					break;
				case ITEM_MORE:// 翻页
					if (isMore) {
						menuGrid.setAdapter(getMenuAdapter(menu_name_array2,
								menu_image_array2));
						isMore = false;
					} else {// 首页
						menuGrid.setAdapter(getMenuAdapter(menu_name_array,
								menu_image_array));
						isMore = true;
					}
					menuGrid.invalidate();// 更新menu
					menuGrid.setSelection(ITEM_MORE);
					break;
				}
				
				
			}
		});

		// 创建底部菜单 Toolbar
		toolbarGrid = (GridView) findViewById(R.id.GridView_toolbar);
		toolbarGrid.setBackgroundResource(R.drawable.channelgallery_bg);// 设置背景
		toolbarGrid.setNumColumns(5);// 设置每行列数
		toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
		toolbarGrid.setVerticalSpacing(10);// 垂直间隔
		toolbarGrid.setHorizontalSpacing(10);// 水平间隔
		toolbarGrid.setAdapter(getMenuAdapter(menu_toolbar_name_array,
				menu_toolbar_image_array));// 设置菜单Adapter
		/** 监听底部菜单选项 **/
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(MainMenuView.this,
						menu_toolbar_name_array[arg2], Toast.LENGTH_SHORT)
						.show();
				switch (arg2) {
				case TOOLBAR_ITEM_PAGEHOME:
					break;
				case TOOLBAR_ITEM_BACK:

					break;
				case TOOLBAR_ITEM_FORWARD:

					break;
				case TOOLBAR_ITEM_NEW:

					break;
				case TOOLBAR_ITEM_MENU:
					menuDialog.show();
					break;
				}
			}
		});
		
		/** ListView列表**/
		listView = (ListView) findViewById(R.id.ListView_catalog);
		listView.setAdapter(getMenuAdapter(menu_name_array2, menu_image_array2));
		
	}

	@Override
	/**
	 * 创建MENU
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	/**
	 * 拦截MENU
	 */
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// 返回为true 则显示系统menu
	}

	/**
	 * 构造菜单Adapter
	 * 
	 * @param menuNameArray
	 *            名称
	 * @param imageResourceArray
	 *            图片
	 * @return SimpleAdapter
	 */
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}

}