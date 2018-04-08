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
		//����һ��ninePatch�Ķ���ʵ������һ��������bitmap���ڶ���������byte[]��������ʵҪ�����Ǵ���
		//��δ������췽ʽ����Ȼ���ǲ���Ҫ�Լ����룬��Ϊ��.9.png��ͼƬ��������Щ��Ϣ���ݣ�
		//Ҳ���������á�9�á����߲�������Ϣ�� ����ֱ���á�.9.png��ͼƬ��������ݵ���getNinePatchChunk()����
		//������������ͼƬԴ�����ƣ��������Ϊ��ѡ������ֱ��null~��OK~
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
		RectF rectf_old_two = new RectF(0, 50, bmp_old.getWidth() * 2, 120 + bmp_old.getHeight() * 2);//��ע1
		RectF rectf_old_third = new RectF(0, 120 + bmp_old.getHeight() * 2, bmp_old.getWidth() * 3, 140 + bmp_old.getHeight() * 2 + bmp_old.getHeight() * 3);
		// --------�����Ƕ�����png�滭����-----------
		canvas.drawBitmap(bmp_old, 0, 0, paint);
		canvas.drawBitmap(bmp_old, null, rectf_old_two, paint);
		canvas.drawBitmap(bmp_old, null, rectf_old_third, paint);

		RectF rectf_9path_two = new RectF(250, 50, 250 + bmp_9path.getWidth() * 2, 90 + bmp_9path.getHeight() * 2);
		RectF rectf_9path_third = new RectF(250, 120 + bmp_9path.getHeight() * 2, 250 + bmp_9path.getWidth() * 3, 140 + bmp_9path.getHeight() * 2
				+ bmp_9path.getHeight() * 3);
		canvas.drawBitmap(bmp_9path, 250, 0, paint);
		// --------������".9.png"ͼ��Ļ滭����-----------
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