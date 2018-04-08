package bbth.engine.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;

public class UIRadioButton extends UIControl {
	private Paint _bg_paint, _fg_paint;
	private int _bg_color, _fg_color;
	private LinearGradient _bg_gradient, _fg_gradient, _fg_down_gradient;

	private boolean _selected, _in_select_motion;
	private float _inner_radius_ratio, _outer_radius;
	private UILabel _label;

	public UIRadioButton(String label) {
		_bg_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		_bg_paint.setStrokeWidth(UIDefaultConstants.BORDER_WIDTH);

		_fg_paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		_outer_radius = Math.min(_rect.width(), _rect.height()) / 2;
		_inner_radius_ratio = UIDefaultConstants.UI_RADIO_BUTTON_INNER_RADIUS_RATIO;
		_selected = false;
		_in_select_motion = false;

		// _bg_paint.setColor(DEFAULT_BG);
		setBackgroundColor(UIDefaultConstants.BACKGROUND_COLOR);
		// _fg_paint.setColor(DEFAULT_FG);
		setForegroundColor(UIDefaultConstants.FOREGROUND_COLOR);

		if (label != null && !label.equals("")) { //$NON-NLS-1$
			_label = new UILabel(label);
			_label.setPosition(1.5f * UIDefaultConstants.UI_RADIO_BUTTON_WIDTH,
					0);
			_label.setSize(0, UIDefaultConstants.UI_RADIO_BUTTON_LABEL_HEIGHT);
			_label.setTextSize(UIDefaultConstants.UI_RADIO_BUTTON_LABEL_HEIGHT);
			_label.sizeToFit();
			addSubview(_label);
			setSize(1.5f * UIDefaultConstants.UI_RADIO_BUTTON_WIDTH
					+ _label._width,
					UIDefaultConstants.UI_RADIO_BUTTON_LABEL_HEIGHT);
		} else {
			setSize(UIDefaultConstants.UI_RADIO_BUTTON_WIDTH,
					UIDefaultConstants.UI_RADIO_BUTTON_LABEL_HEIGHT);
		}
	}

	public UIRadioButton(String text, Object tag) {
		this(text);
		this.tag = tag;
	}

	@Override
	public void onTouchMove(float x, float y) {
		if (_in_select_motion && !containsPoint(x, y)) {
			_in_select_motion = false;
		}
	}

	@Override
	public void onTouchDown(float x, float y) {
		_in_select_motion = true;
	}

	@Override
	public void onTouchUp(float x, float y) {
		_selected = !_selected;
		_in_select_motion = false;
	}

	private static LinearGradient generateVerticalGradient(RectF boundary,
			int color, boolean startColor) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= 0.8f; // value component
		int c2 = Color.HSVToColor(hsv);

		if (startColor) {
			return new LinearGradient(boundary.left, boundary.top,
					boundary.left, boundary.bottom, color, c2,
					Shader.TileMode.MIRROR);
		} else {
			return new LinearGradient(boundary.left, boundary.top,
					boundary.left, boundary.bottom, c2, color,
					Shader.TileMode.MIRROR);
		}
	}

	public void setForegroundColor(int color) {
		_fg_color = color;
		_fg_gradient = generateVerticalGradient(_rect, color, true);
		_fg_paint.setColor(color);

		_fg_down_gradient = generateVerticalGradient(_rect, color, false);
	}

	public void setBackgroundColor(int color) {
		_bg_color = color;
		_bg_gradient = generateVerticalGradient(_rect, color, false);
		_bg_paint.setColor(color);
	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {
		super.setBounds(left, top, right, bottom);

		_outer_radius = Math.min(_rect.width(), _rect.height()) / 2;
		setBackgroundColor(_bg_color);
		setForegroundColor(_fg_color);
	}

	public boolean isSelected() {
		return _selected;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		_bg_paint.setStyle(Style.STROKE);
		canvas.drawCircle(_rect.left + _outer_radius, _rect.centerY(),
				_outer_radius, _bg_paint);
		_bg_paint.setStyle(Style.FILL);

		_bg_paint.setShader(_bg_gradient);
		canvas.drawCircle(_rect.left + _outer_radius, _rect.centerY(),
				_outer_radius, _bg_paint);
		_bg_paint.setShader(null);

		if (_in_select_motion) {
			_fg_paint.setShader(_fg_down_gradient);
			canvas.drawCircle(_rect.left + _outer_radius, _rect.centerY(),
					_outer_radius * _inner_radius_ratio, _fg_paint);
			_fg_paint.setShader(null);
		} else if (_selected) {
			_fg_paint.setShader(_fg_gradient);
			canvas.drawCircle(_rect.left + _outer_radius, _rect.centerY(),
					_outer_radius * _inner_radius_ratio, _fg_paint);
			_fg_paint.setShader(null);
		}
	}
}
