package com.yarin.android.MagicTower;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class MagicTower extends Activity
{
	private ThreadCanvas	mThreadCanvas	= null;


	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		new MainGame(this);
		mThreadCanvas = new ThreadCanvas(this);

		setContentView(mThreadCanvas);
	}


	/**
	 * ��ͣ
	 * 
	 * @param N
	 *            /A
	 * 
	 * @return null
	 */
	protected void onPause()
	{
		super.onPause();
	}


	/**
	 * �ػ�
	 * 
	 * @param N
	 *            /A
	 * 
	 * @return null
	 */
	protected void onResume()
	{
		super.onResume();
		mThreadCanvas.requestFocus();
		mThreadCanvas.start();
	}


	/**
	 * ��������
	 * 
	 * @param N
	 *            /A
	 * 
	 * @return null
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		mThreadCanvas.onKeyDown(keyCode);
		return false;
	}


	/**
	 * ��������
	 * 
	 * @param N
	 *            /A
	 * 
	 * @return null
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		mThreadCanvas.onKeyUp(keyCode);
		return false;
	}
}
