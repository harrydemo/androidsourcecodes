package org.loon.framework.android.game.physics;

import com.badlogic.gdx.math.Vector2;

/**
 * jbox2d
 */
public class MathUtils {
	// Max/min rewritten here because for some reason Math.max/min
	// can run absurdly slow for such simple functions...
	// TODO: profile, see if this just seems to be the case or is actually
	// causing issues...
	public static final float max(float a, float b) {
		return (a > b) ? a : b;
	}

	public static final float min(float a, float b) {
		return (a < b) ? a : b;
	}

	/** Returns the closest value to 'a' that is in between 'low' and 'high' */
	public static final float clamp(float a, float low, float high) {
		return MathUtils.max(low, MathUtils.min(a, high));
	}

	public static final Vector2 clamp(Vector2 a, Vector2 low, Vector2 high) {
		return Vector2.max(low, Vector2.min(a, high));
	}

	public static final float abs(final float x) {
		return Math.abs(x);
	}

	/**
	 * Next Largest Power of 2: Given a binary integer value x, the next largest
	 * power of 2 can be computed by a SWAR algorithm that recursively "folds"
	 * the upper bits into the lower bits. This process yields a bit vector with
	 * the same most significant 1 as x, but all 1's below it. Adding 1 to that
	 * value yields the next largest power of 2.
	 */
	public static final int nextPowerOfTwo(int x) {
		x |= (x >> 1);
		x |= (x >> 2);
		x |= (x >> 4);
		x |= (x >> 8);
		x |= (x >> 16);
		return x + 1;
	}

	public static final float radToDeg(final float rad) {
		return (float) (180.0f / Math.PI * rad);
	}

	public static final float degToRad(final float degree) {
		return (float) (Math.PI / 180.0f * degree);
	}

	public static boolean isPowerOfTwo(int x) {
		return (x != 0) && ((x & (x - 1)) == 0);
	}

}
