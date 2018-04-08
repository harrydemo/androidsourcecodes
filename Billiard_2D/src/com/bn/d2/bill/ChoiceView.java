package com.bn.d2.bill;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * 选项菜单界面
 *
 */
public class ChoiceView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity的引用
	Paint paint;//画笔引用
	//线程引用
	DrawThread drawThread;//绘制线程引用	
	//虚拟按钮图片
	Bitmap choiceBmp0;
	Bitmap choiceBmp1;
	Bitmap choiceBmp2;
	Bitmap choiceBmp3;
	//主菜单上虚拟按钮对象引用
	VirtualButton countDownBtn;
	VirtualButton practiceBtn;
	VirtualButton historyBtn;
	//背景图片
	Bitmap bgBmp;
	
	public ChoiceView(GameActivity activity) {
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
		canvas.drawColor(Color.GRAY);
		canvas.drawBitmap(bgBmp, 0, 0, paint);
		//绘制虚拟按钮
		countDownBtn.drawSelf(canvas, paint);
		practiceBtn.drawSelf(canvas, paint);
		historyBtn.drawSelf(canvas, paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();				
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:    		
    		//点击在哪个按钮上开启哪个按钮
    		if(countDownBtn.isActionOnButton(x, y))
    		{
    			countDownBtn.pressDown();
    			activity.coundDownModeFlag=true;
    			activity.sendMessage(WhatMessage.GOTO_GAME_VIEW);
    		}
    		else if(practiceBtn.isActionOnButton(x, y))
    		{
    			practiceBtn.pressDown();
    			activity.coundDownModeFlag=false;
    			activity.sendMessage(WhatMessage.GOTO_GAME_VIEW);
    		}
    		else if(historyBtn.isActionOnButton(x, y))
    		{
    			historyBtn.pressDown();
    			activity.sendMessage(WhatMessage.GOTO_HIGH_SCORE_VIEW);    			
    		}
    		break;
    	case MotionEvent.ACTION_UP: 
    		//抬起时关掉所有按钮
    		countDownBtn.releaseUp();
    		practiceBtn.releaseUp();
    		historyBtn.releaseUp();	
    		break;    	
    	}
		return true;
	}	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//创建画笔
		paint.setAntiAlias(true);//打开抗锯齿	
		createAllThreads();//创建所有线程
		initBitmap();//初始化位图资源
		//创建虚拟按钮对象
		int btnX=Constant.SCREEN_WIDTH/2-choiceBmp0.getWidth()/2;
		countDownBtn=new VirtualButton(choiceBmp3,choiceBmp0,btnX,Constant.CHOICE_BTN_Y0);
		practiceBtn=new VirtualButton(choiceBmp3,choiceBmp1,btnX,Constant.CHOICE_BTN_Y1);
		historyBtn=new VirtualButton(choiceBmp3,choiceBmp2,btnX,Constant.CHOICE_BTN_Y2);
		startAllThreads();//开启所有线程	
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		  boolean retry = true;
	        stopAllThreads();
	        while (retry) {
	            try {
	            	drawThread.join();
	                retry = false;
	            } 
	            catch (InterruptedException e) {e.printStackTrace();}//不断地循环，直到刷帧线程结束
	        }
	}
	//将图片加载
	public void initBitmap(){
		choiceBmp0=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice0);
		choiceBmp1=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice1);
		choiceBmp2=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice2);
		choiceBmp3=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice3);
		bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		//适应屏
		choiceBmp0=PicLoadUtil.scaleToFit(choiceBmp0, Constant.ssr.ratio);
		choiceBmp1=PicLoadUtil.scaleToFit(choiceBmp1, Constant.ssr.ratio);
		choiceBmp2=PicLoadUtil.scaleToFit(choiceBmp2, Constant.ssr.ratio);
		choiceBmp3=PicLoadUtil.scaleToFit(choiceBmp3, Constant.ssr.ratio);
		bgBmp=PicLoadUtil.scaleToFitFullScreen(bgBmp, Constant.wRatio, Constant.hRatio);
	}
	void createAllThreads()
	{
		drawThread=new DrawThread(this);//创建绘制线程
	}
	void startAllThreads()
	{
		drawThread.setFlag(true);     
		drawThread.start();
	}
	void stopAllThreads()
	{
		drawThread.setFlag(false);       
	}
	private class DrawThread extends Thread{
		private boolean flag = true;	
		private int sleepSpan = 100;
		ChoiceView fatherView;
		SurfaceHolder surfaceHolder;
		public DrawThread(ChoiceView fatherView){
			this.fatherView = fatherView;
			this.surfaceHolder = fatherView.getHolder();
		}
		public void run(){
			Canvas c;
	        while (this.flag) {
	            c = null;
	            try {
	            	// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
	                c = this.surfaceHolder.lockCanvas(null);
	                synchronized (this.surfaceHolder) {
	                	fatherView.onDraw(c);//绘制
	                }
	            } finally {
	                if (c != null) {
	                	//并释放锁
	                    this.surfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
	            try{
	            	Thread.sleep(sleepSpan);//睡眠指定毫秒数
	            }
	            catch(Exception e){
	            	e.printStackTrace();//打印堆栈信息
	            }
	        }
		}
		public void setFlag(boolean flag) {
			this.flag = flag;
		}
	}
}
