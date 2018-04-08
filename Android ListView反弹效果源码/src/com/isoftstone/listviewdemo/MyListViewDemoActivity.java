package com.isoftstone.listviewdemo;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

public class MyListViewDemoActivity extends ListView {
	private boolean outBound = false;
	private int distance;
	private int firstOut;
	private static final String TAG = "wljie";

	public MyListViewDemoActivity(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(TAG, "IN 1");
	}

	public MyListViewDemoActivity(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		Log.d(TAG, "IN 2");
	}

	public MyListViewDemoActivity(Context context) {
		super(context);
		Log.d(TAG, "IN 3");
	}

	GestureDetector gestureDetector = new GestureDetector(
			new OnGestureListener() {
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onShowPress(MotionEvent e) {
					// TODO Auto-generated method stub
				}

				/**
				 * 手势滑动的时候触发
				 */
				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {
					Log.d(TAG, "ENTER onscroll");
					int firstPos = getFirstVisiblePosition();
					int lastPos = getLastVisiblePosition();
					int itemCount = getCount();
					// outbound Top
					if (outBound && firstPos != 0 && lastPos != (itemCount - 1)) {
						scrollTo(0, 0);
						return false;
					}
					View firstView = getChildAt(firstPos);
					View lastView = getChildAt(lastPos - 1);
					if (!outBound)
						firstOut = (int) e2.getRawY();
					if (firstView != null
							&& (outBound || (firstPos == 0
									&& firstView.getTop() == 0 && distanceY < 0))) {
						// Record the length of each slide
						distance = firstOut - (int) e2.getRawY();
						scrollTo(0, distance / 2);
						return true;
					}
					if (lastView == null
							&& (outBound || (lastPos == itemCount - 1 && distanceY > 0))) {
						Log.d("bottom", "bottom");
						distance = (int) (firstOut - e2.getRawY());// 此处应为正直，因为view向上<span
																	// style="background-color: rgb(160, 255, 255);">滑动</span>
						scrollBy(0, distance / 2);
						return true;
					}

					// outbound Bottom
					return false;
				}

				@Override
				public void onLongPress(MotionEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean onDown(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}
			});

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		Log.d(TAG, "dispatchTouchEvent");
		int act = event.getAction();
		if ((act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL)
				&& outBound) {
			outBound = false;
			// scroll back
		}
		if (!gestureDetector.onTouchEvent(event)) {
			outBound = false;
		} else {
			outBound = true;
		}
		Rect rect = new Rect();
		getLocalVisibleRect(rect);
		TranslateAnimation am = new TranslateAnimation(0, 0, -rect.top, 0);
		am.setDuration(300);
		startAnimation(am);
		scrollTo(0, 0);
		return super.dispatchTouchEvent(event);
	}
}