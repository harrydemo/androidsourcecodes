package com.kris.reskin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Re_SkinActivity extends Activity {
	private LinearLayout layout;
	private Button btnSet;
	private Context friendContext;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnSet = (Button)findViewById(R.id.button1);
        layout = (LinearLayout)findViewById(R.id.layout);
        layout.setBackgroundResource(R.drawable.bg);
        //com.kris.reskin1
        try {
			friendContext =  createPackageContext("com.kris.reskin1", Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        btnSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						layout.setBackgroundDrawable(friendContext.getResources().getDrawable(R.drawable.bg));
					}
				});
			}
		});
    }
}