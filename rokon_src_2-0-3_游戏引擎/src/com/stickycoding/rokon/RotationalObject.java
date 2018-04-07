package com.stickycoding.rokon;

/**
 * Rotational.java
 * An object with position, dimensions, and rotations
 * 
 * @author Richard
 */
public class RotationalObject extends DimensionalObject {

	//protected float angularVelocity, angularAcceleration, terminalAngularVelocity;
	////protected boolean useTerminalAngularVelocity;
	
	protected float rotation, rotationPivotX, rotationPivotY;
	protected boolean rotateAboutPoint;

	public RotationalObject(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
    public float getRotation() {
        return rotation;
    }
	
	public float angle() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public void setRotation(float rotation, float rotationPivotX, float rotationPivotY) {
		this.rotation = rotation;
		this.rotationPivotX = rotationPivotX;
		this.rotationPivotY = rotationPivotY;
		rotateAboutPoint = true;
	}
	
	public float getRotationPivotX() {
		return rotationPivotX;
	}
	
	public float getRotationPivotY() {
		return rotationPivotY;
	}
	
	public void rotateAboutCentre() {
		rotateAboutPoint = false;
	}
	
	public void rotateAboutPoint(float rotationPivotX, float rotationPivotY) {
		this.rotationPivotX = rotationPivotX;
		this.rotationPivotY = rotationPivotY;
	}
	
	public void rotate(float rotation) {
		this.rotation += rotation;
	}
	
	public boolean isRotateAboutPoint() {
		return rotateAboutPoint;
	}
	
	protected void onUpdate() {
		super.onUpdate();
	}

}
