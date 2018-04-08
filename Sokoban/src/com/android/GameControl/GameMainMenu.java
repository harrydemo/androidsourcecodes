package com.android.GameControl;

import com.android.GameData.GameDataSaver;
import com.android.GameData.SystemSettingsSaver;
import com.android.GameView.GameLogo;
import com.android.GameView.Menu;
import com.android.GeneralDesign.GameTimer;
import com.android.GeneralDesign.GameTouchEventHandler;
import com.android.GeneralDesign.MusicPlayer;
import com.android.GeneralDesign.StringsProvider;
import com.android.Sokoban.GameView;
import com.android.Sokoban.MainGame;
import com.android.Sokoban.Sokoban;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameMainMenu extends GameView
{
	public static final class MenuID
	{
		public static final int NULL = 						-1;
		public static final int MAIN_MENU = 				0;
		public static final int GAME_SETTING = 				1;
		public static final int SYS_SOUND_EFFECT = 			2;
		public static final int BG_MUSIC_SETTING = 			3;
		public static final int KEY_BOARD_MUSIC_SETTING = 	4;
		public static final int LANGUAGE_SETTING = 			5;
		public static final int LOAD_DEFAULT_SETTING = 		6;
		public static final int MENU_NUM = 					7;
	}
	private static final class MainMenuItem
	{
		public static final int START_PLAY = 				0;
		public static final int LOAD_EDIT_GAME = 			1;
		public static final int EDIT_GAME = 				2;
		public static final int GAME_SETTING = 				3;
		public static final int HELP = 						4;
		public static final int ABOUT =						5;
		public static final int EXIT_GAME = 				6;
	}
	private static final class GameSettingMenuItem
	{
		public static final int CANCEL = 					0;
		public static final int SYS_SOUND_EFFECT_SETTING = 	1;
		public static final int BG_MUSIC_SETTING = 			2;
		public static final int KEY_BOARD_MUSIC_SETTING = 	3;
		public static final int LANGUAGE_SETTING = 			4;
		public static final int LOAD_DEFAULT_SETTING = 		5;
	}
	private static final class SystemSoundMenuItem
	{
		public static final int CANCEL = 					0;
		public static final int SYS_SOUND_ON = 				1;
		public static final int SYS_SOUND_OFF = 			2;
	}
	private static final class BgMusicMenuItem
	{
		public static final int CANCEL = 					0;
		public static final int BG_MUSIC_1 = 				1;
		public static final int BG_MUSIC_2 = 				2;
		public static final int BG_MUSIC_3 = 				3;
		public static final int BG_MUSIC_4 = 				4;
		public static final int BG_MUSIC_OFF = 				5;
	}
	private static final class KeyBoardMusicMenuItem
	{
		public static final int CANCEL = 					0;
		public static final int KB_MUSIC_1 = 				1;
		public static final int KB_MUSIC_2 = 				2;
		public static final int KB_MUSIC_3 = 				3;
		public static final int KB_MUSIC_4 = 				4;
		public static final int KB_MUSIC_OFF = 				5;
	}
	private static final class LanguageMenuItem
	{
		public static final int CANCEL = 					0;
		public static final int LANGUAGE_CHS_SIM =			1;
		public static final int LANGUAGE_ENGLISH =			2;
	}
	private static final class LoadDefaultMenuItem
	{
		public static final int NO = 						0;
		public static final int YES =						1;
	}
	
	private static final int MUSIC_AUTO_PLAY_TIME = 20; 
	private static int mainMenuCurserRecorder = -1;
	
	Context mContext;
	Sokoban mSokoban;
	MainGame mMainGame;
	GameLogo mLogo;
	private MainMenu mMainMenu;
	private GameSettingMenu mGameSettingMenu;
	private SystemSoundEffectSettingMenu mSystemSoundEffectSettingMenu;
	private BackGroundMusicSettingMenu mBackGroundMusicSettingMenu;
	private KeyBoradMusicSettingMenu mKeyBoradMusicSettingMenu;
	private LanguageSettingMenu mLanguageSettingMenu;
	private LoadDefaultSettingMenu mLoadDefaultSettingMenu;

	MusicExplorer mMusicExplorer = new MusicExplorer();
	MenuTouchEventHandler mTouchEventHandler;
	public GameMainMenu(Context context, Sokoban sokoban, MainGame mainGame)
	{
		super(context);
		mContext = context;
		mSokoban = sokoban;
		mMainGame = mainGame;
		
		mLogo = new GameLogo(mMainGame);
		mTouchEventHandler = new MenuTouchEventHandler();
		
		createMenu();
		menuConstruct();
		
		Menu.setFoucus(mMainMenu);
	}
	private void createMenu()
	{
		int languageType = SystemSettingsSaver.getSettingItem(
				SystemSettingsSaver.SettingItem.OSD_LANGUAGE);
		
		mMainMenu = new MainMenu(
				mMainGame,
				StringsProvider.getMenuItemStrings(
						MenuID.MAIN_MENU,
						languageType));
		mGameSettingMenu = new GameSettingMenu(
				mMainGame,
				StringsProvider.getMenuItemStrings(
						MenuID.GAME_SETTING,
						languageType));
		mSystemSoundEffectSettingMenu = new SystemSoundEffectSettingMenu(
				mMainGame,
				StringsProvider.getMenuItemStrings(
						MenuID.SYS_SOUND_EFFECT,
						languageType));
		mBackGroundMusicSettingMenu = new BackGroundMusicSettingMenu(
				mMainGame,
				StringsProvider.getMenuItemStrings(
						MenuID.BG_MUSIC_SETTING,
						languageType));
		mKeyBoradMusicSettingMenu = new KeyBoradMusicSettingMenu(
				mMainGame,
				StringsProvider.getMenuItemStrings(
						MenuID.KEY_BOARD_MUSIC_SETTING,
						languageType));
		mLanguageSettingMenu = new LanguageSettingMenu(
				mMainGame,
				StringsProvider.getMenuItemStrings(
						MenuID.LANGUAGE_SETTING,
						languageType));
		mLoadDefaultSettingMenu = new LoadDefaultSettingMenu(
				mMainGame,
				StringsProvider.getMenuItemStrings(
						MenuID.LOAD_DEFAULT_SETTING,
						languageType));
	}
	private void menuConstruct()
	{
		mMainMenu.setSubMenu(
				MainMenuItem.GAME_SETTING,
				mGameSettingMenu);
		mGameSettingMenu.setSubMenu(
				GameSettingMenuItem.SYS_SOUND_EFFECT_SETTING,
				mSystemSoundEffectSettingMenu);
		mGameSettingMenu.setSubMenu(
				GameSettingMenuItem.BG_MUSIC_SETTING,
				mBackGroundMusicSettingMenu);
		mGameSettingMenu.setSubMenu(
				GameSettingMenuItem.KEY_BOARD_MUSIC_SETTING,
				mKeyBoradMusicSettingMenu);
		mGameSettingMenu.setSubMenu(
				GameSettingMenuItem.LANGUAGE_SETTING,
				mLanguageSettingMenu);
		mGameSettingMenu.setSubMenu(
				GameSettingMenuItem.LOAD_DEFAULT_SETTING,
				mLoadDefaultSettingMenu);
		if(false == GameDataSaver.checkUserFile())
		{
			mMainMenu.setItemEnable(
					MainMenuItem.LOAD_EDIT_GAME, false);
		}
		//播放按键音时操作不顺畅，默认关闭按键音，隐藏按键音设置菜单
		mGameSettingMenu.setItemVisible(
				GameSettingMenuItem.KEY_BOARD_MUSIC_SETTING,
				false);
	}
	class MainMenu extends Menu
	{
		public MainMenu(MainGame mainGame, String[][] menuStrings)
		{
			super(mainGame, menuStrings);
		}
		@Override
		public void onShowMenu()
		{
			if(0 <= mainMenuCurserRecorder)
				setMenuCursor(mainMenuCurserRecorder);
			else
				setMenuCursor(MainMenuItem.START_PLAY);
			mTouchEventHandler.setRect(getDisplayRect());
			MusicPlayer.stopMusic(MusicPlayer.PlayerType.BG_MUSIC_PLAYER);
		}
		@Override
		public void onItemFocusChanged(int selectedItemIndex, boolean abtain)
		{
		}
		@Override
		public void onItemEnter(int selectedItemIndex)
		{
			switch(selectedItemIndex)
			{
			case MainMenuItem.START_PLAY:
				mMainGame.controlView(MainGame.MainGameStatus.RUN_INTER_GAME);
				break;
			case MainMenuItem.LOAD_EDIT_GAME:
				mMainGame.controlView(MainGame.MainGameStatus.LOAD_SAVED_GAME);
				break;
			case MainMenuItem.EDIT_GAME:
				mMainGame.controlView(MainGame.MainGameStatus.EDIT_GAME);
				break;
			case MainMenuItem.GAME_SETTING:
				break;
			case MainMenuItem.HELP:
				mMainGame.controlView(MainGame.MainGameStatus.SHOW_HELP_VIEW);
				break;
			case MainMenuItem.ABOUT:
				mMainGame.controlView(MainGame.MainGameStatus.SHOW_ABOUT_VIEW);
				break;
			case MainMenuItem.EXIT_GAME:
				mMainGame.controlView(MainGame.MainGameStatus.EXIT_GAME);
				break;
			default:
				break;
			}
		}
		@Override
		public void onCloseMenu()
		{
		}
		@Override
		public void setCursorIndex(int itemIndex)
		{
			if(MainMenuItem.EXIT_GAME == itemIndex)
				mainMenuCurserRecorder = -1;
			else
				mainMenuCurserRecorder = itemIndex;
			super.setCursorIndex(itemIndex);
		}
		
	}
	class GameSettingMenu extends Menu
	{
		public GameSettingMenu(MainGame mainGame, String[][] menuStrings)
		{
			super(mainGame, menuStrings);
		}
		@Override
		public void onShowMenu()
		{
			mTouchEventHandler.setRect(getDisplayRect());
		}
		@Override
		public void onItemFocusChanged(int selectedItemIndex, boolean abtain)
		{
		}
		@Override
		public void onItemEnter(int selectedItemIndex)
		{
			switch(selectedItemIndex)
			{
			case GameSettingMenuItem.CANCEL:
				Menu.returnToParentMenu();
				break;
			default:
				break;
			}
		}
		@Override
		public void onCloseMenu()
		{
		}
	}
	class SystemSoundEffectSettingMenu extends Menu
	{
		public SystemSoundEffectSettingMenu(MainGame mainGame, String[][] menuStrings)
		{
			super(mainGame, menuStrings);
		}
		@Override
		public void onShowMenu()
		{
			int sysSoundEffectSettingState = SystemSettingsSaver.getSettingItem(
					SystemSettingsSaver.SettingItem.SYS_SOUND_EFFECT);
			int itemIndex;
			switch(sysSoundEffectSettingState)
			{
			case MusicPlayer.MusicSettingState.ON:
				itemIndex = SystemSoundMenuItem.SYS_SOUND_ON;
				break;
			case MusicPlayer.MusicSettingState.OFF:
				itemIndex = SystemSoundMenuItem.SYS_SOUND_OFF;
				break;
			default:
				itemIndex = SystemSoundMenuItem.CANCEL;
				break;
			}
			setMenuCursor(itemIndex);
			mTouchEventHandler.setRect(getDisplayRect());
		}
		@Override
		public void onItemFocusChanged(int selectedItemIndex, boolean abtain)
		{
		}
		@Override
		public void onItemEnter(int selectedItemIndex)
		{
			int systemMusicState = MusicPlayer.MusicSettingState.OFF;
			switch(selectedItemIndex)
			{
			case SystemSoundMenuItem.SYS_SOUND_ON:
				systemMusicState = MusicPlayer.MusicSettingState.ON;
				break;
			case SystemSoundMenuItem.SYS_SOUND_OFF:
				systemMusicState = MusicPlayer.MusicSettingState.OFF;
				break;
			case SystemSoundMenuItem.CANCEL:
				Menu.returnToParentMenu();
				return;
			default:
				return;
			}
			SystemSettingsSaver.setSettingItem(
					SystemSettingsSaver.SettingItem.SYS_SOUND_EFFECT,
					systemMusicState);
			SystemSettingsSaver.saveSettingsToFile();
			Menu.returnToParentMenu();
		}
		@Override
		public void onCloseMenu()
		{
		}
	}
	class BackGroundMusicSettingMenu extends Menu
	{
		public BackGroundMusicSettingMenu(MainGame mainGame, String[][] menuStrings)
		{
			super(mainGame, menuStrings);
		}
		@Override
		public void onShowMenu()
		{
			int bgMusicId = SystemSettingsSaver.getSettingItem(
					SystemSettingsSaver.SettingItem.GAME_BG_SOUND);
			int itemIndex;
			switch(bgMusicId)
			{
			case MusicPlayer.MusicID.SN_BG_MUSIC_1:
				itemIndex = BgMusicMenuItem.BG_MUSIC_1;
				break;
			case MusicPlayer.MusicID.SN_BG_MUSIC_2:
				itemIndex = BgMusicMenuItem.BG_MUSIC_2;
				break;
			case MusicPlayer.MusicID.SN_BG_MUSIC_3:
				itemIndex = BgMusicMenuItem.BG_MUSIC_3;
				break;
			case MusicPlayer.MusicID.SN_BG_MUSIC_4:
				itemIndex = BgMusicMenuItem.BG_MUSIC_4;
				break;
			case MusicPlayer.MusicID.NULL:
				itemIndex = BgMusicMenuItem.BG_MUSIC_OFF;
				break;
			default:
				itemIndex = BgMusicMenuItem.CANCEL;
				break;
			}
			setCursorIndex(itemIndex);
			mTouchEventHandler.setRect(getDisplayRect());
		}
		@Override
		public void onItemFocusChanged(int selectedItemIndex, boolean abtain)
		{
			if(abtain == false)
			{
				mMusicExplorer.reset();
				MusicPlayer.stopMusic(MusicPlayer.PlayerType.BG_MUSIC_PLAYER);
				return;
			}
			
			int bgMusicId;
			switch(selectedItemIndex)
			{
			case BgMusicMenuItem.BG_MUSIC_1:
				bgMusicId = MusicPlayer.MusicID.SN_BG_MUSIC_1;
				break;
			case BgMusicMenuItem.BG_MUSIC_2:
				bgMusicId = MusicPlayer.MusicID.SN_BG_MUSIC_2;
				break;
			case BgMusicMenuItem.BG_MUSIC_3:
				bgMusicId = MusicPlayer.MusicID.SN_BG_MUSIC_3;
				break;
			case BgMusicMenuItem.BG_MUSIC_4:
				bgMusicId = MusicPlayer.MusicID.SN_BG_MUSIC_4;
				break;
			case BgMusicMenuItem.BG_MUSIC_OFF:
			default:
				bgMusicId = MusicPlayer.MusicID.NULL;
				return;
			}
			if(MusicPlayer.MusicID.NULL != bgMusicId)
			{
				mMusicExplorer.setMusicId(
						MusicPlayer.PlayerType.BG_MUSIC_PLAYER,
						bgMusicId);
				mMusicExplorer.setTime(MUSIC_AUTO_PLAY_TIME, 0);
				mMusicExplorer.start();
			}
		}
		@Override
		public void onItemEnter(int selectedItemIndex)
		{
			int bgMusicId;
			switch(selectedItemIndex)
			{
			case BgMusicMenuItem.BG_MUSIC_1:
				bgMusicId = MusicPlayer.MusicID.SN_BG_MUSIC_1;
				break;
			case BgMusicMenuItem.BG_MUSIC_2:
				bgMusicId = MusicPlayer.MusicID.SN_BG_MUSIC_2;
				break;
			case BgMusicMenuItem.BG_MUSIC_3:
				bgMusicId = MusicPlayer.MusicID.SN_BG_MUSIC_3;
				break;
			case BgMusicMenuItem.BG_MUSIC_4:
				bgMusicId = MusicPlayer.MusicID.SN_BG_MUSIC_4;
				break;
			case BgMusicMenuItem.BG_MUSIC_OFF:
				bgMusicId = MusicPlayer.MusicID.NULL;
				break;
			case BgMusicMenuItem.CANCEL:
				Menu.returnToParentMenu();
				return;
			default:
				return;
			}
			SystemSettingsSaver.setSettingItem(
					SystemSettingsSaver.SettingItem.GAME_BG_SOUND,
					bgMusicId);
			SystemSettingsSaver.saveSettingsToFile();
			Menu.returnToParentMenu();
		}
		@Override
		public void onCloseMenu()
		{
		}
	}
	class KeyBoradMusicSettingMenu extends Menu
	{
		public KeyBoradMusicSettingMenu(MainGame mainGame, String[][] menuStrings)
		{
			super(mainGame, menuStrings);
		}
		@Override
		public void onShowMenu()
		{
			int keyBoardMusicId = SystemSettingsSaver.getSettingItem(
					SystemSettingsSaver.SettingItem.KEY_BOARD_SOUND);
			int itemIndex;
			switch(keyBoardMusicId)
			{
			case MusicPlayer.MusicID.SN_KB_1:
				itemIndex = KeyBoardMusicMenuItem.KB_MUSIC_1;
				break;
			case MusicPlayer.MusicID.SN_KB_2:
				itemIndex = KeyBoardMusicMenuItem.KB_MUSIC_2;
				break;
			case MusicPlayer.MusicID.SN_KB_3:
				itemIndex = KeyBoardMusicMenuItem.KB_MUSIC_3;
				break;
			case MusicPlayer.MusicID.SN_KB_4:
				itemIndex = KeyBoardMusicMenuItem.KB_MUSIC_4;
				break;
			case MusicPlayer.MusicID.NULL:
				itemIndex = KeyBoardMusicMenuItem.KB_MUSIC_OFF;
				break;
			default:
				itemIndex = KeyBoardMusicMenuItem.CANCEL;
				break;
			}
			setMenuCursor(itemIndex);
			mTouchEventHandler.setRect(getDisplayRect());
		}
		@Override
		public void onItemFocusChanged(int selectedItemIndex, boolean abtain)
		{
			int keyBoardMusicId;
			if(false == abtain)
			{
				mMusicExplorer.reset();
				MusicPlayer.stopMusic(
						MusicPlayer.PlayerType.KEY_BOARD_MUSIC_PLAYER);
				return;
			}
			switch(selectedItemIndex)
			{
			case KeyBoardMusicMenuItem.KB_MUSIC_1:
				keyBoardMusicId = MusicPlayer.MusicID.SN_KB_1;
				break;
			case KeyBoardMusicMenuItem.KB_MUSIC_2:
				keyBoardMusicId = MusicPlayer.MusicID.SN_KB_2;
				break;
			case KeyBoardMusicMenuItem.KB_MUSIC_3:
				keyBoardMusicId = MusicPlayer.MusicID.SN_KB_3;
				break;
			case KeyBoardMusicMenuItem.KB_MUSIC_4:
				keyBoardMusicId = MusicPlayer.MusicID.SN_KB_4;
				break;
			case KeyBoardMusicMenuItem.KB_MUSIC_OFF:
			default:
				keyBoardMusicId  = MusicPlayer.MusicID.NULL;
				return;
			}
			if(keyBoardMusicId  != MusicPlayer.MusicID.NULL)
			{
				mMusicExplorer.setMusicId(
						MusicPlayer.PlayerType.KEY_BOARD_MUSIC_PLAYER,
						keyBoardMusicId);
				mMusicExplorer.setTime(MUSIC_AUTO_PLAY_TIME, 0);
				mMusicExplorer.start();
			}
		}
		@Override
		public void onItemEnter(int selectedItemIndex)
		{
			int keyBoradMusicId;
			switch(selectedItemIndex)
			{
			case KeyBoardMusicMenuItem.KB_MUSIC_1:
				keyBoradMusicId = MusicPlayer.MusicID.SN_KB_1;
				break;
			case KeyBoardMusicMenuItem.KB_MUSIC_2:
				keyBoradMusicId = MusicPlayer.MusicID.SN_KB_2;
				break;
			case KeyBoardMusicMenuItem.KB_MUSIC_3:
				keyBoradMusicId = MusicPlayer.MusicID.SN_KB_3;
				break;
			case KeyBoardMusicMenuItem.KB_MUSIC_4:
				keyBoradMusicId = MusicPlayer.MusicID.SN_KB_4;
				break;
			case KeyBoardMusicMenuItem.KB_MUSIC_OFF:
				keyBoradMusicId = MusicPlayer.MusicID.NULL;
				break;
			case KeyBoardMusicMenuItem.CANCEL:
				Menu.returnToParentMenu();
				return;
			default:
				return;
			}
			MusicPlayer.freeMusic(MusicPlayer.PlayerType.KEY_BOARD_MUSIC_PLAYER);
			SystemSettingsSaver.setSettingItem(
					SystemSettingsSaver.SettingItem.KEY_BOARD_SOUND,
					keyBoradMusicId);
			SystemSettingsSaver.saveSettingsToFile();
			Menu.returnToParentMenu();
		}
		@Override
		public void onCloseMenu()
		{
		}
	}
	class LanguageSettingMenu extends Menu
	{
		public LanguageSettingMenu(MainGame mainGame, String[][] menuStrings)
		{
			super(mainGame, menuStrings);
		}
		@Override
		public void onShowMenu()
		{
			int languageType = SystemSettingsSaver.getSettingItem(
					SystemSettingsSaver.SettingItem.OSD_LANGUAGE);
			int itemIndex;
			switch(languageType)
			{
			case StringsProvider.LanguageID.CHINESE_SIMPLE:
				itemIndex = LanguageMenuItem.LANGUAGE_CHS_SIM;
				break;
			case MusicPlayer.MusicSettingState.OFF:
				itemIndex = LanguageMenuItem.LANGUAGE_ENGLISH;
				break;
			default:
				itemIndex = LanguageMenuItem.CANCEL;
				break;
			}
			setCursorIndex(itemIndex);
			mTouchEventHandler.setRect(getDisplayRect());
		}
		@Override
		public void onItemFocusChanged(int selectedItemIndex, boolean abtain)
		{
		}
		@Override
		public void onItemEnter(int selectedItemIndex)
		{
			int languageType;
			switch(selectedItemIndex)
			{
			case LanguageMenuItem.LANGUAGE_CHS_SIM:
				languageType = StringsProvider.LanguageID.CHINESE_SIMPLE;
				break;
			case LanguageMenuItem.LANGUAGE_ENGLISH:
				languageType = StringsProvider.LanguageID.ENGLISH;
				break;
			case LanguageMenuItem.CANCEL:
				Menu.returnToParentMenu();
				return;
			default:
				return;
			}
			SystemSettingsSaver.setSettingItem(
					SystemSettingsSaver.SettingItem.OSD_LANGUAGE,
					languageType);
			SystemSettingsSaver.saveSettingsToFile();
			onOSDLanguaChange(languageType);
			Menu.returnToParentMenu();
		}
		@Override
		public void onCloseMenu()
		{
		}
	}
	class LoadDefaultSettingMenu extends Menu
	{
		public LoadDefaultSettingMenu(MainGame mainGame, String[][] menuStrings)
		{
			super(mainGame, menuStrings);
		}
		@Override
		public void onShowMenu()
		{
			setMenuCursor(LoadDefaultMenuItem.NO);
			mTouchEventHandler.setRect(getDisplayRect());
		}
		@Override
		public void onItemFocusChanged(int selectedItemIndex, boolean abtain)
		{
		}
		@Override
		public void onItemEnter(int selectedItemIndex)
		{
			boolean loadDefaultSetting;
			switch(selectedItemIndex)
			{
			case LoadDefaultMenuItem.YES:
				loadDefaultSetting = true;
				break;
			case LoadDefaultMenuItem.NO:
				loadDefaultSetting = false;
				break;
			default:
				loadDefaultSetting = false;
				break;
			}
			if(true == loadDefaultSetting)
			{
				SystemSettingsSaver.loadSystemDefaultSettings();
				int languageType = SystemSettingsSaver.getSettingItem(
						SystemSettingsSaver.SettingItem.OSD_LANGUAGE);
				onOSDLanguaChange(languageType);
			}
			Menu.returnToParentMenu();
		}
		@Override
		public void onCloseMenu()
		{
		}
	}
	@Override
	protected void onScreenConfigrationChange(int orientation)
	{
		mLogo.onOrientationChange(orientation);
		mMainMenu.onOrientationChange(orientation);
		mGameSettingMenu.onOrientationChange(orientation);
		mSystemSoundEffectSettingMenu.onOrientationChange(orientation);
		mBackGroundMusicSettingMenu.onOrientationChange(orientation);
		mKeyBoradMusicSettingMenu.onOrientationChange(orientation);
		mLanguageSettingMenu.onOrientationChange(orientation);
		mLoadDefaultSettingMenu.onOrientationChange(orientation);
		
		Menu curMenu = Menu.getFoucus();
		if(null != curMenu)
			mTouchEventHandler.setRect(curMenu.getDisplayRect());
	}
	@Override
	protected void onOSDLanguaChange(int newlanguage)
	{
		mMainMenu.setMenuTexts(
				StringsProvider.getMenuItemStrings(
						MenuID.MAIN_MENU,
						newlanguage));
		mGameSettingMenu.setMenuTexts(
				StringsProvider.getMenuItemStrings(
						MenuID.GAME_SETTING,
						newlanguage));
		mSystemSoundEffectSettingMenu.setMenuTexts(
				StringsProvider.getMenuItemStrings(
						MenuID.SYS_SOUND_EFFECT,
						newlanguage));
		mBackGroundMusicSettingMenu.setMenuTexts(
				StringsProvider.getMenuItemStrings(
						MenuID.BG_MUSIC_SETTING,
						newlanguage));
		mKeyBoradMusicSettingMenu.setMenuTexts(
				StringsProvider.getMenuItemStrings(
						MenuID.KEY_BOARD_MUSIC_SETTING,
						newlanguage));
		mLanguageSettingMenu.setMenuTexts(
				StringsProvider.getMenuItemStrings(
						MenuID.LANGUAGE_SETTING,
						newlanguage));
		mLoadDefaultSettingMenu.setMenuTexts(
				StringsProvider.getMenuItemStrings(
						MenuID.LOAD_DEFAULT_SETTING,
						newlanguage));
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
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_DOWN:
			Menu.scrollDown();
			retVal = true;
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			Menu.scrollUp();
			retVal = true;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			Menu.selectedActiveItem();
			retVal = true;
			break;
		case KeyEvent.KEYCODE_BACK:
			Menu.returnToParentMenu();
			retVal = true;
			break;
		case KeyEvent.KEYCODE_1:
		case KeyEvent.KEYCODE_2:
		case KeyEvent.KEYCODE_3:
		case KeyEvent.KEYCODE_4:
		case KeyEvent.KEYCODE_5:
		case KeyEvent.KEYCODE_6:
		case KeyEvent.KEYCODE_7:
		case KeyEvent.KEYCODE_8:
		case KeyEvent.KEYCODE_9:
			Menu.selectedItem(keyCode - KeyEvent.KEYCODE_1);
		default:
			break;
		}
		return retVal;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		mTouchEventHandler.onTouchEvent(event);
		return true;
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		//clienDB.drawGrid(canvas);
		mLogo.onDraw(canvas);
		Menu.drawFoucusMenu(canvas);
	}
	@Override
	protected void reCycle()
	{
	}
	@Override
	protected void refurbish()
	{
		mLogo.refurbish();
		Menu.refurbishFoucusMenu();
		mMusicExplorer.refurbish();
	}
///////////////////////////////////////////////////////////////////////
	class MusicExplorer extends GameTimer
	{
		private int mMusicId;
		private int mPlayerType;
		public MusicExplorer()
		{
		}
		@Override
		public void onTimerActive()
		{
			if(MusicPlayer.MusicID.NULL != mMusicId)
			{
				MusicPlayer.playMusic(mPlayerType, mMusicId);
			}
		}
		public void setMusicId(int playerType, int musicId)
		{
			mPlayerType = playerType;
			mMusicId = musicId;
		}
	}
///////////////////////////////////////////////////////////////////////
	class MenuTouchEventHandler extends GameTouchEventHandler
	{
		public MenuTouchEventHandler()
		{
			super();
		}
		public MenuTouchEventHandler(Rect rect)
		{
			super(rect);
		}
		@Override
		public boolean onActionDone(int x, int y) 
		{
			Menu.selectedItem(x, y);
			return true;
		}
		@Override
		public boolean onActionMoveUp()
		{
			//Menu.scrollUp();
			return true;
		}
		@Override
		public boolean onActionMoveDown()
		{
			//Menu.scrollDown();
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
			Menu.scrollTo(Menu.getFoucus().getItemIndex(x, y));
			return false;
		}
	};
}