package com.mydemo.mei;

import android.view.View;

/**   
 * Copyright (c) 2012
 * All rights reserved.
 * @Description ��ͼ����
 * @author ���޳�
 */
public class BaseView {

	public boolean isScroll=true;//�Ƿ���Թ���
	public View view;//��������ͼ
	public MyMoveView myMoveView;//�ƶ���ͼ
	
	/**
	 * ��ֵ�ƶ���ͼ
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
