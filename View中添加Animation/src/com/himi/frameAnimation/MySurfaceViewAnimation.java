package com.himi.frameAnimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color; 
import android.graphics.Paint; 
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation; 
import android.view.animation.TranslateAnimation;

/**
 *@author Himi
 */
public class MySurfaceViewAnimation extends SurfaceView implements Callback, Runnable {

	private Thread th = new Thread(this);
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private Bitmap bmp;
	/// 
private Animation mAlphaAnimation;
private Animation mScaleAnimation;
private Animation mTranslateAnimation;
private Animation mRotateAnimation;

public MySurfaceViewAnimation(Context context) {
	super(context);
	Log.v("Himi", "MySurfaceView");
	this.setKeepScreenOn(true);
	bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
	sfh = this.getHolder();
	sfh.addCallback(this);
	paint = new Paint();
	paint.setAntiAlias(true);
	setFocusable(true);
	setFocusableInTouchMode(true);
	this.setBackgroundResource(R.drawable.icon);//��ע2
}

public void surfaceCreated(SurfaceHolder holder) {
	Log.v("Himi", "surfaceCreated");
	th.start();
}

public void draw() {
	try {
		canvas = sfh.lockCanvas();
		if (canvas != null) {
			canvas.drawColor(Color.BLACK);
			paint.setColor(Color.WHITE);
			canvas.drawText("������� ����͸���ȶ���Ч��", 80, this.getHeight() - 80, paint);
			canvas.drawText("������� ����ߴ���������Ч��", 80, this.getHeight() - 60, paint);
			canvas.drawText("������� ����ת��λ���ƶ�����Ч��", 80, this.getHeight() - 40, paint);
			canvas.drawText("������� ����ת����ת����Ч��", 80, this.getHeight() - 20, paint);
			canvas.drawBitmap(bmp, this.getWidth() / 2 - bmp.getWidth() / 2, 
					this.getHeight() / 2 - bmp.getHeight() / 2, paint);
		}
	} catch (Exception e) {
		Log.v("Himi", "draw is Error!");
	} finally {
		sfh.unlockCanvasAndPost(canvas);
	}
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {//����͸���ȶ���Ч��
		mAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		mAlphaAnimation.setDuration(3000);
		this.startAnimation(mAlphaAnimation);
	} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {//����ߴ���������Ч��
		mScaleAnimation = new ScaleAnimation(0.0f, 2.0f, 
				1.5f, 1.5f, Animation.RELATIVE_TO_PARENT, 
				0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
		mScaleAnimation.setDuration(2000);
		this.startAnimation(mScaleAnimation);
	} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {//����ת��λ���ƶ�����Ч��
		mTranslateAnimation = new TranslateAnimation(0, 100, 0, 100);
		mTranslateAnimation.setDuration(2000);
		this.startAnimation(mTranslateAnimation);
	} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {//����ת����ת����Ч��
		mRotateAnimation = new RotateAnimation(0.0f, 360.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setDuration(3000);
		this.startAnimation(mRotateAnimation);
	}
	return super.onKeyDown(keyCode, event);
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
	Log.v("Himi", "surfaceChanged");
}

public void surfaceDestroyed(SurfaceHolder holder) {
	Log.v("Himi", "surfaceDestroyed");
	}
}