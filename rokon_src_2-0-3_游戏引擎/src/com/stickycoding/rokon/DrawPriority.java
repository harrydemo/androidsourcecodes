package com.stickycoding.rokon;

/**
 * DrawPriority.java
 * Contains constants and details on the draw priority system
 * 
 * @author Richard
 */
public class DrawPriority {
	
	/**
	 * Used for the drawPriority variable
	 */
	public static final int PRIORITY_VBO = 0, PRIORITY_NORMAL = 1;
	
	/**
	 * Used for DrawableObject.forceDrawType 
	 */
	public static final int DEFAULT = 0, VBO = 1, NORMAL = 2;
	
	protected static int drawPriority = 0;

}
