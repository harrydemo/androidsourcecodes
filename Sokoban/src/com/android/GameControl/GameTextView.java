package com.android.GameControl;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.android.GameData.SystemSettingsSaver;
import com.android.GameView.TextItem;
import com.android.GeneralDesign.LayoutDesign;
import com.android.GeneralDesign.StringsProvider;
import com.android.Sokoban.GameView;
import com.android.Sokoban.MainGame;
import com.android.Sokoban.Sokoban;

public class GameTextView extends GameView
{
	public static class GameTextViewType
	{
		public static final int HELP_VIEW = 0;
		public static final int ABOUT_VIEW = 1;
	}
	private MainGame mMainGame;
	private TextItem mTextItem;
	private int mTextViewType;
	
	public GameTextView(
			Context context,
			Sokoban sokoban,
			MainGame mainGame,
			int textViewType)
	{
		super(context);
		mMainGame = mainGame;
		mTextItem = new TextItem(
				mMainGame,
				LayoutDesign.DisplayItemID.GAME_TEXT_VIEW,
				15);
		mTextViewType = textViewType;
		int languageType = SystemSettingsSaver.getSettingItem(
				SystemSettingsSaver.SettingItem.OSD_LANGUAGE);
		onOSDLanguaChange(languageType);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		mTextItem.onDraw(canvas);
	}
	@Override
	public boolean onKeyDown(int keyCode)
	{
		return false;
	}
	@Override
	public boolean onKeyUp(int keyCode)
	{
		boolean retVal = mTextItem.onKeyDown(keyCode);
		if(false == retVal)
		{
			mMainGame.controlView(MainGame.MainGameStatus.SHOW_MAIN_MENU);
		}
		return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mMainGame.controlView(MainGame.MainGameStatus.SHOW_MAIN_MENU);
		return true;
	}
	@Override
	protected void onScreenConfigrationChange(int orientation)
	{
		mTextItem.onOrientationChange(orientation);
	}
	@Override
	protected void onOSDLanguaChange(int newlanguage)
	{
		switch(mTextViewType)
		{
		case GameTextViewType.HELP_VIEW:
			mTextItem.setText(StringsProvider.getHelpViewMsg(newlanguage));
			break;
		case GameTextViewType.ABOUT_VIEW:
			mTextItem.setText(StringsProvider.getAboutViewMsg(newlanguage));
			break;
		default:
			break;
		}
	}
	@Override
	protected void reCycle()
	{
	}
	@Override
	protected void refurbish()
	{
		mTextItem.refurbish();
	}
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		//mContext = null;
		//mSokoban = null;
		mMainGame = null;
		mTextItem = null;
	}
}

