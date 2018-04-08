package com.shinylife.smalltools;

import java.util.List;

import com.shinylife.smalltools.entity.NumberItem;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

	List<NumberItem> mlist;
	Context mcontext;

	public ItemAdapter(Context context, List<NumberItem> list) {
		mlist = list;
		mcontext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		DataListHolder dh = null;
		if (convertView == null) {
			dh = new DataListHolder();
			convertView = LayoutInflater.from(mcontext).inflate(
					R.layout.number_space_item, null);
			dh.textView = (TextView) convertView.findViewById(R.id.text_nsi);
			dh.imgView = (ImageView) convertView.findViewById(R.id.icon_nsi);
			convertView.setTag(dh);
		} else {
			dh = (DataListHolder) convertView.getTag();
		}
		dh.textView.setText(mlist.get(position).getName());
		dh.imgView.setBackgroundResource(mlist.get(position).getICOId());
		return convertView;
	}

}
