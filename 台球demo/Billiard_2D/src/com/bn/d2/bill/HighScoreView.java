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
 * 积分榜界面
 *
 */
public class HighScoreView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity的引用
	Paint paint;//画笔引用
	DrawThread drawThread;//绘制线程引用		
	Bitmap bgBitmap;//背景图片	
	Bitmap bmp;//文字的图片
	Bitmap defenBitmap;
	Bitmap riqiBitmap;
	Bitmap[] numberBitmaps;//数字图片	
	Bitmap gangBitmap;//符号"-"对应的图片
	int bmpx;//文字位置	
	String queryResultStr;//查询数据库的结果
	String[] splitResultStrs;//将查询结果切分后的数组
	private int numberWidth;//数字图片的宽度
	private int posFrom=-1;//查询的开始位置
	private int length=5;//查询的最大记录个数
	int downY=0;//按下和抬起的y坐标
	int upY=0;
	public HighScoreView(GameActivity activity) {
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
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		//绘制文字+Constant.X_OFFSET,  +Constant.Y_OFFSET
		canvas.drawBitmap(bmp, bmpx+Constant.X_OFFSET, Constant.BMP_Y+Constant.Y_OFFSET, paint);
		canvas.drawBitmap(defenBitmap, Constant.DE_FEN_X+Constant.X_OFFSET, Constant.DE_FEN_Y+Constant.Y_OFFSET, paint);
		canvas.drawBitmap(riqiBitmap, Constant.RI_QI_X+Constant.X_OFFSET, Constant.DE_FEN_Y+Constant.Y_OFFSET, paint);
		//绘制得分和时间
		float x;
		float y;
		for(int i=0;i<splitResultStrs.length;i++)
		{
			if(i%2==0)//计算得分的位置
			{
				x=Constant.SCREEN_WIDTH*3/4;				
			}
			else//计算时间的位置
			{
				x=Constant.SCREEN_WIDTH/2;
			}
			y=Constant.BMP_Y+defenBitmap.getHeight()+(numberBitmaps[0].getHeight()+10)*(i/2+1);
			//绘制字符串
			drawDateBitmap(splitResultStrs[i],x+Constant.X_OFFSET,y+Constant.Y_OFFSET,canvas,paint);
		}
	}
	//画日期图片的方法
	public void drawDateBitmap(String numberStr,float endX,float endY,Canvas canvas,Paint paint)
	{
		for(int i=0;i<numberStr.length();i++)
		{
			char c=numberStr.charAt(i);
			if(c=='-')
			{
				canvas.drawBitmap(gangBitmap,endX-numberWidth*(numberStr.length()-i), endY, paint);
			}
			else
			{
				canvas.drawBitmap
				(
						numberBitmaps[c-'0'], 
						endX-numberWidth*(numberStr.length()-i), 
						endY, 
						paint
				);
			}			
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) event.getY();		
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		downY=y;
    		break;
    	case MotionEvent.ACTION_UP:
    		upY=y;    		
        	if(Math.abs(downY-upY)<20)//在域值范围内，不翻页
        	{
        		return true;
        	}
        	else if(downY<upY)//往下抹
        	{	
        		//如果抹到最前页，不可再抹
        		if(this.posFrom-this.length>=-1)
        		{
        			this.posFrom-=this.length;        			
        		}
        	}
        	else//往上抹
        	{	
        		//如果抹到最后页，不可再抹
        		if(this.posFrom+this.length<activity.getRowCount()-1)
        		{
        			this.posFrom+=this.length;        			
        		}
        	}
        	queryResultStr=activity.query(posFrom,length);//得到数据库中的数据
			splitResultStrs=queryResultStr.split("/", 0);//用"/"切分，且舍掉空串
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
		numberWidth=numberBitmaps[0].getWidth()+3;//得到数字图片的宽度
		//初始化图片的位置
		bmpx=(Constant.SCREEN_WIDTH-bmp.getWidth())/2;
		posFrom=-1;//查询的开始位置置-1			
		queryResultStr=activity.query(posFrom,length);//得到数据库中的数据
		splitResultStrs=queryResultStr.split("/", 0);//用"/"切分，且舍掉空串		
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
		bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bmp);	
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		numberBitmaps=new Bitmap[]{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number1),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number2),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number3),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number4),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number5),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number6),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number7),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number8),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number9),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
		};
		gangBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.gang);
		defenBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.defen);
		riqiBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.riqi);
		//适应屏
		bmp=PicLoadUtil.scaleToFit(bmp, Constant.ssr.ratio);
		gangBitmap=PicLoadUtil.scaleToFit(gangBitmap, Constant.ssr.ratio);
		riqiBitmap=PicLoadUtil.scaleToFit(riqiBitmap, Constant.ssr.ratio);
		defenBitmap=PicLoadUtil.scaleToFit(defenBitmap, Constant.ssr.ratio);				
		for(int i=0;i<numberBitmaps.length;i++){
			numberBitmaps[i]=PicLoadUtil.scaleToFit(numberBitmaps[i], Constant.ssr.ratio);
		}
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
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
		HighScoreView fatherView;
		SurfaceHolder surfaceHolder;
		public DrawThread(HighScoreView fatherView){
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
