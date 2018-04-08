package com.carouseldemo.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CarouselImageView extends ImageView implements Comparable<CarouselImageView> {
	
	private int index;
	private float currentAngle;
	private float x;
	private float y;
	private float z;
	private boolean drawn;
	
	public CarouselImageView(Context context) {
		this(context, null, 0);
	}	

	public CarouselImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CarouselImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}


	public void setCurrentAngle(float currentAngle) {
		this.currentAngle = currentAngle;
	}

	public float getCurrentAngle() {
		return currentAngle;
	}

	public int compareTo(CarouselImageView another) {
		return (int)(another.z - this.z);
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getZ() {
		return z;
	}

	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}

	public boolean isDrawn() {
		return drawn;
	}

}
