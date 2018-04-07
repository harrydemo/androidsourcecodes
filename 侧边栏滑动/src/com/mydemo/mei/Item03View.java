package com.mydemo.mei;

import com.mydemo.mei.R;
import com.mydemo.mei.adapter.ImgTxtAdapter;
import com.mydemo.mei.adapter.ImgTxtBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**   
 * Copyright (c) 2012
 * All rights reserved.
 * @Description 选项3
 * @author 美赞成
 */
public class Item03View extends BaseView {

	private Context context;
	private ListView listview;
	private ImgTxtAdapter adapter;
	private Button leftButton;
	
	public Item03View(Context context){
		this.context = context;
	}
	
	public void init(){
		view = LayoutInflater.from(context).inflate(R.layout.item01, null);
		listview = (ListView)view.findViewById(R.id.item01_listview);
		leftButton = (Button)view.findViewById(R.id.item01_leftButton);
		adapter = new ImgTxtAdapter(context);
		listview.setAdapter(adapter);
		
		leftButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myMoveView.showHideLeftMenu();
			}
		});
		
		this.listview.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!isScroll() || myMoveView.getNowState() == MyMoveView.LEFT){
					return true;
				}
				return false;
			}
		});
		
		for(int i=0;i<20;i++){
			ImgTxtBean b = new ImgTxtBean();
			b.setResid(R.drawable.ic_launcher);
			b.setText("item03 - "+(i+1));
			adapter.addObject(b);
		}
		
		
	}
}
