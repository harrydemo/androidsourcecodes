package com.bn.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView 
implements SurfaceHolder.Callback  //ʵ���������ڻص��ӿ�
{
	TrainSystemHelperActivity activity;
	Paint paint;//����
	int currentA=0;//��ǰ�Ĳ�͸��ֵ
	int sleeptime=50;//������ʱ��ms	
	Bitmap[] logos=new Bitmap[2];//logoͼƬ����
	Bitmap currentLogo;//��ǰlogoͼƬ����
	int currentX;
	int currentY;	
	
	public MySurfaceView(TrainSystemHelperActivity activity) {
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);//�����������ڻص��ӿڵ�ʵ����
		paint = new Paint();//��������
		paint.setAntiAlias(true);//�򿪿����
		
		
		
		//����ͼƬ
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dukea); 
		logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dukeb);		
	}

	public void onDraw(Canvas canvas){	 
		//���ƺ��������屳��
		paint.setColor(Color.BLACK);//���û�����ɫ
		paint.setAlpha(255);
		canvas.drawRect(0, 0, activity.screenWidth, activity.screenHeight, paint);
		
		//����ƽ����ͼ
		if(currentLogo==null)return;
		paint.setAlpha(currentA);		
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
					currentX=activity.screenWidth/2-bm.getWidth()/2;
					currentY=activity.screenHeight/2-bm.getHeight()/2;
					
					for(int i=255;i>-10;i=i-5)
					{//��̬����ͼƬ��͸����ֵ�������ػ�	
						currentA=i;
						if(currentA<0)
						{
							currentA=0;
						}
						SurfaceHolder myholder=MySurfaceView.this.getHolder();
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
								Thread.sleep(2000);
							}
							Thread.sleep(sleeptime);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				activity.hd.sendEmptyMessage(0);
			}
		}.start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {//����ʱ������

	}
}