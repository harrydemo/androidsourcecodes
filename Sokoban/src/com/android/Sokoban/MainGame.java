package com.android.Sokoban;

import com.android.GameControl.GameAboutView;
import com.android.GameControl.GameEditor;
import com.android.GameControl.GameHelpView;
import com.android.GameControl.GameMainMenu;
import com.android.GameControl.GameRunner;
import com.android.GameData.GameDataSaver;
import com.android.GameData.SystemSettingsSaver;
import com.android.GeneralDesign.BitmapProvider;
import com.android.GeneralDesign.LayoutDesign;
import com.android.GeneralDesign.MusicPlayer;
import com.android.GeneralDesign.clienDB;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;

public class MainGame 
{
	public static final class MainGameStatus
	{
		public static final int IDLE = 0;

		public static final int SHOW_MAIN_MENU = 1;
		public static final int RUN_INTER_GAME = 2;
		public static final int LOAD_SAVED_GAME = 3;
		public static final int EDIT_GAME = 4;
		public static final int SHOW_HELP_VIEW = 5;
		public static final int SHOW_ABOUT_VIEW = 6;
		public static final int EXIT_GAME = 7;
	}

	private static GameView mGameView = null;
	private Context mContext = null;
	private Sokoban mSokoban = null;
	private int mStatus;
	
	private int mScreenSizeH;
	private int mScreenSizeV;
	private int mScreenResolutionType;
	private int mScreenOrientation;
	
	public MainGame(Context context)
	{
		mContext = context;
		mSokoban = (Sokoban) context;
		mStatus = MainGameStatus.IDLE;

		initGame();
	}
	public void initGame()
	{
		GameDataSaver.create(mSokoban);
		BitmapProvider.create(mSokoban);
		SystemSettingsSaver.create(mSokoban);
		MusicPlayer.create(mContext);
		
		checkScreenResulution();
		
		controlView(MainGameStatus.SHOW_MAIN_MENU);
	}
	public void checkScreenResulution()
	{
		DisplayMetrics dm = new DisplayMetrics();
		mSokoban.getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenSizeH = dm.widthPixels;
		mScreenSizeV = dm.heightPixels;
		
		Log.i(clienDB.LOG_TAG, "screenSize: (" + mScreenSizeH + "," + mScreenSizeV + ")");
		
		if((mScreenSizeH == 800 && mScreenSizeV == 480) ||
			(mScreenSizeH == 480 && mScreenSizeV == 800))
		{
			mScreenResolutionType = LayoutDesign.ScreenResulution.WVGA_800X480;
		}
		else if((mScreenSizeH == 640 && mScreenSizeV == 480) ||
			(mScreenSizeH == 480 && mScreenSizeV == 640))
		{
			mScreenResolutionType = LayoutDesign.ScreenResulution.VGA_640X480;
		}
		else if((mScreenSizeH == 320 && mScreenSizeV == 480) ||
			(mScreenSizeH == 480 && mScreenSizeV == 320))
		{
			mScreenResolutionType = LayoutDesign.ScreenResulution.HVGA_480X320;
		}
		else if((mScreenSizeH == 320 && mScreenSizeV == 240) ||
			(mScreenSizeH == 240 && mScreenSizeV == 320))
		{
			mScreenResolutionType = LayoutDesign.ScreenResulution.QVGA_320X240;
		}
		else
			mScreenResolutionType = LayoutDesign.ScreenResulution.UNKNOWN_RESOLUTION;
		
		if(mScreenSizeH > mScreenSizeV)
			mScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		else
			mScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	}
	public int getScreenResolutionType()
	{
		return mScreenResolutionType;
	}
	public void setScreenOrentation(int screenOrientation)
	{
		mScreenOrientation = screenOrientation;
	}
	public int getScreenOrentation()
	{
		return mScreenOrientation;
	}
	public int getStatus()
	{
		return mStatus;
	}
	public void setStatus(int status)
	{
		mStatus = status;
	}
	public Activity getSokoban()
	{
		return mSokoban;
	}
	public static GameView getMainView()
	{
		return mGameView;
	}
	public void controlView(int status)
	{
		if (mStatus != status)
		{
			if (mGameView != null)
			{
				mGameView.reCycle();
				System.gc();
			}
		}
		freeGameView(mGameView);
		switch (status)
		{
		case MainGame.MainGameStatus.SHOW_MAIN_MENU:
			mGameView = new GameMainMenu(mContext, mSokoban, this);
			break;
		case MainGame.MainGameStatus.RUN_INTER_GAME:
			mGameView = new GameRunner(mContext, mSokoban, this, false);
			break;
		case MainGame.MainGameStatus.LOAD_SAVED_GAME:
			mGameView = new GameRunner(mContext, mSokoban, this, true);
			break;
		case MainGame.MainGameStatus.EDIT_GAME:
			mGameView = new GameEditor(mContext, mSokoban, this);
			break;
		case MainGame.MainGameStatus.SHOW_HELP_VIEW:
			mGameView = new GameHelpView(mContext, mSokoban, this);
			break;
		case MainGame.MainGameStatus.SHOW_ABOUT_VIEW:
			mGameView = new GameAboutView(mContext, mSokoban, this);
			break;
		case MainGame.MainGameStatus.EXIT_GAME:
			MusicPlayer.freeMusic();
			System.gc();
			mSokoban.finish();
			break;
		default:
			break;
		}
		setStatus(status);
	}
	public void freeGameView(GameView gameView)
	{
		if (gameView != null)
		{
			gameView = null;
			System.gc();
		}
	}
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}
}
