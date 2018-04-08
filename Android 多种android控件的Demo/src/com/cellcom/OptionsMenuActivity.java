package com.cellcom;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//OptionsMenu����
public class OptionsMenuActivity extends Activity {

	private final static int ITEM0=Menu.FIRST;
	private final static int ITEM1=Menu.FIRST+1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("����Menu������Ч����");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//��һ����������ID ���ڶ����������˵���ID��������������˳��š����ĸ��������˵�������ʾ������
		menu.add(0,ITEM0,0,"��ʼ");
		menu.add(0,ITEM1,0,"����");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM0:
			setTitle("��ʼ��Ϸ");
			break;
		case ITEM1:
			setTitle("������Ϸ");
			break;
		default:
			break;
		}
		return true;
	}

}
