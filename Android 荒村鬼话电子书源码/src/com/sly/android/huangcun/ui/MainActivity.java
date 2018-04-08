package com.sly.android.huangcun.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
					Intent intent = new Intent(MainActivity.this, MyListAct.class);
					startActivity(intent);
					MainActivity.this.finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	@Override
	protected void onResume() {
		super.onResume();
		MusicManager.play(this, R.raw.wel);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MusicManager.stop(this);
	}
    }
