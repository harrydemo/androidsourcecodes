package com.renzh.earthtest;

import javax.microedition.khronos.opengles.GL11;

/**
 * The object representation for cache memory of OpenGL.
 */
public abstract class GLCache implements IReleaseable {

	protected GL11 mGL;

	public GLCache() {
		mGL = null;
	}

	/**
	 * bind the cache
	 */
	public void bind(GL11 gl11) {
		if (gl11 != mGL) {
			release();
			if (onGenerate(gl11)) {
				mGL = gl11;
				onBind();
			}
		}else{
			onBind();
		}
	}
	
	protected abstract boolean onGenerate(GL11 gl11);

	protected abstract boolean onBind();

	protected abstract boolean onUnBind();

	/**
	 * unbind the cache
	 */
	public void unBind() {
		onUnBind();
	}

	/**
	 * release the resources of this object
	 */
	public boolean release() {
		return true;
	}
}
