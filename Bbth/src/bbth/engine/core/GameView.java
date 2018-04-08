package bbth.engine.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

final class GameView extends View implements Runnable {

	private float secondsUntilNextTick;
	private float secondsUntilNextNetworkTick;
	private long prevTime;
	private Game game;
	private Handler handler;
	private float offsetX;
	private float offsetY;
	private float scaleX;
	private float scaleY;
	private RectF clipRect;
	private boolean useClipRect;

	public GameView(Context context, Game gameImpl) {
		super(context);
		game = gameImpl;
		handler = new Handler();

		// Cache a RectF so we don't allocate one every time we draw
		clipRect = new RectF();
	}

	public void onStart() {
		prevTime = System.currentTimeMillis();
		run();
	}

	public void onStop() {
		handler.removeCallbacks(this);
	}

	private void updateTransform() {
		float screenWidth = getWidth();
		float screenHeight = getHeight();
		float gameWidth = game.getWidth();
		float gameHeight = game.getHeight();

		if (game.stretchToFillScreen()) {
			offsetX = offsetY = 0;
			scaleX = screenWidth / gameWidth;
			scaleY = screenHeight / gameHeight;
			useClipRect = false;
		} else {
			// calculate scale factor from screen space to game space
			boolean scaleWidth = gameWidth * screenHeight > screenWidth
					* gameHeight;
			float scale = scaleWidth ? screenWidth / gameWidth : screenHeight
					/ gameHeight;

			// make sure there are black bars around the edge of the game
			if (scaleWidth) {
				float y = (screenHeight - gameHeight * scale) / 2;
				clipRect.left = 0;
				clipRect.top = y;
				clipRect.right = screenWidth;
				clipRect.bottom = screenHeight - y;
			} else {
				float x = (screenWidth - gameWidth * scale) / 2;
				clipRect.left = x;
				clipRect.top = 0;
				clipRect.right = screenWidth - x;
				clipRect.bottom = screenHeight;
			}
			useClipRect = true;

			// transform to game space
			offsetX = screenWidth / 2 - gameWidth / 2 * scale;
			offsetY = screenHeight / 2 - gameHeight / 2 * scale;
			scaleX = scaleY = scale;
		}
	}

	private void updateFrameRate(float seconds) {
		if (game.lockUpdateToDraw()) {
			// the game has asked for a variable frame rate
			game.onUpdate(seconds);
		} else {
			// try to make sure the game updates at (1 / updateDelay) frames per
			// second, but don't do more than 10 updates per draw call to avoid
			// an infinite if updating takes too long (it'd be a vicious circle)
			int updateCount = 0;
			float updateDelay = game.getUpdateDelay();
			secondsUntilNextTick += seconds;
			while (++updateCount <= 10 && secondsUntilNextTick > 0) {
				game.onUpdate(updateDelay);
				secondsUntilNextTick -= updateDelay;
			}
		}

		// process network updates
		float updateDelay = game.getNetworkUpdateDelay();
		secondsUntilNextNetworkTick += seconds;
		while (secondsUntilNextNetworkTick > 0) {
			game.onNetworkUpdate();
			secondsUntilNextNetworkTick -= updateDelay;
		}
	}

	@Override
	public void run() {
		// update
		long currentTime = System.currentTimeMillis();
		updateFrameRate((currentTime - prevTime) * 0.001f);
		prevTime = currentTime;

		// redraw
		invalidate();

		// try hard to draw again exactly drawDelay milliseconds in the future
		long drawDelay = (long) (game.getDrawDelay() * 1000);
		long timeOfNextDraw = currentTime + drawDelay;
		long actualDelay = timeOfNextDraw - System.currentTimeMillis();
		handler.postDelayed(this, Math.max(1, Math.min(drawDelay, actualDelay)));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = (event.getX(0) - offsetX) / scaleX;
		float y = (event.getY(0) - offsetY) / scaleY;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			game.onTouchDown(x, y);
			break;

		case MotionEvent.ACTION_MOVE:
			game.onTouchMove(x, y);
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			game.onTouchUp(x, y);
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		updateTransform();

		// make sure there are black bars around the edge of the game
		if (useClipRect) {
			canvas.clipRect(clipRect);
		}

		// transform to game space
		canvas.translate(offsetX, offsetY);
		canvas.scale(scaleX, scaleY);

		game.onDraw(canvas);
	}
}
