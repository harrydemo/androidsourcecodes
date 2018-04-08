package org.void1898.www.agilebuddy.data;

/**
 * 
 * @author void1898@gmail.com
 * 
 * @version 1.2.3
 * 
 */
public class Food {

	public int mFoodReward;

	public int mTimeCounter;

	public int mMinX;

	public int mMinY;

	public int mMaxX;

	public int mMaxY;

	public Food(int foodReward, int timeCounter, int x, int y, int size) {
		mFoodReward = foodReward;
		mTimeCounter = timeCounter;
		mMinX = x;
		mMinY = y;
		mMaxX = x + size;
		mMaxY = y + size;
	}
}
