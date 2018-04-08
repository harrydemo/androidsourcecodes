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
 * ѡ��˵�����
 *
 */
public class ChoiceView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity������
	Paint paint;//��������
	//�߳�����
	DrawThread drawThread;//�����߳�����	
	//���ⰴťͼƬ
	Bitmap choiceBmp0;
	Bitmap choiceBmp1;
	Bitmap choiceBmp2;
	Bitmap choiceBmp3;
	//���˵������ⰴť��������
	VirtualButton countDownBtn;
	VirtualButton practiceBtn;
	VirtualButton historyBtn;
	//����ͼƬ
	Bitmap bgBmp;
	
	public ChoiceView(GameActivity activity) {
		super(activity);
		this.activity=activity;
		//��ý��㲢����Ϊ�ɴ���
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//ע��ص��ӿ�		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);  	
		//���Ʊ���
		canvas.drawColor(Color.GRAY);
		canvas.drawBitmap(bgBmp, 0, 0, paint);
		//�������ⰴť
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
    		//������ĸ���ť�Ͽ����ĸ���ť
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
    		//̧��ʱ�ص����а�ť
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
		paint=new Paint();//��������
		paint.setAntiAlias(true);//�򿪿����	
		createAllThreads();//���������߳�
		initBitmap();//��ʼ��λͼ��Դ
		//�������ⰴť����
		int btnX=Constant.SCREEN_WIDTH/2-choiceBmp0.getWidth()/2;
		countDownBtn=new VirtualButton(choiceBmp3,choiceBmp0,btnX,Constant.CHOICE_BTN_Y0);
		practiceBtn=new VirtualButton(choiceBmp3,choiceBmp1,btnX,Constant.CHOICE_BTN_Y1);
		historyBtn=new VirtualButton(choiceBmp3,choiceBmp2,btnX,Constant.CHOICE_BTN_Y2);
		startAllThreads();//���������߳�	
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
	            catch (InterruptedException e) {e.printStackTrace();}//���ϵ�ѭ����ֱ��ˢ֡�߳̽���
	        }
	}
	//��ͼƬ����
	public void initBitmap(){
		choiceBmp0=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice0);
		choiceBmp1=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice1);
		choiceBmp2=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice2);
		choiceBmp3=BitmapFactory.decodeResource(this.getResources(), R.drawable.choice3);
		bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		//��Ӧ��
		choiceBmp0=PicLoadUtil.scaleToFit(choiceBmp0, Constant.ssr.ratio);
		choiceBmp1=PicLoadUtil.scaleToFit(choiceBmp1, Constant.ssr.ratio);
		choiceBmp2=PicLoadUtil.scaleToFit(choiceBmp2, Constant.ssr.ratio);
		choiceBmp3=PicLoadUtil.scaleToFit(choiceBmp3, Constant.ssr.ratio);
		bgBmp=PicLoadUtil.scaleToFitFullScreen(bgBmp, Constant.wRatio, Constant.hRatio);
	}
	void createAllThreads()
	{
		drawThread=new DrawThread(this);//���������߳�
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
	            	// �����������������ڴ�Ҫ��Ƚϸߵ�����£����������ҪΪnull
	                c = this.surfaceHolder.lockCanvas(null);
	                synchronized (this.surfaceHolder) {
	                	fatherView.onDraw(c);//����
	                }
	            } finally {
	                if (c != null) {
	                	//���ͷ���
	                    this.surfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
	            try{
	            	Thread.sleep(sleepSpan);//˯��ָ��������
	            }
	            catch(Exception e){
	            	e.printStackTrace();//��ӡ��ջ��Ϣ
	            }
	        }
		}
		public void setFlag(boolean flag) {
			this.flag = flag;
		}
	}
}
