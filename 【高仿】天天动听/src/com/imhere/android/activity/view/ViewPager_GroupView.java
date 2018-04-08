package com.imhere.android.activity.view;

import com.imhere.android.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

public class ViewPager_GroupView extends LayoutViews {
	private View mGroupView;

	@Override
	public View createView(Activity mActivity) {
		// TODO Auto-generated method stub
		mGroupView = LayoutInflater.from(mActivity).inflate(
				R.layout.viewpager_group, null);
		return mGroupView;

	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

}
