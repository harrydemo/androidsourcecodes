package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class StrengthBar {
	//关于力度条图的量
	private Bitmap downBmp;//位图
	private Bitmap pointerBmp;
	final float x=Constant.BAR_X+Constant.X_OFFSET;//力度条的实际位置
	final float y=Constant.BAR_Y+Constant.Y_OFFSET;	
	private float width;//力度条宽度
	private float height;//力度条总高度		
	private float currHeight;//力度条当前高度
	private int extendX=50;//虚拟按钮触控面积扩大的尺寸
	private int extendY=10;//虚拟按钮触控面积扩大的尺寸
	//关于彩虹条的量
	private float rainbowWidth=Constant.RAINBOW_WIDTH;//彩虹条宽度
	private float rainbowHeight=Constant.RAINBOW_HEIGHT;//彩虹条高度
	private float rainbowGap=Constant.RAINBOW_GAP;//彩虹条间隙
	private float rainbowX;//第一个彩虹条的位置
	private float rainbowY;
	//关于指示针的量
	private float pointerWidth;
	private float pointerHeight;
	public StrengthBar(Bitmap downBmp,Bitmap pointerBmp)
	{
		this.downBmp=downBmp;
		this.pointerBmp=pointerBmp;
		this.width=downBmp.getWidth();
		this.height=downBmp.getHeight();
		currHeight=downBmp.getHeight();
		rainbowX=Constant.RAINBOW_X+Constant.X_OFFSET;;
		rainbowY=height+Constant.RAINBOW_Y+Constant.Y_OFFSET;
		pointerWidth=pointerBmp.getWidth();
		pointerHeight=pointerBmp.getHeight();
	}
	//绘制力度条的方法
	public void drawSelf(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(downBmp, x, y,paint);//绘制底下的条
		
		int n=(int) (currHeight/(rainbowHeight+rainbowGap));//求彩虹条个数
		n=(n>ColorUtil.result.length)? ColorUtil.result.length :n;//选n和数组长度中最小的值为n赋值
		//绘制彩虹条
		for(int i=0;i<n && i<ColorUtil.result.length;i++)
		{
			int[] c=ColorUtil.getColor(i);
			paint.setARGB(255, c[0], c[1], c[2]);
			float yTemp=rainbowY-(i*(rainbowHeight+rainbowGap));
			canvas.drawRect(rainbowX, yTemp, rainbowX+rainbowWidth, yTemp+rainbowHeight, paint);
		}
		//绘制三角指示针
		canvas.drawBitmap(pointerBmp, x-pointerWidth, rainbowY-((n-1)*(rainbowHeight+rainbowGap))-pointerHeight/2,paint);//绘制底下的条
	}
	//增加力度的方法
	public void changeCurrHeight(float pressX, float pressY)
	{
		if(pressY<=y){//按下的位置在力度条往上
			currHeight=this.height;
		}
		else if(pressY>=y+this.height){//按下的位置在力度条以下
			currHeight=0;
		}
		else{//按下的位置在力度条中间
			currHeight=this.height-(pressY-y);
		}
	}
	//判断按钮是否有触控事件的方法
	public boolean isActionOnBar(float pressX,float pressY)
	{
		return Constant.isPointInRect(pressX, pressY, 
				x-extendX, y-extendY, width+2*extendX, height+2*extendY);
	}
	public float getCurrHeight() {
		return currHeight;
	}
	public float getHeight() {
		return height;
	}
	
}
