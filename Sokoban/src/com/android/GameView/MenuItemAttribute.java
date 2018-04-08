package com.android.GameView;

public class MenuItemAttribute
{
	private boolean mIsEnabled;
	private boolean mIsVisible;
	private Menu mSubMenu;

	public MenuItemAttribute()
	{
		mIsEnabled = true;
		mIsVisible = true;
		mSubMenu = null;
	}
	public MenuItemAttribute(
			boolean isEnabled,
			boolean isVisible,
			Menu subMenu)
	{
		mIsEnabled = isEnabled;
		mIsVisible = isVisible;
		mSubMenu = subMenu;
	}
	public void setEnabled(boolean isEnabled)
	{
		mIsEnabled = isEnabled;
	}
	public boolean isEnabled()
	{
		return mIsEnabled;
	}
	public void setVisible(boolean isVisible)
	{
		mIsVisible = isVisible;
	}
	public boolean isVisible()
	{
		return mIsVisible;
	}
	public void setSubMenu(Menu subMenu)
	{
		mSubMenu = subMenu;
	}
	public Menu getSubMenu()
	{
		return mSubMenu;
	}
}
