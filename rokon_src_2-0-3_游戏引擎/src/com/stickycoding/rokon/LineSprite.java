package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

/**
 * LineSprite.java
 * A very simple drawable straight line. For lines with many vertices, use PolygonSprite
 * 
 * @author Richard
 */

public class LineSprite extends Sprite {

	protected BufferObject lineBuffer;
	protected float x1, y1, x2, y2;
	
	/**
	 * Creates a drawable Line, from two points
	 * 
	 * @param x1 start x-coordinate
	 * @param y1 start y-coordinate
	 * @param x2 end x-coordinate
	 * @param y2 end y-coordinate
	 */
	public LineSprite(float x1, float y1, float x2, float y2) {
		super(x1, y1, 1, 1);
		setLine(x1, y1, x2, y2);
	}
	
	/**
	 * Sets the start position of the line
	 * 
	 * @param x1 x-coordinate
	 * @param y1 y-coordinate
	 */
	public void setLineStart(float x1, float y1) {
		setLine(x1, y1, x2, y2);
	}
	
	/**
	 * Sets the end position of the line
	 * 
	 * @param x2 x-coordinate
	 * @param y2 y-coordinate
	 */
	public void setLineEnd(float x2, float y2) {
		setLine(x1, y1, x2, y2);
	}
	
	/**
	 * Changes the start and end points of the line
	 * 
	 * @param x1 start x-coordinate
	 * @param y1 start y-coordinate
	 * @param x2 end x-coordinate
	 * @param y2 end y-coordinate
	 */
	public void setLine(float x1, float y1, float x2, float y2) {
		setXY(x1, y1);
		setSize(x2 - x1, y2 - y1);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		if(lineBuffer == null) {
			lineBuffer = new BufferObject(4);
		}
		lineBuffer.updateRaw(new float[] { 0, 0, x2 - x1, y2 - y1 });
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.DrawableObject#setTexture(com.stickycoding.rokon.Texture)
	 */
	@Override
	public void setTexture(Texture texture) {
		//Empty, no textures allowed
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.DrawableObject#onDrawNormal(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	protected void onDrawNormal(GL10 gl) {
		GLHelper.drawNormal(false, 0, 0, 0, alpha, blendFunction, null, 0, getX(), getY(), 1, 1, 0, false, 0, 0, true, lineBuffer, red, green, blue, alpha, lineWidth, false, null, 0, null);
	}
	

}
