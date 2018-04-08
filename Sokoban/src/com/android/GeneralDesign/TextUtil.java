package com.android.GeneralDesign;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.view.KeyEvent;

public class TextUtil
{
	int mX;
	int mY;
	int mWidth;
	int mHeight;
	int mFontHeight;
	int mPageLineNum;
	int mBGColor;
	int mTextColor;
	int mAlpha;
	int mRealLine;
	int mCurLine;
	String mStrText;
	Vector<String>	mStrings;
	Paint mPaint;
	int mTextSize;
	public TextUtil()
	{
		mPaint = new Paint();
		mStrings = new Vector<String>();
	}
	public void setAlpha(int a)
	{
		mAlpha = a;
		mPaint.setAlpha(a);
	}
	public void setTextSize(int iTextSize)
	{
		mTextSize = iTextSize;
		mPaint.setTextSize(mTextSize);
	}
	public void setRect(int x, int y, int width, int height)
	{
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
	}
	public void setBGColor(int bgColor)
	{
		mBGColor = bgColor;
	}
	public void setTextColor(int txetColor)
	{
		mTextColor = txetColor;
		mPaint.setARGB(mAlpha,
				Color.red(mTextColor),
				Color.green(mTextColor),
				Color.blue(mTextColor));
	}
	public void setText(String strText)
	{
		mStrText = strText;
	}
	public void updateTextIfon()
	{
		char ch;
		int w = 0;
		int istart = 0;
		
		if(null == mStrText)
			return;
		
		mCurLine = 0;
		mPageLineNum = 0;
		mRealLine = 0;
		mStrings.clear();
		
		FontMetrics fm = mPaint.getFontMetrics();
		mFontHeight = (int) Math.ceil(fm.descent - fm.top) + 2;
		mPageLineNum = (mHeight - mTextSize) / mFontHeight;

		for (int i = 0; i < mStrText.length(); i++)
		{
			ch = mStrText.charAt(i);
			float[] widths = new float[1];
			String str = String.valueOf(ch);
			mPaint.getTextWidths(str, widths);

			if (ch == '\n')
			{
				mRealLine ++;
				mStrings.addElement(mStrText.substring(istart, i));
				istart = i + 1;
				w = 0;
			}
			else
			{
				w += (int) (Math.ceil(widths[0]));
				if (w > mWidth)
				{
					mRealLine++;
					mStrings.addElement(mStrText.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				}
				else
				{
					if (i == (mStrText.length() - 1))
					{
						mRealLine++;
						mStrings.addElement(mStrText.substring(istart, mStrText.length()));
					}
				}
			}
		}
	}
	public void onDraw(Canvas canvas)
	{
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		for (int i = mCurLine, j = 0; i < mRealLine; i++, j++)
		{
			if (j > mPageLineNum)
			{
				break;
			}
			canvas.drawText(
					(String)(mStrings.elementAt(i)),
					mX,
					mY + mFontHeight * j + mPaint.getTextSize() + 3,
					mPaint);
		}
	}
	public boolean onKeyDown(int keyCode)
	{
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			if (mCurLine > 0)
			{
				mCurLine --;
				return true;
			}
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			if ((mCurLine + mPageLineNum) < (mRealLine - 1))
			{
				mCurLine++;
				return true;
			}
		}
		return false;
	}
	
}
