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
 *  ��Ϸʤ���Ľ���
 *
 */
public class FailView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity������
	Paint paint;//��������
	//����ͼƬ
	Bitmap bgBitmap;
	//���ֵ�ͼƬ
	Bitmap loseBitmap;	
	//����λ��
	int bmpx;
	int bmpy;
	
	public FailView(GameActivity activity) {
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
		paint=new Paint();//��������
		paint.setAntiAlias(true);//�򿪿����	
		initBitmap();//��ʼ��λͼ��Դ
		//��ʼ��ͼƬ��λ��
		bmpx=(Constant.SCREEN_WIDTH-loseBitmap.getWidth())/2;
		bmpy=(Constant.SCREEN_HEIGHT-loseBitmap.getHeight())/2;
		new Thread()
		{
			int sleepSpan=100;
			int totalSleepTime=5000;//��ʾ�������ʱ��
			public void run(){
				Canvas c;
		        for(int i=0;i<totalSleepTime/sleepSpan;i++) 
		        {
		        	c = null;
		        	SurfaceHolder myholder=FailView.this.getHolder();
		        	try {
		            	// �����������������ڴ�Ҫ��Ƚϸߵ�����£����������ҪΪnull
		                c = myholder.lockCanvas(null);
		                synchronized (myholder) {
		                	FailView.this.onDraw(c);//����
		                }
		            } finally {
		                if (c != null) {
		                	//���ͷ���
		                	myholder.unlockCanvasAndPost(c);
		                }
		            }
		            try{
		            	Thread.sleep(sleepSpan);//˯��ָ��������
		            }
		            catch(Exception e){
		            	e.printStackTrace();//��ӡ��ջ��Ϣ
		            }
		        }
		        //������Ϻ�ȥ���˵�����
				activity.sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);
			}
		}.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//����ͼƬ�ķ���
	public void initBitmap(){
		loseBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.lose);
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		//��Ӧ��
		loseBitmap=PicLoadUtil.scaleToFitFullScreen(loseBitmap, Constant.wRatio, Constant.hRatio);
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
	}	
}
