package com.android.GeneralDesign;

public abstract class GameTimer
{
	private int mTick;
	private int mStartTick;
	private int mPeriodTick;
		
	public GameTimer()
	{
		mStartTick = Integer.MAX_VALUE;
		reset();
	}
	public void reset()
	{
		mTick = -1;
	}
	public void start()
	{
		mTick = 0;
	}
	public void setTime(int start, int period)
	{
		mStartTick = start;
		mPeriodTick = period;
	}
	public abstract void onTimerActive();
	public void refurbish()
	{
		if(mTick < 0)
			return;
		
		if(mTick < mStartTick)
		{
			mTick ++;
			return;
		}
		
		if(mTick == mStartTick)
		{
			onTimerActive();
			if(mPeriodTick == 0)
				reset();
			else
				mTick ++;
			return;
		}

		if(mTick >= (mStartTick + mPeriodTick))
			mTick -= mPeriodTick;
		else
			mTick ++;
	}
}
