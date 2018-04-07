package com.mydemo.mei.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mydemo.mei.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**   
 * Copyright (c) 2012
 * All rights reserved.
 * @Description Í¼Æ¬ÎÄ×Ö
 * @author ÃÀÔÞ³É
 */
public class ImgTxtAdapter extends BaseAdapter {

	private Context context;
	private List<ImgTxtBean> list;
	private LayoutInflater inflater;
	public ImgTxtAdapter(Context context){
		this.context = context;
		this.list = new ArrayList<ImgTxtBean>();
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ImgTxtBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addObject(ImgTxtBean b){
		list.add(b);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = inflater.inflate(R.layout.imgtxt, null);
		ImgTxtBean b = getItem(position);
		ImageView image = (ImageView)view.findViewById(R.id.img);
		TextView text = (TextView)view.findViewById(R.id.txt);
		text.setText(b.getText());
		if(b.getResid()>0){
			image.setBackgroundResource(b.getResid());
		}
		
		return view;
	}

}
