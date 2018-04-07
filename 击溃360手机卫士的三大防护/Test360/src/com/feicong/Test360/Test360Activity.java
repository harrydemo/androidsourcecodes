package com.feicong.Test360;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Test360Activity extends Activity {
    /** Called when the activity is first created. */
	
	public static void StartActivity(Context context){
		Intent newIntent = new Intent(context, Test360Activity.class);
		newIntent.setAction("android.intent.action.MAIN");
		newIntent.addCategory("android.intent.category.LAUNCHER");
		newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newIntent);  //Æô¶¯Activity
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        (findViewById(R.id.startLocalService)).setOnClickListener( 
                new View.OnClickListener(){ 
                        public void onClick(View view) {                         	
                        	startService(new Intent("com.demo.SERVICE_DEMO"));                         	
                        }  
                });         
        //String apkRoot="chmod 777 "+ getPackageCodePath(); 
        //RootUtils.RootCommand(apkRoot);               

    }    
    
    public static void main(String args[]){
    	Log.i("Test360", "Test 360 boot...");
    }
}


