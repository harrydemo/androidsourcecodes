package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Fastest sprite with no rotation support
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSprite extends AngleAbstractSprite
{
	protected int[] mTextureIV; // Texture coordinates

	/**
	 * 
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(AngleSpriteLayout layout)
	{
		super(layout);
		doInit(0, 0, 1);
	}

	/**
	 * 
	 * @param x Position
	 * @param y Position
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(int x, int y, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x, y, 1);
	}
	
	/**
	 * 
	 * @param x Position
	 * @param y Position
	 * @param alpha Normalized alpha channel value
	 * @param layout AngleSpriteLayout
	 */
	public AngleSprite(int x, int y, float alpha, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x, y, alpha);
	}

	private void doInit(int x, int y, float alpha)
	{
		mTextureIV = new int[4];
		setLayout(roLayout);
		mPosition.set(x,y);
		mAlpha=alpha;
	}

	@Override
	public void setLayout(AngleSpriteLayout layout)
	{
		super.setLayout(layout);
		if (roLayout != null)
		{
			mTextureIV[2] = roLayout.roCropWidth; // Wcr
			mTextureIV[3] = -roLayout.roCropHeight; // Hcr
			setFrame(0);
		}
	}

	@Override
	public void setFrame(int frame)
	{
		if (roLayout != null)
		{
			if (frame < roLayout.roFrameCount)
			{
				roFrame = frame;
				if (mFlipHorizontal)
				{
					mTextureIV[0] = (roLayout.roCropLeft + roLayout.roCropWidth) + ((roFrame % roLayout.mFrameColumns) * roLayout.roCropWidth);// Ucr
					mTextureIV[2] = -roLayout.roCropWidth; // Wcr
				}
				else
				{
					mTextureIV[0] = roLayout.roCropLeft + ((roFrame % roLayout.mFrameColumns) * roLayout.roCropWidth);// Ucr
					mTextureIV[2] = roLayout.roCropWidth; // Wcr
				}

				if (mFlipVertical)
				{
					mTextureIV[1] = roLayout.roCropTop + ((roFrame / roLayout.mFrameColumns) * roLayout.roCropHeight);// Vcr
					mTextureIV[3] = roLayout.roCropHeight; // Hcr
				}
				else
				{
					mTextureIV[1] = (roLayout.roCropTop + roLayout.roCropHeight) + ((roFrame / roLayout.mFrameColumns) * roLayout.roCropHeight);// Vcr
					mTextureIV[3] = -roLayout.roCropHeight; // Hcr
				}
			}
		}
	}

	@Override
	public void draw(GL10 gl)
	{
		if (roLayout != null)
		{
			if (roLayout.roTexture != null)
			{
				if (roLayout.roTexture.mHWTextureID > -1)
				{
					gl.glBindTexture(GL10.GL_TEXTURE_2D, roLayout.roTexture.mHWTextureID);
					gl.glColor4f(mRed, mGreen, mBlue, mAlpha);

					((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);

					((GL11Ext) gl).glDrawTexfOES(mPosition.mX - (roLayout.getPivot(roFrame).mX * mScale.mX), AngleSurfaceView.roHeight
							- ((mPosition.mY - (roLayout.getPivot(roFrame).mY * mScale.mY)) + (roLayout.roHeight * mScale.mY)), 0, roLayout.roWidth
							* mScale.mX, roLayout.roHeight * mScale.mY);
				}
				else
					roLayout.roTexture.linkToGL(gl);
			}
		}
		super.draw(gl);
	}
}
