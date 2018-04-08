package com.gw.android.boom.globe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
/**
 * 为了使游戏更加流畅，所以利用双缓存机制
 * @author 
 */
public class Utils {

	public static Bitmap mscBitmap = Bitmap.createBitmap(480, 320, Config.ARGB_8888);

	public static Canvas getCanvas() {
		Canvas canvas = new Canvas();
		canvas.setBitmap(mscBitmap);
		return canvas;
	}
}
