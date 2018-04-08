package com.myandroid.test;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BodyAdatper extends BaseAdapter {
	
	private List<String> item_names;
	private List<Integer> item_images;
	private Context context;
	
	
	public BodyAdatper(Context context, List<String> item_names,
			List<Integer> item_images) {
		this.context = context;
		this.item_names = item_names;
		this.item_images = item_images;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item_images.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//总布局
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		//选项名称
		TextView tv_item = new TextView(context);
		tv_item.setGravity(Gravity.CENTER);
		tv_item.setLayoutParams(new GridView.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tv_item.setText(item_names.get(position));
		//选项图表
		ImageView img_item = new ImageView(context);
		img_item.setLayoutParams(new LayoutParams(50, 50));
		img_item.setImageResource(item_images.get(position));
		//添加选项图标和名字
		layout.addView(img_item);
		layout.addView(tv_item);
		
		return layout;
	}

}
