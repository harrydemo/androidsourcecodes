package com.imhere.android.activity.view;

import android.app.Activity;
import android.view.View;

public abstract class LayoutViews {
	public abstract View createView(Activity mActivity);

	protected abstract void initViews();

	protected abstract void initEvents();

}
