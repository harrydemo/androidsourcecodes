package com.stickycoding.rokon.device;


/**
 * AccelerometerHandler.java
 * An interface for the Accelerometer class, usual function is to be used by your Scene
 * 
 * @author Richard
 */

public interface OnAccelerometerChange {

	void onAccelerometerChange(float x, float y, float z);
	void onShake(float intensity);

}
