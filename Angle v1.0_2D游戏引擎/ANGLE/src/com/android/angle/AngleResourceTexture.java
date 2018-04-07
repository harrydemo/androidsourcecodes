package com.android.angle;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Creates a texture using drawable resource
 * @author Ivan Pajuelo
 *
 */
public class AngleResourceTexture extends AngleTexture
{
	private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	public int mResourceID;

	public AngleResourceTexture(AngleTextureEngine textureEngine, int resourceId)
	{
		super(textureEngine);
		mResourceID = resourceId;
	}

	public Bitmap create()
	{
		//Log.e("Texture", "HID:"+mHWTextureID+", RID:"+mResourceID);
		sBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		InputStream is = AngleSurfaceView.mContext.getResources().openRawResource(mResourceID);
		Bitmap bitmap;
		try
		{
			bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
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
		return bitmap;
	}
}
