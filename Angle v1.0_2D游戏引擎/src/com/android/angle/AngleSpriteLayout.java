package com.android.angle;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Stores all the information about how to draw a sprite
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleSpriteLayout
{
	public int roWidth; //
	public int roHeight;// Dimensions (ReadOnly)

	public AngleTexture roTexture; //Texture (ReadOnly)
	public int roCropLeft;
	public int roCropTop;
	public int roCropWidth;
	public int roCropHeight; //Crop information (ReadOnly) 
	public int roFrameCount;
	protected int mFrameColumns;
	private AngleTextureEngine mTextureEngine;

	private AngleVector[] mPivot; //Pivot point
	
	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param width
	 *           Width in pixels
	 * @param height
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 * @param cropLeft
	 *           Most left pixel in texture
	 * @param cropTop
	 *           Most top pixel in texture
	 * @param cropWidth
	 *           Width of the cropping rectangle in texture
	 * @param cropHeight
	 *           Height of the cropping rectangle in texture
	 */
	public AngleSpriteLayout(AngleSurfaceView view, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight)
	{
		doInit(view, width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, 1, 1);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param width
	 *           Width in pixels
	 * @param height
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 * @param cropLeft
	 *           Most left pixel in texture
	 * @param cropTop
	 *           Most top pixel in texture
	 * @param cropWidth
	 *           Width of the cropping rectangle in texture
	 * @param cropHeight
	 *           Height of the cropping rectangle in texture
	 * @param frameCount
	 *           Number of frames in animation
	 * @param frameColumns
	 *           Number of frames horizontally in texture
	 */
	public AngleSpriteLayout(AngleSurfaceView view, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth,
			int cropHeight, int frameCount, int frameColumns)
	{
		doInit(view, width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, frameCount, frameColumns);
	}

	/**
	 * This constructor will get the bounds of resource dimensions
	 * @param view			Main AngleSurfaceView
	 * @param resourceId
	 *           Resource bitmap
	 */
	public AngleSpriteLayout(AngleSurfaceView view, int resourceId)
	{
		doInit(view, 0, 0, resourceId, 0, 0, 0, 0, 1, 1);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param width
	 *           Width in pixels
	 * @param height
	 *           Height in pixels
	 * @param resourceId
	 *           Resource bitmap
	 */
	public AngleSpriteLayout(AngleSurfaceView view, int width, int height, int resourceId)
	{
		doInit(view, width, height, resourceId, 0, 0, width, height, 1, 1);
	}

	private void doInit(AngleSurfaceView view, int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount,
			int frameColumns)
	{
		mTextureEngine=view.getTextureEngine();
		roTexture = mTextureEngine.createTextureFromResourceId(resourceId);
		roCropLeft = cropLeft;
		roCropTop = cropTop;
		if ((width==0)||(height==0))
		{
			InputStream is = view.getResources().openRawResource(resourceId);			
			try
			{
				Bitmap bitmap = BitmapFactory.decodeStream(is, null, new BitmapFactory.Options());
				roWidth = bitmap.getWidth();
				roHeight = bitmap.getHeight();
				roCropWidth = bitmap.getWidth();
				roCropHeight = bitmap.getHeight();
				bitmap.recycle();
			}
			finally
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					Log.e("AngleTextureEngine", "loadTexture::InputStream.close error: " + e.getMessage());
				}
			}
		}
		else
		{
			roWidth = width;
			roHeight = height;
			roCropWidth = cropWidth;
			roCropHeight = cropHeight;
		}
		roFrameCount = frameCount;
		mFrameColumns = frameColumns;

		mPivot=new AngleVector[roFrameCount];
		for (int f=0;f<roFrameCount;f++)
			mPivot[f]=new AngleVector(roWidth / 2, roHeight / 2);

	}

	/**
	 * Set pivot point of one frame
	 * @param frame
	 * @param x
	 * @param y
	 */
	public void setPivot(int frame, float x, float y)
	{
		if (frame<roFrameCount)
			mPivot[frame].set(x,y);
	}

	/**
	 * Set pivot point of all frames
	 * @param x
	 * @param y
	 */
	public void setPivot(float x, float y)
	{
		for (int f=0;f<roFrameCount;f++)
			mPivot[f].set(x,y);
	}

	/**
	 * get pivot point of one frame
	 * 
	 * @param frame
	 * @return pivot point 
	 */
	public AngleVector getPivot(int frame)
	{
		if (frame<roFrameCount)
			return mPivot[frame];
		return null;
	}

	public void fillVertexValues(int frame, float[] vertexValues)
	{
		if (frame<roFrameCount)
		{
			vertexValues[0] = -mPivot[frame].mX;
			vertexValues[1] = roHeight - mPivot[frame].mY;
			vertexValues[2] = roWidth - mPivot[frame].mX;
			vertexValues[3] = roHeight - mPivot[frame].mY;
			vertexValues[4] = -mPivot[frame].mX;
			vertexValues[5] = -mPivot[frame].mY;
			vertexValues[6] = roWidth - mPivot[frame].mX;
			vertexValues[7] = -mPivot[frame].mY;
		}
	}
	
	public void onDestroy(GL10 gl)
	{
		mTextureEngine.deleteTexture(roTexture);
	}
	
	/**
	 * Change the content of the texture
	 * @param resourceId Drawable
	 */
	public void changeTexture (int resourceId)
	{
		mTextureEngine.deleteTexture(roTexture);
		roTexture = mTextureEngine.createTextureFromResourceId(resourceId);
	}

	/**
	 * Change the entire layout
	 * @param width
	 * @param height
	 * @param resourceId
	 */
	public void changeLayout(int width, int height, int resourceId)
	{
		changeLayout(width, height, resourceId, 0, 0, width, height, 1, 1);
	}
	
	/**
	 * Change the entire layout
	 * @param width
	 * @param height
	 * @param resourceId
	 * @param cropLeft
	 * @param cropTop
	 * @param cropWidth
	 * @param cropHeight
	 */
	public void changeLayout(int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight)
	{
		changeLayout(width, height, resourceId, cropLeft, cropTop, cropWidth, cropHeight, 1, 1);
	}
	
	/**
	 * Change the entire layout
	 * @param width
	 * @param height
	 * @param resourceId
	 * @param cropLeft
	 * @param cropTop
	 * @param cropWidth
	 * @param cropHeight
	 * @param frameCount
	 * @param frameColumns
	 */
	public void changeLayout(int width, int height, int resourceId, int cropLeft, int cropTop, int cropWidth, int cropHeight, int frameCount, int frameColumns)
	{
		roWidth = width;
		roHeight = height;
		roCropLeft = cropLeft;
		roCropWidth = cropWidth;
		roCropTop = cropTop;
		roCropHeight = cropHeight;
		roFrameCount = frameCount;
		mFrameColumns = frameColumns;
		changeTexture (resourceId);
	}
}
