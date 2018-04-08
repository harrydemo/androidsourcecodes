package bbth.engine.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

public class UICheckBox extends UIControl {

	private Paint _paint;
	private Path _checkPath;
	private int _bg_color, _fg_color, _down_color, _disabled_color,
			bg_end_color;
	private LinearGradient _bg_gradient, _disabled_gradient, _down_gradient;

	private boolean _selected, isDown;
	public boolean isDisabled;
	private UILabel _label;
	private RectF _button_rect;

	public UICheckBox(String label) {

		_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		_paint.setStrokeWidth(UIDefaultConstants.BORDER_WIDTH);

		_checkPath = new Path();

		_selected = false;

		_button_rect = new RectF();

		if (label != null && !label.equals("")) { //$NON-NLS-1$
			_label = new UILabel(label, null);
			_label.setPosition(
					1.5f * UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH, 0);
			_label.setSize(0, UIDefaultConstants.UI_CHECKBOX_LABEL_HEIGHT);
			_label.setTextSize(UIDefaultConstants.UI_CHECKBOX_LABEL_HEIGHT);
			_label.sizeToFit();
			addSubview(_label);
			setSize(1.5f * UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH
					+ _label._width,
					UIDefaultConstants.UI_CHECKBOX_LABEL_HEIGHT);
		} else {
			setSize(UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH,
					UIDefaultConstants.UI_CHECKBOX_LABEL_HEIGHT);
		}

		setBackgroundColor(UIDefaultConstants.UI_CHECKBOX_BACKGROUND_COLOR);
		setForegroundColor(UIDefaultConstants.UI_CHECKBOX_FOREGROUND_COLOR);
		setDisabledColor(UIDefaultConstants.UI_BUTTON_DISABLED_COLOR);
		setButtonDownColor(bg_end_color);

	}

	@Override
	public boolean containsPoint(float x, float y) {
		float padding = _button_rect.height() * .7f;

		boolean inBox = super.containsPoint(x, y);
		boolean nearBox = x > this._button_rect.left - padding
				&& x < this._button_rect.right + padding
				&& y > this._button_rect.top - padding
				&& y < this._button_rect.bottom + padding;

		return nearBox || inBox;
	}

	@Override
	protected void layoutSubviews(boolean force) {

	}

	@Override
	public void onTouchDown(float x, float y) {
		if (isDisabled)
			return;
		isDown = true;
	}

	@Override
	public void onTouchUp(float x, float y) {
		_selected = !_selected;
		isDown = false;
	}

	@Override
	public void onTouchMove(float x, float y) {
		if (isDisabled)
			return;
		if (!containsPoint(x, y))
			isDown = false;

	}

	public void setForegroundColor(int color) {
		_fg_color = color;
	}

	public void setBackgroundColor(int color) {
		_bg_color = color;
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= 0.8f; // value component
		bg_end_color = Color.HSVToColor(hsv);

		_bg_gradient = new LinearGradient(_button_rect.left, _button_rect.top,
				_button_rect.left, _button_rect.bottom, color, bg_end_color,
				Shader.TileMode.MIRROR);
	}

	public void setDisabledColor(int color) {
		_disabled_color = color;
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= 1.1f; // value component
		int s_color = Color.HSVToColor(hsv);
		_disabled_gradient = new LinearGradient(_button_rect.left,
				_button_rect.top, _button_rect.left, _button_rect.bottom,
				s_color, color, Shader.TileMode.MIRROR);
	}

	public void setButtonDownColor(int color) {
		_down_color = color;
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= 0.8f; // value component
		int end_color = Color.HSVToColor(hsv);
		_down_gradient = new LinearGradient(_button_rect.left,
				_button_rect.top, _button_rect.left, _button_rect.bottom,
				_down_color, end_color, Shader.TileMode.MIRROR);
	}

	private void generatePath() {
		_checkPath.reset();
		_checkPath.moveTo(_button_rect.left + 3, _button_rect.top
				+ UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH * 0.25f);
		_checkPath.lineTo(_button_rect.centerX(), _button_rect.centerY());
		_checkPath.lineTo(_button_rect.centerX()
				+ UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH - 2,
				_button_rect.top - UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH
						* 0.25f);
		_checkPath.lineTo(_button_rect.centerX(), _button_rect.centerY()
				+ UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH * 0.25f);
		_checkPath.lineTo(_button_rect.left + 3, _button_rect.top
				+ UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH * 0.25f);
	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {
		super.setBounds(left, top, right, bottom);
		_button_rect.left = left;
		_button_rect.top = top;
		_button_rect.right = left + UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH;
		_button_rect.bottom = top + UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH;

		_label.setPosition(left + 1.5f
				* UIDefaultConstants.UI_CHECKBOX_BUTTON_WIDTH, top);

		setBackgroundColor(_bg_color);
		setDisabledColor(_disabled_color);
		setButtonDownColor(_down_color);
		generatePath();
	}

	public boolean isSelected() {
		return _selected;
	}

	public void setSelected(boolean value) {
		_selected = value;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		_paint.setStyle(Style.STROKE);
		_paint.setColor(_bg_color);
		canvas.drawRect(_button_rect, _paint);

		_paint.setStyle(Style.FILL);
		_paint.setShader((isDisabled ? _disabled_gradient
				: (isDown ? _down_gradient : _bg_gradient)));
		canvas.drawRect(_button_rect, _paint);

		_paint.setShader(null);

		if (_selected) {
			_paint.setStyle(Style.FILL_AND_STROKE);
			_paint.setColor(_fg_color);
			canvas.drawPath(_checkPath, _paint);
		}
	}
}
