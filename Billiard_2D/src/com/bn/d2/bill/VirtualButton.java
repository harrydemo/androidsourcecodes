package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 
 * 虚拟按钮类
 *
 */
public class VirtualButton {
	float x;
	float y;
	int width;
	int height;
	Bitmap downBmp;
	Bitmap upBmp;
	boolean isDown=false;
	private int extendSize=20;//虚拟按钮触控面积扩大的尺寸
	public VirtualButton(Bitmap downBmp,Bitmap upBmp,float x,float y)
	{
		this.downBmp=downBmp;
		this.upBmp=upBmp;
		this.x=x+Constant.X_OFFSET;//将相对位置转换成实际位置
		this.y=y+Constant.Y_OFFSET;
		this.width=upBmp.getWidth();
		this.height=upBmp.getHeight();
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{
		if(isDown)
		{
			canvas.drawBitmap(downBmp, x, y, paint);
		}
		else
		{
			canvas.drawBitmap(upBmp, x, y, paint);
		}
	}
	public void pressDown()
	{
		isDown=true;
	}
	public void releaseUp()
	{
		isDown=false;
	}
	//判断按钮是否有触控事件的方法
	public boolean isActionOnButton(float pressX,float pressY)
	{
		return Constant.isPointInRect(pressX, pressY, 
				x-extendSize, y-extendSize, width+2*extendSize, height+2*extendSize);
	}
}
