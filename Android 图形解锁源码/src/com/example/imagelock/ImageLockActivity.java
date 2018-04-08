package com.example.imagelock;

import com.example.view.NinePointLineView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class ImageLockActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v=new NinePointLineView(this);
        setContentView(v);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_image_lock, menu);
        return true;
    }
}
