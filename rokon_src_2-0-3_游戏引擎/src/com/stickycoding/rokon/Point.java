package com.stickycoding.rokon;

import com.badlogic.gdx.math.Vector2;

/**
 * PositionalObject.java
 * An point which has basic 2D positional coordinates, and methods to move
 * 
 * @author Richard
 */

public class Point extends Vector2 {

	private Point parent;
	
	/**
	 * Creates a 2D point
	 * 
	 * @param x x-value
	 * @param y y-value
	 */
	public Point(float x, float y) {
		super(x, y);
	}
	
	/**
	 * Sets a parent for this Point to lie relative to
	 * 
	 * @param point a valid Point to follow
	 */
	public void setParent(Point point) {
		parent = point;
	}
	
	/**
	 * Removes the parent Point, lies back to origin
	 */
	public void removeParent() {
		parent = null;
	}
	
	/**
	 * Returns the parent Point
	 * 
	 * @return Point object of the parent, NULL if unset
	 */
	public Point getParent() {
		return parent;
	}
	
	/**
	 * Returns the X coordinate for this Point
	 * 
	 * @return x-coordinate
	 */
	public float getX() {
		return parent == null ? x : parent.getX() + x;
	}
	
	/**
	 * Returns the Y coordinate for this Point
	 * 
	 * @return y-coordinate
	 */
	public float getY() {
		return parent == null ? y : parent.getY() + y;
	}
	
	/**
	 * Sets the X coordinate for this Point
	 * 
	 * @param x x-coordinate
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * Sets the Y coordinate for this Point
	 * 
	 * @param y y-coordinate
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * Sets the position of this Point
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Moves the X coordinate of this Point by a given amount
	 * 
	 * @param x x-coordinate to shift by
	 */
	public void moveX(float x) {
		this.x += x;
	}
	
	/**
	 * Moves the Y coordinate of this Point by a given amount
	 * 
	 * @param y y-coordinate to shift by
	 */
	public void moveY(float y) {
		this.y += y;
	}
	
	/**
	 * Moves the position of this Point by a given amount
	 * 
	 * @param x x-coordinate to shift by
	 * @param y y-coordinate to shift by
	 */
	public void move(float x, float y) {
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Moves this Point along a vector, that is, direction and angle 
	 * 
	 * @param distance the modulus of the distance to move
	 * @param angle the angle, in radians, relative to north 
	 */
	public void moveVector(float distance, float angle) {
		this.x += distance * (float)Math.sin(angle);
		this.y += distance * (float)Math.cos(angle);
	}
	
	/**
	 * Moves this Point along a Vector2
	 * 
	 * @param vector valid Vector2 object
	 */
	public void moveVector(Vector2 vector) {
		this.x += vector.x;
		this.y += vector.y;
	}
	
	protected void onUpdate() {
		
	}

}
