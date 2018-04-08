package com.stickycoding.rokon.vbo;

import javax.microedition.khronos.opengles.GL10;

import com.stickycoding.rokon.BufferObject;

/**
 * VBO.java
 * A VBO object, handles basic functions
 * 
 * @author Richard
 */

public class VBO {
	
	public static final int STATIC = 0, DYNAMIC = 1, STREAM = 2;
	
	protected int drawType = STATIC;	
	protected int bufferIndex = -1;
	protected BufferObject bufferObject;
	
	public VBO(BufferObject bufferObject, int drawType) {
		this.bufferObject = bufferObject;
		this.drawType = drawType;
	}
	
	/**
	 * Updates the VBO as a rectangle
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param width width of rectangle
	 * @param height height of rectangle
	 */
	public void update(float x, float y, float width, float height) {
		bufferObject.update(x, y, width, height);
	}
	
	/**
	 * Updates the VBO through a given array of floats
	 * 
	 * @param floats array of floats containing x and y coordinates of each vertex
	 */
	public void updateRaw(float[] floats) {
		bufferObject.updateRaw(floats);
	}
	
	/**
	 * Returns the buffer index (on the hardware) of the VBO
	 * 
	 * @return -1 if not loaded, >= 0 otherwise
	 */
	public int getBufferIndex() {
		return bufferIndex;
	}
	
	/**
	 * @return TRUE if the VBO is on the hardware, FALSE otherwise
	 */
	public boolean isLoaded() {
		return bufferIndex != -1;
	}
	
	/**
	 * Sets the VBO to unloaded, and resets buffer index 
	 */
	public void setUnloaded() {
		bufferIndex = -1;
	}
	
	/**
	 * Sets the VBO as loaded, and stores the buffer index
	 * 
	 * @param bufferIndex integer
	 */
	protected void setLoaded(int bufferIndex) {
		this.bufferIndex = bufferIndex;
	}
	
	/**
	 * Gets the current Buffer for this object
	 * 
	 * @return Buffer, NULL if invalid
	 */
	public BufferObject getBufferObject() {
		return bufferObject;
	}
	
	public void load(GL10 gl) { }

}
