package com.stickycoding.rokon.vbo;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.stickycoding.rokon.BufferObject;
import com.stickycoding.rokon.Debug;
import com.stickycoding.rokon.GLHelper;
import com.stickycoding.rokon.VBOManager;

/**
 * ArrayVBO.java
 * An array VBO, defined by a BufferObject
 * 
 * @author Richard
 */
public class ArrayVBO extends VBO {
	
	public ArrayVBO(BufferObject bufferObject, int drawType) {
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
		GLHelper.bindBuffer(bufferIndex, true);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, bufferObject.get().capacity(), bufferObject.get(), drawType);
		setLoaded(bufferIndex);
		VBOManager.add(this);
		Debug.print("VBO Load (" + bufferIndex + ")");
	}

}
