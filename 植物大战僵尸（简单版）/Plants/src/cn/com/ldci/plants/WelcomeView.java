package cn.com.ldci.plants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 欢迎界面
 * 
 * @author 张峻浦
 */
public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback {
	/**
	 * Activity的引用
	 */
	MainActivity activity;
	/**
	 * 绘制的线程
	 */
	private TutorialThread thread;
	/**
	 * 画笔
	 */
	Paint paint;
	Bitmap bg,bg1;
	Bitmap seedbank;
	Bitmap seeks;
	Bitmap[] seedImage = new Bitmap[6];
	int[] image = { R.drawable.seed_00, R.drawable.seed_01, R.drawable.seed_02,
			R.drawable.seed_03, R.drawable.seed_04, R.drawable.seed_05 };

	/**
	 * 欢迎界面构造函数
	 * @param activity
	 */
	public WelcomeView(MainActivity activity) {
		super(activity);
		this.activity = activity;// 得到activity的引用
		if(activity.processView != null){//走加载界面进度
        	activity.processView.process += 20;
        }
		getHolder().addCallback(this);
		this.thread = new TutorialThread(getHolder(), this);
		if(activity.processView != null){//走加载界面进度
        	activity.processView.process += 20;
        }
		initBitmap();
	}

	/**
	 * 初始化图片资源
	 */
	private void initBitmap() {
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		bg1 = zoomImage(bg, 480, 320);
		seedbank = BitmapFactory.decodeResource(getResources(),
				R.drawable.seedbank);
		if(activity.processView != null){//走加载界面进度
        	activity.processView.process += 20;
        }
		seedbank = zoomImage(seedbank, 317, 65);
		seeks = BitmapFactory.decodeResource(getResources(), R.drawable.seeks);
		seeks = zoomImage(seeks, 395, 255);
		if(activity.processView != null){//走加载界面进度
        	activity.processView.process += 20;
        }
		for (int i = 0; i < seedImage.length; i++) {
			seedImage[i] = BitmapFactory.decodeResource(getResources(),
					image[i]);
			seedImage[i] = zoomImage(seedImage[i], 35, 50);
		}
		if(activity.processView != null){//走加载界面进度
        	activity.processView.process += 20;
        }

	}

	/**
	 * 图片资源缩放到合适大小 
	 * @param bgimage 加载后的图片资源
	 * @param newWidth 图片缩放后的宽度
	 * @param newHeight 图片缩放后的高度
	 * @return 返回缩放后的图片资源
	 */
	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
		// 获得当前图片的宽度和高度
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);
		return bitmap;

	}

	/**
	 * 自定义绘制函数，绘制屏幕
	 * @param canvas 画布
	 */
	public void onDraw(Canvas canvas) {
	
		paint = new Paint();
		canvas.drawBitmap(bg1, 0, 0, paint);
		canvas.drawBitmap(seedbank, 0, 0, paint);
		canvas.drawBitmap(seeks, 0, seedbank.getHeight(), paint);
		canvas.drawText("50", 22, 59, paint);
		for (int i = 0; i < seedImage.length; i++) {
			canvas.drawBitmap(seedImage[i], 63 + (seedImage[0].getWidth() + 6)
					* i, 7, paint);
		}
	}

	/**
	 * 屏幕触摸事件 返回消息
	 * @param 事件
	 * @return 如果这个事件已处理返回true，否则返回false
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 得到X坐标
			double x = event.getX();
			// 得到Y坐标
			double y = event.getY();
			if (x >= 128 && x <= 275 && y >= 278 && y <= 310) {
					// 发送消息
			activity.myHandler.sendEmptyMessage(2);
			}	
		}
		return super.onTouchEvent(event);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		// 设置循环标记位
		this.thread.setFlag(true);
		// 启动绘制线程
		this.thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		 boolean retry = true;//循环标记
	        thread.setFlag(false);
	        while (retry) {
	            try {
	                thread.join();//等待线程的结束
	                retry = false;//设置循环标记停止循环
	            } 
	            catch (InterruptedException e) {}//不断地循环，直到刷帧线程结束
	        }
	}

	/**
	 * 绘制线程
	 */
	class TutorialThread extends Thread {
		/**
		 * 睡眠的毫秒数 
		 */
		private int sleepSpan = 100;
		/**
		 * 用于处理SurfaceView的Canvas上画的效果和动画，控制表面、大小、像素等。
		 */
		private SurfaceHolder surfaceHolder;
		/**
		 * WelcomeView对象的引用
		 */
		private WelcomeView welcomeView;
		private boolean flag = false;

		/**
		 * 构造器
		 * @param surfaceHolder 
		 * @param welcomeView
		 */
		public TutorialThread(SurfaceHolder surfaceHolder,
				WelcomeView welcomeView) {
			this.surfaceHolder = surfaceHolder;
			this.welcomeView = welcomeView;
		}

		/**
		 * 设置循环标记位
		 * @param flag
		 */
		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		@Override
		public void run() {
			Canvas c = null;
			while (this.flag) {
				try {
					// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						welcomeView.onDraw(c);// 绘制
					}
				} finally {
					if (c != null) {
						// 绘制后解锁，绘制后必须解锁才能显示
						this.surfaceHolder.unlockCanvasAndPost(c);
					}
				}
				try {
					Thread.sleep(sleepSpan);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
