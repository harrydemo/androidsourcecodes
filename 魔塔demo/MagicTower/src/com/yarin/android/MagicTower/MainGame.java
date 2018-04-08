package com.yarin.android.MagicTower;

import android.app.Activity;
import android.content.Context;

public class MainGame
{
	private static GameView	m_GameView		= null;		// 当前需要显示的对象
	private Context				m_Context		= null;
	private MagicTower			m_MagicTower	= null;
	private int 				m_status		= -1;		//游戏状态
	public  CMIDIPlayer mCMIDIPlayer;
	public byte mbMusic = 0;
	public MainGame(Context context)
	{	
		m_Context = context;
		m_MagicTower = (MagicTower)context;
		m_status = -1;
		
		initGame();
	}
	
	//初始化游戏
	public void initGame()
	{
		controlView(yarin.GAME_SPLASH);
		mCMIDIPlayer = new CMIDIPlayer(m_MagicTower);
	}
	//得到游戏状态
	public int getStatus()
	{
		return m_status;
	}
	//设置游戏状态
	public void setStatus(int status)
	{
		m_status = status;
	}
	//得到主类对象
	public Activity getMagicTower()
	{
		return m_MagicTower;
	}
	
	//得到当前需要显示的对象
	public static GameView getMainView() 
	{
	    return m_GameView;
	}
	
	//控制显示什么界面
	public void controlView(int status)
	{
		if(m_status != status)
		{
			if(m_GameView != null)
			{
				m_GameView.reCycle();
				System.gc();				
			}
		}
		freeGameView(m_GameView);
		switch (status)
		{
		case yarin.GAME_SPLASH:
			m_GameView = new SplashScreen(m_Context,this);
			break;
		case yarin.GAME_MENU:
			m_GameView = new MainMenu(m_Context,this);
			break;
		case yarin.GAME_HELP:
			m_GameView = new HelpScreen(m_Context,this);
			break;
		case yarin.GAME_ABOUT:
			m_GameView = new AboutScreen(m_Context,this);
			break;
		case yarin.GAME_RUN:
			m_GameView = new GameScreen(m_Context,m_MagicTower,this,true);
			break;
		case yarin.GAME_CONTINUE:
			m_GameView = new GameScreen(m_Context,m_MagicTower,this,false);
			break;
		}
		setStatus(status);
	}
	
	//释放界面对象
	public void freeGameView(GameView gameView)
	{
		if(gameView != null)
		{
			gameView = null;
			System.gc();
		}
	}
}

