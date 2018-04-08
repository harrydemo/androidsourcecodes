package org.loon.test.barrage;

import org.loon.framework.android.game.LGameAndroid2DActivity;

public class Main extends LGameAndroid2DActivity {

	public void onMain() {
	
		this.initialization(false,LMode.Fill);
		this.setScreen(new MyBarrage());
		this.setShowLogo(false);
		this.setShowFPS(true);
		this.showScreen();

	}

	public void onGamePaused() {

	}
	
	public void onGameResumed() {
		
	}

}
