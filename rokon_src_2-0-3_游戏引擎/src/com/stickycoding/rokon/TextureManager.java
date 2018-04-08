package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

/**
 * TextureManager.java
 * Handles queues for loading/unloading textures from the hardware, does not interfere with actual Texture objects
 * 
 * @author Richard
 */

public class TextureManager {
	
	/**
	 * Maximum number of Texture objects that TextureManager can handle
	 */
	public static final int MAX_TEXTURE_COUNT = 256;
	
	protected static Texture[] activeTexture = new Texture[MAX_TEXTURE_COUNT];
	protected static int activeTextureCount = 0;
	
	public static void reloadTextures(GL10 gl) {
		Debug.print("reloadTextures()");
		for(int i = 0; i < MAX_TEXTURE_COUNT; i++) {
			if(activeTexture[i] != null && activeTexture[i].textureIndex == -1) {
				activeTexture[i].onLoadTexture(gl);
				Debug.print(" - Loading [" + i + "] to " + activeTexture[i].textureIndex);
				//activeTexture[i] = null;
				//activeTextureCount--;
			}
		}
	}
	
	/** 
	 * Reloads all active Textures
	 */
	public static void removeTextures() {
		Debug.error("removeTextures()");
		for(int i = 0; i < MAX_TEXTURE_COUNT; i++) {
			if(activeTexture[i] != null) {
				Debug.print(" - Unloaded " + i);
				Texture texture = activeTexture[i];
				texture.setUnloaded();
				//activeTexture[i] = null;
				//activeTextureCount--;
			}
		}
	}
		
	protected static void addToActive(Texture textureId) {
		if(isActive(textureId)) {
			return;
		}
		Debug.error("  addToActive(" + textureId.textureIndex + ")");
		for(int i = 0 ; i < MAX_TEXTURE_COUNT; i++) {
			if(activeTexture[i] == null) {
				activeTexture[i] = textureId;
				activeTextureCount++;
				return;
			}
		}
	}
	
	protected static boolean isActive(Texture texture) {
		for(int i = 0; i < MAX_TEXTURE_COUNT; i++) {
			if(activeTexture[i] == texture) {
				return true;
			}
		}
		return false;
	}

}
