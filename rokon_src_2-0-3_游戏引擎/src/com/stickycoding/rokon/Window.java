package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

/**
 * Window.java
 * A way of controlling what part of a Scene is visible to the player
 * 
 * @author Richard
 */

public class Window extends DimensionalObject {
	
	/**
	 * Creates a Window, with given coordinates and dimensions
	 * 
	 * @param x x-coordinate (top left)
	 * @param y y-coordinate (top left)
	 * @param width width of the Window
	 * @param height height of the Window
	 */
	public Window(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	protected static void setDefault(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, 0, RokonActivity.gameWidth, RokonActivity.gameHeight, 0);
	}
	
	protected void onUpdate(GL10 gl) {
		super.onUpdate();
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, getX(), getX() + width, getY() + height, getY());
	}
	

}
