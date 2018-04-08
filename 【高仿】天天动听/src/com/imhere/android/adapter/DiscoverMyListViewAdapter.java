package com.imhere.android.adapter;

import java.util.List;

import com.imhere.android.R;
import com.imhere.android.entity.DiscoverClassify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class DiscoverMyListViewAdapter extends BaseAdapter {
	Context mContext;
	List<DiscoverClassify> mItmes;
	LayoutInflater mInflater;

	public DiscoverMyListViewAdapter(Context context,
			List<DiscoverClassify> itmes) {
		mContext = context;
		mItmes = itmes;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItmes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mItmes.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {
		v = mInflater.inflate(R.layout.listview_itme_discover_classify, null);
		TextView txt_ClassifyName = (TextView) v
				.findViewById(R.id.txt_ClassifyName);
		txt_ClassifyName.setText(mItmes.get(position).getItemName());
		GridView gridView = (GridView) v.findViewById(R.id.gridview);
		DiscoverMyGridViewAdapter mAdapter = new DiscoverMyGridViewAdapter(
				mContext, mItmes.get(position).getGridViewItems());
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println("ListView---->" + position + "GridView--->"
						+ arg2);
			}
		});
		return v;
	}
}
