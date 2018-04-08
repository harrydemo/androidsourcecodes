package com.example.metroline;

import android.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * ≥µ’æ–≈œ¢
 * @author wby
 *
 */
public class LineInfo extends Activity{
   private TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.metroline.R.layout.resoult);
		tv=(TextView)findViewById(com.example.metroline.R.id.resoutlBestLine);
	   
		
		tv.setText(getIntent().getStringExtra("result"));
	   
	}

}
