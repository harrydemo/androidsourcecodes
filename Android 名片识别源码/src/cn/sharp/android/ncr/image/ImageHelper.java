package cn.sharp.android.ncr.image;

import java.nio.ShortBuffer;

import android.graphics.Bitmap;
import android.util.Log;

public class ImageHelper {
	private final static String TAG = "ImageHelper";

	/**
	 * 
	 * @param yuv
	 * @param width
	 * @param height
	 * @return RGB565 format bitmap
	 */
	public static Bitmap fromYUV420P(byte[] yuv, int width, int height) {
		if (yuv == null) {
			Log.e(TAG, "yuv data==null");
			return null;
		}
		if (yuv.length != width * height * 1.5) {
			Log.e(TAG, "yudData does not match the provided width and height");
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		int offsetY = 0;
		ShortBuffer buffer = ShortBuffer.allocate(width * height * 2);
		for (int line = 0; line < height; line++) {
			for (int col = 0; col < width; col++) {
				int y = yuv[offsetY++] & 0xFF;
				buffer.put((short) ((y >> 3) << 11 | (y >> 2) << 5 | (y >> 3)));
			}
		}
		bitmap.copyPixelsFromBuffer(buffer);
		return bitmap;
	}
}
