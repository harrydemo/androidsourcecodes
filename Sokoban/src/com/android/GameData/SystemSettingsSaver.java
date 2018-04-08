package com.android.GameData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.android.GeneralDesign.MusicPlayer;
import com.android.GeneralDesign.StringsProvider;
import com.android.GeneralDesign.clienDB;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class SystemSettingsSaver
{
	public static class SettingItem
	{
		public static final int SYS_SOUND_EFFECT = 	0;
		public static final int GAME_BG_SOUND = 	1;
		public static final int KEY_BOARD_SOUND =	2;
		public static final int OSD_LANGUAGE = 		3;
		public static final int MAX_VALUE = 		4;
	}
	private static final String[] SETTING_ITEM_LAB =
	{
		"SYS_SOUND_EFFECT",
		"GAME_BG_SOUND",
		"KEY_BOARD_SOUND",
		"OSD_LANGUAGE",
	};
	private static final int[] mSystemDefaultSettings =
    {
		MusicPlayer.MusicSettingState.ON,		/* SYS_SOUND_EFFECT */
		MusicPlayer.MusicID.NULL,			/* GAME_BG_SOUND */
		MusicPlayer.MusicID.NULL,					/* KEY_SOUND */
		StringsProvider.LanguageID.CHINESE_SIMPLE,	/* OSD_LANGUAGE */
    };
		 
	private static final String SYSTEM_SETTING_FILE_NAME = "setting.txt";
	private static Activity mSokoban; 
	private static int[] mSettingsArray;
	private static SystemSettingsSaver instance;
	public static void create(Activity sokoban)
	{
		if(null == instance)
		{
			instance = new SystemSettingsSaver(sokoban);
		}
	}
	private SystemSettingsSaver(Activity sokoban)
	{
		mSokoban = sokoban;
		
		mSettingsArray = importSettingsFromFile();

		if(null == mSettingsArray)
		{
			loadSystemDefaultSettings();
		}
	}
	public static void loadSystemDefaultSettings()
	{
		mSettingsArray = getSystemDefaultSettings();
		saveSettingsToFile();
	}
	private static int[] getSystemDefaultSettings()
	{
		int[] systemDefaultSettings = new int[SettingItem.MAX_VALUE];
		System.arraycopy(
				mSystemDefaultSettings, 0,
				systemDefaultSettings, 0,
				SettingItem.MAX_VALUE);
		return systemDefaultSettings;
	}
	public static void setSettingItem(int itemId, int itemValue)
	{
		//Log.i(clienDB.LOG_TAG, "set Setting: " + SETTING_ITEM_LAB[itemId] + "(" + mSettingsArray[itemId] + ")");
		itemId = itemId % mSettingsArray.length;
		mSettingsArray[itemId] = itemValue;
	}
	public static int getSettingItem(int itemId)
	{
		/*
		Log.i(clienDB.LOG_TAG, "get Setting: "
				+ SETTING_ITEM_LAB[itemId] + "[" + itemId + "]"
				+ "(" + mSettingsArray[itemId] + ")");
		*/
		itemId = itemId % mSettingsArray.length;
		return mSettingsArray[itemId];
	}
	public static boolean saveSettingsToFile()
	{
		Properties properties = new Properties();
		for(int i = 0; i < SettingItem.MAX_VALUE; i++)
		{
			properties.put(
					SETTING_ITEM_LAB[i],
					String.valueOf(mSettingsArray[i]));
		}
		try
		{
			FileOutputStream stream = mSokoban.openFileOutput(
					SYSTEM_SETTING_FILE_NAME,
					Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "");
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}
	private static int[] importSettingsFromFile()
	{
		Properties properties = new Properties();
		try
		{
			FileInputStream stream = mSokoban.openFileInput(SYSTEM_SETTING_FILE_NAME);
			properties.load(stream);
		}
		catch (FileNotFoundException e)
		{
			Log.e(clienDB.LOG_TAG, "Syssetting file read fail(FileNotFoundException): " + e.toString());
			return null;
		}
		catch (IOException e)
		{
			Log.e(clienDB.LOG_TAG, "Syssetting file read fail(IOException): " + e.toString());
			return null;
		}
		
		try
		{
			int[] systemDefaultSettings = new int[SettingItem.MAX_VALUE];
			for(int i = 0; i < SettingItem.MAX_VALUE; i++)
			{
				systemDefaultSettings[i] = Integer.valueOf(
						properties.get(SETTING_ITEM_LAB[i]).toString());
			}
			return systemDefaultSettings;
		}
		catch(Exception e)
		{
			Log.e(clienDB.LOG_TAG, "Syssetting file read exception: " + e.toString());
			return null;
		}
	}
}
