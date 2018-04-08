package org.loon.game.sample.llk;

import org.loon.framework.javase.game.GameScene;

public class Main {

	public static void main(String[] args) {
		GameScene frame = new GameScene("LLK_OpenGL_TEST", 480, 320);
		frame.setShowFPS(true);
		frame.setShowMemory(false);
		frame.setScreen(new LLKScreen());
		frame.showScreen();
	}

}