package com.android.GameView;

import com.android.GeneralDesign.GameTimer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class HilightRectCursor
{
	private int mLeft;
	private int mRight;
	private int mTop;
	private int mBottom;
	private boolean mVisible;
	private boolean mAutoHide;
	private int mPeriod;
	private int mAlpha;
	
	private Paint mPaint = new Paint();
	
	public HilightRectCursor()
	{
		mAutoHide = true;
		mVisible = false;
	}
	GameTimer mHilightTimer = new GameTimer()
	{
		@Override
		public void onTimerActive()
		{
			if(false == mAutoHide || mVisible == false)
				return;
			int dAlpha = 0xff/mPeriod;
			if(mAlpha > dAlpha)
			{
				mAlpha -= dAlpha;
			}
			else if(mAlpha > 0)
			{
				mAlpha = 0;
			}
			else if(0 == mAlpha)
			{
				mVisible = false;
				hide();
				reset();
				onAutoHide();
			}
		}
	};

	public void onDraw(Canvas canvas)
	{
		if(false == mVisible)
			return;
		mPaint.setAlpha(mAlpha);
		canvas.drawRect(mLeft, mTop, mRight, mBottom, mPaint);
	}
	public void onAutoHide()
	{
	}
	public void setRect(int left, int top, int right, int bottom)
	{
		mLeft = left;
		mTop = top;
		mRight = right;
		mBottom = bottom;
	}
	public void setRect(Rect rect)
	{
		mLeft = rect.left;
		mTop = rect.top;
		mRight = rect.right;
		mBottom = rect.bottom;
	}
	public void move(int x, int y)
	{
		mRight = x + mRight - mLeft;
		mBottom = y + mBottom - mTop;
		mLeft = x;
		mTop = y;
	}
	public void setAutoHide(boolean autoHide)
	{
		mAutoHide = autoHide;
	}
	public void setTime(int tick)
	{
		mPeriod = tick;
	}
	public void show()
	{
		mVisible = true;
		mAlpha = 0xff;
		mPaint.setAlpha(0xff);
		mPaint.setColor(Color.GREEN);
		mHilightTimer.setTime(0, 1);
		mHilightTimer.start();
	}
	public void hide()
	{
		mVisible = false;
	}
	public void refurbish()
	{
		mHilightTimer.refurbish();
	}
}
