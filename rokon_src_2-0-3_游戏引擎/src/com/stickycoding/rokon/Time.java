package com.stickycoding.rokon;

/**
 * Time.java
 * Keeps track of the game time, frames, loops etc.
 * 
 * @author Richard
 */
public class Time {
	
	protected static long ticks, realTicks;
	protected static int ticksSinceLastFrame;
	protected static float ticksFraction;
	protected static long lastTicks;
	protected static long pauseStart, pauseTime;
	protected static boolean paused;
	
	protected static void update() {
		lastTicks = ticks;
		realTicks = System.currentTimeMillis();
		if(paused) return;
		ticks = realTicks - pauseTime;
		if(lastTicks == 0) return;
		ticksSinceLastFrame = (int)(ticks - lastTicks);
		ticksFraction = ticksSinceLastFrame / 1000f;
	}
	
	/**
	 * Returns the ticks (in milliseconds) for the current frame
	 * 
	 * @return current ticks
	 */
	public static long getTicks() {
		return ticks;
	}
	
	/**
	 * Returns the difference in ticks between this frame and the previous
	 * 
	 * @return the difference in ticks 
	 */
	public static int getTicksSinceLastFrame() {
		return ticksSinceLastFrame;
	}
	
	/**
	 * Returns the difference in ticks between this frame and the previous, as a fraction
	 * 
	 * @return a fraction, where 1f = 1000ms
	 */
	public static float getTicksFraction() {
		return ticksFraction;
	}
	
	/**
	 * Returns the tick count for the previous frame
	 * 
	 * @return previous ticks
	 */
	public static long getLastTicks() {
		return lastTicks;
	}
	
	/**
	 * Pauses the tick count
	 */
	public static void pause() {
		pauseStart = ticks;
		paused = true;
	}
	
	/**
	 * Resumes the tick count
	 */
	public static void resume() {
		pauseTime = System.currentTimeMillis() - pauseStart;
		paused = false;
	}
	
	

}
