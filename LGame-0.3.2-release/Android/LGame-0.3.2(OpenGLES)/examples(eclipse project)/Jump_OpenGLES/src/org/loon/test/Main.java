package org.loon.test;

import org.loon.framework.android.game.LGameAndroid2DActivity;

public class Main extends LGameAndroid2DActivity {

	public void onMain() {
		setupGravity();
		this.initialization(false, LMode.Fill);
		this.setShowLogo(false);
		this.setShowFPS(true);
		this.setScreen(new Test1());
		this.showScreen();

	}

	@Override
	public void onGamePaused() {

	}

	@Override
	public void onGameResumed() {

	}

}