package org.loon.game.test.physics;

import org.loon.framework.android.game.LGameAndroid2DActivity;


public class Main extends LGameAndroid2DActivity {
	

	public void onMain() {
		this.initialization(false,LMode.Fill);
		this.setShowLogo(false);
		this.setShowFPS(true);
		this.setFPS(40);
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