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
	int currentIndex=2;//当前选中的菜单编号	
	float mPreviousX;//上次触控的X坐标  
	float mPreviousY;//上次触控的Y坐标	
	float changePercent=0;//动画进行的百分比
	int anmiState=0;//0-没有动画  1-向右走  2-向左走	
	
	float currentWidth;//当前菜单项宽度
	float currentHeight;//当前菜单项高度
	float currentX;//当前菜单项X位置
	float currentY;//当前菜单项Y位置	
			
	float leftWidth;//紧邻当前菜单项左侧菜单项的宽度		
	float leftHeight;//紧邻当前菜单项左侧菜单项的高度	
	float tempxLeft;//紧邻当前菜单项左侧菜单项的X坐标
	float tempyLeft;//紧邻当前菜单项左侧菜单项的Y坐标	
	
	float rightWidth;//紧邻当前菜单项右侧菜单项的宽度	
	float rightHeight;//紧邻当前菜单项右侧菜单项的高度	
	float tempxRight;//紧邻当前菜单项右侧菜单项的X坐标
	float tempyRight;//紧邻当前菜单项右侧菜单项的Y坐标	
	
	static float initial_Width;//菜单项图标初始宽度高度 
	static float initial_Height;
	
	final int ABOUT_VIEW=0;			//关于
	final int HELP_VIEW=1;			//帮助	
	final int START_VIEW=2;			//开始游戏
	final int SETUP_VIEW=3;			//设置
	final int EXIT_VIEW=4;			//退出
	
	public MainMenuView(GameActivity activity)
	{
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
		initBitmap(activity.getResources());
		//初始化当前及紧靠其左右的菜单项的位置大小参数
		init();
	}
	 @Override 
	    public boolean onTouchEvent(MotionEvent e) {
	    	
	    	if(anmiState!=0)
	    	{//若动画播放中则触控无效
	    		return true;
	    	}
	    	//获取当前触控点的XY坐标
	        float x = e.getX();
	        float y = e.getY();
	        
	        //根据触控的不同动作执行不同的业务逻辑
	        switch (e.getAction()) 
	        {
	        	case MotionEvent.ACTION_DOWN:
	        	  //若动作为按下触控笔则记录XY位置
	        	  mPreviousX=x;//记录触控笔X位置
	        	  mPreviousY=y;//记录触控笔Y位置
	            break;
	            case MotionEvent.ACTION_UP:
	              //若动作为抬起则根据X位移的不同执行左滑、右滑或选中菜单项的业务逻辑	
	              
	              //计算X位移	
	              float dx=x- mPreviousX;
	              
	              if(dx<-slideSpan)
	              {//若X位移小于阈值则向左滑动
	            	  if(currentIndex<menu.length-1)
	            	  {//若当前菜单项不是最后一个菜单项则向左滑动
	            		  //计算滑动完成后的当前菜单项编号
	            		  int afterCurrentIndex=currentIndex+1;
	            		  //动画状态值设置为2-向左走
	            		  anmiState=2;
	            		  //启动线程播放动画并更新状态值
	            		  new ViewDrawThread(this,afterCurrentIndex).start();
	            	  }
	              }
	              else if(dx>slideSpan)  
	              {//若X位移大于阈值则向右滑动
	            	  if(currentIndex>0)
	            	  {//若当前菜单项不是第一个菜单项则向左滑动
	            		  //计算滑动完成后的当前菜单项编号
	            		  int afterCurrentIndex=currentIndex-1;
	            		  //动画状态值设置为2-向右走
	            		  anmiState=1;
	            		  //启动线程播放动画并更新状态值
	            		  new ViewDrawThread(this,afterCurrentIndex).start();
	            	  }            	  
	              }
					else
					{//若X位移在阈值内则判断有否选中某菜单项
						if(//若按下与抬起的触控点都在当前菜单的范围内则执行按下某菜单项的业务逻辑
			                Constant.isPointInRect(mPreviousX, mPreviousY, 
			                		selectX+Constant.X_OFFSET, selectY+Constant.Y_OFFSET, bigWidth, bigHeight)&&
			                Constant.isPointInRect(x, y, 
			                		selectX+Constant.X_OFFSET, selectY+Constant.Y_OFFSET, bigWidth, bigHeight)
						)							 
						{							
							switch(currentIndex)
							{								
								case ABOUT_VIEW:
									activity.sendMessage(WhatMessage.GOTO_ABOUT_VIEW);	//关于
									break;
								case HELP_VIEW:
									activity.sendMessage(WhatMessage.GOTO_HELP_VIEW);		//帮助
									break;
								case START_VIEW:
									activity.sendMessage(WhatMessage.GOTO_CHOICE_VIEW); //模式选择
									break;
								case SETUP_VIEW:
									activity.sendMessage(WhatMessage.GOTO_SOUND_CONTORL_VIEW);//设置	
									break;
								case EXIT_VIEW:
									System.exit(0);			//退出程序
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
		//绘制背景		
		canvas.drawBitmap(bj, 0, 0, paint);
		
		//计算缩放比
		float ratioX=currentWidth/initial_Width;
		float ratioY=currentHeight/initial_Height;
		//绘制当前菜单项
		drawBitmap(canvas,currentX+Constant.X_OFFSET,currentY+Constant.Y_OFFSET,ratioX,ratioY,menu[currentIndex]);

		//若当前菜单项不是第一项则绘制紧邻当前菜单项左侧的菜单项
		if(currentIndex>0)
		{				
			//计算缩放比
			ratioX=leftWidth/initial_Width;
			ratioY=leftHeight/initial_Height;
			//绘制当前菜单项
			drawBitmap(canvas,tempxLeft+Constant.X_OFFSET, tempyLeft+Constant.Y_OFFSET,ratioX,ratioY,menu[currentIndex-1]);
		}			
		
		//若当前菜单项不是最后一项则绘制紧邻当前菜单项右侧的菜单项
		if(currentIndex<menu.length-1)
		{
			//计算缩放比
			ratioX=rightWidth/initial_Width;
			ratioY=rightHeight/initial_Height;
			//绘制当前菜单项
			drawBitmap(canvas,tempxRight+Constant.X_OFFSET,tempyRight+Constant.Y_OFFSET,ratioX,ratioY,menu[currentIndex+1]);
		}
		
		//向左绘制其他未选中的菜单
		for(int i=currentIndex-2;i>=0;i--)
		{	
			//计算X值
			float tempx=tempxLeft-(span+smallWidth)*(currentIndex-1-i);
			if(tempx<-smallWidth)
			{//若绘制出来不再屏幕上则不用绘制了
				break;
			}
			//计算Y值
			float tempy=selectY+(bigHeight-smallHeight);
			
			//计算缩放比
			ratioX=smallWidth/initial_Width;
			ratioY=smallHeight/initial_Height;
			//绘制当前菜单项
			drawBitmap(canvas,tempx+Constant.X_OFFSET,tempy+Constant.Y_OFFSET,ratioX,ratioY,menu[i]);
		}
		
		//向右绘制其他未选中的菜单
		for(int i=currentIndex+2;i<menu.length;i++)
		{
			//计算X值
            float tempx=tempxRight+rightWidth+span+(span+smallWidth)*(i-(currentIndex+1)-1);			
			if(tempx>screenWidthTest)
			{//若绘制出来不再屏幕上则不用绘制了
				break;
			}			
			//计算Y值
			float tempy=selectY+(bigHeight-smallHeight);	
			
			//计算缩放比
			ratioX=smallWidth/initial_Width;
			ratioY=smallHeight/initial_Height;
			//绘制当前菜单项
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
		SurfaceHolder mHolder=this.getHolder();//得到
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
	{//得到图片资源的方法
		menu=new Bitmap[]{
				BitmapFactory.decodeResource(r, R.drawable.menu0),
				BitmapFactory.decodeResource(r, R.drawable.menu1),
				BitmapFactory.decodeResource(r, R.drawable.menu2),
				BitmapFactory.decodeResource(r, R.drawable.menu3),
				BitmapFactory.decodeResource(r, R.drawable.menu4),
		};
		
		bj=BitmapFactory.decodeResource(r, R.drawable.help);
		//图片自适应屏
		for(int i=0;i<menu.length;i++){
			menu[i]=PicLoadUtil.scaleToFit(menu[i], Constant.ssr.ratio);
		}
		bj=PicLoadUtil.scaleToFitFullScreen(bj, Constant.wRatio, Constant.hRatio);	
		initial_Width=menu[0].getWidth();
		initial_Height=menu[0].getHeight();
	}
	
	public void init()
	{
		currentWidth=bigWidth;//当前选中菜单宽度
		currentHeight=bigHeight;//当前选中菜单高度
		currentX=selectX;//当前选中菜单X位置
		currentY=selectY;//当前选中菜单Y位置	
		rightWidth=smallWidth;//紧邻右侧的宽度
		leftWidth=smallWidth;//紧邻左侧的宽度		
		leftHeight=smallHeight;//紧邻左侧的高度	
		rightHeight=smallHeight;//紧邻右侧的高度
		tempxLeft=currentX-(span+leftWidth);//紧邻左侧的X
		tempyLeft=currentY+(currentHeight-leftHeight);//紧邻左侧的Y坐标	
		tempxRight=currentX+(span+currentWidth);//紧邻右侧的X	
		tempyRight=currentY+(currentHeight-rightHeight);//紧邻右侧的Y坐标
	}
	//在指定的位置按照指定的缩放比绘制图片
	public void drawBitmap
	(
		Canvas c,//画布
		float x,float y,//绘制位置xy坐标
		float xRatio,float yRatio,//XY方向的缩放比
		Bitmap bm//待绘制的图片
	)
	{
		//设置缩放矩阵
		Matrix m1=new Matrix();
		m1.setScale(xRatio, yRatio);
		//设置平移矩阵
		Matrix m2=new Matrix();
		m2.setTranslate(x, y);
		//计算总矩阵
		Matrix mz=new Matrix();
		mz.setConcat(m2, m1);
		//绘制当前的菜单项
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
		
		//循环指定的次数完成动画
		for(int i=0;i<=totalSteps;i++)
		{
			//计算此步的百分比
			mv.changePercent=percentStep*i;
			
			//初始化各个位置值
			mv.init();		
			
			//动画总的思想就是根据进度百分比计算出当前菜单项的位置、大小			
			//并根据当前菜单项的位置大小计算出其左右侧紧邻菜单项的位置、大小
			//非紧邻项的大小是固定的，只需要计算位置
			if(mv.anmiState==1)
			{//向右的动画
				//根据百分比计算当前菜单项位置、大小
				mv.currentX=mv.currentX+(bigWidth+span)*mv.changePercent;
				mv.currentY=mv.currentY+(bigHeight-smallHeight)*mv.changePercent;
				mv.currentWidth=(int)(smallWidth+(bigWidth-smallWidth)*(1-mv.changePercent));
				mv.currentHeight=(int)(smallHeight+(bigHeight-smallHeight)*(1-mv.changePercent));
				//由于动画向右，左侧菜单项会变大，因此计算左侧菜单项大小
				mv.leftWidth=(int)(smallWidth+(bigWidth-smallWidth)*mv.changePercent);
				mv.leftHeight=(int)(smallHeight+(bigHeight-smallHeight)*mv.changePercent);				
			}
			else if(mv.anmiState==2)
			{//向左的动画
				//根据百分比计算当前菜单项位置、大小
				mv.currentX=mv.currentX-(smallWidth+span)*mv.changePercent;
				mv.currentY=mv.currentY+(bigHeight-smallHeight)*mv.changePercent;
				mv.currentWidth=(int)(smallWidth+(bigWidth-smallWidth)*(1-mv.changePercent));
				mv.currentHeight=(int)(smallHeight+(bigHeight-smallHeight)*(1-mv.changePercent));
				//由于动画向左，右侧菜单项会变大，因此计算右侧菜单项大小
				mv.rightWidth=(int)(smallWidth+(bigWidth-smallWidth)*mv.changePercent);
				mv.rightHeight=(int)(smallHeight+(bigHeight-smallHeight)*mv.changePercent);					
			}			
			//计算出紧邻左侧的菜单项位置
			mv.tempxLeft=mv.currentX-(span+mv.leftWidth);			
			mv.tempyLeft=mv.currentY+(mv.currentHeight-mv.leftHeight);	
			//计算出紧邻右侧的菜单项位置
			mv.tempxRight=mv.currentX+(span+mv.currentWidth);
			mv.tempyRight=mv.currentY+(mv.currentHeight-mv.rightHeight);
			
			//重绘画面
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
		//动画完成后设置动画状态为0-无动画
		mv.anmiState=0;
		//动画完成后更新当前的菜单项编号
		mv.currentIndex=afterCurrentIndex;
		//初始化各个位置值
		mv.init();		
		//重绘画面
		mv.repaint(); 
	}
}