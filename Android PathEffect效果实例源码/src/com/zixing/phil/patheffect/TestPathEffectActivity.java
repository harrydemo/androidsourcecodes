package com.zixing.phil.patheffect;

import android.app.Activity;
import android.os.Bundle;

public class TestPathEffectActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PathEffectView(this));
    }
}