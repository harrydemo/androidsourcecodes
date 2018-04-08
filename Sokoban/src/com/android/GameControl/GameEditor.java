package com.android.GameControl;

import com.android.GameData.GameDataSaver;
import com.android.GameData.GameDataStruct;
import com.android.GameData.SystemSettingsSaver;
import com.android.GameView.GameContainer;
import com.android.GameView.Helpbar;
import com.android.GameView.HilightRectCursor;
import com.android.GameView.Toolbar;
import com.android.GeneralDesign.BitmapProvider;
import com.android.GeneralDesign.GameTouchEventHandler;
import com.android.GeneralDesign.MusicPlayer;
import com.android.GeneralDesign.StringsProvider;
import com.android.GeneralDesign.clienDB;
import com.android.Sokoban.GameView;
import com.android.Sokoban.MainGame;
import com.android.Sokoban.Sokoban;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameEditor extends GameView
{
	private static final int USER_INPUP_DELAY = 3;
	private class ToolbarID
	{
		public static final int NULL = -1;
		
		public static final int WALL = 0;
		public static final int PATH = 1;
		public static final int DEST = 2;
		public static final int BOX = 3;
		public static final int MP = 4;
		public static final int LOCK = 5;
		public static final int CLEA = 6;
		public static final int SAVE = 7;
		public static final int EXIT = 8;
		public static final int MAX_VALUE = 9;
	}
	
	private Context mContext;
	private Sokoban mSokoban;
	private MainGame mMainGame;
	
	private Toolbar mToolbar;
	private Helpbar mHelpbar;
	
	private GameContainer mGameContainer;
	private boolean mLockedMode = false;
	
	private boolean mIsSystemMusicOn;
	private boolean mIsBgMusicOn;
	private boolean mIsKbMusicOn;
	private int mBgMusicId;
	private int mKbMusicId;
	private int mOSDLanguageType;
	
	private int[] toolBitmapIds = new int[]
	{
		BitmapProvider.BitmapID.WALL,
		BitmapProvider.BitmapID.PATH,
		BitmapProvider.BitmapID.DEST,
		BitmapProvider.BitmapID.BOXA,
		BitmapProvider.BitmapID.MP_1_1,
		BitmapProvider.BitmapID.LOCK,
		BitmapProvider.BitmapID.CLEAR,
		BitmapProvider.BitmapID.SAVE,
		BitmapProvider.BitmapID.EXIT
	};
	
	TouchEventHandler mTouchEventHandler;
	EditorHilightCursor mEditorHilightCursor;
	
	public GameEditor(Context context, Sokoban sokoban, MainGame mainGame)
	{
		super(context);
		
		mContext = context;
		mSokoban = sokoban;
		mMainGame = mainGame;
		
		mGameContainer = new GameContainer(
				mContext,
				(Sokoban)mSokoban,
				mMainGame,
				GameDataSaver.importDataFromFile(),
				GameContainer.WorkMode.EDIT_MODE);
		
		mToolbar = new Toolbar(mMainGame, toolBitmapIds);
		mHelpbar = new Helpbar(mMainGame);
		mToolbar.setToolCursorIndex(ToolbarID.WALL);
		updateGeneratorCursor();
		
		mTouchEventHandler = new TouchEventHandler();
		mEditorHilightCursor = new EditorHilightCursor();
		mEditorHilightCursor.setTime(USER_INPUP_DELAY);
		
		getSystemSetting();
		playBgMusic();
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
	private void playKeyBoardMusic()
	{
		if(false == mIsKbMusicOn)
			return;

		MusicPlayer.playMusic(
				MusicPlayer.PlayerType.KEY_BOARD_MUSIC_PLAYER,
				mKbMusicId);
	}
	private void showMsg(String msg)
	{
		mHelpbar.setText(msg);
	}
	private void updateGeneratorCursor()
	{
		int toolId = mToolbar.getToolCursorIndex();
		if(true == mLockedMode)
		{
			mGameContainer.setCursorLockState(
					GameContainer.CursorLockState.LOCKED);
		}
		else
		{
			mGameContainer.setCursorLockState(
					GameContainer.CursorLockState.UNLOCK);
		}
		switch(toolId)
		{
		case ToolbarID.WALL:
			mGameContainer.setCursorType(GameContainer.CursorType.WALL);
			break;
		case ToolbarID.PATH:
			mGameContainer.setCursorType(GameContainer.CursorType.PATH);
			break;
		case ToolbarID.DEST:
			mGameContainer.setCursorType(GameContainer.CursorType.DEST);
			break;
		case ToolbarID.BOX:
			mGameContainer.setCursorType(GameContainer.CursorType.BOX);
			break;
		case ToolbarID.MP:
			mGameContainer.setCursorType(GameContainer.CursorType.MP);
			break;
		case ToolbarID.CLEA:
			mGameContainer.setCursorType(GameContainer.CursorType.CLEA);
			break;
		default:
			break;
		}
	}
	private void onToolbarSelect(int selectToolId)
	{	
		if(ToolbarID.NULL == selectToolId ||
			ToolbarID.MAX_VALUE <= selectToolId)
			return;
		
		int currentToolId = mToolbar.getToolCursorIndex();
		
		switch(selectToolId)
		{
		case ToolbarID.NULL:
			break;
		case ToolbarID.LOCK:
			if(currentToolId != ToolbarID.MP)
			{
				mLockedMode = mLockedMode == true ? false : true;
				updateGeneratorCursor();
			}
			break;
		case ToolbarID.WALL:
		case ToolbarID.PATH:
		case ToolbarID.DEST:
		case ToolbarID.BOX:
		case ToolbarID.MP:
		case ToolbarID.CLEA:
			if(currentToolId != selectToolId)
			{
				mLockedMode = false;
				mToolbar.setToolCursorIndex(selectToolId);
				updateGeneratorCursor();
			}
			break;
		case ToolbarID.SAVE:
			saveGame();
			break;
		case ToolbarID.EXIT:
			mMainGame.controlView(MainGame.MainGameStatus.SHOW_MAIN_MENU);
			break;
		default:
			break;
		}
	}
	private void onToolbarEnter()
	{
		int toolId;
		toolId = mToolbar.getToolCursorIndex();
		
		switch(toolId)
		{
		case ToolbarID.WALL:
			mGameContainer.placeObj(clienDB.GameObjectID.WALL);
			break;
		case ToolbarID.PATH:
			mGameContainer.placeObj(clienDB.GameObjectID.PATH);
			break;
		case ToolbarID.DEST:
			mGameContainer.placeObj(clienDB.GameObjectID.DEST);
			break;
		case ToolbarID.BOX:
			mGameContainer.placeObj(clienDB.GameObjectID.BOX);
			break;
		case ToolbarID.MP:
			mGameContainer.placeObj(clienDB.GameObjectID.MP);
			break;
		case ToolbarID.CLEA:
			mGameContainer.clearCell();
			break;
		case ToolbarID.NULL:
		default:
			break;
		}
		int checkResult = mGameContainer.checkGameData();
		showDataCheckRusult(checkResult);
	}
	private void showDataCheckRusult(int checkResult)
	{
		String errorMsg = "";
		if(GameDataStruct.DATA_CHECK_RESULT.OK == checkResult)
		{
			showMsg(StringsProvider.getUserNotifyMsg(
					StringsProvider.UserNotifyMsgID.DATA_CHECK_OK,
					mOSDLanguageType));
			return;
		}
		for(int flag = 0x0001; flag != 0; flag <<=1)
		{
			if(checkResult == 0)
				break;
			if((checkResult & flag) == 0)
				continue;
			
			switch(flag)
			{
			case GameDataStruct.DATA_CHECK_RESULT.ERROR_NO_MP:
				errorMsg += StringsProvider.getUserNotifyMsg(
						StringsProvider.UserNotifyMsgID.ERROR_NO_MP,
						mOSDLanguageType) + "\n";
				break;
			case GameDataStruct.DATA_CHECK_RESULT.ERROR_WRONG_MP_POS:
				errorMsg += StringsProvider.getUserNotifyMsg(
						StringsProvider.UserNotifyMsgID.ERROR_MP_POS_WRONG,
						mOSDLanguageType) + "\n";
				break;
			case GameDataStruct.DATA_CHECK_RESULT.ERROR_NO_B0X:
				errorMsg += StringsProvider.getUserNotifyMsg(
						StringsProvider.UserNotifyMsgID.ERROR_NO_B0X,
						mOSDLanguageType) + "\n";
				break;
			case GameDataStruct.DATA_CHECK_RESULT.ERROR_BOX_NUM_NOT_MATCH:
				errorMsg += StringsProvider.getUserNotifyMsg(
						StringsProvider.UserNotifyMsgID.ERROR_BOX_NUM_NOT_MATCH,
						mOSDLanguageType) + "\n";
				break;
			case GameDataStruct.DATA_CHECK_RESULT.ERROR_NO_MISSION:
				errorMsg += StringsProvider.getUserNotifyMsg(
						StringsProvider.UserNotifyMsgID.ERROR_NO_MISSION,
						mOSDLanguageType) + "\n";
				break;
			default:
				break;
			}
		}
		showMsg(errorMsg);
	}
	public void saveGame()
	{
		boolean retVal;
		int checkResult = mGameContainer.checkGameData();
		if(GameDataStruct.DATA_CHECK_RESULT.OK != checkResult)
		{
			playSystemMusic(MusicPlayer.MusicID.SN_ERROR);
			showDataCheckRusult(checkResult);
			return;
		}
		playSystemMusic(MusicPlayer.MusicID.SN_SAVE);

		//GameDataSaver.logData(mGameContainer.getEditData(), false, true);

		retVal = GameDataSaver.exportDataToFile(
				mGameContainer.getEditData());

		retVal = true;
		if(true == retVal)
		{
			showMsg(StringsProvider.getUserNotifyMsg(
					StringsProvider.UserNotifyMsgID.DATA_SAVE_SUCCESS,
					mOSDLanguageType));
		}
		else
		{
			showMsg(StringsProvider.getUserNotifyMsg(
					StringsProvider.UserNotifyMsgID.DATA_SAVE_SUCCESS,
					mOSDLanguageType));
		}
	}
	private void selectTool(int toolId)
	{
		if(ToolbarID.NULL == toolId ||
			ToolbarID.MAX_VALUE <= toolId)
		{
			return;
		}

		mEditorHilightCursor.setToolbarIndex(toolId);
		mEditorHilightCursor.setRect(mToolbar.getTileDisRect(toolId));
		mEditorHilightCursor.show();
	}
	private void selectContainerTiled(int[] touchTiledPosIndex)
	{
		if(null == touchTiledPosIndex)
			return;
		
		mGameContainer.moveCursor(touchTiledPosIndex);
		onToolbarEnter();
	}
	private void onDPADInput(int keyCode)
	{
		if(KeyEvent.KEYCODE_DPAD_CENTER == keyCode ||
			KeyEvent.KEYCODE_ENTER == keyCode)
		{
			onToolbarEnter();
			return;
		}
		//key UP/DOWN/LEFT/DOWN
		if(mLockedMode == true)
		{
			onToolbarEnter();
		}
		switch(keyCode)
		{
			case KeyEvent.KEYCODE_DPAD_UP:
				mGameContainer.moveCursor(GameContainer.MoveDir.UP);
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				mGameContainer.moveCursor(GameContainer.MoveDir.DOWN);
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				mGameContainer.moveCursor(GameContainer.MoveDir.LEFT);
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT: 
				mGameContainer.moveCursor(GameContainer.MoveDir.RIGHT);
				break;
			default:
				break;
		}
		if(mLockedMode == true)
		{
			onToolbarEnter();
		}
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
	}
	public boolean onKeyDown(int keyCode)
	{
		return false;
	}
	@Override
	public boolean onKeyUp(int keyCode)
	{
		switch(keyCode)
		{
		//case KeyEvent.KEYCODE_0:
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
			selectTool(keyCode - KeyEvent.KEYCODE_1);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			onDPADInput(keyCode);
			break;
		case KeyEvent.KEYCODE_BACK:
			break;
		case KeyEvent.KEYCODE_MENU:
			mMainGame.controlView(MainGame.MainGameStatus.SHOW_MAIN_MENU);
			break;
		case KeyEvent.KEYCODE_C:
			/*
			//清除所有数据
			int[] posIndex = new int[2];
			for(int i = 0; i < clienDB.MAX_COL_NUM; i++)
			{
				for(int j = 0; j < clienDB.MAX_ROW_NUM; j++)
				{
					posIndex[0] = i;
					posIndex[1] = j;
					mGameContainer.moveCursor(posIndex);
					mGameContainer.clearCell();
				}
			}
			mGameContainer.moveCursor(new int[]{0,0});
			*/
			break;
		default:
			break;
		}
		playKeyBoardMusic();
		return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return mTouchEventHandler.onTouchEvent(event);
	}
	public void onDraw(Canvas canvas)
	{
		mToolbar.onDraw(canvas);
		mGameContainer.onDraw(canvas);
		mHelpbar.onDraw(canvas);
		mEditorHilightCursor.onDraw(canvas);
	}
	@Override
	protected void reCycle()
	{
	}
	@Override
	protected void refurbish()
	{
		mGameContainer.refurbish();
		mToolbar.refurbish();
		mHelpbar.refurbish();
		mEditorHilightCursor.refurbish();
	}
	
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
			if(ToolbarID.NULL != toolId)
			{
				playSystemMusic(MusicPlayer.MusicID.SN_TOUTCH);
				selectTool(toolId);
				return true;
			}
			else
			{
				int [] touchTiledPosIndex = mGameContainer.getTiledPosIndex(x, y);
				if(null == touchTiledPosIndex)
					return false;
				selectContainerTiled(touchTiledPosIndex);
				return true;
			}
		}
		@Override
		public boolean onActionMoveUp()
		{
			return false;
		}
		@Override
		public boolean onActionMoveDown()
		{
			return true;
		}
		@Override
		public boolean onActionMoveLeft()
		{
			return false;
		}
		@Override
		public boolean onActionMoveRight()
		{
			return false;
		}
		@Override
		public boolean onActionMoveOn(int x, int y)
		{
			int [] touchTiledPosIndex = mGameContainer.getTiledPosIndex(x, y);
			if(null == touchTiledPosIndex)
				return false;
			selectContainerTiled(touchTiledPosIndex);
			return true;
		}
	}
	class EditorHilightCursor extends HilightRectCursor
	{
		int mToolbarIndex;
		public EditorHilightCursor()
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
			if(ToolbarID.NULL != mToolbarIndex)
			{
				onToolbarSelect(mToolbarIndex);
			}
			super.onAutoHide();
		}
	}
}

