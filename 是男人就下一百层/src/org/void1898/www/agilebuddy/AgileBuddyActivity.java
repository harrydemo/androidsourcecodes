package org.void1898.www.agilebuddy;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * main activity of AgileBuddy
 * 
 * @author void1898@gamil.com
 * 
 */
public class AgileBuddyActivity extends Activity {

	private AgileBuddyView mAgileBuddyView;

	private SensorManager mSensorManager;

	private Sensor mSensor;

	private SensorEventListener mSensorEventListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ��ֹ��Ļ����
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// ȫ��Ļ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		mAgileBuddyView = (AgileBuddyView) findViewById(R.id.agile_buddy);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorEventListener = new SensorEventListener() {
			public void onSensorChanged(SensorEvent e) {
				mAgileBuddyView.handleMoving(e.values[SensorManager.DATA_X]);
			}

			public void onAccuracyChanged(Sensor s, int accuracy) {
			}
		};
		// ע��������Ӧ����
		mSensorManager.registerListener(mSensorEventListener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void finish() {
		// ע��������Ӧ����
		mSensorManager.unregisterListener(mSensorEventListener, mSensor);
		super.finish();
	}
}