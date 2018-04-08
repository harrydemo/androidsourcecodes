package com.android.angle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;

/**
 * Texture created using font characters
 * @author Ivan Pajuelo
 *
 */
public class AngleFontTexture extends AngleTexture
{
	AngleFont mFont;
	
	AngleFontTexture (AngleTextureEngine textureEngine, AngleFont font)
	{
		super(textureEngine);
		mFont=font;
	}

	@Override
	public Bitmap create()
	{
		Bitmap mBitmap = null;
		Paint paint = new Paint();
		paint.setTypeface(mFont.mTypeface);
		paint.setTextSize(mFont.mFontSize);
		paint.setARGB(mFont.mAlpha, mFont.mRed, mFont.mGreen, mFont.mBlue);
		paint.setAntiAlias(true);

		Rect rect = new Rect();
		int totalWidth = 0;
		mFont.mHeight = 0;
		int minTop = 1000;
		int maxBottom = -1000;
		for (int c = 0; c < mFont.mCharCount; c++)
		{
			paint.getTextBounds(new String(mFont.mCodePoints, c, 1), 0, 1, rect);
			mFont.mCharLeft[c] = (short) rect.left;
			mFont.mCharRight[c] = (short) (rect.right + mFont.mBorder);
			totalWidth += mFont.mCharRight[c] - mFont.mCharLeft[c];
			if (rect.top < minTop)
				minTop = rect.top;
			if (rect.bottom > maxBottom)
				maxBottom = rect.bottom;
		}
		mFont.mHeight = (short) ((maxBottom - minTop) + mFont.mBorder);
		mFont.mLineat = (short) (minTop - mFont.mBorder/2);
		int area = mFont.mHeight * totalWidth;
		int mTextSizeX = 0;
		while ((area > ((1 << mTextSizeX) * (1 << mTextSizeX))) && (mTextSizeX < 11))
			mTextSizeX++;
		if (mTextSizeX < 11)
		{
			short x = 0;
			short y = 0;
			for (int c = 0; c < mFont.mCharCount; c++)
			{
				if (x + (mFont.mCharRight[c] - mFont.mCharLeft[c]) > (1 << mTextSizeX))
				{
					x = 0;
					y += mFont.mHeight;
				}
				if (y + mFont.mHeight > (1 << mTextSizeX))
				{
					if (mTextSizeX < 11)
					{
						mTextSizeX++;
						x = 0;
						y = 0;
						c = -1;
						continue;
					}
					else
						break;
				}
				mFont.mCharX[c] = x;
				mFont.mCharTop[c] = y;
				x += (mFont.mCharRight[c] - mFont.mCharLeft[c]);
			}
			paint.getTextBounds(" ", 0, 1, rect);
			mFont.mSpaceWidth = (short) (rect.right - rect.left + mFont.mBorder);
			if (mFont.mSpaceWidth==0)
			{
				paint.getTextBounds("x", 0, 1, rect);
				mFont.mSpaceWidth = (short) (rect.right - rect.left + mFont.mBorder);
			}
		}
		if (mTextSizeX < 11)
		{
			int mTextSizeY = 0;
			while ((mFont.mCharTop[mFont.mCharCount - 1] + mFont.mHeight) > (1 << mTextSizeY))
				mTextSizeY++;
			Bitmap paintBitmap = Bitmap.createBitmap((1 << mTextSizeX), (1 << mTextSizeY), Config.ARGB_8888);

			Canvas canvas = new Canvas(paintBitmap);

			for (int c = 0; c < mFont.mCharCount; c++)
			{
				canvas.drawText(new String(mFont.mCodePoints, c, 1), 0, 1, mFont.mCharX[c] - mFont.mCharLeft[c] + (mFont.mBorder / 2), mFont.mCharTop[c] - minTop
						+ (mFont.mBorder / 2), paint);
			}
			mBitmap=Bitmap.createBitmap(paintBitmap, 0, 0, paintBitmap.getWidth(), paintBitmap.getHeight());
			paintBitmap.recycle();
		}
		return mBitmap;
	}

}
