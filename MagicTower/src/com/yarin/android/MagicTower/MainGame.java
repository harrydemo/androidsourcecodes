package com.yarin.android.MagicTower;

import android.app.Activity;
import android.content.Context;

public class MainGame
{
	private static GameView	m_GameView		= null;		// ��ǰ��Ҫ��ʾ�Ķ���
	private Context				m_Context		= null;
	private MagicTower			m_MagicTower	= null;
	private int 				m_status		= -1;		//��Ϸ״̬
	public  CMIDIPlayer mCMIDIPlayer;
	public byte mbMusic = 0;
	public MainGame(Context context)
	{	
		m_Context = context;
		m_MagicTower = (MagicTower)context;
		m_status = -1;
		
		initGame();
	}
	
	//��ʼ����Ϸ
	public void initGame()
	{
		controlView(yarin.GAME_SPLASH);
		mCMIDIPlayer = new CMIDIPlayer(m_MagicTower);
	}
	//�õ���Ϸ״̬
	public int getStatus()
	{
		return m_status;
	}
	//������Ϸ״̬
	public void setStatus(int status)
	{
		m_status = status;
	}
	//�õ��������
	public Activity getMagicTower()
	{
		return m_MagicTower;
	}
	
	//�õ���ǰ��Ҫ��ʾ�Ķ���
	public static GameView getMainView() 
	{
	    return m_GameView;
	}
	
	//������ʾʲô����
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
	
	//�ͷŽ������
	public void freeGameView(GameView gameView)
	{
		if(gameView != null)
		{
			gameView = null;
			System.gc();
		}
	}
}

