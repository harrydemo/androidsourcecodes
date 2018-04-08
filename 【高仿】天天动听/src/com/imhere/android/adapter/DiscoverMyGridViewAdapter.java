package com.imhere.android.adapter;

import java.util.List;

import com.imhere.android.R;
import com.imhere.android.entity.DiscoverBriefUnit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DiscoverMyGridViewAdapter extends BaseAdapter {
	Context mContext;
	List<DiscoverBriefUnit> items;
	LayoutInflater mInflater;

	public DiscoverMyGridViewAdapter(Context context,
			List<DiscoverBriefUnit> items) {
		mContext = context;
		this.items = items;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup arg2) {
		v = mInflater.inflate(R.layout.grivdview_item_discover_classify, null);
		ImageView img_ItemPic = (ImageView) v.findViewById(R.id.img_ItemPic);
		TextView txt_ItemName = (TextView) v.findViewById(R.id.txt_ItemName);
		img_ItemPic.setBackgroundDrawable(items.get(position).getDrawable());
		txt_ItemName.setText(items.get(position).getName());
		return v;
	}

}
