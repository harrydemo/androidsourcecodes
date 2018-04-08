package com.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Activity1 extends Activity {
	
	private String TAG="activity1";
  
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);
        
        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){
        		Intent intent = new Intent();
        		intent.setClass(Activity1.this,Activity2.class);
        		startActivity(intent);
        		Activity1.this.finish();
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