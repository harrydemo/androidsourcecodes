package bbth.engine.particles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.FloatMath;
import bbth.engine.util.MathUtils;

/**
 * Represents a single particle. Many methods & the constructor are
 * package-private since they are controlled entirely by the particle system in
 * the same package.
 * 
 * @author jardini
 */
public class Particle {
	// Enum types had allocation overhead, so I use ints for the type instead
	private static final int CIRCLE = 0;
	private static final int LINE = 1;
	private static final int TRIANGLE = 2;

	// Particle properties
	int _type;
	float _radius;
	float _shrink;
	float _gravity;
	float _angle;
	float _angularVelocity;
	float _xPos, _yPos;
	float _xVel, _yVel;
	int _color;
	float _threshold;
	float _width;

	// Package private constructor
	Particle(float threshold) {
		_shrink = 1;
		_color = Color.WHITE;
		_threshold = threshold;
		_type = CIRCLE;
		// Use defaults for everything else
	}

	// Returns whether alive next tick or not
	boolean tick(float seconds) {
		float shrinkFactor = (float) Math.pow(_shrink, seconds);
		_radius *= shrinkFactor;
		if (_width > 0f)
			_width *= shrinkFactor;
		_yVel -= _gravity * seconds;
		_xPos += _xVel * seconds;
		_yPos += _yVel * seconds;
		_angle += _angularVelocity * seconds;
		return (_radius > _threshold);
	}

	void draw(Canvas canvas, Paint paint) {
		paint.setColor(_color);
		float oldWidth = paint.getStrokeWidth();
		if (_width > 0f)
			paint.setStrokeWidth(_width);
		switch (_type) {
		case CIRCLE:
			canvas.drawCircle(_xPos, _yPos, _radius, paint);
			break;

		case LINE:
			float dx = FloatMath.cos(_angle) * _radius;
			float dy = FloatMath.sin(_angle) * _radius;
			canvas.drawLine(_xPos - dx, _yPos - dy, _xPos + dx, _yPos + dy,
					paint);
			break;

		case TRIANGLE:
			float x1 = _xPos + _xVel * 0.08f;
			float y1 = _yPos + _yVel * 0.08f;
			float x2 = _xPos - _yVel * 0.02f;
			float y2 = _yPos + _xVel * 0.02f;
			float x3 = _xPos + _yVel * 0.02f;
			float y3 = _yPos - _xVel * 0.02f;
			// THIS IS A NEW, OH NOES
			float lines[] = { x1, y1, x2, y2, x2, y2, x3, y3, x3, y3, x1, y1 };
			canvas.drawLines(lines, paint);
			break;
		}
		if (_width > 0f)
			paint.setStrokeWidth(oldWidth);
	}

	// All operations support chaining for easy modification
	public final Particle circle() {
		_type = CIRCLE;
		_width = 0f;
		return this;
	}

	public final Particle triangle() {
		_type = TRIANGLE;
		_width = 0f;
		return this;
	}

	public final Particle line() {
		_type = LINE;
		_width = 0f;
		return this;
	}

	public final Particle color(int color) {
		_color = color;
		return this;
	}

	public final Particle radius(float radius) {
		_radius = radius;
		return this;
	};

	public final Particle radius(float min, float max) {
		_radius = MathUtils.randInRange(min, max);
		return this;
	}

	public final Particle shrink(float shrink) {
		_shrink = shrink;
		return this;
	}

	public final Particle shrink(float min, float max) {
		_shrink = MathUtils.randInRange(min, max);
		return this;
	}

	public final Particle angle(float angle) {
		_angle = angle;
		return this;
	}

	public final Particle angle(float min, float max) {
		_angle = MathUtils.randInRange(min, max);
		return this;
	}

	public final Particle angularVelocity(float angularVelocity) {
		_angularVelocity = angularVelocity;
		return this;
	}

	public final Particle angularVelocity(float min, float max) {
		_angularVelocity = MathUtils.randInRange(min, max);
		return this;
	}

	public final Particle gravity(float gravity) {
		_gravity = gravity;
		return this;
	}

	public final Particle gravity(float min, float max) {
		_gravity = MathUtils.randInRange(min, max);
		return this;
	}

	public final Particle position(float x, float y) {
		_xPos = x;
		_yPos = y;
		return this;
	};

	public final Particle velocity(float x, float y) {
		_xVel = x;
		_yVel = y;
		return this;
	};

	public final Particle width(float width) {
		_width = width;
		return this;
	}
}
