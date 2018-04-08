package org.app.music;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
/**
 Loading界面，采用Handler的postDelayed开一个线程
 *
 */
public class LocalLoginActivity extends Activity {
	private static final int WAITTIME = 2500;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
        new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent intent = new Intent(LocalLoginActivity.this,
						LocalTabMusicListActivity.class);
				startActivity(intent);
			
				finish();

			}
		}, WAITTIME);
		
	}

}
