package org.loon.act.test;

import org.loon.framework.android.game.LGameAndroid2DActivity;


public class Main extends LGameAndroid2DActivity {

	@Override
	public void onMain() {
		this.initialization(true, LMode.Fill);
		this.setShowFPS(true);
		this.setShowLogo(false);
		this.setScreen(new ACTTest());
		this.showScreen();
	}

	public void onGamePaused() {

	}

	public void onGameResumed() {

	}

}