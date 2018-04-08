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
 * ��Ϸ����
 *
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	GameActivity activity;//activity������
	Paint paint;//��������	
	Bitmap[] tableBmps;//��̨ͼƬ
	Bitmap cueBmp;//���ͼƬ
	Bitmap[][] ballBmps;//�������ͼƬ	
	Bitmap barDownBmp;//��������ͼƬ
	Bitmap barUpBmp;	
	Bitmap goDownBmp;//GO��ťͼƬ
	Bitmap goUpBmp;
	Bitmap leftDownBmp;//����΢����ťͼƬ
	Bitmap leftUpBmp;
	Bitmap rightDownBmp;//����΢����ťͼƬ
	Bitmap rightUpBmp;
	Bitmap aimDownBmp;//Ŀ��ת����ťͼƬ
	Bitmap aimUpBmp;
	Bitmap bgBmp;//����ͼƬ
	Bitmap[] numberBitmaps;//ʱ���ͼƬ	
	Bitmap breakMarkBitmap;
	//��������
	List<Ball> alBalls;//��������б�
	Table table;//��̨
	Cue cue;//���
	StrengthBar strengthBar;//������
	VirtualButton goBtn;//GO��ť
	VirtualButton leftBtn;//��ť
	VirtualButton rightBtn;//�Ұ�ť
	VirtualButton aimBtn;//Ŀ�갴ť	
	Timer timer;//��ʱ��
	//�߳�����
	GameViewDrawThread drawThread;//�����߳�
	BallGoThread ballGoThread;//��ǰ�����߳�
	KeyThread keyThread;//���������߳�
	TimeRunningThread timeRunningThread;//�����ʱ���߳�
	//״ֵ̬
	int keyState=0;//����״̬  1-left 2-right 4-null 8-null 16-change bar 32-button press time
	float btnPressTime=0;//���°�ť��ʱ��	
	//������ر���
	SoundPool soundPool;//����
	HashMap<Integer, Integer> soundPoolMap; 
	MediaPlayer mMediaPlayer;	
	public static final int SHOOT_SOUND=0;//��������
	public static final int HIT_SOUND=1;
	public static final int BALL_IN_SOUND=2;
	public GameView(GameActivity activity) {
		super(activity);
		this.activity=activity;
		//��ý��㲢����Ϊ�ɴ���
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//ע��ص��ӿ�		
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);//������Ļ����ɫ	
		canvas.drawBitmap(bgBmp, 0, 0, paint);//��Ϸ���汳��
		table.drawSelf(canvas, paint);//������̨
		//����������
		List<Ball> alBallsTemp=new ArrayList<Ball>(alBalls);
		for(Ball b:alBallsTemp){
			b.drawSelf(canvas, paint);
		}	
		cue.drawSelf(canvas, paint);//�������		
		strengthBar.drawSelf(canvas,paint);//����������
		goBtn.drawSelf(canvas, paint);//����GO��ť
		leftBtn.drawSelf(canvas, paint);//������ť
		rightBtn.drawSelf(canvas, paint);//�����Ұ�ť
		aimBtn.drawSelf(canvas, paint);//����Ŀ�갴ť
		if(activity.coundDownModeFlag){
			timer.drawSelf(canvas, paint);//����ʱ��
		}		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		//������ڲ��Ż��򶯻�����ʾ��ˣ����д����¼�ȫ��������
		if(cue.isShowingAnimFlag() || !cue.isShowCueFlag()){
			return true;
		}
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:     		
    		if(goBtn.isActionOnButton(x, y))//����go��ť��
    		{
    			goBtn.pressDown();//����go��ť    			
    		}
    		else if(leftBtn.isActionOnButton(x, y))//���������ť��
    		{
    			leftBtn.pressDown();//������ť    		
    			keyState=keyState|0x20;//��0010,0000��λ�򣬵�6λ��1����־���Ӱ���ʱ��
    			keyState=keyState|0x1;//��00001��λ�򣬵�1λ��1����־������
    		}
    		else if(rightBtn.isActionOnButton(x, y))//��������Ұ�ť��
    		{
    			rightBtn.pressDown();//�����Ұ�ť    			
    			keyState=keyState|0x20;//��0010,0000��λ�򣬵�6λ��1����־���Ӱ���ʱ��
    			keyState=keyState|0x2;//��00010��λ�򣬵�2λ��1����־������
    		}
    		else if(aimBtn.isActionOnButton(x, y))//�������Ŀ�갴ť��
    		{
    			//�л�������׼��ʽ
    			cue.setAimFlag(!cue.isAimFlag());
    			//���ò�ͬ��׼��ʽ�İ�ťͼ
    			if(cue.isAimFlag()){
    				aimBtn.releaseUp();
    			}
    			else{
    				aimBtn.pressDown();
    			}
    		}
    		else if(strengthBar.isActionOnBar(x, y)){//���������������
    			strengthBar.changeCurrHeight(x, y);
    		}
    		else//û�а������ⰴť��
    		{    			
        		cue.calcuAngle(x, y);//���������ת�Ƕ�
    		}
    		break;
    	case MotionEvent.ACTION_MOVE: 
    		if(strengthBar.isActionOnBar(x, y)){//���������������
    			strengthBar.changeCurrHeight(x, y);
    		}
    		else if(!goBtn.isActionOnButton(x, y) && 
    				!leftBtn.isActionOnButton(x, y) && 
    				!rightBtn.isActionOnButton(x, y) &&
    				!aimBtn.isActionOnButton(x, y)
    		)//Ҳû�����������Ͱ�ť��
    		{
    			goBtn.releaseUp();//�ɿ�go��ť
        		keyState=keyState&0xFFEF;//��01111��λ�룬��5λ��0��ֹͣ�ı�������
        		leftBtn.releaseUp();//�ɿ���ť
        		keyState=keyState&0xFFFE;//��1110��λ�룬��1λ��0��������Ʊ�־        	
        		rightBtn.releaseUp();//�ɿ��Ұ�ť
        		keyState=keyState&0xFFFD;//��1101��λ�򣬵�2λ��0��������Ʊ�־ 	
        		btnPressTime=0;//識�ʱ����0
        		keyState=keyState&0xFFDF;//��1101,1111��λ�룬��6λ��0���������ʱ���־
        		cue.calcuAngle(x, y);//���������ת�Ƕ�        		
    		}
    		break;
    	case MotionEvent.ACTION_UP:
    		if(goBtn.isActionOnButton(x, y))//����go��ť��
    		{
    			goBtn.releaseUp();//�ɿ�go��ť        		
        		new CueAnimateThread(this).start();//�������Ż��򶯻����̣߳�������        		
    		}
    		else if(leftBtn.isActionOnButton(x, y))//���������ť��
    		{
    			leftBtn.releaseUp();//�ɿ���ť
        		keyState=keyState&0xFFFE;//��1110��λ�룬��1λ��0��������Ʊ�־
        		btnPressTime=0;//識�ʱ����0
        		keyState=keyState&0xFFDF;//��1101,1111��λ�룬��6λ��0���������ʱ���־
    		}
    		else if(rightBtn.isActionOnButton(x, y))//��������Ұ�ť��
    		{
    			rightBtn.releaseUp();//�ɿ��Ұ�ť
        		keyState=keyState&0xFFFD;//��1101��λ�򣬵�2λ��0��������Ʊ�־
        		btnPressTime=0;//識�ʱ����0
        		keyState=keyState&0xFFDF;//��1101,1111��λ�룬��6λ��0���������ʱ���־
    		}
    		break;
    	}
		return true;
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//��������
		paint.setAntiAlias(true);//�򿪿����
		createAllThreads();//���������߳�
		initBitmap();//��ʼ��λͼ��Դ
		changeBmpsRatio(Constant.ssr.ratio);//�ı�ͼƬ�Ĵ�С������ȫ���������ı䰲��߱�
		changeBmpsRatioFullScreen(Constant.wRatio,Constant.hRatio);//���Ա��ε�ͼƬ��Ӧȫ��
		initSounds();//��ʼ��������Դ
		//��ʼ����������
		mMediaPlayer = MediaPlayer.create(activity, R.raw.backsound);
		mMediaPlayer.setLooping(true);
		table=new Table(tableBmps);//��̨
		//�������б�������������
		alBalls=new ArrayList<Ball>();
		for(int i=0;i<Table.AllBallsPos.length;i++)//1
		{
			alBalls.add(new Ball(ballBmps[i],this,0,0,Table.AllBallsPos[i]));
		}
		cue=new Cue(cueBmp,alBalls.get(0));//���	
		strengthBar=new StrengthBar(barDownBmp,barUpBmp);//������
		goBtn=new VirtualButton(goDownBmp,goUpBmp,Constant.GO_BTN_X,Constant.GO_BTN_Y);//GO��ť
		leftBtn=new VirtualButton(leftDownBmp,leftUpBmp,Constant.LEFT_BTN_X,Constant.LEFT_BTN_Y);//��ť
		rightBtn=new VirtualButton(rightDownBmp,rightUpBmp,Constant.RIGHT_BTN_X,Constant.RIGHT_BTN_Y);//�Ұ�ť
		aimBtn=new VirtualButton(aimDownBmp,aimUpBmp,Constant.AIM_BTN_X,Constant.AIM_BTN_Y);//�Ұ�ť
		timer=new Timer(this,breakMarkBitmap,numberBitmaps);//������ʱ������
		if(activity.isBackGroundMusicOn())//������������
		{
			mMediaPlayer.start();
		}
		startAllThreads();//�����߳�
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {	
		boolean retry = true;        
        while (retry){//���ϵ�ѭ����ֱ�������߳̽���
        	joinAllThreads();	      
            retry = false;
        }
        //ֹͣ��������
        if(mMediaPlayer.isPlaying()){
        	mMediaPlayer.stop();
        }
	}	
	//��ͼƬ����
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
		drawThread=new GameViewDrawThread(this);//���������߳�
		ballGoThread=new BallGoThread(this);	
		keyThread=new KeyThread(this);
		if(activity.coundDownModeFlag){
			timeRunningThread=new TimeRunningThread(this);//������ʱ�߳�
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
		    timeRunningThread.start();//������ʱ�߳�
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
	//������Ϸ�ķ���
	public void overGame()
	{
        stopAllThreads();
		//ֹͣ��������
        if(mMediaPlayer.isPlaying()){
        	mMediaPlayer.stop();
        }
        if(activity.coundDownModeFlag){//����ǵ���ʱģʽ
        	activity.currScore=timer.getLeftSecond();//���ܵ÷ָ�ֵ��activity�е�score
    		activity.sendMessage(WhatMessage.OVER_GAME);//��û��ְ�����߷�
        } 
        else{
        	activity.sendMessage(WhatMessage.GOTO_CHOICE_VIEW);//��û��ְ�����߷�
        }
	}	
	//���»��Ƶķ���
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
    //��ʼ�������ķ���
	public void initSounds(){
	     soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     soundPoolMap.put(SHOOT_SOUND, soundPool.load(getContext(), R.raw.shoot, 1));
	     soundPoolMap.put(HIT_SOUND, soundPool.load(getContext(), R.raw.hit, 1));
	     soundPoolMap.put(BALL_IN_SOUND, soundPool.load(getContext(), R.raw.ballin, 1));
	}
	//���������ķ���
	public void playSound(int sound, int loop) {
	    AudioManager mgr = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   	    
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
}
