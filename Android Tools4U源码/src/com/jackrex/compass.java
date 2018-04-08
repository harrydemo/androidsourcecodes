package com.jackrex;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class compass extends Activity implements SensorEventListener {
	
	private ImageView imageView;
	private float currentDegree = 0f;


	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

			float degree = event.values[0];
			// 以指南针图像中心为轴逆时针旋转degree角度
			RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			// 在200毫秒之内完成旋转动作
			ra.setDuration(200);
			// 开始旋转图像
			imageView.startAnimation(ra);
			// 保存旋转后的度数
			currentDegree = -degree;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compass);

		imageView = (ImageView) findViewById(R.id.imageview);

		SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_FASTEST);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
		
		}
		return false;
	}

}