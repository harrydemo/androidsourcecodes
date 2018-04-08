package com.cn.daming;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private LinearLayout mLinearLayout;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
    	mLinearLayout = (LinearLayout)findViewById(R.id.box);
    	new AlarmClockView(this,this,mLinearLayout);
    }
}