package com.crackedcarrot;

import com.crackedcarrot.textures.TextureData;

public class Sprite {
	// Variable used to determine what kind of sprite this is.
	// And what subtype. This is needed to avoid loading lots of
	// VBOs needlessly. if two sprites are just the same
	// they can share the same VBO.

	public static final int BACKGROUND = 0;
	public static final int EFFECT = 1;
	public static final int CREATURE = 2;
	public static final int SHOT = 3;
	public static final int OVERLAY = 4;
	public static final int TOWER = 5;
	public static final int HEALTHBAR = 6;
	private int type;
	private int subType;
	// Position.
	public float x;
	public float y;
	public float z;

	// Is the sprite going to be draw'd or not?
	public boolean draw = true;
	// color and opacity and for this sprite.
	public float r = 1.0f;
	public float g = 1.0f;
	public float b = 1.0f;
	public float opacity = 1.0f;
	// This needs to be here for the sake of animated sprites.
	// It makes the implementation of animated sprites mutch simpler.
	protected int cFrame;
	// Size.
	private float width;
	private float height;
	// Scale.
	public float scale = 1.0f;
	// The OpenGL ES texture handle to draw.
	private int texIndex;
	public TextureData texData;
	// The id of the original resource that the firstCurrTexName is based on.
	private int mResourceId;

	/**
	 * Empy constructor for a sprite object
	 */
	public Sprite() {
	}

	/**
	 * Main constructor of a sprite object. Needs texture resource id ,sprite
	 * type and subtype.
	 * 
	 * @param resourceId
	 * @param type
	 * @param subType
	 */
	public Sprite(int resourceId, int type, int subType) {
		mResourceId = resourceId;
		this.type = type;
		this.subType = subType;
		this.cFrame = 0;
	}

	/**
	 * Change frames. Each time this method is called sprite will pick next
	 * frame of sprite. When end is reached the method will start over.
	 */
	public void animate() {
		cFrame = (cFrame + 1) % this.texData.nFrames;
	}

	/**
	 * Will compare to sprites to each other and return true if equal.
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean equals(Object sprite) {
		if (Sprite.class.isInstance(sprite)) {
			Sprite testSprite = (Sprite) sprite;
			if (testSprite.height == this.height
					&& testSprite.width == this.width // && testSprite.texData
														// == this.texData
			) {

				return true;
			}

			else
				return false;
		} else {
			return false;
		}
	}

	/**
	 * This is used to make find the center of a sprite. Only used by the
	 * scaleSprite so far.
	 * 
	 * @param width
	 *            ,height
	 */
	private void setToCenterOfSprite(float width, float height) {
		// Find center of the submitted sprite
		// float cen_x = x + (width/2);
		// float cen_y = y + (height*(this.scale/scale2))/2;
		x = x + width / 2;
		y = y + height / 2;
	}

	/**
	 * This method is used when a sprite want to scale itself according to
	 * another sprite
	 * 
	 * @param newSize
	 * @param width
	 * @param height
	 */
	public void scaleSprite(int x, int y, float newSize, int width, int height) {
		this.x = x;
		this.y = y;
		setToCenterOfSprite(width, height);
		scale(newSize);
	}

	/**
	 * This method is used when a sprite wants to scale itself
	 * 
	 * @param newSize
	 */
	public void scaleSprite(float newSize) {
		setToCenterOfSprite(this.width, this.height);
		scale(newSize);
	}

	/**
	 * Will try to scale a sprite to the new size. May only be called after
	 * scaleSprite has been called. Don't ever make this public. If you
	 * rearrange this i will rearrange your face. --Henrik
	 * 
	 * @param newSize
	 */
	private void scale(float newSize) {
		// Calculate how much we have to scale
		this.scale = (newSize * 2) / this.width;
		float scale2 = (newSize * 2) / this.height;
		// Calculate the real position without scaling
		float new_x = x - newSize;
		float new_y = y - newSize * (this.scale / scale2);
		// Apply scaling to sprite
		x = new_x / this.scale;
		y = new_y / this.scale;
	}

	// ///////////////////////////////////////////
	// Getters
	// ///////////////////////////////////////////

	/**
	 * Return the number of frame this Sprite contains
	 * 
	 * @return
	 */
	public int getNbrOfFrames() {
		return texData.nFrames;
	}

	/**
	 * Return subtype of Sprite
	 * 
	 * @return
	 */
	public int getSubType() {
		return this.subType;
	}

	/**
	 * Get height of sprite
	 * 
	 * @return
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * return width of sprite
	 * 
	 * @return
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Return current TextureData for this sprite
	 * 
	 * @return
	 */
	public TextureData getCurrentTexture() {
		return this.texData;
	}

	/**
	 * Return texture resource for this sprite.
	 * 
	 * @return resource id (int)
	 */
	public int getResourceId() {
		return mResourceId;
	}

	// ///////////////////////////////////////////
	// Setters
	// ///////////////////////////////////////////

	/**
	 * Set texture resource for this sprite.
	 * 
	 * @param id
	 */
	public void setResourceId(int id) {
		mResourceId = id;
	}

	/**
	 * Change current texture to a new one.
	 * 
	 * @param texture
	 */
	public void setCurrentTexture(TextureData texture) {
		this.cFrame = 0;
		this.texData = texture;
		this.texIndex = this.texData.texIndex;
	}

	/**
	 * Set width of sprite
	 * 
	 * @param width
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Set height of sprite
	 * 
	 * @param height
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * Set type and subtype
	 * 
	 * @param type
	 * @param subType
	 */
	public void setType(int type, int subType) {
		this.type = type;
		this.subType = subType;
	}
}
