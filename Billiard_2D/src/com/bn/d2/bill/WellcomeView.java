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
 * ��ʾ���ɿƼ��Ķ�������
 *
 */
public class WellcomeView extends SurfaceView 
implements SurfaceHolder.Callback  //ʵ���������ڻص��ӿ�
{
	GameActivity activity;
	Paint paint;//����
	int currentAlpha=0;//��ǰ�Ĳ�͸��ֵ
	
	int screenWidth=Constant.SCREEN_WIDTH;//��Ļ���
	int screenHeight=Constant.SCREEN_HEIGHT;//��Ļ�߶�
	int sleepSpan=50;//������ʱ��ms
	
	Bitmap[] logos=new Bitmap[2];//logoͼƬ����
	Bitmap currentLogo;//��ǰlogoͼƬ����
	int currentX;
	int currentY;
	
	public WellcomeView(GameActivity activity) {
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);//�����������ڻص��ӿڵ�ʵ����
		paint = new Paint();//��������
		paint.setAntiAlias(true);//�򿪿����
		//����ͼƬ
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dukea); 
		logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dukeb);		
		for(int i=0;i<logos.length;i++){
			logos[i]=PicLoadUtil.scaleToFit(logos[i], Constant.ssr.ratio);
		}
	}
	public void onDraw(Canvas canvas){	
		//���ƺ��������屳��
		paint.setColor(Color.BLACK);//���û�����ɫ
		paint.setAlpha(255);
		canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
		
		//����ƽ����ͼ
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}
	public void surfaceCreated(SurfaceHolder holder) {//����ʱ������		
		new Thread()
		{
			public void run()
			{
				for(Bitmap bm:logos)
				{
					currentLogo=bm;
					//����ͼƬλ��
					currentX=screenWidth/2-bm.getWidth()/2;
					currentY=screenHeight/2-bm.getHeight()/2;
					
					for(int i=255;i>-10;i=i-10)
					{//��̬����ͼƬ��͸����ֵ�������ػ�	
						currentAlpha=i;
						if(currentAlpha<0)
						{
							currentAlpha=0;
						}
						SurfaceHolder myholder=WellcomeView.this.getHolder();
						Canvas canvas = myholder.lockCanvas();//��ȡ����
						try{
							synchronized(myholder){
								onDraw(canvas);//����
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
							{//������ͼƬ����ȴ�һ��
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
				//����������Ϻ�ȥ���˵�����
				activity.sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);
//				activity.sendMessage(WhatMessage.GOTO_GAME_VIEW);
			}
		}.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//����ʱ������

	}
}