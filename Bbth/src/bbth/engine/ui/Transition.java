package bbth.engine.ui;

import android.graphics.Canvas;

public abstract class Transition {
	public abstract void draw(Canvas canvas, UIView oldView, UIView newView);

	public abstract boolean isDone();

	public abstract void setTime(float transitionTime);
}
