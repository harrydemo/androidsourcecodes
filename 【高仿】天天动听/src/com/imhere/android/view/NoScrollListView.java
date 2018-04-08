package com.imhere.android.view;

import com.imhere.android.adapter.DiscoverMyListViewAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class NoScrollListView extends LinearLayout {
	public DiscoverMyListViewAdapter adapter;

	public NoScrollListView(Context context) {
		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DiscoverMyListViewAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(DiscoverMyListViewAdapter adapter) {
		this.adapter = adapter;
		bindLinearLayout();
	}

	public void bindLinearLayout() {
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			View v = adapter.getDropDownView(i, null, null);
			this.addView(v);
		}
		Log.v("countTAG", "" + count);
	}

	public void setSelection(int position) {
		this.setSelection(position);

	}
}
