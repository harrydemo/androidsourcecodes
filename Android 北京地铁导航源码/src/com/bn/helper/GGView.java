package com.bn.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GGView extends View 
{
	TrainSystemHelperActivity activity;  //Activity
	int view_WIDTH;           //View�Ŀ��
	int view_HEIGHT;          //View�ĸ߶�
	boolean middleflag=false;	
	Bitmap[] bimtt;	               //ͼƬ����
	Paint paint;
	int[] drawablesId;
	int currIndex=0;                 //��ǰ����
	boolean workFlag=true;
	
	public GGView(Context father,AttributeSet as) //�вι�����
	{ 
		super(father,as);	        //���ø���
		this.drawablesId=new int[]  //ͼƬ����
		{
			R.drawable.adv1,	
			R.drawable.adv2,					
			R.drawable.adv3,
			R.drawable.adv4,
		};
		bimtt=new Bitmap[drawablesId.length];
		initBitmaps();            //����ͼƬ�ķ���
		paint=new Paint();      //����
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);//�������		
		
		new Thread()           //�߳�
		{
			public void run()   //��д����
			{
				while(workFlag)
				{
					currIndex=(currIndex+1)%drawablesId.length;
					GGView.this.postInvalidate(); //�ػ�
					try 
					{
						Thread.sleep(3000);    //�߳���Ϣ
					} catch (InterruptedException e) 
					{						
						e.printStackTrace();   //��ӡ�쳣��Ϣ
					}
				}
			}
		}.start();
	}
	
	public void initBitmaps()   //����ͼƬ�ķ���
	{
		Resources res=this.getResources(); //�õ���Դ
		for(int i=0;i<drawablesId.length;i++)
		{
			bimtt[i]=BitmapFactory.decodeResource(res, drawablesId[i]);
		}
	}
	
	public void onDraw(Canvas canvas)
	{
		if(!middleflag)
		{
			view_WIDTH=this.getWidth();//��ȡview�Ŀ��
			view_HEIGHT=this.getHeight();//��ȡview�ĸ߶�
			middleflag=true;
		}
		
		int picWidth=bimtt[currIndex].getWidth();   //ͼƬ�Ŀ��
		int picHeight=bimtt[currIndex].getHeight();  //ͼƬ�ĸ߶�
		
		int startX=(view_WIDTH-picWidth)/2;   //��ʼ��ͼ�����x����
		int startY=(view_HEIGHT-picHeight)/2; //��ʼ��ͼ������y����
		
		//���Ʊ���ɫ
		canvas.drawARGB(255, 255,204, 128);		
		canvas.drawBitmap(bimtt[currIndex], startX,startY, paint); //��ʽ����ͼƬ
		
	}
}
