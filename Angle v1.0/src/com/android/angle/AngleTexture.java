package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Base texture
 * 
 * @author Ivan Pajuelo
 * 
 */
public abstract class AngleTexture
{
	public int mRefernces = 0;
	public int mHWTextureID = -1;
	public int mWidth = 0;
	public int mHeight = 0;
	private AngleTextureEngine mTextureEngine;
	
	public AngleTexture(AngleTextureEngine textureEngine)
	{
		mTextureEngine=textureEngine;
	}
	
	public void linkToGL (GL10 gl)
	{
		mHWTextureID=mTextureEngine.generateTexture();
		load(gl);
	}

	public void load(GL10 gl)
	{
		if ((gl!=null)&&(mHWTextureID>-1))
		{
			gl.glBindTexture(GL10.GL_TEXTURE_2D, mHWTextureID);
			int error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "load Bind GLError: " + error);
	
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "load Parameterf GLError: " + error);
	
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
			
			error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "load Envf GLError: " + error);
	
	
			Bitmap bitmap = create();
	
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			error = gl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "load Image2D GLError: " + error);
	
			mWidth = bitmap.getWidth();
			mHeight = bitmap.getHeight();
	
			bitmap.recycle();
		}
	}

	public abstract Bitmap create();
};
