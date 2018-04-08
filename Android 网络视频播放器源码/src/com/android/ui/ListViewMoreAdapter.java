// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ListViewMoreAdapter.java

package com.android.ui;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.*;
import android.widget.*;
import com.android.tools.RelativeCallback;
import java.util.Map;

public class ListViewMoreAdapter extends BaseAdapter
{
	public class ViewHolder
	{

		public ImageView imgViewLogo;
		public TextView txtViewTitle;

		public ViewHolder()
		{
		}
	}


	public Activity context;
	private boolean flag;
	public LayoutInflater inflater;
	Map mapContent;
	View myConvertView;
	private int selectItem;
	private RelativeCallback typeAc;

	public ListViewMoreAdapter(RelativeCallback relativecallback, Activity activity, Map map)
	{
		myConvertView = null;
		mapContent = null;
		typeAc = null;
		flag = false;
		selectItem = -1;
		context = activity;
		mapContent = map;
		typeAc = relativecallback;
		LayoutInflater layoutinflater = (LayoutInflater)activity.getSystemService("layout_inflater");
		inflater = layoutinflater;
	}

	public int getCount()
	{
		return mapContent.size();
	}

	public Object getItem(int i)
	{
		return null;
	}

	public long getItemId(int i)
	{
		return (long)i;
	}

	public View getView(int i, View view, ViewGroup viewgroup)
	{
		ViewHolder viewholder;
		Map map;
		StringBuilder stringbuilder;
		int j;
		String s;
		String s1;
		int k;
		if (view == null)
		{
			viewholder = new ViewHolder();
			view = inflater.inflate(0x7f030017, null);
			ImageView imageview = (ImageView)view.findViewById(0x7f06006e);
			viewholder.imgViewLogo = imageview;
			TextView textview = (TextView)view.findViewById(0x7f06006d);
			viewholder.txtViewTitle = textview;
			viewholder.txtViewTitle.getPaint().setFakeBoldText(true);
			view.setTag(viewholder);
		} else
		{
			viewholder = (ViewHolder)view.getTag();
		}
		viewholder.imgViewLogo.setImageResource(0x7f020021);
		map = mapContent;
		stringbuilder = new StringBuilder("item");
		j = i + 1;
		s = stringbuilder.append(j).toString();
		s1 = (String)map.get(s);
		viewholder.txtViewTitle.setText(s1);
		k = selectItem;
		if (i == k)
		{
			int l = Color.rgb(58, 161, 248);
			view.setBackgroundColor(l);
			if (mapContent != null)
			{
				int i1 = i + 1;
				RelativeCallback relativecallback = typeAc;
				String s2 = String.valueOf(i1);
				String s3 = (new StringBuilder(s2)).toString();
				relativecallback.GotoDetailView(s3, s1);
			} else
			{
				Toast.makeText(context, "Êý¾ÝHashMapÎªnull", 0).show();
			}
		} else
		{
			view.setBackgroundColor(-1);
		}
		return view;
	}

	public void setSelectItem(int i)
	{
		selectItem = i;
	}
}
