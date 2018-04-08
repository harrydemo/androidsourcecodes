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
		// 绑定XML中的ListView，作为Item的容器
		MyListViewDemoActivity list = (MyListViewDemoActivity) findViewById(R.id.MyListView);
		// 生成动态数组，并且转载数据
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 30; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ItemTitle", "This is Title.....");
			map.put("ItemText", "This is text.....");
			mylist.add(map);
		}
		// 生成适配器，数组===》ListItem
		SimpleAdapter mSchedule = new SimpleAdapter(this, // 没什么解释
				mylist,// 数据来源
				R.layout.my_listitem,// ListItem的XML实现
				// 动态数组与ListItem对应的子项
				new String[] { "ItemTitle", "ItemText" },
				// ListItem的XML文件里面的两个TextView ID
				new int[] { R.id.ItemTitle, R.id.ItemText });
		// 添加并且显示
		list.setAdapter(mSchedule);

	}
}