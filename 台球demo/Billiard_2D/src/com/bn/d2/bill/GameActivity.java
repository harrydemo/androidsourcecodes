package com.bn.d2.bill;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;

import android.view.WindowManager;
import android.widget.Toast;


//标识所有SurfaceView的常量类
class WhatMessage{
	
public static final int GOTO_WELLCOME_VIEW=0;//欢迎界面
	public static final int GOTO_MAIN_MENU_VIEW=1;//主菜单界面
	public static final int GOTO_GAME_VIEW=2;//游戏界面
	public static final int GOTO_SOUND_CONTORL_VIEW=3;//声音控制界面
	
public static final int GOTO_WIN_VIEW=4;//胜利界面
	public static final int GOTO_FAIL_VIEW=5;//失败界面
	public static final int GOTO_HIGH_SCORE_VIEW=6;//排行榜界面
	public static final int GOTO_HELP_VIEW=7;

public static final int GOTO_ABOUT_VIEW=8;

public static final int GOTO_CHOICE_VIEW=9;

public static final int OVER_GAME=20;//游戏结束
	
}


public class GameActivity extends Activity {

	int currentView;//标识当前的界面

	WellcomeView wellcomeView;//显示欢迎动画界面
	MainMenuView mainMenuView;//主菜单界面
	
HelpView helpView;//帮助界面
	
AboutView aboutView;//关于界面
   
 GameView gameView;//游戏界面
    WinView winView;//胜利界面

	FailView failView;//失败界面	
	SoundControlView soundControlView;//音效控制界面
	HighScoreView highScoreView;//积分榜界面
	ChoiceView choiceView;
	
	boolean coundDownModeFlag=true;//是否为倒计时模式的标志
	private boolean backGroundMusicOn=false;//背景音乐是否开启的标志
	private boolean soundOn=true;//音效是否开启的标志
	static int initTime=0;//初始化的次数   
    int currScore;//游戏结束后的得分
	int highestScore;
    SQLiteDatabase sld;//SQLiteDatabase数据库
    Handler myHandler = new Handler(){//处理各个SurfaceView发送的消息
        public void handleMessage(Message msg) {
        	switch(msg.what)
        	{
        	case WhatMessage.GOTO_MAIN_MENU_VIEW:
        		gotoMainMenuView();
        		break;
        	case WhatMessage.GOTO_GAME_VIEW:
        		gotoGameView();
        		break;
        	case WhatMessage.GOTO_SOUND_CONTORL_VIEW:
        		gotoSoundControlView();
        		break;
        	case WhatMessage.GOTO_WIN_VIEW:
        		gotoWinView();
        		break;
        	case WhatMessage.GOTO_FAIL_VIEW:
        		gotoFailView();
        		break;
        	case WhatMessage.GOTO_HIGH_SCORE_VIEW:
        		gotoHighScoreView();
        		break;
        	case WhatMessage.GOTO_WELLCOME_VIEW:
        		gotoWellcomeView();
        		break;
        	case WhatMessage.GOTO_HELP_VIEW:
        		gotoHelpView();
        		break;
        	case WhatMessage.GOTO_ABOUT_VIEW:
        		gotoAboutView();
        		break;
        	case WhatMessage.GOTO_CHOICE_VIEW:
        		gotoChoiceView();
        		break;
        	case WhatMessage.OVER_GAME:
        		goToOverView();
        		break;
        	}
        }
	};
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);    
        //全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置为横屏
        //游戏过程中只允许调整多媒体音量，而不允许调整通话音量
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //获取分辨率
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);    
        if(initTime==0){
        	Constant.initConst(dm.widthPixels, dm.heightPixels);//初始化常量
        	initTime++;
        }
        gotoWellcomeView();//去欢迎界面  
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e)
    {
    	switch(keyCode)
    	{
    	    case 4://返回键
		    	switch(currentView)
		    	{
		    	case WhatMessage.GOTO_WELLCOME_VIEW:
		    		break;
		    	case WhatMessage.GOTO_MAIN_MENU_VIEW:
		    		System.exit(0);
		    		break;
		    	case WhatMessage.GOTO_HIGH_SCORE_VIEW:
		    		gotoChoiceView();
		    		break;
		    	case WhatMessage.GOTO_GAME_VIEW:
		    	case WhatMessage.GOTO_SOUND_CONTORL_VIEW:
		    	case WhatMessage.GOTO_WIN_VIEW:
		    	case WhatMessage.GOTO_FAIL_VIEW: 
		    	case WhatMessage.GOTO_HELP_VIEW:
		    	case WhatMessage.GOTO_ABOUT_VIEW:
		    	case WhatMessage.GOTO_CHOICE_VIEW:
		    		gotoMainMenuView();
		    		break;
		    	}
		    return true;	    	
    	}
		return false;
    }
    //向Handler发送信息的方法
    public void sendMessage(int what)
    {
    	Message msg1 = myHandler.obtainMessage(what); 
    	myHandler.sendMessage(msg1);
    }  
    public boolean isBackGroundMusicOn() {
		return backGroundMusicOn;
	}
	public void setBackGroundMusicOn(boolean backGroundMusicOn) {
		this.backGroundMusicOn = backGroundMusicOn;
	}
	
	public boolean isSoundOn() {
		return soundOn;
	}
	public void setSoundOn(boolean soundOn) {
		this.soundOn = soundOn;
	}
    //去欢迎界面的方法
    private void gotoWellcomeView()
    {
    	if(wellcomeView==null)
    	{
    		 wellcomeView = new WellcomeView(this);
    	}
    	this.setContentView(wellcomeView);
    	currentView=WhatMessage.GOTO_WELLCOME_VIEW;
    } 
    //去主菜单界面的方法
    private void gotoMainMenuView()
    {
    	//如果游戏正在进行中退出了游戏，先停止游戏中所有线程
    	if(gameView!=null){
    		gameView.stopAllThreads();
    	}
    	if(mainMenuView==null)
    	{
    		mainMenuView = new MainMenuView(this);
    	}
    	this.setContentView(mainMenuView);
    	currentView=WhatMessage.GOTO_MAIN_MENU_VIEW;
    }   
    //去游戏界面
    private void gotoGameView()
    {
    	if(gameView==null)
    	{
    		gameView = new GameView(this);
    	}
    	this.setContentView(gameView);
    	currentView=WhatMessage.GOTO_GAME_VIEW;
    }
    //去帮助界面的方法
    private void gotoHelpView()
    {
    	if(helpView==null)
    	{
    		helpView = new HelpView(this);
    	}
    	this.setContentView(helpView);
    	currentView=WhatMessage.GOTO_HELP_VIEW;
    } 
    //去关于界面的方法
    private void gotoAboutView()
    {
    	if(aboutView==null)
    	{
    		aboutView = new AboutView(this);
    	}
    	this.setContentView(aboutView);
    	currentView=WhatMessage.GOTO_ABOUT_VIEW;
    } 
    //去设置声音界面的方法
    private void gotoSoundControlView()
    {
    	if(soundControlView==null)
    	{
    		soundControlView = new SoundControlView(this);
    	}
    	this.setContentView(soundControlView);
    	currentView=WhatMessage.GOTO_SOUND_CONTORL_VIEW;
    }
    //去胜利界面的方法
    private void gotoWinView()
    {
    	if(winView==null)
    	{
    		winView=new WinView(this);
    	}
    	this.setContentView(winView);
    	currentView=WhatMessage.GOTO_WIN_VIEW;
    }
    //去失败界面的方法
    private void gotoFailView()
    {
    	if(failView==null)
    	{
    		failView=new FailView(this);
    	}
    	this.setContentView(failView);
    	currentView=WhatMessage.GOTO_FAIL_VIEW;
    }
    //去选项界面的方法
    private void gotoChoiceView()
    {
    	if(choiceView==null)
    	{
    		choiceView=new ChoiceView(this);
    	}
    	this.setContentView(choiceView);
    	currentView=WhatMessage.GOTO_CHOICE_VIEW;
    }
    //去积分榜界面的方法
    private void gotoHighScoreView()
    {
    	if(highScoreView==null)
    	{
    		highScoreView = new HighScoreView(this);
    	}
    	this.setContentView(highScoreView);
    	currentView=WhatMessage.GOTO_HIGH_SCORE_VIEW;
    }
    //打开或创建数据库的方法
    public void openOrCreateDatabase()
    {
    	try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/com.bn.d2.bill/mydb", //数据库所在路径
	    			null, 								//CursorFactory
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //读写、若不存在则创建
	    	);
	    	String sql="create table if not exists highScore" +
	    			"( " +
	    			"score integer," +
	    			"date varchar(20)" +
	    			");";
	    	sld.execSQL(sql);
    	}
    	catch(Exception e)
    	{
    		Toast.makeText(this, "数据库错误："+e.toString(), Toast.LENGTH_SHORT).show();
    	}
    }
    //关闭数据库的方法
    public void closeDatabase()
    {
    	try
    	{
	    	sld.close();
    	}
		catch(Exception e)
		{
			Toast.makeText(this, "数据库错误："+e.toString(), Toast.LENGTH_SHORT).show();;
		}
    }
    //插入记录的方法
    public void insert(int score,String date)
    {
    	try
    	{
        	String sql="insert into highScore values("+score+",'"+date+"');";
        	sld.execSQL(sql);
        	sld.close();
    	}
		catch(Exception e)
		{
			Toast.makeText(this, "数据库错误："+e.toString(), Toast.LENGTH_SHORT).show();;
		}
    }
    //查询的方法
    public String query(int posFrom,int length)//开始的位置，要查寻的记录条数
    {
    	StringBuilder sb=new StringBuilder();//要返回的结果
    	Cursor cur=null;
    	openOrCreateDatabase();
        String sql="select score,date from highScore order by score desc;";
        cur=sld.rawQuery(sql, null);
    	try
    	{    		
        	cur.moveToPosition(posFrom);//将游标移动到指定的开始位置
        	int count=0;//当前查询记录条数
        	while(cur.moveToNext()&&count<length)
        	{
        		int score=cur.getInt(0);
        		String date=cur.getString(1);        		
        		sb.append(score);
        		sb.append("/");
        		sb.append(date);
        		sb.append("/");//将记录用"/"分隔开
        		count++;
        	}        			
    	}
		catch(Exception e)
		{
			Toast.makeText(this, "数据库错误："+e.toString(), Toast.LENGTH_SHORT).show();
		}
		finally
		{
			cur.close();
			closeDatabase();
		}
		//转换成字符，并返回
		return sb.toString();
    }
    //得到数据库中记录条数的方法
    public int getRowCount()
    {
    	int result=0;
    	Cursor cur=null;
    	openOrCreateDatabase();
    	try
    	{
    		String sql="select count(score) from highScore;";    	
            cur=sld.rawQuery(sql, null);
        	if(cur.moveToNext())
        	{
        		result=cur.getInt(0);
        	}
    	}
    	catch(Exception e)
		{
			Toast.makeText(this, "数据库错误："+e.toString(), Toast.LENGTH_SHORT).show();
		}
		finally
		{
			cur.close();
			closeDatabase();
		}
        
    	return result;
    }
    //将得分和时间插入数据库，并跳转到相应的结束界面
    private void goToOverView()
    {
    	Cursor cur=null;
    	openOrCreateDatabase();//打开或创建数据库
    	try{
    		//从数据库中选出最高分
        	String sql="select max(score) from highScore;";   	
        	cur=sld.rawQuery(sql, null);
        	if(cur.moveToNext())//如果结果集不为空，移到下一行
        	{
        		this.highestScore=cur.getInt(0);
        	}
        	insert(currScore,DateUtil.getCurrentDate());//获得当前分数和日期并插入数据库        	
    	}
		catch(Exception e)
		{
			Toast.makeText(this, "数据库错误："+e.toString(), Toast.LENGTH_SHORT).show();
		}
		finally
		{
			cur.close();
			closeDatabase();
		}		
		if(currScore>highestScore)//如果当前得分大于积分榜中最高分
		{    	
	    	this.gotoWinView();//进入胜利的界面
		}
		else//如果当前得分不大于积分榜中最高分
		{
			this.gotoFailView();//进入失败的界面
		}
    	
    }
}