package com.stickycoding.rokon.tileengine;

import com.stickycoding.rokon.Scene;


/**
 * RectangularTiledLayer.java
 * A TiledLayer working with basic squares
 * 
 * @author Richard
 */

public class RectangularLayer extends TiledLayer {
	
	protected float width, height;

	public RectangularLayer(Scene parentScene, int maximumDrawableObjects, float width, float height) {
		super(parentScene, maximumDrawableObjects);
		this.width = width;
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.tileengine.TiledLayer#getDrawX(int, int)
	 */
	@Override
	public float getDrawX(int x, int y) { 
		return x * width;
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.tileengine.TiledLayer#getDrawY(int, int)
	 */
	@Override
	public float getDrawY(int x, int y) { 
		return y * height; 
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.tileengine.TiledLayer#getDrawX(int, int, int, int, float)
	 */
	@Override
	public float getDrawX(int x, int y, int targetX, int targetY, float offset) {
		return (x * width) + ((targetX - x) * offset * width);
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.tileengine.TiledLayer#getDrawY(int, int, int, int, float)
	 */
	@Override
	public float getDrawY(int x, int y, int targetX, int targetY, float offset) {
		return (y * height) + ((targetY - y) * offset * height);
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.tileengine.TiledLayer#getTileX(float, float)
	 */
	@Override
	public int getTileX(float x, float y) { 
		return (int)((x - (x % width)) / width);
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.tileengine.TiledLayer#getTileY(float, float)
	 */
	@Override
	public int getTileY(float x, float y) { 
		return (int)((y - (y % height)) / height);
	}

}
