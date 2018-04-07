package com.mydemo.mei;

import java.io.Serializable;
import java.util.List;

import com.mydemo.mei.face.TabView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**   
 * Copyright (c) 2012
 * All rights reserved.
 * @Description 主界面的Tab
 * @author 美赞成
 */
public class MainActivity extends Activity implements  Serializable{
	

	private Context context;
	private MyMoveView moveView;
	private LeftMenuView leftMenuView;
	public Item01View item01View;
	public Item02View item02View;
	public Item03View item03View;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.context = this;
		
		//创建移动视图
		moveView = new MyMoveView(context);
		//创建左边菜单视图
		leftMenuView = new LeftMenuView(this, moveView);
		
		/****************************************************/
		//界面01
		item01View = new Item01View(context);
		item01View.setMyMoveView(moveView);//赋值移动视图
		item01View.init();
		/***************************************************/
		
		item02View = new Item02View(context);
		item03View = new Item03View(context);
		
		//移动视图默认界面：界面01，
		moveView.setMainView(item01View, leftMenuView, TabView.TAB_ITEM01);
		moveView.getLeft_show_view().setCurrentTab(TabView.TAB_ITEM01);

		setContentView(moveView);
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}
	
	@Override
	protected void onResume() {
		super.onResume();

	}

	/**
     * 返回健
     */
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	 if(moveView.getNowState()==MyMoveView.MAIN){
     			moveView.moveToLeft(true);
     			return true;
        	 }else{
        		 finish();
        	 }
         }
         return false;
         //return super.onKeyDown(keyCode, event);
     }
	
}
