package com.isoftstone.listviewdemo;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class Main extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// ��XML�е�ListView����ΪItem������
		MyListViewDemoActivity list = (MyListViewDemoActivity) findViewById(R.id.MyListView);
		// ���ɶ�̬���飬����ת������
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 30; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ItemTitle", "This is Title.....");
			map.put("ItemText", "This is text.....");
			mylist.add(map);
		}
		// ����������������===��ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // ûʲô����
				mylist,// ������Դ
				R.layout.my_listitem,// ListItem��XMLʵ��
				// ��̬������ListItem��Ӧ������
				new String[] { "ItemTitle", "ItemText" },
				// ListItem��XML�ļ����������TextView ID
				new int[] { R.id.ItemTitle, R.id.ItemText });
		// ��Ӳ�����ʾ
		list.setAdapter(mSchedule);

	}
}