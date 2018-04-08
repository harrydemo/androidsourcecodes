package com.bn.d2.bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 
 * 游戏界面
 *
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity的引用
	Paint paint;//画笔引用	
	Bitmap[] tableBmps;//球台图片
	Bitmap cueBmp;//球杆图片
	Bitmap[][] ballBmps;//所有球的图片	
	Bitmap barDownBmp;//力度条的图片
	Bitmap barUpBmp;	
	Bitmap goDownBmp;//GO按钮图片
	Bitmap goUpBmp;
	Bitmap leftDownBmp;//向左微调按钮图片
	Bitmap leftUpBmp;
	Bitmap rightDownBmp;//向右微调按钮图片
	Bitmap rightUpBmp;
	Bitmap aimDownBmp;//目标转换按钮图片
	Bitmap aimUpBmp;
	Bitmap bgBmp;//背景图片
	Bitmap[] numberBitmaps;//时间的图片	
	Bitmap breakMarkBitmap;
	//对象引用
	List<Ball> alBalls;//所有球的列表
	Table table;//球台
	Cue cue;//球杆
	StrengthBar strengthBar;//力度条
	VirtualButton goBtn;//GO按钮
	VirtualButton leftBtn;//左按钮
	VirtualButton rightBtn;//右按钮
	VirtualButton aimBtn;//目标按钮	
	Timer timer;//计时器
	//线程引用
	GameViewDrawThread drawThread;//绘制线程
	BallGoThread ballGoThread;//球前进的线程
	KeyThread keyThread;//按键监听线程
	TimeRunningThread timeRunningThread;//负责计时的线程
	//状态值
	int keyState=0;//键盘状态  1-left 2-right 4-null 8-null 16-change bar 32-button press time
	float btnPressTime=0;//按下按钮的时间	
	//声音相关变量
	SoundPool soundPool;//声音
	HashMap<Integer, Integer> soundPoolMap; 
	MediaPlayer mMediaPlayer;	
	public static final int SHOOT_SOUND=0;//声音常量
	public static final int HIT_SOUND=1;
	public static final int BALL_IN_SOUND=2;
	public GameView(GameActivity activity) {
		super(activity);
		this.activity=activity;
		//获得焦点并设置为可触控
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//注册回调接口		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);//整个屏幕背景色	
		canvas.drawBitmap(bgBmp, 0, 0, paint);//游戏界面背景
		table.drawSelf(canvas, paint);//绘制球台
		//绘制所有球
		List<Ball> alBallsTemp=new ArrayList<Ball>(alBalls);
		for(Ball b:alBallsTemp){
			b.drawSelf(canvas, paint);
		}	
		cue.drawSelf(canvas, paint);//绘制球杆		
		strengthBar.drawSelf(canvas,paint);//绘制力度条
		goBtn.drawSelf(canvas, paint);//绘制GO按钮
		leftBtn.drawSelf(canvas, paint);//绘制左按钮
		rightBtn.drawSelf(canvas, paint);//绘制右按钮
		aimBtn.drawSelf(canvas, paint);//绘制目标按钮
		if(activity.coundDownModeFlag){
			timer.drawSelf(canvas, paint);//绘制时间
		}		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		//如果正在播放击球动画或不显示球杆，所有触控事件全都不接受
		if(cue.isShowingAnimFlag() || !cue.isShowCueFlag()){
			return true;
		}
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:     		
    		if(goBtn.isActionOnButton(x, y))//按在go按钮上
    		{
    			goBtn.pressDown();//按下go按钮    			
    		}
    		else if(leftBtn.isActionOnButton(x, y))//如果按在左按钮上
    		{
    			leftBtn.pressDown();//按下左按钮    		
    			keyState=keyState|0x20;//和0010,0000按位或，第6位置1，标志增加按键时间
    			keyState=keyState|0x1;//和00001按位或，第1位置1，标志向左移
    		}
    		else if(rightBtn.isActionOnButton(x, y))//如果按在右按钮上
    		{
    			rightBtn.pressDown();//按下右按钮    			
    			keyState=keyState|0x20;//和0010,0000按位或，第6位置1，标志增加按键时间
    			keyState=keyState|0x2;//和00010按位或，第2位置1，标志向右移
    		}
    		else if(aimBtn.isActionOnButton(x, y))//如果按在目标按钮上
    		{
    			//切换两种瞄准方式
    			cue.setAimFlag(!cue.isAimFlag());
    			//设置不同瞄准方式的按钮图
    			if(cue.isAimFlag()){
    				aimBtn.releaseUp();
    			}
    			else{
    				aimBtn.pressDown();
    			}
    		}
    		else if(strengthBar.isActionOnBar(x, y)){//如果按在力度条上
    			strengthBar.changeCurrHeight(x, y);
    		}
    		else//没有按在虚拟按钮上
    		{    			
        		cue.calcuAngle(x, y);//计算球杆旋转角度
    		}
    		break;
    	case MotionEvent.ACTION_MOVE: 
    		if(strengthBar.isActionOnBar(x, y)){//如果按在力度条上
    			strengthBar.changeCurrHeight(x, y);
    		}
    		else if(!goBtn.isActionOnButton(x, y) && 
    				!leftBtn.isActionOnButton(x, y) && 
    				!rightBtn.isActionOnButton(x, y) &&
    				!aimBtn.isActionOnButton(x, y)
    		)//也没有在力度条和按钮上
    		{
    			goBtn.releaseUp();//松开go按钮
        		keyState=keyState&0xFFEF;//和01111按位与，第5位清0，停止改变力度条
        		leftBtn.releaseUp();//松开左按钮
        		keyState=keyState&0xFFFE;//和1110按位与，第1位清0，清除左移标志        	
        		rightBtn.releaseUp();//松开右按钮
        		keyState=keyState&0xFFFD;//和1101按位或，第2位清0，清除右移标志 	
        		btnPressTime=0;//铵键时间清0
        		keyState=keyState&0xFFDF;//和1101,1111按位与，第6位清0，清除按键时间标志
        		cue.calcuAngle(x, y);//计算球杆旋转角度        		
    		}
    		break;
    	case MotionEvent.ACTION_UP:
    		if(goBtn.isActionOnButton(x, y))//按在go按钮上
    		{
    			goBtn.releaseUp();//松开go按钮        		
        		new CueAnimateThread(this).start();//创建播放击球动画的线程，并开启        		
    		}
    		else if(leftBtn.isActionOnButton(x, y))//如果按在左按钮上
    		{
    			leftBtn.releaseUp();//松开左按钮
        		keyState=keyState&0xFFFE;//和1110按位与，第1位清0，清除左移标志
        		btnPressTime=0;//铵键时间清0
        		keyState=keyState&0xFFDF;//和1101,1111按位与，第6位清0，清除按键时间标志
    		}
    		else if(rightBtn.isActionOnButton(x, y))//如果按在右按钮上
    		{
    			rightBtn.releaseUp();//松开右按钮
        		keyState=keyState&0xFFFD;//和1101按位或，第2位清0，清除右移标志
        		btnPressTime=0;//铵键时间清0
        		keyState=keyState&0xFFDF;//和1101,1111按位与，第6位清0，清除按键时间标志
    		}
    		break;
    	}
		return true;
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//创建画笔
		paint.setAntiAlias(true);//打开抗锯齿
		createAllThreads();//创建所有线程
		initBitmap();//初始化位图资源
		changeBmpsRatio(Constant.ssr.ratio);//改变图片的大小，尽量全屏，但不改变安宽高比
		changeBmpsRatioFullScreen(Constant.wRatio,Constant.hRatio);//可以变形的图片适应全屏
		initSounds();//初始化声音资源
		//初始化背景音乐
		mMediaPlayer = MediaPlayer.create(activity, R.raw.backsound);
		mMediaPlayer.setLooping(true);
		table=new Table(tableBmps);//球台
		//创建球列表，并加入所有球
		alBalls=new ArrayList<Ball>();
		for(int i=0;i<Table.AllBallsPos.length;i++)//1
		{
			alBalls.add(new Ball(ballBmps[i],this,0,0,Table.AllBallsPos[i]));
		}
		cue=new Cue(cueBmp,alBalls.get(0));//球杆	
		strengthBar=new StrengthBar(barDownBmp,barUpBmp);//力度条
		goBtn=new VirtualButton(goDownBmp,goUpBmp,Constant.GO_BTN_X,Constant.GO_BTN_Y);//GO按钮
		leftBtn=new VirtualButton(leftDownBmp,leftUpBmp,Constant.LEFT_BTN_X,Constant.LEFT_BTN_Y);//左按钮
		rightBtn=new VirtualButton(rightDownBmp,rightUpBmp,Constant.RIGHT_BTN_X,Constant.RIGHT_BTN_Y);//右按钮
		aimBtn=new VirtualButton(aimDownBmp,aimUpBmp,Constant.AIM_BTN_X,Constant.AIM_BTN_Y);//右按钮
		timer=new Timer(this,breakMarkBitmap,numberBitmaps);//创建计时器对象
		if(activity.isBackGroundMusicOn())//开启背景音乐
		{
			mMediaPlayer.start();
		}
		startAllThreads();//开启线程
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {	
		boolean retry = true;        
        while (retry){//不断地循环，直到其它线程结束
        	joinAllThreads();	      
            retry = false;
        }
        //停止背景音乐
        if(mMediaPlayer.isPlaying()){
        	mMediaPlayer.stop();
        }
	}	
	//将图片加载
	public void initBitmap(){		
		tableBmps=new Bitmap[]{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table0),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table1),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table2),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table3),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table4),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table5),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table6),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table7),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table8),				
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table9),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table10),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table11),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.table12),
			};				
		ballBmps=new Bitmap[][]{
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball00),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball01),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball02),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball00),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball01),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball02),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball10),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball11),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball12),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball10),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball11),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball12),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball20),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball21),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball22),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball20),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball21),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball22),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball30),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball31),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball32),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball30),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball31),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball32),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball40),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball41),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball42),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball40),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball41),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball42),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball50),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball51),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball52),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball50),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball51),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball52),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball60),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball61),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball62),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball60),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball61),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball62),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball70),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball71),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball72),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball70),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball71),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball72),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball80),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball81),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball82),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball80),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball81),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball82),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball90),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball91),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball92),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball90),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball91),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball92),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball100),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball101),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball102),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball100),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball101),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball102),
			},			
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball110),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball111),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball112),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball110),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball111),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball112),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball120),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball121),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball122),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball120),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball121),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball122),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball130),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball131),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball132),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball130),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball131),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball132),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball140),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball141),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball142),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball140),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball141),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball142),
			},
			{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball150),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball151),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball152),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball150),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball151),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.ball152),
			},
		};		
		cueBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.qiu_gan);
		barDownBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.ruler);
		barUpBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.pointer);		
		goDownBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.go_down);
		goUpBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.go_up);
		
		leftDownBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.left_down);
		leftUpBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.left_up);
		rightDownBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.right_down);
		rightUpBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.right_up);
		aimDownBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.aim_down);
		aimUpBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.aim_up);
		
		bgBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		numberBitmaps=new Bitmap[]{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number1),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number2),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number3),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number4),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number5),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number6),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number7),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number8),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number9),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
		};
		breakMarkBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.breakmark);
	}
	public void changeBmpsRatio(float ratio){
		for(int i=0;i<tableBmps.length;i++){
			tableBmps[i]=PicLoadUtil.scaleToFit(tableBmps[i], ratio);
		}
		for(int i=0;i<ballBmps.length;i++){
			for(int j=0;j<ballBmps[i].length;j++){
				ballBmps[i][j]=PicLoadUtil.scaleToFit(ballBmps[i][j], ratio);
			}			
		}
		cueBmp=PicLoadUtil.scaleToFit(cueBmp, ratio);
		barDownBmp=PicLoadUtil.scaleToFit(barDownBmp, ratio);
		barUpBmp=PicLoadUtil.scaleToFit(barUpBmp, ratio);
		goDownBmp=PicLoadUtil.scaleToFit(goDownBmp, ratio);
		goUpBmp=PicLoadUtil.scaleToFit(goUpBmp, ratio);
		
		leftDownBmp=PicLoadUtil.scaleToFit(leftDownBmp, ratio);
		leftUpBmp=PicLoadUtil.scaleToFit(leftUpBmp, ratio);
		rightDownBmp=PicLoadUtil.scaleToFit(rightDownBmp, ratio);
		rightUpBmp=PicLoadUtil.scaleToFit(rightUpBmp, ratio);
		aimDownBmp=PicLoadUtil.scaleToFit(aimDownBmp, ratio);		
		aimUpBmp=PicLoadUtil.scaleToFit(aimUpBmp, ratio);	
		for(int i=0;i<numberBitmaps.length;i++){
			numberBitmaps[i]=PicLoadUtil.scaleToFit(numberBitmaps[i], ratio);
		}
		breakMarkBitmap=PicLoadUtil.scaleToFit(breakMarkBitmap, ratio);	
	}
	public void changeBmpsRatioFullScreen(float wRatio,float hRatio){
		bgBmp=PicLoadUtil.scaleToFitFullScreen(bgBmp, wRatio, hRatio);		
	}
	void createAllThreads()
	{
		drawThread=new GameViewDrawThread(this);//创建绘制线程
		ballGoThread=new BallGoThread(this);	
		keyThread=new KeyThread(this);
		if(activity.coundDownModeFlag){
			timeRunningThread=new TimeRunningThread(this);//创建计时线程
		}
	}
	void startAllThreads()
	{
		drawThread.setFlag(true);  
		drawThread.start();
		ballGoThread.setFlag(true);
		ballGoThread.start();
		keyThread.setFlag(true);
		keyThread.start();
		if(activity.coundDownModeFlag){
			timeRunningThread.setFlag(true);		
		    timeRunningThread.start();//启动计时线程
		}		
	}
	void stopAllThreads()
	{
		drawThread.setFlag(false);   
		ballGoThread.setFlag(false);
		keyThread.setFlag(false);
		if(activity.coundDownModeFlag){
			timeRunningThread.setFlag(false);
		}
	}
	void joinAllThreads()
	{
		try {
			drawThread.join();		
			keyThread.join();
			ballGoThread.join();
			if(activity.coundDownModeFlag){
				timeRunningThread.join();
			}			
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
	//结束游戏的方法
	public void overGame()
	{
        stopAllThreads();
		//停止背景音乐
        if(mMediaPlayer.isPlaying()){
        	mMediaPlayer.stop();
        }
        if(activity.coundDownModeFlag){//如果是倒计时模式
        	activity.currScore=timer.getLeftSecond();//将总得分赋值给activity中的score
    		activity.sendMessage(WhatMessage.OVER_GAME);//获得积分榜中最高分
        } 
        else{
        	activity.sendMessage(WhatMessage.GOTO_CHOICE_VIEW);//获得积分榜中最高分
        }
	}	
	//重新绘制的方法
    public void repaint()
	{
		Canvas canvas=this.getHolder().lockCanvas();
		try
		{
			synchronized(canvas)
			{
				onDraw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				this.getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
    //初始化声音的方法
	public void initSounds(){
	     soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     soundPoolMap.put(SHOOT_SOUND, soundPool.load(getContext(), R.raw.shoot, 1));
	     soundPoolMap.put(HIT_SOUND, soundPool.load(getContext(), R.raw.hit, 1));
	     soundPoolMap.put(BALL_IN_SOUND, soundPool.load(getContext(), R.raw.ballin, 1));
	}
	//播放声音的方法
	public void playSound(int sound, int loop) {
	    AudioManager mgr = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   	    
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
}
