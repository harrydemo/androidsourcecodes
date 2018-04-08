package com.bn.summer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GGView3 extends View{

	int COMPONENT_WIDTH;
	int COMPONENT_HEIGHT;
	boolean initflag=false;	
	Bitmap[] bma;	
	Paint paint;
	int[] drawablesId;
	int currIndex=0;
	boolean workFlag=true;
	
	public GGView3(Context father,AttributeSet as) 
	{ 
		super(father,as);	
		this.drawablesId=new int[]
		{
			R.drawable.room1,	
			R.drawable.room2,	
			R.drawable.room3,		
		};
		bma=new Bitmap[drawablesId.length];
		initBitmaps();
		paint=new Paint();
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿		
		
		new Thread()
		{
			public void run()
			{
				while(workFlag)
				{
					currIndex=(currIndex+1)%drawablesId.length;
					GGView3.this.postInvalidate();
					try 
					{
						Thread.sleep(2500);
					} catch (InterruptedException e) 
					{						
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public void initBitmaps()
	{
		Resources res=this.getResources();
		for(int i=0;i<drawablesId.length;i++)
		{
			bma[i]=BitmapFactory.decodeResource(res, drawablesId[i]);
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if(!initflag)
		{
			COMPONENT_WIDTH=this.getWidth();//获取view的宽度
			COMPONENT_HEIGHT=this.getHeight();//获取view的高度
			initflag=true;
		}
		
		int picWidth=bma[currIndex].getWidth();
		int picHeight=bma[currIndex].getHeight();
		
		int startX=(COMPONENT_WIDTH-picWidth)/2;
		int startY=(COMPONENT_HEIGHT-picHeight)/2;
		
		//绘制背景色
		//canvas.drawARGB(255, 244, 214, 116);
		canvas.drawARGB(255, 200, 128, 128);
		
		canvas.drawBitmap(bma[currIndex], startX,startY, paint);
	}
	
}
