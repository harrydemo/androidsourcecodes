package com.stickycoding.rokon;

import android.view.MotionEvent;

/**
 * RokonSurfaceView.java
 * The SurfaceView used for OpenGL rendering
 * 
 * @author Richard
 */
public class RokonSurfaceView extends GLSurfaceView {

	protected RokonActivity rokonActivity;
	protected RokonRenderer renderer;
	
	public RokonSurfaceView(RokonActivity rokonActivity, RokonRenderer renderer) {
		super(rokonActivity);
		this.rokonActivity = rokonActivity;
		this.renderer = renderer;
		setRenderer(renderer);
		setKeepScreenOn(true);
	}
	
	/**
	 * Creates RokonSurfaceView
	 * 
	 * @param rokonActivity valid RokonActivity
	 */
	public RokonSurfaceView(RokonActivity rokonActivity) {
	    super(rokonActivity);
	    this.rokonActivity = rokonActivity;
	    renderer = new RokonRenderer(rokonActivity);
	    setRenderer(renderer);
	    setKeepScreenOn(true);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	rokonActivity.onTouchEvent(event);
        try {
        	Thread.sleep(16);
        } catch (Exception e) { }
        return true;
    }

}
