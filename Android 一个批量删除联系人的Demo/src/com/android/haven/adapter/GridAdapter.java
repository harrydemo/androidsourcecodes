package com.android.haven.adapter;

import java.util.List;
import java.util.Map;

import com.android.haven.contact.ContactManagerActvity;
import com.android.haven.contact.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @auther hujh
 * @version 2010-10-30 ÏÂÎç01:35:54
 */

public class GridAdapter extends BaseAdapter {
	private List<Map<String,Integer>> list;
	private ContactManagerActvity activity;
	public GridAdapter(List<Map<String,Integer>> list,ContactManagerActvity activity){
		this.list = list;
		this.activity = activity;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(activity);
		View view = li.inflate(R.layout.grid_item, null);
		ImageView imageView = (ImageView)view.findViewById(R.id.grid_item_image);
		imageView.setBackgroundResource(list.get(position).get("ID"));
		TextView textView = (TextView)view.findViewById(R.id.grid_item_text);
		textView.setText(list.get(position).get("MSG"));
		return view;
	}

}
