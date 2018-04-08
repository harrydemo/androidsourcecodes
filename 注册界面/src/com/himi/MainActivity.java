package com.himi;

import android.app.Activity;
import android.os.Bundle; 
import android.os.SystemClock;
import android.provider.Settings.System;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    public static Activity instance;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance =this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐去电池等图标和一切修饰部分（状态栏部分） 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐去标题栏（程序的名字）
        setContentView(new MySurfaceView(this)); 
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());  
		
	}
    
}