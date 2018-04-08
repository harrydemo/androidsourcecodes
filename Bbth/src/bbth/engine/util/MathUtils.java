package bbth.engine.util;

import java.util.Random;

import android.util.FloatMath;

public final class MathUtils {
	public static final float PI = (float) Math.PI;
	public static final float TWO_PI = 2 * PI;
	private static final Random _random = new Random();

	public static void resetRandom(long seed) {
		_random.setSeed(seed);
	}

	public static int randInRange(int min, int max) {
		return min + _random.nextInt(max - min);
	}

	// random number in the range [min, max)
	public static float randInRange(float min, float max) {
		return (max - min) * _random.nextFloat() + min;
	}

	public static float getAngle(float x, float y, float x2, float y2) {
		return (float) Math.atan2(y2 - y, x2 - x);
	}

	public static float normalizeAngle(float a, float center) {
		return a - TWO_PI * FloatMath.floor((a + PI - center) / TWO_PI);
	}

	// linear interpolation
	public static final float lerp(float f1, float f2, float proportion) {
		return f1 * (1 - proportion) + f2 * proportion;
	}

	public static float getDist(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		return FloatMath.sqrt(dx * dx + dy * dy);
	}

	public static float getDistSqr(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		return dx * dx + dy * dy;
	}

	public static void getProjection(Point u, Point v, Point result) {
		float dotp = dot(u, v);
		result.x = u.x * dotp;
		result.y = u.y * dotp;
	}

	public static float dot(Point u, Point v) {
		return u.x * v.x + u.y * v.y;
	}

	public static float clamp(float min, float max, float val) {
		return Math.max(min, Math.min(val, max));
	}

	public static float toDegrees(float heading) {
		return heading * 180 / PI;
	}

	public static float toRadians(float heading) {
		return heading * PI / 180;
	}

	public static float scale(float min, float max, float target_min,
			float target_max, float val, boolean clamp) {
		float result = (val / ((max - min) / (target_max - target_min)))
				+ target_min;
		return (clamp ? MathUtils.clamp(target_min, target_max, result)
				: result);
	}
}
