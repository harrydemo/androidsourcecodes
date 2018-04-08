package org.loon.game.sample.llk;

import org.loon.framework.javase.game.GameDeploy;
import org.loon.framework.javase.game.GameScene;

public class Main {

	public static void main(String[] args) {
		GameScene scene = new GameScene("LLK_AWT_TEST", 480, 320);
		GameDeploy deploy = scene.getDeploy();
		deploy.setShowFPS(true);
		deploy.setShowLogo(false);
		deploy.setScreen(new LLKScreen());
		deploy.mainLoop();
		scene.showFrame();
	}

}