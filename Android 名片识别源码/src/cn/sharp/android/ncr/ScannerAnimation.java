package cn.sharp.android.ncr;

import java.nio.ShortBuffer;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * 
 * @author shao chuanchao 自定义的 动画效果
 */

public class ScannerAnimation extends Animation {
	private final static String TAG = "ScannerAnimation";
	private Bitmap bitmap;
	private int bandWidth;
	private ShortBuffer pixelBuffer;
	private short[] pixelBackup;

	private void init() {
		bandWidth = 50;
		setInterpolator(new LinearInterpolator());
		setRepeatCount(Animation.INFINITE);
		setDuration(4000);
		setStartOffset(1000);
		setRepeatMode(Animation.REVERSE);
	}

	public ScannerAnimation() {
		init();
	}

	public ScannerAnimation(Bitmap bitmap) {
		setBitmap(bitmap);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		Log.d(TAG, "apply transformation");
		if (bitmap != null) {
			int leftBound = 0;
			leftBound = (int) ((bitmap.getWidth() + bandWidth - 1) * interpolatedTime)
					- bandWidth;
			/** leftBound ranges from 0 to bitmap.getWidth()-1 */
			int pixelsPerRow = 1;
			if (leftBound < 0) {
				pixelsPerRow = leftBound + bandWidth;
				leftBound = 0;
			} else if (leftBound + bandWidth < bitmap.getWidth()) {
				pixelsPerRow = bandWidth;
			} else {
				pixelsPerRow = bitmap.getWidth() - leftBound;
			}
			ShortBuffer buffer2 = pixelBuffer.duplicate();
			pixelBuffer.rewind();
			buffer2.rewind();
			int start = leftBound;
			int count = 0;
			for (int i = 0; i < bitmap.getHeight(); i++) {
				pixelBuffer.position(start);
				buffer2.position(start);
				for (int j = 0; j < pixelsPerRow; j++) {
					short pixel = pixelBuffer.get();
					pixelBackup[count] = pixel;
					count++;
					pixel = (short) (pixel ^ 0xDDDD);
					buffer2.put(pixel);
				}
				start += bitmap.getWidth();
			}
			bitmap.copyPixelsFromBuffer(pixelBuffer);
			/** restore the pixel value */
			pixelBuffer.rewind();
			start = leftBound;
			count = 0;
			for (int i = 0; i < bitmap.getHeight(); i++) {
				pixelBuffer.position(start);
				for (int j = 0; j < pixelsPerRow; j++) {
					pixelBuffer.put(pixelBackup[count++]);
				}
				start += bitmap.getWidth();
			}
		} else
			Log.e(TAG, "bitmap==null");
	}

	public int getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(int bandWidth) {
		if (bandWidth > 0) {
			this.bandWidth = bandWidth;
			if (bitmap != null)
				pixelBackup = new short[bandWidth * bitmap.getHeight()];
		}
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		pixelBuffer = ShortBuffer.allocate(bitmap.getWidth()
				* bitmap.getHeight() * 2);
		pixelBackup = new short[bandWidth * bitmap.getHeight()];
		bitmap.copyPixelsToBuffer(pixelBuffer);
	}
}
