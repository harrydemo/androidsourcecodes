package com.himi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private Thread th = new Thread(this);
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private Bitmap bmp_old;
	private Bitmap bmp_9path;
	private NinePatch np;

	public MySurfaceView(Context context) {
		super(context);
		this.setKeepScreenOn(true);
		bmp_old = BitmapFactory.decodeResource(getResources(), R.drawable.himi_old);
		bmp_9path = BitmapFactory.decodeResource(getResources(), R.drawable.himi_9path);
		np = new NinePatch(bmp_9path, bmp_9path.getNinePatchChunk(), null);
		//创建一个ninePatch的对象实例，第一个参数是bitmap、第二个参数是byte[]，这里其实要求我们传入
		//如何处理拉伸方式，当然我们不需要自己传入，因为“.9.png”图片自身有这些信息数据，
		//也就是我们用“9妹”工具操作的信息！ 我们直接用“.9.png”图片自身的数据调用getNinePatchChunk()即可
		//第三个参数是图片源的名称，这个参数为可选参数，直接null~就OK~
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		setFocusable(true);

	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.v("Himi", "surfaceCreated");
		th.start();
	}

	/**
	 * @author Himi
	 */
	public void draw() {
		canvas = sfh.lockCanvas();
		canvas.drawColor(Color.BLACK);
		RectF rectf_old_two = new RectF(0, 50, bmp_old.getWidth() * 2, 120 + bmp_old.getHeight() * 2);//备注1
		RectF rectf_old_third = new RectF(0, 120 + bmp_old.getHeight() * 2, bmp_old.getWidth() * 3, 140 + bmp_old.getHeight() * 2 + bmp_old.getHeight() * 3);
		// --------下面是对正常png绘画方法-----------
		canvas.drawBitmap(bmp_old, 0, 0, paint);
		canvas.drawBitmap(bmp_old, null, rectf_old_two, paint);
		canvas.drawBitmap(bmp_old, null, rectf_old_third, paint);

		RectF rectf_9path_two = new RectF(250, 50, 250 + bmp_9path.getWidth() * 2, 90 + bmp_9path.getHeight() * 2);
		RectF rectf_9path_third = new RectF(250, 120 + bmp_9path.getHeight() * 2, 250 + bmp_9path.getWidth() * 3, 140 + bmp_9path.getHeight() * 2
				+ bmp_9path.getHeight() * 3);
		canvas.drawBitmap(bmp_9path, 250, 0, paint);
		// --------下面是".9.png"图像的绘画方法-----------
		np.draw(canvas, rectf_9path_two);
		np.draw(canvas, rectf_9path_third);
		sfh.unlockCanvasAndPost(canvas);
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