package com.stickycoding.rokon;

import android.view.MotionEvent;

public class MotionEventWrapper5 {

	protected static final int ACTION_POINTER_1_DOWN = 0x00000005;
	protected static final int ACTION_POINTER_1_UP = 0x00000006;
	protected static final int ACTION_POINTER_2_DOWN = 0x00000105;
	protected static final int ACTION_POINTER_2_UP = 0x00000106;
	protected static final int ACTION_POINTER_3_DOWN = 0x00000205;
	protected static final int ACTION_POINTER_3_UP = 0x00000206;
	
	protected MotionEventWrapper5() {
		try {
			MotionEvent.class.getMethod("getPointerCount", new Class[] { });
		} catch (Exception e) {
			Debug.print("MOTIONEVENT 5 NOT FOUND");
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
