package bbth.engine.entity;

public interface Movable {

	public void setPosition(float x, float y);

	public void setX(float x);

	public void setY(float y);

	public float getX();

	public float getY();

	public void setVelocity(float vel, float dir);

	public void setVelocityComponents(float x_vel, float y_vel);

	public void setXVel(float x_vel);

	public void setYVel(float y_vel);

	public float getHeading();

	public float getSpeed();

	public float getXVel();

	public float getYVel();

}
