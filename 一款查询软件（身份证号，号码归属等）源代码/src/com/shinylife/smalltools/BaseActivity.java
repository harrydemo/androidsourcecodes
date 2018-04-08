package com.shinylife.smalltools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public abstract class BaseActivity extends Activity implements OnClickListener {

	protected BaseLayout ly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onClick(View view) {
		Button leftButton = this.ly.leftButton;
		Button rightButton = this.ly.rightButton;
		if (view == leftButton) {
			HandleTitleBarEvent(0);
		} else if (view == rightButton) {
			HandleTitleBarEvent(1);
		}
	}

	protected abstract void HandleTitleBarEvent(int buttonId);

	protected void SetTitle(String title) {
		if (ly != null) {
			ly.setTitle(title);
		}
	}

	protected void setTitleBar(int leftBtnDrawId, String leftBtnText,
			int rightBtnDrawId, String rightBtnText) {
		if (leftBtnDrawId > 0 || leftBtnText.length() > 0) {
			ly.leftButton.setVisibility(View.VISIBLE);
			if (leftBtnDrawId > 0) {
				ly.leftButton.setBackgroundResource(leftBtnDrawId);
			}
			if (leftBtnText.length() > 0) {
				ly.leftButton.setText(leftBtnText);
			}
		} else {
			ly.leftButton.setVisibility(View.INVISIBLE);
		}
		if (rightBtnDrawId > 0 || rightBtnText.length() > 0) {
			ly.rightButton.setVisibility(View.VISIBLE);
			if (rightBtnDrawId > 0) {
				ly.rightButton.setBackgroundResource(rightBtnDrawId);
			}
			if (rightBtnText.length() > 0) {
				ly.rightButton.setText(rightBtnText);
			}
		} else {
			ly.rightButton.setVisibility(View.INVISIBLE);
		}
	}

	protected void setView(int layoutId) {
		ly = new BaseLayout(this, layoutId);
		setContentView(ly);
		ly.leftButton.setOnClickListener(this);
		ly.rightButton.setOnClickListener(this);
	}

	protected void showToast(String text) {
		Toast localToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
		//localToast.setGravity(17, 0, 0);
		localToast.show();
	}
}
