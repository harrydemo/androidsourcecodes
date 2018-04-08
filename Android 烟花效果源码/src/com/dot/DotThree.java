package com.dot;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;

public class DotThree extends Dot {

	public DotThree(Context context, int color, int endX, int entY) {
		super(context, color, endX, entY);
		// TODO Auto-generated constructor stub
		pace = 20; // 上升速度，每种烟火可以指定不同的上升速度
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
		if (Math.random() < 0.5) {
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
			}
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
		final int EVERY = 40;// 爆炸的每圈的半径
		Random rand = new Random();
		if (circle >= maxCircle) {

			for (int i = 0; i < ld.length; i++) {
				int red = (int) (Math.random() * 255);
				int green = (int) (Math.random() * 255);
				int blue = (int) (Math.random() * 255);
				int col = 0xff000000 | red << 16 | green << 8 | blue;
				ld[i].color = col;
			}
		}

		for (int i = 0; i < ld.length; i++) {
			ld[i].x += rand.nextInt(EVERY) - EVERY / 2;
			ld[i].y += rand.nextInt(EVERY) - EVERY / 2;

		}
		return ld;
	}

	public void myPaint(Canvas canvas, Vector<Dot> lList) {

		super.myPaint(canvas, lList);
	}

}
