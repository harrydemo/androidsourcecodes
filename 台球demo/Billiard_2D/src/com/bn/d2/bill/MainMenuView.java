package com.bn.d2.bill;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.bn.d2.bill.Constant.*;

public class MainMenuView extends SurfaceView implements SurfaceHolder.Callback
{
	GameActivity activity;
	Paint paint;
	Bitmap[] menu;
	Bitmap bj;
	int currentIndex=2;//��ǰѡ�еĲ˵����	
	float mPreviousX;//�ϴδ��ص�X����  
	float mPreviousY;//�ϴδ��ص�Y����	
	float changePercent=0;//�������еİٷֱ�
	int anmiState=0;//0-û�ж���  1-������  2-������	
	
	float currentWidth;//��ǰ�˵�����
	float currentHeight;//��ǰ�˵���߶�
	float currentX;//��ǰ�˵���Xλ��
	float currentY;//��ǰ�˵���Yλ��	
			
	float leftWidth;//���ڵ�ǰ�˵������˵���Ŀ��		
	float leftHeight;//���ڵ�ǰ�˵������˵���ĸ߶�	
	float tempxLeft;//���ڵ�ǰ�˵������˵����X����
	float tempyLeft;//���ڵ�ǰ�˵������˵����Y����	
	
	float rightWidth;//���ڵ�ǰ�˵����Ҳ�˵���Ŀ��	
	float rightHeight;//���ڵ�ǰ�˵����Ҳ�˵���ĸ߶�	
	float tempxRight;//���ڵ�ǰ�˵����Ҳ�˵����X����
	float tempyRight;//���ڵ�ǰ�˵����Ҳ�˵����Y����	
	
	static float initial_Width;//�˵���ͼ���ʼ��ȸ߶� 
	static float initial_Height;
	
	final int ABOUT_VIEW=0;			//����
	final int HELP_VIEW=1;			//����	
	final int START_VIEW=2;			//��ʼ��Ϸ
	final int SETUP_VIEW=3;			//����
	final int EXIT_VIEW=4;			//�˳�
	
	public MainMenuView(GameActivity activity)
	{
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
		initBitmap(activity.getResources());
		//��ʼ����ǰ�����������ҵĲ˵����λ�ô�С����
		init();
	}
	 @Override 
	    public boolean onTouchEvent(MotionEvent e) {
	    	
	    	if(anmiState!=0)
	    	{//�������������򴥿���Ч
	    		return true;
	    	}
	    	//��ȡ��ǰ���ص��XY����
	        float x = e.getX();
	        float y = e.getY();
	        
	        //���ݴ��صĲ�ͬ����ִ�в�ͬ��ҵ���߼�
	        switch (e.getAction()) 
	        {
	        	case MotionEvent.ACTION_DOWN:
	        	  //������Ϊ���´��ر����¼XYλ��
	        	  mPreviousX=x;//��¼���ر�Xλ��
	        	  mPreviousY=y;//��¼���ر�Yλ��
	            break;
	            case MotionEvent.ACTION_UP:
	              //������Ϊ̧�������Xλ�ƵĲ�ִͬ���󻬡��һ���ѡ�в˵����ҵ���߼�	
	              
	              //����Xλ��	
	              float dx=x- mPreviousX;
	              
	              if(dx<-slideSpan)
	              {//��Xλ��С����ֵ�����󻬶�
	            	  if(currentIndex<menu.length-1)
	            	  {//����ǰ�˵�������һ���˵��������󻬶�
	            		  //���㻬����ɺ�ĵ�ǰ�˵�����
	            		  int afterCurrentIndex=currentIndex+1;
	            		  //����״ֵ̬����Ϊ2-������
	            		  anmiState=2;
	            		  //�����̲߳��Ŷ���������״ֵ̬
	            		  new ViewDrawThread(this,afterCurrentIndex).start();
	            	  }
	              }
	              else if(dx>slideSpan)  
	              {//��Xλ�ƴ�����ֵ�����һ���
	            	  if(currentIndex>0)
	            	  {//����ǰ�˵���ǵ�һ���˵��������󻬶�
	            		  //���㻬����ɺ�ĵ�ǰ�˵�����
	            		  int afterCurrentIndex=currentIndex-1;
	            		  //����״ֵ̬����Ϊ2-������
	            		  anmiState=1;
	            		  //�����̲߳��Ŷ���������״ֵ̬
	            		  new ViewDrawThread(this,afterCurrentIndex).start();
	            	  }            	  
	              }
					else
					{//��Xλ������ֵ�����ж��з�ѡ��ĳ�˵���
						if(//��������̧��Ĵ��ص㶼�ڵ�ǰ�˵��ķ�Χ����ִ�а���ĳ�˵����ҵ���߼�
			                Constant.isPointInRect(mPreviousX, mPreviousY, 
			                		selectX+Constant.X_OFFSET, selectY+Constant.Y_OFFSET, bigWidth, bigHeight)&&
			                Constant.isPointInRect(x, y, 
			                		selectX+Constant.X_OFFSET, selectY+Constant.Y_OFFSET, bigWidth, bigHeight)
						)							 
						{							
							switch(currentIndex)
							{								
								case ABOUT_VIEW:
									activity.sendMessage(WhatMessage.GOTO_ABOUT_VIEW);	//����
									break;
								case HELP_VIEW:
									activity.sendMessage(WhatMessage.GOTO_HELP_VIEW);		//����
									break;
								case START_VIEW:
									activity.sendMessage(WhatMessage.GOTO_CHOICE_VIEW); //ģʽѡ��
									break;
								case SETUP_VIEW:
									activity.sendMessage(WhatMessage.GOTO_SOUND_CONTORL_VIEW);//����	
									break;
								case EXIT_VIEW:
									System.exit(0);			//�˳�����
									break;
							}
						}
					}
				 break; 
	        }   
	        return true;        
	    }
	
	@Override
	public void onDraw(Canvas canvas)
	{
		canvas.drawColor(Color.GRAY);
		//���Ʊ���		
		canvas.drawBitmap(bj, 0, 0, paint);
		
		//�������ű�
		float ratioX=currentWidth/initial_Width;
		float ratioY=currentHeight/initial_Height;
		//���Ƶ�ǰ�˵���
		drawBitmap(canvas,currentX+Constant.X_OFFSET,currentY+Constant.Y_OFFSET,ratioX,ratioY,menu[currentIndex]);

		//����ǰ�˵���ǵ�һ������ƽ��ڵ�ǰ�˵������Ĳ˵���
		if(currentIndex>0)
		{				
			//�������ű�
			ratioX=leftWidth/initial_Width;
			ratioY=leftHeight/initial_Height;
			//���Ƶ�ǰ�˵���
			drawBitmap(canvas,tempxLeft+Constant.X_OFFSET, tempyLeft+Constant.Y_OFFSET,ratioX,ratioY,menu[currentIndex-1]);
		}			
		
		//����ǰ�˵�������һ������ƽ��ڵ�ǰ�˵����Ҳ�Ĳ˵���
		if(currentIndex<menu.length-1)
		{
			//�������ű�
			ratioX=rightWidth/initial_Width;
			ratioY=rightHeight/initial_Height;
			//���Ƶ�ǰ�˵���
			drawBitmap(canvas,tempxRight+Constant.X_OFFSET,tempyRight+Constant.Y_OFFSET,ratioX,ratioY,menu[currentIndex+1]);
		}
		
		//�����������δѡ�еĲ˵�
		for(int i=currentIndex-2;i>=0;i--)
		{	
			//����Xֵ
			float tempx=tempxLeft-(span+smallWidth)*(currentIndex-1-i);
			if(tempx<-smallWidth)
			{//�����Ƴ���������Ļ�����û�����
				break;
			}
			//����Yֵ
			float tempy=selectY+(bigHeight-smallHeight);
			
			//�������ű�
			ratioX=smallWidth/initial_Width;
			ratioY=smallHeight/initial_Height;
			//���Ƶ�ǰ�˵���
			drawBitmap(canvas,tempx+Constant.X_OFFSET,tempy+Constant.Y_OFFSET,ratioX,ratioY,menu[i]);
		}
		
		//���һ�������δѡ�еĲ˵�
		for(int i=currentIndex+2;i<menu.length;i++)
		{
			//����Xֵ
            float tempx=tempxRight+rightWidth+span+(span+smallWidth)*(i-(currentIndex+1)-1);			
			if(tempx>screenWidthTest)
			{//�����Ƴ���������Ļ�����û�����
				break;
			}			
			//����Yֵ
			float tempy=selectY+(bigHeight-smallHeight);	
			
			//�������ű�
			ratioX=smallWidth/initial_Width;
			ratioY=smallHeight/initial_Height;
			//���Ƶ�ǰ�˵���
			drawBitmap(canvas,tempx+Constant.X_OFFSET,tempy+Constant.Y_OFFSET,ratioX,ratioY,menu[i]);
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
	{
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		repaint();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		
	}
	public void repaint()
	{
		SurfaceHolder mHolder=this.getHolder();//�õ�
		Canvas canvas=mHolder.lockCanvas();
		try
		{
			synchronized(mHolder)
			{
				onDraw(canvas);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}  
		finally
		{
			if(canvas!=null)
			{
				mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void initBitmap(Resources r)
	{//�õ�ͼƬ��Դ�ķ���
		menu=new Bitmap[]{
				BitmapFactory.decodeResource(r, R.drawable.menu0),
				BitmapFactory.decodeResource(r, R.drawable.menu1),
				BitmapFactory.decodeResource(r, R.drawable.menu2),
				BitmapFactory.decodeResource(r, R.drawable.menu3),
				BitmapFactory.decodeResource(r, R.drawable.menu4),
		};
		
		bj=BitmapFactory.decodeResource(r, R.drawable.help);
		//ͼƬ����Ӧ��
		for(int i=0;i<menu.length;i++){
			menu[i]=PicLoadUtil.scaleToFit(menu[i], Constant.ssr.ratio);
		}
		bj=PicLoadUtil.scaleToFitFullScreen(bj, Constant.wRatio, Constant.hRatio);	
		initial_Width=menu[0].getWidth();
		initial_Height=menu[0].getHeight();
	}
	
	public void init()
	{
		currentWidth=bigWidth;//��ǰѡ�в˵����
		currentHeight=bigHeight;//��ǰѡ�в˵��߶�
		currentX=selectX;//��ǰѡ�в˵�Xλ��
		currentY=selectY;//��ǰѡ�в˵�Yλ��	
		rightWidth=smallWidth;//�����Ҳ�Ŀ��
		leftWidth=smallWidth;//�������Ŀ��		
		leftHeight=smallHeight;//�������ĸ߶�	
		rightHeight=smallHeight;//�����Ҳ�ĸ߶�
		tempxLeft=currentX-(span+leftWidth);//��������X
		tempyLeft=currentY+(currentHeight-leftHeight);//��������Y����	
		tempxRight=currentX+(span+currentWidth);//�����Ҳ��X	
		tempyRight=currentY+(currentHeight-rightHeight);//�����Ҳ��Y����
	}
	//��ָ����λ�ð���ָ�������űȻ���ͼƬ
	public void drawBitmap
	(
		Canvas c,//����
		float x,float y,//����λ��xy����
		float xRatio,float yRatio,//XY��������ű�
		Bitmap bm//�����Ƶ�ͼƬ
	)
	{
		//�������ž���
		Matrix m1=new Matrix();
		m1.setScale(xRatio, yRatio);
		//����ƽ�ƾ���
		Matrix m2=new Matrix();
		m2.setTranslate(x, y);
		//�����ܾ���
		Matrix mz=new Matrix();
		mz.setConcat(m2, m1);
		//���Ƶ�ǰ�Ĳ˵���
		c.drawBitmap(bm, mz, paint);
	}	
}
class ViewDrawThread extends Thread
{
	MainMenuView mv;
	int afterCurrentIndex;
	static boolean flag;
	public ViewDrawThread(MainMenuView mv,int afterCurrentIndex)
	{
		this.mv=mv;
		this.afterCurrentIndex=afterCurrentIndex;
	}
	@Override
	public void run()
	{
		
		//ѭ��ָ���Ĵ�����ɶ���
		for(int i=0;i<=totalSteps;i++)
		{
			//����˲��İٷֱ�
			mv.changePercent=percentStep*i;
			
			//��ʼ������λ��ֵ
			mv.init();		
			
			//�����ܵ�˼����Ǹ��ݽ��Ȱٷֱȼ������ǰ�˵����λ�á���С			
			//�����ݵ�ǰ�˵����λ�ô�С����������Ҳ���ڲ˵����λ�á���С
			//�ǽ�����Ĵ�С�ǹ̶��ģ�ֻ��Ҫ����λ��
			if(mv.anmiState==1)
			{//���ҵĶ���
				//���ݰٷֱȼ��㵱ǰ�˵���λ�á���С
				mv.currentX=mv.currentX+(bigWidth+span)*mv.changePercent;
				mv.currentY=mv.currentY+(bigHeight-smallHeight)*mv.changePercent;
				mv.currentWidth=(int)(smallWidth+(bigWidth-smallWidth)*(1-mv.changePercent));
				mv.currentHeight=(int)(smallHeight+(bigHeight-smallHeight)*(1-mv.changePercent));
				//���ڶ������ң����˵��������˼������˵����С
				mv.leftWidth=(int)(smallWidth+(bigWidth-smallWidth)*mv.changePercent);
				mv.leftHeight=(int)(smallHeight+(bigHeight-smallHeight)*mv.changePercent);				
			}
			else if(mv.anmiState==2)
			{//����Ķ���
				//���ݰٷֱȼ��㵱ǰ�˵���λ�á���С
				mv.currentX=mv.currentX-(smallWidth+span)*mv.changePercent;
				mv.currentY=mv.currentY+(bigHeight-smallHeight)*mv.changePercent;
				mv.currentWidth=(int)(smallWidth+(bigWidth-smallWidth)*(1-mv.changePercent));
				mv.currentHeight=(int)(smallHeight+(bigHeight-smallHeight)*(1-mv.changePercent));
				//���ڶ��������Ҳ�˵��������˼����Ҳ�˵����С
				mv.rightWidth=(int)(smallWidth+(bigWidth-smallWidth)*mv.changePercent);
				mv.rightHeight=(int)(smallHeight+(bigHeight-smallHeight)*mv.changePercent);					
			}			
			//������������Ĳ˵���λ��
			mv.tempxLeft=mv.currentX-(span+mv.leftWidth);			
			mv.tempyLeft=mv.currentY+(mv.currentHeight-mv.leftHeight);	
			//����������Ҳ�Ĳ˵���λ��
			mv.tempxRight=mv.currentX+(span+mv.currentWidth);
			mv.tempyRight=mv.currentY+(mv.currentHeight-mv.rightHeight);
			
			//�ػ滭��
			mv.repaint();			
			try
			{
				Thread.sleep(timeSpan);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		//������ɺ����ö���״̬Ϊ0-�޶���
		mv.anmiState=0;
		//������ɺ���µ�ǰ�Ĳ˵�����
		mv.currentIndex=afterCurrentIndex;
		//��ʼ������λ��ֵ
		mv.init();		
		//�ػ滭��
		mv.repaint(); 
	}
}