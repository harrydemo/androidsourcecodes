package bbth.engine.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

/**
 * All games should extend this base class and implement getGame() to return a
 * custom subclass of Game.
 */
public abstract class GameActivity extends Activity {

	public static GameActivity instance = null;
	private GameView view;
	private Game game;

	protected abstract Game getGame();

	public GameActivity() {
		instance = this;
	}

	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		game = getGame();
		view = new GameView(this, game);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN,
				LayoutParams.FLAG_FULLSCREEN);
		setContentView(view);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		game.onActivityResult(requestCode, resultCode);
	}

	private void start() {
		game.onStart();
		view.onStart();
	}

	private void stop() {
		view.onStop();
		game.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		start();
	}

	@Override
	public void onPause() {
		stop();
		super.onPause();
	}

	@Override
	public void onStop() {
		stop();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		stop();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		game.onBackPressed();
	}
}
