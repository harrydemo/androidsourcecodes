package com.stickycoding.rokon;

import java.util.Comparator;

/**
 * DrawQueue.java
 * 
 * Stores the type and list of draw queue for each layer
 * @author Richard
 */
public class DrawOrder {
	
	/**
	 * The methods for choosing render order
	 */
	public static final int FASTEST = 0, X_ASCENDING = 1, X_DESCENDING = 2, Y_ASCENDING = 3, Y_DESCENDING = 4, Z_ORDER = 5;
	
	protected int method = FASTEST;
	
	/**
	 * Sorts a FixedSizeArray of Drawables
	 * 
	 * @param gameObjects FixedSizeArray of Drawable objects
	 * @param type FASETEST, X_ASCENDING, X_DESCENDING, Y_ASCENDING, Y_DESCENDING
	 */
	public static void sort(FixedSizeArray<Drawable> gameObjects, int type) {
		switch(type) {
			case X_ASCENDING:
				gameObjects.setComparator(XAscendingComparator);
				gameObjects.sort(true);
				break;
			case Y_ASCENDING:
				gameObjects.setComparator(YAscendingComparator);
				gameObjects.sort(true);
				break;
			case X_DESCENDING:
				gameObjects.setComparator(XDescendingComparator);
				gameObjects.sort(true);
				break;
			case Y_DESCENDING:
				gameObjects.setComparator(YDescendingComparator);
				gameObjects.sort(true);
				break;
			case Z_ORDER:
				gameObjects.setComparator(ZComparator);
				gameObjects.sort(true);
				break;
		}
	}
	
	protected static Comparator<Drawable> XAscendingComparator = new Comparator<Drawable>() {
		public int compare(Drawable object1, Drawable object2) {
			if(object1.getX() > object2.getX())
				return 1;
			if(object1.getX() == object2.getX())
				return 0;
			return -1;
		}
	};
	
	protected static Comparator<Drawable> XDescendingComparator = new Comparator<Drawable>() {
		public int compare(Drawable object1, Drawable object2) {
			if(object1.getX() < object2.getX())
				return 1;
			if(object1.getX() == object2.getX())
				return 0;
			return -1;
		}
	};
	
	protected static Comparator<Drawable> YAscendingComparator = new Comparator<Drawable>() {
		public int compare(Drawable object1, Drawable object2) {
			if(object1.getY() > object2.getY())
				return 1;
			if(object1.getY() == object2.getY())
				return 0;
			return -1;
		}
	};
	
	protected static Comparator<Drawable> YDescendingComparator = new Comparator<Drawable>() {
		public int compare(Drawable object1, Drawable object2) {
			if(object1.getY() < object2.getY())
				return 1;
			if(object1.getY() == object2.getY())
				return 0;
			return -1;
		}
	};
	
	protected static Comparator<Drawable> ZComparator = new Comparator<Drawable>() {
		public int compare(Drawable object1, Drawable object2) {
			if(object1.getZ() > object2.getZ())
				return 1;
			if(object1.getZ() == object2.getZ())
				return 0;
			return -1;
		}
	};

}
