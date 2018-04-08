package cn.sharp.android.ncr.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ScannerView extends View {
	private final static String TAG = "ScannerView";
	private Bitmap bitmap;

	public ScannerView(Context context) {
		super(context);
	}

	public ScannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.e(TAG, "调用了scannerView的onDraw方法");
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, 0, 0, null);
		} else {
			Log.i(TAG, "bitmap is null");
		}
		super.onDraw(canvas);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}
