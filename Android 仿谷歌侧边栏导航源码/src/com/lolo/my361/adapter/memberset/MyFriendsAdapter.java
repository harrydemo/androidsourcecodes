package com.lolo.my361.adapter.memberset;

import java.util.List;

import com.lolo.my361.activity.slyday.R;
import com.lolo.my361.entity.Friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFriendsAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<Friend> list;

	public MyFriendsAdapter(Context context, List<Friend> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("null")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.vip_myactivity_myfriend_item, null);
			holder = new viewHolder();
			holder.vip_friend_letter = (TextView) convertView
					.findViewById(R.id.vip_friend_letter);
			holder.vip_friend_name = (TextView) convertView
					.findViewById(R.id.vip_friend_item_name);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}

		holder.vip_friend_name.setText(list.get(position).getName());
		char letter = list.get(position).getName().substring(0, 1).charAt(0);
		String currentLetter = (letter + "").toUpperCase();
		
		String lastLetter = (position - 1) > 0 ? (list.get(position - 1)
				.getName().substring(0, 1).charAt(0) + "") : "";
		if (!lastLetter.equals(currentLetter)) {
			holder.vip_friend_letter.setText(currentLetter);
			holder.vip_friend_letter.setVisibility(View.VISIBLE);
		} else {
			holder.vip_friend_letter.setVisibility(View.GONE);

		}

		return convertView;
	}

	public class viewHolder {
		TextView vip_friend_letter;
		ImageView vip_friend_logo;
		TextView vip_friend_name;
	}

}
