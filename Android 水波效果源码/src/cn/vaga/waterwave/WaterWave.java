package cn.vaga.waterwave;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class WaterWave extends Activity {
	WaterWaveView waterWaveView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		waterWaveView = new WaterWaveView(this);
		setContentView(waterWaveView);
	}
	
//	@Override
//	protected void onResume() {
//		waterWaveView.wavingThread.setRunning(true);
//		super.onResume();
//	}
//	
//	@Override
//	protected void onPause() {
//		waterWaveView.wavingThread.setRunning(false);
//		super.onPause();
//	}

}