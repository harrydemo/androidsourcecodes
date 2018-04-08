package com.imhere.android.adapter;

import java.util.List;

import com.imhere.android.R;
import com.imhere.android.entity.Thoughts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutMeSpaceListAdapter extends BaseAdapter {
	List<Thoughts> mThoughts;
	Context mContext;
	LayoutInflater mInflater;

	public AboutMeSpaceListAdapter(List<Thoughts> thoughts, Context context) {
		this.mThoughts = thoughts;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThoughts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mThoughts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parme) {
		v = mInflater.inflate(R.layout.listview_item_aboutme, null);
		TextView txt_itemTime = (TextView) v.findViewById(R.id.txt_itemTime);
//		TextView txt_itemAddress = (TextView) v
//				.findViewById(R.id.txt_itemAddress);
		ImageView img_itemPhoto = (ImageView) v
				.findViewById(R.id.img_itemPhoto);
		TextView txt_itemMsgContent = (TextView) v
				.findViewById(R.id.txt_itemMsgContent);
		txt_itemTime.setText(mThoughts.get(position).getTime());
//		txt_itemAddress.setText(mThoughts.get(position).getAddress());
		img_itemPhoto.setBackgroundDrawable(mThoughts.get(position)
				.getDrawable());
		txt_itemMsgContent.setText(mThoughts.get(position).getMsg());
		return v;
	}

}
