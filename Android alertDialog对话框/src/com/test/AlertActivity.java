package com.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlertActivity extends Activity {

	private Button mButton1;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mButton1 = (Button) findViewById(R.id.myButton1);
		mButton1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(AlertActivity.this).setTitle(
						R.string.app_about).setMessage(R.string.app_about_msg)
						.setPositiveButton(R.string.str_ok,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialoginterface,
											int i) {
									}
								}).show();
			}
		});
	}
}