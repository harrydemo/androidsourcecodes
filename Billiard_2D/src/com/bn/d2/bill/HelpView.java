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
 *  帮助界面
 *
 */
public class HelpView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity的引用
	Paint paint;//画笔引用
	//背景图片
	Bitmap bgBitmap;
	Bitmap helpBmp;
	public HelpView(GameActivity activity) {
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
		canvas.drawColor(Color.GRAY);
		//绘制背景
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		float x=(Constant.SCREEN_WIDTH-helpBmp.getWidth())/2;
		canvas.drawBitmap(helpBmp, x, Constant.HELP_Y, paint);
	}
	//重新绘制的方法
    public void repaint()
	{
		Canvas canvas=this.getHolder().lockCanvas();
		try
		{
			synchronized(canvas)
			{
				onDraw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				this.getHolder().unlockCanvasAndPost(canvas);
			}
		}
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
		repaint();//绘制界面
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//加载图片的方法
	public void initBitmap(){
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		helpBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.help0);
		//适应屏
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);		
		helpBmp=PicLoadUtil.scaleToFitFullScreen(helpBmp, Constant.wRatio, Constant.hRatio);
	}	
}
