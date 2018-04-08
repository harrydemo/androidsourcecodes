package com.himi;

import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnTouchListener;

/**
 *@author Himi
 *@ Gesture （上文）触摸屏手势识别
 */
public class MySurfaceViewAnimation extends SurfaceView implements Callback,
		Runnable, OnGestureListener, OnTouchListener {

	private Thread th = new Thread(this);
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private Bitmap bmp;
	private GestureDetector gd;
	private int bmp_x, bmp_y;
	private boolean isChagePage;
	private Vector<String> v_str;// 备注1

	public MySurfaceViewAnimation(Context context) {
		super(context);
		v_str = new Vector<String>();
		this.setKeepScreenOn(true);
		bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.himi_dream);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		this.setLongClickable(true);
		// setLongClickable( true )是必须的，因为 只有这样，
		// 我们当前的SurfaceView(view)才能够处理不同于触屏形式;
		// 例如：ACTION_MOVE，或者多个ACTION_DOWN
		this.setOnTouchListener(this);// 将本类绑定触屏监听器
		gd = new GestureDetector(this);
		gd.setIsLongpressEnabled(true);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// 当系统调用了此方法才创建了view所以在这里才能取到view的宽高！！有些童鞋总是把东西都放在初始化函数里！
		// 线程最好放在这里来启动，因为放在初始化里的画，那view还没有呢,到了提交画布unlockCanvasAndPost的时候就异常啦！
		bmp_x = (getWidth() - bmp.getWidth()) >> 2;
		bmp_y = (getHeight() - bmp.getHeight()) >> 2;
		th.start();
	}

	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.WHITE);// 画布刷屏
				canvas.drawBitmap(bmp, bmp_x, bmp_y, paint); 
				paint.setTextSize(20);// 设置文字大小 
				paint.setColor(Color.WHITE);
				//这里画出一个矩形方便童鞋们看到手势操作调用的函数都是哪些
				canvas.drawRect(50, 30, 175,120, paint);
				paint.setColor(Color.RED);// 设置文字颜色
				if (v_str != null) {
					for (int i = 0; i < v_str.size(); i++) {
						canvas.drawText(v_str.elementAt(i), 50, 50 + i * 30,
								paint);
					}
				}
			}
		} catch (Exception e) {
			Log.v("Himi", "draw is Error!");
		} finally {
			sfh.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			draw();
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
			}
		}
	} 
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {// 备注2
	// return true;
	// }

	@Override
	public boolean onTouch(View v, MotionEvent event) {// 备注3
		if (v_str != null)
			v_str.removeAllElements();
		return gd.onTouchEvent(event);// 备注4
	}

	// --------------以下是使用OnGestureListener手势监听的时候重写的函数---------
	/**
	 * @以下方法中的参数解释：
	 * @e1：第1个是 ACTION_DOWN MotionEvent 按下的动作
	 * @e2：后一个是ACTION_UP MotionEvent 抬起的动作(这里要看下备注5的解释)
	 * @velocityX：X轴上的移动速度，像素/秒
	 * @velocityY：Y轴上的移动速度，像素/秒
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		// ACTION_DOWN
		v_str.add("onDown");
		return false;
	} 
	@Override
	// ACTION_DOWN 、短按不移动
	public void onShowPress(MotionEvent e) {
		v_str.add("onShowPress");

	} 
	@Override
	// ACTION_DOWN 、长按不滑动
	public void onLongPress(MotionEvent e) {
		v_str.add("onLongPress");
	}

	@Override
	// ACTION_DOWN 、慢滑动
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		v_str.add("onScroll");
		return false;
	}

	@Override
	// ACTION_DOWN 、快滑动、 ACTION_UP
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		v_str.add("onFling");
		//-------备注5----------
		// if(e1.getAction()==MotionEvent.ACTION_MOVE){
		// v_str.add("onFling");
		// }else if(e1.getAction()==MotionEvent.ACTION_DOWN){
		// v_str.add("onFling");
		// }else if(e1.getAction()==MotionEvent.ACTION_UP){
		// v_str.add("onFling");
		// }
		// if(e2.getAction()==MotionEvent.ACTION_MOVE){
		// v_str.add("onFling");
		// }else if(e2.getAction()==MotionEvent.ACTION_DOWN){
		// v_str.add("onFling");
		// }else if(e2.getAction()==MotionEvent.ACTION_UP){
		// v_str.add("onFling");
		// }
		if (isChagePage)
			bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.himi_dream);
		else
			bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.himi_warm);
		isChagePage = !isChagePage;
		return false;
	}

	@Override
	// 短按ACTION_DOWN、ACTION_UP
	public boolean onSingleTapUp(MotionEvent e) {
		v_str.add("onSingleTapUp");
		return false;
	}
}



