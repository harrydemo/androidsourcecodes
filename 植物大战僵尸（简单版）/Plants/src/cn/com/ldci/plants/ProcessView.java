package cn.com.ldci.plants;

import java.io.InputStream;
import java.lang.Thread.State;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * 缓冲进度条界面
 * 
 * @author 庞一平
 * @进度条来缓冲后台进行的程序，当后台完毕后，进度条满。
 */

public class ProcessView extends SurfaceView implements SurfaceHolder.Callback {
	// 传递种类用的东西
	Handler b;
	// 传递种类用的type
	int TYPE = 1;
	Bitmap bmpBackground = null;
	// 引用，用来传递值
	MainActivity activity;
	Bitmap bmpLogo = null;
	Paint paint;
	private AThread thread;
	// 静态参数，用来接收数值，加载进度条
	public static int process;
	// 引用ProgressView，目的是传值。
	ProcessView progressView;

	/**
	 * @View加载 加载背景以及logo并启动，放到流里面
	 */

	public ProcessView(MainActivity activity,int type) {
		super(activity);
		this.activity = activity;
		this.TYPE=type;
		getHolder().addCallback(this);
		this.thread = new AThread(getHolder(), this);
		initBitmap();// 初始化图片资源
	}

	/**
	 * @初始化图片资源的方法
	 */
	public void initBitmap() {
		paint = new Paint();
		bmpBackground = BitmapFactory.decodeResource(getResources(),
				R.drawable.background);
		bmpLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
	}

	/**
	 * @画进度条 进度条画成长方，填充黄色。方块的长宽要计算
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint = new Paint();
		canvas.drawBitmap(this.bmpBackground, 0, 0, paint);
		int nLeft = (activity.nScreenWidth - this.bmpLogo.getWidth()) >> 1;
		int nTop = 95;
		canvas.drawBitmap(this.bmpLogo, nLeft, nTop, paint);

		int rectWidth = 200;
		int rectHeight = 20;
		int rectLeft = (activity.nScreenWidth - rectWidth) / 2;
		int rectTop = 270;
		int rectRight = (rectLeft + rectWidth);
		int rectBottom = (rectTop + rectHeight);

		paint.setColor(0xFFFF0000);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, paint);
		paint.setColor(0xFFFFAA00);
		paint.setStyle(Style.FILL);
		canvas.drawRect(rectLeft, rectTop, rectLeft + process * 2, rectBottom,
				paint);
	}

	/**
	 * @执行run方法 run方法执行的加载process，从而达到进度条满。
	 */

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * @创建时启动线程
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		this.thread.setFlag(true);
		this.thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setFlag(false);
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {// 不断地循环，直到刷帧线程结束
			}
		}
	}

	/**
	 * 
	 * @刷帧线程
	 * 
	 */
	class AThread extends Thread {
		// 睡眠的毫秒数
		private int span = 500;
		private SurfaceHolder surfaceHolder;
		private ProcessView pView;
		private boolean flag = false;

		/**
		 * @构造器
		 * @param surfaceHolder
		 * @param pView
		 * 
		 */
		public AThread(SurfaceHolder surfaceHolder, ProcessView pView) {
			this.surfaceHolder = surfaceHolder;
			this.pView = pView;
		}

		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		public void run() {
			ProcessView activity;
			Canvas c;// 画布

			while (this.flag) {// 循环
				c = null;
				try {
					// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						// 调用绘制方法
						pView.onDraw(c);
					}
				} // 使用finally语句保证下面代码一定被执行
				finally {
					if (c != null) {
						// 更新屏幕显示内容
						this.surfaceHolder.unlockCanvasAndPost(c);

					}
				}

				if (process >= 100) {
					if(pView.TYPE == 1){//切到WelcomeView
                		pView.activity.myHandler.sendEmptyMessage(4);//向主activity发送Handler消息
                		process=0;
					}
                	else if(pView.TYPE == 2){//切到GameView
                		pView.activity.myHandler.sendEmptyMessage(6);//向主activity发送Handler消息
                		process=0;
                	}
				}
				try {

					thread.sleep(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}
}
