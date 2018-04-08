package com.catt.oss.common.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	    
	}
	protected void initialComponent(BaseActivity context){
		
	}
	protected void setComponentListener(BaseActivity context) {

	}

	protected void initialSetup(BaseActivity context) {

	}
	
   protected void onStart() {
		super.onStart();
		initialComponent(this);
		initialSetup(this);
		setComponentListener(this);
		
	}
}
