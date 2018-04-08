package com.android.GameView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.android.GeneralDesign.BitmapProvider;
import com.android.GeneralDesign.LayoutDesign;
import com.android.Sokoban.MainGame;

public class GameLogo extends GameDisplayItem
{
	Paint mPaint = new Paint();
	Bitmap mBitmapLogo;
	public GameLogo(MainGame mainGame)
	{
		super(mainGame, LayoutDesign.DisplayItemID.MAIN_MENU_LOGO);
		setDisplayRectSize();
		updataDisplaySetting();
		updateBitmap();
	}
	private void updateBitmap()
	{
		mBitmapLogo = BitmapProvider.getBitmap(
				BitmapProvider.BitmapID.GAME_LOGO, mDisRectWidth, mDisRectHeight);
	}
	@Override
	protected void drawDisplayArea(Canvas canvas)
	{
		canvas.drawBitmap(mBitmapLogo, 0, 0, mPaint);
	}
	@Override
	public void setDisplayRectSize()
	{
		int[] logoBitmapSize = BitmapProvider.getBitampSize(BitmapProvider.BitmapID.GAME_LOGO);
		float factorLogo = (1.0f) * logoBitmapSize[0] / logoBitmapSize[1];
		float factorWind = (1.0f) * mDisWinWidth / mDisWinHeight;
		if(factorLogo > factorWind)
		{
			mDisRectWidth = mDisWinWidth;
			mDisRectHeight = (int)(mDisWinWidth / factorLogo);
		}
		else
		{
			mDisRectHeight = mDisWinHeight;
			mDisRectWidth = (int)(mDisWinHeight * factorLogo);
		}
	}
	@Override
	protected void onDisRectSizeChange(
			int preDisRectWidth, int preDisRectHeight,
			int newDisRectWidth, int newDisRectHeight)
	{
		updateBitmap();
	}
}
