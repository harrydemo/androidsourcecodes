package yzy.Tank;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
public class WelcomeView extends SurfaceView implements Callback,Runnable{
	private MainActivity mainActivity;//�õ�activity������  ������handle��Ϣ
	private SurfaceHolder sh;
	private Paint paint;
	private boolean isRunning = true;
	private int logoY ;
	private int dayunduoX ;
	private int xiaoyunduoX ;
	private boolean yplus = true;
	private Canvas canvas;
	private Bitmap beijing;//����
	private Bitmap tankelogo;//̹��LOGOͼƬ
	private Bitmap dayunduo;//���ƶ�
	private Bitmap xiaoyunduo;//���ƶ�
	private Bitmap tishitiao;//͸��Ӣ����ʾ��  
	private int alpha;
	private SoundPool soundPool;//��Ч
	private int sound;
	public WelcomeView(Context context) {
		super(context);
		this.mainActivity = (MainActivity) context;
		beijing = BitmapFactory.decodeResource(this.getResources(), R.drawable.kcbj);
		tankelogo = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk26);
		dayunduo = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk20);
		xiaoyunduo = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk23);
		tishitiao = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk30);
		dayunduoX= beijing.getWidth()/2-dayunduo.getWidth()/2;
		xiaoyunduoX=dayunduoX;
		logoY=142;
		alpha=0;
		paint = new Paint();
		soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC, 100);//�����Ч�ض���
		sound = soundPool.load(context, R.raw.tk1, 0);//������Ч
		this.setFocusable(true);//���õ�ǰView��ӵ�п��ƽ���
		sh = this.getHolder();//View�൱�ڵ��� SurfaceHolder����ң����
		sh.addCallback(this);
	}
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();//�����߳�
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		isRunning = false;
	}

	private void drawView() {
		try {
			if (sh != null) {
				canvas = sh.lockCanvas();// �õ���� �������
				canvas.drawBitmap(beijing, 0, 0, null);// ���Ʊ���
				canvas.drawBitmap(dayunduo, dayunduoX, 140, null);// ���ƴ��ƶ�
				paint.setAlpha(alpha);
				canvas.drawBitmap(tankelogo,
						beijing.getWidth() / 2 - tankelogo.getWidth() / 2,
						logoY, paint);// ����LOGO
				alpha = alpha + 20;
				if (alpha >= 240)
					alpha = 255;
				canvas.drawBitmap(xiaoyunduo, xiaoyunduoX, 140, null);// ����С�ƶ�
				canvas.drawBitmap(tishitiao,beijing.getWidth() / 2 - tishitiao.getWidth() / 2, 305,null);// ����͸��Ӣ����ʾ��
				if (logoY == 136)
					yplus = false;
				else if (logoY == 142)
					yplus = true;
				logoY = yplus ? logoY - 1 : logoY + 1;
				dayunduoX = yplus ? dayunduoX - 1 : dayunduoX + 1;
				xiaoyunduoX = yplus ? xiaoyunduoX + 1 : xiaoyunduoX - 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				sh.unlockCanvasAndPost(canvas);// �Ӵ����� ���͸�ң����
			}
		}
	}
	public void run() {
		while (isRunning) {
			drawView();
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			isRunning = false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		soundPool.play(sound, 1, 1, 0, 0, 1);//play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)leftVolume ��ʾ������������ rightVolume ��ʾ���������� �� loop ��ʾ ѭ������ rate��ʾ�������0.5���Ϊ2��1���������ٶ�
		mainActivity.myHandler.sendEmptyMessage(1);//��activity������Ϣ ��activity�л�ҳ��
		return super.onTouchEvent(event);
	}
}
