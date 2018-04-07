package com.android.angle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Have the texture and parameters to draw characters
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleFont
{
	private static final short DEFAULT_FONT_CHARS = 93;
	protected AngleTexture mTexture;
	protected float mFontSize;
	protected Typeface mTypeface;
	protected int mBorder;
	protected int mAlpha;
	protected int mRed;
	protected int mGreen;
	protected int mBlue;

	protected int[] mCodePoints; // Unicode 'chars'
	protected short[] mCharX;
	protected short[] mCharLeft;
	protected short[] mCharTop;
	protected short[] mCharRight;
	protected short mCharCount;
	protected short mHeight;
	protected short mSpace;
	protected short mSpaceWidth;
	protected short mLineat;
	private AngleTextureEngine mTextureEngine;

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param fontSize	Size of the font
	 * @param typeface	Typeface
	 * @param space		Space between characters
	 * @param red			Color of the font (Red)
	 * @param green		Color of the font (Green)
	 * @param blue			Color of the font (Blue)
	 * @param alpha		Color of the font (Alpha channel)
	 */
	public AngleFont(AngleSurfaceView view, float fontSize, Typeface typeface, int space, int red, int green, int blue, int alpha)
	{
		mTextureEngine=view.getTextureEngine();
		doInit(fontSize, typeface, DEFAULT_FONT_CHARS, (short) space, red, green, blue, alpha);
		for (int c = 0; c < mCharCount; c++)
			mCodePoints[c] = 33 + c;
		mBorder=1;
		mTexture=mTextureEngine.createTextureFromFont(this);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param fontSize	Size of the font
	 * @param typeface	Typeface
	 * @param charCount	Number of characters into font
	 * @param border		Border of every character
	 * @param space		Space between characters
	 * @param red			Color of the font (Red)
	 * @param green		Color of the font (Green)
	 * @param blue			Color of the font (Blue)
	 * @param alpha		Color of the font (Alpha channel)
	 */
	public AngleFont(AngleSurfaceView view, float fontSize, Typeface typeface, int charCount, int border, int space, int red, int green,
			int blue, int alpha)
	{
		mTextureEngine=view.getTextureEngine();
		doInit(fontSize, typeface, (short) charCount, (short) space, red, green, blue, alpha);
		for (int c = 0; c < mCharCount; c++)
			mCodePoints[c] = 33 + c;
		mBorder=border;
		mTexture=mTextureEngine.createTextureFromFont(this);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param fontSize	Size of the font
	 * @param typeface	Typeface
	 * @param chars		String with characters into font 
	 * @param border		Border of every character
	 * @param space		Space between characters
	 * @param red			Color of the font (Red)
	 * @param green		Color of the font (Green)
	 * @param blue			Color of the font (Blue)
	 * @param alpha		Color of the font (Alpha channel)
	 */
	public AngleFont(AngleSurfaceView view, float fontSize, Typeface typeface, char[] chars, int border, int space, int red, int green,
			int blue, int alpha)
	{
		mTextureEngine=view.getTextureEngine();
		doInit(fontSize, typeface, (short) chars.length, (short) space, red, green, blue, alpha);
		for (int c = 0; c < chars.length; c++)
			mCodePoints[c] = (int) chars[c];
		mBorder=border;
		mTexture=mTextureEngine.createTextureFromFont(this);
	}

	/**
	 * 
	 * @param view			Main AngleSurfaceView
	 * @param asset		FNT file (to load font from) 
	 * @param resourceId PNG file (to load font from)
	 * @param space		Space between characters
	 */
	public AngleFont(AngleSurfaceView view, String asset, int resourceId, int space)
	{
		mTextureEngine=view.getTextureEngine();
		loadFrom(asset);
		mTexture=mTextureEngine.createTextureFromResourceId(resourceId);
		mSpace = (short) space;
	}

	private void doInit(short charCount)
	{
		mCharCount = charCount;
		mTexture=null;
		mCodePoints = new int[mCharCount];
		mCharX = new short[mCharCount];
		mCharLeft = new short[mCharCount];
		mCharTop = new short[mCharCount];
		mCharRight = new short[mCharCount];
		mHeight = 0;
	}

	private void doInit(float fontSize, Typeface typeface, short charCount, short space, int red, int green, int blue, int alpha)
	{
		doInit(charCount);
		mSpace = space;
		mFontSize = fontSize;
		mTypeface = typeface;
		mAlpha = alpha;
		mRed = red;
		mGreen = green;
		mBlue = blue;
	}

	/**
	 * Save texture to the root of the SD card using 2 files. 1 PNG with graphics and 1 FNT with data.
	 * Edit the PNG and use it as a drawable resource and put the FNT into asset folder to reload edited font.  
	 * @param fileName
	 */
	public void saveTo(String fileName)
	{
		Bitmap bitmap=mTexture.create();
		if (bitmap != null)
		{
			try
			{
				FileOutputStream stream = new FileOutputStream("/sdcard/" + fileName + ".png");
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				stream.flush();
				stream.close();
				ByteBuffer out = ByteBuffer.allocate(10 + mCharCount * 12);
				out.putShort(mCharCount);
				out.putShort(mHeight);
				out.putShort(mSpace);
				out.putShort(mSpaceWidth);
				out.putShort(mLineat);
				for (int c = 0; c < mCharCount; c++)
				{
					out.putInt(mCodePoints[c]);
					out.putShort(mCharX[c]);
					out.putShort(mCharLeft[c]);
					out.putShort(mCharTop[c]);
					out.putShort(mCharRight[c]);
				}
				stream = new FileOutputStream("/sdcard/" + fileName + ".fnt");
				stream.write(out.array());
				stream.flush();
				stream.close();
				out = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			bitmap.recycle();
		}
	}

	private void loadFrom(String asset)
	{
		InputStream is=null;
		try
		{
			is = AngleSurfaceView.mContext.getAssets().open(asset);
			ByteBuffer in = ByteBuffer.allocate(10);
			is.read(in.array());
			doInit(in.getShort(0));
			mHeight = in.getShort(2);
			mSpace = in.getShort(4);
			mSpaceWidth = in.getShort(6);
			mLineat = in.getShort(8);
			in = ByteBuffer.allocate(mCharCount * 12);
			is.read(in.array());
			for (int c = 0; c < mCharCount; c++)
			{
				mCodePoints[c] = in.getInt(c * 12);
				mCharX[c] = in.getShort(c * 12 + 4);
				mCharLeft[c] = in.getShort(c * 12 + 6);
				mCharTop[c] = in.getShort(c * 12 + 8);
				mCharRight[c] = in.getShort(c * 12 + 10);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				Log.e("AngleFont", "loadFrom::InputStream.close error: " + e.getMessage());
			}
		}
	}

	protected int getChar(char chr)
	{
		for (int c = 0; c < mCharCount; c++)
		{
			if (mCodePoints[c] == chr)
				return c;
		}
		return -1;
	}

	/**
	 * 
	 * @param c Index of the character in font character array
	 * @return The character width in pixels
	 */
	public int charWidth(char c)
	{
		int chr = getChar(c);
		if (chr == -1)
			return mSpaceWidth;
		else
			return mCharRight[chr] + mSpace;
	}
}
