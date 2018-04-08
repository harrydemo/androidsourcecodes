package com.android.Sokoban;

import com.android.GeneralDesign.clienDB;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ThreadCanvas extends View implements Runnable
{
	protected int mThreadState = CanvasThreadState.READY;
	public static final class CanvasThreadState
	{
		public static final int READY = 1;
		public static final int RUNNING = 2;
		public static final int PAUSE = 3;
		public static final int STOP = 4;
	}
	public ThreadCanvas(Context context)
	{
		super(context);
	}
	protected void onDraw(Canvas canvas)
	{
		if (MainGame.getMainView() != null)
		{
			MainGame.getMainView().onDraw(canvas);
		}
		else
		{
			Log.i(clienDB.LOG_TAG, "null");
		}
	}
	public void start()
	{		
		Thread t = new Thread(this);
		t.start();
		mThreadState = CanvasThreadState.RUNNING;
	}
	public void refurbish()
	{
		if (MainGame.getMainView() != null)
		{
			MainGame.getMainView().refurbish();
		}
	}
	public void onThreadPause()
	{
		mThreadState = CanvasThreadState.PAUSE;
	}
	public void onThreadResume()
	{
		mThreadState = CanvasThreadState.RUNNING;
	}
	public void onThreadStop()
	{
		mThreadState = CanvasThreadState.STOP;
	}
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(clienDB.GAME_LOOP);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			switch(mThreadState)
			{
			case CanvasThreadState.RUNNING:
				refurbish();
				postInvalidate();
				break;
			case CanvasThreadState.STOP:
				return;
			case CanvasThreadState.PAUSE:
			case CanvasThreadState.READY:
			default:
				break;
			}
		}
	}
	boolean onKeyDown(int keyCode)
	{
		if (MainGame.getMainView() != null)
		{
			MainGame.getMainView().onKeyDown(keyCode);
		}
		else
		{
			Log.i(clienDB.LOG_TAG, "null");
		}
		return true;
	}
	boolean onKeyUp(int keyCode)
	{
		if (MainGame.getMainView() != null)
		{
			MainGame.getMainView().onKeyUp(keyCode);
		}
		else
		{
			Log.i(clienDB.LOG_TAG, "null");
		}
		return true;
	}
	public boolean onTouchEvent(MotionEvent event)
	{
		if (MainGame.getMainView() != null)
		{
			MainGame.getMainView().onTouchEvent(event);
		}
		else
		{
			Log.i(clienDB.LOG_TAG, "null");
		}
		return true;
	}
	public boolean onScreenConfigrationChange(int newOrientation)
	{
		if (MainGame.getMainView() != null)
		{
			MainGame.getMainView().onScreenConfigrationChange(newOrientation);
		}
		else
		{
			Log.i(clienDB.LOG_TAG, "null");
		}
		return true;
	}
}
