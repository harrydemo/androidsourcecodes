package com.lequ.Base;

import net.youmi.android.AdView;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Toast;

import com.lequ.Blackjack.R;

public class CommonActivity extends Activity
{
	private static final int DIALOG_ABOUT = 1;
	protected AdView mAdView;
	protected LayoutInflater mInflater;
	private MyToast toast;

	protected void initView()
	{
		AdView localAdView = (AdView)findViewById(R.id.adView);
		this.mAdView = localAdView;
		ToastClickListener local1 = new ToastClickListener();
		MyToast localMyToast = new MyToast(this, local1);
		this.toast = localMyToast;
	}

	protected void onCreate(Bundle paramBundle)
	{
		super.onCreate(paramBundle);
		LayoutInflater localLayoutInflater = getLayoutInflater();
		this.mInflater = localLayoutInflater;
	}

	protected void onPause()
	{
		super.onPause();
		this.toast.dismiss();
	}

	protected void showTip()
	{
		this.toast.show();
	}
	class ToastClickListener implements MyToast.OnClickListener
	{
		public void onClick(MotionEvent paramMotionEvent)
		{
			if (mAdView != null)
				mAdView.dispatchTouchEvent(paramMotionEvent);
			Toast.makeText(CommonActivity.this, 2130968631, 0).show();
		}
	}
}