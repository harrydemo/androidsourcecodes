package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * 显示百纳科技的动画界面
 *
 */
public class WellcomeView extends SurfaceView 
implements SurfaceHolder.Callback  //实现生命周期回调接口
{
	GameActivity activity;
	Paint paint;//画笔
	int currentAlpha=0;//当前的不透明值
	
	int screenWidth=Constant.SCREEN_WIDTH;//屏幕宽度
	int screenHeight=Constant.SCREEN_HEIGHT;//屏幕高度
	int sleepSpan=50;//动画的时延ms
	
	Bitmap[] logos=new Bitmap[2];//logo图片数组
	Bitmap currentLogo;//当前logo图片引用
	int currentX;
	int currentY;
	
	public WellcomeView(GameActivity activity) {
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);//设置生命周期回调接口的实现者
		paint = new Paint();//创建画笔
		paint.setAntiAlias(true);//打开抗锯齿
		//加载图片
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dukea); 
		logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dukeb);		
		for(int i=0;i<logos.length;i++){
			logos[i]=PicLoadUtil.scaleToFit(logos[i], Constant.ssr.ratio);
		}
	}
	public void onDraw(Canvas canvas){	
		//绘制黑填充矩形清背景
		paint.setColor(Color.BLACK);//设置画笔颜色
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
		
		//进行平面贴图
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}
	public void surfaceCreated(SurfaceHolder holder) {//创建时被调用		
		new Thread()
		{
			public void run()
			{
				for(Bitmap bm:logos)
				{
					currentLogo=bm;
					//计算图片位置
					currentX=screenWidth/2-bm.getWidth()/2;
					currentY=screenHeight/2-bm.getHeight()/2;
					
					for(int i=255;i>-10;i=i-10)
					{//动态更改图片的透明度值并不断重绘	
						currentAlpha=i;
						if(currentAlpha<0)
						{
							currentAlpha=0;
						}
						SurfaceHolder myholder=WellcomeView.this.getHolder();
						Canvas canvas = myholder.lockCanvas();//获取画布
						try{
							synchronized(myholder){
								onDraw(canvas);//绘制
							}
						}
						catch(Exception e){
							e.printStackTrace();
						}
						finally{
							if(canvas != null){
								myholder.unlockCanvasAndPost(canvas);
							}
						}						
						try
						{
							if(i==255)
							{//若是新图片，多等待一会
								Thread.sleep(1000);
							}
							Thread.sleep(sleepSpan);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				//动画播放完毕后，去主菜单界面
				activity.sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);
//				activity.sendMessage(WhatMessage.GOTO_GAME_VIEW);
			}
		}.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//销毁时被调用

	}
}