package bbth.engine.sound;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * Represents either a tap or a hold. All times are in terms of milliseconds
 * from the start of a song. Appears to be a simple struct outside this package
 * 
 * @author jardini
 * 
 */
public class Beat {
	public final static float RADIUS = 8.f;
	private final static int OFFSET_DENOM = 12;
	private static final int TAPPED_COLOR = Color.YELLOW;
	private static final int INCOMING_COLOR = Color.GRAY;
	private static final int MISSED_COLOR = Color.DKGRAY;

	public enum BeatType {
		TAP, HOLD, REST
	}

	public final BeatType type;
	public final float duration;

	// start time internally used by this and BeatTracker only
	float _startTime;
	private boolean _tapped;
	private RectF cachedRect = new RectF();

	// package private
	Beat(BeatType beatType, float durationMillis) {
		this(beatType, durationMillis, -1);
	}

	// package private
	Beat(BeatType beatType, float durationMillis, float startTime) {
		type = beatType;
		duration = durationMillis;
		_startTime = startTime;
		_tapped = false;
	}

	public static Beat tap(float duration) {
		return new Beat(BeatType.TAP, duration);
	}

	public static Beat hold(float duration) {
		return new Beat(BeatType.HOLD, duration);
	}

	public static Beat rest(float duration) {
		return new Beat(BeatType.REST, duration);
	}

	public static Beat tap(float duration, float startTime) {
		return new Beat(BeatType.TAP, duration, startTime);
	}

	public static Beat hold(float duration, float startTime) {
		return new Beat(BeatType.HOLD, duration, startTime);
	}

	public static Beat rest(float duration, float startTime) {
		return new Beat(BeatType.REST, duration, startTime);
	}

	// returns true if this noted was tapped, false if the note was missed
	public boolean onTouchDown(int songTime) {
		if (_tapped || type == BeatType.REST)
			return false;

		boolean changed = false;
		if (Math.abs(songTime - _startTime) < BeatTracker.TOLERANCE) {
			_tapped = true;
			changed = true;
		}

		return changed;
	}

	// returns true if in the touch zone
	boolean inTouchZone(int songTime) {
		return Math.abs(songTime - _startTime) < BeatTracker.TOLERANCE;
	}

	float getEndTime() {
		if (type == BeatType.HOLD) {
			return _startTime + duration;
		}
		return _startTime;
	}

	// draw the note given a location for taps
	public void draw(int songTime, float x, float yMiddle, Canvas canvas,
			Paint paint) {
		if (type == BeatType.REST)
			return;

		float stroke = paint.getStrokeWidth();
		float offset = songTime - _startTime;
		float y1 = yMiddle + offset / OFFSET_DENOM;
		float y2 = yMiddle + (offset - duration) / OFFSET_DENOM;

		// Black out the beat track line first
		if (type == BeatType.HOLD) {
			paint.setStrokeWidth(stroke * 2);
			paint.setColor(Color.BLACK);
			canvas.drawLine(x, y1 + RADIUS - stroke, x, y2 - RADIUS + stroke,
					paint);
			paint.setStrokeWidth(stroke);
		}

		// Set the tap color
		if (_tapped) {
			paint.setColor(TAPPED_COLOR);
		} else if (offset > BeatTracker.TOLERANCE) {
			paint.setColor(MISSED_COLOR);
		} else {
			paint.setColor(INCOMING_COLOR);
		}

		// Draw the dot where they need to tap
		canvas.drawCircle(x, y1, RADIUS, paint);

		// Draw the length of the hold
		if (type == BeatType.HOLD) {
			paint.setStyle(Style.STROKE);
			cachedRect.left = x - RADIUS + stroke / 2;
			cachedRect.right = x + RADIUS - stroke / 2;
			cachedRect.top = y2 - RADIUS;
			cachedRect.bottom = y2 + RADIUS;
			canvas.drawArc(cachedRect, 180, 180, false, paint);
			paint.setStyle(Style.FILL);
			canvas.drawLine(cachedRect.left, y1, cachedRect.left, y2, paint);
			canvas.drawLine(cachedRect.right, y1, cachedRect.right, y2, paint);
		}
	}

	public boolean isTapped() {
		return _tapped;
	}
}
