package com.android.GameControl;

import com.android.GameData.GameDataSaver;
import com.android.GameData.SystemSettingsSaver;
import com.android.GameView.GameContainer;
import com.android.GameView.Helpbar;
import com.android.GameView.HilightRectCursor;
import com.android.GameView.Toolbar;
import com.android.GeneralDesign.BitmapProvider;
import com.android.GeneralDesign.GameStateDataProvider;
import com.android.GeneralDesign.GameTouchEventHandler;
import com.android.GeneralDesign.MusicPlayer;
import com.android.GeneralDesign.StringsProvider;
import com.android.GeneralDesign.clienDB;
import com.android.Sokoban.GameView;
import com.android.Sokoban.MainGame;
import com.android.Sokoban.Sokoban;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameRunner extends GameView
{
	private static final int USER_INPUP_DELAY = 3;
	private final static class RunningState
	{
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int FADE_IN = 2;
		public static final int FADE_OUT = 3;
	}
	private class ToolbarID
	{
		public static final int NULL = 			-1;
		public static final int UN_DO = 		0;
		public static final int RE_DO = 		1;
		public static final int RESET = 		2;
		public static final int PRE_STATE =		3;
		public static final int NEXT_STATE =	4;
		public static final int EXIT =			5;
		public static final int TOOL_NUM = 	6;
	}
	private class MoveDir
	{
		public static final int UP = 1;
		public static final int DOWN = 2;
		public static final int LEFT = 3;
		public static final int RIGHT = 4;
	}
	private int[] toolBitmapIds = new int[]
	{
		BitmapProvider.BitmapID.UN_DO,
		BitmapProvider.BitmapID.RE_DO,
		BitmapProvider.BitmapID.RESET,
		BitmapProvider.BitmapID.PRE_STATE,
		BitmapProvider.BitmapID.NEXT_STATE,
		BitmapProvider.BitmapID.EXIT
	};
	
	public static final int FADE_FRAME_FREQUENCE = 20;
	
	private Context mContext;
	private Sokoban mSokoban;
	private MainGame mMainGame;
	
	private GameContainer mGameContainer;
	private int mRunningState;
	private int mFadeFrameAlpha;
	private Rect mFadeFrameRect;
	private Paint mFadeFramePaint;

	private Toolbar mToolbar;
	private Helpbar mHelpbar;
	
	private int mStepIndex;
	private int mCurStepIndex;
	private int mStateNo;
	
	private boolean mIsSystemMusicOn;
	private boolean mIsBgMusicOn;
	private boolean mIsKbMusicOn;
	private int mBgMusicId;
	private int mKbMusicId;
	private int mOSDLanguageType;
	
	TouchEventHandler mTouchEventHandler;
	RunnerHilightCursor mRunnerHilightCursor;
	
	private static final int KEY_RECORD_NUM = 5; 
	int[] keySequence = new int[KEY_RECORD_NUM];
	public GameRunner(
			Context context,
			Sokoban sokoban,
			MainGame mainGame,
			boolean loadGameFromeFile)
	{
		super(context);

		mContext = context;
		mSokoban = sokoban;
		mMainGame = mainGame;

		mToolbar = new Toolbar(mMainGame, toolBitmapIds);
		mHelpbar = new Helpbar(mMainGame);
		
		mTouchEventHandler = new TouchEventHandler();
		mRunnerHilightCursor = new RunnerHilightCursor();
		mRunnerHilightCursor.setTime(USER_INPUP_DELAY);
		
		getSystemSetting();
		onOSDLanguaChange(mOSDLanguageType);
		
		if(true == loadGameFromeFile)
		{
			loadGame(GameDataSaver.DEFAULT_DATA_FILE_NAME);
		}
		else
		{
			loadGame(0);
		}
	}
	private void loadGame(int[][] dataArray)
	{
		mGameContainer = new GameContainer(
				mContext,
				mSokoban,
				mMainGame,
				dataArray,
				GameContainer.WorkMode.PLAY_MODE);
	}
	private void loadGame(int stateNo)
	{
		if(stateNo == GameStateDataProvider.getGameStateToltalNumer())
			stateNo = 0;
		mStateNo = stateNo;
		loadGame(GameStateDataProvider.getGameStateData(mStateNo));
		startFadeInAnimation();
	}
	private void loadGame(String fileName)
	{
		mStateNo = -1;
		loadGame(GameDataSaver.importDataFromFile());
		startFadeInAnimation();
	}
	private void getSystemSetting()
	{
		int systemMusicEffectState = SystemSettingsSaver.getSettingItem(
				SystemSettingsSaver.SettingItem.SYS_SOUND_EFFECT);
		mBgMusicId = SystemSettingsSaver.getSettingItem(
				SystemSettingsSaver.SettingItem.GAME_BG_SOUND);
		mKbMusicId = SystemSettingsSaver.getSettingItem(
				SystemSettingsSaver.SettingItem.KEY_BOARD_SOUND);
		mOSDLanguageType = SystemSettingsSaver.getSettingItem(
				SystemSettingsSaver.SettingItem.OSD_LANGUAGE);
		
		mIsBgMusicOn = MusicPlayer.MusicID.NULL == mBgMusicId ?
				false : true;
		mIsKbMusicOn = MusicPlayer.MusicID.NULL == mKbMusicId ?
				false : true;
		mIsSystemMusicOn = MusicPlayer.MusicSettingState.ON == systemMusicEffectState ?
				true : false;
	}
	private void playSystemMusic(int musicId)
	{
		if(false == mIsSystemMusicOn)
			return;

		MusicPlayer.playMusic(
				MusicPlayer.PlayerType.SYS_MUSIC_PLAYER,
				musicId);
	}
	private void playBgMusic()
	{
		if(false == mIsBgMusicOn)
			return;

		MusicPlayer.playMusic(
				MusicPlayer.PlayerType.BG_MUSIC_PLAYER,
				mBgMusicId,
				true);
	}
	private void stopBgMusic()
	{
		MusicPlayer.freeMusic(MusicPlayer.PlayerType.BG_MUSIC_PLAYER);
	}
	private void playKeyBoardMusic()
	{
		if(false == mIsKbMusicOn)
			return;

		MusicPlayer.playMusic(
				MusicPlayer.PlayerType.KEY_BOARD_MUSIC_PLAYER,
				mKbMusicId);
	}
	private void updateHelpbarMsg()
	{
		if(mStateNo < 0)
		{
			String stateMsg = StringsProvider.getUserNotifyMsg(
					StringsProvider.UserNotifyMsgID.MSG_USER_GAME,
					mOSDLanguageType)
				+	"\n"
				+	StringsProvider.getUserNotifyMsg(
						StringsProvider.UserNotifyMsgID.LAB_STEP_INDEX,
						mOSDLanguageType)
				+	mCurStepIndex;
			mHelpbar.setText(stateMsg);
		}
		else
		{
			String stateMsg = StringsProvider.getUserNotifyMsg(
					StringsProvider.UserNotifyMsgID.LAB_GAME_STATE_NO_LEFT,
					mOSDLanguageType)
				+	(mStateNo + 1)
				+	StringsProvider.getUserNotifyMsg(
						StringsProvider.UserNotifyMsgID.LAB_GAME_STATE_NO_RIGHT,
						mOSDLanguageType)
				+	"\n"
				+	StringsProvider.getUserNotifyMsg(
						StringsProvider.UserNotifyMsgID.LAB_STEP_INDEX,
						mOSDLanguageType)
				+	mCurStepIndex;
			mHelpbar.setText(stateMsg);
		}
	}
	private void moveMainPlayer(int moveDir)
	{
		boolean actionResult;
		switch(moveDir)
		{
		case MoveDir.UP:
			mGameContainer.setMPAnimation(
					GameContainer.MPAnimation.ANIMATION_TYPE_MOVE_UP);
			actionResult = 
				mGameContainer.moveMainPlayer(GameContainer.MoveDir.UP);
			break;
		case MoveDir.DOWN:
			mGameContainer.setMPAnimation(
					GameContainer.MPAnimation.ANIMATION_TYPE_MOVE_DOWN);
			actionResult = 
				mGameContainer.moveMainPlayer(GameContainer.MoveDir.DOWN);
			break;
		case MoveDir.LEFT:
			mGameContainer.setMPAnimation(
					GameContainer.MPAnimation.ANIMATION_TYPE_MOVE_LEFT);
			actionResult = 
				mGameContainer.moveMainPlayer(GameContainer.MoveDir.LEFT);
			break;
		case MoveDir.RIGHT:
			mGameContainer.setMPAnimation(
					GameContainer.MPAnimation.ANIMATION_TYPE_MOVE_RIGHT);
			actionResult = 
				mGameContainer.moveMainPlayer(GameContainer.MoveDir.RIGHT);
			break;
		default:
			actionResult = false;
		}
		if(true == actionResult)
		{
			recordStep(mCurStepIndex + 1);
		}
	}
	private void recordStep(int stepIndex)
	{
		mCurStepIndex = stepIndex;
		mStepIndex = stepIndex;
		mGameContainer.recordStep(stepIndex);
		updateHelpbarMsg();
	}
	private void resetCurrentState()
	{
		if(mCurStepIndex > 0)
		{
			mCurStepIndex = 0;
			mGameContainer.gotoStep(mCurStepIndex);
		}
		updateHelpbarMsg();
	}
	private void gotoNextStep()
	{
		if(mCurStepIndex < mStepIndex)
		{
			mCurStepIndex ++;
			mGameContainer.gotoStep(mCurStepIndex);
		}
		updateHelpbarMsg();
	}
	private void gotoPreStep()
	{
		if(mCurStepIndex > 0)
		{
			mCurStepIndex --;
			mGameContainer.gotoStep(mCurStepIndex);
			updateHelpbarMsg();
		}
	}
	private void switchState(int toStateNum)
	{
		mStateNo = toStateNum;
		stopBgMusic();
		playSystemMusic(MusicPlayer.MusicID.SN_STATE_SWITCH);
		startFadeOutAnimation();
	}
	private void switchNextState()
	{
		int toStateNum = mStateNo;
		if(toStateNum < GameStateDataProvider.getGameStateToltalNumer() - 1)
			toStateNum++;
		else
			toStateNum = 0;
		switchState(toStateNum);
	}
	private void switchPreState()
	{
		int toStateNum = mStateNo;
		if(toStateNum < 0)
			return;
		if(toStateNum > 0)
			toStateNum--;
		else
			toStateNum = GameStateDataProvider.getGameStateToltalNumer() - 1;
		switchState(toStateNum);
	}
	private void exit()
	{
		mMainGame.controlView(MainGame.MainGameStatus.SHOW_MAIN_MENU);
	}
	private void startFadeOutAnimation()
	{
		mRunningState = RunningState.FADE_OUT;
		resetFadeOutFrame();
	}
	private void startFadeInAnimation()
	{
		mRunningState = RunningState.FADE_IN;
		resetFadeInFrame();
	}
	private void resetFadeInFrame()
	{
		mFadeFrameAlpha = 0xff;
		mFadeFrameRect = mGameContainer.getDisplayRect();
		mFadeFramePaint = new Paint();
		mFadeFramePaint.setColor(Color.BLACK);
		mFadeFramePaint.setStyle(Paint.Style.FILL);
		mHelpbar.setText("");
	}
	private void resetFadeOutFrame()
	{
		mFadeFrameAlpha = 0x00;
		mFadeFrameRect = mGameContainer.getDisplayRect();
		mFadeFramePaint = new Paint();
		mFadeFramePaint.setColor(Color.BLACK);
		mFadeFramePaint.setStyle(Paint.Style.FILL);
		mHelpbar.setText("");
	}
	private void playFadeInAnimation()
	{
		boolean isFadeAnimationCompleted;
		if(mFadeFrameAlpha > FADE_FRAME_FREQUENCE)
			isFadeAnimationCompleted = false;
		else
			isFadeAnimationCompleted = true;
		
		if(true == isFadeAnimationCompleted)
		{
			mRunningState = RunningState.RUNNING;
			mStepIndex = 0;
			mCurStepIndex = 0;
			updateHelpbarMsg();
			mFadeFrameRect = null;
			playBgMusic();
		}
		else
		{
			mFadeFrameAlpha -= FADE_FRAME_FREQUENCE;
		}
	}
	private void playFadeOutAnimation()
	{
		boolean isFadeAnimationCompleted;
		if(mFadeFrameAlpha < 0xff - FADE_FRAME_FREQUENCE)
			isFadeAnimationCompleted = false;
		else
			isFadeAnimationCompleted = true;
		
		if(true == isFadeAnimationCompleted)
		{
			loadGame(mStateNo);
		}
		else
		{
			mFadeFrameAlpha += FADE_FRAME_FREQUENCE;
		}
	}
	private void selectTool(int toolId)
	{
		if(ToolbarID.NULL == toolId ||
			ToolbarID.TOOL_NUM <= toolId)
		{
			return;
		}
		mRunnerHilightCursor.setToolbarIndex(toolId);
		mRunnerHilightCursor.setRect(
				mToolbar.getTileDisRect(toolId));
		mRunnerHilightCursor.show();
	}
	private void selectContainerTiled(int[] tiledPosIndex)
	{
		if(null == tiledPosIndex)
		{
			return;
		}

		int[] MPPosIndex = 
			mGameContainer.getTiledPosIndex(clienDB.GameObjectID.MP);
		if(null == MPPosIndex)
			return ;
			
		if(tiledPosIndex[0] > MPPosIndex[0] &&
				tiledPosIndex[1] == MPPosIndex[1])
		{
			moveMainPlayer(GameContainer.MoveDir.RIGHT);
		}
		if(tiledPosIndex[0] < MPPosIndex[0] &&
			tiledPosIndex[1] == MPPosIndex[1])
		{
			moveMainPlayer(GameContainer.MoveDir.LEFT);
		}
		else if(tiledPosIndex[0] == MPPosIndex[0] &&
				tiledPosIndex[1] < MPPosIndex[1])
		{
			moveMainPlayer(GameContainer.MoveDir.UP);
		}
		else if(tiledPosIndex[0] == MPPosIndex[0] &&
				tiledPosIndex[1] > MPPosIndex[1])
		{
			moveMainPlayer(GameContainer.MoveDir.DOWN);
		}
	}
	private void onToolbarEnter(int toolId)
	{
		switch(toolId)
		{
		case ToolbarID.UN_DO:
			gotoPreStep();
			break;
		case ToolbarID.RE_DO:
			gotoNextStep();
			break;
		case ToolbarID.RESET:
			resetCurrentState();
			break;
		case ToolbarID.PRE_STATE:
			switchPreState();
			break;
		case ToolbarID.NEXT_STATE:
			switchNextState();
			break;
		case ToolbarID.EXIT:
			exit();
		case ToolbarID.NULL:
		default:
			break;
		}
	}
	private boolean checkKeyInput(int keyCode)
	{
		boolean retVal = false;
		int i;
		for(i = 0; i < KEY_RECORD_NUM - 1; i++)
		{
			keySequence[i] = keySequence[i + 1];
		}
		keySequence[KEY_RECORD_NUM - 1] = keyCode;

		int index_s = -1;
		int index_enter = -1;
		int stateNo = -1;
		for(i = 0;(i < KEY_RECORD_NUM && index_enter == -1); i++)
		{
			switch(keySequence[i])
			{
			case KeyEvent.KEYCODE_S:
				index_s = i;
				stateNo = -1;
				break;
			case  KeyEvent.KEYCODE_K:
				if(index_s >= 0 &&
					i == index_s + 1)
				{
					stateNo = 0;
				}
				else
				{
					stateNo = -1;
				}
				break;
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_9:
			if(stateNo >= 0)
			{
				stateNo *= 10;
				stateNo += keySequence[i] - KeyEvent.KEYCODE_0;
			}
			else
			{
				stateNo = -1;
			}
			break;
			case KeyEvent.KEYCODE_ENTER:
				index_enter = i;
				if(stateNo <= 0)
					stateNo = -1;
				break;
			default:
				stateNo = -1;
				break;
			}
		}
		if(stateNo > 0)
		{
			retVal = true;
			if(index_s == 0 || index_enter != -1)
			{
				stateNo --;
				stateNo %= GameStateDataProvider.getGameStateToltalNumer();
				switchState(stateNo);
				for(i = 0; i < KEY_RECORD_NUM; i++)
				{
					keySequence[i] = 0;
				}
				retVal = true;
			}
		}
		return retVal;
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		mToolbar.onDraw(canvas);
		mGameContainer.onDraw(canvas);
		switch(mRunningState)
		{
		case RunningState.FADE_OUT:
		case RunningState.FADE_IN:
			mFadeFramePaint.setAlpha(mFadeFrameAlpha);
			canvas.drawRect(mFadeFrameRect, mFadeFramePaint);
			break;
		default:
			break;
		}
		mHelpbar.onDraw(canvas);
		mRunnerHilightCursor.onDraw(canvas);
	}
	@Override
	protected void onScreenConfigrationChange(int orientation)
	{
		mGameContainer.onOrientationChange(orientation);
		mToolbar.onOrientationChange(orientation);
		mHelpbar.onOrientationChange(orientation);
	}
	@Override
	protected void onOSDLanguaChange(int newlanguage)
	{
		mOSDLanguageType = newlanguage;
		updateHelpbarMsg();
	}
	@Override
	public boolean onKeyDown(int keyCode)
	{
		return false;
	}
	@Override
	public boolean onKeyUp(int keyCode)
	{
		boolean retVal = false;
		if(RunningState.RUNNING != mRunningState)
			return false;
		
		if(true == checkKeyInput(keyCode))
			return true;
		
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_UP:
			moveMainPlayer(MoveDir.UP);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			moveMainPlayer(MoveDir.DOWN);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			moveMainPlayer(MoveDir.LEFT);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			moveMainPlayer(MoveDir.RIGHT);
			break;
		case KeyEvent.KEYCODE_BACK:
			gotoPreStep();
			break;
		case KeyEvent.KEYCODE_MENU:
			exit();
			break;
		case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
			if(KeyEvent.KEYCODE_0 == keyCode)
				selectTool(10 + KeyEvent.KEYCODE_0 - KeyEvent.KEYCODE_1);
			else
				selectTool(keyCode - KeyEvent.KEYCODE_1);
			break;
		default:
			break;
		}
		playKeyBoardMusic();
		return retVal;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if(RunningState.RUNNING != mRunningState)
			return false;
		return mTouchEventHandler.onTouchEvent(event);
	}
	@Override
	protected void reCycle()
	{
	}
	@Override
	protected void refurbish()
	{
		switch(mRunningState)
		{
			case RunningState.FADE_OUT:
			{
				playFadeOutAnimation();
				break;
			}
			case RunningState.FADE_IN:
			{
				playFadeInAnimation();
				break;
			}
			case RunningState.RUNNING:
			{
				boolean isMissionCompleted = mGameContainer.checkMission();
				if(true == isMissionCompleted)
				{
					switchNextState();
				}
				break;
			}
			default:
				break;
		}
		mGameContainer.refurbish();
		mToolbar.refurbish();
		mHelpbar.refurbish();
		mRunnerHilightCursor.refurbish();
	}
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
	}
/////////////////////////////////////////////////////////////////////
	class TouchEventHandler extends GameTouchEventHandler
	{
		public TouchEventHandler()
		{
			super();
		}
		public TouchEventHandler(Rect rect)
		{
			super(rect);
		}
		@Override
		public boolean onActionDone(int x, int y)
		{
			int toolId = mToolbar.getToolIndex(x, y);
			if(toolId >= 0)
			{
				playSystemMusic(MusicPlayer.MusicID.SN_TOUTCH);
				selectTool(toolId);
				return true;
			}
			else
			{
				int[] containerTiledPosIndex = mGameContainer.getTiledPosIndex(x, y);
				if(null != containerTiledPosIndex)
				{
					selectContainerTiled(containerTiledPosIndex);
					return true;
				}
			}
			return false;
		}
		@Override
		public boolean onActionMoveUp()
		{
			moveMainPlayer(GameContainer.MoveDir.UP);
			return false;
		}
		@Override
		public boolean onActionMoveDown()
		{
			moveMainPlayer(GameContainer.MoveDir.DOWN);
			return true;
		}
		@Override
		public boolean onActionMoveLeft()
		{
			moveMainPlayer(GameContainer.MoveDir.LEFT);
			return false;
		}
		@Override
		public boolean onActionMoveRight()
		{
			moveMainPlayer(GameContainer.MoveDir.RIGHT);
			return false;
		}
		@Override
		public boolean onActionMoveOn(int x, int y)
		{
			return false;
		}
	}
	class RunnerHilightCursor extends HilightRectCursor
	{
		int mToolbarIndex;
		public RunnerHilightCursor()
		{
			super();
		}
		public void setToolbarIndex(int toolbarIndex)
		{
			mToolbarIndex = toolbarIndex;
		}
		@Override
		public void onAutoHide()
		{
			onToolbarEnter(mToolbarIndex);
			super.onAutoHide();
		}
	}
}