package com.eric;


import android.app.Activity;
import android.os.Bundle;
public class main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SmiliesEditText et=(SmiliesEditText)findViewById(R.id.EditText1);
        et.insertIcon(R.drawable.smile);
    }
}