package com.renzh.earthtest;
import java.nio.Buffer;

import javax.microedition.khronos.opengles.GL11;

/**
 * Vertex buffer object presentation for gl
 * 
 */
public class GLVBO extends GLCache {

	public static final int VBO_VERTEX = 1;
	public static final int VBO_TEXTURE = 2;
	public static final int VBO_INDEX = 3;

	// name of VBO
	protected int VBO;
	// buffer for VBO
	protected Buffer buffer;
	// the target of VBO
	protected int type;

	public GLVBO(int type, Buffer buffer) {
		this.type = type;
		this.buffer = buffer;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	@Override
	protected boolean onUnBind() {
		if (mGL != null) {
			if (type == VBO_VERTEX) {
				mGL.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			}else if(type == VBO_TEXTURE){
				mGL.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			}else{
				mGL.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
			}
		}
		return true;
	}

	@Override
	protected boolean onBind() {
		if (type == VBO_VERTEX) {
			mGL.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBO);
		}else if(type == VBO_TEXTURE){
			mGL.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBO);
		}else{
			mGL.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, VBO);
		}
		return true;
	}

	@Override
	protected boolean onGenerate(GL11 gl11) {
		int bufId[] = new int[1];
		gl11.glGenBuffers(1, bufId, 0);

		if (type == VBO_VERTEX) {
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, bufId[0]);
			gl11.glBufferData(GL11.GL_ARRAY_BUFFER, buffer.capacity() * 4,
					buffer, GL11.GL_STATIC_DRAW);

		} else if (type == VBO_TEXTURE) {
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, bufId[0]);
			gl11.glBufferData(GL11.GL_ARRAY_BUFFER, buffer.capacity() * 4,
					buffer, GL11.GL_STATIC_DRAW);

		} else if (type == VBO_INDEX) {
			gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, bufId[0]);
			gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER,
					buffer.capacity() * 2, buffer, GL11.GL_STATIC_DRAW);

		} else {
			String str = new String("GBVBO----Unsupport type");
			throw new RuntimeException(str);
		}

		int ret = gl11.glGetError();
		if (ret != GL11.GL_NO_ERROR) {
			throw new RuntimeException("GLVBO--GL error");
		}

		VBO = bufId[0];
		return true;
	}

	@Override
	public boolean release() {
		if (mGL != null) {
			int[] bufs = new int[1];
			bufs[0] = this.VBO;
			mGL.glDeleteBuffers(1, bufs, 0);
			mGL = null;
		}
		return true;
	}
}
