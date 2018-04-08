package com.stickycoding.rokon.background;

import javax.microedition.khronos.opengles.GL10;

import com.stickycoding.rokon.Background;
import com.stickycoding.rokon.DrawableObject;
import com.stickycoding.rokon.RokonActivity;
import com.stickycoding.rokon.Texture;

/**
 * FixedBackground.java
 * The most basic textured background, a fixed image stretched the size of the screen
 * 
 * @author Richard
 */
public class FixedBackground extends Background {
	
	private DrawableObject bg;
	
	public FixedBackground(Texture texture) {
		bg = new DrawableObject(0, 0, RokonActivity.getGameWidth(), RokonActivity.getGameHeight(), texture);
	}
	
	public void onDraw(GL10 gl) {
		super.onDraw(gl);
		bg.onDraw(gl);
	}

}
