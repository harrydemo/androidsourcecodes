package com.mzba.peng;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mzba.peng.util.AppContext;

/**
 * 
 * @author mzba
 * 
 */
public class BasicActivity extends FragmentActivity {

	protected Toast toast;
	public int CanvasHeight;
	public int CanvasWidth;

	/**
	 * Activity被销毁后调用 关闭程序
	 */
	@Override
	protected void onDestroy() {
		if (toast != null) {
			toast.cancel();
		}
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		AppContext.setContext(this);
		LinearLayout layout = new LinearLayout(this);
		Button bt = new Button(this);
		layout.addView(bt);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		CanvasWidth = displayMetrics.widthPixels;
		CanvasHeight = displayMetrics.heightPixels;
//		getWindow().setSoftInputMode(
//				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	

	protected boolean onBackClick() {
		finish();
		return false;
	}

	public View getLayoutView(int layout) {
		return LayoutInflater.from(this).inflate(layout, null);
	}

	public void onToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

}
