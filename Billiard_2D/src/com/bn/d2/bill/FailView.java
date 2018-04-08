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
 *  游戏胜利的界面
 *
 */
public class FailView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity的引用
	Paint paint;//画笔引用
	//背景图片
	Bitmap bgBitmap;
	//文字的图片
	Bitmap loseBitmap;	
	//文字位置
	int bmpx;
	int bmpy;
	
	public FailView(GameActivity activity) {
		super(activity);
		this.activity=activity;
		//获得焦点并设置为可触控
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//注册回调接口		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);  	
		//绘制背景
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		
		canvas.drawBitmap(loseBitmap, bmpx, bmpy, paint);
	}	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//创建画笔
		paint.setAntiAlias(true);//打开抗锯齿	
		initBitmap();//初始化位图资源
		//初始化图片的位置
		bmpx=(Constant.SCREEN_WIDTH-loseBitmap.getWidth())/2;
		bmpy=(Constant.SCREEN_HEIGHT-loseBitmap.getHeight())/2;
		new Thread()
		{
			int sleepSpan=100;
			int totalSleepTime=5000;//显示界面的总时间
			public void run(){
				Canvas c;
		        for(int i=0;i<totalSleepTime/sleepSpan;i++) 
		        {
		        	c = null;
		        	SurfaceHolder myholder=FailView.this.getHolder();
		        	try {
		            	// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
		                c = myholder.lockCanvas(null);
		                synchronized (myholder) {
		                	FailView.this.onDraw(c);//绘制
		                }
		            } finally {
		                if (c != null) {
		                	//并释放锁
		                	myholder.unlockCanvasAndPost(c);
		                }
		            }
		            try{
		            	Thread.sleep(sleepSpan);//睡眠指定毫秒数
		            }
		            catch(Exception e){
		            	e.printStackTrace();//打印堆栈信息
		            }
		        }
		        //播放完毕后，去主菜单界面
				activity.sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);
			}
		}.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//加载图片的方法
	public void initBitmap(){
		loseBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.lose);
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		//适应屏
		loseBitmap=PicLoadUtil.scaleToFitFullScreen(loseBitmap, Constant.wRatio, Constant.hRatio);
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
	}	
}
