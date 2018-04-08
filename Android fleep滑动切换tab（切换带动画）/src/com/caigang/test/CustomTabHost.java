package com.caigang.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

/*
 * �Զ���TabHost��ʹtab�л���ʱ���ж���Ч��
 */
public class CustomTabHost extends TabHost {
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;

	private int tabCount;//tabҳ����

	public CustomTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		slideLeftIn = AnimationUtils.loadAnimation(context,R.anim.slide_left_in);
		slideLeftOut = AnimationUtils.loadAnimation(context,R.anim.slide_left_out);
		slideRightIn = AnimationUtils.loadAnimation(context,R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(context,R.anim.slide_right_out);
	}
	
	public int getTabCount() {
		return tabCount;
	}

	@Override
	public void addTab(TabSpec tabSpec) {
		tabCount++;
		super.addTab(tabSpec);
	}
	
	@Override
	public void setCurrentTab(int index) {
		//indexΪҪ�л�����tabҳ������currentTabIndexΪ����Ҫ��ǰtabҳ������
		int currentTabIndex = getCurrentTab();
		
		//���õ�ǰtabҳ�˳�ʱ�Ķ���
		if (null != getCurrentView()){//��һ�ν���MainActivityʱ��getCurrentView()ȡ�õ�ֵΪ��
			if (currentTabIndex == (tabCount - 1) && index == 0) {//����߽绬��
				getCurrentView().startAnimation(slideLeftOut);
			} else if (currentTabIndex == 0 && index == (tabCount - 1)) {//����߽绬��
				getCurrentView().startAnimation(slideRightOut);
			} else if (index > currentTabIndex) {//�Ǳ߽�����´�������fleep
				getCurrentView().startAnimation(slideLeftOut);
			} else if (index < currentTabIndex) {//�Ǳ߽�����´�������fleep
				getCurrentView().startAnimation(slideRightOut);
			}
		}
		
		super.setCurrentTab(index);
		
		//���ü�����ʾ��tabҳ�Ķ���
		if (currentTabIndex == (tabCount - 1) && index == 0){//����߽绬��
			getCurrentView().startAnimation(slideLeftIn);
		} else if (currentTabIndex == 0 && index == (tabCount - 1)) {//����߽绬��
			getCurrentView().startAnimation(slideRightIn);
		} else if (index > currentTabIndex) {//�Ǳ߽�����´�������fleep
			getCurrentView().startAnimation(slideLeftIn);
		} else if (index < currentTabIndex) {//�Ǳ߽�����´�������fleep
			getCurrentView().startAnimation(slideRightIn);
		}
	}
}
