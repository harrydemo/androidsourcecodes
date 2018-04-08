package com.qsa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class NewView extends View {

	protected static int getReturn = -1;

	private Paint mPaint = new Paint();

	// stone列表
	private BigStone[] mStones;

	int mode = NONE;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;

	private BigStone[] mMenus;
	private BigStone[] addMenus = new BigStone[MENUS];
	// 数目
	private static int STONE_COUNT = 5;

	private static int MENUS = 4;
	// 圆心坐标
	private float mPointX = 0, mPointY = 0;

	private int flagwai = 0;

	private int flag = 0;
	// 半径
	private int mRadius = 0;
	// 每两个点间隔的角度
	private int mDegreeDelta;

	private float maxX, maxY, minX, minY;

	public NewView(Context context, int px, int py, int radius) {
		super(context);

		mPointX = px;
		mPointY = py;
		mRadius = radius;

		setBackgroundResource(R.drawable.menubkground);
		setupStones();
		computeCoordinates();

	}

	float oldDist = 1f;
	PointF start = new PointF();
	PointF mid = new PointF();

	int change = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		dumpEvent(e);
		switch (e.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:

			for (int i = 0; i < STONE_COUNT; i++) {
				if (e.getX() >= mStones[i].x - 20
						&& e.getX() <= mStones[i].x + 40
						&& e.getY() >= mStones[i].y
						&& e.getY() <= mStones[i].y + 40) {

					if (i < 5) {
						getReturn = Integer.valueOf(mStones[i].text);
					} else {
						getReturn = Integer.valueOf(mStones[i].text) + 5;
					}
					Toast.makeText(getContext(), String.valueOf(getReturn),
							Toast.LENGTH_SHORT).show();

				}
			}

			mode = DRAG;
			return true;

		case MotionEvent.ACTION_POINTER_DOWN:

			oldDist = spacing(e);

			if (oldDist > 100f) {

				mode = ZOOM;

			}

			return true;

		case MotionEvent.ACTION_MOVE:

			GetMaxMin(e);

			if (mode == DRAG) {

				// System.out.println("move:mode == DRAG");
			} else if (mode == ZOOM) {
				float newDist = spacing(e);

				if (newDist > 100f) {
					/*
					 * if (change == 0) { change = 1;
					 * 
					 * resetStonesAngle(e.getX(), e.getY());
					 * computeCoordinates(); invalidate(); }
					 */
				}
			}
			int a = 0;
			for (int i = 0; i < MENUS; i++) {
				if (e.getX() > mMenus[i].x - 40 && e.getX() < mMenus[i].x + 40
						&& e.getY() > mMenus[i].y - 40
						&& e.getY() < mMenus[i].y + 40) {

					mMenus[i].x = e.getX();
					mMenus[i].y = e.getY();

					flag = 1;
					computeCoordinates();
					postInvalidate();

					if (e.getX() < maxX && e.getX() > minX && e.getY() < maxY
							&& e.getY() > minY) {

						if (mMenus[i].isVisible) {

							for (int j = 0; j < MENUS; j++) {
								if (addMenus[j] == null && a == 0) {
									addMenus[j] = mMenus[i];

									a = 1;

								}

							}
							STONE_COUNT++;
							mDegreeDelta = 360 / STONE_COUNT;
							mStones = new BigStone[STONE_COUNT];
							flagwai = 1;
							int angle = 0;
							BigStone stone;
							for (int index = 0; index < STONE_COUNT; index++) {
								stone = new BigStone();

								if (index < 5) {
									stone.bitmap = BitmapFactory
											.decodeResource(getResources(),
													R.drawable.menu1 + index);
									stone.text = String.valueOf(1 + index);
								} else {
									stone.bitmap = addMenus[index - 5].bitmap;

									stone.text = addMenus[index - 5].text;
								}
								stone.angle = angle;
								angle += mDegreeDelta;
								mStones[index] = stone;

							}
							mMenus[i].isVisible = false;
						}
					}
					break;
				}
			}

			if (e.getX() < maxX && e.getX() > minX && e.getY() < maxY
					&& e.getY() > minY) {

				if (e.getX() < maxX - 80 && e.getX() > minX + 81
						&& e.getY() < maxY - 80 && e.getY() > minY + 81) {

					mPointX = e.getX();
					mPointY = e.getY();

				}

				resetStonesAngle(e.getX(), e.getY());
				computeCoordinates();
				invalidate();
			}

			break;

		case MotionEvent.ACTION_UP:
			// System.out.println("ACTION_UP:" + e.getX() + " " + e.getY());

			// Log.d(TAG, "dispatchTouchEvent action:ACTION_UP");

			break;

		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			// System.out.println("Pointer_up:	mode = NONE;");
			// Log.d(TAG, "dispatchTouchEvent action:ACTION_CANCEL");

			break;

		}
		return super.dispatchTouchEvent(e);
	}

	private void GetMaxMin(MotionEvent e) {
		float tempx;
		float tempy;
		for (int i = 0; i < STONE_COUNT; i++) {
			for (int j = 0; j < STONE_COUNT; j++) {

				if (mStones[i].x < mStones[j].x) {
					tempx = mStones[i].x;
					mStones[i].x = mStones[j].x;
					mStones[j].x = tempx;
				}
				if (mStones[i].y < mStones[j].y) {
					tempy = mStones[i].y;
					mStones[i].y = mStones[j].y;
					mStones[j].y = tempy;
				}
			}
		}
		maxX = mStones[STONE_COUNT - 1].x;
		minX = mStones[0].x;
		maxY = mStones[STONE_COUNT - 1].y;
		minY = mStones[0].y;

		// System.out.println(maxX+" "+minX+" "+maxY+" "+minY);
	}

	@Override
	public void onDraw(Canvas canvas) {

		Paint paint = new Paint();

		paint.setAntiAlias(true);

		paint.setColor(Color.WHITE);

		paint.setStyle(Paint.Style.FILL);

		paint.setAlpha(0x30);
		if (change == 0) {
			canvas.drawCircle(mPointX, mPointY, mRadius - 80, paint);
		}
		// xiaoyuan

		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setAlpha(0x30);
		if (change == 0) {
			canvas.drawCircle(mPointX, mPointY, mRadius + 41, paint); // dayuan
		}

		if (change == 1) {
			// manzu yi ge tiao jian bu huizhi yuanpan caidan ,hui zhi juxing
			// caidan
			canvas.drawRect(800, 240, 0, 140, paint);
		}
		for (int i = 0; i < MENUS; i++) {
			if (!mMenus[i].isVisible)
				continue;
			drawMenus(canvas, mMenus[i].bitmap, mMenus[i].x, mMenus[i].y);
		}

		for (int index = 0; index < STONE_COUNT; index++) {
			if (!mStones[index].isVisible)
				continue;
			drawInCenter(canvas, mStones[index].bitmap, mStones[index].x,
					mStones[index].y, mStones[index].text);

		}
	}

	void drawMenus(Canvas canvas, Bitmap b, float x, float y) {

		// System.out.println(x+" "+y);
		// System.out.println(":"+b.getHeight()+" "+b.getWidth());
		canvas.drawBitmap(b, x - b.getWidth() / 2, y - b.getHeight() / 2, null); // tubiao
		// System.out.println(x + " " + y);
	}

	void drawInCenter(Canvas canvas, Bitmap bitmap, float left, float top,
			String text) {

		canvas.drawText(text, left, top, mPaint);
		canvas.drawBitmap(bitmap, left - bitmap.getWidth() / 2,
				top - bitmap.getHeight() / 2, null);

	}

	private void resetStonesAngle(float x, float y) {

		int angle = computeCurrentAngle(x, y);
		// Log.d("RoundSpinView", "angle:" + angle);
		for (int index = 0; index < STONE_COUNT; index++) {
			mStones[index].angle = angle;
			angle += mDegreeDelta;
		}
	}

	private int computeCurrentAngle(float x, float y) {
		float distance = (float) Math
				.sqrt(((x - mPointX) * (x - mPointX) + (y - mPointY)
						* (y - mPointY)));
		int degree = (int) (Math.acos((x - mPointX) / distance) * 180 / Math.PI);
		if (y < mPointY) {
			degree = -degree;
		}

		// Log.d("RoundSpinView", "x:" + x + ",y:" + y + ",degree:" + degree);
		return degree;
	}

	private void computeCoordinates() {
		BigStone stone;
		BigStone menus;
		for (int index = 0; index < STONE_COUNT; index++) {
			stone = mStones[index];
			stone.x = mPointX
					+ (float) (mRadius * Math.cos(stone.angle * Math.PI / 180));
			stone.y = mPointY
					+ (float) (mRadius * Math.sin(stone.angle * Math.PI / 180));
		}
		if (flag == 0) {
			for (int i = 0; i < MENUS; i++) {

				menus = mMenus[i];

				switch (i) {
				case 0:
					menus.x = 300 * 1.8f;
					menus.y = 50;
					break;
				case 1:
					menus.x = 300 * 1.8f + 100;
					menus.y = 50;
					break;
				case 2:
					menus.x = 300 * 1.8f + 200;
					menus.y = 50;
					break;
				case 3:
					menus.x = 300 * 1.8f;
					menus.y = 150;
					break;
				case 4:
					menus.x = 300 * 1.8f + 10 + 100;
					menus.y = 250;
					break;

				}

			}
		}
	}

	private void setupStones() {
		mStones = new BigStone[STONE_COUNT];
		mMenus = new BigStone[MENUS];
		BigStone stone;
		BigStone menus;
		int angle = 0;
		mDegreeDelta = 360 / STONE_COUNT;
		if (flagwai == 0) {

			for (int i = 0; i < MENUS; i++) {
				menus = new BigStone();
				menus.bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.menu6 + i);
				menus.text = String.valueOf(1 + i);
				mMenus[i] = menus;

			}
		}

		for (int index = 0; index < STONE_COUNT; index++) {
			stone = new BigStone();
			stone.angle = angle;
			stone.bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.menu1 + index);

			stone.text = String.valueOf(1 + index);
			angle += mDegreeDelta;

			mStones[index] = stone;

		}

	}

	int getReturn() {

		return getReturn;
	}

	void unVisbile(int a) {

		mMenus[a].isVisible = false;

	}

	class BigStone {

		// 图片
		Bitmap bitmap;

		// 角度
		int angle;

		// x坐标
		float x;

		// y坐标
		float y;

		String text;
		// 是否可见
		boolean isVisible = true;
	}

	private float spacing(MotionEvent event) {

		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return FloatMath.sqrt(x * x + y * y);

	}

	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
				"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("pid ").append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount()) {
				sb.append(";");
			}
		}
		sb.append("]");

	}

}
