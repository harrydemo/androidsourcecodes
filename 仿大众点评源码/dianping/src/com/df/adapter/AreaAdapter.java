package com.df.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.df.dianping.R;
import com.df.dianping.ResultActivity;
import com.df.widget.PoiListItem;


public class AreaAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<ArrayList<String>> mData;
	private boolean isTopLevel = true;
	
	private int typeIndex = 0;
	
	private Context context;
	
	public AreaAdapter(Context context) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		mData = getData();
	}

	public ArrayList<ArrayList<String>> getDataList()
	{
		return mData;
	}
	
	public String getSelect()
	{
		return mData.get(typeIndex).get(1);
	}
	
	public boolean isTopLevel()
	{
		return isTopLevel;
	}
	
	public void setTypeIndex(int index)
	{
		typeIndex = index;
		if( index > 0)
		{
			isTopLevel = false;
		}
		else
		{
			isTopLevel = true;
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(isTopLevel)
		{
			return mData.size();
		}
		else
		{
			return mData.get(typeIndex).size()-1;
		}
		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = mInflater.inflate(R.layout.dialog_list_item, null);
		
		if(isTopLevel)
		{
			View view = new View(context);
			LayoutParams param = new LayoutParams(30, 30);
			view.setLayoutParams(param);
			
			String area = mData.get(position).get(1);
			((TextView)convertView.findViewById(R.id.id_area)).setText(area);
			
			if(position == 0)
			{		
				convertView.findViewById(R.id.ic_checked).setVisibility(View.VISIBLE);
			}
			else
			{
				((LinearLayout)convertView).addView(view, 0);
			}
			
		}
		else
		{
			
			
			String area = mData.get(typeIndex).get(position);
			((TextView)convertView.findViewById(R.id.id_area)).setText(area);
			
			if(position == 1)
			{
				View view = new View(context);
				LayoutParams param = new LayoutParams(30, 30);
				view.setLayoutParams(param);
				convertView.findViewById(R.id.ic_checked).setVisibility(View.VISIBLE);
				((LinearLayout)convertView).addView(view, 0);
			}
			else if(position > 1 )
			{
				View view = new View(context);
				LayoutParams param = new LayoutParams(60, 30);
				view.setLayoutParams(param);
				((LinearLayout)convertView).addView(view, 0);
			}

		}
		
		return convertView;
	}
	
	private ArrayList<ArrayList<String>> getData()
	{
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> quanbu = new ArrayList<String>();
		quanbu.add("全部地区");
		quanbu.add("全部地区 ");
		data.add(quanbu);
		
		ArrayList<String> hongkou = new ArrayList<String>();
		hongkou.add("全部地区");
		hongkou.add("虹口区");
		hongkou.add("海宁路/七浦路");
		hongkou.add("临平路/和平公园");
		hongkou.add("曲阳地区");
		hongkou.add("四川北路");
		hongkou.add("鲁迅公园");
		data.add(hongkou);
		
		ArrayList<String> zhabei = new ArrayList<String>();
		zhabei.add("全部地区");
		zhabei.add("闸北区");
		zhabei.add("大宁大区");
		zhabei.add("北区汽车站");
		zhabei.add("火车站");
		zhabei.add("闸北公园");
		data.add(zhabei);
		
		ArrayList<String> xuhui = new ArrayList<String>();
		xuhui.add("全部地区");
		xuhui.add("徐汇区");
		xuhui.add("衡山路");
		xuhui.add("音乐学院");
		xuhui.add("肇家浜路沿线");
		xuhui.add("漕河泾/田林");
		data.add(xuhui);
		
		ArrayList<String> changning = new ArrayList<String>();
		changning.add("全部地区");
		changning.add("长宁区");
		changning.add("天山");
		changning.add("上海影城/新华路");
		changning.add("中山公园");
		changning.add("古北");
		data.add(changning);
		
		ArrayList<String> yangpu = new ArrayList<String>();
		yangpu.add("全部地区");
		yangpu.add("杨浦区");
		yangpu.add("五角场");
		yangpu.add("控江地区");
		yangpu.add("平凉路");
		yangpu.add("黄兴公园");
		data.add(yangpu);
		
		ArrayList<String> qingpu = new ArrayList<String>();
		qingpu.add("全部地区");
		qingpu.add("青浦区");
		qingpu.add("朱家角");
		data.add(qingpu);
		
		ArrayList<String> songjiang = new ArrayList<String>();
		songjiang.add("全部地区");
		songjiang.add("松江区");
		songjiang.add("松江镇");
		songjiang.add("九亭");
		songjiang.add("佘山");
		songjiang.add("松江大学城");
		data.add(songjiang);
		
		ArrayList<String> baoshan = new ArrayList<String>();
		baoshan.add("全部地区");
		baoshan.add("宝山区");
		baoshan.add("大华大区");
		baoshan.add("庙行镇");
		baoshan.add("吴淞");
		baoshan.add("上海大学");
		data.add(baoshan);
		
		ArrayList<String> pudong = new ArrayList<String>();
		pudong.add("全部地区");
		pudong.add("康桥/周浦");
		pudong.add("陆家嘴");
		pudong.add("世纪公园");
		pudong.add("八佰伴");
		data.add(pudong);
		
		return data;
	}
}
