package bbth.engine.collision;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CompoundShape extends Shape {

	public Shape[] shapes;

	public CompoundShape(int count) {
		shapes = new Shape[count];
	}

	@Override
	public void updateWorldTransform(Transform parentTransform) {
		super.updateWorldTransform(parentTransform);
		for (Shape shape : shapes) {
			shape.updateWorldTransform(worldTransform);
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		for (Shape shape : shapes) {
			shape.draw(canvas, paint);
		}
	}

	@Override
	public boolean isCrossingLineSegment(float originX, float originY,
			float rayX, float rayY) {
		for (Shape shape : shapes) {
			if (shape.isCrossingLineSegment(originX, originY, rayX, rayY)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void recordPoints(SeparatingAxisTheorem theorem) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, Shape other) {
		theorem.flipRole();
		other.applyTheorem(theorem, this);
		theorem.flipRole();
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, CircleShape other) {
		for (Shape shape : shapes) {
			shape.applyTheorem(theorem, other);
			theorem.prepareForNewShapePair();
		}
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, PolygonShape other) {
		for (Shape shape : shapes) {
			shape.applyTheorem(theorem, other);
			theorem.prepareForNewShapePair();
		}
	}

	@Override
	public void applyTheorem(SeparatingAxisTheorem theorem, CompoundShape other) {
		for (Shape shape : shapes) {
			for (Shape otherShape : other.shapes) {
				shape.applyTheorem(theorem, otherShape);
				theorem.prepareForNewShapePair();
			}
		}
	}
}
