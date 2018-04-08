package com.xcontacts.ui.model;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcontacts.activities.R;

/**
 * Email、Phone、Im的容器继承自该类
 * 
 * @author Lefter
 */
public abstract class AbstractItemsContainer extends LinearLayout {

	/*
	 * *******************************************************************
	 * .................. //////////////////////////////////////////////////////
	 * .Phone...........+. /////////////////////////////【mHeader】//////////////
	 * .[Type][电话号码]——. //////////////////////////【mItemsContainer】/////////
	 * .[Type][电话号码]——. //////////////////////////【mItemsContainer】/////////
	 * .[Type][电话号码]——. //////////////////////////【mItemsContainer】/////////
	 * .................. /////////////////////////////////////////////////////
	 * *************************************************************************
	 */

	/**
	 * 放一个TextView和ImageButton.
	 */
	protected LinearLayout mHeader;
	protected TextView tvHeader;
	protected ImageButton ibAdd;
	/**
	 * 存放所有的同类信息。如：所有与Phone相关的信息
	 */
	protected LinearLayout mItemsContainer;
	protected Context mContext;

	/**
	 * 创建对象时，item必须初始化完毕
	 * 
	 * @param context
	 *            上下文环境
	 * @param item
	 *            被添加的一行
	 */
	public AbstractItemsContainer(Context context) {
		super(context);
		this.mContext = context;
		// this代表存放mHeader和mContainer的LinearLayout
		// 设置layout_width和layout_height
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		// 设置为垂直方向
		this.setOrientation(VERTICAL);

		mHeader = new LinearLayout(mContext);
		mHeader.setOrientation(HORIZONTAL);
		// 在这个LinearLayout中可以随意添加、删除一行
		mItemsContainer = new LinearLayout(context);
		mItemsContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		// 默认HORIZONTAL
		mItemsContainer.setOrientation(VERTICAL);

		tvHeader = new TextView(context);
		tvHeader.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT,
				1.0f));

		ibAdd = new ImageButton(context);
		ibAdd.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		ibAdd.setBackgroundResource(R.drawable.ic_btn_round_plus);
		mHeader.addView(tvHeader);
		mHeader.addView(ibAdd);
		this.addView(mHeader);
		this.addView(mItemsContainer);
	}

	/**
	 * 负责对同一类型信息的初始化
	 * 
	 * @param strRes
	 *            设置每一类信息的左上角的文字。如：Phone、Email、Postal Address、Organization等
	 * @param typesRes
	 *            该类信息可以设置哪些类型
	 * @param hintRes
	 *            EditText的提示信息(hint)
	 */
	public abstract void initViews(int strRes, int typesRes, int hintRes);

	/**
	 * 将构建好的同类信息的ContentProviderOperation放到ArrayList中
	 */
	public abstract ArrayList<ContentProviderOperation> getItemsContentProviderOperation();

	/**
	 * @return 返回同类信息的容器
	 */
	public LinearLayout getmItemsContainer() {
		return mItemsContainer;
	}
}