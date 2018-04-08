package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * Have the string and its position. Length is automatically set when string content is changed. But can be altered to create typing effect.
 * 
 * @author Ivan Pajuelo
 * 
 */
public class AngleString extends AngleObject
{
	public static final int aLeft = 0;
	public static final int aCenter = 1;
	public static final int aRight = 2;
	protected String mString;
	protected String mWantString;
	public int mLength; // Length to display
	protected AngleFont mFont; // Font
	protected int[] mTextureIV = new int[4]; // Texture coordinates
	public AngleVector mPosition; // Position
	public float mZ; // Z position (0=Near, 1=Far)
	public int mAlignment; // Text alignment
	public float mRed; // Red tint (0 - 1)
	public float mGreen; // Green tint (0 - 1)
	public float mBlue; // Blue tint (0 - 1)
	public float mAlpha; // Alpha channel (0 - 1)
	public int mDisplayWidth;
	public int mDisplayLines;
	protected boolean mIgnoreNL;
	protected int mTabLength;
	private int mLinesCount;
	private int[] mLineStart;
	private int[] mLineEnd;
	private int mWidth;
	private boolean mNewString;

	/**
	 * 
	 * @param font AngleFont
	 */
	public AngleString(AngleFont font)
	{
		init(font, 3, false);
	}

	/**
	 * 
	 * @param font AngleFont
	 * @param tabLength Length in spaces of \t 
	 * @param ignoreNL Ignore \n
	 */
	public AngleString(AngleFont font, int tabLength, boolean ignoreNL)
	{
		init(font, tabLength, ignoreNL);
	}

	/**
	 * 
	 * @param font AngleFont
	 * @param string The text
	 * @param x Position
	 * @param y Position
	 * @param alignment aLeft, aCenter or aRight
	 */
	public AngleString(AngleFont font, String string, int x, int y, int alignment)
	{
		init(font, 3, false);
		set(string);
		mPosition.set(x,y); 
		mAlignment = alignment;
	}

	private void init(AngleFont font, int tabLength, boolean ignoreNL)
	{
		mPosition = new AngleVector();
		mFont = font;
		mLength = 0;
		mLinesCount = 0;
		mAlignment = aLeft;
		mRed = 1;
		mGreen = 1;
		mBlue = 1;
		mAlpha = 1;
		mDisplayWidth = 0;
		mDisplayLines = 1;
		mTabLength = tabLength;
		mIgnoreNL = ignoreNL;
		mNewString=false;
	}

	/**
	 * Changes the string content and hides it
	 * 
	 * @param src
	 */
	public void setAndHide(String src)
	{
		prepareString(src);
	}

	/**
	 * Changes the string content
	 * 
	 * @param src
	 */
	public void set(String src)
	{
		prepareString(src);
		mLength=mWantString.length();
	}
	
	private void prepareString(String src)
	{
		mLength = 0;
		mWantString="";
		if (src == null)
			return;

		String mStep1 = "";
		int lineLength=0;
		for (int c = 0; c < src.length(); c++)
		{
			if ((src.charAt(c) == '\n') && mIgnoreNL)
			{
				mStep1=mStep1.concat(" ");
				continue;
			}
			if (src.charAt(c) == '\n')
			{
				mStep1=mStep1.concat(src.substring(c, c + 1));
				lineLength = 0;
			}
			else if (src.charAt(c) == '\t')
			{
				if (mTabLength>0)
				{
					int tab=mTabLength-(lineLength%mTabLength);
					if (tab==0)
						tab=mTabLength;
					for (int t = 0; t < tab; t++)
					{
						mStep1=mStep1.concat(" ");
						lineLength++;
					}
				}
			}
			else if (src.charAt(c) >= ' ')
			{
				mStep1=mStep1.concat(src.substring(c, c + 1));
				lineLength++;
			}
		}
		if (mDisplayWidth > 0)
		{
			int lineWidth = 0;
			int flc = 0; // FirstLineChar
			for (int c = 0; c < mStep1.length(); c++)
			{
				if (mStep1.charAt(c) == '\n')
				{
					lineWidth = 0;
					flc = c + 1;
				}
				else
					lineWidth += mFont.charWidth(mStep1.charAt(c));
				
				boolean copy=false;
				int llc = c; // Last Line Char
				if (lineWidth > mDisplayWidth)
				{
					while ((lineWidth > mDisplayWidth) && (llc > flc))
					{
						while ((mStep1.charAt(llc) == ' ') && (llc > flc)) // Quita los espacios del final
						{
							lineWidth -= mFont.charWidth(mStep1.charAt(llc));
							llc--;
						}
						if (lineWidth <= mDisplayWidth)
							break;
						while ((mStep1.charAt(llc) != ' ') && (llc > flc)) // Quita la última palabra
						{
							lineWidth -= mFont.charWidth(mStep1.charAt(llc));
							llc--;
						}
					}
					if (llc == flc) //Hay una palabra + larga que la linea
					{
						mWantString = mStep1;
						break;
					}
					copy=true;
				}
				if (copy||(c == mStep1.length()-1))
				{
					mWantString=mWantString.concat(mStep1.substring(flc,llc+1));
					if (llc+1==mStep1.length())
						break;
					mWantString=mWantString.concat("\n");
					flc=llc+1;
					while ((mStep1.charAt(flc) == ' ') && (flc < mStep1.length())) // Quita los espacios del final
						flc++;
					c=flc;
					lineWidth=0;
				}
			}
		}
		else
			mWantString = mStep1;
		mDisplayLines=1;
		for (int c=0;c<mWantString.length();c++)
		{
			if (mWantString.charAt(c) == '\n')
				mDisplayLines++;
		}
		mNewString=true;
	}
	
	private void tsSetString()
	{
		mString=mWantString;
		mNewString=false;
		mLinesCount=1;
		for (int c=0;c<mString.length();c++)
		{
			if (mString.charAt(c) == '\n')
				mLinesCount++;
		}
		mLineStart=new int[mLinesCount];
		mLineEnd=new int[mLinesCount];
		int l=0;
		mLineStart[l]=0;
		mLineEnd[mLinesCount-1]=mString.length();
		for (int c=0;c<mString.length();c++)
		{
			if (mString.charAt(c) == '\n')
			{
				mLineEnd[l++]=c;
				if (l<mLinesCount)
					mLineStart[l]=c+1;
			}
		}
		mWidth=0;
		for (l=0;l<mLinesCount;l++)
		{
			int lw=getWidth(mLineStart[l], mLineEnd[l]);
			if (mWidth<lw)
				mWidth=lw;
		}
	}

	/**
	 * Test if a point is within extent of the string
	 * 
	 * @param x Point
	 * @param y Point
	 * @return Returns true if point(x,y) is within string
	 */
	public boolean test(float x, float y)
	{
		float left=mPosition.mX;
		if (mAlignment == aRight)
			left=mPosition.mX - mWidth;
		else if (mAlignment == aCenter)
			left=mPosition.mX - mWidth / 2;

		if (x >= left)
			if (y >= mPosition.mY + mFont.mLineat)
				if (x < left + mWidth)
					if (y < mPosition.mY + mFont.mLineat + getHeight())
						return true;
		return false;
	}
	
	
	private int drawLine(GL10 gl, float y, int line)
	{
		if ((line>=0)&&(line<mLinesCount))
		{
			float x=mPosition.mX;
			if (mAlignment == aRight)
				x-= getWidth(mLineStart[line], mLineEnd[line]);
			else if (mAlignment == aCenter)
				x-= getWidth(mLineStart[line], mLineEnd[line]) / 2;
			for (int c=mLineStart[line];(c<mLineEnd[line])&&(c<mLength);c++)
			{
				int chr = mFont.getChar(mString.charAt(c));
				if (chr == -1)
				{
					x += mFont.mSpaceWidth;
					continue;
				}
				int chrWidth = mFont.mCharRight[chr] - mFont.mCharLeft[chr];
				mTextureIV[0] = mFont.mCharX[chr];
				mTextureIV[1] = mFont.mCharTop[chr] + mFont.mHeight;
				mTextureIV[2] = chrWidth;
				mTextureIV[3] = -mFont.mHeight;
				((GL11) gl).glTexParameteriv(GL11.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);

				((GL11Ext) gl).glDrawTexfOES(x + mFont.mCharLeft[chr], AngleSurfaceView.roHeight - (y + mFont.mHeight + mFont.mLineat),
						mZ, chrWidth, mFont.mHeight);
				x += mFont.mCharRight[chr] + mFont.mSpace;
			}
			return mFont.mHeight;
		}
		return 0;
	}

	private int getWidth(int flc, int llc)
	{
		int result=0;
		for (int c=flc;c<llc;c++)
			result += mFont.charWidth(mString.charAt(c));
		return result;
	}

	@Override
	public void draw(GL10 gl)
	{
		if (mFont != null)
		{
			if (mFont.mTexture != null)
			{
				if (mFont.mTexture.mHWTextureID > -1)
				{
					if (mNewString)
						tsSetString();
					else
					{
						if (mLength>0)
						{
						gl.glBindTexture(GL10.GL_TEXTURE_2D, mFont.mTexture.mHWTextureID);
						gl.glColor4f(mRed, mGreen, mBlue, mAlpha);
	
						int LC=linesCount();
						float y = mPosition.mY;
						for (int l = LC-mDisplayLines; l < LC; l++)
							y += drawLine(gl,y,l);
						}
					}
				}
				else
					mFont.mTexture.linkToGL(gl);
			}
		}
		super.draw(gl);
	}

	private int linesCount()
	{
		int result=1;
		if (mLength>mString.length())
			mLength=mString.length();
		for (int c=0;c<mLength;c++)
		{
			if (mString.charAt(c)=='\n')
				result++;
		}
		return result;
	}
	/**
	 * 
	 * @return String height in pixels
	 */
	public int getHeight()
	{
		return mFont.mHeight*linesCount();
	}

	/**
	 * 
	 * @return String length
	 */
	public int getLength()
	{
		if (mString != null)
			return mString.length();
		return 0;
	}
}
