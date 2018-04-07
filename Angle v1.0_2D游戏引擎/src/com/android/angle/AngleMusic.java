package com.android.angle;

/** 
* @author Ivan Pajuelo
*/
public class AngleMusic extends AngleAudioObject
{
	public AngleMusic(AngleActivity activity)
	{
		super(activity,1);
	}

	public AngleMusic(AngleActivity activity, int resId)
	{
		super(activity,1);
		load(resId);
	}

}
