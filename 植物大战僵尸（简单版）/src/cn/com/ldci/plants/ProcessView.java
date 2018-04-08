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
 * �������������
 * 
 * @author ��һƽ
 * @�������������̨���еĳ��򣬵���̨��Ϻ󣬽���������
 */

public class ProcessView extends SurfaceView implements SurfaceHolder.Callback {
	// ���������õĶ���
	Handler b;
	// ���������õ�type
	int TYPE = 1;
	Bitmap bmpBackground = null;
	// ���ã���������ֵ
	MainActivity activity;
	Bitmap bmpLogo = null;
	Paint paint;
	private AThread thread;
	// ��̬����������������ֵ�����ؽ�����
	public static int process;
	// ����ProgressView��Ŀ���Ǵ�ֵ��
	ProcessView progressView;

	/**
	 * @View���� ���ر����Լ�logo���������ŵ�������
	 */

	public ProcessView(MainActivity activity,int type) {
		super(activity);
		this.activity = activity;
		this.TYPE=type;
		getHolder().addCallback(this);
		this.thread = new AThread(getHolder(), this);
		initBitmap();// ��ʼ��ͼƬ��Դ
	}

	/**
	 * @��ʼ��ͼƬ��Դ�ķ���
	 */
	public void initBitmap() {
		paint = new Paint();
		bmpBackground = BitmapFactory.decodeResource(getResources(),
				R.drawable.background);
		bmpLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
	}

	/**
	 * @�������� ���������ɳ���������ɫ������ĳ���Ҫ����
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
	 * @ִ��run���� run����ִ�еļ���process���Ӷ��ﵽ����������
	 */

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/**
	 * @����ʱ�����߳�
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
			} catch (InterruptedException e) {// ���ϵ�ѭ����ֱ��ˢ֡�߳̽���
			}
		}
	}

	/**
	 * 
	 * @ˢ֡�߳�
	 * 
	 */
	class AThread extends Thread {
		// ˯�ߵĺ�����
		private int span = 500;
		private SurfaceHolder surfaceHolder;
		private ProcessView pView;
		private boolean flag = false;

		/**
		 * @������
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
			Canvas c;// ����

			while (this.flag) {// ѭ��
				c = null;
				try {
					// �����������������ڴ�Ҫ��Ƚϸߵ�����£����������ҪΪnull
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						// ���û��Ʒ���
						pView.onDraw(c);
					}
				} // ʹ��finally��䱣֤�������һ����ִ��
				finally {
					if (c != null) {
						// ������Ļ��ʾ����
						this.surfaceHolder.unlockCanvasAndPost(c);

					}
				}

				if (process >= 100) {
					if(pView.TYPE == 1){//�е�WelcomeView
                		pView.activity.myHandler.sendEmptyMessage(4);//����activity����Handler��Ϣ
                		process=0;
					}
                	else if(pView.TYPE == 2){//�е�GameView
                		pView.activity.myHandler.sendEmptyMessage(6);//����activity����Handler��Ϣ
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
