package com.appspot.hexagon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;

public class HexagonGameActivity extends Activity {

	// Just a RANDOM ID to recognize a Message later
	protected static final int GUIUPDATEIDENTIFIER = 0x101;
	private HexagonGameThread gameThread = null;
	/* Our 'ball' is located within this View */
	HexagonGameView hexView = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// Set fullscreen
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main);

		this.hexView = (HexagonGameView) findViewById(R.id.hexagon);
        gameThread = this.hexView.getThread();

		gameThread.doStart();
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        gameThread.pause(); // pause game when Activity pauses
    }
	
	@Override
    protected void onSaveInstanceState(Bundle state) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(state);
        gameThread.saveState(state);
        Log.w(this.getClass().getName(), "saving game ...");
    }
}