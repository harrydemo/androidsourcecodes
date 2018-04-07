package cn.com.ldci.plants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WinView extends SurfaceView implements SurfaceHolder.Callback {
	/**
	 * Activity������
	 */
	MainActivity activity;
	/**
	 * ���Ƶ��߳�
	 */
	private TutorialThread thread;
	/**
	 * ����
	 */
	Paint paint;
	Bitmap win;

	/**
	 * ʤ�����湹�캯��
	 * 
	 * @param activity
	 */
	public WinView(MainActivity activity) {
		super(activity);
		this.activity = activity;// �õ�activity������
		getHolder().addCallback(this);
		this.thread = new TutorialThread(getHolder(), this);
		initBitmap();
	}

	/**
	 * ��ʼ��ͼƬ��Դ
	 */
	private void initBitmap() {
		win = BitmapFactory.decodeResource(getResources(), R.drawable.win);
		win = zoomImage(win, 480, 320);
	}

	/**
	 * ͼƬ��Դ���ŵ����ʴ�С
	 * 
	 * @param bgimage
	 *            ���غ��ͼƬ��Դ
	 * @param newWidth
	 *            ͼƬ���ź�Ŀ��
	 * @param newHeight
	 *            ͼƬ���ź�ĸ߶�
	 * @return �������ź��ͼƬ��Դ
	 */
	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
		// ��õ�ǰͼƬ�Ŀ�Ⱥ͸߶�
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();
		// ���������ʣ��³ߴ��ԭʼ�ߴ�
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ����ͼƬ����
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
				matrix, true);
		return bitmap;

	}

	/**
	 * �Զ�����ƺ�����������Ļ
	 * 
	 * @param canvas ����
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint = new Paint();
		canvas.drawBitmap(win, 0, 0, paint);

	}

	/**
	 * ��Ļ�����¼� ������Ϣ
	 * 
	 * @param event �¼�
	 * @return �������¼��Ѵ�����true�����򷵻�false
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		activity.myHandler.sendEmptyMessage(7);
		return super.onTouchEvent(event);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		// ����ѭ�����λ
		this.thread.setFlag(true);
		// ���������߳�
		this.thread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;// ѭ�����
		thread.setFlag(false);
		while (retry) {
			try {
				thread.join();// �ȴ��̵߳Ľ���
				retry = false;// ����ѭ�����ֹͣѭ��
			} catch (InterruptedException e) {
			}// ���ϵ�ѭ����ֱ��ˢ֡�߳̽���
		}
	}

	/**
	 * �����߳�
	 */
	class TutorialThread extends Thread {
		/**
		 * ˯�ߵĺ�����
		 */
		private int sleepSpan = 100;
		/**
		 * ���ڴ���SurfaceView��Canvas�ϻ���Ч���Ͷ��������Ʊ��桢��С�����صȡ�
		 */
		private SurfaceHolder surfaceHolder;
		/**
		 * WelcomeView���������
		 */
		private WinView winView;
		private boolean flag = false;

		/**
		 * ������
		 * 
		 * @param surfaceHolder
		 * @param welcomeView
		 */
		public TutorialThread(SurfaceHolder surfaceHolder, WinView winView) {
			this.surfaceHolder = surfaceHolder;
			this.winView = winView;
		}

		/**
		 * ����ѭ�����λ
		 * 
		 * @param flag
		 */
		public void setFlag(boolean flag) {
			this.flag = flag;
		}

		@Override
		public void run() {
			Canvas c;
			while (this.flag) {
				c = null;
				try {
					// �����������������ڴ�Ҫ��Ƚϸߵ�����£����������ҪΪnull
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						winView.onDraw(c);// ����
					}
				} finally {
					if (c != null) {
						// ���ƺ���������ƺ�������������ʾ
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
