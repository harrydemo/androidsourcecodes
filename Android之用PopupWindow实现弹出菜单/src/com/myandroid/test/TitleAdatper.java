package com.myandroid.test;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class TitleAdatper extends BaseAdapter {
	private List<String> titles;
	private Context context;
	private final TextView[] tv_titels;
	
	
	public TitleAdatper(Context context, List<String> titles) {
		this.context = context;
		this.titles = titles;
		tv_titels = new TextView[titles.size()];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.size();
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
	
	/**
	 * 选中后，改变菜单颜色。
	 * @param position
	 */
	public void setFocus(int position) {
		
		for (int i = 0; i < titles.size(); i++) {
			
			tv_titels[i].setBackgroundColor(Color.WHITE);
		}
		
		tv_titels[position].setBackgroundColor(Color.BLUE);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//菜单栏文字项
		tv_titels[position] = new TextView(context);
		tv_titels[position].setGravity(Gravity.CENTER);
		tv_titels[position].setText(titles.get(position));
		tv_titels[position].setTextSize(18);
		tv_titels[position].setLayoutParams(new GridView.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		return tv_titels[position];
	}

}
