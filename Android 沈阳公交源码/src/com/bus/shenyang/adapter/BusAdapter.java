package com.bus.shenyang.adapter;

import java.util.ArrayList;

import com.bus.shenyang.R;
import com.bus.shenyang.common.Bus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BusAdapter extends BaseAdapter {
	class ViewHolder {
		TextView content;
		TextView distance;
	}

	LayoutInflater mInflater;
	ArrayList<Plan> program = new ArrayList<Plan>();

	public BusAdapter(LayoutInflater inflater) {
		this.mInflater = inflater;
	}

	public void setData(ArrayList<Plan> plan) {
		System.out.println("1111111111111");
		this.program = plan;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return program.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.item, null);
			viewHolder = new ViewHolder();
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.Textfirst);
			viewHolder.distance = (TextView) convertView
			.findViewById(R.id.Textsecond);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String str=program.get(position).content;
		int str1 = program.get(position).distance;
		System.out.println("str=" + str1);
		String s=Integer.toString(str1);
		viewHolder.content.setText(str);
		viewHolder.distance.setText("总行程"+s+"米");
		return convertView;
	}
}
