package org.loon.test;

import org.loon.framework.android.game.LGameAndroid2DActivity;
import org.loon.framework.android.game.LMode;

public class Main extends LGameAndroid2DActivity {

	public void onMain() {
		
		// 横屏
		this.initialization(true,LMode.Fill);
		// 不显示logo
		this.setShowLogo(false);
		// 显示实际fps
		this.setShowFPS(true);
		// 初始画面使用TD
		this.setScreen(new TD());
		
		// 显示画面
		this.showScreen();

	}

	@Override
	public void onGamePaused() {
		
	}

	@Override
	public void onGameResumed() {
		
	}

}