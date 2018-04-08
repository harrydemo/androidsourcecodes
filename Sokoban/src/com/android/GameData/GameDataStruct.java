package com.android.GameData;

import com.android.GeneralDesign.clienDB;

public class GameDataStruct
{
	public static final class DATA_FLAG
	{
		public static final int NULL = 0x0000;
		public static final int MASK_ALL = 0xffff;
		public static final int MASK_MAP = 0x00ff;
		public static final int MASK_SPRITE = 0x7f00;
		public static final int MASK_MAP_AND_SPRITE = 0x7fff;
		public static final int WALL = 0x0001;
		public static final int PATH = 0x0002;
		public static final int DEST = 0x0004;
		public static final int BOX = 0x0100;
		public static final int MP = 0x0200;
		public static final int CURSOR = 0x8000;
	}
	public static final class DATA_CHECK_RESULT
	{
		public static final int OK = 0x0000;
		public static final int ERROR_NO_MP = 0x0001;
		public static final int ERROR_WRONG_MP_POS = 0x0002;
		public static final int ERROR_NO_B0X = 0x0004;
		public static final int ERROR_BOX_NUM_NOT_MATCH = 0x0008;
		public static final int ERROR_NO_MISSION = 0x0010;
	}
	
	public int[][] mDataArray;
	private int[][] mOriginalDataArray;
	private int[][][][] mRecordDataArray;
	private int[][] mRecorderSize;
	private boolean mIsSupportedRecorder;
	private static final int MIN_ALLOCATION_SIZE = 10;
	private static final int MAX_RECORD_SIZE = Integer.MAX_VALUE;
	
	private int mColNum;
	private int mRowNum;
	
	public GameDataStruct(int[][] dataArray, boolean createRecorder)
	{
		if(null == dataArray)
		{
			mDataArray = new int[clienDB.MAX_ROW_NUM][clienDB.MAX_COL_NUM];
			mColNum = clienDB.MAX_COL_NUM;
			mRowNum = clienDB.MAX_ROW_NUM;
			mIsSupportedRecorder = false;
			mOriginalDataArray = null;
			mRecordDataArray = null;
			mRecorderSize = null;
			return;
		}
		
		mColNum = dataArray[0].length;
		mRowNum = dataArray.length;
		mDataArray = new int[mRowNum][mColNum];
		
		for(int i = 0; i < mRowNum; i++)
		{
			System.arraycopy(
					dataArray[i], 0,
					mDataArray[i], 0,
					mColNum);
		}

		if(createRecorder == true)
		{
			mIsSupportedRecorder = true;
			initRecorder();
		}
		else
		{
			mIsSupportedRecorder = false;
			mOriginalDataArray = null;
			mRecordDataArray = null;
			mRecorderSize = null;
		}
	}
	public GameDataStruct(int colNum, int rowNum, int[][] dataArray, boolean createRecorder)
	{
		mColNum = colNum;
		mRowNum = rowNum;
		mDataArray = new int[mRowNum][mColNum];
		if(dataArray != null)
		{
			int copyRangeWidth = mColNum;
			int copyRangeHeight = mRowNum;
			int offsetX = 0;
			int offsetY = 0;
			if(dataArray[0].length < colNum)
			{
				copyRangeWidth = dataArray[0].length;
				offsetX = (mColNum - copyRangeWidth) >> 1;
				
			}
			if(dataArray.length < rowNum)
			{
				copyRangeHeight = dataArray.length;
				offsetY = (mRowNum - copyRangeHeight) >> 1;
			}
			for(int i = 0; i < copyRangeHeight; i++)
			{
				System.arraycopy(dataArray[i], 0, mDataArray[i + offsetY], offsetX, copyRangeWidth);
			}
		}
		if(createRecorder == true)
		{
			mIsSupportedRecorder = true;
			initRecorder();
		}
		else
		{
			mIsSupportedRecorder = false;
			mOriginalDataArray = null;
			mRecordDataArray = null;
			mRecorderSize = null;
		}
	}
	private void initRecorder()
	{
		if(null == mDataArray)
			return;

		mOriginalDataArray = new int[mRowNum][mColNum];
		for(int i = 0; i < mRowNum; i++)
		{
			System.arraycopy(
					mDataArray[i], 0,
					mOriginalDataArray[i], 0,
					mColNum);
		}
		mRecordDataArray = new int[mRowNum][mColNum][][];
		mRecorderSize = new int[mRowNum][mColNum];

		/*
		for(int j = 0; j < mColNum; j++)
		{
			for(int i = 0; i < mRowNum; i++)
			{
				mRecordDataArray[j][i] = null;
				mRecorderSize[j][i] = 0;
			}
		}
		*/
	}
	public void addObj(int[] posIndex, int objId)
	{
		if(null == posIndex)
			return;
		
		int posIndexX = posIndex[0];
		int posIndexY = posIndex[1];
		
		if(posIndexX < 0 || posIndexX >= mColNum)
			return;
		if(posIndexY < 0 || posIndexY >= mRowNum)
			return;

		switch(objId)
		{
		case clienDB.GameObjectID.WALL:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MASK_MAP_AND_SPRITE);
			mDataArray[posIndexY][posIndexX] |= DATA_FLAG.WALL;
			break;
		case clienDB.GameObjectID.PATH:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MASK_MAP_AND_SPRITE);
			//mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MASK_MAP);
			mDataArray[posIndexY][posIndexX] |= DATA_FLAG.PATH;
			break;
		case clienDB.GameObjectID.DEST:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MASK_MAP_AND_SPRITE);
			//mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MASK_MAP);
			mDataArray[posIndexY][posIndexX] |= DATA_FLAG.DEST;
			break;
		case clienDB.GameObjectID.BOX:
			if((mDataArray[posIndexY][posIndexX] & DATA_FLAG.MASK_MAP)== DATA_FLAG.DEST)
			{
				mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MASK_MAP_AND_SPRITE);
				mDataArray[posIndexY][posIndexX] |= DATA_FLAG.DEST;
			}
			else
			{
				mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MASK_MAP_AND_SPRITE);
				mDataArray[posIndexY][posIndexX] |= DATA_FLAG.PATH;
			}
			mDataArray[posIndexY][posIndexX] |= DATA_FLAG.BOX;
			break;
		case clienDB.GameObjectID.MP:
			mDataArray[posIndexY][posIndexX] |= DATA_FLAG.MP;
			break;
		case clienDB.GameObjectID.CURSOR:
			mDataArray[posIndexY][posIndexX] |= DATA_FLAG.CURSOR;
			break;
		default:
			break;
		}
	}
	public void removeObj(int[] posIndex, int objId)
	{
		if(null == posIndex)
			return;
		
		int posIndexX = posIndex[0];
		int posIndexY = posIndex[1];
		
		if(posIndexX < 0 || posIndexX >= mColNum)
			return;
		if(posIndexY < 0 || posIndexY >= mRowNum)
			return;

		switch(objId)
		{
		case clienDB.GameObjectID.WALL:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.WALL);
			break;
		case clienDB.GameObjectID.PATH:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.PATH);
			break;
		case clienDB.GameObjectID.DEST:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.DEST);
			break;
		case clienDB.GameObjectID.BOX:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.BOX);
			break;
		case clienDB.GameObjectID.MP:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MP);
			break;
		case clienDB.GameObjectID.CURSOR:
			mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.CURSOR);
			break;
		default:
			break;
		}
	}
	public boolean isExist(int[] posIndex, int objId)
	{
		boolean retVal = false;
		
		if(null == posIndex)
			return false;
		
		int posIndexX = posIndex[0];
		int posIndexY = posIndex[1];
		
		if(posIndexX < 0 || posIndexX >= mColNum)
			return false;
		if(posIndexY < 0 || posIndexY >= mRowNum)
			return false;
		
		switch(objId)
		{
		case clienDB.GameObjectID.WALL:
			if(0 != (mDataArray[posIndexY][posIndexX] & DATA_FLAG.WALL))
				retVal = true;
			break;
		case clienDB.GameObjectID.PATH:
			if(0 != (mDataArray[posIndexY][posIndexX] & DATA_FLAG.PATH))
				retVal = true;
			break;
		case clienDB.GameObjectID.DEST:
			if(0 != (mDataArray[posIndexY][posIndexX] & DATA_FLAG.DEST))
				retVal = true;
			break;
		case clienDB.GameObjectID.BOX:
			if(0 != (mDataArray[posIndexY][posIndexX] & DATA_FLAG.BOX))
				retVal = true;
			break;
		case clienDB.GameObjectID.MP:
			if(0 != (mDataArray[posIndexY][posIndexX] & DATA_FLAG.MP))
				retVal = true;
			break;
		case clienDB.GameObjectID.CURSOR:
			if(0 != (mDataArray[posIndexY][posIndexX] & DATA_FLAG.CURSOR))
				retVal = true;
			break;
		default:
			retVal = false;
			break;
		}
		return retVal;
	}
	public void clearCell(int posIndex[])
	{
		if(null == posIndex)
			return;
		
		int posIndexX = posIndex[0];
		int posIndexY = posIndex[1];
		
		if(posIndexX < 0 || posIndexX >= mColNum)
			return;
		if(posIndexY < 0 || posIndexY >= mRowNum)
			return;

		mDataArray[posIndexY][posIndexX] &= (~DATA_FLAG.MASK_MAP_AND_SPRITE);
	}
	public int[] getObjPosIndex(int objId)
	{
		int[] posIndex = new int[2];
		int i, j;
		for(j = 0; j < mRowNum; j++)
		{
			for(i = 0; i < mColNum; i++)
			{
				posIndex[0] = i;
				posIndex[1] = j;
				if(true == isExist(posIndex, objId))
				{
					return(posIndex);
				}
			}
		}
		return null;
	}
	public boolean isMissionCompleted()
	{
		boolean checkResult = true;
		boolean haveMission = false;
		int i,j;
		int missionCheckFlag = DATA_FLAG.BOX | DATA_FLAG.DEST;
		int checkedValue;
		for(j = 0; j < mRowNum; j++)
		{
			for(i = 0; i < mColNum; i++)
			{
				checkedValue = mDataArray[j][i] & missionCheckFlag;
				if(0 != checkedValue)
				{
					haveMission = true;
					if(missionCheckFlag != checkedValue)
						checkResult = false;
				}
					
			}
		}
		if(true == haveMission && true == checkResult)
			return true;
		else
			return false;
	}
	public int checkData()
	{
		int i,j;
		int configData;
		int destNum = 0;
		int boxNum = 0;
		int flag;
		int checkResult = 0;
		int missionNum = 0;
		int mainPlayerPosIndex[] = null;
		for(j = 0; j < mRowNum; j++)
		{
			for(i = 0; i< mColNum; i++)
			{
				configData = mDataArray[j][i];
				if((configData & DATA_FLAG.MASK_MAP_AND_SPRITE) == 0)
					continue;
				for(flag = 0x0001; flag != 0; flag <<= 1)
				{
					if(configData ==0)
							break;
					if((configData & flag) ==0)
							continue;
					switch(flag)
					{
					case DATA_FLAG.DEST:
						destNum ++;
						if(0 == (configData & DATA_FLAG.BOX))
							missionNum ++;
						break;
					case DATA_FLAG.BOX:
						boxNum ++;
						if(0 == (configData & DATA_FLAG.DEST))
							missionNum ++;
						break;
					case DATA_FLAG.MP:
						mainPlayerPosIndex = new int[]{i, j};
						break;
					default:
						break;
					}
				}
			}
		}
		if(null == mainPlayerPosIndex)
		{
			checkResult |= DATA_CHECK_RESULT.ERROR_NO_MP;
		}
		else
		{
			if(false == isExist(mainPlayerPosIndex, clienDB.GameObjectID.PATH) &&
				false == isExist(mainPlayerPosIndex, clienDB.GameObjectID.DEST) )
			{
				checkResult |= DATA_CHECK_RESULT.ERROR_WRONG_MP_POS;
			}
			else if(true == isExist(mainPlayerPosIndex, clienDB.GameObjectID.BOX))
			{
				checkResult |= DATA_CHECK_RESULT.ERROR_WRONG_MP_POS;
			}
		}
		if(boxNum == 0)
		{
			checkResult |= DATA_CHECK_RESULT.ERROR_NO_B0X;
		}
		else if(boxNum != destNum)
		{
			checkResult |= DATA_CHECK_RESULT.ERROR_BOX_NUM_NOT_MATCH;
		}
		else if(missionNum == 0)
		{
			checkResult |= DATA_CHECK_RESULT.ERROR_NO_MISSION;
		}
		
		return checkResult;
	}
	public int[] getEditDataRange()
	{
		int[] dataRange = new int[4];
		int startX = mColNum;
		int startY = mRowNum;
		int endX = 0;
		int endY = 0;
		int i, j, configData;
		for(j = 0; j < mRowNum; j++)
		{
			for(i = 0; i < mColNum; i++)
			{
				configData = mDataArray[j][i];
				if(0 == (configData & DATA_FLAG.MASK_MAP_AND_SPRITE))
					continue;
				if(startX > i) startX = i;
				if(startY > j) startY = j;
				if(endX < i) endX = i;
				if(endY < j) endY = j;
			}
		}
		if(startX <= endX)
		{
			dataRange[0] = startX;
			dataRange[1] = startY;
			dataRange[2] = endX;
			dataRange[3] = endY;
		}
		else
		{
			dataRange[0] = 0;
			dataRange[1] = 0;
			dataRange[2] = 0;
			dataRange[3] = 0;
		}
		return dataRange;
	}
	public int[][] getEditDataArray()
	{
		int[] range = getEditDataRange();
		int colNum = range[2] - range[0] + 1;
		int rowNum = range[3] - range[1] + 1;
		int[][] datas = new int[rowNum][colNum];
		for(int i = 0; i < rowNum; i++)
		{
			System.arraycopy(mDataArray[range[1] + i], range[0], datas[i], 0, colNum);
		}
		return datas;
	}
	private int getRecordData(int posIndexX, int posIndexY, int stepIndex)
	{
		/*
		if(posIndexX < 0 || posIndexX >= mColNum)
			return 0;
		if(posIndexY < 0 || posIndexY >= mRowNum)
			return 0;
		if(stepIndex < 0)
			return mOriginalDataArray[posIndexY][posIndexX];
		*/
		
		int configData = mOriginalDataArray[posIndexY][posIndexX];
		int[][] recorderItem = mRecordDataArray[posIndexY][posIndexX];
		int recorderSize = mRecorderSize[posIndexY][posIndexX];
		if(0 == recorderSize)
			return configData;
		
		int index;
		for(index = recorderSize - 1; index >= 0; index--)
		{
			if(stepIndex >= recorderItem[index][0])
				break;
		}
		if(index < 0)
			return configData;
		else
			return recorderItem[index][1];
	}
	private void saveRecordData(int posIndexX, int posIndexY, int stepIndex)
	{
		/*
		if(posIndexX < 0 || posIndexX >= mColNum)
			return;
		if(posIndexY < 0 || posIndexY >= mRowNum)
			return;
			
		if(stepIndex < 0)
			return;
		*/

		int configData = mDataArray[posIndexY][posIndexX];
		int recorderSize = mRecorderSize[posIndexY][posIndexX];

		if(0 == recorderSize)
		{
			if(configData == mOriginalDataArray[posIndexY][posIndexX])
				return;
			
			//showAllocationSize(posIndexX, posIndexY, MIN_ALLOCATION_SIZE);
			mRecordDataArray[posIndexY][posIndexX] = new int[MIN_ALLOCATION_SIZE][];
			
			//showAddedRecordData(posIndexX, posIndexY, 0, stepIndex, configData);
			mRecordDataArray[posIndexY][posIndexX][0] = new int[] {stepIndex, configData};
			mRecorderSize[posIndexY][posIndexX] = 1;
			
			return;
		}
		
		int[][] recorderItem = mRecordDataArray[posIndexY][posIndexX];
		int index;
		for(index = recorderSize - 1; index >= 0; index--)
		{
			if(stepIndex > recorderItem[index][0])
				break;
		}
		index ++;
		if(0 == index)
		{
			if(configData == mOriginalDataArray[posIndexY][posIndexX])
			{
				mRecorderSize[posIndexY][posIndexX] = 0;
			}
			else
			{
				//showAddedRecordData(posIndexX, posIndexY, index, stepIndex, configData);
				mRecordDataArray[posIndexY][posIndexX][0] = new int[] {stepIndex, configData};
				mRecorderSize[posIndexY][posIndexX] = 1;
			}

		}
		else if(index < recorderItem.length)
		{
			if(configData == recorderItem[index - 1][1])
			{
				mRecorderSize[posIndexY][posIndexX] = index;
				//showAddedRecordData(posIndexX, posIndexY, index, stepIndex, configData);
			}
			else
			{
				//showAddedRecordData(posIndexX, posIndexY, index, stepIndex, configData);
				mRecordDataArray[posIndexY][posIndexX][index] = new int[] {stepIndex, configData};
				mRecorderSize[posIndexY][posIndexX] = index + 1;
			}
		}
		else
		{
			if(recorderSize + MIN_ALLOCATION_SIZE > MAX_RECORD_SIZE)
			{
				//Log.i(clienDB.LOG_TAG, "Waring, recorder size overflow");
				initRecorder();
				return;
			}
			else
			{
				//showAllocationSize(posIndexX, posIndexY, recorderSize + MIN_ALLOCATION_SIZE);
				int[][] newItem = new int[recorderSize + MIN_ALLOCATION_SIZE][2];
				System.arraycopy(recorderItem, 0, newItem, 0, recorderSize);
				mRecordDataArray[posIndexY][posIndexX] = newItem;
			}
			//showAddedRecordData(posIndexX, posIndexY, index, stepIndex, configData);
			mRecordDataArray[posIndexY][posIndexX][index] = new int[] {stepIndex, configData};
			mRecorderSize[posIndexY][posIndexX] = index + 1;
		}
	}
	public void recordData(int stepIndex)
	{
		if(false == mIsSupportedRecorder)
		{
			return;
		}
		int recordMask = DATA_FLAG.PATH | DATA_FLAG.DEST; 
		
		int i, j;
		for(j = 0; j < mRowNum; j++)
		{
			for(i = 0; i < mColNum; i++)
			{
				if(0 == (mDataArray[j][i] & recordMask))
					continue;
				saveRecordData(i, j, stepIndex);
			}
		}
	}
	public void gotoStep(int stepIndex)
	{
		if(false == mIsSupportedRecorder)
		{
			return;
		}
		int recordMask = DATA_FLAG.PATH | DATA_FLAG.DEST; 
		int i, j;
		for(j = 0; j < mRowNum; j++)
		{
			for(i = 0; i < mColNum; i++)
			{
				if(0 == (mDataArray[j][i] & recordMask))
					continue;
				mDataArray[j][i] = getRecordData(i, j, stepIndex);
			}
		}
	}
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		mDataArray = null;
		mOriginalDataArray = null;
		mRecordDataArray = null;
		mRecorderSize = null;
	}
	/*
	private void showAllocationSize(int posIndexX, int posIndexY, int recorderSize)
	{
		Log.i(clienDB.LOG_TAG, "allocate new size: ("
				+ posIndexY + ","
				+ posIndexX + ")"
				+ " (" + recorderSize + ")");
	}
	private void showAddedRecordData(int posIndexX, int posIndexY, int index, int stepIndex, int configData)
	{
		Log.i(clienDB.LOG_TAG,"add record: ("
				+ posIndexY + ","
				+ posIndexX + ","
				+ index + ")"
				+ "(" + stepIndex + ":" + GameDataSaver.configDataToString(configData,false) + ")");
	}
	*/
}
