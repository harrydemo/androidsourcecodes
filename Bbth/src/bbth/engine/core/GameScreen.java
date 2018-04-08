package bbth.engine.core;

import android.graphics.Canvas;

public abstract class GameScreen {

	protected GameScreen nextScreen;

	/**
	 * Set nextScreen to null to stay on the same screen. To change screens, set
	 * it to an object that implements screen, in which case that will become
	 * the current screen.
	 * 
	 * @return the protected member nextScreen
	 */
	public final GameScreen getNextScreen() {
		return nextScreen;
	}

	/**
	 * @param canvas
	 *            the canvas to draw into
	 */
	public abstract void onDraw(Canvas canvas);

	/**
	 * @param seconds
	 *            the number of seconds to advance
	 */
	public void onUpdate(float seconds) {
	}

	/**
	 * Send network messages, if any.
	 */
	public void onNetworkUpdate() {
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
	}

	/**
	 * Called when the phone resumes from pause, use this to acquire any
	 * resources that were freed on pause.
	 */
	public void onStart() {
	}

	/**
	 * Called when the phone pauses, use this to free any resources not needed
	 * while paused.
	 */
	public void onStop() {
	}

	/**
	 * Stupid method necessary because of android's weird context/activity mess,
	 * override it if you need to hook into this method on GameActivity.
	 */
	public void onActivityResult(int requestCode, int resultCode) {
	}
}
