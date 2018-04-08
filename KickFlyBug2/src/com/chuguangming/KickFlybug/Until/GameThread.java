package com.chuguangming.KickFlybug.Until;

import com.chuguangming.R;
import com.chuguangming.KickFlybug.View.GameView;

import android.util.Log;

public class GameThread extends Thread
{

	//游戏主view
	GameView mGv;
	//设定线程轮询时间为100毫秒
	public int delay = 100;
	public GameThread(GameView gv)
	{
		mGv = gv;
	}

	@Override
	public void run()
	{
		while (!Thread.currentThread().isInterrupted() && mGv.gameLoop)
		{
			mGv.doPaint();
			mGv.doUpdate();
			try
			{
				sleep(delay);
			} catch (InterruptedException e)
			{
				mGv.gameLoop = false;
			}

		}
		Log.i(ActivityUtil.infoMessage, "GameThread Over");
	}
}
