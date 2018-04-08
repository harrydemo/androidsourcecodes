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
 * ��ӭ����
 * 
 * @author �ž���
 */
public class WelcomeView extends SurfaceView implements SurfaceHolder.Callback {
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
	Bitmap bg,bg1;
	Bitmap seedbank;
	Bitmap seeks;
	Bitmap[] seedImage = new Bitmap[6];
	int[] image = { R.drawable.seed_00, R.drawable.seed_01, R.drawable.seed_02,
			R.drawable.seed_03, R.drawable.seed_04, R.drawable.seed_05 };

	/**
	 * ��ӭ���湹�캯��
	 * @param activity
	 */
	public WelcomeView(MainActivity activity) {
		super(activity);
		this.activity = activity;// �õ�activity������
		if(activity.processView != null){//�߼��ؽ������
        	activity.processView.process += 20;
        }
		getHolder().addCallback(this);
		this.thread = new TutorialThread(getHolder(), this);
		if(activity.processView != null){//�߼��ؽ������
        	activity.processView.process += 20;
        }
		initBitmap();
	}

	/**
	 * ��ʼ��ͼƬ��Դ
	 */
	private void initBitmap() {
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		bg1 = zoomImage(bg, 480, 320);
		seedbank = BitmapFactory.decodeResource(getResources(),
				R.drawable.seedbank);
		if(activity.processView != null){//�߼��ؽ������
        	activity.processView.process += 20;
        }
		seedbank = zoomImage(seedbank, 317, 65);
		seeks = BitmapFactory.decodeResource(getResources(), R.drawable.seeks);
		seeks = zoomImage(seeks, 395, 255);
		if(activity.processView != null){//�߼��ؽ������
        	activity.processView.process += 20;
        }
		for (int i = 0; i < seedImage.length; i++) {
			seedImage[i] = BitmapFactory.decodeResource(getResources(),
					image[i]);
			seedImage[i] = zoomImage(seedImage[i], 35, 50);
		}
		if(activity.processView != null){//�߼��ؽ������
        	activity.processView.process += 20;
        }

	}

	/**
	 * ͼƬ��Դ���ŵ����ʴ�С 
	 * @param bgimage ���غ��ͼƬ��Դ
	 * @param newWidth ͼƬ���ź�Ŀ��
	 * @param newHeight ͼƬ���ź�ĸ߶�
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
	 * @param canvas ����
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
	 * ��Ļ�����¼� ������Ϣ
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
			if (x >= 128 && x <= 275 && y >= 278 && y <= 310) {
					// ������Ϣ
			activity.myHandler.sendEmptyMessage(2);
			}	
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
		 boolean retry = true;//ѭ�����
	        thread.setFlag(false);
	        while (retry) {
	            try {
	                thread.join();//�ȴ��̵߳Ľ���
	                retry = false;//����ѭ�����ֹͣѭ��
	            } 
	            catch (InterruptedException e) {}//���ϵ�ѭ����ֱ��ˢ֡�߳̽���
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
		private WelcomeView welcomeView;
		private boolean flag = false;

		/**
		 * ������
		 * @param surfaceHolder 
		 * @param welcomeView
		 */
		public TutorialThread(SurfaceHolder surfaceHolder,
				WelcomeView welcomeView) {
			this.surfaceHolder = surfaceHolder;
			this.welcomeView = welcomeView;
		}

		/**
		 * ����ѭ�����λ
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
					// �����������������ڴ�Ҫ��Ƚϸߵ�����£����������ҪΪnull
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						welcomeView.onDraw(c);// ����
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
