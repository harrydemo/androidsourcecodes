package bbth.engine.ui;

import java.util.Collection;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import bbth.engine.core.GameScreen;
import bbth.engine.util.BlockingArrayList;
import bbth.engine.util.Point;

public class UIView extends GameScreen {

	// protected CopyOnWriteArrayList<UIView> subviews;
	protected BlockingArrayList<UIView> subviews;
	protected boolean _hasAppeared, _layedOut;
	public Object tag;
	protected RectF _rect;
	protected float _width, _height, _h_width, _h_height;
	protected int subviewCount;
	protected Point center;
	protected UIDelegate delegate;
	protected Point _position;
	protected Rect _clip_rect;
	protected float target_x, target_y;
	float position_x_step, position_y_step, position_animation_duration;
	public boolean isAnimatingPosition;

	protected Anchor anchor;

	public UIView() {
		this(null);
	}

	public UIView(Object tag) {
		// subviews = new CopyOnWriteArrayList<UIView>();
		subviews = new BlockingArrayList<UIView>();
		this.anchor = Anchor.TOP_LEFT;
		_rect = new RectF(0, 0, 0, 0);
		_clip_rect = new Rect(0, 0, 0, 0);
		center = new Point(0, 0);
		_position = new Point(0, 0);
		this.tag = tag;
	}

	@Override
	public void onUpdate(float seconds) {
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			e.onUpdate(seconds);
		}
		linearInterpolatePosition(seconds);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (!_hasAppeared)
			willAppear(true);
		// canvas.save();
		// canvas.clipRect(_rect);
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			// if(RectF.intersects(_rect, e._rect))
			e.onDraw(canvas);
		}
		// canvas.restore();
	}

	@Override
	public void onTouchDown(float x, float y) {
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			if (e.containsPoint(x, y)) {
				e.onTouchDown(x, y);
			}
		}
	}

	@Override
	public void onTouchUp(float x, float y) {
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			if (e.containsPoint(x, y)) {
				e.onTouchUp(x, y);
			}
		}
	}

	@Override
	public void onTouchMove(float x, float y) {
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			e.onTouchMove(x, y);
		}
	}

	protected void setBounds(float left, float top, float right, float bottom) {
		_rect.left = left;
		_rect.top = top;
		_rect.right = right;
		_rect.bottom = bottom;

		center.x = _rect.left + _h_width;
		center.y = _rect.top + _h_height;
	}

	public void addSubview(UIView subview) {
		if (!subviews.contains(subview)) {
			subviews.add(subview);
			subviewCount = subviews.size();
		}
	}

	public void removeSubview(UIView subview) {
		subview.willHide(true);
		subviews.remove(subview);
		subviewCount = subviews.size();
	}

	public void removeSubviews(Collection<UIView> views) {
		// subviews.removeAll(views);
	}

	public void willAppear(boolean animated) {
		_hasAppeared = true;
		layoutSubviews(false);

		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			e.willAppear(animated);
		}
	}

	public void willHide(boolean animated) {
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			e.willHide(animated);
		}
	}

	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
	}

	public void setSize(float width, float height) {
		this._width = width;
		this._height = height;
		this._h_width = _width / 2.f;
		this._h_height = _height / 2.f;

		switch (this.anchor) {
		case TOP_LEFT:
			this.setBounds(_rect.left, _rect.top, _rect.left + _width,
					_rect.top + _height);
			break;
		case TOP_CENTER:
			this.setBounds(center.x - _h_width, _rect.top, center.x + _h_width,
					_rect.top + _height);
			break;
		case TOP_RIGHT:
			this.setBounds(_rect.right - _width, _rect.top, _rect.right,
					_rect.top + _height);
			break;
		case CENTER_LEFT:
			this.setBounds(_rect.left, center.y - _h_height, _rect.left
					+ _width, center.y + _h_height);
			break;
		case CENTER_CENTER:
			this.setBounds(center.x - _h_width, center.y - _h_height, center.x
					+ _h_width, center.y + _h_height);
			break;
		case CENTER_RIGHT:
			this.setBounds(_rect.right - _width, center.y - _h_height,
					_rect.right, center.y + _h_height);
			break;
		case BOTTOM_LEFT:
			this.setBounds(_rect.left, _rect.bottom - _height, _rect.left
					+ _width, _rect.bottom);
			break;
		case BOTTOM_CENTER:
			this.setBounds(center.x - _h_width, _rect.bottom - _height,
					center.x + _h_width, _rect.bottom);
			break;
		case BOTTOM_RIGHT:
			this.setBounds(_rect.right - _width, _rect.bottom - _height,
					_rect.right, _rect.bottom);
			break;
		}
	}

	public void setPosition(float x, float y) {
		switch (this.anchor) {
		case TOP_LEFT:
			this.setBounds(x, y, x + _width, y + _height);
			break;
		case TOP_CENTER:
			this.setBounds(x - _h_width, y, x + _h_width, y + _height);
			break;
		case TOP_RIGHT:
			this.setBounds(x - _width, y, x, y + _height);
			break;
		case CENTER_LEFT:
			this.setBounds(x, y - _h_height, x + _width, y + _height / 2.f);
			break;
		case CENTER_CENTER:
			this.setBounds(x - _h_width, y - _h_height, x + _h_width, y
					+ _height / 2.f);
			break;
		case CENTER_RIGHT:
			this.setBounds(x - _width, y - _h_height, x, y + _height / 2.f);
			break;
		case BOTTOM_LEFT:
			this.setBounds(x, y - _height, x + _width, y);
			break;
		case BOTTOM_CENTER:
			this.setBounds(x - _h_width, y - _height, x + _h_width, y);
			break;
		case BOTTOM_RIGHT:
			this.setBounds(x - _width, y - _height, x, y);
			break;
		}

		_position.x = x;
		_position.y = y;
	}

	public Point getPosition() {
		return _position;
	}

	public boolean containsPoint(float x, float y) {
		return this._rect.contains(x, y);
	}

	public void setDelegate(UIDelegate delegate) {
		this.delegate = delegate;
	}

	protected void layoutSubviews(boolean force) {
		// derp
	}

	public void animatePosition(float target_x, float target_y, float duration) {
		this.target_x = target_x;
		this.target_y = target_y;
		this.position_x_step = ((target_x - _position.x) / duration);
		this.position_y_step = ((target_y - _position.y) / duration);
		this.position_animation_duration = duration;
		this.isAnimatingPosition = true;
	}

	public void linearInterpolatePosition(float seconds) {
		if (isAnimatingPosition) {
			float new_x = (_position.x + position_x_step * seconds);
			float new_y = (_position.y + position_y_step * seconds);
			this.setPosition(new_x, new_y);
			position_animation_duration -= seconds;
			if (position_animation_duration <= 0) {
				isAnimatingPosition = false;
				this.setPosition(target_x, target_y);
			}
		}
	}

	public boolean isDraggable() {
		return false;
	}

	public RectF getRect() {
		return _rect;
	}

	public float getWidth() {
		return _width;
	}

	public float getHeight() {
		return _height;
	}

	@Override
	public void onStop() {
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			e.onStop();
		}
	}

	public boolean shouldPlayMenuMusic() {
		return true;
	}
}