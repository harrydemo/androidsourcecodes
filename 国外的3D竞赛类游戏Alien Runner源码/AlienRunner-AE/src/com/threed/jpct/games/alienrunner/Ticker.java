package com.threed.jpct.games.alienrunner;

public class Ticker {

	private int rate;
	private long s2;
	private static boolean paused = false;
	private static long sub = 0;
	private static long pauseTime = 0;

	public static void setPaused(boolean paused) {
		if (paused && !Ticker.paused) {
			pauseTime = getRawTime();
		} else {
			if (!paused && Ticker.paused) {
				sub += getRawTime() - pauseTime;
			}
		}
		Ticker.paused = paused;
	}

	public static long getRawTime() {
		return System.nanoTime() / 1000000L;
	}

	public static long getTime() {
		if (paused) {
			return pauseTime - sub;
		}
		return getRawTime() - sub;
	}

	public static boolean isPaused() {
		return Ticker.paused;
	}

	public Ticker(int tickrateMS) {
		rate = tickrateMS;
		s2 = Ticker.getTime();
	}

	public int getTicks() {
		long i = Ticker.getTime();
		if (i - s2 > rate) {
			int ticks = (int) ((i - s2) / (long) rate);
			s2 += (long) rate * ticks;
			return ticks;
		}
		return 0;
	}

	public void reset() {
		s2 = Ticker.getTime();
	}

	public static boolean hasPassed(long startTime, long time) {
		long dif = Ticker.getTime() - startTime;
		if (dif < 0) {
			return true;
		}
		return dif > time;
	}
}