package com.stickycoding.rokon.tileengine;

import com.stickycoding.rokon.Debug;
import com.stickycoding.rokon.DrawableObject;
import com.stickycoding.rokon.Layer;
import com.stickycoding.rokon.Scene;

/**
 * TiledLayer.java
 * An extension of Layer, this is the parent of other tiled layers
 * 
 * @author Richard
 *
 */
public class TiledLayer extends Layer {

	public TiledLayer(Scene parentScene, int maximumDrawableObjects) {
		super(parentScene, maximumDrawableObjects);
	}
	
	/**
	 * Adds a TiledSprite to the TiledLayer, normal Sprites and DrawableObjects cannot be added
	 * 
	 * @param tiledSprite valid TiledSprite object
	 */
	public void add(TiledSprite tiledSprite) {
		super.add(tiledSprite);
	}
	
	/**
	 * This is to prevent adding a non-TiledSprite DrawableObject to the TiledLayer
	 */
	public void add(DrawableObject drawableObject) {
		if(drawableObject instanceof TiledSprite) {
			add((TiledSprite)drawableObject);
			return;
		}
		Debug.warning("TiledLayer.add", "Tried adding oa non-TiledSprite to the TiledLayer");
	}

	/**
	 * Determines the X location of a TiledSprite on the layer, taking into account the tilemap
	 * 
	 * @param x X (or column) location of the tile
	 * @return float
	 */
	public float getDrawX(int x, int y) { return -1; }

	/**
	 * Determines the Y location of a TiledSprite on the layer, taking into account the tilemap
	 * 
	 * @param y Y (or row) location of the tile
	 * @return float
	 */
	public float getDrawY(int x, int y) { return -1; }

	/**
	 * Determines the X location of a TiledSprite on the layer, taking into account the tilemap
	 * 
	 * @param x X (or column) location of the tile
	 * @param targetX the destination of the TiledSprite
	 * @param offset the offset beween current and target sprite (0 to 1)
	 * @return float
	 */
	public float getDrawX(int x, int y, int targetX, int targetY, float offset) { return -1; }


	/**
	 * Determines the Y location of a TiledSprite on the layer, taking into account the tilemap
	 * 
	 * @param y Y (or row) location of the tile
	 * @param targetY the destination of the TiledSprite
	 * @param offset the offset beween current and target sprite (0 to 1)
	 * @return float
	 */
	public float getDrawY(int x, int y, int targetX, int targetY, float offset) { return -1; }
	
	/**
	 * Determines the X location of the nearest tile to a given position
	 * 
	 * @param x float
	 * @param y float
	 * @return the X (or column) location of the nearest tile
	 */
	public int getTileX(float x, float y) { return -1; }
	
	/**
	 * Determines the Y location of the nearest tile to a given position
	 * 
	 * @param x float
	 * @param y float
	 * @return the Y (or column) location of the nearest tile
	 */
	public int getTileY(float x, float y) { return -1; }

}
