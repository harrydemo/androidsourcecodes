package com.stickycoding.rokon.vbo;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.stickycoding.rokon.BufferObject;
import com.stickycoding.rokon.Debug;
import com.stickycoding.rokon.GLHelper;

/**
 * ElementVBO.java
 * An element VBO, defined by BufferObject.
 * Currently unused, but may be introduced for vertex-colouring
 * 
 * @author Richard
 */
public class ElementVBO extends VBO {
	
	public ElementVBO(BufferObject bufferObject, int drawType) {
		super(bufferObject, drawType);
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.vbo.VBO#load(javax.microedition.khronos.opengles.GL10)
	 */
	public void load(GL10 gl) {
		GL11 gl11 = (GL11)gl;
		int[] nameArray = new int[1];
		gl11.glGenBuffers(1, nameArray, 0);
		bufferIndex = nameArray[0];
		Debug.print("Adding new element VBO idx=" + bufferIndex);
		GLHelper.bindElementBuffer(bufferIndex);
		gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, bufferObject.get().capacity(), bufferObject.get(), drawType);
		Debug.print("Element VBO Added");
	}

}
