package com.starnet.android.pisces.compass;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class about extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		StringBuilder sb = new StringBuilder(getString(R.string.app_name));
		sb.append(" ");
		sb.append(getString(R.string.app_ver));
		
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		
		
		((TextView) findViewById(R.id.tvTitle)).setText(sb.toString());
		((TextView) findViewById(R.id.tvContent)).setText(R.string.aboutContent);
		
		((Button) findViewById(R.id.btnOk)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
