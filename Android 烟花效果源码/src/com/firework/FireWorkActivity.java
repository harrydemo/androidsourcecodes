package com.firework;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fireview.MyView;

public class FireWorkActivity extends Activity {
	/** Called when the activity is first created. */

	// EventListener mListener = new EventListener();

	static final String LOG_TAG = FireWorkActivity.class.getSimpleName();
	static int SCREEN_W = 480;// 当前窗口的大小
	static int SCREEN_H = 800;

	MyView fireworkView;

	// get the current looper (from your Activity UI thread for instance

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
		fireworkView = new MyView(this);
		setContentView(fireworkView);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (fireworkView.isRunning()) {
			fireworkView.setRunning(false);
		}
	}
}