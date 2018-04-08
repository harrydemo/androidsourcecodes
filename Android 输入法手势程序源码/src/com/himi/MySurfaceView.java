package com.himi;

import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	private Thread th;
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint p;
	public static Vector<Bitmap> vec_bmp;
	public static Vector<String> vec_string;
	private int col;

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		p = new Paint();
		p.setAntiAlias(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		th = new Thread(this);
		this.setKeepScreenOn(true);
		setFocusable(true);
		vec_string = new Vector<String>();
		vec_bmp = new Vector<Bitmap>();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		col = this.getWidth() / 100;
		th.start();
	}

	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				if (vec_bmp != null && vec_bmp.size() != 0) {
					for (int i = 0; i < vec_bmp.size(); i++) {
						Bitmap bitmap = vec_bmp.elementAt(i);
						p.setStyle(Style.STROKE);
						canvas.drawRect((i % col) * 104 + 1, (i / col) * 100 + 1, (i % col) * 104 + 104 - 2, (i / col) * 100 + 100 - 2, p);
						canvas.drawBitmap(bitmap, (i % col) * 100, (i / col) * 100, p);
						p.setColor(Color.YELLOW);
						canvas.drawText(vec_string.elementAt(i), (i % col) * 104 + 10, (i / col) * 100 + 97, p);
						p.setColor(Color.WHITE);
					}
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			sfh.unlockCanvasAndPost(canvas);
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
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
