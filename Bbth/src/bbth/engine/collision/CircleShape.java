package bbth.engine.collision;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.FloatMath;
import bbth.engine.util.Point;

public class CircleShape extends Shape {

	public float radius;

	public CircleShape(float radius) {
		this.radius = radius;
	}

	public CircleShape(float x, float y, float radius) {
		relativeTransform.offset.x = x;
		relativeTransform.offset.y = y;
		this.radius = radius;
	}

	@Override
	public boolean isCrossingLineSegment(float originX, float originY,
			float rayX, float rayY) {
		float deltaX = originX - this.worldTransform.offset.x;
		float deltaY = originY - this.worldTransform.offset.y;
		float a = rayX * rayX + rayY * rayY;
		float b = 2 * (rayX * deltaX + rayY * deltaY);
		float c = deltaX * deltaX + deltaY * deltaY - radius * radius;
		float insideSqrt = b * b - 4 * a * c;
		if (insideSqrt > 0) {
			float t0 = -b / (2 * a);
			float t1 = FloatMath.sqrt(insideSqrt) / (2 * a);
			return (t0 + t1 > 0 && t0 + t1 < 1) || (t0 - t1 > 0 && t0 - t1 < 1);
		}
		return false;
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawCircle(worldTransform.offset.x, worldTransform.offset.y,
				radius, paint);
	}

	@Override
	public void recordPoints(SeparatingAxisTheorem theorem) {
		Point p = worldTransform.offset;
		theorem.recordPoint(p.x - theorem.axisX * radius, p.y - theorem.axisY
				* radius);
		theorem.recordPoint(p.x + theorem.axisX * radius, p.y + theorem.axisY
				* radius);
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, Shape other) {
		theorem.flipRole();
		other.applyTheorem(theorem, this);
		theorem.flipRole();
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, CircleShape other) {
		theorem.changeAxis(other.worldTransform.offset.x
				- worldTransform.offset.x, other.worldTransform.offset.y
				- worldTransform.offset.y);
		recordPoints(theorem);
		theorem.storeRange();
		other.recordPoints(theorem);
		theorem.compareRanges();
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, PolygonShape other) {
		theorem.flipRole();
		other.applyTheorem(theorem, this);
		theorem.flipRole();
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, CompoundShape other) {
		theorem.flipRole();
		other.applyTheorem(theorem, this);
		theorem.flipRole();
	}
}
