package com.sordid.particly;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Oldskool73
 * 
 * Parallax background that scrolls in both X and/or Y directions.
 * 
 * Usage:
 * 
 * ...x & y free scrolling tiled background...
 * mParallaxBackground.addParallaxEntity(new ParallaxBackground2d.ParallaxBackground2dEntity(-0.2f,-0.2f, new Sprite(0, 0, this.mParallaxLayerStars)));
 * 
 * ...side scroller repeating strip...
 * mParallaxBackground.addParallaxEntity(new ParallaxBackground2d.ParallaxBackground2dEntity(-0.4f, 0.0f, new Sprite(0, 100, this.mParallaxLayerHills),true,false));
 *
 * ...vertical scroller repeating strip...
 * mParallaxBackground.addParallaxEntity(new ParallaxBackground2d.ParallaxBackground2dEntity(-0.0f,-0.4f, new Sprite(100, 0, this.mParallaxLayerHills),false,true));
 *
 * ...non repeating positioned item...
 * mParallaxBackground.addParallaxEntity(new ParallaxBackground2d.ParallaxBackground2dEntity(-0.4f,-0.4f, new Sprite(100, 100, this.mParallaxLayerSun),false,false,true));
 * 
 * 
 */
public class ParallaxBackground2d extends ColorBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<ParallaxBackground2dEntity> mParallaxEntities = new ArrayList<ParallaxBackground2dEntity>();
	private int mParallaxEntityCount;

	protected float mParallaxValueX;
	protected float mParallaxValueY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallaxBackground2d(float pRed, float pGreen, float pBlue) {
		super(pRed, pGreen, pBlue);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setParallaxValue(final float pParallaxValueX, final float pParallaxValueY) {
		this.mParallaxValueX = pParallaxValueX;
		this.mParallaxValueY = pParallaxValueY;
	}
	
	public void offsetParallaxValue(final float pParallaxValueX, final float pParallaxValueY) {
		this.mParallaxValueX += pParallaxValueX;
		this.mParallaxValueY += pParallaxValueY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDraw(final GL10 pGL, final Camera pCamera) {
		super.onDraw(pGL, pCamera);

		final float parallaxValueX = this.mParallaxValueX;
		final float parallaxValueY = this.mParallaxValueY;
		final ArrayList<ParallaxBackground2dEntity> parallaxEntities = this.mParallaxEntities;

		for(int i = 0; i < this.mParallaxEntityCount; i++) {
			parallaxEntities.get(i).onDraw(pGL, parallaxValueX, parallaxValueY, pCamera);
		}
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	public void addParallaxEntity(final ParallaxBackground2dEntity pParallaxEntity) {
		this.mParallaxEntities.add(pParallaxEntity);
		this.mParallaxEntityCount++;
	}

	public boolean removeParallaxEntity(final ParallaxBackground2dEntity pParallaxEntity) {
		final boolean success = this.mParallaxEntities.remove(pParallaxEntity);
		if (success == true) {
			this.mParallaxEntityCount--;
		}
		return success;
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class ParallaxBackground2dEntity {
		final float mParallaxFactorX;
		final float mParallaxFactorY;
		final Shape mShape;
		private final float mOY;

		public ParallaxBackground2dEntity(final float pParallaxFactorX, final float pParallaxFactorY, final Shape pShape, final float pOY) {
			this.mParallaxFactorX = pParallaxFactorX;
			this.mParallaxFactorY = pParallaxFactorY;
			this.mShape = pShape;
			this.mOY = pOY;
		}
		
		public void onDraw(final GL10 pGL, final float pParallaxValueX, final float pParallaxValueY, final Camera pCamera) {
			pGL.glPushMatrix();
			{
				final float cameraWidth = pCamera.getWidth();
				final float shapeWidthScaled = this.mShape.getWidthScaled();

				//reposition
				float baseOffsetX = (pParallaxValueX * this.mParallaxFactorX);

				baseOffsetX = baseOffsetX % shapeWidthScaled;
				while(baseOffsetX > 0) {
					baseOffsetX -= shapeWidthScaled;
				}

				float baseOffsetY = (pParallaxValueY * this.mParallaxFactorY)+mOY;
				
				//draw
				pGL.glTranslatef(baseOffsetX, baseOffsetY, 0);
				do {														//rows
					this.mShape.onDraw(pGL, pCamera);
					pGL.glTranslatef(shapeWidthScaled, 0, 0);
					baseOffsetX += shapeWidthScaled;
				} while (baseOffsetX < cameraWidth);		//end rows
			}
			pGL.glPopMatrix();
		}
	}
}

