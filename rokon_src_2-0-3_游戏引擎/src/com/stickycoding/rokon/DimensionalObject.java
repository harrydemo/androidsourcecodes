package com.stickycoding.rokon;


/**
 * Dimensional.java
 * An object with position and dimensions
 * 
 * @author Richard
 */

public class DimensionalObject extends Point {
	
	protected float width, height;

	protected boolean moving;
	protected float startX, startY, startWidth, startHeight;
	protected float finishX, finishY, finishWidth, finishHeight;
	protected long startTime;
	protected int moveTime, moveType;

	public DimensionalObject(float x, float y, float width, float height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}

	
	/**
	 * Gets the aspect ratio, width / height
	 * 
	 * @return aspect ratio
	 */
	public float getRatio() {
		return width / height;
	}
	
	/**
	 * Resizes this object, following the same aspect ratio at a new a width
	 * @param width
	 */
	public void resize(float width) {
		float ratio = getRatio();
		this.width = width;
		this.height = width / ratio;
	}
	
	/**
	 * Returns the width of this object
	 * 
	 * @return width of object
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of this object
	 * 
	 * @return height of object
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Sets the width of this object
	 * 
	 * @param width width of the object
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	
	/**
	 * Sets the height of this object
	 * 
	 * @param height height of the object
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	
	/**
	 * Sets the size of this object
	 * 
	 * @param width width of the object
	 * @param height height of the object
	 */
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Increases the width by a given amount
	 * 
	 * @param width amount to increase width by (will shrink if < 0)
	 */
	public void growWidth(float width) {
		this.width += width;
		if(this.width < 0 || this.height < 0) {
			Debug.warning("StaticObject.growWidth", "Dimensions < 0");
			if(this.width < 0) this.width = 0;
			if(this.height < 0) this.height = 0;
		}
	}
	
	/**
	 * Increases the height by a given amount
	 * 
	 * @param height amount to increase height by (will shrink if < 0)
	 */
	public void growHeight(float height) {
		this.height += height;
		if(this.width < 0 || this.height < 0) {
			Debug.warning("StaticObject.growHeight", "Dimensions < 0");
			if(this.width < 0) this.width = 0;
			if(this.height < 0) this.height = 0;
		}
	}
	
	/**
	 * Shrinks the width by a given amount
	 * 
	 * @param width amount to decrease width by (will grow if < 0)
	 */
	public void shrinkWidth(float width) {
		this.width -= width;
		if(this.width < 0 || this.height < 0) {
			Debug.warning("StaticObject.shrinkWidth", "Dimensions < 0");
			if(this.width < 0) this.width = 0;
			if(this.height < 0) this.height = 0;
		}
	}
	
	/**
	 * Shrinks the height by a given amount
	 * 
	 * @param height amount to decrease height by (will grow if < 0)
	 */
	public void shrinkHeight(float height) {
		this.height -= height;
		if(this.width < 0 || this.height < 0) {
			Debug.warning("StaticObject.shrinkHeight", "Dimensions < 0");
			if(this.width < 0) this.width = 0;
			if(this.height < 0) this.height = 0;
		}
	}
	
	/**
	 * Shrinks the object by a given amount
	 * 
	 * @param width amount to decrease width by (will grow if < 0)
	 * @param height amount to decrease height by (will grow if < 0)
	 */
	public void shrink(float width, float height) {
		this.width -= width;
		this.height -= height;
		if(this.width < 0 || this.height < 0) {
			Debug.warning("StaticObject.shrink", "Dimensions < 0");
			if(this.width < 0) this.width = 0;
			if(this.height < 0) this.height = 0;
		}
	}
	
	/**
	 * Expands the object by a given amount
	 * 
	 * @param width amount to increase width by (will shrink if < 0)
	 * @param height amount to increase height by (will shrink if < 0)
	 */
	public void grow(float width, float height) {
		this.width += width;
		this.height += height;
		if(this.width < 0 || this.height < 0) {
			Debug.warning("StaticObject.grow", "Dimensions < 0");
			if(this.width < 0) this.width = 0;
			if(this.height < 0) this.height = 0;
		}
	}
	
	/**
	 * Positions the object, centred at a given point]
	 * 
	 * @param x the x-coordinate for the centre of the object
	 * @param y the y-coordinate for the centre of the object 
	 */
	public void centre(float x, float y) {
		setX(x - width / 2);
		setY(y - height / 2);
	}
	
	protected void onUpdate() {
		super.onUpdate();
		if(moving) {
			float position = (float)(Time.ticks - startTime) / (float)moveTime;
			float factor = Movement.getPosition(position, moveType);
			if(position >= 1) {
				setXY(finishX, finishY);
				width = finishWidth;
				height = finishHeight;
				moving = false;
			} else {
				setX(startX + ((finishX - startX) * factor));
				setY(startY + ((finishY - startY) * factor));
				width = startWidth + ((finishWidth - startWidth) * factor);
				height = startHeight + ((finishHeight - startHeight) * factor);
			}
		}
	}

	/**
	 * Resizes this object over time, defaulting with Movement.SMOOTH
	 * 
	 * @param width target width
	 * @param height target height
	 * @param time target time
	 */
	public void resize(float width, float height, int time) {
		move(getX(), getY(), width, height, time, Movement.SMOOTH);
	}
	
	/**
	 * Resizes this object over time, using a given movement type
	 * 
	 * @param width target witdh
	 * @param height target height
	 * @param time time, in milliseconds
	 * @param movementType valid movement type, see Movement constants
	 */
	public void resize(float width, float height, int time, int movementType) {
		move(getX(), getY(), width, height, time, movementType);
	}
	
	/**
	 * Moves the object over time, using Movement.SMOOTH
	 * 
	 * @param x target x-coordinate
	 * @param y target y-coordinate
	 * @param time time, in milliseconds
	 */
	public void move(float x, float y, int time) {
		move(x, y, width, height, time, Movement.SMOOTH);
	}

	/**
	 * Moves the object over time
	 * 
	 * @param x target x-coordinate
	 * @param y target y-coordinate
	 * @param time time, in milliseconds
	 * @param movementType valid movement type, see Movement constants
	 */
	public void move(float x, float y, int time, int movementType) {
		move(x, y, width, height, time, movementType);
	}
	
	/**
	 * Moves and resizes this object over time
	 * 
	 * @param x target x-coordinate
	 * @param y target y-coordinate
	 * @param width target width
	 * @param height target height
	 * @param time time, in milliseconds
	 */
	public void move(float x, float y, float width, float height, int time) {
		move(x, y, width, height, time, Movement.SMOOTH);
	}
	
	/**
	 * Moves and resizes this object over time
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param width target width
	 * @param height target height
	 * @param time time, in milliseconds
	 * @param movementType valid movement type, see Movement constants
	 */
	public void move(float x, float y, float width, float height, int time, int movementType) {
		startX = getX();
		startY = getY();
		startWidth = this.width;
		startHeight = this.height;
		startTime = Time.ticks;
		moveTime = time;
		moveType = movementType;
		finishX = x;
		finishY = y;
		finishWidth = width;
		finishHeight = height;
		Debug.print("moving from " + startX + " to " + finishX );
		moving = true;
	}

}
