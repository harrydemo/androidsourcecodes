package com.android.angle;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;


/**
 * Tile map
 * @author Ivan Pajuelo
 *
 */
public class AngleTileMap extends AngleObject
{
	protected int[] mTextureIV = new int[4];
	protected AngleTileBank mTileBank;
	public AngleVector mPosition; 
	public float mZ; // Z position (0=Near, 1=Far)
	protected boolean mHorizontalRepeat;
	protected boolean mVerticalRepeat;
	public int mColumnsCount;
	public int mRowsCount;
	public int[] mMap;
	public int mWidth;
	public int mHeight;
	public float mRed;
	public float mGreen;
	public float mBlue;
	public float mAlpha;
	public float mLeft;
	public float mTop;
	public float mScale;

	public AngleTileMap (AngleTileBank tileBank, int width, int height, int columnsCount, int rowsCount, boolean horizontalRepeat, boolean verticalRepeat)
	{
		Init (tileBank);
		mWidth=width;
		mHeight=height;
		mColumnsCount=columnsCount;
		mRowsCount=rowsCount;
		mHorizontalRepeat=horizontalRepeat;
		mVerticalRepeat=verticalRepeat;
		mScale=1f;
		mMap=new int[mColumnsCount*mRowsCount];
	}

	private void Init(AngleTileBank tileBank)
	{
		mTileBank=tileBank;
		mPosition = new AngleVector(0, 0);
		mZ = 0;
		mRed=1;
		mGreen=1;
		mBlue=1;
		mAlpha=1;
	}
	
	@Override
	public void draw(GL10 gl)
	{
		if (mTileBank.mTexture != null)
		{
			if (mTileBank.mTexture.mHWTextureID>-1)
			{
				if (mTileBank!=null)
				{
					int TW=(int) (mTileBank.mTileWidth*mScale);
					int TH=(int) (mTileBank.mTileHeight*mScale);
					int ttdX=(mWidth/TW)+1;
					int ttdY=(mHeight/TH)+1;

					gl.glBindTexture(GL10.GL_TEXTURE_2D, mTileBank.mTexture.mHWTextureID);
					gl.glColor4f(mRed,mGreen,mBlue,mAlpha);

				   for (int y=0;y<ttdY;y++)
				   {
				   	int py=y*TH-(((int) (mTop*mScale))%TH);

				   	mTextureIV[3] = -mTileBank.mTileHeight;//(int) (-H/mScale); // Hcr
					   for (int x=0;x<ttdX;x++)
					   {
					   	int tile=getTile(x+(int)(mLeft*mScale/TW),y+(int)(mTop*mScale/TH));
					   	int px=x*TW-(((int)(mLeft*mScale))%TW);
					   	
					   	if ((tile>=0)&&(tile<mTileBank.mTilesCount))
					   	{
					   		mTextureIV[0] = (tile%mTileBank.mTilesColumns)*(mTileBank.mTileWidth);//+(dx/mScale));// Ucr
					   		mTextureIV[1] = (tile/mTileBank.mTilesColumns)*(mTileBank.mTileHeight)+mTileBank.mTileHeight;//-(dy/mScale));// Vcr
								mTextureIV[2] = mTileBank.mTileWidth;//(int) (W/mScale); // Wcr
					   		((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, GL11Ext.GL_TEXTURE_CROP_RECT_OES, mTextureIV, 0);
					   		((GL11Ext) gl).glDrawTexfOES(mPosition.mX+px, AngleSurfaceView.roHeight-(mPosition.mY+py+TH), mZ, TW, TH);
					   	}
					   }
				   }
				}
			}
			else
				mTileBank.mTexture.linkToGL(gl);
		}
		super.draw(gl);
	}

	private int getTile(int c, int r)
	{
		if (c<0)
		{
			if (mHorizontalRepeat)
			{
				while (c<0)
					c+=mColumnsCount;
			}
			else
				return -1;
		}
		if (c>=mColumnsCount)
		{
			if (mHorizontalRepeat)
			{
				while (c>=mColumnsCount)
					c-=mColumnsCount;
			}
			else
				return -1;
		}
		if (r<0)
		{
			if (mVerticalRepeat)
			{
				while (r<0)
					r+=mRowsCount;
			}
			else
				return -1;
		}
		if (r>=mRowsCount)
		{
			if (mVerticalRepeat)
			{
				while (r>=mRowsCount)
					r-=mRowsCount;
			}
			else
				return -1;
		}
		return mMap[r*mColumnsCount+c];
	}
}
