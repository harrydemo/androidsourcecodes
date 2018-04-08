package cn.sharp.android.ncr;

import cn.sharp.android.ncr.view.ScannerView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;

public class RecInProgress extends Activity {
	private final static String TAG = "RecInProgress";

	public final static String THUMB_IMAGE = "thumb_image";

	private ScannerView scannerView;
	private ScannerAnimation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra(THUMB_IMAGE);
		if (bitmap != null) {
			scannerView = new ScannerView(this);
			scannerView.setBitmap(bitmap);
			setContentView(scannerView);
			animation = new ScannerAnimation(bitmap);
			animation.setDuration(4000);
			animation.setBandWidth(50);
			animation.setStartOffset(1000);
			animation.setRepeatMode(Animation.REVERSE);
			animation.setRepeatCount(Animation.INFINITE);
		} else {
			Log.e(TAG, "bitmap==null");
			setResult(RESULT_CANCELED);
			finish();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		scannerView.startAnimation(animation);
	}

}
