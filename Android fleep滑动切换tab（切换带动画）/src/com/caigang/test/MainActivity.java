package com.caigang.test;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;

public class MainActivity extends TabActivity implements OnTabChangeListener,OnGestureListener {
	private GestureDetector gestureDetector;
	private FrameLayout frameLayout;
	private CustomTabHost tabHost;
	private TabWidget tabWidget;
	
	private static final int FLEEP_DISTANCE = 120;

	/** 记录当前分页ID */
	private int currentTabID = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		tabHost = (CustomTabHost) findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabHost.setOnTabChangedListener(this);

		init();
		
		gestureDetector = new GestureDetector(this);
		new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
		frameLayout = tabHost.getTabContentView();
	}

	private void init() {
		setIndicator(R.drawable.icon1, 0, new Intent(this,TabActivity01.class));
		setIndicator(R.drawable.icon2, 1, new Intent(this,TabActivity02.class));
		setIndicator(R.drawable.icon3, 2, new Intent(this,TabActivity03.class));
		setIndicator(R.drawable.icon4, 3, new Intent(this,TabActivity04.class));
	}

	private void setIndicator(int icon, int tabId, Intent intent) {
		View localView = LayoutInflater.from(this.tabHost.getContext()).inflate(R.layout.tab, null);
		((ImageView) localView.findViewById(R.id.tab_image)).setBackgroundResource(icon);
		String str = String.valueOf(tabId);
		TabHost.TabSpec localTabSpec = tabHost.newTabSpec(str).setIndicator(localView).setContent(intent);
		tabHost.addTab(localTabSpec);
	}
	

	@Override
	public void onTabChanged(String tabId) {
		//tabId值为要切换到的tab页的索引位置
		int tabID = Integer.valueOf(tabId);
		for (int i = 0; i < tabWidget.getChildCount(); i++) {
			if (i == tabID) {
				tabWidget.getChildAt(Integer.valueOf(i)).setBackgroundColor(R.color.bule);
			} else {
				tabWidget.getChildAt(Integer.valueOf(i)).setBackgroundColor(R.color.white);
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		if (e1.getX() - e2.getX() <= (-FLEEP_DISTANCE)) {//从左向右滑动
			currentTabID = tabHost.getCurrentTab() - 1;
			if (currentTabID < 0) {
				currentTabID = tabHost.getTabCount() - 1;
			}
		} else if (e1.getX() - e2.getX() >= FLEEP_DISTANCE) {//从右向左滑动
			currentTabID = tabHost.getCurrentTab() + 1;
			if (currentTabID >= tabHost.getTabCount()) {
				currentTabID = 0;
			}
		}
		tabHost.setCurrentTab(currentTabID);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}