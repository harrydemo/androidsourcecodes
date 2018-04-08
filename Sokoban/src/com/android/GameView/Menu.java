package com.android.GameView;

import com.android.GameData.SystemSettingsSaver;
import com.android.GeneralDesign.BitmapProvider;
import com.android.GeneralDesign.LayoutDesign;
import com.android.GeneralDesign.MusicPlayer;
import com.android.GeneralDesign.clienDB;
import com.android.Sokoban.MainGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;

public abstract class Menu extends GameDisplayItem
{
	private static final int USER_INPUP_DELAY = 3;
	private static Menu mFocusMenu;
	private Bitmap mBitmapCursorFrames[];
	private int mCurAniFrameIndex;
	private int mCurAniFrameNum;
	private int mCurAniTick;
	private int mTextSize;
	private int mTextBaseLine;
	private int mFontHeight;
	private int mItemHeight;
	private int mTileRectHeith;
	
	private int mCursorWidth;
	private int mCursorIndex;
	
	private int mItemCount;
	
	private String mMenuTile;
	private String mMenuItemStrings[];
	private MenuItemAttribute[] mMenuItemAttributes;
	private Menu mParentMenu;
	
	private Paint mPaint;
///////////////////////////////////////////////////
//	static functions
///////////////////////////////////////////////////
	public static Menu getFoucus()
	{
		return mFocusMenu;
	}
	public static void setFoucus(Menu menu)
	{
		if(mFocusMenu == menu)
			return;
		
		if(null != mFocusMenu)
		{
			mFocusMenu.onFocusChange(false);
		}
		mFocusMenu = menu;
		mFocusMenu.onFocusChange(true);
	}
	public static void selectedActiveItem()
	{
		if(null == mFocusMenu)
		{
			return;
		}
		selectedItem(mFocusMenu.getCursorIndex());
	}
	public static void selectedItem(int x, int y)
	{
		if(null == mFocusMenu)
		{
			return;
		}
		selectedItem(mFocusMenu.getItemIndex(x, y));
	}
	public static void selectedItem(int itemIndex)
	{
		if(null == mFocusMenu)
		{
			return;
		}
		mFocusMenu.onShowHiligCursor(itemIndex);
	}
	public static void scrollUp()
	{
		if(null == mFocusMenu)
		{
			return;
		}
		mFocusMenu.onScrollUp();
	}
	public static void scrollDown()
	{
		if(null == mFocusMenu)
		{
			return;
		}
		mFocusMenu.onScrollDown();
	}
	public static void scrollTo(int toIndex)
	{
		if(null == mFocusMenu)
		{
			return;
		}
		mFocusMenu.onScrollTo(toIndex);
	}
	public static void returnToParentMenu()
	{
		if(null == mFocusMenu)
		{
			return;
		}
		mFocusMenu.onReturnToParentMenu();
	}
	public static void refurbishFoucusMenu()
	{
		if(null == mFocusMenu)
		{
			return;
		}
		mFocusMenu.onRefurbish();
	}
	public static void drawFoucusMenu(Canvas canvas)
	{
		if(null == mFocusMenu)
		{
			return;
		}
		mFocusMenu.onDraw(canvas);
	}
////////////////////////////////////////////////////
	public boolean onFocusChange(boolean abtain)
	{
		if(false == abtain)
		{
			onItemFocusChanged(getCursorIndex(), false);
			onCloseMenu();
		}
		else
		{
			onShowMenu();
			onItemFocusChanged(getCursorIndex(), true);
		}
		return true;
	}
	public void onItemSelected(int selectedItemIndex)
	{
		if(selectedItemIndex < 0)
			return;
		selectedItemIndex %= mMenuItemAttributes.length;
		MenuItemAttribute selectedMenuItemArribute = mMenuItemAttributes[selectedItemIndex];
		Menu subMenu = selectedMenuItemArribute.getSubMenu();
		if(false == selectedMenuItemArribute.isEnabled() ||
			false == selectedMenuItemArribute.isVisible())
		{
			return;
		}
		setCursorIndex(selectedItemIndex);
		if(null != subMenu)
		{
			mFocusMenu.onFocusChange(false);
			mFocusMenu = subMenu;
			mFocusMenu.onFocusChange(true);
			return;
		}
		onItemEnter(selectedItemIndex);
	}
	public void onShowHiligCursor(int selectedItemIndex)
	{
		if(selectedItemIndex < 0 || selectedItemIndex >= mItemCount)
			return;
		selectedItemIndex %= mMenuItemAttributes.length;
		MenuItemAttribute selectedMenuItemArribute = mMenuItemAttributes[selectedItemIndex];
		if(false == selectedMenuItemArribute.isEnabled() ||
			false == selectedMenuItemArribute.isVisible())
		{
			return;
		}
		playSystemMusic();
		setCursorIndex(selectedItemIndex);
		mHiligtCursor.setItemIndex(selectedItemIndex);
		mHiligtCursor.show();
	}
	public void onScrollUp()
	{
		int itemIndex1;
		int itemIndex2;
		itemIndex1 = getCursorIndex();
		cursorMoveUp();
		itemIndex2 = getCursorIndex();
		if(itemIndex1 != itemIndex2)
		{
			onItemFocusChanged(itemIndex1, false);
			onItemFocusChanged(itemIndex2,true);
		}
	}
	public void onScrollDown()
	{
		int itemIndex1;
		int itemIndex2;
		itemIndex1 = getCursorIndex();
		cursorMoveDown();
		itemIndex2 = getCursorIndex();
		if(itemIndex1 != itemIndex2)
		{
			onItemFocusChanged(itemIndex1, false);
			onItemFocusChanged(itemIndex2,true);
		}
	}
	public void onScrollTo(int toItemIndex)
	{
		int curItemIndex = getCursorIndex();
		if(toItemIndex < 0 ||
			toItemIndex >= mItemCount ||
			toItemIndex == curItemIndex)
		{
			return;
		}
		toItemIndex %= mMenuItemAttributes.length;
		MenuItemAttribute selectedMenuItemArribute = mMenuItemAttributes[toItemIndex];
		if(false == selectedMenuItemArribute.isEnabled() ||
			false == selectedMenuItemArribute.isVisible())
		{
			return;
		}
		
		onItemFocusChanged(curItemIndex, false);
		setCursorIndex(toItemIndex);
		onItemFocusChanged(toItemIndex,true);
	}
	public void onReturnToParentMenu()
	{
		Menu parentMenu = getParentMenu();
		if(null == parentMenu)
			return;
		onFocusChange(false);
		mFocusMenu = parentMenu;
		mFocusMenu.onFocusChange(true);
	}
	public void onRefurbish()
	{
		refurbish();
	}
////////////////////////////////////////////////////
	public Menu(MainGame mainGame, String[][] menuStrings)
	{
		super(mainGame, LayoutDesign.DisplayItemID.MAIN_MENU_ITEMS);

		mMenuTile = menuStrings[0][0];
		mItemCount = menuStrings[1].length;
		mParentMenu = null;
		mPaint = new Paint();
		mMenuItemStrings = new String[mItemCount];
		mMenuItemAttributes = new MenuItemAttribute[mItemCount];
		for(int i = 0; i < mItemCount; i++)
		{
			mMenuItemStrings[i] = menuStrings[1][i];
			mMenuItemAttributes[i] = new MenuItemAttribute();
		}
		int textSize = LayoutDesign.getTextSize(
				mainGame.getScreenResolutionType(),
				mainGame.getScreenOrentation(),
				mDisplayItemId);
		setTextSize(textSize);
		resetCursorAnimation();
		setMenuCursor(0);
		mHiligtCursor = new MenuHilicursor();
		mHiligtCursor.setTime(USER_INPUP_DELAY);
		setDisplayRectSize();
		updataDisplaySetting();
	}
	private MenuHilicursor mHiligtCursor;
	public void setMenuTexts(String[][] menuStrings)
	{
		mMenuTile = menuStrings[0][0];
		for(int i = 0; i < mItemCount; i++)
		{
			mMenuItemStrings[i] = menuStrings[1][i];
		}
		setDisplayRectSize();
		updataDisplaySetting();
	}
	private void resetCursorAnimation()
	{
		mCurAniFrameIndex = 0;
		mCurAniFrameNum = 2;
		mCurAniTick = 0;
	}
	public void setParentMenu(Menu parentMenu)
	{
		mParentMenu = parentMenu;
	}
	public Menu getParentMenu()
	{
		return mParentMenu;
	}
	public void setTextSize(int textSize)
	{
		mTextSize = textSize;
		mPaint.setTextSize(mTextSize);

		FontMetrics fm = mPaint.getFontMetrics();
		mFontHeight = (int) Math.ceil(fm.descent - fm.top);
		mTextBaseLine = (int) Math.ceil(0 - fm.top);
		mItemHeight = mFontHeight + 5;
		mTileRectHeith = mItemHeight + (mItemHeight >> 1);
		mCursorWidth = mFontHeight + 10;

		mBitmapCursorFrames = new Bitmap[2];
		mBitmapCursorFrames[0] = 
			BitmapProvider.getBitmap(
					BitmapProvider.BitmapID.MP_4_1,
					mFontHeight,
					mFontHeight);
		mBitmapCursorFrames[1] = 
			BitmapProvider.getBitmap(
					BitmapProvider.BitmapID.MP_4_2,
					mFontHeight,
					mFontHeight);
	}
	public void setItemEnable(int itemIndex, boolean isEnabled)
	{
		if(itemIndex == mCursorIndex && false == isEnabled)
			cursorMoveDown();
		mMenuItemAttributes[itemIndex].setEnabled(isEnabled);
	}
	public void setItemVisible(int itemIndex, boolean isVisible)
	{
		if(itemIndex == mCursorIndex && false == isVisible)
			cursorMoveDown();
		mMenuItemAttributes[itemIndex].setVisible(isVisible);
		setDisplayRectSize();
		updataDisplaySetting();
	}
	public void setSubMenu(int itemIndex, Menu subMenu)
	{
		mMenuItemAttributes[itemIndex].setSubMenu(subMenu);
		subMenu.setParentMenu(this);
	}
	public void setMenuCursor(int itemIndex)
	{
		mCursorIndex = itemIndex;
		MenuItemAttribute menuItemAttribute = mMenuItemAttributes[mCursorIndex];
		if(false == menuItemAttribute.isEnabled() || false == menuItemAttribute.isVisible())
			cursorMoveDown();
	}
	public void  cursorMoveUp()
	{
		int index = mCursorIndex;
		int num;
		
		MenuItemAttribute menuItemAttribute;
		for(num = mItemCount; num > 0; num--)
		{
			index--;
			if(-1 == index)
			{
				index = mItemCount - 1;
			}
			menuItemAttribute = mMenuItemAttributes[index];
			if(true == menuItemAttribute.isEnabled() &&
				true == menuItemAttribute.isVisible())
			{
				mCursorIndex = index;
				break;
			}
		}
	}
	public void  cursorMoveDown()
	{
		int index = mCursorIndex;
		int num;
		MenuItemAttribute menuItemAttribute;
		for(num = mItemCount; num > 0; num--)
		{
			index++;
			if(mItemCount == index)
			{
				index = 0;
			}
			menuItemAttribute = mMenuItemAttributes[index];
			if(true == menuItemAttribute.isEnabled() &&
				true == menuItemAttribute.isVisible())
			{
				mCursorIndex = index;
				break;
			}
		}
	}
	public int getCursorIndex()
	{
		return mCursorIndex;
	}
	public void setCursorIndex(int itemIndex)
	{
		itemIndex %= mMenuItemAttributes.length;
		mCursorIndex = itemIndex;
		MenuItemAttribute menuItemAttribute = mMenuItemAttributes[itemIndex];
		if(false == menuItemAttribute.isEnabled() ||
			false == menuItemAttribute.isVisible())
		{
			cursorMoveDown();
		}
	}
	public int getItemIndex(int x, int y)
	{
		int destRectX, destRectY;
		int itemIndex;
		destRectX = mDisWinX + mOffsetX;// + mCursorWidth;
		destRectY = mDisWinY + mOffsetY;
		if(0 != mMenuTile.length())
			destRectY += mTileRectHeith;
		if(y < destRectY ||
			y >= destRectY + mItemHeight * mItemCount)
		{
			return -1;
		}
		int searchHeight = y - destRectY;
		int searchOffsetY = 0;
		for(itemIndex = 0; itemIndex < mItemCount; itemIndex++)
		{
			if(false == mMenuItemAttributes[itemIndex].isVisible())
			{
				continue;
			}
			if(searchOffsetY + mItemHeight > searchHeight)
				break;
			searchOffsetY += mItemHeight;
		}

		Rect textBound = new Rect();
		mPaint.getTextBounds(
				mMenuItemStrings[itemIndex],
				0,
				mMenuItemStrings[itemIndex].length(),
				textBound);
		if(x < destRectX ||
			x > destRectX + mCursorWidth + textBound.right - textBound.left)
		{
			return -1;
		}
		return itemIndex;
	}
	public Rect getItemDisplayRect(int itemIndex)
	{
		if(itemIndex < 0)
			return null;
		
		Rect textBound = new Rect();
		itemIndex = itemIndex % mItemCount;
		mPaint.getTextBounds(
				mMenuItemStrings[itemIndex],
				0,
				mMenuItemStrings[itemIndex].length(),
				textBound);
		
		int itemStartY = 0;
		if(0 != mMenuTile.length())
		{
			itemStartY += mTileRectHeith;
		}
		for(int i = 0; i < itemIndex; i++)
		{
			if(false == mMenuItemAttributes[i].isVisible())
			{
				continue;
			}
			itemStartY += mItemHeight;
		}
		
		int left, top, right, bottom;
		int hMargin = -2;
		int vMargin = 2;
		
		left = mCursorWidth;
		top = itemStartY;
		right = left + textBound.right - textBound.left;
		bottom = top + mItemHeight;

		left += hMargin;
		top += vMargin;
		right -= hMargin;
		bottom -= vMargin;
		
		return(new Rect(left, top, right, bottom));
	}
	@Override
	public void setDisplayRectSize()
	{
		int maxTextWidth = Integer.MIN_VALUE;
		int displayRectHeight = 0;
		Rect textBound = new Rect();
		int textWidth;
		if(0 != mMenuTile.length())
		{
			mPaint.getTextBounds(mMenuTile, 0, mMenuTile.length(), textBound);
			textWidth = textBound.right - textBound.left;
			if(maxTextWidth < textWidth)
				maxTextWidth = textWidth;
			displayRectHeight += mTileRectHeith;
		}
		for(int i = 0; i < mMenuItemStrings.length; i++)
		{
			if(false == mMenuItemAttributes[i].isVisible())
				continue;
			mPaint.getTextBounds(mMenuItemStrings[i], 0, mMenuItemStrings[i].length(), textBound);
			textWidth = textBound.right - textBound.left;
			if(maxTextWidth < textWidth)
				maxTextWidth = textWidth;
			displayRectHeight += mItemHeight;
		}
		
		if(mCursorWidth + maxTextWidth > mDisWinWidth)
			mDisRectWidth = mDisWinWidth;
		else
			mDisRectWidth = mCursorWidth + maxTextWidth;
		
		mDisRectHeight = displayRectHeight;
	}
	@Override
	protected void updataDisplaySetting()
	{
		super.updataDisplaySetting();
		if(mOffsetX > mCursorWidth)
		{
			mOffsetX -= (mCursorWidth >> 1);
		}
	}
	@Override
	public void refurbish()
	{
		mCurAniTick ++;
		if(mCurAniTick >= clienDB.MP_ANIMAITON_FRAME_PERIOD)
		{
			mCurAniTick = 0;
			mCurAniFrameIndex ++;
			if(mCurAniFrameIndex >= mCurAniFrameNum)
				mCurAniFrameIndex = 0;
		}
		mHiligtCursor.refurbish();
		super.refurbish();
	}
	@Override
	protected void drawDisplayArea(Canvas canvas)
	{
		int x = 0;
		int y = (mItemHeight - mFontHeight) >> 1;
		
		if(0 != mMenuTile.length())
		{
			mPaint.setColor(Color.WHITE);
			canvas.drawText(">>", x, y + mTextBaseLine, mPaint);
			canvas.drawText(mMenuTile, x + mCursorWidth, y + mTextBaseLine, mPaint);
			y += mTileRectHeith;
		}
		for(int i = 0; i < mItemCount; i++)
		{
			MenuItemAttribute menuItemAttribute = mMenuItemAttributes[i];
			if(false == menuItemAttribute.isVisible())
				continue;
			if(false == menuItemAttribute.isEnabled())
			{
				mPaint.setColor(0xff505050);
				canvas.drawText(mMenuItemStrings[i], x + mCursorWidth, y + mTextBaseLine, mPaint);
			}
			else if(i == mCursorIndex)
			{
				canvas.drawBitmap(
						mBitmapCursorFrames[mCurAniFrameIndex],
						0 ,
						y,
						mPaint);
				mPaint.setColor(Color.RED);
				canvas.drawText(mMenuItemStrings[i], x + mCursorWidth, y + mTextBaseLine, mPaint);
			}
			else
			{
				mPaint.setColor(Color.WHITE);
				canvas.drawText(mMenuItemStrings[i], x + mCursorWidth, y + mTextBaseLine, mPaint);
			}
			y += mItemHeight;
		}
		mHiligtCursor.onDraw(canvas);
	}
	@Override
	protected void onDisRectSizeChange(
			int preDisRectWidth, int preDisRectHeight,
			int newDisRectWidth, int newDisRectHeight)
	{
	}
	public abstract void onShowMenu();
	public abstract void onCloseMenu();
	public abstract void onItemFocusChanged(int selectedItemIndex, boolean abtain);
	public abstract void onItemEnter(int selectedItemIndex);
	class MenuHilicursor extends HilightRectCursor
	{
		private int mItemIndex;
		public void setItemIndex(int itemIndex)
		{
			if(itemIndex < 0 || itemIndex >= mItemCount)
				return;

			Rect rect = getItemDisplayRect(itemIndex);
			if(null != rect)
			{
				mItemIndex = itemIndex;
				setRect(rect);
			}
		}
		@Override
		public void onAutoHide()
		{
			onItemSelected(mItemIndex);
		}
	};
	private void playSystemMusic()
	{
		if(MusicPlayer.MusicSettingState.OFF == 
			SystemSettingsSaver.getSettingItem(
				SystemSettingsSaver.SettingItem.SYS_SOUND_EFFECT))
		{
			return;
		}
		MusicPlayer.playMusic(
				MusicPlayer.PlayerType.SYS_MUSIC_PLAYER,
				MusicPlayer.MusicID.SN_TOUTCH);
	}
}

