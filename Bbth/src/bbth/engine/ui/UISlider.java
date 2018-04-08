package bbth.engine.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import bbth.engine.util.MathUtils;
import bbth.engine.util.Point;

public class UISlider extends UIControl {
	/**
	 * Values related to the logical slider.
	 */
	private float minValue, maxValue, currValue, range;
	private boolean isDepressed;
	private RectF barRect;

	/**
	 * Stuff that has to do with drawing. This is probably all entangled up and
	 * whatnot. >_<
	 */
	private Point circleLocation;
	private Paint paint;
	private float cornerRadius;
	private RectF emptyBarRect, filledBarRect;
	private float barHeight;
	private float circleRadius;
	private Shader upCircleShader, downCircleShader, filledShader, emptyShader;

	private int fillColor;

	public UISlider() {
		this(0.f, 1.f, 0.f);
	}

	public UISlider(float defaultValue) {
		this(0.f, 1.f, defaultValue);
	}

	public UISlider(float minValue, float maxValue, float defaultValue) {
		this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		this.paint
				.setStrokeWidth(UIDefaultConstants.UI_SLIDER_CIRCLE_BORDER_WIDTH);

		this.barRect = new RectF();
		this.filledBarRect = new RectF();
		this.emptyBarRect = new RectF();
		this.circleLocation = new Point();

		this.cornerRadius = UIDefaultConstants.CORNER_RADIUS;
		this.barHeight = UIDefaultConstants.UI_SLIDER_BAR_HEIGHT;
		this.circleRadius = UIDefaultConstants.UI_SLIDER_CIRCLE_RADIUS;
		this.isDepressed = false;

		this.fillColor = UIDefaultConstants.ACTIVE_COLOR;

		this.setRange(minValue, maxValue);
		this.setValue(defaultValue);
	}

	public void setFillColor(int color) {
		this.fillColor = color;
		this.filledShader = UIDefaultConstants
				.generateD2LVerticalLinearGradient(filledBarRect,
						this.fillColor);
	}

	public void setRange(float minValue, float maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.range = maxValue - minValue;
		recomputeDrawingLocations();
	}

	public void setValue(float value) {
		this.currValue = MathUtils.clamp(this.minValue, this.maxValue, value);
		this.recomputeDrawingLocations();
	}

	public float getValue() {
		return this.currValue;
	}

	@Override
	protected void setBounds(float left, float top, float right, float bottom) {
		super.setBounds(left, top, right, bottom);
		this.recomputeDrawingLocations();
	}

	private void recomputeDrawingLocations() {
		this.circleRadius = _h_height;
		this.barHeight = _rect.height() / 2.f;

		float halfBarHeight = this.barHeight / 2.f;
		float centerY = center.y;

		this.emptyBarRect.set(_rect.left, centerY - halfBarHeight, _rect.right,
				centerY + halfBarHeight);
		this.barRect.set(_rect.left + circleRadius, centerY - halfBarHeight,
				_rect.right - circleRadius, centerY + halfBarHeight);

		this.circleLocation.set(this.barRect.left + (currValue - this.minValue)
				/ range * (this.barRect.width()), _rect.centerY());
		this.filledBarRect.set(emptyBarRect.left, emptyBarRect.top,
				circleLocation.x, emptyBarRect.bottom);

		this.upCircleShader = UIDefaultConstants
				.generateD2LVerticalLinearGradient(_rect,
						UIDefaultConstants.BACKGROUND_COLOR);
		this.downCircleShader = UIDefaultConstants
				.generateD2LVerticalLinearGradient(_rect,
						UIDefaultConstants.FOREGROUND_COLOR);

		this.filledShader = UIDefaultConstants
				.generateD2LVerticalLinearGradient(filledBarRect,
						this.fillColor);
		this.emptyShader = UIDefaultConstants
				.generateD2LVerticalLinearGradient(emptyBarRect,
						UIDefaultConstants.BACKGROUND_COLOR);
	}

	private void recomputeValueWithTouch(float touchLoc) {
		float computedValue = this.minValue + (touchLoc - _rect.left)
				/ (_rect.width()) * (this.range);
		this.setValue(computedValue);
	}

	@Override
	public boolean containsPoint(float x, float y) {
		if (isDepressed) {
			return true;
		} else {
			boolean onBar = super.containsPoint(x, y);
			boolean inCircle = MathUtils.getDistSqr(x, y, circleLocation.x,
					circleLocation.y) < this.circleRadius * this.circleRadius
					* 9.f;

			return onBar || inCircle;
		}
	}

	@Override
	public void onTouchDown(float x, float y) {
		super.onTouchDown(x, y);

		this.isDepressed = true;
		this.recomputeValueWithTouch(x);
	}

	@Override
	public void onTouchMove(float x, float y) {
		super.onTouchMove(x, y);

		if (this.isDepressed) {
			this.recomputeValueWithTouch(x);
		}
	}

	@Override
	public void onTouchUp(float x, float y) {
		super.onTouchUp(x, y);

		if (this.isDepressed) {
			this.isDepressed = false;
			this.recomputeValueWithTouch(x);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// The background rectangle
		paint.setShader(this.emptyShader);
		canvas.drawRoundRect(this.emptyBarRect, this.cornerRadius,
				this.cornerRadius, paint);

		// The overlay filled rectangle
		paint.setShader(this.filledShader);
		canvas.drawRoundRect(this.filledBarRect, this.cornerRadius,
				this.cornerRadius, paint);
		paint.setShader(null);

		// The little circle dealy
		paint.setColor(UIDefaultConstants.FOREGROUND_COLOR);
		paint.setStyle(Style.STROKE);
		canvas.drawCircle(this.circleLocation.x, this.circleLocation.y,
				this.circleRadius, paint);
		paint.setStyle(Style.FILL);

		if (this.isDepressed) {
			paint.setShader(this.downCircleShader);
		} else {
			paint.setShader(this.upCircleShader);
		}

		canvas.drawCircle(this.circleLocation.x, this.circleLocation.y,
				this.circleRadius, paint);
		paint.setShader(null);
	}
}
