package bbth.engine.collision;

import android.util.FloatMath;
import bbth.engine.util.Point;

public class SeparatingAxisTheorem {

	// per-axis variables
	public float axisX;
	public float axisY;
	private float min;
	private float max;
	private float storedMin;
	private float storedMax;
	private boolean rolesFlipped;
	private boolean collision;
	private float minAxisPenetration;
	private Point minAxisPenetrationVector;

	// theorem variables
	private float minOverallPenetration;
	private Point minOverallPenetrationVector;

	public SeparatingAxisTheorem() {
		minAxisPenetrationVector = new Point();
		minOverallPenetrationVector = new Point();
		reset();
	}

	public boolean isColliding() {
		prepareForNewShapePair();
		return minOverallPenetration != Float.MAX_VALUE;
	}

	public Point getMinimumVectorOfNonCollison() {
		return isColliding() ? minOverallPenetrationVector : null;
	}

	public void reset() {
		prepareForNewShapePair();
		minOverallPenetration = Float.MAX_VALUE;
	}

	public void prepareForNewShapePair() {
		if (collision && minAxisPenetration < minOverallPenetration) {
			minOverallPenetration = minAxisPenetration;
			minOverallPenetrationVector.x = minAxisPenetrationVector.x;
			minOverallPenetrationVector.y = minAxisPenetrationVector.y;
		}
		collision = true;
		minAxisPenetration = Float.MAX_VALUE;
	}

	public void changeAxis(float x, float y) {
		float length = FloatMath.sqrt(x * x + y * y);
		axisX = x / length;
		axisY = y / length;
		min = Float.MAX_VALUE;
		max = -Float.MAX_VALUE;
	}

	public void storeRange() {
		storedMin = min;
		storedMax = max;
		min = Float.MAX_VALUE;
		max = -Float.MAX_VALUE;
	}

	public void compareRanges() {
		float penetration = min + max < storedMin + storedMax ? storedMin - max
				: storedMax - min;
		float absPenetration = Math.abs(penetration);

		if (absPenetration < minAxisPenetration) {
			if (!rolesFlipped) {
				penetration = -penetration;
			}
			minAxisPenetration = absPenetration;
			minAxisPenetrationVector.x = axisX * penetration;
			minAxisPenetrationVector.y = axisY * penetration;
		}

		boolean overlapping = max > storedMin && storedMax > min;
		if (!overlapping) {
			collision = false;
		}
	}

	public void recordPoint(float x, float y) {
		float value = x * axisX + y * axisY;
		if (value < min) {
			min = value;
		}
		if (value > max) {
			max = value;
		}
	}

	public void flipRole() {
		rolesFlipped = !rolesFlipped;
	}
}
