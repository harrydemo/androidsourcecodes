package yzy.Tank;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 游戏主界面
 */
public class GameView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder sh;
	private Canvas canvas;
	
	MainActivity activity;//引用activity
	
	private Bitmap gdbj;//背景
	private Bitmap[] gdbjs = new Bitmap[ConstantUtil.pictureCount];//装分割以后的图片
	float backGroundIX = 0;//核心图的x坐标
	int i = 0;//核心图的索引
	BackGroundThread backGroundThread;//背景滚动线程 
	
	private Bitmap control;//控制按钮
	SoundPool soundPool;//声音
	HashMap<Integer, Integer> soundPoolMap; 
	KeyThread keyThread;//键盘监听线程
	int action = 0;//键盘的状态
	
	MoveThread moveThread;//移动物体的线程 
	ExplodeThread explodeThread;//爆炸换帧的线程 
	int status = 1;//游戏的状态1表示游戏中,2表示游戏失败即我方飞机没有了生命
	private boolean isdraw = true;//是否停止绘制
	Tank tank = new Tank(140, 389,ConstantUtil.life, this);
	ArrayList<Bullet> goodBollets = new ArrayList<Bullet>();//我方坦克发出的子弹
	ArrayList<Bullet> badBollets = new ArrayList<Bullet>();//敌方坦克发出的子弹
	ArrayList<EnemyTank> enemyTanks;//敌方的坦克 
	Bitmap enemyTank1;//敌坦克1
	Bitmap enemyTank2;//敌坦克2
	Bitmap enemyTank3;//敌坦克3
	Bitmap enemyTank4;//敌飞碟
	Bitmap enemyTank5;
	ArrayList<Life> lifes;//存放血块
	Bitmap life;//血块的图片
	ArrayList<Explode> explodeList = new ArrayList<Explode>();//爆炸效果1
	int[] explodesID = new int[]{//爆炸的所有帧
			R.drawable.baozha1,R.drawable.baozha2,R.drawable.baozha3,
			R.drawable.baozha4,R.drawable.baozha5,R.drawable.baozha6,
			R.drawable.baozha7,R.drawable.baozha8,R.drawable.baozha9,
		};
	Bitmap[] explodes = new Bitmap[explodesID.length];//爆炸1的数组
	
	ArrayList<Explode> explodeList2 = new ArrayList<Explode>();//爆炸效果2
	int[] explodesID2 = new int[]{//爆炸2的所有帧
			R.drawable.bz1,R.drawable.bz2,
			R.drawable.bz3,R.drawable.bz4,R.drawable.bz5,
	};
	Bitmap[] explodes2 = new Bitmap[explodesID2.length];//爆炸2的数组
	
	public GameView(MainActivity activity) {
		super(activity);
		this.activity = activity;//activity的引用
		gdbj = BitmapFactory.decodeResource(this.getResources(), R.drawable.gdbj);//背景图片
		for(int i=0; i<gdbjs.length; i++){//切成小图片
			gdbjs[i] = Bitmap.createBitmap(gdbj, ConstantUtil.pictureWidth*i, 0, ConstantUtil.pictureWidth, 480);
		}
		gdbj=null;//释放掉大背景图
		control= BitmapFactory.decodeResource(this.getResources(), R.drawable.control);//控制键图片
		initSounds();//初始化音乐
		
		this.backGroundThread = new BackGroundThread(this);//初始化背景滚动线程
		this.keyThread = new KeyThread(this);//初始化键盘监听
		this.moveThread = new MoveThread(this);//初始化物体移动线程
		this.explodeThread = new ExplodeThread(this);//初始化爆炸线程
		enemyTanks = Maps.getFirst();//取第一关的敌坦克
		lifes = Maps.getFirstLife();//取第一关的的血块
		enemyTank1 = BitmapFactory.decodeResource(getResources(), R.drawable.tk1);//敌坦1的图片
		enemyTank2 = BitmapFactory.decodeResource(getResources(), R.drawable.tk2);//敌坦2的图片
		enemyTank3 = BitmapFactory.decodeResource(getResources(), R.drawable.tk3);//敌坦3的图片
		enemyTank4 = BitmapFactory.decodeResource(getResources(), R.drawable.feidie);//敌飞碟图片
		enemyTank5 = BitmapFactory.decodeResource(getResources(), R.drawable.boss);//boss
		
		for(int i=0; i<explodes.length; i++){//初始化爆炸1图片
			explodes[i] = BitmapFactory.decodeResource(getResources(), explodesID[i]);
		}
		for(int i=0; i<explodes2.length; i++){//初始化爆炸2图片
			explodes2[i] = BitmapFactory.decodeResource(getResources(), explodesID2[i]);
		}
		for(EnemyTank ep : enemyTanks){//为敌坦初始化图片
			if(ep.type == 1){ 
				ep.bitmap = enemyTank1;
			}
			else if(ep.type == 2){
				ep.bitmap = enemyTank2;
			}
			else if(ep.type == 3){
				ep.bitmap = enemyTank3;
			}
			else if(ep.type == 4){
				ep.bitmap = enemyTank4;
			}
			else if(ep.type == 5){
				ep.bitmap = enemyTank5;
			}
		}
		life = BitmapFactory.decodeResource(getResources(), R.drawable.xuekuai);//血块图片
		for(Life l : lifes){//为血块初始化图片
			l.bitmap = life;
		}
		
		this.setFocusable(true);// 设置当前View先拥有控制焦点
		sh = this.getHolder();// View相当于电视 SurfaceHolder就是遥控器
		sh.addCallback(this);
	} 
	public void initSounds(){//初始化音乐
		 soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	     soundPoolMap = new HashMap<Integer, Integer>();   	
	     soundPoolMap.put(1, soundPool.load(getContext(), R.raw.tk3, 1));
	     soundPoolMap.put(2, soundPool.load(getContext(), R.raw.zidanfashe, 1));
	     soundPoolMap.put(3, soundPool.load(getContext(), R.raw.baozha, 1));
	     soundPoolMap.put(4, soundPool.load(getContext(), R.raw.daodanyinxiao, 1));
	     soundPoolMap.put(5, soundPool.load(getContext(), R.raw.chixue, 1));
	}
	public void playSound(int sound, int loop) {
		AudioManager mgr = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   
	    
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
	public void drawView() {
		//画的内容是x轴的，后画的会覆盖前面画的
		float backGroundIX=this.backGroundIX;
		int i=this.i;
		try {
			if (sh != null) {
				canvas = sh.lockCanvas();// 得到面板 锁定面板
				canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));//抗锯齿
				
				//画i左侧背景
				if(backGroundIX>0){
					float n=(backGroundIX/ConstantUtil.pictureWidth)+((backGroundIX%ConstantUtil.pictureWidth==0)?0:1);//计算i左面有几幅图
					for(int j=1;j<=n;j++){
						canvas.drawBitmap(gdbjs[(i-j+ConstantUtil.pictureCount)%ConstantUtil.pictureCount], backGroundIX-ConstantUtil.pictureWidth*j, 0, null);
					}
				}
				//画i处背景
				canvas.drawBitmap(gdbjs[i], backGroundIX, 0, null);
				//画i右侧背景
				if(backGroundIX<854-ConstantUtil.pictureWidth){
					float k=854-(backGroundIX+ConstantUtil.pictureWidth);
					float n=(k/ConstantUtil.pictureWidth)+((k%ConstantUtil.pictureWidth==0)?0:1);//计算i右面有几幅图
					for(int j=1;j<=n;j++){
						canvas.drawBitmap(gdbjs[(i+j)%ConstantUtil.pictureCount], backGroundIX+ConstantUtil.pictureWidth*j, 0, null
						);
					}
				}	
				canvas.drawBitmap(control, 42, 138, null);//画控制键
				
				tank.drawView(canvas);//画我坦克
				try{//绘制敌坦克
					for(EnemyTank ep:enemyTanks){
						if(ep.status == true){
							ep.draw(canvas);
						}
					}
				}catch(Exception e){  }
				
				try{//绘制我方子弹
					for(Bullet b:goodBollets){
						b.draw(canvas);
					}			
				}catch(Exception e){  }
				
				try{//绘制敌方子弹  
					for(Bullet b:badBollets){
						b.draw(canvas);
					}
				}
				catch(Exception e){
				}	
				
				try{//绘制爆炸
					for(Explode e : explodeList){
						e.draw(canvas);
					}
				}
				catch(Exception e){}
				
				try{//绘制血块
					for(Life l : lifes){
						if(l.status == true){
							l.draw(canvas);
						}
					}
				}
				catch(Exception e){}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				sh.unlockCanvasAndPost(canvas);// 接触锁定 发送给遥控器
			}
		}
	}
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start();//启动GameView线程
		
		this.backGroundThread.setFlag(true);
		this.backGroundThread.start();//启动背景滚动线程
		
		this.keyThread.setFlag(true);
		keyThread.start();//启动键盘监听线程
		
		this.moveThread.setFlag(true);
        moveThread.start();//启动子弹已经其他移动物的移动线程
        
        this.explodeThread.setFlag(true);
        explodeThread.start();//启动爆炸线程
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		isdraw = false;
		this.backGroundThread.setFlag(false);//停止背景滚动线程
		this.keyThread.setFlag(false);//停止键盘监听
		this.moveThread.setFlag(false);//停止子弹已经其他移动物的移动线程
		this.explodeThread.setFlag(false);//停止爆炸线程
		
		while (retry) {
            try {
            	keyThread.join();
                backGroundThread.join();
                moveThread.join();
                explodeThread.join();
                retry = false;
            } 
            catch (InterruptedException e) {//不断地循环，直到刷帧线程结束
            }
        }
	}

	@Override
	public void run() {
		while (isdraw) {
			drawView();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if ((event.getX() >= 100 & event.getX() <= 170)& (event.getY() >= 125 & event.getY() <= 201)) {// 上
				action = 1;
			}else if ((event.getX() >= 100 & event.getX() <= 170)& (event.getY() >= 275 & event.getY() <= 349)) {// 下
				action = 2;
			}else if ((event.getX() >= 0 & event.getX() <= 100)& (event.getY() >= 201 & event.getY() <= 275)) {// 左
				action = 3;
			}else if ((event.getX() >= 171 & event.getX() <= 241)& (event.getY() >= 201 & event.getY() <= 275)) {// 右
				action = 4;
			}else{
				action = 6;//如果在控制区域外 触摸屏幕  则代表发射子弹
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
				action = 5;
		}
		return true;
	}
}
