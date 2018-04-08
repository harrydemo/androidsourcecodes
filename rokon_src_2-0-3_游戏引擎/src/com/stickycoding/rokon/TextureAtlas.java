package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

/**
 * TextureAtlas.java
 * Holds many textures, loaded onto the atlas rather than single textures. Good for reducing actual texture count on hardware, reducing state changes, improving speed.
 * 
 * @author Richard
 */

public class TextureAtlas extends Texture {

	/**
	 * The maximum number of textures able to be loaded onto one TextureAtlas, by default
	 */
	public static final int DEFAULT_MAX_TEXTURE_COUNT = 64;
	
	/**
	 * The default width of a TextureAtlas
	 */
	public static final int DEFAULT_TEXTURE_ATLAS_WIDTH = 1024;
	
	/**
	 * The default height of a TextureAtlas 
	 */
	public static final int DEFAULT_TEXTURE_ATLAS_HEIGHT = 1024;
	
	protected int atlasWidth, atlasHeight, maxTextureCount;	
	protected Texture[] texture;
	protected boolean complete;

	/**
	 * Creates a TextureAtlas with default configuration
	 */
	public TextureAtlas() {
		super();
		maxTextureCount = DEFAULT_MAX_TEXTURE_COUNT;
		texture = new Texture[this.maxTextureCount];
		atlasWidth = DEFAULT_TEXTURE_ATLAS_WIDTH;
		atlasHeight = DEFAULT_TEXTURE_ATLAS_HEIGHT;
		
	}
	
	/**
	 * Marks this TextureAtlas as complete. No more Textures can be added, and it can be loaded onto the hardware.
	 */
	public void complete() {
		complete = true;
	}
	
	/**
	 * Creates a TextureAtlas width default dimensions, but a given maximum number of Textures
	 * 
	 * @param maxTextureCount maximum number of Textures on this TextureAtlas
	 */
	public TextureAtlas(int maxTextureCount) {
		super();
		this.maxTextureCount = maxTextureCount;
		texture = new Texture[this.maxTextureCount];
		atlasWidth = DEFAULT_TEXTURE_ATLAS_WIDTH;
		atlasHeight = DEFAULT_TEXTURE_ATLAS_WIDTH;
	}
	
	/**
	 * Creates a TextureAtlas from a given configuration
	 * 
	 * @param maxTextureCount maximum number of Textures on this TextureAtlas
	 * @param textureAtlasWidth width of this TextureAtlas, in pixels
	 * @param textureAtlasHeight height of this TextureAtlas, in pixels
	 */
	public TextureAtlas(int maxTextureCount, int textureAtlasWidth, int textureAtlasHeight) {
		this(maxTextureCount);
		atlasWidth = textureAtlasWidth;
		atlasHeight = textureAtlasHeight;
	}
	
	/**
	 * Inserts a Texture onto the TextureAtlas, finds the next best spot.
	 * To optimize, this should be done from largest to smallest.
	 * 
	 * @param texture valid Texture object
	 */
	public void insert(Texture texture) {
		if(texture instanceof DynamicTexture) {
			Debug.error("Tried inserting DynamicTexture into TextureAtlas, not possible");
			Debug.forceExit();
			return;
		}
		if(complete) {
			Debug.error("Tried inserting Texture into TextureAtlas after complete()");
			Debug.forceExit();
			return;
		}
		int slot = getNextEmptySlot();
		if(slot == -1) {
			Debug.error("TextureAtlas is full");
			return;
		} else {
			this.texture[slot] = texture;
			findRightSpot(texture);
			texture.parentAtlas = this;

		}
	}
	
	private void findRightSpot(Texture texture) {
		boolean foundSpot = false;
		int checkX = 0, checkY = 0;
		while(!foundSpot) {
			if(checkY + texture.height > atlasHeight) {
				Debug.error("Can't find a spot on TextureAtlas?!");
				return;
			}
			int textureAt = textureAt(checkX, checkY, texture);
			if(textureAt == -1) {
				texture.atlasX = checkX;
				texture.atlasY = checkY;
				foundSpot = true;
			} else {
				checkX = this.texture[textureAt].atlasX + this.texture[textureAt].width + 1;
				if(checkX + texture.width > atlasWidth) {
					checkX = 0;
					checkY += 16;
				}
			}
		}
	}
	
	private int textureAt(int x, int y, Texture skip) {
		for(int i = 0; i < maxTextureCount; i++) {
			if(this.texture[i] != null && this.texture[i] != skip) {
				if(MathHelper.rectOverlap(x, y, x + skip.width, y + skip.width, texture[i].atlasX, texture[i].atlasY, texture[i].atlasX + texture[i].width, texture[i].atlasY + texture[i].height)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	private int getNextEmptySlot() {
		for(int i = 0; i < maxTextureCount; i++) {
			if(texture[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.Texture#onLoadTexture(javax.microedition.khronos.opengles.GL10)
	 */
	protected void onLoadTexture(GL10 gl) {
		if(!complete) {
			Debug.error("Tried loading TextureAtlas without calling complete() first");
			Debug.forceExit();
			return;
		}
		
		Debug.error("Loading TextureAtlas");
		
		int[] nameArray = new int[1];
		GLHelper.enableTextures();
		gl.glGenTextures(1, nameArray, 0);
		textureIndex = nameArray[0];
		GLHelper.bindTexture(textureIndex);
		Bitmap bmp = Bitmap.createBitmap(atlasWidth, atlasHeight, Bitmap.Config.ARGB_8888);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);


		for(int i = 0; i < maxTextureCount; i++) {
			if(texture[i] != null) {
                texture[i].textureIndex = textureIndex;
				texture[i].prepareBuffers();
				bmp = texture[i].getBitmap();
                GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, texture[i].atlasX, texture[i].atlasY, bmp);
                texture[i].clearBitmap();
                bmp = null;
            }
		}
		
		TextureManager.addToActive(this);

	}
	
	@Override
	public void setUnloaded() {
		textureIndex = -1;
		for(int i = 0; i < maxTextureCount; i++) {
			if(texture[i] != null) {
				texture[i].setUnloaded();
			}
		}
	}

}
