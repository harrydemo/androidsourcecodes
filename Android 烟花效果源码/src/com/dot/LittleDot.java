package com.dot;


public class LittleDot {
	// 这是烟花爆破时的碎点
	int x = 0;
	int y = 0;

	int color;

	int horizontal = 0; // 水平方向的速度

	int plumb = 0;

	public LittleDot(int x, int y, int col) {
		// x,y是随机分配的
		this.x = x;
		this.y = y;
		this.color = col;
	}

	public void setPlace() {
		x = x + horizontal;
		y = y + plumb;
		if (horizontal > 3)
			horizontal = horizontal - 2;

		if (plumb > 3)
			plumb = plumb - 2;
	}

	public void setPace(int wallop, int pointX, int pointY) {
		// 先计算斜边的长度
		double hypotenuse = Math.hypot(Math.abs(y * 1.0 - pointY),
				Math.abs(x * 1.0 - pointX));

		this.horizontal = (int) ((Math.abs(x * 1.0 - pointX) / hypotenuse) * wallop)
				+ (int) ((Math.random() * 10));

		this.plumb = (int) ((Math.abs(y * 1.0 - pointY) / hypotenuse) * wallop)
				+ (int) (Math.random() * 10);

		if (x < pointX) {
			this.horizontal = this.horizontal * -1;
		}

		if (y < pointY) {
			this.plumb = this.plumb * -1;
		}

	}
	
	public void setPace(int paceX, int paceY) {
		this.horizontal = paceX;
		this.plumb = paceY;
	}

}
