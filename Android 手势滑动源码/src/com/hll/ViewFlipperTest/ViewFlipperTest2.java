package com.hll.ViewFlipperTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Button;
import android.widget.ViewFlipper;

public class ViewFlipperTest2 extends Activity implements OnGestureListener,
		OnDoubleTapListener {
	private ViewFlipper mViewFlipper;
	private GestureDetector mGestureDetector;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mGestureDetector = new GestureDetector(this);
		mGestureDetector.setOnDoubleTapListener(this);

		
		Button buttonNext1 = (Button) findViewById(R.id.Button_next1);
		mViewFlipper = (ViewFlipper) findViewById(R.id.details);
		buttonNext1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// 在layout中定义的属性，也可以在代码中指定
				// mViewFlipper.setInAnimation(getApplicationContext(),
				// R.anim.push_left_in);
				// mViewFlipper.setOutAnimation(getApplicationContext(),
				// R.anim.push_left_out);
				// mViewFlipper.setPersistentDrawingCache(ViewGroup.PERSISTENT_ALL_CACHES);
				// mViewFlipper.setFlipInterval(1000);
				mViewFlipper.showNext();
				// 调用下面的函数将会循环显示mViewFlipper内的所有View。
				// mViewFlipper.startFlipping();
			}
		});

		Button buttonNext2 = (Button) findViewById(R.id.Button_next2);
		buttonNext2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				mViewFlipper.showNext();
			}

		});
	}

	public boolean onDoubleTap(MotionEvent e) {
		 Log.d("tes", "...onDoubleTap...");   
	       if(mViewFlipper.isFlipping()) {   
	         mViewFlipper.stopFlipping();   
	       }else {   
	          mViewFlipper.startFlipping();   
	       }   
	       return true;   
	}

	
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d("tes", "...onFling...");
		if (e1.getX() > e2.getX()) {// move to left
			mViewFlipper.showNext();
		} else if (e1.getX() < e2.getX()) {
			mViewFlipper.setInAnimation(getApplicationContext(),
					R.anim.push_left_in);
			mViewFlipper.setOutAnimation(getApplicationContext(),
					R.anim.push_left_out);
			mViewFlipper.showPrevious();
			mViewFlipper.setInAnimation(getApplicationContext(),
					R.anim.push_left_in);
			mViewFlipper.setOutAnimation(getApplicationContext(),
					R.anim.push_left_out);
		} else {
			return false;
		}
		return true;
	}

	
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}