package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * DynamicTexture.java
 * A Texture which can be changed at runtime. A separate class was created to cut down on unecessary memory allocations,
 * since it is much more likely for a Texture to remain unchanged.
 * 
 * This cannot be used inside a TextureAtlas
 * 
 * @author Richard
 */

public class DynamicTexture extends Texture {
	
	protected Bitmap bmpCopy;
	protected Canvas canvas;

	/**
	 * Creates a DynamicTexture, based from a file in assets
	 * 
	 * @param filename valid file in assets
	 */
	public DynamicTexture(String filename) {
		super(filename);
	}
	
	/**
	 * Creates a DynamicTexture, based from a file in assets, split into columns and rows
	 * 
	 * @param filename valid file in assets
	 * @param columns number of columns in the image
	 * @param rows number of rows in the image
	 */
	public DynamicTexture(String filename, int columns, int rows) {
		super(filename, columns, rows);
	}
	
	@Override
	protected Bitmap getBitmap() {
		if(bmp == null) {
		        try {
		        	Bitmap tBitmap = BitmapFactory.decodeStream(Rokon.currentActivity.getAssets().open(path));
		        	bmp = tBitmap.copy(tBitmap.getConfig(), true);
		        	return bmp;
		        } catch (Exception e) {
		        	Debug.error("Texture.getBitmap() error, bad asset?");
		        	return null;
		        }
		}
		return bmp;
	}
	
	@Override
	protected void clearBitmap() {
		// Intentionally empty, Bitmap needs to be kept in memory
	}
	
	/**
	 * Fetches the Canvas for this Bitmap, can be drawn on to update the texture
	 * When work is done, call reload()
	 * 
	 * @return valid Canvas object
	 */
	public Canvas getCanvas() {
		if(canvas == null) {
			canvas = new Canvas(bmp);
		}
		return canvas;
	}
	
	/**
	 * Informs the TextureManager to reload the texture from the Bitmap, any updates in Canvas will be drawn in here
	 */
	public void reload() {
		//TODO Remove old texture from hardware, this isn't right!
		//TextureManager.removeFromActiveTextures(this);
	}
	
	protected void onRefreshTexture(GL10 gl) {
		GLHelper.removeTextures(new Texture[] { this });
		onLoadTexture(gl);
	}

}
