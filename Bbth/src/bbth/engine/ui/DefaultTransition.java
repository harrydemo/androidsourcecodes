package bbth.engine.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import bbth.engine.util.Envelope;

public class DefaultTransition extends Transition {
	Envelope newAlpha, oldAlpha, newX, oldX, newY, oldY, newScale, oldScale;
	float length, currentTime;

	public DefaultTransition(float length) {
		this(length, new Envelope(255f,
				Envelope.OutOfBoundsHandler.RETURN_FIRST_OR_LAST),
				new Envelope(255f,
						Envelope.OutOfBoundsHandler.RETURN_FIRST_OR_LAST),
				Envelope.ALWAYS_ZERO, Envelope.ALWAYS_ZERO,
				Envelope.ALWAYS_ZERO, Envelope.ALWAYS_ZERO,
				Envelope.ALWAYS_ONE, Envelope.ALWAYS_ONE);
	}

	public DefaultTransition(float length, Envelope newAlpha,
			Envelope oldAlpha, Envelope newX, Envelope oldX, Envelope newY,
			Envelope oldY, Envelope newScale, Envelope oldScale) {
		this.length = length;
		this.newAlpha = newAlpha;
		this.oldAlpha = oldAlpha;
		this.newX = newX;
		this.oldX = oldX;
		this.newY = newY;
		this.oldY = oldY;
		this.newScale = newScale;
		this.oldScale = oldScale;
	}

	public void setNewAlpha(Envelope newAlpha) {
		this.newAlpha = newAlpha;
	}

	public void setOldAlpha(Envelope oldAlpha) {
		this.oldAlpha = oldAlpha;
	}

	public void setNewX(Envelope newX) {
		this.newX = newX;
	}

	public void setOldX(Envelope oldX) {
		this.oldX = oldX;
	}

	public void setNewY(Envelope newY) {
		this.newY = newY;
	}

	public void setOldY(Envelope oldY) {
		this.oldY = oldY;
	}

	public void setNewScale(Envelope newScale) {
		this.newScale = newScale;
	}

	public void setOldScale(Envelope oldScale) {
		this.oldScale = oldScale;
	}

	private Rect clipBounds = new Rect();

	private void drawLayer(UIView view, Canvas canvas, Envelope alpha,
			Envelope x, Envelope y, Envelope scale) {
		canvas.getClipBounds(clipBounds);
		int restoreNumber = canvas.saveLayerAlpha(0f, 0f,
				(float) canvas.getWidth(), (float) canvas.getHeight(),
				(int) Math.round(alpha.getValueAtTime(currentTime)),
				Canvas.ALL_SAVE_FLAG);
		float dx = (float) x.getValueAtTime(currentTime);
		float dy = (float) y.getValueAtTime(currentTime);
		float scaleAmount = (float) scale.getValueAtTime(currentTime);
		canvas.translate(dx, dy);
		canvas.clipRect(0f, 0f, scaleAmount * clipBounds.width(), scaleAmount
				* clipBounds.height());
		canvas.scale(scaleAmount, scaleAmount);

		view.onDraw(canvas);

		canvas.restoreToCount(restoreNumber);
	}

	@Override
	public void draw(Canvas canvas, UIView oldView, UIView newView) {
		drawLayer(oldView, canvas, oldAlpha, oldX, oldY, oldScale);
		drawLayer(newView, canvas, newAlpha, newX, newY, newScale);
	}

	@Override
	public boolean isDone() {
		return currentTime >= length;
	}

	@Override
	public void setTime(float transitionTime) {
		currentTime = transitionTime;
	}

}
