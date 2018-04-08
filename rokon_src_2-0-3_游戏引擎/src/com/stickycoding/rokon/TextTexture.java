package com.stickycoding.rokon;

import android.graphics.Bitmap;

/**
 * TextTexture.java
 * A Texture specifically designed for TTF text
 * These are created through Font.createTexture
 * 
 * @author Richard
 */
public class TextTexture extends Texture {
	
	protected Bitmap bmp;
	
	protected TextTexture(Bitmap bmp) {
		this.bmp = bmp;
		width = bmp.getWidth();
		height = bmp.getHeight();
		columns = 1;
		rows = 1;
		tileCount = 1;
		textureWidth = nextPowerOfTwo(width);
		textureHeight = nextPowerOfTwo(height);
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Texture#getBitmap()
	 */
	protected Bitmap getBitmap() {
		return bmp;
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Texture#clearBitmap()
	 */
	protected void clearBitmap() {
		//Empty
	}

}
