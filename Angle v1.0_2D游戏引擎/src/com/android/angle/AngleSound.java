package com.android.angle;

import android.content.Context;

/** 
* @author Ivan Pajuelo
*/
public class AngleSound extends AngleAudioObject
{
	private static final int sDefaultSimultaneousSounds = 3;

	public AngleSound (Context activity)
	{
		super(activity,sDefaultSimultaneousSounds);
	}
	
	public AngleSound(AngleActivity activity, int resId)
	{
		super(activity,sDefaultSimultaneousSounds);
		load(resId);
	}

	public AngleSound(AngleActivity activity, int resId, int simultaneousSounds)
	{
		super(activity,simultaneousSounds);
		load(resId);
	}

}
