package org.loon.test;

import org.loon.framework.android.game.LGameAndroid2DActivity;

public class Main extends LGameAndroid2DActivity {

	public void onMain() {
		    this.setupGravity();
			this.initialization(true, LMode.Fill);
			this.setShowFPS(true);
			this.setShowLogo(false);
			this.setScreen(new ShootTest());
			this.showScreen();

	}

	@Override
	public void onGamePaused() {
	
	}

	@Override
	public void onGameResumed() {
	
	}

}