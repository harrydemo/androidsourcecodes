package com.dot;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;

public class DotFour extends Dot {

	public DotFour(Context context, int color, int endX, int entY) {
		super(context, color, endX, entY);
		// TODO Auto-generated constructor stub
		pace = 20;
	}

	@Override
	public LittleDot[] initBlast() {
		// TODO Auto-generated method stub
		// 初始化爆炸的情况
		final int ONE = 20; // 这是爆炸的第一圈的半径

		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);

		int col = 0xff000000 | red << 16 | green << 8 | blue;

		Random rand = new Random();
		int blastX = endPoint.x - ONE * circle;
		int blastY = endPoint.y - ONE * circle;

		if (Math.random() < 0.3) {
			for (int i = 0; i < ld.length; i++) {
				ld[i] = new LittleDot(rand.nextInt(ONE * circle * 2) + blastX,
						rand.nextInt(ONE * circle * 2) + blastY, col);
				ld[i].setPace(WALLOP, endPoint.x, endPoint.y);
			}
		} else {
			for (int i = 0; i < ld.length; i++) {
				ld[i] = new LittleDot(rand.nextInt(ONE * circle * 2) + blastX,
						rand.nextInt(ONE * circle * 2) + blastY, color);
				ld[i].setPace(WALLOP, endPoint.x, endPoint.y);
			}// for
		}
		color = 0xff000000 | 225 << 16 | 203 << 8 | 114;
		size = 10;
		state = 3;
		return ld;
	}

	@Override
	public LittleDot[] blast() {
		// TODO Auto-generated method stub
		// 处理爆炸的情况
		if (circle >= maxCircle - 1) {
			for (int i = 0; i < ld.length; i++) {
				// 前三圈按照下面的代码来计算爆炸点的碎花的位置
				ld[i].setPlace();

			}
		} else {
			for (int i = 0; i < ld.length; i++) {
				// 三圈后就碎花就开始下坠
				ld[i].setPace(ld[i].horizontal, GRAVITY);
				ld[i].setPlace();
			}
		}
		if (circle >= maxCircle) {

			for (int i = 0; i < ld.length; i++) {
				int red = (int) (Math.random() * 255);
				int green = (int) (Math.random() * 255);
				int blue = (int) (Math.random() * 255);
				int col = 0xff000000 | red << 16 | green << 8 | blue;
				ld[i].color = col;
			}
		}
		return ld;
	}

	public void myPaint(Canvas canvas, Vector<Dot> lList) {

		super.myPaint(canvas, lList);
	}
}
