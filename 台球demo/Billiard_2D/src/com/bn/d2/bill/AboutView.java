package com.bn.d2.bill;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 *  帮助界面
 *
 */
public class AboutView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity的引用
	Paint paint;//画笔引用
	//背景图片
	Bitmap bgBitmap;
	Bitmap bmp;//文字的图片
	float bmpx;//文字位置	
	public AboutView(GameActivity activity) {
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
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		canvas.drawBitmap(bmp, bmpx+Constant.X_OFFSET, Constant.BMP_Y+Constant.Y_OFFSET, paint);
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
		//初始化图片的位置
		bmpx=(Constant.SCREEN_WIDTH-bmp.getWidth())/2;
		
		repaint();//绘制界面
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	//加载图片的方法
	public void initBitmap(){
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
		bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bmp0);	
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
		bmp=PicLoadUtil.scaleToFit(bmp, Constant.ssr.ratio);
		
	}	
}
