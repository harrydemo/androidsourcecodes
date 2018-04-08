package com.stickycoding.rokon;

import android.view.MotionEvent;

public class MotionEventWrapper8 {

	protected static final int ACTION_MASK = 0x000000ff;
	protected static final int ACTION_POINTER_DOWN = 0x00000005;
	protected static final int ACTION_POINTER_UP = 0x00000006;
	protected static final int ACTION_POINTER_INDEX_MASK = 0x0000ff00;
	protected static final int ACTION_POINTER_INDEX_SHIFT = 0x00000008;
	
	protected MotionEventWrapper8() {
		try {
			MotionEvent.class.getMethod("getPointerCount", new Class[] { });
		} catch (Exception e) {
			Debug.print("MOTIONEVENT 8 NOT FOUND");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	protected static void checkAvailable() { }
	
	protected int getPointerCount(MotionEvent motionEvent) {
		return motionEvent.getPointerCount();
	}
	
	protected int getPointerId(MotionEvent motionEvent, int index) {
		return motionEvent.getPointerId(index);
	}
	
	protected float getX(MotionEvent motionEvent, int index) {
		return motionEvent.getX(index);
	}
	
	protected float getY(MotionEvent motionEvent, int index) {
		return motionEvent.getY(index);
	}



}
