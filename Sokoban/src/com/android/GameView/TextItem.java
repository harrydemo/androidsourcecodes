package com.android.GameView;

import android.graphics.Canvas;
import android.graphics.Color;

import com.android.GeneralDesign.LayoutDesign;
import com.android.GeneralDesign.TextUtil;
import com.android.Sokoban.MainGame;

public class TextItem extends GameDisplayItem
{
	TextUtil mTextRect;
	MainGame mMainGame;
	int mMargin;
	public TextItem(MainGame mainGame, int displayItemId, int margin)
	{
		super(mainGame, displayItemId);
		mMainGame = mainGame;
		mMargin = margin;
		
		mTextRect = new TextUtil();
		mTextRect.setBGColor(Color.BLACK);
		mTextRect.setTextColor(Color.WHITE);
		mTextRect.setAlpha(0xff);

		setDisplayRectSize();
		updataDisplaySetting();
		updataTextRect();
	}
	public void updataTextRect()
	{
		mTextRect.setTextSize(
				LayoutDesign.getTextSize(
						mMainGame.getScreenResolutionType(),
						mMainGame.getScreenOrentation(),
						mDisplayItemId)
				);
		mTextRect.setRect(0, 0, mDisRectWidth, mDisRectHeight);
		mTextRect.updateTextIfon();
	}
	public void setText(String str)
	{
		mTextRect.setText(str);
		mTextRect.updateTextIfon();
	}
	public boolean onKeyDown(int keyCode)
	{
		return mTextRect.onKeyDown(keyCode);
	}
	@Override
	protected void drawDisplayArea(Canvas canvas)
	{
		mTextRect.onDraw(canvas);
	}
	@Override
	public void setDisplayRectSize()
	{
		mDisRectWidth = mDisWinWidth - (mMargin << 1);
		mDisRectHeight = mDisWinHeight - (mMargin << 1);
	}
	@Override
	protected void onDisRectSizeChange(
			int preDisRectWidth, int preDisRectHeight,
			int newDisRectWidth, int newDisRectHeight)
	{
		updataTextRect();
	}
}

