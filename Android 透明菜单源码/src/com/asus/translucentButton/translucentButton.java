package com.asus.translucentButton;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class translucentButton extends Activity {
	int m_nSreenHeight = 0;
	Button m_menu1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translucent_button);
        
        m_menu1 = (Button)findViewById(R.id.menu1);
        Button menu2 = (Button)findViewById(R.id.menu2);
        Button menu3 = (Button)findViewById(R.id.menu3);
        Button menu4 = (Button)findViewById(R.id.menu4);
        m_menu1.setOnClickListener(menu1ClickListener);
        menu2.setOnClickListener(menu2ClickListener);
        menu3.setOnClickListener(menu3ClickListener);
        menu4.setOnClickListener(menu4ClickListener);
        
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        m_nSreenHeight = dm.heightPixels;
        
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_MENU) {
    		this.finish();
    	}
    	return super.onKeyUp(keyCode, event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getX() < m_nSreenHeight - m_menu1.getHeight()) {
    		finish();
    	}
        return false;
    }
    private OnClickListener menu1ClickListener = new OnClickListener() {
    	public void onClick(View v) {
    		Toast.makeText(translucentButton.this, "menu1 clicked!", 
    				Toast.LENGTH_SHORT).show();
    	}
    };
    private OnClickListener menu2ClickListener = new OnClickListener() {
    	public void onClick(View v) {
    		Toast.makeText(translucentButton.this, "menu2 clicked!", 
    				Toast.LENGTH_SHORT).show();
    	}
    };
    private OnClickListener menu3ClickListener = new OnClickListener() {
    	public void onClick(View v) {
    		Toast.makeText(translucentButton.this, "menu3 clicked!", 
    				Toast.LENGTH_SHORT).show();
    	}
    };
    private OnClickListener menu4ClickListener = new OnClickListener() {
    	public void onClick(View v) {
    		Toast.makeText(translucentButton.this, "menu4 clicked!", 
    				Toast.LENGTH_SHORT).show();
    	}
    };
}