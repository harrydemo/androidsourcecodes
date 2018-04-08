package com.android.angle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Sprite with rotating capabilities. Uses hardware buffers if available
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleRotatingSprite extends AngleAbstractSprite
{

	public float mRotation;
	protected float[] mTexCoordValues;
	protected int mTextureCoordBufferIndex;
	public float[] mVertexValues;
	public int mVertBufferIndex;
	private boolean isFrameInvalid;

	/**
	 * 
	 * @param layout AngleSpriteLayout
	 */
	public AngleRotatingSprite(AngleSpriteLayout layout)
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
	public AngleRotatingSprite(int x, int y, AngleSpriteLayout layout)
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
	public AngleRotatingSprite(int x, int y, float alpha, AngleSpriteLayout layout)
	{
		super(layout);
		doInit(x, y, alpha);
	}

	private void doInit(int x, int y, float alpha)
	{
		mRotation = 0;
		mTexCoordValues = new float[8];
		mTextureCoordBufferIndex = -1;
		mVertexValues = new float[8];
		mVertBufferIndex = -1;
		setLayout(roLayout);
		mPosition.set(x,y);
		mAlpha=alpha;
		isFrameInvalid=true;
	}

	@Override
	public void setLayout(AngleSpriteLayout layout)
	{
		super.setLayout(layout);
		setFrame(0);
	}

	@Override
	public void invalidateTexture(GL10 gl)
	{
		setFrame(roFrame);
		super.invalidateTexture(gl);
	}

	@Override
	public void setFrame(int frame)
	{
		if (roLayout != null)
		{
			if (frame < roLayout.roFrameCount)
			{
				roFrame = frame;
				float W = roLayout.roTexture.mWidth;
				float H = roLayout.roTexture.mHeight;
				if ((W>0)&(H>0))
				{
				float frameLeft = (roFrame % roLayout.mFrameColumns) * roLayout.roCropWidth;
				float frameTop = (roFrame / roLayout.mFrameColumns) * roLayout.roCropHeight;

				float left = (roLayout.roCropLeft + frameLeft) / W;
				float bottom = (roLayout.roCropTop + roLayout.roCropHeight + frameTop) / H;
				float right = (roLayout.roCropLeft + roLayout.roCropWidth + frameLeft) / W;
				float top = (roLayout.roCropTop + frameTop) / H;

				if (mFlipHorizontal)
				{
					mTexCoordValues[0] = right;
					mTexCoordValues[2] = left;
					mTexCoordValues[4] = right;
					mTexCoordValues[6] = left;
				}
				else
				{
					mTexCoordValues[0] = left;
					mTexCoordValues[2] = right;
					mTexCoordValues[4] = left;
					mTexCoordValues[6] = right;
				}
				if (mFlipVertical)
				{
					mTexCoordValues[1] = top;
					mTexCoordValues[3] = top;
					mTexCoordValues[5] = bottom;
					mTexCoordValues[7] = bottom;
				}
				else
				{
					mTexCoordValues[1] = bottom;
					mTexCoordValues[3] = bottom;
					mTexCoordValues[5] = top;
					mTexCoordValues[7] = top;
				}

				roLayout.fillVertexValues(roFrame, mVertexValues);
				isFrameInvalid=false;
				}
				mTextureCoordBufferIndex=-1;
				mVertBufferIndex=-1;
			}
		}
	}

	@Override
	public void invalidateHardwareBuffers(GL10 gl)
	{
		int[] hwBuffers = new int[2];
		((GL11) gl).glGenBuffers(2, hwBuffers, 0);

		// Allocate and fill the texture buffer.
		mTextureCoordBufferIndex = hwBuffers[0];
		((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
		((GL11) gl).glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4, FloatBuffer.wrap(mTexCoordValues), GL11.GL_STATIC_DRAW);
		mVertBufferIndex = hwBuffers[1];
		((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
		((GL11) gl).glBufferData(GL11.GL_ARRAY_BUFFER, 8 * 4, FloatBuffer.wrap(mVertexValues), GL11.GL_STATIC_DRAW);
		((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

		super.invalidateHardwareBuffers(gl);

	}

	@Override
	public void releaseHardwareBuffers(GL10 gl)
	{
		int[] hwBuffers = new int[2];
		hwBuffers[0] = mTextureCoordBufferIndex;
		hwBuffers[1] = mVertBufferIndex;
		if (gl!=null)
			((GL11) gl).glDeleteBuffers(2, hwBuffers, 0);
		mTextureCoordBufferIndex = -1;
		mVertBufferIndex = -1;
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
					if (isFrameInvalid)
						setFrame(roFrame);

					gl.glPushMatrix();
					gl.glLoadIdentity();

					gl.glTranslatef(mPosition.mX, mPosition.mY, 0);
					if (mRotation != 0)
						gl.glRotatef(-mRotation, 0, 0, 1);
					if ((mScale.mX != 1) || (mScale.mY != 1))
						gl.glScalef(mScale.mX, mScale.mY, 1);

					gl.glBindTexture(GL10.GL_TEXTURE_2D, roLayout.roTexture.mHWTextureID);
					gl.glColor4f(mRed, mGreen, mBlue, mAlpha);

					if (AngleSurfaceView.sUseHWBuffers)
					{
						if ((mTextureCoordBufferIndex < 0)||(mVertBufferIndex < 0))
							invalidateHardwareBuffers(gl);

						((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
						((GL11) gl).glVertexPointer(2, GL10.GL_FLOAT, 0, 0);

						((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
						((GL11) gl).glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);

						((GL11) gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, AngleSurfaceView.roIndexBufferIndex);
						((GL11) gl).glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, 0);

						((GL11) gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
						((GL11) gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
					}
					else
					{
						CharBuffer mIndexBuffer;
						FloatBuffer mVertexBuffer;
						FloatBuffer mTexCoordBuffer;

						mIndexBuffer = ByteBuffer.allocateDirect(AngleSurfaceView.sIndexValues.length * 2).order(ByteOrder.nativeOrder())
								.asCharBuffer();
						mVertexBuffer = ByteBuffer.allocateDirect(mVertexValues.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
						mTexCoordBuffer = ByteBuffer.allocateDirect(mTexCoordValues.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

						for (int i = 0; i < AngleSurfaceView.sIndexValues.length; ++i)
							mIndexBuffer.put(i, AngleSurfaceView.sIndexValues[i]);
						for (int i = 0; i < mVertexValues.length; ++i)
							mVertexBuffer.put(i, mVertexValues[i]);
						for (int i = 0; i < mTexCoordValues.length; ++i)
							mTexCoordBuffer.put(i, mTexCoordValues[i]);

						gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertexBuffer);
						gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
						gl.glDrawElements(GL10.GL_TRIANGLES, AngleSurfaceView.sIndexValues.length, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);

					}

					gl.glPopMatrix();
				}
				else
					roLayout.roTexture.linkToGL(gl);
			}
		}
		super.draw(gl);
	}

}
