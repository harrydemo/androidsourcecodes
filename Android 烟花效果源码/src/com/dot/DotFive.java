package com.dot;

import android.content.Context;
import android.graphics.Color;

public class DotFive extends Dot {

	public DotFive(Context context, int color, int endX, int entY) {
		super(context, color, endX, entY);
		// TODO Auto-generated constructor stub
		pace = 20;
	}

	public double qiu(int data1, int data2) {
		int xx = Math.abs(data1 - endPoint.x);
		int yy = Math.abs(data2 - endPoint.y);
		return Math.sqrt(xx * xx + yy * yy);
	}

	@Override
	public LittleDot[] blast() {
		// TODO Auto-generated method stub
		for (int i = 0; i < ld.length; i++) {
			int lineLong = (int) qiu(ld[i].x, ld[i].y) / 3;

			int level;

			if (ld[i].x <= endPoint.x) {
				if (ld[i].y <= endPoint.y) {
					level = 1;
				} else {
					level = 2;
				}
			} else {
				if (ld[i].y <= endPoint.y) {
					level = 3;
				} else {
					level = 4;
				}
			}

			switch (level) {
			case 1:
				ld[i].x += lineLong / 4;
				ld[i].y -= lineLong / 2;
				break;

			case 2:
				ld[i].x -= lineLong / 2;
				ld[i].y -= lineLong / 4;
				break;

			case 3:
				ld[i].x += lineLong / 2;
				ld[i].y += lineLong / 4;
				break;
			case 4:
				ld[i].x -= lineLong / 4;
				ld[i].y += lineLong / 2;
			default:
				break;
			}

		}
		if (circle >= maxCircle) {
			for (int i = 0; i < ld.length; i++) {
				ld[i].color = Color.WHITE;
			}
		}
		return ld;
	}

	@Override
	public LittleDot[] initBlast() {
		// TODO Auto-generated method stub
		// 初始化爆炸的情况,初始化爆炸得粒子

		int[] lineOne = new int[] { endPoint.x - 50, endPoint.y };
		int[] lineTwo = new int[] { endPoint.x + 50, endPoint.y };
		int[] lineThree = new int[] { endPoint.x, endPoint.y - 50 };
		int[] lineFour = new int[] { endPoint.x, endPoint.y + 50 };
		// 35是50/2得开根求得得
		int[] lineFive = new int[] { endPoint.x - 35, endPoint.y - 35 };
		int[] lineSix = new int[] { endPoint.x + 35, endPoint.y + 35 };
		int[] lineSeven = new int[] { endPoint.x - 35, endPoint.y + 35 };
		int[] lineEight = new int[] { endPoint.x + 35, endPoint.y - 35 };

		// 每条线是200/8＝25
		int red = (int) (Math.random() * 255);
		int green = (int) (Math.random() * 255);
		int blue = (int) (Math.random() * 255);
		int[] cols = new int[8];
		for (int i = 0; i < cols.length; i++) {
			red = (int) (Math.random() * 255);
			green = (int) (Math.random() * 255);
			blue = (int) (Math.random() * 255);
			cols[i] = 0xff000000 | red << 16 | green << 8 | blue;
		}

		for (int i = 0; i < ld.length; i++) {
			if (i < 25) {
				ld[i] = new LittleDot(lineOne[0] + 2 * i, lineOne[1], cols[0]);

			} else if (i < 50) {
				ld[i] = new LittleDot(lineTwo[0] - 2 * (i - 25), lineTwo[1],
						cols[1]);

			} else if (i < 75) {
				ld[i] = new LittleDot(lineThree[0],
						lineThree[1] + 2 * (i - 50), cols[2]);

			} else if (i < 100) {
				ld[i] = new LittleDot(lineFour[0], lineFour[1] - 2 * (i - 75),
						cols[3]);

			} else if (i < 125) {
				ld[i] = new LittleDot(lineFive[0] + (int) (1.4 * (i - 100)),
						lineFive[1] + (int) (1.4 * (i - 100)), cols[4]);

			} else if (i < 150) {
				ld[i] = new LittleDot(lineSix[0] - (int) (1.4 * (i - 125)),
						lineSix[1] - (int) (1.4 * (i - 125)), cols[5]);

			} else if (i < 175) {
				ld[i] = new LittleDot(lineSeven[0] + (int) (1.4 * (i - 150)),
						lineSeven[1] - (int) (1.4 * (i - 150)), cols[6]);

			} else if (i < 200) {
				ld[i] = new LittleDot(lineEight[0] - (int) (1.4 * (i - 175)),
						lineEight[1] + (int) (1.4 * (i - 175)), cols[7]);
			}
		}
		return ld;
	}
}
