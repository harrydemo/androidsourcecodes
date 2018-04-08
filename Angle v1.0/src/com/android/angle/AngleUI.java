package com.android.angle;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.android.angle.AngleObject;

/**
 * Base user interface
 * @author Ivan Pajuelo
 *
 */
public class AngleUI extends AngleObject
{
	protected AngleActivity mActivity;

	public AngleUI (AngleActivity activity)
	{
		mActivity=activity;
	}
	
	public boolean onTouchEvent(MotionEvent event) 
	{
		return false;
	}

	public boolean onTrackballEvent(MotionEvent event) 
	{ 
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{ 
		return false;
	}
	
	public void onPause() {}

	public void onResume() {}

	public void onActivate() {}

	public void onDeactivate() {}

}
