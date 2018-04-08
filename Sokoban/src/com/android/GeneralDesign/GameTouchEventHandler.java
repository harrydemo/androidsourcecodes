package com.android.GeneralDesign;

import android.graphics.Rect;
import android.view.MotionEvent;

public abstract class GameTouchEventHandler
{
	private static class SokobanActionType
	{
		public static final int NOP = 			0;
		public static final int CLICK_DOWN = 	1;
		public static final int MOVE_ON =		2;
		public static final int CLICK_UP = 		3;
	}
	private int mActionRecorder = SokobanActionType.NOP;
	private int mPointX1, mPointY1;
	private int mPointX2, mPointY2;
	private int mLeft, mTop, mRight, mBottom;
	public GameTouchEventHandler()
	{
		mLeft = 0;
		mTop = 0;
		mRight = Integer.MAX_VALUE;
		mBottom = Integer.MAX_VALUE;
	}
	public GameTouchEventHandler(Rect rect)
	{
		setRect(rect);
	}
	public void setRect(Rect rect)
	{
		mLeft = rect.left;
		mTop = rect.top;
		mRight = rect.right;
		mBottom = rect.bottom;
	}
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean retVal = false;
		int x = (int)event.getX();
		int y = (int)event.getY();
		if(x < mLeft ||
			x > mRight ||
			y < mTop ||
			y > mBottom)
		{
			mActionRecorder = SokobanActionType.NOP;
			return false;
		}

		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			mPointX1 = (int)event.getX();
			mPointY1 = (int)event.getY();
			mActionRecorder = SokobanActionType.CLICK_DOWN;
			break;
		case MotionEvent.ACTION_MOVE:
			mPointX2 = (int)event.getX();
			mPointY2 = (int)event.getY();
			mActionRecorder = SokobanActionType.MOVE_ON;
			retVal = onActionMoveOn(mPointX2, mPointY2);
			break;
		case MotionEvent.ACTION_UP:
			mPointX2 = (int)event.getX();
			mPointY2 = (int)event.getY();
			if(SokobanActionType.CLICK_DOWN == mActionRecorder)
			{
				retVal = onActionDone(mPointX2, mPointY2);
			}
			else if(SokobanActionType.MOVE_ON == mActionRecorder)
			{
				int dx = mPointX2 - mPointX1;
				int dy = mPointY2 - mPointY1;
				if(Math.abs(dx) < 10 &&
					Math.abs(dy)  < 10)
				{
					retVal = onActionDone(mPointX2, mPointY2);
				}
				else
				{					
					if(dx < 0 && Math.abs(dy) < -dx)
						retVal = onActionMoveLeft();
					else if(dx > 0 && Math.abs(dy) < dx)
						retVal = onActionMoveRight();
					else if(dy < 0)
						retVal = onActionMoveUp();
					else
						retVal = onActionMoveDown();
				}
			}
			else
			{
				mActionRecorder = SokobanActionType.NOP;
			}
			mActionRecorder = SokobanActionType.NOP;
			break;
		default:
			mActionRecorder = SokobanActionType.NOP;
			break;
		}
		return retVal;
	}
	public abstract boolean onActionDone(int x, int y);
	public abstract boolean onActionMoveOn(int x, int y);
	public abstract boolean onActionMoveLeft();
	public abstract boolean onActionMoveRight();
	public abstract boolean onActionMoveUp();
	public abstract boolean onActionMoveDown();
}
