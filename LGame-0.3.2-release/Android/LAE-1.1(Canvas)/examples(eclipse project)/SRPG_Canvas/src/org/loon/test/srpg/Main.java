package org.loon.test.srpg;

import org.loon.framework.android.game.LGameAndroid2DActivity;
import org.loon.framework.android.game.LMode;

public class Main extends LGameAndroid2DActivity {

	public void onMain() {
		this.initialization(true, LMode.Fill);
		this.setShowFPS(true);
		this.setShowLogo(false);
		this.setScreen(new MySRPGScreen());
		this.showScreen();

	}
	
	public void onGamePaused(){

	}

	public void onGameResumed() {
		
	}
}