package com.gw.andorid.anim;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SlidingDrawer;

public class AnimActivity extends Activity {
    /** Called when the activity is first created. */
	private SlidingDrawer mSlidingDrawer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSlidingDrawer=(SlidingDrawer)findViewById(R.id.drawer);
    }
}