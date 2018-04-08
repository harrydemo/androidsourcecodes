package bbth.engine.collision;

import android.util.FloatMath;
import bbth.engine.util.Point;

public class Transform {

	public static final Transform IDENTITY = new Transform();

	/**
	 * The offset from the origin.
	 */
	public Point offset;

	/**
	 * The clockwise rotation about offset.
	 */
	public float angle;

	/**
	 * Initializes this to the identity transform.
	 */
	public Transform() {
		offset = new Point();
	}

	/**
	 * Applies the transform parent to this transform and stores it in result.
	 */
	public void apply(Transform transform, Transform result) {
		transform(transform.offset, result.offset);
		result.angle = angle + transform.angle;
	}

	/**
	 * Applies this transform to point and stores it in result.
	 */
	public void transform(Point point, Point result) {
		float sin = FloatMath.sin(angle);
		float cos = FloatMath.cos(angle);
		result.x = offset.x + point.x * cos - point.y * sin;
		result.y = offset.y + point.y * cos + point.x * sin;
	}

	/**
	 * Applies the inverse of this transform to point and stores it in result.
	 */
	public void inverseTransform(Point point, Point result) {
		float sin = FloatMath.sin(angle);
		float cos = FloatMath.cos(angle);
		result.x = (point.x - offset.x) * cos + (point.y - offset.y) * sin;
		result.y = (point.y - offset.y) * cos - (point.x - offset.x) * sin;
	}
}
