package com.china.gltry;


import android.app.Activity;
import android.os.Bundle;

public class GLTry extends Activity {
	
	EGLSurfaceView mGLView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new EGLSurfaceView(this);
        mGLView.setRender(new LocalRender(this));
        setContentView(mGLView);
    }
}