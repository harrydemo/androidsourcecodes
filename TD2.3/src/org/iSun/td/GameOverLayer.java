package org.iSun.td;

import org.loon.framework.android.game.IAndroid2DHandler;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.window.LPaper;
import org.loon.framework.android.game.core.graphics.window.actor.Layer;

import android.content.Intent;

/**
 * 游戏结束
 * 
 * @author Administrator
 * 
 */
public class GameOverLayer extends Layer {
	final IAndroid2DHandler handler = (IAndroid2DHandler) LSystem
			.getSystemHandler();

	LImage background;

	public GameOverLayer(String background, int w, int h) {
		super(w, h);
		this.background = new LImage(background);
		this.setLocation(0, 0);
		setBackground(background);
		setLocked(true);
		setLimitMove(false);
		setActorDrag(false);
		setDelay(200);

		LPaper goHome = new LPaper("assets/retry.png") {
			@Override
			public void downClick() {
				// TODO Auto-generated method stub
				goHomePage();
			}

		};
		goHome.setLocation(210, 220);
		add(goHome);
	}

	public void goHomePage() {
		Intent intent = new Intent();
		intent.setClass(handler.getLGameActivity(), MapSelectActivity.class);
		handler.getLGameActivity().startActivity(intent);
		handler.getLGameActivity().finish();
	}
}
