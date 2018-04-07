package cn.com.ldci.plants;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
 * ��Ϸʧ�ܽ���
 * 
 * @author �ž���
 */
public class FailView extends SurfaceView implements SurfaceHolder.Callback {
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
	Bitmap dead, dead002;
	int k;

	public FailView(MainActivity activity) {
		super(activity);
		this.activity = activity;
		getHolder().addCallback(this);
		thread = new TutorialThread(getHolder(), this);
		initBitmap();
	}

	/**
	 * ��ʼ��ͼƬ��Դ
	 */
	private void initBitmap() {
		dead = BitmapFactory.decodeResource(getResources(), R.drawable.dead);
		//dead = zoomImage(dead, 480, 320);
//		dead002 = BitmapFactory.decodeResource(getResources(),
//				R.drawable.dead002);
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
		// ��ȡ���ͼƬ�Ŀ�͸�
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
	 * @param canvas
	 *            ����
	 */
	protected void Draw(Canvas canvas) {
		super.onDraw(canvas);

//		
			canvas.drawBitmap(dead, 0, 0, new Paint());
	//		canvas.drawBitmap(dead002, 0, 0,new Paint());
	}

	/**
	 * ��Ļ�����¼� ������Ϣ
	 * 
	 * @param �¼�
	 * @return �������¼��Ѵ�����true�����򷵻�false
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// �õ�X����
			double x = event.getX();
			// �õ�Y����
			double y = event.getY();
			int action = event.getAction();
			Log.d("game", x + "," + y + ", action=" + action);
			if (action == MotionEvent.ACTION_DOWN) {
				if (x >= 163 && x <= 320 && y >= 202 && y <= 236) {
					// ������Ϣ
					activity.myHandler.sendEmptyMessage(7);
				}
				if (x >= 189 && x <= 293 && y >= 153 && y <= 179) {
					activity.myHandler.sendEmptyMessage(7);
				}
			}
			return true;
			// �����жϷ�����Ϣ

		}
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
		private int sleepSpan = 50;
		/**
		 * ���ڴ���SurfaceView��Canvas�ϻ���Ч���Ͷ��������Ʊ��桢��С�����صȡ�
		 */
		private SurfaceHolder surfaceHolder;
		/**
		 * WelcomeView���������
		 */
		private FailView failView;
		private boolean flag = false;

		/**
		 * ������
		 * 
		 * @param surfaceHolder
		 * @param welcomeView
		 */
		public TutorialThread(SurfaceHolder surfaceHolder, FailView failView) {
			this.surfaceHolder = surfaceHolder;
			this.failView = failView;
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
						failView.Draw(c);// ����
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
