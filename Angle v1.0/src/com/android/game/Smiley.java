package com.android.game;


/** 
* @author Ivan Pajuelo
*/
public class Smiley extends Scrollable
{
	private static final int aJumping = 0;
	private static final float[] sFrameCount = {8};
	private static final int sDeadZone = 40;
	private int mAnimation;
	private float aFrame;
	private float[] mFH={0,-2,5,16,16,13,6,0};
	private GameUI mGame;

	public Smiley(GameUI game)
	{
		super(game.mField.mGround, game.slSmiley);
		mGame=game;
		mAnimation=aJumping;
		mFieldX=(float) (sDeadZone+Math.random()*(512-sDeadZone*2));
		place();
	}

	@Override
	public void step(float secondsElapsed)
	{
		animate(secondsElapsed);
		if (mFieldZ<320)
		{
			mFieldZ+=secondsElapsed*10;
			place();
		}
		else
			die(true);
	}

	private void animate(float secondsElapsed)
	{
		switch (mAnimation)
		{
			case aJumping:
				aFrame+=secondsElapsed*24;
				if (aFrame>sFrameCount[mAnimation])
					aFrame-=sFrameCount[mAnimation];
				if (((int)aFrame)!=roFrame)
					setFrame((int)aFrame);
				break;
		}
	}

	public void shotAt(float sX, float sZ)
	{
		float dX=mFieldX-sX;
		float dZ=(mFieldZ-mFH[roFrame]*mScale.mY)-sZ;
		float dist=(dX*dX+dZ*dZ);
		if (dist<(13*mScale.mX*13*mScale.mY))
			die(false);
	}

	private void die(boolean damage)
	{
		if (damage)
			mGame.updateLifes(-1);
		else
			mGame.updateScore((int) (320-mFieldZ));
		mDie=true;
	}

}
