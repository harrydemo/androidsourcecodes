package cn.knight.barchart ;

import android.graphics.Canvas ;
import android.graphics.Paint ;

public class Chart {

	private final int w = 20 ;
	private int h ;
	private final int total_y = 300 ;
	private int x ;

	public int getX() {
		return x ;
	}

	public void setX(int x) {
		this.x = x ;
	}

	public int getH() {
		return h ;
	}

	public void setH(int h) {
		this.h = h ;
	}

	public void drawSelf(Canvas canvas, Paint paint) {
		canvas.drawRect(x, total_y - h, w + x, total_y - 1, paint) ;
	}

}
