package com.shinylife.smalltools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseLayout extends RelativeLayout {
	public Button leftButton,rightButton;
	
	public TextView TitleText;

	public BaseLayout(Context context, int layoutId) {
		super(context);
		LayoutInflater localLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		View titleView = localLayoutInflater.inflate(R.layout.titlebar, null);
		addView(titleView, p);

		RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		p1.addRule(RelativeLayout.BELOW, titleView.getId());
		View contView = localLayoutInflater.inflate(layoutId, null);
		addView(contView, p1);
		leftButton = (Button) findViewById(R.id.leftButton);
		rightButton = (Button) findViewById(R.id.rightButton);
		TitleText = (TextView) findViewById(R.id.titleText);
	}

	public void setTitle(String title) {
		TextView.BufferType bt = TextView.BufferType.SPANNABLE;
		TitleText.setText(title, bt);
	}
}
