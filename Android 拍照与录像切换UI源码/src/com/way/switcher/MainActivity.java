package com.way.switcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.way.view.RotateImageView;
import com.way.view.Switcher;
import com.way.view.Switcher.OnSwitchListener;

public class MainActivity extends Activity implements OnClickListener {
	private RotateImageView mSwitchCameraView;
	private RotateImageView mSwitchVideoView;
	private MyOrientationEventListener mOrientationListener;
	private Switcher mSwitcher;
	private long exitTime = 0L;// 保存上次按的返回键的时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mSwitcher = (Switcher) findViewById(R.id.camera_switch);
		mSwitchCameraView = (RotateImageView) findViewById(R.id.camera);
		mSwitchVideoView = (RotateImageView) findViewById(R.id.video);
		mSwitchCameraView.setOnClickListener(this);
		mSwitchVideoView.setOnClickListener(this);

		mOrientationListener = new MyOrientationEventListener(this);
		mOrientationListener.enable();
		
		mSwitcher.setOnSwitchListener(new OnSwitchListener() {

			@Override
			public boolean onSwitchChanged(Switcher source, boolean onOff) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "现在选择是：" + onOff,
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}

	class MyOrientationEventListener extends OrientationEventListener {

		public MyOrientationEventListener(Context context) {
			super(context);
		}

		@Override
		public void onOrientationChanged(int orientation) {
			// TODO Auto-generated method stub
			if (orientation == ORIENTATION_UNKNOWN)
				return;
			int mOrientation = ((orientation + 45) / 90 * 90) % 360;
			int orientationCompensation = mOrientation
					+ getDisplayRotation(MainActivity.this);
			mSwitchCameraView.setDegree(orientationCompensation);
			mSwitchVideoView.setDegree(orientationCompensation);
		}

		public int getDisplayRotation(Activity activity) {
			int rotation = activity.getWindowManager().getDefaultDisplay()
					.getRotation();
			switch (rotation) {
			case Surface.ROTATION_0:
				return 0;
			case Surface.ROTATION_90:
				return 90;
			case Surface.ROTATION_180:
				return 180;
			case Surface.ROTATION_270:
				return 270;
			}
			return rotation;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.camera:
			break;
		case R.id.video:
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - exitTime > 2000) {
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = currentTime;
		} else {
			finish();
		}
	}
}
