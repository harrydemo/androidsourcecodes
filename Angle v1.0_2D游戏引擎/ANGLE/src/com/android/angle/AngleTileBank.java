package com.android.angle;

import javax.microedition.khronos.opengles.GL10;

/**
 * Bank with tiles for tile map
 * @author Ivan Pajuelo
 *
 */
public class AngleTileBank
{
	public int mTilesCount;
	public int mTilesColumns;

	private AngleTextureEngine mTextureEngine;
	public AngleTexture mTexture;
	public int mTileWidth;
	public int mTileHeight;

	public AngleTileBank (AngleSurfaceView view, int resourceId, int tilesCount, int tilesColumns, int tileWidth, int tileHeight)
	{
		mTextureEngine=view.getTextureEngine();
		mTexture = mTextureEngine.createTextureFromResourceId(resourceId);
		mTilesCount=tilesCount;
		mTilesColumns=tilesColumns;
		mTileWidth=tileWidth;
		mTileHeight=tileHeight;
		
	}

	public void onDestroy(GL10 gl)
	{
		mTextureEngine.deleteTexture(mTexture);
	}
}
