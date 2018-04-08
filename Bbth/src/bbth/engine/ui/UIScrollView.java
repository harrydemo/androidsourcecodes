package bbth.engine.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import bbth.engine.util.MathUtils;

public class UIScrollView extends UIView {

	protected float dx, dy, _x, _y, pos_x, pos_y, max_x, max_y, max_offset_y,
			max_offset_x, _ix, _iy, _vx, _vy;
	protected boolean isScrolling, isDown, scrollsHorizontal = true,
			scrollsVertical = true, scrollEnabled = true;
	protected RectF _content_bounds, _v_scroll_handle_rect, _v_track_rect,
			_h_track_rect, _h_scroll_handle_rect;
	protected Paint _scroll_paint, _track_paint;

	private static final float MIN_SCROLLING_SWIPE_DIST = 900.0f;

	private float CORNER_RADIUS = 5, TRACK_THICKNESS = 8,
			SPACE_BETWEEN_TRACKS = 4, MIN_SCROLL_VELOCITY = 10,
			SCROLL_DECELERATION = -500f;

	public UIScrollView(Object tag) {
		super(tag);

		_content_bounds = new RectF();
		_v_scroll_handle_rect = new RectF();
		_v_track_rect = new RectF();
		_h_scroll_handle_rect = new RectF();
		_h_track_rect = new RectF();

		_scroll_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		_scroll_paint.setColor(Color.GRAY);
		_scroll_paint.setAlpha(255);

		_track_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		_track_paint.setColor(Color.DKGRAY);
		_track_paint.setAlpha(200);

	}

	@Override
	public void onUpdate(float seconds) {
		super.onUpdate(seconds);
		if (isScrolling) {
			_vy += (_vy > 0 ? SCROLL_DECELERATION : -SCROLL_DECELERATION)
					* seconds;
			if (_vy > MIN_SCROLL_VELOCITY || _vy < -MIN_SCROLL_VELOCITY)
				scrollTo(pos_x, pos_y + _vy * seconds);
			else if (!isDown)
				isScrolling = false;
			if ((pos_y == 0 || pos_y == max_y) && !isDown)
				isScrolling = false;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.save();

		if (!_hasAppeared)
			willAppear(true);

		canvas.clipRect(_rect, Region.Op.INTERSECT);
		canvas.translate(-pos_x, -pos_y);
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			if (e._rect.intersects(_rect.left + pos_x, _rect.top + pos_y,
					_rect.right + pos_x, _rect.bottom + pos_y))
				e.onDraw(canvas);
		}
		canvas.restore();

		if (isScrolling && scrollEnabled) {
			if (scrollsVertical && max_y > 0) {
				canvas.drawRoundRect(_v_track_rect, CORNER_RADIUS,
						CORNER_RADIUS, _track_paint);
				canvas.drawRoundRect(_v_scroll_handle_rect, CORNER_RADIUS,
						CORNER_RADIUS, _scroll_paint);
			}

			if (scrollsHorizontal && max_x > 0) {
				canvas.drawRoundRect(_h_track_rect, CORNER_RADIUS,
						CORNER_RADIUS, _track_paint);
				canvas.drawRoundRect(_h_scroll_handle_rect, CORNER_RADIUS,
						CORNER_RADIUS, _scroll_paint);
			}
		}
	}

	@Override
	public void onTouchDown(float x, float y) {
		_vy = 0;
		_vx = 0;

		_x = x;
		_y = y;

		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			if (e.containsPoint(x + pos_x, y + pos_y) && !e.isDraggable()) {
				e.onTouchDown(x + pos_x, y + pos_y);
			}
		}
	}

	@Override
	public void onTouchUp(float x, float y) {
		if (!(isDown && scrollEnabled)) {
			super.onTouchUp(x + pos_x, y + pos_y);
		}
		isDown = false;
		_vy = 10 * (_iy - y);

	}

	@Override
	public void onTouchMove(float x, float y) {
		dx = x - _x;
		dy = y - _y;

		if (dx * dx + dy * dy > MIN_SCROLLING_SWIPE_DIST) {
			isDown = true;
			isScrolling = true;
			_iy = y;

			int idx = subviewCount;
			while (idx-- > 0) {
				UIView e = subviews.get(idx);
				// XXX: HACK TO TELL OBJECT IT'S NO LONGER TOUCHED. SHOULD BE
				// IMPROVED.
				e.onTouchMove(e._position.x + e._width + 1, e._position.y
						+ e._height + 1);
			}
		}

		if (isDown && scrollEnabled) {
			if (scrollsVertical && max_y > 0) {
				dy = (_y - y);
				pos_y = MathUtils.clamp(0, max_y, pos_y + dy);
				_v_scroll_handle_rect.offsetTo(_v_scroll_handle_rect.left,
						_v_track_rect.top + (pos_y / max_y) * max_offset_y);
			}
			if (scrollsHorizontal && max_x > 0) {
				dx = (_x - x);
				pos_x = MathUtils.clamp(0, max_x, pos_x + dx);
				_h_scroll_handle_rect.offsetTo(_h_track_rect.left
						+ (pos_x / max_x) * max_offset_x,
						_h_scroll_handle_rect.top);
			}
			_x = x;
			_iy = _y;
			_y = y;
		} else
			super.onTouchMove(x + pos_x, y + pos_y);
	}

	@Override
	public void addSubview(UIView view) {
		int i = 0;
		_content_bounds.setEmpty();
		for (i = 0; i < subviews.size(); i++) {
			_content_bounds.union(subviews.get(i)._rect);
		}
		_content_bounds.union(view._rect);

		_v_scroll_handle_rect.bottom = _v_scroll_handle_rect.top
				+ getVerticalHandleHeight();
		_h_scroll_handle_rect.right = _h_scroll_handle_rect.left
				+ getHorizontalHandleWidth();

		max_offset_y = _v_track_rect.height() - _v_scroll_handle_rect.height();
		max_offset_x = _h_track_rect.width() - _h_scroll_handle_rect.width();

		super.addSubview(view);
	}

	@Override
	public void removeSubview(UIView view) {
		super.removeSubview(view);

		int i = 0;
		_content_bounds.setEmpty();
		for (i = 0; i < subviews.size(); i++) {
			_content_bounds.union(subviews.get(i)._rect);
		}

		_v_scroll_handle_rect.bottom = _v_scroll_handle_rect.top
				+ getVerticalHandleHeight();
		_h_scroll_handle_rect.right = _h_scroll_handle_rect.left
				+ getHorizontalHandleWidth();

		max_offset_y = _v_track_rect.height() - _v_scroll_handle_rect.height();
		max_offset_x = _h_track_rect.width() - _h_scroll_handle_rect.width();
	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {
		super.setBounds(left, top, right, bottom);
		dx = 0;
		dy = 0;
		pos_y = 0;
		pos_x = 0;

		max_y = Math.abs(_content_bounds.height() - _rect.height());
		if (max_y > 0)
			max_y += TRACK_THICKNESS;

		max_x = Math.abs(_content_bounds.width() - _rect.width());
		if (max_x > 0)
			max_x += TRACK_THICKNESS;

		_v_track_rect.left = right - TRACK_THICKNESS;
		_v_track_rect.right = right;
		_v_track_rect.top = top + TRACK_THICKNESS;
		_v_track_rect.bottom = bottom - TRACK_THICKNESS
				- (scrollsHorizontal ? SPACE_BETWEEN_TRACKS : 0);

		_v_scroll_handle_rect.left = right - TRACK_THICKNESS;
		_v_scroll_handle_rect.right = right;
		_v_scroll_handle_rect.top = top;
		_v_scroll_handle_rect.bottom = top + getVerticalHandleHeight();

		_h_track_rect.left = left;
		_h_track_rect.right = right;
		_h_track_rect.top = bottom - TRACK_THICKNESS;
		_h_track_rect.bottom = bottom;

		_h_scroll_handle_rect.left = left;
		_h_scroll_handle_rect.right = right - getHorizontalHandleWidth();
		_h_scroll_handle_rect.top = bottom - TRACK_THICKNESS;
		_h_scroll_handle_rect.bottom = bottom;

		max_offset_y = _v_track_rect.height() - _v_scroll_handle_rect.height();
		max_offset_x = _h_track_rect.width() - _h_scroll_handle_rect.width();

		if (_v_scroll_handle_rect.top < _v_track_rect.top)
			_v_scroll_handle_rect.top = _v_track_rect.top;
	}

	private float getVerticalHandleHeight() {
		return MathUtils.clamp(0, _v_track_rect.height(), _rect.height()
				/ _content_bounds.height() * _v_track_rect.height());
	}

	private float getHorizontalHandleWidth() {
		return MathUtils.clamp(0, _h_track_rect.width(), _rect.width()
				/ _content_bounds.width() * _h_track_rect.width());
	}

	public void setScrollsVertical(boolean scrolls) {
		scrollsVertical = scrolls;
	}

	public void setScrollsHorizontal(boolean scrolls) {
		scrollsHorizontal = scrolls;
		_v_track_rect.bottom = _rect.bottom - TRACK_THICKNESS
				- (scrollsHorizontal ? SPACE_BETWEEN_TRACKS : 0);
		_v_scroll_handle_rect.bottom = _v_scroll_handle_rect.top
				+ getVerticalHandleHeight();
		max_offset_y = _v_track_rect.height() - _v_scroll_handle_rect.height();

	}

	public void setScrolls(boolean scrolls) {
		scrollEnabled = scrolls;
	}

	@Override
	protected void layoutSubviews(boolean force) {
		_content_bounds.set(_rect);
		int idx = subviewCount;
		while (idx-- > 0) {
			UIView e = subviews.get(idx);
			if (force || !e._layedOut) {
				e._rect.offset(_rect.left, _rect.top);
				e.center.x = e._rect.centerX();
				e.center.y = e._rect.centerY();
				e.layoutSubviews(force);
			}
			_content_bounds.union(e._rect);
		}

		_v_scroll_handle_rect.bottom = _v_scroll_handle_rect.top
				+ getVerticalHandleHeight();
		_h_scroll_handle_rect.right = _h_scroll_handle_rect.left
				+ getHorizontalHandleWidth();

		max_y = Math.abs(_content_bounds.height() - _rect.height());
		if (max_y > 0)
			max_y += TRACK_THICKNESS;
		max_offset_y = _v_track_rect.height() - _v_scroll_handle_rect.height();

		max_x = Math.abs(_content_bounds.width() - _rect.width());
		if (max_x > 0)
			max_x += TRACK_THICKNESS;
		max_offset_x = _h_track_rect.width() - _h_scroll_handle_rect.width();

		if (_v_scroll_handle_rect.top < _v_track_rect.top)
			_v_scroll_handle_rect.top = _v_track_rect.top;

	}

	public void scrollTo(float x, float y) {
		pos_x = MathUtils.clamp(0, max_x, x);
		pos_y = MathUtils.clamp(0, max_y, y);

		if (max_y > 0)
			_v_scroll_handle_rect.offsetTo(_v_scroll_handle_rect.left,
					_v_track_rect.top + (pos_y / max_y) * max_offset_y);

		if (max_x > 0)
			_h_scroll_handle_rect.offsetTo(_h_track_rect.left + (pos_x / max_x)
					* max_offset_x, _h_scroll_handle_rect.top);
	}

	public void setContentRect(float left, float top, float right, float bottom) {
		_hasAppeared = true;

		_content_bounds.left = left;
		_content_bounds.top = top;
		_content_bounds.right = right;
		_content_bounds.bottom = bottom;

		_v_scroll_handle_rect.bottom = _v_scroll_handle_rect.top
				+ getVerticalHandleHeight();
		_h_scroll_handle_rect.right = _h_scroll_handle_rect.left
				+ getHorizontalHandleWidth();

		max_y = Math.abs(_content_bounds.height() - _rect.height());
		if (max_y > 0)
			max_y += TRACK_THICKNESS;
		max_offset_y = _v_track_rect.height() - _v_scroll_handle_rect.height();

		max_x = Math.abs(_content_bounds.width() - _rect.width());
		if (max_x > 0)
			max_x += TRACK_THICKNESS;
		max_offset_x = _h_track_rect.width() - _h_scroll_handle_rect.width();
	}

}
