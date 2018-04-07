package com.genius.demo;


import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;

public class MenuItemData {

	
	private LevelListDrawable	mLevelListDrawable;
	
	private String mTitleID[];
	
	private int    mCount;
	
	public MenuItemData(LevelListDrawable levelListDrawable, String titleID[], int count)
	{
		refreshData(levelListDrawable, titleID, count);
	}
	
	public void refreshData(LevelListDrawable levelListDrawable, String titleID[], int count)
	{
		mLevelListDrawable = levelListDrawable; 
		mTitleID = titleID;
		mCount = count;
		

	}
	
	public String getTitle(int index)
	{
		return mTitleID[index];
	}
	
	public Drawable getDrawable(int index)
	{
		mLevelListDrawable.setLevel(index);
		Drawable drawable = mLevelListDrawable.getCurrent();
		
		return drawable;
	}
	
	public int getCount()
	{
		return mCount;
	}
}
