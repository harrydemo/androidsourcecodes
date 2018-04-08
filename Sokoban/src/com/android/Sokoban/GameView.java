package com.android.Sokoban;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
public abstract class GameView extends View
{
	public GameView(Context context)
	{
		super(context);
	}
	protected abstract void onScreenConfigrationChange(int orientation);
	protected abstract void onOSDLanguaChange(int newlanguage);
	protected abstract void onDraw(Canvas canvas);
	public abstract boolean onTouchEvent(MotionEvent event);
	public abstract boolean onKeyDown(int keyCode);
	public abstract boolean onKeyUp(int keyCode);
	protected abstract void reCycle();
	protected abstract void refurbish();
}

