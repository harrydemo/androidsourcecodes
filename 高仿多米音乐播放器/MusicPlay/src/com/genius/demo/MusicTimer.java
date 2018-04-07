package com.genius.demo;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MusicTimer {

	private int 	mEventID;
	
	private Handler mHandler;
	
	private Timer   mTimer;
	
	private TimerTask	mTimerTask;		// ��ʱ������
			 
	private int     mTimerInterval;		// ��ʱ���������ʱ��(ms)
	
	private boolean mBStartTimer;		// ��ʱ���Ƿ��ѿ���
	
	public MusicTimer(Handler handler, int eventID)
	{
		initParam(handler, eventID);
	}
	
	private void initParam(Handler handler,  int eventID)
	{
		mHandler = handler;
		
		mEventID = eventID;
		
		mTimerInterval = 1000;
		
		mBStartTimer = false;
		
		mTimerTask = null;
		
		mTimer = new Timer();
	}
	
	public void startTimer()
	{
		if (mHandler == null || mBStartTimer == true)
		{
			return ;
		}		
		
		mBStartTimer = true;
		mTimerTask = new MusicTimerTask();
		mTimer.schedule(mTimerTask, mTimerInterval, mTimerInterval);
		
	}
	
	public void stopTimer()
	{
		if (mBStartTimer == false)
		{
			return ;
		}
		
		mBStartTimer = false;
		if (mTimerTask != null)
		{
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}
	
	
	class MusicTimerTask extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mHandler != null)
			{
				Message msgMessage = mHandler.obtainMessage(mEventID);
				msgMessage.sendToTarget();
			}
		}
		
	}
}
