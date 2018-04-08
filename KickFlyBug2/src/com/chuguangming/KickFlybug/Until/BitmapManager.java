package com.chuguangming.KickFlybug.Until;


import java.util.HashMap;

import com.chuguangming.R;
import com.chuguangming.KickFlybug.GameObject.Fly;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class BitmapManager {
	//定义资源文件
	static Resources staticResources;
    //定义资源文件集合
	static HashMap<Integer, Bitmap> map = new HashMap<Integer, Bitmap>();
	/**
	 * 加载并初始化苍蝇的图片
	 * 
	 * @param res
	 */
	public static void initFlyBitmap(Resources res) 
	{
		map.put(R.drawable.fly, 
		loadBitmap(res.getDrawable(R.drawable.fly), Fly.FLY_WIDTH, Fly.FLY_HEIGHT));
		map.put(R.drawable.deadfly,
		loadBitmap(res.getDrawable(R.drawable.deadfly), Fly.FLY_WIDTH, Fly.FLY_HEIGHT));
	}
	/**
	 * 加载图片
	 * 
	 * @param d
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap loadBitmap(Drawable d, int width, int height) {
		Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		d.setBounds(0, 0, width, height);
		d.draw(c);
		return b;
	}

	/**
	 * 初始化并加载图片资源
	 * 
	 * @param res
	 */
	public static void init(Resources res) {
		staticResources = res;
	}

	/**
	 * 获取指定资源Id的图片
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap getBitmap(int drawable) {
		if (!map.containsKey(drawable)) {
			return null;
		}
		return map.get(drawable);
	}


}
