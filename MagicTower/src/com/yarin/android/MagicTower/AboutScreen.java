package com.yarin.android.MagicTower;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AboutScreen extends GameView
{
	private MainGame	mMainGame	= null;
	private Paint		paint		= null;
	private TextUtil	tu			= null;


	public AboutScreen(Context context, MainGame mainGame)
	{
		super(context);
		mMainGame = mainGame;
		paint = new Paint();
		tu = new TextUtil();
		Resources r = this.getContext().getResources();
		tu.InitText(((String) r.getString(R.string.about)), 0, 0, yarin.SCREENW, yarin.SCREENH, 0x0, 0xff0000, 255, yarin.TextSize);
	}


	protected void onDraw(Canvas canvas)
	{
		paint.setColor(Color.BLACK);
		yarin.fillRect(canvas, 0, 0, yarin.SCREENW, yarin.SCREENH, paint);
		tu.SetTextColor(0xffff00);
		tu.DrawText(canvas);
	}


	public boolean onKeyUp(int keyCode)
	{
		if (keyCode == yarin.KEY_DPAD_UP || keyCode == yarin.KEY_DPAD_DOWN)
		{
			tu.Key(keyCode);
		}
		else
		{
			mMainGame.controlView(yarin.GAME_MENU);
		}
		return true;
	}


	public boolean onKeyDown(int keyCode)
	{
		return true;
	}


	public void refurbish()
	{

	}


	public void reCycle()
	{
		paint = null;
		tu = null;
		System.gc();
	}
}
