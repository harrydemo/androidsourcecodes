package com.stickycoding.rokon.tileengine;

import com.stickycoding.rokon.Sprite;

/**
 * TileSprite.java
 * A Sprite specifically designed for use with TiledLayer
 * 
 * @author Richard
 */

public class TiledSprite extends Sprite {
	
	protected TiledLayer tiledLayer;
	protected int tileX, tileY;
	protected int targetTileX, targetTileY;
	protected float tileOffset;

	public TiledSprite(TiledLayer tiledLayer, int tileX, int tileY, float width, float height) {
		super(0, 0, width, height);
		this.tiledLayer = tiledLayer;
		this.tileX = tileX;
		this.tileY = tileY;
		updateDrawPositions();
	}
	
	protected void updateDrawPositions() {
		if(tileOffset < 0) tileOffset = 0;
		if(tileOffset > 1) tileOffset = 1;
		if(tileOffset == 0) {
			setX(tiledLayer.getDrawX(tileX, tileY));
			setY(tiledLayer.getDrawY(tileX, tileY));
		} else {
			setX(tiledLayer.getDrawX(tileX, tileY, targetTileX, targetTileY, tileOffset));
			setY(tiledLayer.getDrawY(tileY, tileY, targetTileX, targetTileY, tileOffset));
		}
	}

	/**
	 * Sets the X (or column) tile position 
	 * 
	 * @param tileX integer
	 */
	public void setTileX(int tileX) {
		this.tileX = tileX;
		updateDrawPositions();
	}
	
	/**
	 * Sets the Y (or row) tile position
	 * 
	 * @param tileY integer
	 */
	public void setTileY(int tileY) {
		this.tileY = tileY;
		updateDrawPositions();
	}
	
	
	/**
	 * Sets the X (column) and Y (row) tile positions
	 * 
	 * @param tileX integer
	 * @param tileY integer
	 */
	public void setTile(int tileX, int tileY) {
		this.tileX = tileX;
		this.tileY = tileY;
		updateDrawPositions();
	}
	
	/**
	 * Sets the target X (column) tile position
	 * 
	 * @param targetTileX integer
	 */
	public void setTargetTileX(int targetTileX) {
		this.targetTileX = targetTileX;
		updateDrawPositions();
	}
	
	/**
	 * Sets the target Y (column) tile position
	 * 
	 * @param targetTileY integer
	 */
	public void setTargetTileY(int targetTileY) {
		this.targetTileY = targetTileY;
		updateDrawPositions();
	}
	
	/**
	 * Sets the target X (column) and Y (row) tile position
	 * 
	 * @param targetTileX integer
	 * @param targetTileY integer
	 */
	public void setTargetTile(int targetTileX, int targetTileY) {
		this.targetTileX = targetTileX;
		this.targetTileY = targetTileY;
		updateDrawPositions();
	}
	
	/**
	 * Returns the current X tile
	 * 
	 * @return integer
	 */
	public int getTileX() {
		return tileX;
	}
	
	/**
	 * Returns the current Y tile
	 * 
	 * @return integer
	 */
	public int getTileY() {
		return tileY;
	}
	
	/**
	 * Returns the current X tile
	 * 
	 * @return integer
	 */
	public int getTargetTileX() {
		return targetTileX;
	}
	
	/**
	 * Returns the current Y tile
	 * 
	 * @return integer
	 */
	public int getTargetTileY() {
		return targetTileY;
	}
	
	/**
	 * Sets the offset (0f to 1f) between current and target tiles
	 * May be greater than 1f, and will scale the drawn coordinates with tile width 
	 * 
	 * @param offset float 0f to 1f (if invalid, scaled back)
	 */
	public void setTileOffset(float offset) {
		this.tileOffset = offset;
		updateDrawPositions();
	}
	
	/**
	 * Returns the offset between current and target tiles
	 *  
	 * @return float, 0f to 1f
	 */
	public float getTileOffset() {
		return tileOffset;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
	}

}
