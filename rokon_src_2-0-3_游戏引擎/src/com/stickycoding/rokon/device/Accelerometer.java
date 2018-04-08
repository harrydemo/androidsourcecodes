package com.stickycoding.rokon.device;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.stickycoding.rokon.Debug;
import com.stickycoding.rokon.Rokon;
import com.stickycoding.rokon.Time;

/**
 * Accelerometer.java
 * Helper class for the accelerometer on the device, if it has one
 * 
 * @author Richard
 */

public class Accelerometer {
	
	private static SensorManager sensorManager;
	private static Sensor sensor;
	private static SensorEventListener sensorEventListener;
	private static OnAccelerometerChange onAccelerometerChange;
	private static long lastUpdate = 0;
	private static long lastShake = 0;
	private static float x, y, z, lastX, lastY, lastZ;
	
	/**
	 * The minimum time (in milliseconds) between onShake
	 */
	public static long minShakeInterval = 1000;
	
	
	/**
	 * The minimum intensity of absolute accelerometer values to change by to trigger onShake 
	 */
	public static float shakeThreshold = 3000;
	
	/**
	 * Returns the current X value of the accelerometer
	 * 
	 * @return x value as float
	 */
	public static float getX() {
		return x;
	}

	/**
	 * Returns the current Y value of the accelerometer
	 * 
	 * @return y value as float
	 */
	public static float getY() {
		return y;
	}

	/**
	 * Returns the current Z value of the accelerometer
	 * 
	 * @return z value as float
	 */
	public static float getZ() {
		return z;
	}

	/**
	 * Returns the previous X value of the accelerometer
	 * 
	 * @return previous x value as float
	 */
	public static float getLastX() {
		return lastX;
	}


	/**
	 * Returns the previous Y value of the accelerometer
	 * 
	 * @return previous y value as float
	 */
	public static float getLastY() {
		return lastY;
	}


	/**
	 * Returns the previous Z value of the accelerometer
	 * 
	 * @return previous z value as float
	 */
	public static float getLastZ() {
		return lastZ;
	}
	
	/**
	 * Returns the last change time of the accelerometer
	 * 
	 * @return the Time.getTicks at the point of the last onChange
	 */
	public long getLastUpdateTime() {
		return lastUpdate;
	}
	
	/**
	 * Returns the last shake time of the accelerometer 
	 * 
	 * @return the Time.getTicks at the point of the last onShake
	 */
	public long getLastShakeTime() {
		return lastShake;
	}
	
	private static boolean prepareAccelerometer() {
		if(sensorEventListener != null) return true;
		sensorManager = (SensorManager)Rokon.getActivity().getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if(sensors.size() < 1) {
			Debug.warning("DEVICE HAS NO ACCELEROMETER");
			return false;
		}
		sensor = sensors.get(0);
		sensorEventListener = new SensorEventListener() {
			public void onAccuracyChanged(Sensor sensor, int accuracy) {

			}

			public void onSensorChanged(SensorEvent sensorEvent) {
				lastX = x;
				lastY = y;
				lastZ = z;
				x = sensorEvent.values[0];
				y = sensorEvent.values[1];
				z = sensorEvent.values[2];
				onAccelerometerChange.onAccelerometerChange(x, y, z);
				if(lastUpdate > 0) {
					float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / (Time.getTicks() - lastUpdate) * 10000;
					if(speed > shakeThreshold && Time.getTicks() - lastShake >= minShakeInterval) {
						onAccelerometerChange.onShake(speed);
						lastShake = Time.getTicks();
					}
				}
				lastUpdate = Time.getTicks();
			}			
		};
		sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
		return true;
	}
	
	/**
	 * Starts listening for accelerometer changes
	 * 
	 * @param onAccelerometerChange a valid OnAccelerometerChange object, usually your Scene
	 * 
	 * @return TRUE if the accelerometer is being listened to, FALSE otherwise
	 */
	public static boolean startListening(OnAccelerometerChange onAccelerometerChange) {
		Accelerometer.onAccelerometerChange = onAccelerometerChange;
		return prepareAccelerometer();
	}
	
	/**
	 * Stops listening for accelerometer changes
	 */
	public static void stopListening() {
		try {
			sensorManager.unregisterListener(sensorEventListener);
		} catch (Exception e) { /* Ignore */ }
	}

}
