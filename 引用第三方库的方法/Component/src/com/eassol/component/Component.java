package com.eassol.component;

import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class Component extends LinearLayout {
	private static String tag = "Base";
	protected Context context;

	public Component(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public Component(Context context) {
		super(context);
		this.context = context;
		init();
	}
	private void init() {
		try {
			//获取图片转换为Bitmap
			AssetManager localAssetManager = this.getResources().getAssets();
			String str = "home_pressed.png";
			InputStream localInputStream = localAssetManager.open(str);
			Bitmap localBitmap = BitmapFactory.decodeStream(localInputStream);
			localInputStream.close();
			//把Bitmap图像转换为Drawable
			Drawable drawable = new BitmapDrawable(localBitmap);  
			//设置背景
			if(localBitmap != null){
				setBackgroundDrawable(drawable);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
