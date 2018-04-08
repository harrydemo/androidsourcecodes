package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

/**
 * Layer.java
 * A Layer is contained inside a Scene, and are drawn in ascending order
 * Each Layer has a many DrawableObjects and one DrawQueue
 * @author Richard
 *
 */
public class Layer {
	
	protected Scene parentScene;
	protected FixedSizeArray<Drawable> gameObjects;
	protected FixedSizeArray<Drawable> gameObjectsSorted;
	protected int maximumDrawableObjects;
	protected boolean ignoreWindow;
	protected int drawQueueType = DrawOrder.FASTEST;
	
	/**
	 * Creates a Layer inside a Scene
	 * 
	 * @param parentScene parent Scene for the Layer
	 * @param maximumDrawableObjects the size of array for Drawables in this Layer
	 */
	public Layer(Scene parentScene, int maximumDrawableObjects) {
		this.parentScene = parentScene;
		this.maximumDrawableObjects = maximumDrawableObjects;
		gameObjects = new FixedSizeArray<Drawable>(maximumDrawableObjects);
		gameObjectsSorted = new FixedSizeArray<Drawable>(maximumDrawableObjects);
	}
	
	/**
	 * Tells this layer to ignore the current Scenes Window (if any) and draw straight to the device
	 * Useful for interface layers
	 */
	public void ignoreWindow() {
		ignoreWindow = true;
	}
	
	/**
	 * Tells this layer to use the current Scenes Window (if any)
	 * By default, this is on
	 */
	public void useWindow() {
		ignoreWindow = false;
	}
	
	/**
	 * @return TRUE if this Layer is drawing with respect to the current Scenes Window (if any)
	 */
	public boolean isUsingWindow() {
		return !ignoreWindow;
	}
	
	/**
	 * Returns a DrawableObject from this layer
	 * 
	 * @param index position in the array
	 * @return NULL if not found
	 */
	public Drawable getGameObject(int index) {
		return gameObjects.get(index);
	}
	
	/**
	 * @return the current draw queue type for this Layer
	 */
	public int getDrawQueue() {
		return drawQueueType;
	}
	
	/**
	 * Sets the DrawOrder method
	 * Defaults to DrawOrder if unset
	 * @param type Taken from the constants in DrawQueue
	 */
	public void setDrawOrder(int type) {
		if(type < 0 || type > 5) {
			Debug.warning("Scene.setDrawOrder", "Tried setting DrawOrder type to " + type + ", defaulting to 0");
			drawQueueType = 0;
			return;
		}
		drawQueueType = type; 
	}
	
	/**
	 * Clears all the DrawableObjects off this Layer
	 */
	public void clear() {
		gameObjects.clear();
	}
	
	/**
	 * Adds a DrawableObject to this Layer
	 * 
	 * @param drawableObject a valid DrawableObject
	 */
	public void add(GameObject drawableObject) {
		if(drawableObject == null) {
			Debug.warning("Layer.add", "Tried adding an invalid DrawableObject");
			return;
		}
		if(gameObjects.getCount() == gameObjects.getCapacity()) {
			Debug.warning("Layer.add", "Tried adding to a Layer which is full, maximum=" + maximumDrawableObjects);
			return;
		}
		gameObjects.add(drawableObject);
		drawableObject.onAdd(this);
	}
	
	protected void removeDead() {
		for(int i = 0; i < gameObjects.getCount(); i++) {
			if(gameObjects.get(i) != null) {
				while(gameObjects.get(i) != null && !gameObjects.get(i).isAlive()) {
					gameObjects.remove(i);
				}
			}
		}
	}
	
	protected void onDraw(GL10 gl) {
		DrawOrder.sort(gameObjects, drawQueueType);
		
		removeDead();
		
		if(ignoreWindow && parentScene.window != null) {
			Window.setDefault(gl);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
	        gl.glLoadIdentity();
		}
		for(int i = 0; i < gameObjects.getCount(); i++) {
			if(gameObjects.get(i) != null) {
				try {
					gameObjects.get(i).onUpdate();
					if(gameObjects.get(i).isOnScreen()) {
						gameObjects.get(i).onDraw(gl);
					}
				} catch (Exception e) { 
					e.printStackTrace();
				}	
			}
		}
		if(ignoreWindow && parentScene.window != null) {
			parentScene.window.onUpdate(gl);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
	        gl.glLoadIdentity();
		}
	}
	
}
