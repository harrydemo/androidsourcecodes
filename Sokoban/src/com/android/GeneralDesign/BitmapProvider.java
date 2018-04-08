package com.android.GeneralDesign;

import com.android.Sokoban.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapProvider
{
	public static final class BitmapID
	{
		public static final int GAME_LOGO = 1;
		public static final int WALL = 2;
		public static final int PATH = 3;
		public static final int DEST = 4;
		public static final int BOXA = 5;
		public static final int BOXB = 6;
		public static final int MP_1_1 = 7;
		public static final int MP_1_2 = 8;
		public static final int MP_2_1 = 9;
		public static final int MP_2_2 = 10;
		public static final int MP_3_1 = 11;
		public static final int MP_3_2 = 12;
		public static final int MP_4_1 = 13;
		public static final int MP_4_2 = 14;
		public static final int EXIT = 15;
		public static final int SAVE = 16;
		public static final int LOCK = 17;
		public static final int RESET = 18;
		public static final int CLEAR = 19;
		public static final int UN_DO = 20;
		public static final int RE_DO = 21;
		public static final int PRE_STATE = 22;
		public static final int NEXT_STATE = 23;
	}
	private static Bitmap mBitmapSrcGameIcons;
	private static Bitmap mBitmapSrcLogo;
	public static final int SRC_ICON_WIDTH = 40;
	public static final int SRC_ICON_HEIGHT = 40;
	private static Activity mSokoban;
	private static BitmapProvider instance;
	
	public static void create(Activity sokoban)
	{
		if(null == instance)
		{
			instance = new BitmapProvider(sokoban);
		}
	}
	private BitmapProvider(Activity sokoban)
	{
		mSokoban = sokoban;
		mBitmapSrcGameIcons = BitmapFactory.decodeResource(
				mSokoban.getResources(), R.drawable.sokoban_game_icons);
		mBitmapSrcLogo = BitmapFactory.decodeResource(
				mSokoban.getResources(), R.drawable.sokoban_logo);
	}
	public static int[] getBitampSize(int bitmapId)
	{
		int bitmapSize[] = new int[2];
		if(BitmapID.GAME_LOGO == bitmapId)
		{
			bitmapSize[0] = mBitmapSrcLogo.getWidth();
			bitmapSize[1] = mBitmapSrcLogo.getHeight();
		}
		else
		{
			bitmapSize[0] = SRC_ICON_WIDTH;
			bitmapSize[1] = SRC_ICON_HEIGHT;
		}
		return bitmapSize;
	}
	public static Bitmap getBitmap(int bitmapId)
	{
		if(BitmapID.GAME_LOGO == bitmapId)
		{
			return getBitmap(
					BitmapID.GAME_LOGO,
					mBitmapSrcLogo.getWidth(),
					mBitmapSrcLogo.getHeight());
		}
		return getBitmap(bitmapId, SRC_ICON_WIDTH, SRC_ICON_HEIGHT);
	}
	public static Bitmap getBitmap(int bitmapId, int width, int height)
	{
		Bitmap bitmap = null;
		switch(bitmapId)
		{
		case BitmapID.GAME_LOGO:
			bitmap = Bitmap.createBitmap(mBitmapSrcLogo);
			break;
		//////////////////////////////////////////////////////////
		case BitmapID.WALL:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					0,						/* SRC_ICON_WIDTH * 0 */
					0,						/* SRC_ICON_HEIGHT * 0 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.PATH:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH,			/* SRC_ICON_WIDTH * 1 */
					0,						/* SRC_ICON_HEIGHT * 0 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.DEST:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 2,		/* SRC_ICON_WIDTH * 2 */
					0,						/* SRC_ICON_HEIGHT * 0 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.BOXA:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 3,		/* SRC_ICON_WIDTH * 3 */
					0,						/* SRC_ICON_HEIGHT * 0 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.BOXB:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 4,		/* SRC_ICON_WIDTH * 4 */
					0,						/* SRC_ICON_HEIGHT * 0 */
					SRC_ICON_WIDTH, SRC_ICON_HEIGHT);
			break;
		//////////////////////////////////////////////////////////
		case BitmapID.MP_1_1:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					0,						/* SRC_ICON_WIDTH * 0 */
					SRC_ICON_HEIGHT,		/* SRC_ICON_HEIGHT * 1 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.MP_1_2:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH,			/* SRC_ICON_WIDTH * 1 */
					SRC_ICON_HEIGHT,		/* SRC_ICON_HEIGHT * 1 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.MP_2_1:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 2,		/* SRC_ICON_WIDTH * 2 */
					SRC_ICON_HEIGHT,		/* SRC_ICON_HEIGHT * 1 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.MP_2_2:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 3,		/* SRC_ICON_WIDTH * 3 */
					SRC_ICON_HEIGHT,		/* SRC_ICON_HEIGHT * 1 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		//////////////////////////////////////////////////////////
		case BitmapID.MP_3_1:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					0,						/* SRC_ICON_WIDTH * 0 */
					SRC_ICON_HEIGHT * 2,	/* SRC_ICON_HEIGHT * 2 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.MP_3_2:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH,			/* SRC_ICON_WIDTH * 1 */
					SRC_ICON_HEIGHT * 2,	/* SRC_ICON_HEIGHT * 2 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.MP_4_1:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 2,		/* SRC_ICON_WIDTH * 2 */
					SRC_ICON_HEIGHT * 2,	/* SRC_ICON_HEIGHT * 2 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.MP_4_2:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 3,		/* SRC_ICON_WIDTH * 3 */
					SRC_ICON_HEIGHT * 2,	/* SRC_ICON_HEIGHT * 2 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		//////////////////////////////////////////////////////////
		case BitmapID.EXIT:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					0,						/* SRC_ICON_WIDTH * 0 */
					SRC_ICON_HEIGHT * 3,	/* SRC_ICON_HEIGHT * 3 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.SAVE:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH,			/* SRC_ICON_WIDTH * 1 */
					SRC_ICON_HEIGHT * 3,	/* SRC_ICON_HEIGHT * 3 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.LOCK:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 2,		/* SRC_ICON_WIDTH * 2 */
					SRC_ICON_HEIGHT * 3,	/* SRC_ICON_HEIGHT * 3 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.RESET:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 3,		/* SRC_ICON_WIDTH * 3 */
					SRC_ICON_HEIGHT * 3,	/* SRC_ICON_HEIGHT * 3 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.CLEAR:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 4,		/* SRC_ICON_WIDTH * 4 */
					SRC_ICON_HEIGHT * 3,	/* SRC_ICON_HEIGHT * 3 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		//////////////////////////////////////////////////////////
		case BitmapID.UN_DO:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					0,						/* SRC_ICON_WIDTH * 0 */
					SRC_ICON_HEIGHT * 4,	/* SRC_ICON_HEIGHT * 4 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.RE_DO:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH,			/* SRC_ICON_WIDTH * 1 */
					SRC_ICON_HEIGHT * 4,	/* SRC_ICON_HEIGHT * 4 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.PRE_STATE:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 2,		/* SRC_ICON_WIDTH * 2 */
					SRC_ICON_HEIGHT * 4,	/* SRC_ICON_HEIGHT * 4 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		case BitmapID.NEXT_STATE:
			bitmap = Bitmap.createBitmap(
					mBitmapSrcGameIcons,
					SRC_ICON_WIDTH * 3,		/* SRC_ICON_WIDTH * 3 */
					SRC_ICON_HEIGHT * 4,	/* SRC_ICON_HEIGHT * 4 */
					SRC_ICON_WIDTH,
					SRC_ICON_HEIGHT);
			break;
		default:
			return null;
		}
		int srcWidth;
		int srcHeight;
		if(BitmapID.GAME_LOGO == bitmapId)
		{
			srcWidth = mBitmapSrcLogo.getWidth();
			srcHeight = mBitmapSrcLogo.getHeight();
		}
		else
		{
			srcWidth = SRC_ICON_WIDTH;
			srcHeight = SRC_ICON_WIDTH;
		}
		if(width != srcWidth || height != srcHeight)
		{
			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
		}
		return bitmap;
	}
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		mBitmapSrcLogo = null;
		mBitmapSrcLogo = null;
	}
}
