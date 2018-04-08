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
	private MainActivity mainActivity;//得到activity的引用  用来发handle消息
	private SurfaceHolder sh;
	private Paint paint;
	private boolean isRunning = true;
	private int logoY ;
	private int dayunduoX ;
	private int xiaoyunduoX ;
	private boolean yplus = true;
	private Canvas canvas;
	private Bitmap beijing;//背景
	private Bitmap tankelogo;//坦克LOGO图片
	private Bitmap dayunduo;//大云朵
	private Bitmap xiaoyunduo;//大云朵
	private Bitmap tishitiao;//透明英文提示条  
	private int alpha;
	private SoundPool soundPool;//音效
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
		soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC, 100);//获得音效池对象
		sound = soundPool.load(context, R.raw.tk1, 0);//加载音效
		this.setFocusable(true);//设置当前View先拥有控制焦点
		sh = this.getHolder();//View相当于电视 SurfaceHolder就是遥控器
		sh.addCallback(this);
	}
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();//启动线程
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
				canvas = sh.lockCanvas();// 得到面板 锁定面板
				canvas.drawBitmap(beijing, 0, 0, null);// 绘制背景
				canvas.drawBitmap(dayunduo, dayunduoX, 140, null);// 绘制大云朵
				paint.setAlpha(alpha);
				canvas.drawBitmap(tankelogo,
						beijing.getWidth() / 2 - tankelogo.getWidth() / 2,
						logoY, paint);// 绘制LOGO
				alpha = alpha + 20;
				if (alpha >= 240)
					alpha = 255;
				canvas.drawBitmap(xiaoyunduo, xiaoyunduoX, 140, null);// 绘制小云朵
				canvas.drawBitmap(tishitiao,beijing.getWidth() / 2 - tishitiao.getWidth() / 2, 305,null);// 绘制透明英文提示条
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
				sh.unlockCanvasAndPost(canvas);// 接触锁定 发送给遥控器
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
		soundPool.play(sound, 1, 1, 0, 0, 1);//play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)leftVolume 表示对左音量设置 rightVolume 表示右音量设置 ， loop 表示 循环次数 rate表示速率最低0.5最高为2，1代表正常速度
		mainActivity.myHandler.sendEmptyMessage(1);//给activity发送消息 让activity切换页面
		return super.onTouchEvent(event);
	}
}
