package com.demo.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 焦点图适配器
 * @author xmz
 *
 */
public class FocusAdapter extends BaseAdapter {
	private List<Map<String,Object>> list;
	private Context context;
	public FocusAdapter(Context context){
		list=new ArrayList<Map<String, Object>>();
		this.context=context;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Map getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	public void addFocus(Map<String, Object> map){
		list.add(map);
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=LayoutInflater.from(context).inflate(R.layout.focus,null);
		Map<String,Object> map=getItem(position);
		//焦点图
		RelativeLayout mainLayout=(RelativeLayout)view.findViewById(R.id.focus_mainLayout);
		mainLayout.setBackgroundResource(Integer.parseInt(map.get("focusImage").toString()));
		//影片说明
		TextView introText=(TextView)view.findViewById(R.id.focus_introText);
		introText.setText(map.get("intro").toString());
		//焦点指针
		ImageView pointImage=(ImageView)view.findViewById(R.id.focus_pointImage);
		int resid=0;
		switch (position){
			case 0:
				resid=R.drawable.focus_point_1;
				break;
			case 1:
				resid=R.drawable.focus_point_2;
				break;
			case 2:
				resid=R.drawable.focus_point_3;
				break;
			case 3:
				resid=R.drawable.focus_point_4;
				break;
			case 4:
				resid=R.drawable.focus_point_5;
				break;
		}
		pointImage.setBackgroundResource(resid);
		
		return view;
	}

}
