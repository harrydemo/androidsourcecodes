package com.dot;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class DotSix extends Dot {

	public DotSix(Context context,int color, int endX, int entY) {
		super(context, color, endX, entY);
		// TODO Auto-generated constructor stub
		pace = 20;
	}

	@Override
	public LittleDot[] blast() {
		// TODO Auto-generated method stub
		int j = -1;
		for (int i = 0; i < ld.length; i++) {
			j = -j;
			ld[i].x += (ld[i].x - endPoint.x) / 4 + j;
			ld[i].y += (ld[i].y - endPoint.y) / 4 + 1;
		}

		return ld;
	}

	@Override
	public LittleDot[] initBlast() {
		// TODO Auto-generated method stub
		// 初始化粒子
		// 只要分成20
		// 初始化爆炸的情况
		Random rand = new Random();

		for (int i = 0; i < ld.length; i++) {
			int x = rand.nextInt(20);
			int y = rand.nextInt(20);

			if (Math.random() < 0.5) {
				x = -x;
			}

			if (Math.random() < 0.5) {
				y = -y;
			}

			if (x * x + y * y > 400) {
				x -= x / Math.abs(x) * 10;
				y -= y / Math.abs(y) * 10;
			}
			ld[i] = new LittleDot(x + endPoint.x, y + endPoint.y, color);
		}
		return ld;
	}

	public void myPaint(Canvas canvas, Vector<Dot> lList) {
		Paint mPaint = new Paint();
		RectF oval = new RectF(x, y, x + size, y + size);
		if (state == 1) {
			mPaint.setColor(color);
//			canvas.drawOval(oval, mPaint);
			if (mFireAnim != null) {
				mFireAnim.DrawAnimation(canvas, null, x, y);
			}

		} else if (state == 2) {
			mPaint.setColor(color);
			canvas.drawOval(oval, mPaint);
			LittleDot[] ld2 = initBlast();
			ld = new LittleDot[ld2.length];
			ld = ld2;
			for (int i = 0; i < ld.length; i++) {
				mPaint.setColor(ld[i].color);
				canvas.drawLine(ld[i].x, ld[i].y, ld[i].x
						+ (ld[i].x - endPoint.x) / 2, ld[i].y
						+ (ld[i].y - endPoint.y) / 2, mPaint);
			}
			state = 3;
		} else if (state == 3) {
			if (circle < maxCircle) {
				circle++;
				this.ld = blast();
				for (int i = 0; i < ld.length; i++) {
					mPaint.setColor(ld[i].color);
					canvas.drawLine(ld[i].x, ld[i].y, ld[i].x
							+ (ld[i].x - endPoint.x) / circle, ld[i].y
							+ (ld[i].y - endPoint.y) / circle, mPaint);
				}
			} else {
				for (int i = 0; i < ld.length; i++) {
					mPaint.setColor(ld[i].color);
					mPaint.setAlpha(20 + (int) (Math.random() * 0xff));
					canvas.drawLine(ld[i].x, ld[i].y, ld[i].x
							+ (ld[i].x - endPoint.x) / circle, ld[i].y
							+ (ld[i].y - endPoint.y) / circle, mPaint);
				}
			}

		} else if (state == 4) {
			synchronized (lList) {
				lList.remove(this);
			}
		}
	}
}
