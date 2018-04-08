package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TextViewActivity extends Activity {

	  private TextView textView;
	  
	  public void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.main);
	    
	    textView = (TextView) findViewById(R.id.textView1);
	    
	    String str2 = "»¶Ó­";
	    textView.setText(str2);
	  }
}