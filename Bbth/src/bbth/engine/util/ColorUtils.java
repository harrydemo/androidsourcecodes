package bbth.engine.util;

import android.graphics.Color;

/**
 * Utilities for color-related stuff. Not thread-safe.
 * 
 * @author jardini
 * 
 */
public final class ColorUtils {
	private static final float[] hsv = new float[3];

	// Hue values 0-360
	// Saturation values 0-1
	// Value values 0-1
	public static final int randomHSV(float minHue, float maxHue,
			float minSaturation, float maxSaturation, float minValue,
			float maxValue) {
		float hue = MathUtils.randInRange(minHue, maxHue);
		float saturation = MathUtils.randInRange(minSaturation, maxSaturation);
		float value = MathUtils.randInRange(minValue, maxValue);
		// avoidable allocation :)
		hsv[0] = hue;
		hsv[1] = saturation;
		hsv[2] = value;
		return Color.HSVToColor(hsv);
	}
}
