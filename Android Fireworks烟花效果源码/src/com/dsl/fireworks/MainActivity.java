package com.dsl.fireworks;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display=this.getWindowManager().getDefaultDisplay();
        setContentView(new FireworksSurface(this,display.getWidth(),display.getHeight()));
    }

}
