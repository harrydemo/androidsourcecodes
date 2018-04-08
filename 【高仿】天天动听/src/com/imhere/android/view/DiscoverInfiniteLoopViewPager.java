package com.imhere.android.view;


import com.imhere.android.BaseApplication;
import com.imhere.android.adapter.DiscoverInfiniteLoopViewPagerAdapter;
import com.imhere.android.adapter.DiscoverMPagerAdapter;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DiscoverInfiniteLoopViewPager extends DiscoverMViewPager {
	
	private BaseApplication mApplication;
	private Handler handler;
	
	public DiscoverInfiniteLoopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mApplication = (BaseApplication)context.getApplicationContext();
	}

	public DiscoverInfiniteLoopViewPager(Context context) {
		super(context);
		mApplication = (BaseApplication)context.getApplicationContext();
	}

	@Override
	public void setAdapter(DiscoverMPagerAdapter adapter) {
		super.setAdapter(adapter);
		//设置当前展示的位置
		setCurrentItem(0);
	}
	
	public void setInfinateAdapter(Handler handler,DiscoverMPagerAdapter adapter){
		this.handler = handler;
		setAdapter(adapter);
	}
	
	@Override
	public void setCurrentItem(int item) {
		item = getOffsetAmount() + (item % getAdapter().getCount());
		super.setCurrentItem(item);
	}
	/**
	 * 从0开始向右(负向滑动)可以滑动的次数
	 * @return
	 */
	private int getOffsetAmount() {
		if (getAdapter() instanceof DiscoverInfiniteLoopViewPagerAdapter) {
			DiscoverInfiniteLoopViewPagerAdapter infiniteAdapter = (DiscoverInfiniteLoopViewPagerAdapter) getAdapter();
			// 允许向前滚动400000次
			return infiniteAdapter.getRealCount() * 100000;
		} else {
			return 0;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			mApplication.isRun = false;
			mApplication.isDown = true;
			handler.removeCallbacksAndMessages(null);
//			System.out.println("InfiniteLoopViewPager  dispatchTouchEvent =====>>> ACTION_DOWN");
		} else if (action == MotionEvent.ACTION_UP) {
			mApplication.isRun = true;
			mApplication.isDown = false;
			handler.removeCallbacksAndMessages(null);
			handler.sendEmptyMessage(1);
//			System.out.println("InfiniteLoopViewPager  dispatchTouchEvent =====>>> ACTION_UP");
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public void setOffscreenPageLimit(int limit) {
		super.setOffscreenPageLimit(limit);
	}
}
