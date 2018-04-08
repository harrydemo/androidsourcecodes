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
 *@ Gesture �����ģ�����������ʶ��
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
	private Vector<String> v_str;// ��ע1

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
		// setLongClickable( true )�Ǳ���ģ���Ϊ ֻ��������
		// ���ǵ�ǰ��SurfaceView(view)���ܹ�����ͬ�ڴ�����ʽ;
		// ���磺ACTION_MOVE�����߶��ACTION_DOWN
		this.setOnTouchListener(this);// ������󶨴���������
		gd = new GestureDetector(this);
		gd.setIsLongpressEnabled(true);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// ��ϵͳ�����˴˷����Ŵ�����view�������������ȡ��view�Ŀ�ߣ�����ЩͯЬ���ǰѶ��������ڳ�ʼ�������
		// �߳���÷�����������������Ϊ���ڳ�ʼ����Ļ�����view��û����,�����ύ����unlockCanvasAndPost��ʱ����쳣����
		bmp_x = (getWidth() - bmp.getWidth()) >> 2;
		bmp_y = (getHeight() - bmp.getHeight()) >> 2;
		th.start();
	}

	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.WHITE);// ����ˢ��
				canvas.drawBitmap(bmp, bmp_x, bmp_y, paint); 
				paint.setTextSize(20);// �������ִ�С 
				paint.setColor(Color.WHITE);
				//���ﻭ��һ�����η���ͯЬ�ǿ������Ʋ������õĺ���������Щ
				canvas.drawRect(50, 30, 175,120, paint);
				paint.setColor(Color.RED);// ����������ɫ
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
	// public boolean onTouchEvent(MotionEvent event) {// ��ע2
	// return true;
	// }

	@Override
	public boolean onTouch(View v, MotionEvent event) {// ��ע3
		if (v_str != null)
			v_str.removeAllElements();
		return gd.onTouchEvent(event);// ��ע4
	}

	// --------------������ʹ��OnGestureListener���Ƽ�����ʱ����д�ĺ���---------
	/**
	 * @���·����еĲ������ͣ�
	 * @e1����1���� ACTION_DOWN MotionEvent ���µĶ���
	 * @e2����һ����ACTION_UP MotionEvent ̧��Ķ���(����Ҫ���±�ע5�Ľ���)
	 * @velocityX��X���ϵ��ƶ��ٶȣ�����/��
	 * @velocityY��Y���ϵ��ƶ��ٶȣ�����/��
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		// ACTION_DOWN
		v_str.add("onDown");
		return false;
	} 
	@Override
	// ACTION_DOWN ���̰����ƶ�
	public void onShowPress(MotionEvent e) {
		v_str.add("onShowPress");

	} 
	@Override
	// ACTION_DOWN ������������
	public void onLongPress(MotionEvent e) {
		v_str.add("onLongPress");
	}

	@Override
	// ACTION_DOWN ��������
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		v_str.add("onScroll");
		return false;
	}

	@Override
	// ACTION_DOWN ���컬���� ACTION_UP
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		v_str.add("onFling");
		//-------��ע5----------
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
	// �̰�ACTION_DOWN��ACTION_UP
	public boolean onSingleTapUp(MotionEvent e) {
		v_str.add("onSingleTapUp");
		return false;
	}
}



