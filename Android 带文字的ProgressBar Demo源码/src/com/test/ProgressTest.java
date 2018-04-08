package com.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProgressTest extends Activity {

	private Button btn_go = null;
	private MyProgress myProgress = null;

	private Handler mHandler;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		findView();
		setParam();
		addListener();

		mHandler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				myProgress.setProgress(msg.what);
				return false;
			}
		});
	}

	private void findView() {
		btn_go = (Button) findViewById(R.id.btn_start);
		myProgress = (MyProgress) findViewById(R.id.progressBar);
	}

	private void setParam() {
		btn_go.setText("¿ªÊ¼");
	}

	private void addListener() {

		btn_go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i <= 50; i++) {
							mHandler.sendEmptyMessage(i * 2);
							try {
								Thread.sleep(80);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

				}).start();
			}
		});
	}

}