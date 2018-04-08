package com.stickycoding.rokon;

/**
 * Movement.java
 * Defines several different types of movement, which can be applied to varying situations
 * 
 * @author Richard
 */
public class Movement {
	
	public static final float PI = 3.14159265f;
	public static final float PI_OVER_TWO = PI / 2f;
	public static final float TWO_PI = 2f * PI;
	
	public static final int DEFAULT = 0, CLOCKWISE = 1, ANTICLOCKWISE = 2;
	
	/**
	 * A linear movement, constant velocity
	 */
	public static final int LINEAR = 0;
	
	/**
	 * A smooth cosinal movement
	 */
	public static final int SMOOTH = 1;
	
	/**
	 * Begins slowly, accelerates
	 */
	public static final int SQUARED = 2;
	
	/**
	 * Begins slowly, accelerates harder
	 */
	public static final int CUBIC = 3;
	
	/**
	 * Begins slowly, accelerates harder
	 */
	public static final int QUADRATIC = 4;
	
	/**
	 * Begins quickly, ends slowly
	 */
	public static final int SLOWING = 3;
	
	/**
	 * Calculates a relative position along a 'path'
	 * 
	 * @param position position in the path
	 * @param method type of path used, see Movement constants for explanations
	 * 
	 * @return position on the path, between 0f and 1f
	 */
	public static float getPosition(float position, int method) {
		switch(method) {
			case SMOOTH:
				return 1f - (1f + (float)Math.cos(position * PI)) / 2f;
			case SQUARED:
				return position * position;
			case CUBIC:
				return position * position * position;
			case QUADRATIC:
				return position * position * position * position;
			default:
				return position;
		}
	}

}
