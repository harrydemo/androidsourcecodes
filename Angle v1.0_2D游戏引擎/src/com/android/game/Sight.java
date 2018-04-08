package com.android.game;

import com.android.angle.AngleSprite;
import com.android.angle.AngleVector;

/** 
* @author Ivan Pajuelo
*/
public class Sight extends AngleSprite
{
	private boolean mAutofire;
	private GameUI mGame;
	private float mReloadTO;
	private int mWeapon;
	private boolean[] mOneShot={false};
	private float[] mReload={0.12f};
	private float mShotTO;

	public Sight(GameUI game)
	{
		super(game.slSight);
		mAlpha=0;
		mGame=game;
		mPosition.set(160,140);
	}

	public void moveTo(AngleVector mSight)
	{
		mPosition.set(160+mSight.mX*160,140+mSight.mY*140);
	}

	private void shot()
	{
		setFrame(1);
		mShotTO=0.04f;
		mGame.mField.shotAt(mPosition,mWeapon);
		mReloadTO=mReload[mWeapon];
	}

	public void fire(boolean isDown)
	{
		mAutofire=isDown;
		mAlpha=(isDown)?1:0;
		if ((!isDown)&&mOneShot[mWeapon])
		{
			if (mReloadTO<=0)
				shot();
		}
	}

	@Override
	public void step(float secondsElapsed)
	{
		mReloadTO-=secondsElapsed;
		if (mShotTO>0)
		{
			mShotTO-=secondsElapsed;
			if (mShotTO<0)
			{
				setFrame(0);
				mShotTO=0;
			}
		}
		if (mAutofire)
		{
			if (!mOneShot[mWeapon])
			{
				if (mReloadTO<=0)
					shot();
			}
		}
		super.step(secondsElapsed);
	}

}
