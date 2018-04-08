package bbth.engine.achievements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import bbth.engine.core.GameActivity;
import bbth.engine.util.MathUtils;
import bbth.game.R;

/**
 * An animation that occurs when an achievement is unlocked. The game must call
 * tick and draw until the time of the animation runs out
 * 
 * @author Justin
 * 
 */
public class UnlockAnimation {
	public static final float FADE_OUT_TIME = 0.6f;
	public static final float WAITING_TIME = 4.4f;
	public static final float TOTAL_TIME = 5;

	private final String _name;
	private float _timeLeft;

	public UnlockAnimation(String name) {
		_name = name;
		_timeLeft = TOTAL_TIME;
	}

	public void tick(float seconds) {
		_timeLeft -= seconds;
	}

	String achievementUnlockedString = GameActivity.instance
			.getString(R.string.achievementunlocked);

	// returns bottom
	public float draw(Canvas canvas, Paint paint, float width, float height,
			float top) {
		if (_timeLeft < 0) {
			return top;
		} else if (_timeLeft < FADE_OUT_TIME) {
			top = MathUtils.lerp(top - height, top, 1.666f * _timeLeft);
		} else if (_timeLeft >= WAITING_TIME) {
			top = MathUtils.lerp(top, top - height,
					1.666f * (_timeLeft - WAITING_TIME));
		}
		float bottom = top + height;

		// draw the background
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(0, top, width, bottom, paint);
		paint.setColor(Color.argb(120, 0, 0, 0));
		paint.setStyle(Style.FILL);
		canvas.drawRect(0, top, width, bottom, paint);

		// draw the title
		paint.setColor(Color.WHITE);
		// paint.setTextAlign(Align.CENTER);
		paint.setTextSize(height * 0.45f);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(achievementUnlockedString, width * 0.01f, top + height
				* 0.45f, paint); // width * 0.5f, top + height * 0.45f, paint);

		// draw the description
		paint.setTextSize(height * 0.35f);
		paint.setTypeface(Typeface.DEFAULT);
		canvas.drawText(_name, width * 0.01f, top + height * 0.85f, paint); // width
																			// *
																			// 0.5f,
																			// top
																			// +
																			// height
																			// *
																			// 0.85f,
																			// paint);

		return bottom;
	}

	public boolean isOver() {
		return _timeLeft < 0;
	}
}
