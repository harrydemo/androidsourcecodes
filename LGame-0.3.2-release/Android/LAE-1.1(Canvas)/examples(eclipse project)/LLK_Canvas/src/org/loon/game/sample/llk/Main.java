package org.loon.game.sample.llk;

import org.loon.framework.android.game.LGameAndroid2DActivity;


public class Main extends LGameAndroid2DActivity {
	

	public void onMain() {
		this.initialization(true);
		this.setFPS(40);
		this.setScreen(new LLKScreen());
		this.setShowLogo(false);
		this.setShowFPS(true);
		this.showScreen();

	}

	@Override
	public void onGamePaused() {
		
	}

	@Override
	public void onGameResumed() {
		
	}

}