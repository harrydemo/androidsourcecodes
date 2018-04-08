package com.android.angle;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Texture engine
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleTextureEngine
{
	private CopyOnWriteArrayList<AngleTexture> mTexturesX;

	private GL10 mGl;

	AngleTextureEngine()
	{
		mTexturesX = new CopyOnWriteArrayList<AngleTexture>();
	}

	public void destroy(GL10 gl)
	{
		mGl = gl;
		onContextLost();
	}

	public void onContextLost()
	{
		if (mGl != null)
		{
			int d = 0;
			int[] textures = new int[mTexturesX.size()];
			Iterator<AngleTexture> it = mTexturesX.iterator();
			while (it.hasNext())
				textures[d++] = it.next().mHWTextureID;

			mGl.glDeleteTextures(d, textures, 0);
			mTexturesX.clear();
		}
	}

	public void loadTextures(GL10 gl)
	{
		mGl = gl;
		if (mGl != null)
		{
			Iterator<AngleTexture> it = mTexturesX.iterator();
			while (it.hasNext())
				it.next().linkToGL(mGl);
		}
	}

	public AngleTexture createTextureFromFont(AngleFont font)
	{
		AngleTexture tex = null;
		Iterator<AngleTexture> it = mTexturesX.iterator();
		while (it.hasNext())
		{
			tex = it.next();
			if (tex instanceof AngleFontTexture)
			{
				// Texture already exists
				if (((AngleFontTexture) tex).mFont == font)
				{
					tex.mRefernces++;
					return tex;
				}
			}
		}

		tex = new AngleFontTexture(this, font);
		mTexturesX.add(tex);
		tex.linkToGL(mGl);
		return tex;
	}
	
	public AngleTexture createTextureFromResourceId(int resourceId)
	{
		AngleTexture tex = null;
		Iterator<AngleTexture> it = mTexturesX.iterator();
		while (it.hasNext())
		{
			tex = it.next();
			if (tex instanceof AngleResourceTexture)
			{
				// Texture already exists
				if (((AngleResourceTexture) tex).mResourceID == resourceId)
				{
					tex.mRefernces++;
					return tex;
				}
			}
		}

		tex = new AngleResourceTexture(this, resourceId);
		mTexturesX.add(tex);
		return tex;
	}

	public int generateTexture()
	{
		if (mGl != null)
		{
			int[] textureIDs = new int[1];

			mGl.glGenTextures(1, textureIDs, 0);

			int error = mGl.glGetError();
			if (error != GL10.GL_NO_ERROR)
				Log.e("AngleTexture", "generate GLError: " + error);
			else
				return textureIDs[0];
		}
		return -1;
	}

	public void deleteTexture(AngleTexture tex)
	{
		if (mTexturesX.indexOf(tex) > -1)
		{
			tex.mRefernces--;
			if (tex.mRefernces < 0)
				mTexturesX.remove(tex);
		}
		if (tex.mHWTextureID > -1)
		{
			int[] texture = new int[1];
			texture[0] = tex.mHWTextureID;
			mGl.glDeleteTextures(1, texture, 0);
		}
	}
}