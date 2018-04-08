package bbth.engine.core;

import android.graphics.Canvas;

public abstract class Game {

	protected GameScreen currentScreen;

	/**
	 * @return the width of the game, used for transforming from game space to
	 *         screen space and back
	 */
	public float getWidth() {
		return 1;
	}

	/**
	 * @return the height of the game, used for transforming from game space to
	 *         screen space and back
	 */
	public float getHeight() {
		return 1;
	}

	/**
	 * @return the desired number of seconds between successive onDraw() calls
	 */
	public float getDrawDelay() {
		return 1.0f / 30.0f;
	}

	/**
	 * @return the desired number of seconds between successive onUpdate() calls
	 */
	public float getUpdateDelay() {
		return 1.0f / 30.0f;
	}

	/**
	 * Return the desired number of seconds between successive onNetworkUpdate()
	 * calls
	 */
	public float getNetworkUpdateDelay() {
		return 1.0f / 10.0f;
	}

	/**
	 * A variable frame rate causes onUpdate() to be called once before every
	 * onDraw() call (it is variable since the number of seconds passed to
	 * onUpdate() varies).
	 * 
	 * A fixed frame rate causes onUpdate() to be called once every
	 * getUpdateDelay() seconds (it is fixed since the number of seconds passed
	 * to onUpdate() will always be the number returned by getUpdateDelay()).
	 * 
	 * @return true to use a variable frame rate, false to use a fixed frame
	 *         rate
	 */
	public boolean lockUpdateToDraw() {
		return true;
	}

	/**
	 * Called when the user begins to touch the screen. Will be followed by zero
	 * or more onTouchMove() calls followed by an onTouchUp() call. This only
	 * supports a single touch.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public void onTouchDown(float x, float y) {
		if (currentScreen != null) {
			currentScreen.onTouchDown(x, y);
		}
	}

	/**
	 * Called when the user moves a touch on the screen. Will be preceded by an
	 * onTouchDown() call and followed by an onTouchUp() call. This only
	 * supports a single touch.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public void onTouchMove(float x, float y) {
		if (currentScreen != null) {
			currentScreen.onTouchMove(x, y);
		}
	}

	/**
	 * Called when the user ends a touch on the screen. Will be preceded by an
	 * onTouchDown() call and zero or more onTouchMove() calls. This only
	 * supports a single touch.
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 */
	public void onTouchUp(float x, float y) {
		if (currentScreen != null) {
			currentScreen.onTouchUp(x, y);
		}
	}

	/**
	 * @return false to use uniform scaling (which leaves black bars on the
	 *         edges), true to use non-uniform scaling (which stretches to fill
	 *         all available screen space)
	 */
	public boolean stretchToFillScreen() {
		return false;
	}

	/**
	 * Called once every getDrawDelay() seconds with a canvas that has been
	 * transformed to game space.
	 * 
	 * @param canvas
	 *            the canvas to draw to
	 */
	public void onDraw(Canvas canvas) {
		if (currentScreen != null) {
			currentScreen.onDraw(canvas);
		}
	}

	/**
	 * Called once every getUpdateDelay() seconds if useVariableFrameRate()
	 * returns true, otherwise called once before every onDraw() call.
	 * 
	 * @param seconds
	 *            the number of seconds that have passed since the last call to
	 *            onUpdate()
	 */
	public void onUpdate(float seconds) {
		if (currentScreen != null) {
			currentScreen.onUpdate(seconds);
			GameScreen nextScreen = currentScreen.getNextScreen();
			if (nextScreen != null) {
				currentScreen = nextScreen;
				onScreenChange();
			}
		}
	}

	/**
	 * Send network messages, if any.
	 */
	public void onNetworkUpdate() {
		if (currentScreen != null) {
			currentScreen.onNetworkUpdate();
		}
	}

	/**
	 * Called when the game gains focus. This corresponds to the onResume()
	 * android event.
	 */
	public void onStart() {
		if (currentScreen != null) {
			currentScreen.onStart();
		}
	}

	/**
	 * Called when the game loses focus. This corresponds to the onPause()
	 * android event.
	 */
	public void onStop() {
		if (currentScreen != null) {
			currentScreen.onStop();
		}
	}

	/**
	 * Called when the currentScreen is changed, override to provide custom
	 * behavior.
	 */
	public void onScreenChange() {
	}

	/**
	 * Stupid method necessary because of android's weird context/activity mess,
	 * override it if you need to hook into this method on GameActivity.
	 */
	public void onActivityResult(int requestCode, int resultCode) {
		if (currentScreen != null) {
			currentScreen.onActivityResult(requestCode, resultCode);
		}
	}

	/**
	 * Occurs when the back button is pressed
	 */
	public void onBackPressed() {
	}
}
