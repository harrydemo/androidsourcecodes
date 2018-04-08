package com.yarin.android.MagicTower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SplashScreen extends GameView
{
	private int			tick		= 0;
	private Paint		paint		= null;
	private MainGame	mMainGame	= null;


	public SplashScreen(Context context, MainGame mainGame)
	{
		super(context);
		paint = new Paint();
		mMainGame = mainGame;
		paint.setTextSize(yarin.TextSize);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}


	protected void onDraw(Canvas canvas)
	{
		tick++;
		paint.setColor(Color.BLACK);
		yarin.fillRect(canvas, 0, 0, yarin.SCREENW, yarin.SCREENH, paint);

		paint.setColor(Color.WHITE);
		String string = "是否开启音效？";
		yarin.drawString(canvas, string, (yarin.SCREENW - paint.measureText(string)) / 2, (yarin.SCREENH - paint.getTextSize()) / 2, paint);
		string = "是";
		yarin.drawString(canvas, string, 5, yarin.SCREENH - paint.getTextSize() - 5, paint);
		string = "否";
		yarin.drawString(canvas, string, yarin.SCREENW - paint.measureText(string) - 5, yarin.SCREENH - paint.getTextSize() - 5, paint);
	}


	public boolean onKeyUp(int keyCode)
	{
		if (keyCode == yarin.KEY_DPAD_OK)
		{
			mMainGame.mbMusic = 1;
		}
		mMainGame.controlView(yarin.GAME_MENU);
		if (mMainGame.mbMusic == 1)
		{
			mMainGame.mCMIDIPlayer.PlayMusic(1);
		}
		return true;
	}


	public boolean onKeyDown(int keyCode)
	{
		return true;
	}


	public void refurbish()
	{
		// if (tick > 10)
		// {
		// mMainGame.controlView(yarin.GAME_MENU);
		// }
	}


	public void reCycle()
	{
		paint = null;
		System.gc();
	}
}
