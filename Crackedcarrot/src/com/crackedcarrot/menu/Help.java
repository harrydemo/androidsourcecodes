package com.crackedcarrot.menu;

import com.crackedcarrot.menu.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Help extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}