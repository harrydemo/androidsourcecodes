package com.ebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Readaddress extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.address);
		
	}
	public void onBackPressed() {
		super.onBackPressed();
		
    	Intent intent = new Intent(Readaddress.this,Contents.class);
    	startActivity(intent);
        finish();
    	}

	
}
