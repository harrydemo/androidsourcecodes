package com.himi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView; 
import android.view.SurfaceHolder.Callback;  

public class MySurfaceView extends SurfaceView implements Callback, Runnable {
 
	public static String button_str = "Himi_SurfaceViewÌí¼Ó×é¼þ";
	private int move_x = 2, x = 80;
	private Thread th;
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint p; 
	public MySurfaceView(Context context, AttributeSet attrs) { 
		super(context, attrs);
		p = new Paint(); 
		p.setAntiAlias(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		th = new Thread(this);
		this.setKeepScreenOn(true);
		setFocusable(true);
	}

 
	public void surfaceCreated(SurfaceHolder holder) {
		th.start(); 
	} 
	public void draw() {
		canvas = sfh.lockCanvas();
		canvas.drawColor(Color.WHITE);
		canvas.drawText(button_str, x + move_x, 280, p);
		sfh.unlockCanvasAndPost(canvas);
	}

	private void logic() {  
		x += move_x;
		if (x > 200 || x < 80) {
			move_x = -move_x;
		}
	}

	@Override
	public boolean onKeyDown(int key, KeyEvent event) {

		return super.onKeyDown(key, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return true;
	}

	 
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			draw();
			logic();
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
			}
		}
	}

	 
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

 
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
