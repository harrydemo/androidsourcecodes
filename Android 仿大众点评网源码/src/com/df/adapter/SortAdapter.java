package com.df.adapter;

import java.util.ArrayList;

import com.df.dianping.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter
{
	private ArrayList<String> data;
	Context mContext;
	private LayoutInflater mInflater;
	private int disableIndex = -1;
	
	public SortAdapter(Context context)
	{
		mContext = context;
		data = getData();
		this.mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() 
	{
		return data.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	 public boolean isEnabled(int position)
	 {
		 if(position == disableIndex)
		 {
			 return false;
		 }
		 return true;
	 }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		convertView = mInflater.inflate(R.layout.dialog_list_item, null);
		
		String area = data.get(position);
		
		((TextView) convertView.findViewById(R.id.id_area)).setText(area);

		if(area.equals("titlebar"))
		{
			convertView = mInflater.inflate(R.layout.sort_title_item, null);
			disableIndex = position;
		}
		
		if(position == 0)
		{
			convertView.findViewById(R.id.ic_checked).setVisibility(View.VISIBLE);
		}
			
		
		return convertView;
	}
	
	private ArrayList<String> getData()
	{
		ArrayList<String> data = new ArrayList<String>();
		data.add("按默认排序");
		data.add("按距离排序");
		data.add("按人气排序");
		data.add("按星级排序");
		data.add("按点评数排序");
		data.add("优惠劵商户优先");
		data.add("titlebar");
		data.add("20元以下");
		data.add("21-50");
		data.add("51-80");
		data.add("81-120");
		data.add("121-200");
		data.add("201以上");
		return data;
	}
}