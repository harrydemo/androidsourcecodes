package com.mydemo.mei;


import java.io.Serializable;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydemo.mei.R;
import com.mydemo.mei.face.TabView;

/**   
 * Copyright (c) 2012
 * All rights reserved.
 * @Description 左边菜单 
 * @author 美赞成
 */
public class LeftMenuView {
	
	private LinearLayout item01Layout;
	private LinearLayout item02Layout;
	private LinearLayout item03Layout;
	
	private static MainActivity mainActivity;
	private View view;
	private MyMoveView moveView;
	private LinearLayout titleLayout;
	private int currentTab=TabView.TAB_ITEM01;
	
	
	
	/**
	 * 构造初始化
	 * @param mainActivity
	 * @param moveView
	 */
	public LeftMenuView(MainActivity mainActivity,MyMoveView moveView){
		this.mainActivity = mainActivity;
		this.moveView = moveView;
		this.view = LayoutInflater.from(mainActivity).inflate(R.layout.leftmenu, null);
		createView();
		setViewProperty();
	}

	public void createView() {
		this.item01Layout  = (LinearLayout)view.findViewById(R.id.leftmenu_item01Layout);
		this.item02Layout  = (LinearLayout)view.findViewById(R.id.leftmenu_item02Layout);
		this.item03Layout  = (LinearLayout)view.findViewById(R.id.leftmenu_item03Layout);
		this.titleLayout = (LinearLayout)view.findViewById(R.id.leftmenu_titleLayout);
	}

	public void setViewProperty() {
		item01Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startItem01();
			}
		});
		
		item02Layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startItem02();
					}
				});
		
		item03Layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startItem03();
			}
		});
	}

	
	/**
	 * 进入item01
	 */
	public void startItem01(){
		if(!startInit(TabView.TAB_ITEM01)){
			item01Layout.setBackgroundResource(R.color.yellow);

			//打开视图
			setCurrentTab(TabView.TAB_ITEM01);
			if(mainActivity.item01View.view==null){
				mainActivity.item01View.init();
				mainActivity.item01View.setMyMoveView(moveView);
			}
			
			moveView.setMainView(mainActivity.item01View, null,TabView.TAB_ITEM01);
		}
	}
	/**
	 * 打开itm02
	 */
	public void startItem02(){
		if(!startInit(TabView.TAB_ITEM02)){
			item02Layout.setBackgroundResource(R.color.yellow);
			
			//打开视图
			setCurrentTab(TabView.TAB_ITEM02);
			
			if(mainActivity.item02View.view==null){
				mainActivity.item02View.init();
				mainActivity.item02View.setMyMoveView(moveView);
			}
			
			moveView.setMainView(mainActivity.item02View, null,TabView.TAB_ITEM02);
		}
	}
	
	/**
	 * 打开itm03
	 */
	public void startItem03(){
		if(!startInit(TabView.TAB_ITEM03)){
			item03Layout.setBackgroundResource(R.color.yellow);
			
			//打开视图
			setCurrentTab(TabView.TAB_ITEM03);
			
			if(mainActivity.item03View.view==null){
				mainActivity.item03View.init();
				mainActivity.item03View.setMyMoveView(moveView);
			}
			
			moveView.setMainView(mainActivity.item03View, null,TabView.TAB_ITEM03);
		}
	}

	
	public boolean startInit(int tabId){
		if(moveView.getNowState()==MyMoveView.MAIN){
			return true;
		}
		
		if(currentTab == tabId ){
			int now_state = moveView.getNowState();
			if (now_state == MyMoveView.MAIN) {
				moveView.moveToLeft(true);
			} else {
				moveView.moveToMain(true,0);
			}
			return true;
		}else{
			
			initLeftMenuBackGround();

			return false;
		}
	}
	/**
	 * 初始化左边菜单背景
	 */
	private void initLeftMenuBackGround(){
		this.item01Layout.setBackgroundResource(R.color.white);
		this.item02Layout.setBackgroundResource(R.color.white);
		this.item03Layout.setBackgroundResource(R.color.white);
	}

	public void setWidth(int w) { 
		// 为了使侧边栏布局合理 所以要为某项布局中宽度fill_parent的控件设置绝对宽度即侧边栏的宽度 
		LayoutParams p = view.getLayoutParams();
		p.width = w;
		view.setLayoutParams(p);
		
		ViewGroup.LayoutParams params = titleLayout.getLayoutParams();
		params.width = w;
		titleLayout.setLayoutParams(params);
	}
	
	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public View getView() {
		return view;
	}
	
	
}
