package com.android.GameView;

import com.android.GameData.GameDataStruct;
import com.android.GeneralDesign.BitmapProvider;
import com.android.GeneralDesign.LayoutDesign;
import com.android.GeneralDesign.clienDB;
import com.android.Sokoban.MainGame;
import com.android.Sokoban.Sokoban;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameContainer extends GameDisplayItem
{
	public static final class CursorType
	{
		public static final int NULL = 0;
		public static final int WALL = 1;
		public static final int PATH = 2;
		public static final int DEST = 3;
		public static final int BOX = 4;
		public static final int MP = 6;
		public static final int CLEA = 7;
	}
	public static final class WorkMode
	{
		public static final int EDIT_MODE = 1;
		public static final int PLAY_MODE = 2;
	}
	public static final class CursorLockState
	{
		public static final int LOCKED = 1;
		public static final int UNLOCK = 2;
	}
	public static final class MoveDir
	{
		public static final int UP = 1;
		public static final int DOWN = 2;
		public static final int LEFT = 3;
		public static final int RIGHT = 4;
	}
	public static final class OptionType
	{
		public static final int NOP = 0;
		public static final int SET_CURSOR_TYPE = 1;
		public static final int SET_WORK_MODE = 2;
		public static final int SET_LOCK_STATE = 3;
		public static final int PLACE_OBJ = 4;
		public static final int REMOVE_OBJ = 5;
		public static final int MOVE_MP = 6;
		public static final int MOVE_CURSOR = 7;
		public static final int CLEAR_CELL = 8;
		public static final int CHECK_GAME_DATA = 9;
		public static final int SET_MP_ANIMATION = 10;
		public static final int CHECK_MISSION = 11;
		public static final int RECORD_STEP = 12;
		public static final int GOTO_STEP = 13;
	}
	public static final class MPAnimation
	{
		public static final int ANIMATION_TYPE_MOVE_UP = 0;
		public static final int ANIMATION_TYPE_MOVE_DOWN = 1;
		public static final int ANIMATION_TYPE_MOVE_LEFT = 2;
		public static final int ANIMATION_TYPE_MOVE_RIGHT = 3;
		public static final int ANIMATION_NUM = 4;
		public static final int ANIMATION_FRAME_NUM = 2;
	}
	public static final class ActionRusult
	{
		public static final int SUCCESS = 0;
		public static final int FAILED =1;
		public static final int TRUE = 0;
		public static final int FALSE = 1;
	}
	
	private int mTiledSize; 
	private int mColNum;
	private int mRowNum;
	
	private Bitmap mBitmapWall;
	private Bitmap mBitmapPath;
	private Bitmap mBitmapDest;
	private Bitmap mBitmapBoxA;
	private Bitmap mBitmapBoxB;
	private Bitmap mBitmapClear;
	private Bitmap mBitmapCursor;
	private Bitmap mBitmapLock;
	private Bitmap[][] mBitmapMPFrames;
	private Bitmap mBitmapMainPlayer;
	
	private GameDataStruct mGameDataStruct;
	private int mWorkMode;
	private int mCursorLockState;
	
	private int mMPAniMPType;
	private int mMPAniMPFrameIndex;
	private int mMPAniTick;
	
	private Paint mPaint = new Paint();

	public GameContainer(
			Context context,
			Sokoban sokoban,
			MainGame mainGame,
			int[][] mapData,
			int workMode)
	{
		super(mainGame, LayoutDesign.DisplayItemID.GAME_CONTAINER);
		mWorkMode = workMode;
		loadGameData(mapData);
	}
	private void setBitmap()
	{
		mBitmapWall = BitmapProvider.getBitmap(
				BitmapProvider.BitmapID.WALL,
				mTiledSize,
				mTiledSize);
		mBitmapPath = BitmapProvider.getBitmap(
				BitmapProvider.BitmapID.PATH,
				mTiledSize,
				mTiledSize);
		mBitmapDest = BitmapProvider.getBitmap(
				BitmapProvider.BitmapID.DEST,
				mTiledSize,
				mTiledSize);
		mBitmapBoxA = BitmapProvider.getBitmap(
				BitmapProvider.BitmapID.BOXA,
				mTiledSize,
				mTiledSize);
		mBitmapBoxB = BitmapProvider.getBitmap(
				BitmapProvider.BitmapID.BOXB,
				mTiledSize,
				mTiledSize);
		mBitmapClear = BitmapProvider.getBitmap(
				BitmapProvider.BitmapID.CLEAR,
				mTiledSize,
				mTiledSize);
		mBitmapLock = BitmapProvider.getBitmap(
				BitmapProvider.BitmapID.LOCK,
				mTiledSize >> 1,
				mTiledSize >> 1);
		
		mBitmapMPFrames = new Bitmap[MPAnimation.ANIMATION_NUM][MPAnimation.ANIMATION_FRAME_NUM];
		int[][] mBitmapMPFrameIds = new int[][]
  		{
  			{	
  				BitmapProvider.BitmapID.MP_1_1, 
  				BitmapProvider.BitmapID.MP_1_2
  			},
  			{
  				BitmapProvider.BitmapID.MP_2_1,
  				BitmapProvider.BitmapID.MP_2_2
  			},
  			{
  				BitmapProvider.BitmapID.MP_3_1,
  				BitmapProvider.BitmapID.MP_3_2
  			},
  			{
  				BitmapProvider.BitmapID.MP_4_1,
  				BitmapProvider.BitmapID.MP_4_2
  			}
  		};
		for(int i = 0; i < MPAnimation.ANIMATION_NUM; i++)
		{
			for(int j = 0; j < MPAnimation.ANIMATION_FRAME_NUM; j++)
			{
				mBitmapMPFrames[i][j] = BitmapProvider.getBitmap(
						mBitmapMPFrameIds[i][j],
						mTiledSize,
						mTiledSize);
			}
		}
		mBitmapMainPlayer = mBitmapMPFrames[0][0];
	}
	public void loadGameData(int[][] mapData)
	{
		if(WorkMode.EDIT_MODE == mWorkMode)
		{
			mColNum = clienDB.MAX_COL_NUM;
			mRowNum = clienDB.MAX_ROW_NUM;
			mGameDataStruct = new GameDataStruct(
					mColNum, mRowNum, mapData, false);
			placeObj(clienDB.GameObjectID.CURSOR);
		}
		else
		{
			if(null == mapData)
			{
				mColNum = clienDB.DEFAULT_COL_NUM;
				mRowNum = clienDB.DEFAULT_ROW_NUM;
				mGameDataStruct = new GameDataStruct(
						new int[mRowNum][mColNum], true);
			}
			else
			{
				mColNum = mapData[0].length;
				mRowNum = mapData.length;
				mGameDataStruct = new GameDataStruct(mapData, true);
			}
		}
		setDisplayRectSize();
		updataDisplaySetting();
		setBitmap();
	}
	private int calculateTiledSize()
	{
		if(mColNum == 0 || 0 == mRowNum)
			return 0;
		int displayColNum = mColNum;
		int displayRowNum = mRowNum;
		if(displayColNum < clienDB.DEFAULT_COL_NUM)
			displayColNum = clienDB.DEFAULT_COL_NUM;
		if(displayRowNum < clienDB.DEFAULT_ROW_NUM)
			displayRowNum = clienDB.DEFAULT_ROW_NUM;

		int tiledSize;
		tiledSize = mDisWinWidth / displayColNum;
		if(tiledSize * displayColNum == mDisWinWidth)
			tiledSize --;
		if(tiledSize * displayRowNum >= mDisWinHeight)
			tiledSize = mDisWinHeight / displayRowNum;
		if(tiledSize * displayRowNum == mDisWinHeight)
			tiledSize --;
		return tiledSize;
	}
	public int[][] getEditData()
	{
		return mGameDataStruct.getEditDataArray();
	}
	public void setCursorType(int cursorType)
	{
		switch(cursorType)
		{
		case CursorType.NULL:
			mBitmapCursor = null;
			break;
		case CursorType.WALL:
			mBitmapCursor = mBitmapWall;
			break;
		case CursorType.PATH:
			mBitmapCursor = mBitmapPath;
			break;
		case CursorType.DEST:
			mBitmapCursor = mBitmapDest;
			break;
		case CursorType.BOX:
			mBitmapCursor = mBitmapBoxA;
			break;
		case CursorType.MP:
			mBitmapCursor = mBitmapMainPlayer;
			break;
		case CursorType.CLEA:
			mBitmapCursor = mBitmapClear;
			break;
		default:
			break;
		}
	}
	public void setWorkMode(int workMode)
	{
		mWorkMode = workMode;
	}
	public void setCursorLockState(int lockState)
	{
		mCursorLockState = lockState;
	}
	public void placeObj(int objId)
	{
		int[] cursorPosIndex =  
			mGameDataStruct.getObjPosIndex(clienDB.GameObjectID.CURSOR);
		switch(objId)
		{
		case clienDB.GameObjectID.WALL:
			if(null != cursorPosIndex)
				mGameDataStruct.addObj(cursorPosIndex, clienDB.GameObjectID.WALL);
			break;
		case clienDB.GameObjectID.PATH:
			if(null != cursorPosIndex)
				mGameDataStruct.addObj(cursorPosIndex, clienDB.GameObjectID.PATH);
			break;
		case clienDB.GameObjectID.DEST:
			if(null != cursorPosIndex)
				mGameDataStruct.addObj(cursorPosIndex, clienDB.GameObjectID.DEST);
			break;
		case clienDB.GameObjectID.BOX:
			if(null != cursorPosIndex)
				mGameDataStruct.addObj(cursorPosIndex, clienDB.GameObjectID.BOX);
			break;
		case clienDB.GameObjectID.MP:
			if(null == cursorPosIndex)
				break;
			int[] mainPlayerPosIndex =
				mGameDataStruct.getObjPosIndex(clienDB.GameObjectID.MP);
			if(null == mainPlayerPosIndex)
				mGameDataStruct.addObj(cursorPosIndex, clienDB.GameObjectID.MP);
			else
				moveObj(clienDB.GameObjectID.MP, mainPlayerPosIndex, cursorPosIndex);
		case clienDB.GameObjectID.CURSOR:
			if(null == cursorPosIndex)
				mGameDataStruct.addObj(new int[]{0, 0}, clienDB.GameObjectID.CURSOR);
			break;
		}
		int[] cursorIndexPos = mGameDataStruct.getObjPosIndex(clienDB.GameObjectID.CURSOR);
		if(null == cursorIndexPos)
		{
			return;
		}
	}
	public void removeObj(int objId)
	{
		int[] cursorPosIndex =
			mGameDataStruct.getObjPosIndex(clienDB.GameObjectID.CURSOR);
		if(null != cursorPosIndex)
			mGameDataStruct.removeObj(cursorPosIndex, objId);
	}
	public int[] getDestPosIndex(int[] curPosIndex, int moveDir)
	{
		if(null == curPosIndex)
			return null;
		
		int destPosIndex[] = new int[2];
		destPosIndex[0] = curPosIndex[0];
		destPosIndex[1] = curPosIndex[1];
		
		switch(moveDir)
		{
		case MoveDir.UP:
			if(curPosIndex[1] > 0)
				destPosIndex[1]--;
			break;
		case MoveDir.DOWN:
			if(curPosIndex[1] < mRowNum - 1)
				destPosIndex[1]++;
			break;
		case MoveDir.LEFT:
			if(curPosIndex[0] > 0)
				destPosIndex[0]--;
			break;
		case MoveDir.RIGHT:
			if(curPosIndex[0] < mColNum - 1)
				destPosIndex[0]++;
			break;
		default:
			break;
		}
		return destPosIndex;
	}
	public void moveObj(int objId, int[] fromPosIndex, int[] toPosIndex)
	{
		mGameDataStruct.removeObj(fromPosIndex, objId);
		mGameDataStruct.addObj(toPosIndex, objId);
	}
	public boolean moveMainPlayer(int moveDir)
	{
		int[] fromPosIndex = mGameDataStruct.getObjPosIndex(clienDB.GameObjectID.MP);
		int[] toMPPosIndex = getDestPosIndex(fromPosIndex, moveDir);
		
		if(null == fromPosIndex ||
			toMPPosIndex == null ||
			toMPPosIndex == fromPosIndex)
			return false;

		if(true == mGameDataStruct.isExist(toMPPosIndex, clienDB.GameObjectID.BOX))
		{
			int[] toBoxPosIndex = getDestPosIndex(toMPPosIndex, moveDir);
			if(null == toBoxPosIndex||
				toBoxPosIndex == toMPPosIndex)
			{
				return false;
			}
			if( true == mGameDataStruct.isExist(toBoxPosIndex, clienDB.GameObjectID.BOX) ||
				(false == mGameDataStruct.isExist(toBoxPosIndex, clienDB.GameObjectID.PATH) &&
				false == mGameDataStruct.isExist(toBoxPosIndex, clienDB.GameObjectID.DEST)))
			{
				return false;
			}
			moveObj(clienDB.GameObjectID.BOX, toMPPosIndex, toBoxPosIndex);
			moveObj(clienDB.GameObjectID.MP, fromPosIndex, toMPPosIndex);
		}
		else
		{
			if(false == mGameDataStruct.isExist(toMPPosIndex, clienDB.GameObjectID.PATH) &&
				false == mGameDataStruct.isExist(toMPPosIndex, clienDB.GameObjectID.DEST))
			{
				return false;
			}
			moveObj(clienDB.GameObjectID.MP, fromPosIndex, toMPPosIndex);
		}
		return true;
	}
	public boolean moveCursor(int moveDir)
	{
		int[] fromPosIndex = mGameDataStruct.getObjPosIndex(clienDB.GameObjectID.CURSOR);
		int[] toPosIndex = getDestPosIndex(fromPosIndex, moveDir);
		return moveCursor(fromPosIndex, toPosIndex);
	}
	public boolean moveCursor(int[] toPosIndex)
	{
		int[] fromPosIndex = mGameDataStruct.getObjPosIndex(clienDB.GameObjectID.CURSOR);
		return moveCursor(fromPosIndex, toPosIndex);
	}
	public boolean moveCursor(int[] fromPosIndex, int[] toPosIndex)
	{
		if(null == fromPosIndex ||
			null == toPosIndex ||
			fromPosIndex == toPosIndex)
		{
			return false;
		}
		moveObj(clienDB.GameObjectID.CURSOR, fromPosIndex, toPosIndex);
		return true;
	}
	public void clearCell()
	{
		int[] cursorPosIndex = 
			mGameDataStruct.getObjPosIndex(
					clienDB.GameObjectID.CURSOR);
		if(null == cursorPosIndex)
			return;
		mGameDataStruct.clearCell(cursorPosIndex);
	}
	public int checkGameData()
	{
		return mGameDataStruct.checkData();
	}
	public void setMPAnimation(int animationType)
	{
		mMPAniMPType = animationType % MPAnimation.ANIMATION_NUM;
		mMPAniMPFrameIndex = 0;
		mMPAniTick = 0;
	}
	public boolean checkMission()
	{
		if(true == mGameDataStruct.isMissionCompleted())
			return true;
		else
			return false;
	}
	public void recordStep(int stepIndex)
	{
		mGameDataStruct.recordData(stepIndex);
	}
	public void gotoStep(int stepIndex)
	{
		mGameDataStruct.gotoStep(stepIndex);
	}
	public int[] getTiledPosIndex(int x, int y)
	{
		int destRectX, destRectY;
		destRectX = mDisWinX + mOffsetX;
		destRectY = mDisWinY + mOffsetY;
		if(x < destRectX || x >= destRectX + mTiledSize * mColNum ||
			y < destRectY || y >= destRectY + mTiledSize * mRowNum)
		{
			return null;
		}
		int[] posIndex = new int[2];
		posIndex[0] = (x - destRectX) / mTiledSize;
		posIndex[1] = (y - destRectY) / mTiledSize;
		return posIndex;
	}
	public int[] getTiledPosIndex(int objId)
	{
		int[] tiledPosIndex = 
			mGameDataStruct.getObjPosIndex(clienDB.GameObjectID.MP);
		return tiledPosIndex;
	}
	public Rect getTiledRect(int[] posIndex)
	{
		if(posIndex[0] < 0 ||
			posIndex[1] < 0)
		{
			return null;
		}
		else
		{
			int left = mDisWinX + mOffsetX + mTiledSize * posIndex[0];
			int top = mDisWinY + mOffsetY + mTiledSize * posIndex[1];
			int right = left + mTiledSize;
			int bottom = top + mTiledSize;
			return(new Rect(left, top, right, bottom));
		}
	}
	public void setDisplayRectSize()
	{
		mTiledSize = calculateTiledSize();
		mDisRectWidth = mTiledSize * mColNum;
		mDisRectHeight = mTiledSize * mRowNum;
	}
	@Override
	public void refurbish()
	{
		mMPAniTick ++;
		if(mMPAniTick >= clienDB.MP_ANIMAITON_FRAME_PERIOD)
		{
			mMPAniTick = 0;
			mMPAniMPFrameIndex ++;
			if(mMPAniMPFrameIndex >= MPAnimation.ANIMATION_FRAME_NUM)
				mMPAniMPFrameIndex = 0;
		}
		super.refurbish();
	}
	@Override
	protected void onDisRectSizeChange(
			int preDisRectWidth, int preDisRectHeight,
			int newDisRectWidth, int newDisRectHeight)
	{
		setBitmap();
		setMPAnimation(MPAnimation.ANIMATION_TYPE_MOVE_UP);
	}
	private void drawCursor(Canvas canvas, float x, float y)
	{
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
		mPaint.setColor(Color.GREEN);
		if(mBitmapCursor != null)
		{
			mPaint.setAlpha(0x80);
			canvas.drawBitmap(mBitmapCursor, x, y, mPaint);
			if(CursorLockState.LOCKED == mCursorLockState)
			{
				mPaint.setAlpha(0xff);
				canvas.drawBitmap(mBitmapLock, x, y, mPaint);
			}
		}
		mPaint.setAlpha(0xff);
		canvas.drawRect(
				x + 1, y + 1,
				x + mTiledSize - 1, y + mTiledSize - 1,
				mPaint);
	}
	private void drawGrid(Canvas canvas)
	{
		int i;
		int x, y;
		mPaint.setColor(Color.GRAY);
		mPaint.setStrokeWidth(1);
		x = 0;
		for(i = 0; i <= mColNum; i++)
		{
			canvas.drawLine(
					x, 0,
					x, mDisRectHeight,
					mPaint);
			x += mTiledSize;
		}
		y = 0;
		for(i = 0; i <= mRowNum; i++)
		{
			canvas.drawLine(
					0,y,
					mDisRectWidth, y,
					mPaint);
			y += mTiledSize;
		}
	}
	private void drawGameObj(Canvas canvas)
	{
		int i, j;
		int configData, flag;
		float x , y;
		y = 0.0f;

		if(null == mGameDataStruct)
			return;

		for(j = 0; j < mRowNum; j++, y += mTiledSize)
		{
			x = 0.0f;
			for(i = 0; i < mColNum; i++, x += mTiledSize)
			{
				configData = mGameDataStruct.mDataArray[j][i];
				if(0 == (configData & GameDataStruct.DATA_FLAG.MASK_ALL))
					continue;
				for(flag = 0x0001; flag != 0; flag <<= 1)
				{
					if(0 == configData)
						break;
					if(0 == (configData & flag))
						continue;
					switch(flag)
					{
					case GameDataStruct.DATA_FLAG.WALL:
						canvas.drawBitmap(mBitmapWall, x, y, mPaint);
						break;
					case GameDataStruct.DATA_FLAG.PATH:
						if(0 == (configData & GameDataStruct.DATA_FLAG.BOX))
						{
							canvas.drawBitmap(mBitmapPath, x, y, mPaint);
						}
						else
						{
							canvas.drawBitmap(mBitmapBoxA, x, y, mPaint);
							configData &= (~GameDataStruct.DATA_FLAG.BOX);
						}
						break;
					case GameDataStruct.DATA_FLAG.DEST:
						if(0 == (configData & GameDataStruct.DATA_FLAG.BOX))
						{
							canvas.drawBitmap(mBitmapDest, x, y, mPaint);
						}
						else
						{
							canvas.drawBitmap(mBitmapBoxB, x, y, mPaint);
							configData &= (~GameDataStruct.DATA_FLAG.BOX);
						}
						break;
					case GameDataStruct.DATA_FLAG.BOX:
						canvas.drawBitmap(mBitmapBoxA, x, y, mPaint);
						break;
					case GameDataStruct.DATA_FLAG.MP:
						canvas.drawBitmap(
								mBitmapMPFrames[mMPAniMPType][mMPAniMPFrameIndex],
								x, y,
								mPaint);
						break;
					case GameDataStruct.DATA_FLAG.CURSOR:
						drawCursor(canvas, x, y);
						break;
					default:
						break;
					}
					configData &= (~flag);
				}
				mPaint.setColor(Color.BLACK);
			}
		}
	}
	@Override
	protected void drawDisplayArea(Canvas canvas)
	{
		if(WorkMode.EDIT_MODE == mWorkMode)
		{
			drawGrid(canvas);
		}
		drawGameObj(canvas);
	}
}