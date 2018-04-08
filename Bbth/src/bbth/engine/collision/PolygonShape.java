package bbth.engine.collision;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import bbth.engine.util.Point;

public class PolygonShape extends Shape {

	public Point[] relativePoints;
	public Point[] worldPoints;
	private Path path;

	public PolygonShape(int count) {
		relativePoints = new Point[count];
		worldPoints = new Point[count];
		path = new Path();
		for (int i = 0; i < count; i++) {
			relativePoints[i] = new Point();
			worldPoints[i] = new Point();
		}
	}

	public PolygonShape(Point[] points) {
		relativePoints = points;
		worldPoints = new Point[points.length];
		path = new Path();
		for (int i = 0; i < points.length; i++) {
			worldPoints[i] = new Point();
		}
	}

	@Override
	public boolean isCrossingLineSegment(float originX, float originY,
			float rayX, float rayY) {
		for (int i = 0, j = worldPoints.length - 1; i < worldPoints.length; j = i++) {
			float ix = worldPoints[i].x;
			float iy = worldPoints[i].y;
			float jx = worldPoints[j].x;
			float jy = worldPoints[j].y;
			float denom = rayY * (jx - ix) - rayX * (jy - iy);
			if (denom != 0) {
				float t1 = (rayX * (iy - originY) - rayY * (ix - originX))
						/ denom;
				float t2 = ((jx - ix) * (iy - originY) - (jy - iy)
						* (ix - originX))
						/ denom;
				if (t1 > 0 && t1 < 1 && t2 > 0 && t2 < 1) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void updateWorldTransform(Transform parentTransform) {
		super.updateWorldTransform(parentTransform);
		for (int i = 0; i < relativePoints.length; i++) {
			worldTransform.transform(relativePoints[i], worldPoints[i]);
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		for (int i = 0; i < worldPoints.length; i++) {
			if (i == 0) {
				path.moveTo(worldPoints[i].x, worldPoints[i].y);
			} else {
				path.lineTo(worldPoints[i].x, worldPoints[i].y);
			}
		}

		canvas.drawPath(path, paint);
		path.rewind();
	}

	private void applyTheoremToCircle(SeparatingAxisTheorem theorem,
			CircleShape other) {
		for (int i = 0; i < worldPoints.length; i++) {
			theorem.changeAxis(
					other.worldTransform.offset.x - worldPoints[i].x,
					other.worldTransform.offset.y - worldPoints[i].y);
			recordPoints(theorem);
			theorem.storeRange();
			other.recordPoints(theorem);
			theorem.compareRanges();
		}
	}

	private void applyTheoremToSimpleShape(SeparatingAxisTheorem theorem,
			Shape other) {
		for (int i = 0, j = worldPoints.length - 1; i < worldPoints.length; j = i++) {
			theorem.changeAxis(worldPoints[i].y - worldPoints[j].y,
					worldPoints[j].x - worldPoints[i].x);
			recordPoints(theorem);
			theorem.storeRange();
			other.recordPoints(theorem);
			theorem.compareRanges();
		}
	}

	@Override
	public void recordPoints(SeparatingAxisTheorem theorem) {
		for (Point p : worldPoints) {
			theorem.recordPoint(p.x, p.y);
		}
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, Shape other) {
		theorem.flipRole();
		other.applyTheorem(theorem, this);
		theorem.flipRole();
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, CircleShape other) {
		applyTheoremToSimpleShape(theorem, other);
		applyTheoremToCircle(theorem, other);
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, PolygonShape other) {
		applyTheoremToSimpleShape(theorem, other);
		theorem.flipRole();
		other.applyTheoremToSimpleShape(theorem, this);
		theorem.flipRole();
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, CompoundShape other) {
		theorem.flipRole();
		other.applyTheorem(theorem, this);
		theorem.flipRole();
	}
}
