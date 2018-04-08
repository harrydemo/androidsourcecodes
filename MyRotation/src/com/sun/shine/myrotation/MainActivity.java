
package com.sun.shine.myrotation;

import com.sun.shine.myrotation.view.RotatView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RotatView rotatView=(RotatView)findViewById(R.id.myRotatView);
        rotatView.setRotatDrawableResource(R.drawable.cycle);
    }

}
