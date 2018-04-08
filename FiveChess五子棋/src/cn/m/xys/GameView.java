package cn.m.xys;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Administrator
 * 
 */

public class GameView extends SurfaceView implements Const, SurfaceHolder.Callback, Runnable
{

	static GameView sInstance = null;

	public static void init(Activity mActivity, int screenWidth, int screenHeight)
	{
		sInstance = new GameView(mActivity, screenWidth, screenHeight);
	}

	public static GameView getInstance()
	{
		return sInstance;
	}

	// 控制循环
	boolean mbLoop = false;

	// 定义SurfaceHolder对象
	SurfaceHolder mSurfaceHolder = null;

	public static Paint sPaint = null;
	public static Canvas sCanvas = null;
	public static Resources sResources = null;
	private int mGameState = 0;

	private int mScreenWidth = 0;
	private int mScreenHeight = 0;

	public int[][] mGameMap = null;
	private int mMapHeightLengh = 0;
	private int mMapWidthLengh = 0;

	private int mMapIndexX = 0;
	private int mMapIndexY = 0;

	public int mCampTurn = 0;
	public int mCampWinner = 0;

	private float mTitleSpace = 0;
	private int mTitleHeight = 0;

	private float mTitleIndex_x = 0;
	private float mTitleIndex_y = 0;

	Bitmap bitmapBg = null;
	Bitmap mBlack = null;
	Bitmap mWhite = null;

	Context mContext = null;

	public GameView(Activity activity, int screenWidth, int screenHeight)
	{
		super(activity);
		sPaint = new Paint();
		sPaint.setAntiAlias(true);
		sResources = getResources();
		mContext = activity;
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		setFocusable(true);
		mbLoop = true;
		bitmapBg = CreatMatrixBitmap(R.drawable.status, mScreenWidth, mScreenHeight);
		mBlack = BitmapFactory.decodeResource(GameView.sResources, R.drawable.ai);
		mWhite = BitmapFactory.decodeResource(GameView.sResources, R.drawable.human);
		mTitleSpace = (float) mScreenWidth / CHESS_WIDTH;
		mTitleHeight = mScreenHeight / 3;
		mTitleIndex_x = (float) (mTitleSpace / 2);
		mTitleIndex_y = (float) (mTitleSpace / 2);
		setGameState(GS_GAME);
	}

	public void setGameState(int newState)
	{
		mGameState = newState;
		switch (mGameState)
		{
		case GS_GAME:
			mGameMap = new int[CHESS_HEIGHT][CHESS_WIDTH];
			mMapHeightLengh = mGameMap.length;
			mMapWidthLengh = mGameMap[0].length;
			mCampTurn = CAMP_HERO;
			break;
		}
	}

	protected void Draw()
	{
		sCanvas = mSurfaceHolder.lockCanvas();
		if (mSurfaceHolder == null || sCanvas == null)
		{
			return;
		}
		RenderGame();
		mSurfaceHolder.unlockCanvasAndPost(sCanvas);
	}

	private void RenderGame()
	{
		switch (mGameState)
		{
		case GS_GAME:
			DrawRect(Color.WHITE, 0, 0, mScreenWidth, mScreenHeight);
			RenderMap();
			break;
		case GS_END:
			DrawRect(Color.RED, 0, 0, mScreenWidth, mScreenHeight);
			DrawString(Color.WHITE, sResources.getString(mCampWinner) + "胜利 点击继续游戏", 50, 50);
			break;
		}

	}

	private void RenderMap()
	{
		int i, j;
		DrawImage(bitmapBg, 0, 0, 0);

		for (i = 0; i < mMapHeightLengh; i++)
		{
			for (j = 0; j < mMapWidthLengh; j++)
			{
				int CampID = mGameMap[i][j];
				float x = (j * mTitleSpace) + mTitleIndex_x;
				float y = (i * mTitleSpace) + mTitleHeight + mTitleIndex_y;
				
				if (CampID == CAMP_HERO)
				{
					DrawImage(mBlack, x, y, ALIGN_VCENTER | ALIGN_HCENTER);
				} 
				else if (CampID == CAMP_ENEMY)
				{
					DrawImage(mWhite, x, y, ALIGN_VCENTER | ALIGN_HCENTER);
				}
			}
		}

	}

	private void DrawRect(int color, int x, int y, int width, int height)
	{
		sPaint.setColor(color);
		sCanvas.clipRect(x, y, width, height);
		sCanvas.drawRect(x, y, width, height, sPaint);
	}

	private void DrawString(int color, String str, int x, int y)
	{
		sPaint.setColor(color);
		sCanvas.drawText(str, x, y, sPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			UpdateTouchEvent(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return super.onTouchEvent(event);
	}

	public boolean CheckPiecesMeet(int Camp)
	{
		int MeetCount = 0;
		// 横向
		for (int i = 0; i < CALU_ALL_COUNT; i++)
		{
			int index = mMapIndexX - CALU_SINGLE_COUNT + i;
			if (index < 0 || index >= mMapWidthLengh)
			{
				if (MeetCount == CALU_SINGLE_COUNT)
				{
					return true;
				}
				MeetCount = 0;
				continue;
			}
			if (mGameMap[mMapIndexY][index] == Camp)
			{
				MeetCount++;
				if (MeetCount == CALU_SINGLE_COUNT)
				{
					return true;
				}
			} else
			{
				MeetCount = 0;
			}
		}
		// 纵向
		MeetCount = 0;
		for (int i = 0; i < CALU_ALL_COUNT; i++)
		{
			int index = mMapIndexY - CALU_SINGLE_COUNT + i;
			if (index < 0 || index >= mMapHeightLengh)
			{
				if (MeetCount == CALU_SINGLE_COUNT)
				{
					return true;
				}
				MeetCount = 0;
				continue;
			}
			if (mGameMap[index][mMapIndexX] == Camp)
			{
				MeetCount++;
				if (MeetCount == CALU_SINGLE_COUNT)
				{
					return true;
				}
			} else
			{
				MeetCount = 0;
			}
		}

		// 右斜
		MeetCount = 0;
		for (int i = 0; i < CALU_ALL_COUNT; i++)
		{
			int indexX = mMapIndexX - CALU_SINGLE_COUNT + i;
			int indexY = mMapIndexY - CALU_SINGLE_COUNT + i;
			if ((indexX < 0 || indexX >= mMapWidthLengh) || (indexY < 0 || indexY >= mMapHeightLengh))
			{
				if (MeetCount == CALU_SINGLE_COUNT)
				{
					return true;
				}
				MeetCount = 0;
				continue;
			}
			if (mGameMap[indexY][indexX] == Camp)
			{
				MeetCount++;
				if (MeetCount == CALU_SINGLE_COUNT)
				{
					return true;
				}
			} else
			{
				MeetCount = 0;
			}
		}

		// 左斜
		MeetCount = 0;
		for (int i = 0; i < CALU_ALL_COUNT; i++)
		{
			int indexX = mMapIndexX - CALU_SINGLE_COUNT + i;
			int indexY = mMapIndexY + CALU_SINGLE_COUNT - i;
			if ((indexX < 0 || indexX >= mMapWidthLengh) || (indexY < 0 || indexY >= mMapHeightLengh))
			{
				if (MeetCount == CALU_SINGLE_COUNT)
				{
					return true;
				}
				MeetCount = 0;
				continue;
			}
			if (mGameMap[indexY][indexX] == Camp)
			{
				MeetCount++;
				if (MeetCount == CALU_SINGLE_COUNT)
				{
					return true;
				}
			} else
			{
				MeetCount = 0;
			}
		}
		return false;
	}

	private void UpdateTouchEvent(int x, int y)
	{
		switch (mGameState)
		{
		case GS_GAME:
			if (x > 0 && y > mTitleHeight)
			{
				mMapIndexX = (int) (x / mTitleSpace);
				mMapIndexY = (int) ((y - mTitleHeight) / mTitleSpace);

				if (mMapIndexX > mMapWidthLengh)
				{
					mMapIndexX = mMapWidthLengh;
				}
				if (mMapIndexX < 0)
				{
					mMapIndexX = 0;
				}

				if (mMapIndexY > mMapHeightLengh)
				{
					mMapIndexY = mMapHeightLengh;
				}

				if (mMapIndexY < 0)
				{
					mMapIndexY = 0;
				}
				if (mGameMap[mMapIndexY][mMapIndexX] == CAMP_DEFAULT)
				{

					if (mCampTurn == CAMP_HERO)
					{
						mGameMap[mMapIndexY][mMapIndexX] = CAMP_HERO;
						if (CheckPiecesMeet(CAMP_HERO))
						{
							mCampWinner = R.string.Role_black;
							setGameState(GS_END);
						}
						else
						{
							mCampTurn = CAMP_ENEMY;
						}

					} 
					else
					{
						mGameMap[mMapIndexY][mMapIndexX] = CAMP_ENEMY;
						if (CheckPiecesMeet(CAMP_ENEMY))
						{
							mCampWinner = R.string.Role_white;
							setGameState(GS_END);
						} else
						{
							mCampTurn = CAMP_HERO;
						}
					}
				}
			}
			break;
		case GS_END:
			setGameState(GS_GAME);
			break;

		}
	}

	public boolean isCheckInvite(String body)
	{
		if (body.indexOf("invite") >= 0)
		{
			if (mGameState != GS_INVITING && mGameState != GS_COMFIRE && mGameState != GS_GAME)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 创建一个缩小或放大的新图片
	 * 
	 * @param resourcesID
	 * @param scr_width
	 * @param res_height
	 * @return
	 */
	private Bitmap CreatMatrixBitmap(int resourcesID, float scr_width, float res_height)
	{
		Bitmap bitMap = null;
		bitMap = BitmapFactory.decodeResource(sResources, resourcesID);
		int bitWidth = bitMap.getWidth();
		int bitHeight = bitMap.getHeight();
		float scaleWidth = scr_width / (float) bitWidth;
		float scaleHeight = res_height / (float) bitHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, bitWidth, bitHeight, matrix, true);
		return bitMap;
	}

	/**
	 * 绘制一个字符串
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param anchor
	 * @param Canvas
	 * @param paint
	 */
	private void DrawString(int color, String text, int x, int y, int anchor)
	{
		Rect rect = new Rect();
		sPaint.getTextBounds(text, 0, text.length(), rect);
		int w = rect.width();
		int h = rect.height();
		int tx = 0;
		int ty = 0;
		if ((anchor & ALIGN_RIGHT) != 0)
		{
			tx = x - w;
		} 
		else if ((anchor & ALIGN_HCENTER) != 0)
		{
			tx = x - (w >> 1);
		}
		else
		{
			tx = x;
		}
		
		if ((anchor & ALIGN_TOP) != 0)
		{
			ty = y + h;
		} 
		else if ((anchor & ALIGN_VCENTER) != 0)
		{
			ty = y + (h >> 1);
		}
		else
		{
			ty = y;
		}
		sPaint.setColor(color);
		sCanvas.drawText(text, tx, ty, sPaint);
	}

	/**
	 * 绘制一张图片可以选择图片的锚点位置
	 * 
	 * @param canvas
	 * @param paint
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param angle
	 */
	private void DrawImage(Bitmap bitmap, float x, float y, int anchor)
	{
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		float tx = 0;
		float ty = 0;
		if ((anchor & ALIGN_RIGHT) != 0)
		{
			tx = x - w;
		} 
		else if ((anchor & ALIGN_HCENTER) != 0)
		{
			tx = x - (w >> 1);
		} 
		else
		{
			tx = x;
		}
		
		if((anchor & ALIGN_TOP) != 0)
		{
			ty = y + h;
		} 
		else if ((anchor & ALIGN_VCENTER) != 0)
		{
			ty = y - (h >> 1);
		} 
		else if ((anchor & ALIGN_BOTTOM) != 0)
		{
			ty = y - h;
		} 
		else
		{
			ty = y;
		}

		sCanvas.drawBitmap(bitmap, tx, ty, sPaint);
	}

	//@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
		// TODO Auto-generated method stub

	}

	//@Override
	public void surfaceCreated(SurfaceHolder arg0)
	{
		new Thread(this).start();
	}

	//@Override
	public void surfaceDestroyed(SurfaceHolder arg0)
	{
		mbLoop = false;
	}

	//@Override
	public void run()
	{
		while (mbLoop)
		{
			try
			{
				Thread.sleep(200);
			} 
			catch (Exception e)
			{

			}
			synchronized (mSurfaceHolder)
			{
				Draw();
			}
		}

	}
}
