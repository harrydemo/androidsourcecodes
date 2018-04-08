package com.android.angle;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
* Audo Objects base class  
* @author Ivan Pajuelo
*/
public class AngleAudioObject extends AngleObject
{
	protected int mSimultaneousSounds;
	protected MediaPlayer[] mPlayer;
	protected int mCurrentSound;
	protected Context mActivity;
	private float mVolumeTo;
	private float mVolumeChangeSpeed;
	private float roVolume;
	private boolean isStopping;

	public AngleAudioObject(Context activity, int simultaneousSounds)
	{
		mActivity = activity;
		mSimultaneousSounds = simultaneousSounds;
		mPlayer = new MediaPlayer[mSimultaneousSounds];
		mCurrentSound = 0;
		isStopping=false;
	}

	public void delete()
	{
		stop();
		for (int s = 0; s < mSimultaneousSounds; s++)
		{
			if (mPlayer[s] != null)
			{
				mPlayer[s].release();
				mPlayer[s] = null;
			}
		}
	}

	public void seek(int pSeek)
	{
		for (int s = 0; s < mSimultaneousSounds; s++)
		{
			if (mPlayer[s] != null)
				mPlayer[s].seekTo(pSeek);
		}
	}
	public void pause()
	{
		for (int s = 0; s < mSimultaneousSounds; s++)
		{
			if (mPlayer[s] != null)
				if (mPlayer[s].isPlaying())
					mPlayer[s].pause();
		}
	}

	public void resume()
	{
		for (int s = 0; s < mSimultaneousSounds; s++)
		{
			if (mPlayer[s] != null)
				mPlayer[s].start();
		}
	}
	
	public void stop(float time)
	{
		isStopping=true;
		mVolumeTo = 0;
		if (time > 0)
			mVolumeChangeSpeed = Math.abs(roVolume - mVolumeTo) / time;
		else
			stop();
	}


	public void stop()
	{
		isStopping=false;
		for (int s = 0; s < mSimultaneousSounds; s++)
		{
			if (mPlayer[s] != null)
			{
				if (mPlayer[s].isPlaying())
					mPlayer[s].stop();
			}
		}
	}

	public void load(int resId)
	{
		if (resId > 0)
		{
			delete();
			for (int s = 0; s < mSimultaneousSounds; s++)
				mPlayer[s] = MediaPlayer.create(mActivity, resId);
		}
	}

	public void load(String fileName)
	{
		try
		{
			delete();
			
			for (int s = 0; s < mSimultaneousSounds; s++)
			{
				mPlayer[s] = new MediaPlayer();
				if (mPlayer[s] != null)
				{
					AssetFileDescriptor afd = mActivity.getAssets().openFd(fileName);
					mPlayer[s].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
					mPlayer[s].prepare();
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void play()
	{
		play(1, false);
	}

	public void play(float volume)
	{
		play(volume, false);
	}

	public void play(float volume, float time, boolean loop)
	{
		play(0, loop);
		setVolume(volume,time);
	}
	public void play(float volume, boolean loop)
	{
		if (mPlayer[mCurrentSound] != null)
		{
			roVolume=volume;
			mPlayer[mCurrentSound].setVolume(roVolume, roVolume);
			mPlayer[mCurrentSound].setLooping(loop);
			mPlayer[mCurrentSound].start();
		}
		mCurrentSound++;
		mCurrentSound %= mSimultaneousSounds;
	}

	public void setVolume(float volume, float time)
	{
		mVolumeTo = volume;
		if (time > 0)
			mVolumeChangeSpeed = Math.abs(roVolume - mVolumeTo) / time;
		else
			setVolume(volume);
	}

	private void setVolume(float volume)
	{
		roVolume = volume;
		if (roVolume > 1)
			roVolume = 1;
		if (roVolume < 0)
			roVolume = 0;
		for (int s = 0; s < mSimultaneousSounds; s++)
		{
			if (mPlayer[s] != null)
			{
				if (mPlayer[s].isPlaying())
				{
					if (roVolume > 0)
						mPlayer[s].setVolume(roVolume, roVolume);
					else
						stop();
				}
			}
		}
	}

	public boolean isPlaying()
	{
		for (int s = 0; s < mSimultaneousSounds; s++)
		{
			if (mPlayer[s] != null)
				if (mPlayer[s].isPlaying())
					return true;
		}
		return false;
	}

	@Override
	public void step(float secondsElapsed)
	{
		if (roVolume<mVolumeTo)
		{
			roVolume+=secondsElapsed*mVolumeChangeSpeed;
			if (roVolume>mVolumeTo)
				roVolume=mVolumeTo;
			setVolume(roVolume);
		}
		else if (roVolume>mVolumeTo)
		{
			roVolume-=secondsElapsed*mVolumeChangeSpeed;
			if (roVolume<mVolumeTo)
				roVolume=mVolumeTo;
			if (roVolume>0)
				setVolume(roVolume);
			else if(isStopping)
				stop();
		}
	}
	

}
