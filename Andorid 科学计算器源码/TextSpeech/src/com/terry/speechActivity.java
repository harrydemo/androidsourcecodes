package com.terry;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class speechActivity extends Activity {
	private TextToSpeech mSpeech;
	private Button btn;

	private EditText mEditText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btn = (Button) findViewById(R.id.Button01);
		mEditText = (EditText) findViewById(R.id.EditText01);
		btn.setEnabled(false);
		mSpeech = new TextToSpeech(this, new OnInitListener() {

			@Override
			public void onInit(int status) {
				// TODO Auto-generated method stub
				if (status == TextToSpeech.SUCCESS) {
					int result = mSpeech.setLanguage(Locale.ENGLISH);
					if (result == TextToSpeech.LANG_MISSING_DATA
							|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
						Log.e("lanageTag", "not use");
					} else {
						btn.setEnabled(true);
						mSpeech.speak("i love you", TextToSpeech.QUEUE_FLUSH,
								null);
					}
				}
			}
		});

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSpeech.speak(mEditText.getText().toString(),
						TextToSpeech.QUEUE_FLUSH, null);
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mSpeech != null) {
			mSpeech.stop();
			mSpeech.shutdown();
		}
		super.onDestroy();
	}
}