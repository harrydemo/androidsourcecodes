package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

import com.stickycoding.rokon.device.Graphics;

/**
 * @author Richard
 *
 */
public class DrawableObject extends BasicGameObject implements Drawable, Updateable {

	protected boolean killNextUpdate = false;
	
	protected int z = 0;
	protected BlendFunction blendFunction;
	protected int forceDrawType = DrawPriority.DEFAULT;
	protected float red = 1, green = 1, blue = 1, alpha = 1;
	protected BufferObject buffer;
	protected Texture texture;
	protected int textureTile = 0;
	
	protected boolean invisible;
	
	protected boolean isFading;
	protected int fadeTime, fadeType;
	protected long fadeStartTime;
	protected float fadeTo, fadeStart;
	protected boolean fadeUp;
	
	protected boolean animated, hasCustomAnimation, animationReturnToStart;
	protected int animationStartTile, animationEndTile, animationLoops, animationCustomPosition;
	protected int[] customAnimationSequence;
	private long animationFrameTicks, animationLastTicks;
	
	protected float lineWidth = -1;
	protected boolean fill = true;
	protected float borderRed = 0, borderGreen = 0, borderBlue = 0, borderAlpha = 1;
	
	protected boolean border;
	
	
	protected ColourBuffer colourBuffer;
	
	/**
	 * Removes the border
	 */
	public void noBorder() {
		fill = false;
	}
	
	/**
	 * Determines whether this DrawableObject is using VBOs
	 * 
	 * @return TRUE if using VBO, FALSE otherwise
	 */
	public boolean isVBO() {
		return forceDrawType == DrawPriority.VBO || DrawPriority.drawPriority == DrawPriority.PRIORITY_VBO;
	}
	
	/**
	 * Draws a border around the object
	 * This only applied to untextured objects
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	public void setBorder(float red, float green, float blue, float alpha) {
		border = true;
		borderRed = red;
		borderGreen = green;
		borderBlue = blue;
		borderAlpha = alpha;
	}
	
	/**
	 * Sets the line width for this object, if not set the default line width set in Scene will be used
	 *  
	 * @param lineWidth float
	 */
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}
	
	/**
	 * Fetches the line width for this object, if a custom line width is being used
	 * 
	 * @return custom line width, -1 if not set
	 */
	public float getLineWidth() {
		return lineWidth;
	}
	
	/**
	 * Removes the custom line width for this object, and goes back to using the default
	 */
	public void useDefaultLineWidth() {
		lineWidth = -1;
	}
	
	/**
	 * Sets whether or not to show a border
	 * 
	 * @param border TRUE if border in use, FALSE otherwise
	 */
	public void setBorder(boolean border) {
		this.border = border;
	}
	
	/**
	 * Prevents this DrawableObject from being rendered 
	 */
	public void hide() {
		invisible = true;
	}
	
	/**
	 * Shows this DrawableObject, only needs to be called after hide()
	 */
	public void show() {
		invisible = false;
	}
	
	/**
	 * Determines whether ot not the DrawableObject has been told to hide
	 * 
	 * @return TRUE if visible, FALSE if hide() has been called 
	 */
	public boolean isVisible() {
		return !invisible;
	}
	
	/**
	 * Fades this DrawableObject to a given alpha value over time
	 * 
	 * @param alpha target alpha value, between 0f and 1f
	 * @param time the time (in milliseconds) to take while fading
	 * @param movementType valid movement type, see constants in Movement
	 */
	public void fade(float alpha, int time, int movementType) {
		fade(this.alpha, alpha, time, movementType);
	}
	
	/**
	 * Fades this DrawableObject to a given alpha value over time, using a linear movement type
	 * 
	 * @param alpha target alpha value, between 0f and 1f
	 * @param time the time (in milliseconds) to take while fading
	 */
	public void fade(float alpha, int time) {
		fade(this.alpha, alpha, time, Movement.LINEAR);
	}
	
	/**
	 * Fades this DrawableObject to a given alpha value over time, starting at a specific alpha
	 * 
	 * @param startAlpha start alpha value, between 0f and 1f
	 * @param alpha target alpha value, between 0f and 1f
	 * @param time the time (in milliseconds) to take while fading
	 * @param movementType valid movement type, see constants in Movement
	 */
	public void fade(float startAlpha, float alpha, int time, int movementType) {
		if(alpha == startAlpha) return;
		this.alpha = startAlpha;
		fadeType = movementType; 
		isFading = true;
		fadeTime = time;
		fadeStartTime = Time.ticks;
		fadeTo = alpha;
		fadeStart = this.alpha;
		fadeUp = alpha > startAlpha;
		
	}
	
	private void updateFadeTo() {
		if(!isFading) return;
		float position = (float)(Time.ticks - fadeStartTime) / (float)fadeTime;
		float factor = Movement.getPosition(position, fadeType);
		if(position >= 1) {
			this.alpha = fadeTo;
			isFading = false;
			return;
		}
		if(fadeUp) {
			setAlpha(fadeStart + ((fadeTo - fadeStart) * factor));
		} else {
			setAlpha(fadeStart - ((fadeStart - fadeTo) * factor));
		}
	}
	
	public DrawableObject(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public DrawableObject(float x, float y, float width, float height, Texture texture) {
		super(x, y, width, height);
		setTexture(texture);
	}
	
	/**
	 * Uses a Texture rather than a blank square
	 * 
	 * @param texture valid Texture object
	 */
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	/**
	 * Uses a Texture rather than a blank square
	 * 
	 * @param texture valid Texture object
	 * @param tile the tile index to use
	 */
	public void setTextureTile(Texture texture, int tileIndex) {
		setTexture(texture);
		textureTile = tileIndex;
	}
	
	/**
	 * Sets a specific BlendFunction to be applied to this DrawableObject
	 * If no BlendFunction is passed, the default will be used
	 * 
	 * @param blendFunction a valid BlendFunction
	 */
	public void setBlendFunction(BlendFunction blendFunction) {
		this.blendFunction = blendFunction;
	}
	
	/**
	 * Returns the specific BlendFunction associated with this DrawableObject
	 * 
	 * @return NULL if no specific BlendFunction has been given
	 */
	public BlendFunction getBlendFunction() {
		return blendFunction;
	}

	/**
	 * Forces this DrawableObject to use a specific DrawType
	 * 
	 * @param drawType valid draw type, see constants in DrawPriority
	 */
	public void forceDrawType(int drawType) {
		if(drawType == DrawPriority.VBO) {
			if(!Graphics.isSupportsVBO()) {
				Debug.warning("Tried forcing DrawableObject to VBO, device does not support it");
				drawType = DrawPriority.NORMAL;
			}
		}
		forceDrawType = drawType;
	}
	
	/**
	 * Sets the red value
	 * 
	 * @param red fixed point integer, 0 to ONE
	 */
	public void setRed(int red) {
		this.red = red;
	}
	
	/**
	 * @return current red value
	 */
	public float getRed() {
		return red;
	}
	
	/**
	 * Sets the green value
	 * 
	 * @param green float, 0 to 1
	 */
	public void setGreen(float green) {
		this.green = green;
	}
	
	/**
	 * @return current green value
	 */
	public float getGreen() {
		return green;
	}
	
	/**
	 * Sets the blue value
	 * 
	 * @param blue float, 0 to 1
	 */
	public void setBlue(float blue) {
		this.blue = blue;
	}
	
	/**
	 * @return blue value
	 */
	public float getBlue() {
		return blue;
	}
	
	/**
	 * Sets the alpha value
	 * 
	 * @param alpha float, 0 to 1
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	/**
	 * @return alpha value
	 */
	public float getAlpha() {
		return alpha;
	}
	
	/**
	 * Sets the red, green and blue values
	 * 
	 * @param red float, 0 to 1f
	 * @param green float, 0 to 1f
	 * @param blue float, 0 to 1f
	 */
	public void setRGB(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	/**
	 * Sets the red, green, blue and alpha values
	 * 
	 * @param red float, 0 to 1f
	 * @param green float, 0 to 1f
	 * @param blue float, 0 to 1f
	 * @param alpha float, 0 to 1f
	 */
	public void setRGBA(float red, float green, float blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public void onDraw(GL10 gl) {
		if(invisible) return;
		switch(forceDrawType) {
			case DrawPriority.DEFAULT:
				switch(DrawPriority.drawPriority) {
					case DrawPriority.PRIORITY_VBO:
						if(Graphics.isSupportsVBO()) {
							onDrawVBO(gl);
							return;
						}
						onDrawNormal(gl);
						return;
					case DrawPriority.PRIORITY_NORMAL:
						onDrawNormal(gl);
						return;
					default:
						Debug.warning("DrawableObject.onDraw", "Invalid draw priority on DrawableObject");
						return;
				}
			case DrawPriority.NORMAL:
				onDrawNormal(gl);
				return;
			case DrawPriority.VBO:
				onDrawVBO(gl);
				return;
			default:
				Debug.warning("DrawableObject.onDraw", "Invalid forced draw priority");
				return;
		}
	}
	
	protected void onDrawNormal(GL10 gl) {
		GLHelper.drawNormal(fill, red, green, blue, alpha, blendFunction, Rokon.triangleStripBoxBuffer, GL10.GL_TRIANGLE_STRIP, getX(), getY(), width, height, rotation, rotateAboutPoint, rotationPivotX, rotationPivotY, border, Rokon.lineLoopBoxBuffer, borderRed, borderGreen, borderBlue, borderAlpha, lineWidth, texture != null, texture, textureTile, colourBuffer);
	}
	
	protected void onDrawVBO(GL10 gl) {
		GLHelper.drawVBO(fill, red, green, blue, alpha, blendFunction, Rokon.arrayVBO, GL10.GL_TRIANGLE_STRIP, getX(), getY(), width, height, rotation, rotateAboutPoint, rotationPivotX, rotationPivotY, border, Rokon.boxArrayVBO, borderRed, borderGreen, borderBlue, borderAlpha, lineWidth, texture != null, texture, textureTile, colourBuffer);
	}
	
	/**
	 * Determines whether this object is actually visible on screen
	 * 
	 * @return TRUE if on screen, FALSE otherwise
	 */
	public boolean isOnScreen() {
		if(parentLayer == null) {
			return false;
		}
		float maxSize = width;
		if(height > width) maxSize = height;
		if(parentLayer.ignoreWindow || parentScene.window == null) {
			if (getX() - (maxSize / 2) < RokonActivity.gameWidth && getX() + maxSize + (maxSize / 2) > 0 && getY() - (maxSize / 2) < RokonActivity.gameHeight && getY() + maxSize + (maxSize / 2) > 0) {
				return true;
			}
		} else {
			if (getX() - (maxSize / 2) < parentScene.window.getX() + parentScene.window.width && getX() + maxSize + (maxSize / 2) > parentScene.window.getX() && getY() - (maxSize / 2) < parentScene.window.height + parentScene.window.getY() && getY() + maxSize + (maxSize / 2) > parentScene.window.getY()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sets the tile inside the Texture that this DrawableObject should use
	 * 
	 * @param tileIndex integer, starting at 0
	 */
	public void setTextureTile(int tileIndex) {
		textureTile = tileIndex;
	}
	
	/**
	 * Sets the tile inside the Texture that this DrawableObject should use
	 * based on columsn and rows, not the index
	 * 
	 * @param column integer, starting at 0
	 * @param row integer, starting at 0
	 */
	public void setTextureTile(int column, int row) {
		textureTile = (row * texture.columns) + column;
	}
	
	/**
	 * Returns the current tile which the Texture is using
	 * 
	 * @return 0 as default
	 */
	public int getTextureTile() {
		return textureTile;
	}
	
	/**
	 * Returns the current row of the Texture which this DrawableObject is using
	 * 
	 * @return positive integer
	 */
	public int getTextureTileRow() {
		float col = textureTile % texture.columns;
		return (int)((textureTile - col) / texture.columns);
	}
	
	/**
	 * Returns the current row of the Texture which this DrawableObject is using
	 * 
	 * @return positive integer
	 */
	public int getTextureTileColumn() {
		return textureTile % texture.columns;
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.RotationalObject#onUpdate()
	 */
	public void onUpdate() {
		super.onUpdate();
		updateFadeTo();
		updateAnimation();
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#onAdd(com.stickycoding.rokon.Layer)
	 */
	public void onAdd(Layer layer) {
		parentLayer = layer;
        killNextUpdate = false;
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#onRemove()
	 */
	public void onRemove() { }

	/**
	 * Removes this DrawableObject from the Scene
	 */
	public void remove() {
		killNextUpdate = true;
		parentLayer = null;
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#isAlive()
	 */
	public boolean isAlive() {
		if(killNextUpdate) {
			onRemove();
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Animates the Textures tile index, between minimum and maximum
	 * 
	 * @param startTile first tile index to animate from
	 * @param endTile the final tile index
	 * @param frameTime length of time to show one frame (in ms)
	 * @param loops number of animation loops before stopping
	 * @param returnToStart TRUE if the animation should return to the start after finished
	 */
	public void animate(int startTile, int endTile, long frameTime, int loops, boolean returnToStart) {
		animationStartTile = startTile;
		animationEndTile = endTile;
		animationFrameTicks = frameTime;
		animationLoops = loops;
		animationLastTicks = Time.ticks;
		textureTile = startTile;
		hasCustomAnimation = false;
		animationReturnToStart = returnToStart;
		animated = true;
	}
	
	/**
	 * Animates the Textures tile index, beween minimum and maximum
	 * 
	 * @param startTile first tile index to animate from
	 * @param endTile the final tile index
	 * @param frameTime length of time to show one frame (in ms)
	 */
	public void animate(int startTile, int endTile, long frameTime) {
		animate(startTile, endTile, frameTime, -1, false);
	}
	
	/**
	 * Animates the Texture tile index, through a custom array
	 * 
	 * @param animationTiles int array of texture tile indices
	 * @param frameTime length of time to show one frame (in ms)
	 * @param loops number of animation loops before stopping
	 * @param returnToStart TRUE if the animation should return to the start after finished
	 */
	public void animate(int[] animationTiles, long frameTime, int loops, boolean returnToStart) {
		hasCustomAnimation = true;
		textureTile = animationTiles[0];
		animationReturnToStart = returnToStart;
		animationLoops = loops;
		animationLastTicks = Time.ticks;
		animationFrameTicks = frameTime;
		customAnimationSequence = animationTiles;
		animated = true;
	}
	
	/**
	 * Animates the Texture tile index, through a custom array
	 * 
	 * @param animationTiles int array of texture tile indices
	 * @param frameTime length of time to show one frame (in ms)
	 */	
	public void animate(int[] animationTiles, long frameTime) {
		animate(animationTiles, frameTime, -1, false);
	}
	
	private void updateAnimation() {
		if(!animated) return;
		long tickDifference = Time.ticks - animationLastTicks - animationFrameTicks;
		if(tickDifference > 0) {
			int frameSkip = 0;
			while(tickDifference > 0) {
				tickDifference -= animationFrameTicks;
				frameSkip++;
			}
			if(hasCustomAnimation) {
				while(frameSkip > 0) {
					animationCustomPosition++;
					if(animationCustomPosition == customAnimationSequence.length) {
						animationCustomPosition = 0;
						if(animationLoops > 0) {
							animationLoops--;
							if(animationReturnToStart) {
								textureTile = customAnimationSequence[0];
							} else {
								animated = false;
								return;
							}
						}
					}
					textureTile = customAnimationSequence[animationCustomPosition];
					frameSkip--;
				}
			} else {
				while(frameSkip > 0) {
					textureTile++;
					if(textureTile > animationEndTile) {
						if(animationLoops > 0) {
							animationLoops--;
							if(animationLoops == 0) {
								if(animationReturnToStart) {
									textureTile = animationStartTile;
								} else {
									textureTile--;
									animated = false;
									return;
								}
							}
						}
						textureTile = animationStartTile;
					}
					frameSkip--;
				}
			}
			animationLastTicks -= tickDifference;
		}
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#isTouchable()
	 */
	public boolean isTouchable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#onTouchDown(float, float, android.view.MotionEvent, int)
	 */
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#onTouchDown(float, float, android.view.MotionEvent, int)
	 */
	public void onTouchDown(float x, float y, MotionEvent event, int pointerCount, int pointerId) { }

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#onTouchUp(float, float, android.view.MotionEvent, int)
	 */
	public void onTouchUp(float x, float y, MotionEvent event, int pointerCount, int pointerId) { }

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#onTouch(float, float, android.view.MotionEvent, int)
	 */
	public void onTouch(float x, float y, MotionEvent event, int pointerCount, int pointerId) { }

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#onTouchMove(float, float, android.view.MotionEvent, int)
	 */
	public void onTouchMove(float x, float y, MotionEvent event, int pointerCount, int pointerId) { }
	
	/**
	 * Returns the current Layer to which this DrawableObject is included
	 * 
	 * @return NULL if not set
	 */
	public Layer getParentLayer() {
		return parentLayer;
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Drawable#getZ()
	 */
	public int getZ() {
		return z;
	}
	
	/**
	 * Sets the (relative) Z position of this object. It will only take affect if setDrawOrder is DrawOrder.Z_ORDER
	 * 
	 * @param z integer
	 */
	public void setZ(int z) {
		this.z = z;
	}
	
	/**
	 * Stops any fading animation
	 */
	public void stopFade() {
		isFading = false;
	}
	
	/**
	 * Sets a ColourBuffer object to this DrawableObject
	 * 
	 * @param colourBuffer valid ColourBuffer
	 */
	public void setColourBuffer(ColourBuffer colourBuffer) {
		this.colourBuffer = colourBuffer;
	}
	
	/**
	 * Returns the current ColourBuffer object
	 * 
	 * @return NULL if not set, ColourBuffer otherwise
	 */
	public ColourBuffer getColourBuffer() {
		return colourBuffer;
	}
	
	/**
	 * Determines whether the ColourBuffer is being used
	 * 
	 * @return TRUE if ColourBuffer is being used, FALSE otherwise
	 */
	public boolean hasColourBuffer() {
		return colourBuffer != null;
	}
	
	/**
	 * Removes the ColourBuffer object from this DrawableObject
	 */
	public void removeColourBuffer() {
		colourBuffer = null;
	}
}
