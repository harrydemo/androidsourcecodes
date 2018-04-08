package com.appspot.hexagon.util;

import android.os.SystemClock;

public class FrameRateCounter {

  static public int WINDOW_SIZE = 20;

  private long[] timestamps;
  private int nextTimestamp;
  private boolean primed;

  public FrameRateCounter() {
    timestamps = new long[WINDOW_SIZE];
  }

  public void update() {
    timestamps[nextTimestamp] = SystemClock.uptimeMillis();
    nextTimestamp = (nextTimestamp + 1) % timestamps.length;
    if (!primed && nextTimestamp == 0) {
      primed = true;
    }
  }

  public float getFps() {
    if (!primed) {
      return 0;
    }
    long oldest = timestamps[nextTimestamp];
    long newest = timestamps[(nextTimestamp + timestamps.length - 1) % timestamps.length];
    return 1000f * timestamps.length / (newest - oldest);
  }
}
