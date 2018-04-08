package com.android.GameView;

import com.android.GeneralDesign.BitmapProvider;
import com.android.GeneralDesign.LayoutDesign;
import com.android.Sokoban.MainGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;

public class Toolbar extends GameDisplayItem
{
	private int mToolNum;
	private int mTiledSize;
	private int mCursorIndex;
	private Bitmap[] mIconBitmaps;
	private int[] mIconBitmapIds;
	
	private Paint mPaint = new Paint();
	
	public Toolbar(MainGame mainGame, int[] bitmapIds)
	{
		super(mainGame, LayoutDesign.DisplayItemID.TOOL_BAR);
		mCursorIndex = -1;
		mToolNum = bitmapIds.length;
		mIconBitmapIds = new int[mToolNum];
		mIconBitmaps = new Bitmap[mToolNum];
		for(int i = 0; i < mToolNum; i++)
		{
			mIconBitmapIds[i] = bitmapIds[i];
		}
		setDisplayRectSize();
		updataDisplaySetting();
		updataIconBitmap();
	}
	private int calculateTieldSize()
	{
		if(0 == mToolNum)
			return 0;
		int tiledSize;
		switch(mStyle)
		{
		case LayoutDesign.DispalyStyle.HORIZONTAL:
			tiledSize = mDisWinWidth / mToolNum;
			if(tiledSize * mToolNum == mDisWinWidth)
				tiledSize --;
			if(tiledSize >= mDisWinHeight)
				tiledSize = mDisWinHeight - 1;
			break;
		case LayoutDesign.DispalyStyle.VERTICAL:
			tiledSize = mDisWinHeight / mToolNum;
			if(tiledSize * mToolNum == mDisWinHeight)
				tiledSize --;
			if(tiledSize >= mDisWinWidth)
				tiledSize = mDisWinWidth - 1;
			break;
		default:
			tiledSize = 0;
		}
		return tiledSize;
	}
	public void setToolCursorIndex(int toolId)
	{
		mCursorIndex = toolId % mToolNum;
	}
	public int getToolCursorIndex()
	{
		return mCursorIndex;
	}
	public int getToolIndex(int x, int y)
	{
		int destRectX, destRectY;
		int itemIndex;
		destRectX = mDisWinX + mOffsetX;
		destRectY = mDisWinY + mOffsetY;
		if(LayoutDesign.DispalyStyle.HORIZONTAL == mStyle)
		{
			if(x < destRectX || x >= destRectX + mTiledSize * mToolNum)
				return -1;
			if(y < destRectY || y >= destRectY + mTiledSize)
				return -1;
			itemIndex = (x - destRectX) / mTiledSize;
		}
		else
		{
			if(x < destRectX || x >= destRectX + mTiledSize)
				return -1;
			if(y < destRectY || y >= destRectY + mTiledSize * mToolNum)
				return -1;
			itemIndex = (y - destRectY) / mTiledSize;
		}
		return itemIndex;
	}
	@Override
	public void setDisplayRectSize()
	{
		mTiledSize = calculateTieldSize();
		switch(mStyle)
		{
		case LayoutDesign.DispalyStyle.HORIZONTAL:
			mDisRectWidth = mTiledSize * mToolNum;
			mDisRectHeight = mTiledSize;
			break;
		case LayoutDesign.DispalyStyle.VERTICAL:
			mDisRectWidth = mTiledSize;
			mDisRectHeight = mTiledSize * mToolNum;
			break;
		default:
		}
	}
	private void updataIconBitmap()
	{
		for(int i = 0; i < mToolNum; i++)
		{
			mIconBitmaps[i] = BitmapProvider.getBitmap(
					mIconBitmapIds[i],
					mTiledSize,
					mTiledSize);
		}
	}
	public Rect getTileDisRect(int tiledIndex)
	{
		int left;
		int top;
		int right;
		int bottom;
		
		tiledIndex %= mToolNum;
		if(LayoutDesign.DispalyStyle.HORIZONTAL == mStyle)
		{
			left = mDisWinX + mOffsetX + tiledIndex * mTiledSize;
			right = left + mTiledSize;
			top = mDisWinY + mOffsetY;
			bottom = top + mTiledSize;
		}
		else
		{
			left = mDisWinX + mOffsetX;
			right = left + mTiledSize;
			top = mDisWinY + mOffsetY + tiledIndex * mTiledSize;
			bottom = top + mTiledSize;
		}
		return(new Rect(left, top, right, bottom));
	}
	@Override
	protected void onDisRectSizeChange(int preDisRectWidth,
			int preDisRectHeight, int newDisRectWidth, int newDisRectHeight)
	{
		updataIconBitmap();
	}
	@Override
	protected void drawDisplayArea(Canvas canvas)
	{
		int i;
		int x = 0, y = 0;
		int textRectSize = 12;
		
		for(i = 0; i < mToolNum; i++)
		{
			if(null != mIconBitmaps[i])
			{
				canvas.drawBitmap(mIconBitmaps[i], x, y, mPaint);
			}
			mPaint.setColor(Color.GRAY);
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(x, y, x + mTiledSize, y + mTiledSize, mPaint);
			mPaint.setStyle(Style.FILL);
			canvas.drawRect(x, y, x + textRectSize, y + textRectSize, mPaint);
			if(mCursorIndex == i)
				mPaint.setColor(Color.RED);
			else
				mPaint.setColor(Color.WHITE);
			canvas.drawText(Integer.toString(i + 1), x + 3, y + textRectSize - 2, mPaint);
			if(LayoutDesign.DispalyStyle.HORIZONTAL == mStyle)
			{
				x += mTiledSize;
				y = 0;
			}
			else
			{
				x = 0;
				y += mTiledSize;
			}
		}
	}

}

