package com.xcontacts.ui.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 自定义这个类,只是为了在MyDialog类中使用
 * 
 * @author Lefter
 */
public abstract class MyLinearLayout extends LinearLayout {

	public MyLinearLayout(Context context) {
		super(context);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 根据Email、Phone、Im等的类型数组，判断用户选择的数据类型
	 * 
	 * @param postion
	 *            用户选择的类型在类型数组中的位置
	 * @return
	 */
	public abstract int getItemType(int position);
}