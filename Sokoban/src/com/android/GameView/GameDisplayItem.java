package com.android.GameView;

import com.android.GeneralDesign.LayoutDesign;
import com.android.Sokoban.MainGame;

import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class GameDisplayItem
{
	protected int mDisWinX, mDisWinY;
	protected int mOffsetX, mOffsetY;
	protected int mDisWinWidth, mDisWinHeight;
	protected int mDisRectWidth, mDisRectHeight;
	protected boolean mVisible;
	protected int mStyle;
	protected int mAlignMode;
	protected MainGame mMainGame;
	protected int mDisplayItemId;
	public GameDisplayItem(MainGame mainGame, int displayItemId)
	{
		mMainGame = mainGame;
		mDisplayItemId = displayItemId;
		int[] settingParam = LayoutDesign.getDisplayConfigParam(
				mDisplayItemId,
				mMainGame.getScreenResolutionType(),
				mMainGame.getScreenOrentation());
		setDisplayParam(settingParam);
		mDisRectWidth = 0;
		mDisRectHeight = 0;
		mVisible = true;
	}
	public Rect getDisplayRect()
	{
		Rect rect = new Rect();
		rect.left = mDisWinX + mOffsetX;
		rect.top = mDisWinY + mOffsetY;
		rect.right = mDisWinX + mOffsetX + mDisRectWidth;
		rect.bottom = mDisWinY + mOffsetY + mDisRectHeight;
		return rect;
	}
	public void onOrientationChange(int orientation)
	{
		int screenResType = mMainGame.getScreenResolutionType();
		int[] settingParam = LayoutDesign.getDisplayConfigParam(
				mDisplayItemId,
				screenResType,
				orientation);
		setDisplayParam(settingParam);
		
		int preDisplayRectWidth = mDisRectWidth;
		int preDisplayRectHeight = mDisRectHeight;
		setDisplayRectSize();
		
		if(preDisplayRectWidth != mDisRectWidth ||
			preDisplayRectHeight != mDisRectHeight)
		{
			onDisRectSizeChange(preDisplayRectWidth, preDisplayRectHeight,
					mDisRectWidth, mDisRectHeight);
		}
		updataDisplaySetting();
	}
	protected void setDisplayParam(int[] settingParam)
	{
		if(null == settingParam)
			return;
		mDisWinX = settingParam[LayoutDesign.DispalyParamType.DIS_WIN_X];
		mDisWinY = settingParam[LayoutDesign.DispalyParamType.DIS_WIN_Y];
		mDisWinWidth = settingParam[LayoutDesign.DispalyParamType.DIS_WIN_WIDTH];
		mDisWinHeight = settingParam[LayoutDesign.DispalyParamType.DIS_WID_HEIGHT];
		mStyle = settingParam[LayoutDesign.DispalyParamType.STYLE];
		mAlignMode = settingParam[LayoutDesign.DispalyParamType.ALIGEN_MODE];
	}
	protected void updataDisplaySetting()
	{
		int	dx = (int)((mDisWinWidth - mDisRectWidth) * 0.5f);
		int	dy = (int)((mDisWinHeight - mDisRectHeight) * 0.5f);
		switch(mAlignMode)
		{
		case LayoutDesign.AlignMode.CENTER:
			mOffsetX = dx;
			mOffsetY = dy;
			break;
		case LayoutDesign.AlignMode.LEFT_TOP:
			mOffsetX = 0;
			mOffsetY = 0;
			break;
		case LayoutDesign.AlignMode.LEFT_MIDDLE:
			mOffsetX = 0;
			mOffsetY = dy;
			break;
		case LayoutDesign.AlignMode.RIGHT_MIDDLE:
			mOffsetX = mDisWinWidth - mDisRectWidth;
			mOffsetY = dy;
			break;
		case LayoutDesign.AlignMode.TOP_CENTER:
			mOffsetX = dx;
			mOffsetY = 0;
			break;
		case LayoutDesign.AlignMode.BUTTOM_CENTER:
			mOffsetX = dx;
			mOffsetY = mDisWinHeight - mDisRectHeight;
			break;
		default:
			mOffsetX = 0;
			mOffsetY = 0;
			break;
		}
	}
	public void setVisible(boolean isVisible)
	{
		mVisible = isVisible;
	}
	public abstract void setDisplayRectSize();
	protected abstract void drawDisplayArea(Canvas canvas);
	protected abstract void onDisRectSizeChange(
			int preDisRectWidth,
			int preDisRectHeight,
			int newDisRectWidth,
			int newDisRectHeight);
	public void refurbish()
	{
	}
	public void onDraw(Canvas canvas)
	{
		if(false == mVisible)
			return;
		canvas.save();
		canvas.translate(mDisWinX + mOffsetX , mDisWinY + mOffsetY);
		canvas.clipRect(-mOffsetX, -mOffsetY, mDisWinWidth - mOffsetX, mDisWinHeight - mOffsetY);
		drawDisplayArea(canvas);
		canvas.restore();
/*
		if(true == clienDB.enableSwDebug)
		{
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.WHITE);
			canvas.drawRect(
					mDisWinX,
					mDisWinY,
					mDisWinX + mDisWinWidth - 1,
					mDisWinY + mDisWinHeight - 1,
					paint);
			paint.setColor(Color.BLUE);
			canvas.drawRect(
					mDisWinX + mOffsetX,
					mDisWinY + mOffsetY,
					mDisWinX + mOffsetX + mDisRectWidth - 1,
					mDisWinY + mOffsetY + mDisRectHeight - 1,
					paint);
		}
*/
	}
}
