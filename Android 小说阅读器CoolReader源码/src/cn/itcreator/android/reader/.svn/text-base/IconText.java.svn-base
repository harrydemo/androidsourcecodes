/**
 * <This class for a item contain icon,text and selectable.>
 *  Copyright (C) <2009>  <Wang XinFeng,ACC http://androidos.cc/dev>
 *
 *   This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package cn.itcreator.android.reader;

import android.graphics.drawable.Drawable;

/**
 * This class for a item contain icon,text and selectable
 * @author Wang XinFeng
 * @version 1.0
 *
 */
public class IconText implements Comparable<IconText>{
    /**the text*/
	private String mText = "";
	
	/**icon*/
	private Drawable mIcon;
	/**can be select?*/
	private boolean mSelectable = true;

	public IconText(String text, Drawable bullet) {
		mIcon = bullet;
		mText = text;
	}
	
	public boolean isSelectable() {
		return mSelectable;
	}
	
	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}
	
	public String getText() {
		return mText;
	}
	
	public void setText(String text) {
		mText = text;
	}
	
	public void setIcon(Drawable icon) {
		mIcon = icon;
	}
	
	public Drawable getIcon() {
		return mIcon;
	}

	public int compareTo(IconText other) {
		if(this.mText != null)
			return this.mText.compareTo(other.getText()); 
		else 
			throw new IllegalArgumentException();
	}
}
