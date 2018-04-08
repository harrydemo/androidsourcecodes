package com.cosina.game.kickkick;

import android.app.Activity;
import android.os.Bundle;

public class KickKick extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageManager.init(this);
		resetGame();
	}

	public void resetGame() {
		setContentView(new KickView(this));
	}
}