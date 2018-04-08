package com.crackedcarrot.menu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;

/**
 * A class that is called first when applikation is started, it starts the intro
 * picture and then calls the main menu activity
 */
public class Intro extends Activity {

	private final int INTRO_LENGTH = 500;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/**
		 * This handler starts the main activity and closes this intro after
		 * INTRO_LENGTH seconds
		 */
		new Handler().postDelayed(new Runnable() {
			public void run() {
				/* Create an Intent that will start the Menu-Activity. */
				Intent theMain = new Intent(Intro.this, MainMenu.class);
				startActivity(theMain);
				finish();
				/* The fade- in and out animations, only works from version 2.0 */
				overridePendingTransition(R.anim.mainfadein,
						R.anim.introfadeout);
			}
		}, INTRO_LENGTH);

	}
}