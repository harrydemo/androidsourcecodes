package bbth.engine.entity;

import android.util.FloatMath;
import bbth.engine.util.MathUtils;

public class BasicMovable implements Movable {

	private float m_x;
	private float m_y;
	private float m_x_vel;
	private float m_y_vel;

	@Override
	public void setPosition(float x, float y) {
		m_x = x;
		m_y = y;
	}

	@Override
	public void setX(float x) {
		m_x = x;
	}

	@Override
	public void setY(float y) {
		m_y = y;
	}

	@Override
	public float getX() {
		return m_x;
	}

	@Override
	public float getY() {
		return m_y;
	}

	@Override
	public void setVelocity(float vel, float dir) {
		m_x_vel = vel * FloatMath.cos(dir);
		m_y_vel = vel * FloatMath.sin(dir);
	}

	@Override
	public void setVelocityComponents(float x_vel, float y_vel) {
		m_x_vel = x_vel;
		m_y_vel = y_vel;
	}

	@Override
	public void setXVel(float x_vel) {
		m_x_vel = x_vel;
	}

	@Override
	public void setYVel(float y_vel) {
		m_y_vel = y_vel;
	}

	@Override
	public float getHeading() {
		return MathUtils.getAngle(0, 0, m_x_vel, m_y_vel);
	}

	@Override
	public float getSpeed() {
		return FloatMath.sqrt((m_x_vel * m_x_vel) + (m_y_vel * m_y_vel));
	}

	@Override
	public float getXVel() {
		return m_x_vel;
	}

	@Override
	public float getYVel() {
		return m_y_vel;
	}

	public void update(float seconds) {
		m_x += m_x_vel * seconds;
		m_y += m_y_vel * seconds;
	}
}
