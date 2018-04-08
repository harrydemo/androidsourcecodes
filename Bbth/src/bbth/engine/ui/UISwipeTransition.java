package bbth.engine.ui;

import android.graphics.Canvas;
import bbth.engine.util.MathUtils;

public class UISwipeTransition {

	public static enum Direction {
		FROM_RIGHT, FROM_LEFT
	}

	private float duration, elapsed, dx, width;
	private Direction direction;
	private boolean first;

	public UISwipeTransition(float width, Direction dir, float duration) {
		this.duration = duration;
		this.direction = dir;
		this.width = width;
	}

	public void onUpdate(float seconds) {
		if (first) {
			first = false;
			return;
		}

		elapsed += seconds;

		switch (direction) {
		case FROM_RIGHT:
			dx = -MathUtils.lerp(0, width,
					MathUtils.scale(0, duration, 0, 1, elapsed, true));
			break;
		case FROM_LEFT:
			dx = MathUtils.lerp(0, width,
					MathUtils.scale(0, duration, 0, 1, elapsed, true));
			break;
		}

	}

	public void onDraw(Canvas canvas, UIView currentView, UIView nextView) {
		canvas.save();
		canvas.translate(dx, 0);
		currentView.onDraw(canvas);
		canvas.restore();
		if (elapsed > 0) {
			canvas.save();
			canvas.translate((direction == Direction.FROM_RIGHT ? width + dx
					: -width + dx), 0);
			nextView.onDraw(canvas);
			canvas.restore();
		}
	}

	public boolean isDone() {
		return elapsed >= duration;
	}

	public void reset() {
		elapsed = 0;
		first = true;
		switch (direction) {
		case FROM_RIGHT:
			dx = -1;
			break;
		case FROM_LEFT:
			dx = 1;
			break;
		}
	}

}
