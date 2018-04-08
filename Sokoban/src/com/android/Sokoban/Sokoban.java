package com.android.Sokoban;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class Sokoban extends Activity
{
	private ThreadCanvas	mThreadCanvas	= null;
	private MainGame mMainGame;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mMainGame = new MainGame(this);
		mThreadCanvas = new ThreadCanvas(this);
		
		setContentView(mThreadCanvas);
		mThreadCanvas.start();
		
		//setContentView(R.layout.main);
	}
	protected void onPause()
	{
		super.onPause();
		mThreadCanvas.onThreadPause();
	}
	protected void onResume()
	{
		super.onResume();
		mThreadCanvas.requestFocus();
		mThreadCanvas.onThreadResume();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		mThreadCanvas.onKeyDown(keyCode);
		return false;
	}
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if(KeyEvent.KEYCODE_T == keyCode)
		{
			if(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			else
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			return true;
		}
		mThreadCanvas.onKeyUp(keyCode);
		return true;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		if(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == newConfig.orientation ||
			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == newConfig.orientation)
		{
			mMainGame.setScreenOrentation(newConfig.orientation);
			mThreadCanvas.onScreenConfigrationChange(newConfig.orientation);
		}
		else
		{
			mMainGame.checkScreenResulution();
			mThreadCanvas.onScreenConfigrationChange(mMainGame.getScreenOrentation());
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	@Override
	public void finish()
	{
		mThreadCanvas.onThreadStop();
		super.finish();
	}
}