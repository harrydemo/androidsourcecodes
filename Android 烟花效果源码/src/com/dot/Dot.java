package com.dot;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.fireview.Animation;
import com.firework.R;

//所有烟花式样的父类，方便以后添加烟花式样
public abstract class Dot {
	int x = 30; // 该点的横坐标
	int y = 30; // 该点的竖坐标

	int color; // 该点的颜色

	int pace = 30; // 该点上升的速度
	int size = 6; // 点的大小

	final Point endPoint = new Point(); // 记录该点完结的位置

	// 记录该点的状态，就是是否是爆破状态,1是上升状态，2是爆炸状
	// 态的初始化，3是爆炸状态，4是消失状态
	public int state = 1;
	public int circle = 1;

	LittleDot[] ld = new LittleDot[200];

	int WALLOP = 20; // 设爆炸后每个点所受到的冲击力是10N
	int GRAVITY = 20;

	Animation mFireAnim = null;
	Context mContext;
	
	int maxCircle;

	public Dot(Context context, int color, int endX, int endY) {
		circle = 1;
		state = 1;
		pace = 25;

		this.x = endX;
		this.y = 800;
		this.color = color;
		endPoint.x = endX;
		endPoint.y = endY;
		mContext = context;
		 new loadFireSrc().start();// 用线程去加载图片数据，避免数据量过大造成屏幕一卡一卡的
//		new AnmiLoad().execute();
		 
		 maxCircle = new Random().nextInt(6) + 5;
	}

	class loadFireSrc extends Thread {
		public void run() {
			mFireAnim = new Animation(mContext, new int[] { R.drawable.trail1,
					R.drawable.trail2, R.drawable.trail3, R.drawable.trail4,
					R.drawable.trail5, R.drawable.trail6 }, true);
		}
	}


	public void rise() {
		// 处理上升的的情况
		if (mFireAnim != null) {
			y -= pace;
			// 确保x轴不变
			x = x * 1;

			if (y <= endPoint.y) {
				y = endPoint.y;

			}

			if (x <= endPoint.x) {
				x = endPoint.x;
			}

		}
		
	}

	// 判断是否爆炸
	public boolean whetherBlast() {
		// 判断是否爆炸
		if (y <= endPoint.y && x <= endPoint.x) {
			return true;
		}
		return false;
	}

	// 初始化爆炸的情况
	public abstract LittleDot[] initBlast();

	// 处理爆炸的情况
	public abstract LittleDot[] blast();

	public void myPaint(Canvas canvas, Vector<Dot> lList) {
		Paint mPaint = new Paint();
		mPaint.setColor(color);
		RectF oval = new RectF(x, y, x + size, y + size);
		if (state == 1) {
			if (mFireAnim != null) {
				mFireAnim.DrawAnimation(canvas, mPaint, x, y);
			}
		}
		if (state == 2) {
			canvas.drawOval(oval, mPaint);
			LittleDot[] ld2 = initBlast();
			ld = new LittleDot[ld2.length];
			ld = ld2;

			for (int i = 0; i < ld.length / 4; i++) {
				mPaint.setColor(ld[i].color);
				oval = new RectF(ld[i].x, ld[i].y, ld[i].x + 2, ld[i].y + 2);
				canvas.drawOval(oval, mPaint);
			}
			state = 3;

		} else if (state == 3) {
			if (circle <= maxCircle) {
				circle++;
				this.ld = blast();
				for (int i = 0; i < ld.length; i++) {
					mPaint.setColor(ld[i].color);
					oval = new RectF(ld[i].x, ld[i].y, ld[i].x + 2, ld[i].y + 2);
					canvas.drawOval(oval, mPaint);
				}
			} else {
				//滞留在空中，并且闪烁
				for (int i = 0; i < ld.length; i++) {
					mPaint.setColor(ld[i].color);
					mPaint.setAlpha(20 + (int) (Math.random() * 0xff));
					oval = new RectF(ld[i].x, ld[i].y, ld[i].x + 2, ld[i].y + 2);
					canvas.drawOval(oval, mPaint);
				}
			}

		} else if (state == 4) {
			synchronized (lList) {
				lList.remove(this);
			}
		}
	}

	public String toString() {
		return "该点现在得位置是x＝" + x + ",y=" + y + "," + "爆炸点是x=" + endPoint.x
				+ ",y=" + endPoint.y + "颜色是" + color;
	}
}
