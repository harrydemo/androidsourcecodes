package bbth.engine.util;

import android.graphics.PointF;

public class Point extends PointF {

	public Point() {
		super();
	}

	public Point(float x, float y) {
		super(x, y);
	}

	/**
	 * We need a deterministic hash code for a deterministic simulation.
	 */
	@Override
	public int hashCode() {
		return Float.floatToRawIntBits(x) ^ Float.floatToRawIntBits(y);
	}
}
