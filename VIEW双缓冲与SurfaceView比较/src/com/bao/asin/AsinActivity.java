package com.bao.asin;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AsinActivity extends Activity {
    /** Called when the activity is first created. */
	
	AsinSurfaceView asinSurfaceView;
	AsinView assinView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        asinSurfaceView = new AsinSurfaceView(this);
        assinView = new AsinView(this);
        
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.addView(asinSurfaceView);
        l.addView(assinView);
        setContentView(l);
    }
}