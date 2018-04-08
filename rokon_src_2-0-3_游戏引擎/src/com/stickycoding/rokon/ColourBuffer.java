package com.stickycoding.rokon;

/**
 * ColourBuffer.java
 * Holds a set of colours, representative of each vertex
 * This is an extension of BufferObject, and behaves very similarly
 * This is used solely for glColorPointer.
 * 
 * Each vertex has 4 floats, RGBA.
 * The number of vertices here should match the number of vertices in your shape, 
 * otherwise you will see unexpected results, or trigger Exceptions.
 * 
 * @author Richard
 */

public class ColourBuffer extends BufferObject {

	public ColourBuffer(float[] floats) {
		super(floats);
	}

}
