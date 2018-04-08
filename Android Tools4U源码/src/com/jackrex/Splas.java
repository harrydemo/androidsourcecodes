package com.jackrex;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class Splas extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 setContentView(R.layout.splash);
			new Handler().postDelayed(new Runnable() {

				
				public void run() {
					// TODO Auto-generated method stub
					Intent it = new Intent(Splas.this, Tools4UActivity.class);
					startActivity(it);
					finish();
				}
			}, 1000);
	}
	
	
	
	
}
