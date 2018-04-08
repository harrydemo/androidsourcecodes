package bbth.engine.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.Shader;
import bbth.engine.util.MathUtils;
import bbth.game.R;

public class UISwitch extends UIControl {
	private static float MIN_OFFSET_DURATION = .01f;

	private UILabel onTextLabel, offTextLabel;
	private RectF switchButtonRect, onBackgroundRect, offBackgroundRect;
	private Paint paint;
	private Shader buttonUpGradient, buttonDownGradient, onGradient,
			offGradient;

	private boolean isOn, userDragging, isAnimating, continueAnimation;
	private float labelWidth, duration, elapsed, offset, touchDownX;

	private int onBackgroundColor;

	public UISwitch() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		this.onBackgroundColor = UIDefaultConstants.ACTIVE_COLOR;

		this.onTextLabel = new UILabel(R.string.on, this);
		this.onTextLabel.setTextColor(Color.WHITE);
		this.onTextLabel.setBold(true);

		this.offTextLabel = new UILabel(R.string.off, this);
		this.offTextLabel
				.setTextColor(UIDefaultConstants.UI_BUTTON_DISABLED_COLOR);
		this.offTextLabel.setBold(true);

		this.switchButtonRect = new RectF();

		this.onBackgroundRect = new RectF();
		this.offBackgroundRect = new RectF();

		this.duration = .25f;

		this.recomputeDrawLocations();
	}

	public void setOn(boolean value) {
		isOn = value;
		offset = (isOn ? 0 : labelWidth);
	}

	public void setOnBackgroundColor(int color) {
		this.onBackgroundColor = color;
		this.onGradient = UIDefaultConstants.generateD2LVerticalLinearGradient(
				onBackgroundRect, this.onBackgroundColor);
	}

	public void setOnTextColor(int color) {
		this.onTextLabel.setTextColor(color);
	}

	public boolean isOn() {
		return this.isOn;
	}

	@Override
	public boolean containsPoint(float x, float y) {
		if (this.userDragging) {
			return true;
		} else {
			float padding = this.switchButtonRect.height() * 0.7f;

			boolean inSwitch = super.containsPoint(x, y);
			boolean onButton = x > this.switchButtonRect.left - this.offset
					- padding
					&& x < this.switchButtonRect.right - this.offset + padding
					&& y > this.switchButtonRect.top - padding
					&& y < this.switchButtonRect.bottom + padding;

			return inSwitch || onButton;
		}
	}

	@Override
	protected void setBounds(float left, float top, float right, float bottom) {
		super.setBounds(left, top, right, bottom);
		this.recomputeDrawLocations();
	}

	private void recomputeDrawLocations() {
		this.labelWidth = _width * 7.f / 12.f;

		this.onTextLabel.setPosition(_rect.left, _rect.top);
		this.onTextLabel.setSize(labelWidth, _height);

		this.switchButtonRect.set(_rect.left + labelWidth, _rect.top,
				_rect.right, _rect.bottom);

		this.offTextLabel.setPosition(_rect.right, _rect.top);
		this.offTextLabel.setSize(labelWidth, _height);

		this.onBackgroundRect.set(this.onTextLabel.getRect());
		this.onBackgroundRect.right = (this.onBackgroundRect.right + _rect.right) / 2;

		this.offBackgroundRect.set(this.offTextLabel.getRect());
		this.offBackgroundRect.left = (this.offBackgroundRect.left + center.x) / 2;

		this.buttonUpGradient = UIDefaultConstants
				.generateD2LVerticalLinearGradient(switchButtonRect,
						UIDefaultConstants.BACKGROUND_COLOR);
		this.buttonDownGradient = UIDefaultConstants
				.generateD2LVerticalLinearGradient(switchButtonRect,
						UIDefaultConstants.FOREGROUND_COLOR);

		// this.onGradient =
		// UIDefaultConstants.generateD2LVerticalLinearGradient(onBackgroundRect,
		// UIDefaultConstants.ACTIVE_COLOR);
		this.onGradient = UIDefaultConstants.generateD2LVerticalLinearGradient(
				onBackgroundRect, this.onBackgroundColor);

		this.offGradient = UIDefaultConstants
				.generateD2LVerticalLinearGradient(offBackgroundRect,
						UIDefaultConstants.UI_SWITCH_OFF_COLOR);

		offset = (this.isOn) ? 0 : this.labelWidth;
	}

	@Override
	public void onUpdate(float seconds) {
		super.onUpdate(seconds);

		if (this.isAnimating) {
			this.elapsed += seconds;

			float offsetVal = MathUtils.lerp(0, labelWidth,
					MathUtils.scale(0, duration, 0, 1, elapsed, true));

			if (this.continueAnimation) {
				if (this.isOn) {
					offset = offsetVal;
				} else {
					offset = this.labelWidth - offsetVal;
				}
			} else {
				if (this.isOn) {
					offset = this.labelWidth - offsetVal;
				} else {
					offset = offsetVal;
				}
			}
		}

		if (this.isAnimating && this.elapsed >= this.duration) {
			if (this.continueAnimation) {
				this.isOn = !this.isOn;
			}

			this.isAnimating = false;
			this.continueAnimation = false;
		}
	}

	@Override
	public void onTouchDown(float x, float y) {
		super.onTouchDown(x, y);

		this.userDragging = true;

		this.elapsed = 0;
		this.touchDownX = x;
		this.isAnimating = false;
	}

	@Override
	public void onTouchMove(float x, float y) {
		super.onTouchMove(x, y);

		if (this.userDragging) {
			this.offset = MathUtils.clamp(0, this.labelWidth, offset
					- (x - touchDownX));
			this.elapsed = MathUtils.scale(0, this.labelWidth, 0, duration,
					offset, true);
			this.touchDownX = x;
		}
	}

	@Override
	public void onTouchUp(float x, float y) {
		super.onTouchUp(x, y);

		this.isAnimating = true;
		this.userDragging = false;

		// THIS DOESNT MAKE ANY SENSE
		if ((this.isOn && elapsed > UISwitch.MIN_OFFSET_DURATION)
				|| (!this.isOn && elapsed < this.duration
						- UISwitch.MIN_OFFSET_DURATION)) {
			if ((this.isOn && elapsed > duration / 2.f)
					|| (!this.isOn && elapsed < duration / 2.f)) {
				this.continueAnimation = true;
			} else {
				this.continueAnimation = false;
			}
		} else {
			this.continueAnimation = true;
		}

		if ((!this.isOn && this.continueAnimation)
				|| (this.isOn && !this.continueAnimation)) {
			this.elapsed = duration - this.elapsed;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		canvas.clipRect(_rect, Op.INTERSECT);

		canvas.translate(-this.offset, 0);

		// Backgrounds for the labels
		paint.setShader(this.onGradient);
		canvas.drawRoundRect(this.onBackgroundRect,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS, paint);
		paint.setShader(this.offGradient);
		canvas.drawRoundRect(this.offBackgroundRect,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS, paint);
		paint.setShader(null);

		this.onTextLabel.onDraw(canvas);

		// Button border
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(UIDefaultConstants.UI_SWITCH_BUTTON_BORDER);
		paint.setColor(UIDefaultConstants.FOREGROUND_COLOR);
		canvas.drawRoundRect(this.switchButtonRect,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS, paint);
		paint.setStyle(Style.FILL);

		// The button itself
		paint.setShader(userDragging ? this.buttonDownGradient
				: this.buttonUpGradient);
		canvas.drawRoundRect(this.switchButtonRect,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS, paint);
		paint.setShader(null);

		this.offTextLabel.onDraw(canvas);

		canvas.restore();

		// Outside border
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(UIDefaultConstants.UI_SWITCH_BORDER);
		paint.setColor(Color.BLACK);
		canvas.drawRoundRect(_rect, UIDefaultConstants.UI_SWITCH_CORNER_RADIUS,
				UIDefaultConstants.UI_SWITCH_CORNER_RADIUS, paint);
		paint.setStyle(Style.FILL);
	}
}
