package com.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Activity2 extends Activity {

	private String TAG = "activity2";

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity2);

		Button button2 = (Button) findViewById(R.id.button2);

		button2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Activity2.this, Activity1.class);
				startActivity(intent);
				Activity2.this.finish();
			}
		});
		Log.v(TAG, "onCreate");
	}

	  public void onStart(){
	    	super.onStart();
	    	Log.v(TAG, "onStart");
	    }
	    
	    public void onPause(){
	    	super.onPause();
	    	Log.v(TAG, "onPause");
	    }
	    
	    public void onResume(){
	    	super.onResume();
	    	Log.v(TAG, "onResume");
	    }
	    
	    public void onStop(){
	    	super.onStop();
	    	Log.v(TAG, "onStop");
	    }
	    
	    public void onRestart(){
	    	super.onRestart();
	    	Log.v(TAG, "onRestart");
	    }
	    
	    public void onDestroy(){
	    	super.onDestroy();
	    	Log.v(TAG, "onDestroy");    	
	    }

}
