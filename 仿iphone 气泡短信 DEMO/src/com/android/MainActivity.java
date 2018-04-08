package com.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity
{
	private ListView talkView;
	private List<DetailEntity> list = null;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listInit();

	}

	public void listInit()
	{

		talkView = (ListView) findViewById(R.id.list);

		list = new ArrayList<DetailEntity>();

		DetailEntity d1 = new DetailEntity("我", "2010-11-11", "你好!",
				R.layout.list_say_me_item);
		list.add(d1);
		DetailEntity d2 = new DetailEntity("美女", "2010-11-11", "你好!",
				R.layout.list_say_he_item);
		list.add(d2);
		DetailEntity d3 = new DetailEntity("美女", "2010-11-11", "你是谁?",
				R.layout.list_say_he_item);
		list.add(d3);
		DetailEntity d4 = new DetailEntity("我", "2010-11-11", "不认识么？",
				R.layout.list_say_me_item);
		list.add(d4);

		DetailEntity d5 = new DetailEntity("美女", "2010-11-11", "鬼认识你",
				R.layout.list_say_he_item);
		list.add(d5);
		DetailEntity d6 = new DetailEntity("我", "2010-11-11", "那就认识下",
				R.layout.list_say_me_item);
		list.add(d6);
		DetailEntity d7 = new DetailEntity("美女", "2010-11-11", "切",
				R.layout.list_say_he_item);
		list.add(d7);
		DetailEntity d8 = new DetailEntity("我", "2010-11-11", "~~！",
				R.layout.list_say_me_item);
		list.add(d8);

		talkView.setAdapter(new DetailAdapter(MainActivity.this, list));
		talkView.setDivider(null);// 去掉分割线
	}

}