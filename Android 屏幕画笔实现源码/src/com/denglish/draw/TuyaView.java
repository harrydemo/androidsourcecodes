package com.denglish.draw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @category: Viewʵ��Ϳѻ�������Լ���������
 * @author: лΰ
 * @date: 2012-11-9
 * 
 */

public class TuyaView extends View {

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;// �����Ļ���
	private Paint mPaint;// ��ʵ�Ļ���
	private float mX, mY;// ��ʱ������
	private static final float TOUCH_TOLERANCE = 4;

	// ����Path·���ļ���,��List������ģ��ջ
	private static List<DrawPath> savePath;
	// ��¼Path·���Ķ���
	private DrawPath dp;

	private int screenWidth, screenHeight;// ��Ļ����

	private class DrawPath {
		public Path path;// ·��
		public Paint paint;// ����
	}

	public TuyaView(Context context, int w, int h) {
		super(context);
		screenWidth = w;
		screenHeight = h;

		mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		// ����һ��һ�λ��Ƴ�����ͼ��
		mCanvas = new Canvas(mBitmap);

		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// �������Ե
		mPaint.setStrokeCap(Paint.Cap.SQUARE);// ��״
		mPaint.setStrokeWidth(8);// ���ʿ��
		mPaint.setColor(0xFF2145FF);// ������ɫ
		savePath = new ArrayList<DrawPath>();
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(0x0FFFFFFF);
		// ��ǰ���Ѿ���������ʾ����
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		if (mPath != null) {
			// ʵʱ����ʾ
			canvas.drawPath(mPath, mPaint);
		}
	}

	private void touch_start(float x, float y) {
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(mY - y);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			// ��x1,y1��x2,y2��һ�����������ߣ���ƽ��(ֱ����mPath.lineToҲ�ǿ��Ե�)
			// �ɴ˾Ϳ����������ֻ���
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		mCanvas.drawPath(mPath, mPaint);
		// ��һ��������·����������(�൱����ջ����)
		savePath.add(dp);
		mPath = null;// �����ÿ�
	}

	/**
	 * �����ĺ���˼����ǽ�������գ� ������������Path·�����һ���Ƴ����� ���½�·�����ڻ������档
	 */
	public void undo() {
		mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);// �������û������൱����ջ���
		// ��ջ������������ͼƬ�б����Ļ�����ʹ����������³�ʼ���ķ������ø÷����Ὣ������յ�...
		if (savePath != null && savePath.size() > 0) {
			// �Ƴ����һ��path,�൱�ڳ�ջ����
			savePath.remove(savePath.size() - 1);

			Iterator<DrawPath> iter = savePath.iterator();
			while (iter.hasNext()) {
				DrawPath drawPath = iter.next();
				mCanvas.drawPath(drawPath.path, drawPath.paint);
			}
			invalidate();// ˢ��

			/* �����ﱣ��ͼƬ������Ϊ�˷���,����ͼƬ������֤ */
			String fileUrl = Environment.getExternalStorageDirectory()
					.toString() + "/android/data/test.png";
			try {
				FileOutputStream fos = new FileOutputStream(new File(fileUrl));
				mBitmap.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * �����ĺ���˼����ǽ�������·�����浽����һ����������(ջ)�� Ȼ���redo�ļ�������ȡ����˶��� ���ڻ������漴�ɡ�
	 */
	public void redo() {
		// TODO
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ÿ��down��ȥ����newһ��Path
			mPath = new Path();
			// ÿһ�μ�¼��·�������ǲ�һ����
			dp = new DrawPath();
			dp.path = mPath;
			dp.paint = mPaint;
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

}
