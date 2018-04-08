package org.loon.game.test.avg;

import org.loon.framework.android.game.LGameAndroid2DActivity;
import org.loon.framework.android.game.LMode;

public class Main extends LGameAndroid2DActivity {

	public void onMain() {
		this.initialization(true, LMode.Fill);
		this.setShowLogo(false);
		this.setShowFPS(true);
		this.setScreen(new TitleScreen());
		this.showScreen();
	}

	@Override
	public void onGamePaused() {

	}

	@Override
	public void onGameResumed() {

	}

}