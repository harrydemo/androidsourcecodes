package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 
 * 设置声音的虚拟按钮
 *
 */
public class SoundSwitchButton {
	//关于按钮的量
	Bitmap onBitmap;
	Bitmap offBitmap;
	float btnX;
	float btnY;	
	float btnWidth;
	float btnHeight;
	//关于文字的量
	Bitmap onTextBitmap;
	Bitmap offTextBitmap;
	float textX;
	float textY;
	float textWidth;
	float textHeight;
	//其它量
	float space;//文字与按钮间距
	private boolean isOn=false;//按钮开关标志
	public SoundSwitchButton(Bitmap onTextBitmap,Bitmap offTextBitmap,Bitmap onBitmap,Bitmap offBitmap,float textX,float textY,boolean isOn)
	{		
		this.onTextBitmap=onTextBitmap;
		this.offTextBitmap=offTextBitmap;
		this.onBitmap=onBitmap;
		this.offBitmap=offBitmap;
		this.textX=textX;
		this.textY=textY;
		this.textWidth=onTextBitmap.getWidth();
		this.textHeight=onTextBitmap.getWidth();
		
		this.btnX=textX+textWidth+space;
		this.btnY=textY;
		this.btnWidth=onBitmap.getWidth();
		this.btnHeight=onBitmap.getWidth();
		this.isOn=isOn;
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{		
		if(isOn)
		{
			canvas.drawBitmap(onTextBitmap, textX, textY, paint);//绘制文字
			canvas.drawBitmap(onBitmap, btnX, btnY, paint);//绘制虚拟按钮
		}
		else
		{
			canvas.drawBitmap(offTextBitmap, textX, textY, paint);//绘制文字
			canvas.drawBitmap(offBitmap, btnX, btnY, paint);//绘制虚拟按钮
		}
	}
	//判断左按钮是否有触控事件的方法
	public boolean isActionOnButtonOn(int pressX,int pressY)
	{
		return Constant.isPointInRect(pressX, pressY, btnX, btnY, btnWidth/2.0f, btnHeight);
	}
	//判断右按钮是否有触控事件的方法
	public boolean isActionOnButtonOff(int pressX,int pressY)
	{
		return Constant.isPointInRect(pressX, pressY, btnX+btnWidth/2.0f, btnY, btnWidth/2.0f, btnHeight);
	}
	public void switchOn()
	{
		isOn=true;
	}
	public void switchOff()
	{
		isOn=false;
	}
}
