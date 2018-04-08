package com.cellcom;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

//�Ӳ˵�subMenu
public class SubMenuActivity extends Activity {

	private final static int ITEM_NEW_FILE=Menu.FIRST;
	private final static int ITEM_OPEN_FILE=Menu.FIRST+1;
	private final static int ITEM_COPY=Menu.FIRST+2;
	private final static int ITEM_CLOSE=Menu.FIRST+3;
	private final static int ITEM_SAVE=Menu.FIRST+4;
	private final static int ITEM_SAVE_ALL=Menu.FIRST+5;
	private final static int ITEM_CUT=Menu.FIRST+6;
	private final static int ITEM_PASTE=Menu.FIRST+7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_menu);
		setTitle("����Menu������Ч����");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SubMenu file=menu.addSubMenu("�ļ�");
		SubMenu edit=menu.addSubMenu("�༭");
		
		file.add(0,ITEM_NEW_FILE,0,"�½�");
		file.add(0,ITEM_OPEN_FILE,0,"��");
		file.add(0,ITEM_CLOSE,0,"�ر�");
		file.add(0,ITEM_SAVE,0,"����");
		file.add(0,ITEM_SAVE_ALL,0,"����ȫ��");
		
		
		edit.add(0,ITEM_COPY,0,"����");
		edit.add(0,ITEM_CUT,0,"����");
		edit.add(0,ITEM_PASTE,0,"ճ��");
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM_NEW_FILE:
			setTitle("�½��ļ���");
			break;
		case ITEM_OPEN_FILE:
			setTitle("���ļ�");
			break;
		case ITEM_COPY:
			setTitle("�����ļ�");
			break;
		default:
			break;
		}
		return true;
	}
}
