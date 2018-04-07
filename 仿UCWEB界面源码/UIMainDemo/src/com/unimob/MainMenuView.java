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
	AlertDialog menuDialog;// menu�˵�Dialog
	ListView listView;
	GridView menuGrid, toolbarGrid;
	View menuView;
	private boolean isMore = false;// menu�˵���ҳ����
	/*-- MENU�˵�ѡ���±� --*/
	private final int ITEM_SEARCH = 0;// ����
	private final int ITEM_FILE_MANAGER = 1;// �ļ�����
	private final int ITEM_DOWN_MANAGER = 2;// ���ع���
	private final int ITEM_FULLSCREEN = 3;// ȫ��
	private final int ITEM_MORE = 11;// �˵�

	/*-- Toolbar�ײ��˵�ѡ���±�--*/
	private final int TOOLBAR_ITEM_PAGEHOME = 0;// ��ҳ
	private final int TOOLBAR_ITEM_BACK = 1;// �˺�
	private final int TOOLBAR_ITEM_FORWARD = 2;// ǰ��
	private final int TOOLBAR_ITEM_NEW = 3;// ����
	private final int TOOLBAR_ITEM_MENU = 4;// �˵�
	/** �˵�ͼƬ **/
	int[] menu_image_array = { R.drawable.menu_search,
			R.drawable.menu_filemanager, R.drawable.menu_downmanager,
			R.drawable.menu_fullscreen, R.drawable.menu_inputurl,
			R.drawable.menu_bookmark, R.drawable.menu_bookmark_sync_import,
			R.drawable.menu_sharepage, R.drawable.menu_quit,
			R.drawable.menu_nightmode, R.drawable.menu_refresh,
			R.drawable.menu_more };
	/** �˵����� **/
	String[] menu_name_array = { "����", "�ļ�����", "���ع���", "ȫ��", "��ַ", "��ǩ",
			"������ǩ", "����ҳ��", "�˳�", "ҹ��ģʽ", "ˢ��", "����" };
	/** �˵�ͼƬ2 **/
	int[] menu_image_array2 = { R.drawable.menu_auto_landscape,
			R.drawable.menu_penselectmodel, R.drawable.menu_page_attr,
			R.drawable.menu_novel_mode, R.drawable.menu_page_updown,
			R.drawable.menu_checkupdate, R.drawable.menu_checknet,
			R.drawable.menu_refreshtimer, R.drawable.menu_syssettings,
			R.drawable.menu_help, R.drawable.menu_about, R.drawable.menu_return };
	/** �˵�����2 **/
	String[] menu_name_array2 = { "�Զ�����", "��ѡģʽ", "�Ķ�ģʽ", "���ģʽ", "��ݷ�ҳ",
			"������", "�������", "��ʱˢ��", "����", "����", "����", "����" };

	/** �ײ��˵�ͼƬ **/
	int[] menu_toolbar_image_array = { R.drawable.controlbar_homepage,
			R.drawable.controlbar_backward_enable,
			R.drawable.controlbar_forward_enable, R.drawable.controlbar_window,
			R.drawable.controlbar_showtype_list };
	/** �ײ��˵����� **/
	String[] menu_toolbar_name_array = { "��ҳ", "����", "ǰ��", "����", "�˵�" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// �����Զ���menu�˵�
		menuView = View.inflate(this, R.layout.gridview_menu, null);
		// ����AlertDialog
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// ��������
					dialog.dismiss();
				return false;
			}
		});

		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		/** ����menuѡ�� **/
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case ITEM_SEARCH:// ����

					break;
				case ITEM_FILE_MANAGER:// �ļ�����

					break;
				case ITEM_DOWN_MANAGER:// ���ع���

					break;
				case ITEM_FULLSCREEN:// ȫ��

					break;
				case ITEM_MORE:// ��ҳ
					if (isMore) {
						menuGrid.setAdapter(getMenuAdapter(menu_name_array2,
								menu_image_array2));
						isMore = false;
					} else {// ��ҳ
						menuGrid.setAdapter(getMenuAdapter(menu_name_array,
								menu_image_array));
						isMore = true;
					}
					menuGrid.invalidate();// ����menu
					menuGrid.setSelection(ITEM_MORE);
					break;
				}
				
				
			}
		});

		// �����ײ��˵� Toolbar
		toolbarGrid = (GridView) findViewById(R.id.GridView_toolbar);
		toolbarGrid.setBackgroundResource(R.drawable.channelgallery_bg);// ���ñ���
		toolbarGrid.setNumColumns(5);// ����ÿ������
		toolbarGrid.setGravity(Gravity.CENTER);// λ�þ���
		toolbarGrid.setVerticalSpacing(10);// ��ֱ���
		toolbarGrid.setHorizontalSpacing(10);// ˮƽ���
		toolbarGrid.setAdapter(getMenuAdapter(menu_toolbar_name_array,
				menu_toolbar_image_array));// ���ò˵�Adapter
		/** �����ײ��˵�ѡ�� **/
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
		
		/** ListView�б�**/
		listView = (ListView) findViewById(R.id.ListView_catalog);
		listView.setAdapter(getMenuAdapter(menu_name_array2, menu_image_array2));
		
	}

	@Override
	/**
	 * ����MENU
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// ���봴��һ��
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	/**
	 * ����MENU
	 */
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}

	/**
	 * ����˵�Adapter
	 * 
	 * @param menuNameArray
	 *            ����
	 * @param imageResourceArray
	 *            ͼƬ
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