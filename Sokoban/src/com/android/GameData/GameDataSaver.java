package com.android.GameData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.android.GameData.GameDataStruct.DATA_FLAG;
import com.android.GeneralDesign.clienDB;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class GameDataSaver
{
	public static final String DEFAULT_DATA_FILE_NAME = "dataSaver.txt";
	private static Activity mSokoban;
	private static GameDataSaver instance;
	public static void create(Activity sokoban)
	{
		if(null == instance)
		{
			instance = new GameDataSaver(sokoban);
		}
	}
	private GameDataSaver(Activity sokoban)
	{
		mSokoban = sokoban;
	}
	public static boolean exportDataToFile(
			int[][] editDataArray)
	{
		if(null == editDataArray)
			return false;
		if(0 == editDataArray.length)
			return false;
		
		int colNum = editDataArray[0].length;
		int rowNum = editDataArray.length;
		Properties properties = new Properties();
		properties.put("ColNum", String.valueOf(colNum));
		properties.put("RowNum", String.valueOf(rowNum));
		
		for(int j = 0; j < rowNum; j++)
		{
			for(int i = 0; i < colNum; i++)
			{
				properties.put(
						"Array_" + String.valueOf(j)
						+ "_" + String.valueOf(i),
						String.valueOf(editDataArray[j][i] & GameDataStruct.DATA_FLAG.MASK_MAP_AND_SPRITE));
			}
		}
		try
		{
			FileOutputStream stream = mSokoban.openFileOutput(
					DEFAULT_DATA_FILE_NAME,
					Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "");
			return true;
		}
		catch (FileNotFoundException e)
		{
			Log.e(clienDB.LOG_TAG, "GameData saving fail(FileNotFoundException): " + e.toString());
			return false;
		}
		catch (IOException e)
		{
			Log.e(clienDB.LOG_TAG, "GameData saving fail(IOException): " + e.toString());
			return false;
		}
	}
	public static boolean checkUserFile()
	{
		Properties properties = new Properties();
		try
		{
			FileInputStream stream = mSokoban.openFileInput(DEFAULT_DATA_FILE_NAME);
			properties.load(stream);
		}
		catch (FileNotFoundException e)
		{
			Log.e(clienDB.LOG_TAG, "GameData file read fail(FileNotFoundException): " + e.toString());
			return false;
		}
		catch (IOException e)
		{
			Log.e(clienDB.LOG_TAG, "GameData file read fail(IOException): " + e.toString());
			return false;
		}
		
		try
		{
			int colNum = Integer.valueOf(properties.get("ColNum").toString());
			int rowNum = Integer.valueOf(properties.get("RowNum").toString());
			if(colNum <= 0 || rowNum <= 0)
				return false;
			return true;
		}
		catch(Exception e)
		{
			Log.e(clienDB.LOG_TAG, "GameData checking exception: " + e.toString());
			return false;
		}
	}
	public static int[][] importDataFromFile()
	{
		Properties properties = new Properties();
		try
		{
			FileInputStream stream = mSokoban.openFileInput(DEFAULT_DATA_FILE_NAME);
			properties.load(stream);
		}
		catch (FileNotFoundException e)
		{
			Log.e(clienDB.LOG_TAG, "GameData file import fail(FileNotFoundException): " + e.toString());
			return null;
		}
		catch (IOException e)
		{
			Log.e(clienDB.LOG_TAG, "GameData file import fail(IOException): " + e.toString());
			return null;
		}
		try
		{
			int colNum = Integer.valueOf(properties.get("ColNum").toString());
			int rowNum = Integer.valueOf(properties.get("RowNum").toString());
			
			int[][] editDataArray = new int[rowNum][colNum];
			for(int j = 0; j < rowNum; j++)
			{
				for(int i = 0; i < colNum; i++)
				{
					editDataArray[j][i] = Integer.valueOf(
							properties.get(
									"Array_" + String.valueOf(j)
									+ "_" + String.valueOf(i))
									.toString());
				}
			}
			return editDataArray;
		}
		catch(Exception e)
		{
			Log.e(clienDB.LOG_TAG, "GameData file import exception: " + e.toString());
			return null;
		}
	}
	public static String configDataToString(int configData, boolean showClassName)
	{
		String str;
		String className;
		if(true == showClassName)
			className = "GameDataStruct.DATA_FLAG.";
		else
			className = "";
		if(0 == (configData & DATA_FLAG.MASK_MAP_AND_SPRITE))
		{
			str = className + "NULL";
			return str;
		}
		int flag;
		boolean lowestBit = true;

		str = "";
		for(flag = 0x0001; flag != 0; flag <<= 1)
		{
			//if((configData & DATA_FLAG.MASK_MAP_AND_SPRITE) == 0)
			if((configData & DATA_FLAG.MASK_ALL) == 0)
				break;
			if((configData & flag) == 0)
				continue;
			
			if(lowestBit == false)
				str += " | ";
				
			switch(flag)
			{
			case DATA_FLAG.WALL:
				str += className + "WALL";
				break;
			case DATA_FLAG.PATH:
				str += className + "PATH";
				break;
			case DATA_FLAG.DEST:
				str += className + "DEST";
				break;
			case DATA_FLAG.BOX:
				str += className + "BOX";
				break;
			case DATA_FLAG.MP:
				str += className + "MP";
				break;
			case DATA_FLAG.CURSOR:
				str += className + "CURSOR";
				break;
			default:
				break;
			}
			configData &= (~flag);
			lowestBit = false;
		}
		return str;
	}
	public static void logData(int[][] editDataArray, boolean showBinaryValue,boolean showClassName)
	{
		if(null == editDataArray)
		{
			Log.i(clienDB.LOG_TAG, "null array");
			return;
		}
		if(null == editDataArray[0])
		{
			Log.i(clienDB.LOG_TAG, "null editDataArray[0]");
			return;
		}
		
		int colNum = editDataArray[0].length;
		int rowNum = editDataArray.length;
		
		if(colNum == 0 ||
			rowNum == 0)
		{
			return;
		}
		
		String str = "//-----------------------------------------------------------\n";
		str += "//state xxx\n";
		str += "{\n";
		Log.i(clienDB.LOG_TAG,str);
		int i,j;
		for(j = 0; j < rowNum; j++)
		{
			str = "\t/* " + (j + 1) + " */\n" + "\t{\n";
			Log.i(clienDB.LOG_TAG,str);
			for(i = 0; i < colNum; i++)
			{
				str = "\t\t";
				str += "/* ";
				if(i + 1 < 10)
					str += "0";
				str += Integer.toString(i + 1);
				str += " */";
				
				if(true == showBinaryValue)
				{
					str +=(
							"/*("
							+ Integer.toBinaryString((editDataArray[j][i] & 0xff00) >> 8)
							+ " "
							+ Integer.toBinaryString((editDataArray[j][i] & 0x00ff))
							+ ")*/"
							);
				}
				str += configDataToString(
						(editDataArray[j][i] &
						 GameDataStruct.DATA_FLAG.MASK_MAP_AND_SPRITE),
						showClassName);
				if(i < colNum - 1)
					str += ",";
				//str += "\n";
				Log.i(clienDB.LOG_TAG,str);
			}
			str = "\t}";
			if(j < rowNum - 1)
				str += ",\n";
			Log.i(clienDB.LOG_TAG,str);
		}
		str = "},\n";
		Log.i(clienDB.LOG_TAG,str);
	}
}
