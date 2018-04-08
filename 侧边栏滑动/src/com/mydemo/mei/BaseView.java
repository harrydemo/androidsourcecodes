package com.mydemo.mei;

import android.view.View;

/**   
 * Copyright (c) 2012
 * All rights reserved.
 * @Description 视图基类
 * @author 美赞成
 */
public class BaseView {

	public boolean isScroll=true;//是否可以滚动
	public View view;//主界面视图
	public MyMoveView myMoveView;//移动视图
	
	/**
	 * 赋值移动视图
	 * @param myMoveView
	 */
	public void setMyMoveView(MyMoveView myMoveView) {
		this.myMoveView = myMoveView;
	}

	public boolean isScroll() {
		return isScroll;
	}

	public void setScroll(boolean isScroll) {
		this.isScroll = isScroll;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
}
