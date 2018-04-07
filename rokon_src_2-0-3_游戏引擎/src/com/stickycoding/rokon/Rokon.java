package com.stickycoding.rokon;

import com.stickycoding.rokon.vbo.ArrayVBO;
import com.stickycoding.rokon.vbo.ElementVBO;

/**
 * Rokon.java
 * Various static methods and fields, used by the engine
 * 
 * @author Richard
 */

public class Rokon {

	protected static MotionEventWrapper5 motionEvent5;
	protected static MotionEventWrapper8 motionEvent8;
	
	protected static BlendFunction blendFunction;
	protected static BufferObject triangleStripBoxBuffer;
	protected static BufferObject lineLoopBoxBuffer;
	protected static BufferObject lineVertexBuffer;
	
	protected static RokonActivity currentActivity;
	protected static ElementVBO elementVBO;
	protected static ArrayVBO arrayVBO;
	protected static ArrayVBO boxArrayVBO;
	protected static boolean verbose = false;
	
	public static Polygon rectangle;
	public static Polygon circle;
	
	/**
	 * Fetches the current Activity associated with Rokon
	 * 
	 * @return RokonActivity object, NULL if unset
	 */
	public static RokonActivity getActivity() {
		return currentActivity;
	}
	
	/**
	 * Fetches a default straight line BufferObject
	 * 
	 * @return valid BufferObject
	 */
	public static BufferObject lineVertexBuffer() {
		return lineVertexBuffer;
	}
	
	/**
	 * Returns the default element (index array) VBO
	 * 
	 * @return ElementVBO
	 */
	public static ElementVBO defaultElementVBO() {
		return elementVBO;
	}
	
	/**
	 * Returns the default (1x1 square) VBO object
	 * 
	 * @return ArrayVBO
	 */
	public static ArrayVBO defaultVBO() {
		return arrayVBO;
	}
	
	/**
	 * Returns the default (1x1 square) Buffer object for normal/VBO drawing
	 * 
	 * @return Buffer
	 */
	public static BufferObject defaultVertexBuffer() {
		return triangleStripBoxBuffer;
	}
	
	/**
	 * Sets the default BlendFunction to be used when rendering
	 * 
	 * @param blendFunction valid BlendFunction object
	 */
	public static void setBlendFunction(BlendFunction blendFunction) {
		Rokon.blendFunction = blendFunction;
	}
	
	/**
	 * Returns the default BlendFunction to be used when rendering
	 * When no specific BlendFunction is attached to a DrawableObject, this will be used
	 * A default will be used here if setBlendFunction is not used
	 * 
	 * @return the current BlendFunction
	 */
	public static BlendFunction getBlendFunction() {
		return blendFunction;
	}

}
