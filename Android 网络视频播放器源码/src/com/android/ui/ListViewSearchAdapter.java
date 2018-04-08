package com.android.ui;
/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import android.app.Activity;
import android.view.*;
import android.widget.*;

import java.util.*;

import com.android.tools.AsyncImgLoader;
import com.sph.player.R;

public class ListViewSearchAdapter extends ArrayAdapter
{

	private AsyncImgLoader imageLoader;
	private Map viewMap;

	public ListViewSearchAdapter(Activity activity, List list)
	{
		super(activity, 0, list);
		AsyncImgLoader asyncimgloader = new AsyncImgLoader();
		imageLoader = asyncimgloader;
		HashMap hashmap = new HashMap();
		viewMap = hashmap;
	}

	public View getView(int i, View view, ViewGroup viewgroup)
	{
		Map map = viewMap;
		Integer integer = Integer.valueOf(i);
		View view1 = (View)map.get(integer);
		if (view1 == null)
		{
			view1 = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.search_activity_list, null);
			String s = (String)getItem(i);
			((TextView)view1.findViewById(R.id.search_keywords)).setText(s);
			((ImageView)view1.findViewById(R.id.search_icon)).setImageResource(R.drawable.header_icon_loupe);
			Map map1 = viewMap;
			Integer integer1 = Integer.valueOf(i);
			Object obj = map1.put(integer1, view1);
		}
		return view1;
	}
}
