package com.Aina.Android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.view.View;

public final class ViewlrcKLOK extends View implements Runnable {

	private final Paint paint;
	

	private Bitmap mBitlrcBack = null;
	private Bitmap mBitlrc     = null;
	private Canvas lrc_backCanvas =null;
	private Canvas lrc_Canvas = null;
	
	private Canvas lrc_backNextCanvas =null;
	private Bitmap mBitlrcNextBack = null;
	private Canvas lrc_Canvas1 = null;
	private Bitmap mBitlrc1    = null;
	
	private int    lrcWidth  = 0;
	private int    lrcWidth1  = 0;
	private int    lrcHeight = 0;
	private int    lrcSpeed  = 100;
	private int    lrcSpeed1 = 100;
	
	private int movex = 0;
	private int movex1 = 0;
	private int dispIndex = 0;
	private int mTimeduff = 0;
	
	private int DisplayWidth;//屏幕宽带
	private int DisplayHeight;//屏幕高度	

	
	private String lrc_content="这是一个测试"; //歌词内容
	private String lrc_Nextcontent=""; //歌词内容
	private int    lrc_timelen = 0;
	private int    lrc_timelen1 = 0;
	private Thread  ThreadKLOK =null;
	private int    nState = 0;
	public ViewlrcKLOK(Context context,AttributeSet attrs) {
		super(context,attrs);
		paint = new Paint();
		
		//DisplayMetrics displayMetrics = new DisplayMetrics();   
		//this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);   
		//DisplayHeight = displayMetrics.heightPixels;   
		//DisplayWidth = displayMetrics.widthPixels;
		ThreadKLOK = new Thread(this);
		ThreadKLOK.start();
	}
	
	  @Override
	  public void onDraw(Canvas canvas) {
	    Rect frame = new Rect();
 
	   // canvas.drawText("speed"+lrcSpeed, 2, 30, paint);
        if (mBitlrc==null)
        	return;
        
	    this.DrawImage(canvas, mBitlrcBack, 
			     2, 8,
			     lrcWidth,lrcHeight,
	 	         0,0);
	    
	    
	    this.DrawImage(canvas, mBitlrc, 
			     2, 8,
			     movex,lrcHeight,
	 	         0,0);
	    
	    this.DrawImage(canvas, mBitlrcNextBack, 
			     20, lrcHeight+15,
			     lrcWidth1,lrcHeight,
	 	         0,0);

	    this.DrawImage(canvas, mBitlrc1, 
	    		20, lrcHeight+15,
			     movex1,lrcHeight,
	 	         0,0);	    
	  }

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				if (nState==1)
				{
					if (dispIndex==0)
					{
						if (movex<lrcWidth)
							movex++;
						else
						{
							movex1 = 0;
							dispIndex = 1;
							Thread.sleep(mTimeduff);
						}
						Thread.sleep(lrcSpeed);
					}
					else
					{
						if (movex1<lrcWidth1)
							movex1++;	
						Thread.sleep(lrcSpeed1);				
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Thread.currentThread().interrupt();
			}
			this.postInvalidate();
		}
	}
	
	/**
	 * x:屏幕上的x坐标
	 * y:屏幕上的y坐标 
	 * w:要绘制的图片的宽度
	 * h:要绘制的图片的高度 
	 * bx:图片上的x坐标 
	 * by:图片上的y坐标
	 * 
	 * @param canvas
	 * @param mBitmap
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param bx
	 * @param by
	 */
	public void DrawImage(Canvas canvas, Bitmap mBitmap, int x, int y, int w,
			int h, int bx, int by) {
		Rect src = new Rect();// 图片裁剪区域
		Rect dst = new Rect();// 屏幕裁剪区域
		src.left = bx;
		src.top = by;
		src.right = bx + w;
		src.bottom = by + h;

		dst.left = x;
		dst.top = y;
		dst.right = x + w;
		dst.bottom = y + h;

		canvas.drawBitmap(mBitmap, src, dst, paint);
		src = null;
		dst = null;
	}
	

	public void SetlrcContent(String lrc,int tiemlen,int timeduff,String lrc_1,int timelen1)
	{
		ThreadKLOK.suspend();
		lrc_content = lrc;
		lrc_timelen  = tiemlen;
		lrc_timelen1 = timelen1;
		movex  = 0;
		movex1 = 0;
		dispIndex = 0;
		paint.setTextSize(24);
		Rect rect = new Rect();
		mTimeduff = timeduff;
		

		//返回包围整个字符串的最小的一个Rect区域
		paint.getTextBounds(lrc, 0, 1, rect); 
		
		float strwid = paint.measureText(lrc);
		float strwid1 = paint.measureText(lrc_1);
		lrcWidth  = (int)strwid;
		lrcWidth1 = (int)strwid1;
		lrcHeight = 23;
		if (lrc_timelen>0)
		   lrcSpeed  = (lrc_timelen/lrcWidth);
		else
			lrcSpeed = 1000;
		if (lrc_timelen1>0)
			lrcSpeed1  = (lrc_timelen1/lrcWidth1);
		else
			lrcSpeed1 = 1000;
		
		
		mBitlrcBack = Bitmap.createBitmap( lrcWidth,lrcHeight, Config.ARGB_8888 );
		mBitlrc     = Bitmap.createBitmap( lrcWidth,lrcHeight, Config.ARGB_8888 );

		mBitlrcNextBack = Bitmap.createBitmap(lrcWidth1,lrcHeight, Config.ARGB_8888);
		mBitlrc1        = Bitmap.createBitmap(lrcWidth1,lrcHeight, Config.ARGB_8888 );

		lrc_backCanvas  = new Canvas(mBitlrcBack);
		lrc_Canvas      = new Canvas(mBitlrc);
		lrc_Canvas1     = new Canvas(mBitlrc1);
		
		lrc_backNextCanvas = new Canvas(mBitlrcNextBack);

		/*
		paint.setColor(Color.GREEN);
		rect.top = 0;
		rect.left = 0;
		rect.right =lrcWidth;
		rect.bottom = lrcHeight;
		lrc_backCanvas.drawRect(rect, paint);
		*/
		paint.setColor(Color.WHITE);
	    lrc_backCanvas.drawText(lrc_content, 0, 20, paint);
	    lrc_backNextCanvas.drawText(lrc_1, 0, 20, paint);
	    
		paint.setColor(Color.RED);
		lrc_Canvas.drawText(lrc_content, 0, 20, paint);
		lrc_Canvas1.drawText(lrc_1, 0, 20, paint);
		
	}
	
	public void Star()
	{
		ThreadKLOK.resume();
		nState = 1;
	}
	
	public void Stop()
	{
		ThreadKLOK.suspend();
		nState =0;
	}
}
