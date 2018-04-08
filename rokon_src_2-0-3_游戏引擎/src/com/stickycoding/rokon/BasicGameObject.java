package com.stickycoding.rokon;

/**
 * StaticObject.java
 * StaticObjects have a position and 2D dimensions
 * Has id and state functions, to aid with game design somewhat
 * 
 * @author Richard
 *
 */
/**
 * @author Richard
 *
 */
public class BasicGameObject extends RotationalObject {
	
	protected int id = -1;
	protected String name = null;
	protected Scene parentScene;
	protected Layer parentLayer;

	/**
	 * Attempts to invoke a method in this objects parentScene
	 * Finds a method with no parameters
	 * 
	 * @param Callback a Callback reference
	 * @return TRUE if successful, FALSE otherwise
	 */
	public boolean attemptInvoke(Callback callback) {
		if(name != null && parentScene != null) {
			return parentScene.invoke(callback);
		}
		return false;
	}
	
	/**
	 * Attempts to invoke a method in this objects parentScene
	 * Finds a method with no parameters
	 * 
	 * @param methodSuffix method name to invoke, [name]_[methodSuffix]
	 * @return TRUE if successful, FALSE otherwise
	 */
	public boolean attemptInvoke(String methodSuffix) {
		if(name != null && parentScene != null) {
			return parentScene.invoke(name + "_" + methodSuffix);
		}
		return false;
	}
	
	/**
	 * USE AT YOUR OWN RISK
	 * 
	 * Attempts to invoke a method in this objects parentScene
	 * Finds the first method with matching name, very risky if you aren't careful
	 * 
	 * @param methodSuffix method name to invoke, [name]_[methodSuffix]
	 * @param parameters an array of Objects to pass as parameters
	 * @return TRUE if successful, FALSE otherwise
	 */
	public boolean attemptInvoke(String methodSuffix, Object[] parameters) {
		if(name != null && parentScene != null) {
			return parentScene.invoke(name + "_" + methodSuffix, parameters);
		}
		return false;
	}
	
	/**
	 * Attempts to invoke a method in this objects parentScene
	 * Matches parameter name and parameter class types
	 * 
	 * @param methodSuffix method name to invoke, [name]_[methodSuffix]
	 * @param parameterTypes an array of Class objects which represent the type of parameter needed
	 * @param parameters an array of Objects to pass as parameters
	 * @return TRUE if successful, FALSE otherwise
	 */
	public boolean attemptInvoke(String methodSuffix, Class<?>[] parameterTypes, Object[] parameters) {
		if(name != null && parentScene != null) {
			return parentScene.invoke(name + "_" + methodSuffix, parameterTypes, parameters);
		}
		return false;
	}
	
	/**
	 * Fetches the Scene to which this object has been added to
	 * 
	 * @return NULL if not added to a Scene
	 */
	public Scene getParentScene() {
		return parentScene;
	}
	
	/**
	 * Creates a BasicGameObject
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param width width
	 * @param height height
	 */
	public BasicGameObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		if(width < 0 || height < 0) {
			Debug.warning("StaticObject()", "Tried creating StaticObject with dimensions < 0");
			return;
		}
	}
	
	/**
	 * Gives this object a name, can be used to identify objects - or for invoked methods
	 * 
	 * @param name a name to give this object
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Fetches the name of this object
	 * 
	 * @return NULL if not set
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gives this object a specific ID number, useful for referencing objects
	 * 
	 * @param id any integer
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Fetches this objects ID number
	 * 
	 * @return -1 if not set
	 */
	public int getId() {
		return id;
	}

}
