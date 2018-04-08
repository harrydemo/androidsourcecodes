package com.geolo.android.list;

import java.util.List;

import com.geolo.android.R;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyItemAdapter extends BaseAdapter {

	private Context context;
	private List<MyItem> list;
	public MyItemAdapter(Context ct,List<MyItem> list)
	{
		context = ct;
		this.list = list;
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		if (position < list.size()) {
			return list.get(position);
			} else {
			return null;
			}
		}

	public long getItemId(int position) {
		if (position < list.size()) {
			return list.get(position).getId();
			} else {
			return -1;
			}
		}

	public View getView(int position, View convertView, ViewGroup parent) {
		MyItem item = list.get(position);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View view;
		if (convertView != null && convertView.getId() == R.id.users) {
		view = convertView;
		} else {
		view = mInflater.inflate(R.layout.user_item_list, parent, false);
		}
		ItemViewHolder holder = (ItemViewHolder) view.getTag();
		if (holder == null) {
			holder = new ItemViewHolder();
			holder.myImageView = (ImageView) view.findViewById(R.id.img);
			holder.nameTextView = (TextView) view.findViewById(R.id.name);
			holder.ageTextView = (TextView) view.findViewById(R.id.age);
			holder.infoTextView = (TextView) view.findViewById(R.id.info);
			view.setTag(holder);
		}
		if (item != null) {
			
			if(item.getBitMap() != null)
			{
				holder.myImageView.setImageBitmap(item.getBitMap());
			}
			else
			{
				System.out.print("set ImageRes and resId = "+item.getIcon());
				holder.myImageView.setImageResource(item.getIcon());
			}
			holder.nameTextView.setText(item.getName());
			holder.ageTextView.setText(item.getAge());
			holder.infoTextView.setText(item.getInfo());
		}
		return view;
		}

	protected class ItemViewHolder {
		public ImageView myImageView;
		public TextView nameTextView;
		public TextView ageTextView;
		public TextView infoTextView;
		}
}
