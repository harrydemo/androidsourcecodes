package hong.specialEffects.ui;

import hong.specialEffects.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 *  Copyright (C) 2010 ideasandroid
 *  ��ӭ����http://www.ideasandroid.com
 *  �ó��򿪷�������ô����
 */
public class HeaderBottomListActivity extends Activity {

	private ListView mylist = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.head_bottom);
		mylist = (ListView) findViewById(R.id.myListView);
		ArrayAdapter<String> arrayA=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray);
		mylist.setAdapter(arrayA);
	}
	
	String[] listArray = { "ideasAndrid", "ideasandroid.com", "��ʾ����", "��ӭ����ideasandroid.com",
			"�ó��򿪷���������",  "android", "ListView", "����Ч��",
			"��ӭ���٣�","����1", "����2", "����3",
			"����4"};
}