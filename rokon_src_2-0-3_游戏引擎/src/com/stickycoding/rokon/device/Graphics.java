package com.stickycoding.rokon.device;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Device.java
 * Discovers and retrieves information about the devices hardware
 * 
 * @author Richard 
 */
public class Graphics {
	
	/**
	 * The aspect ratio of the device screen
	 */
	public static int NORMAL = 0, WIDE = 1, FULL_WIDTH = 2;
	
	/**
	 * The aspect ratio as defined for wide screen (eg 800x480)
	 */
	public static final float WIDE_RATIO = 0.60037524f;
	
	private static DisplayMetrics displayMetrics;	
	private static int widthPixels, heightPixels, halfWidthPixels, halfHeightPixels;
	private static float aspectRatio;
	private static int screenType;
	private static boolean supportsVBO;
	
	/**
	 * Fetches the screen type, as per Graphics constants
	 * 
	 * @return NORMAL, WIDE, FULL_WIDTH
	 */
	public static int getScreenType() {
		return screenType;
	}
	
	/**
	 * Fetches the actual aspect ratio for the device screen
	 * 
	 * @return aspect ratio
	 */
	public static float getAspectRatio() {
		return aspectRatio;
	}
	
	/**
	 * @return TRUE if device is normal aspect ratio, FALSE otherwise
	 */
	public static boolean isNormalAspect() {
		return screenType == NORMAL;
	}
	
	/**
	 * @return TRUE if device is widescreen, FALSE otherwise
	 */
	public static boolean isWideAspect() {
		return screenType == WIDE;
	}
	
	/**
	 * @return TRUE if device is full width, FALSE otherwise
	 */
	public static boolean isFullWidthAspect() {
		return screenType == FULL_WIDTH;
	}
	
	/**
	 * Determines several characterstics of the physical device and stores for later usage
	 * This must be called before any other methods in this class are used
	 * 
	 * @param activity a currently active Activity
	 */
	public static void determine(Activity activity) {
		displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		widthPixels = displayMetrics.widthPixels;
		heightPixels = displayMetrics.heightPixels;
		halfWidthPixels = widthPixels / 2;
		halfHeightPixels = heightPixels / 2;
		aspectRatio = (float)widthPixels / (float)heightPixels;
		if(aspectRatio == WIDE_RATIO) {
			screenType = WIDE;
		}
		if(aspectRatio < WIDE_RATIO) {
			screenType = NORMAL;
		}
		if(aspectRatio > FULL_WIDTH) {
			screenType = FULL_WIDTH;
		}
	}
	
	/**
	 * Returns the DisplayMetrics object associated with the current screen
	 * 
	 * @return NULL if determine() has not been called previously
	 */
	public static DisplayMetrics getDisplayMetrics() {
		if(displayMetrics == null)
			return null;
		return displayMetrics;
	}
	
	/**
	 * Returns the width of the screen, in pixels
	 * 
	 * @return integer
	 */
	public static int getWidthPixels() {
		return widthPixels;
	}

	
	/**
	 * Returns the height of the screen, in pixels
	 * 
	 * @return integer
	 */
	public static int getHeightPixels() {
		return heightPixels;
	}

	
	/**
	 * Returns half the width of the screen, in pixels
	 * 
	 * @return integer
	 */
	public static int getHalfWidthPixels() {
		return halfWidthPixels;
	}

	
	/**
	 * Returns half the height of the screen, in pixels
	 * 
	 * @return integer
	 */
	public static int getHalfHeightPixel() {
		return halfHeightPixels;
	}

	/**
	 * @return TRUE if the device supports VBOs
	 */
	public static boolean supportsVBO() {
		return isSupportsVBO();
	}

	/**
	 * Sets whether the device can support VBOs or not, do not use this unless you are aware of new bugged devices
	 * 
	 * @param supportsVBO TRUE if supports VBOs, FALSE otherwise
	 */
	public static void setSupportsVBO(boolean supportsVBO) {
		Graphics.supportsVBO = supportsVBO;
	}

	/**
	 * @return TRUE if the device supports VBOs, FALSE otherwise
	 */
	public static boolean isSupportsVBO() {
		return supportsVBO;
	}
	
}
