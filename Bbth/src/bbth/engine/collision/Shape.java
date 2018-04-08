package bbth.engine.collision;

import android.graphics.Canvas;
import android.graphics.Paint;
import bbth.engine.util.Point;

public abstract class Shape {

	/**
	 * This is to avoid allocations for collidesWith().
	 */
	private static final SeparatingAxisTheorem defaultTheorem = new SeparatingAxisTheorem();

	public Transform relativeTransform;
	public Transform worldTransform;
	public float friction;

	public Shape() {
		relativeTransform = new Transform();
		worldTransform = new Transform();
	}

	public Point getOffset() {
		return relativeTransform.offset;
	}

	public void setOffset(float x, float y) {
		relativeTransform.offset.x = x;
		relativeTransform.offset.y = y;
	}

	/**
	 * Must be called for all shapes before collision detection methods. This is
	 * to support compound objects. If we don't need to support compound objects
	 * we can just have a world transform (get rid of relative transform) and do
	 * away with this method.
	 * 
	 * @param parentTransform
	 *            The transform of the parent, or Transform.IDENTITY if there is
	 *            no parent.
	 */
	public void updateWorldTransform(Transform parentTransform) {
		parentTransform.apply(relativeTransform, worldTransform);
	}

	public boolean collidesWith(Shape other) {
		defaultTheorem.reset();
		applyTheorem(defaultTheorem, other);
		return defaultTheorem.isColliding();
	}

	public abstract boolean isCrossingLineSegment(float originX, float originY,
			float rayX, float rayY);

	public abstract void draw(Canvas canvas, Paint paint);

	public abstract void recordPoints(SeparatingAxisTheorem theorem);

	public abstract void applyTheorem(SeparatingAxisTheorem theorem, Shape other);

	public abstract void applyTheorem(SeparatingAxisTheorem theorem,
			CircleShape other);

	public abstract void applyTheorem(SeparatingAxisTheorem theorem,
			PolygonShape other);

	public abstract void applyTheorem(SeparatingAxisTheorem theorem,
			CompoundShape other);
}
