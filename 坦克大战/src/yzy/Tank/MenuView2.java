/*package yzy.Tank;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MenuView2 extends SurfaceView implements Runnable{

	private MainActivity mainActivity;//得到activity的引用  用来发handle消息
	private Bitmap caidanbj;//背景  
	private Bitmap tk56;//tank1  
	private Bitmap tk53;//tank2  
	private Bitmap tk50;//tank3  
	private Bitmap tk46;//tank4 
	private Bitmap bgS;//选择框 
	private Bitmap bgSL;//选择框 左
	private Bitmap bgSLTank;//选择框 Tank
	private Bitmap tmKL; 
	private Bitmap tmKR;
	private Bitmap daihao;
	private Bitmap sudu;
	private Bitmap xueliang;
	private Bitmap zb;
	private Bitmap gege;
	private int bgsHeight=86;
	private int speed=5;
	private int health=3;
	private Paint paint;//画笔
	private SurfaceHolder sh;//面板
	private Canvas canvas;//绘制
	private boolean isRunning = true;
	private String Tanktext="挑战者M2";
	private SoundPool soundPool;//音效
	private int sound;
	public MenuView2(Context context) {
		super(context);
		this.mainActivity = (MainActivity) context;
		caidanbj = BitmapFactory.decodeResource(this.getResources(), R.drawable.caidanbj);
		tk56 = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk56);
		bgSLTank = tk56;
		tk53 = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk53);
		tk50 = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk50);
		tk46 = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk46);
		bgS = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk105);
		bgSL = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk74);
		tmKL = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk40);
		tmKR = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk43);
		daihao = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk69);
		sudu = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk73);
		xueliang = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk80);
		zb = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk82);
		gege = BitmapFactory.decodeResource(this.getResources(), R.drawable.tk78);
		paint = new Paint();
		soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC, 100);//获得音效池对象
		sound = soundPool.load(context, R.raw.tk12, 0);//加载音效
		this.setFocusable(true);//设置当前View先拥有控制焦点
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
				canvas.drawBitmap(caidanbj, 0, 0, null);// 绘制背景
				canvas.drawBitmap(tmKL, 305, 85, null);
				canvas.drawBitmap(tmKR, 540, 85, null);
				canvas.drawBitmap(tk56, 359, 85, null);// 绘制tank1
				canvas.drawBitmap(tk53, 359, 181, null);// 绘制tank2
				canvas.drawBitmap(tk50, 359, 277, null);// 绘制tank3
				canvas.drawBitmap(tk46, 359, 373, null);// 绘制tank4
				canvas.drawBitmap(bgS, 352, bgsHeight, null);// 
				leftInfo(canvas);//绘制左侧信息
				paint.setColor(Color.rgb(255, 153, 0));
				paint.setTextSize(16);
				paint.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
				canvas.drawText(Tanktext, 61, 162, paint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				sh.unlockCanvasAndPost(canvas);// 接触锁定 发送给遥控器
			}
		}
	}
	private void leftInfo(Canvas canvas) {
		canvas.drawBitmap(bgSL, 18, 188, null);
		canvas.drawBitmap(bgSLTank, 25, 189, null);//左侧坦克
		canvas.drawBitmap(daihao, 18, 145, null);
		canvas.drawBitmap(sudu, 18, 275, null);
		for(int x=56;x<=56+speed*11 ;x=x+11){
			canvas.drawBitmap(gege, x, 275, null);
		}
		canvas.drawBitmap(xueliang, 18, 298, null);
		
		for(int x=56;x<=56+health*11 ;x=x+11){
			canvas.drawBitmap(gege, x, 298, null);
		}
		canvas.drawBitmap(zb, 124, 269, null);
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
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if((event.getX()>=359 & event.getX()<=496)&(event.getY()>=85&event.getY()<=158)){
				bgsHeight=86;
				bgSLTank = tk56;
				speed=5;
				health=3;
				Tanktext="挑战者M2";
				soundPool.play(sound, 1, 1, 0, 0, 1);//play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)leftVolume 表示对左音量设置 rightVolume 表示右音量设置 ， loop 表示 循环次数 rate表示速率最低0.5最高为2，1代表正常速度
				mainActivity.myHandler.sendEmptyMessage(2);//给activity发送消息 让activity切换页面
			}else if((event.getX()>=359 & event.getX()<=496)&(event.getY()>=181&event.getY()<=254)){
				bgsHeight=182;
				bgSLTank = tk53;
				speed=3;
				health=4;
				Tanktext="虎式重型坦克";
				soundPool.play(sound, 1, 1, 0, 0, 1);//play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)leftVolume 表示对左音量设置 rightVolume 表示右音量设置 ， loop 表示 循环次数 rate表示速率最低0.5最高为2，1代表正常速度
			}else if((event.getX()>=359 & event.getX()<=496)&(event.getY()>=277&event.getY()<=350)){
				bgsHeight=278;
				bgSLTank = tk50;
				speed=2;
				health=4;
				Tanktext="M46谢尔曼";
				soundPool.play(sound, 1, 1, 0, 0, 1);//play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)leftVolume 表示对左音量设置 rightVolume 表示右音量设置 ， loop 表示 循环次数 rate表示速率最低0.5最高为2，1代表正常速度
			}else if((event.getX()>=359 & event.getX()<=496)&(event.getY()>=373&event.getY()<=446)){
				bgsHeight=374;
				bgSLTank = tk46;
				speed=2;
				health=3;
				Tanktext="追猎者V1";
				soundPool.play(sound, 1, 1, 0, 0, 1);//play (int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)leftVolume 表示对左音量设置 rightVolume 表示右音量设置 ， loop 表示 循环次数 rate表示速率最低0.5最高为2，1代表正常速度
			}
			break;
		default:
			break;
		}
		return true;//触摸时间不传递给activity
	}
}
*/