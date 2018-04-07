package com.hrw.android.player.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

import com.hrw.android.player.R;

public class ImageScale {

	public static BitmapDrawable getImage(Context context) {
		// 加载需要操作的图片，这里是eoeAndroid的logo图片
		Bitmap bitmapOrg = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon);

		// 获取这个图片的宽和高
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		// 定义预转换成的图片的宽度和高度
		int newWidth = 22;
		int newHeight = 22;

		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);

		// 旋转图片 动作
		// matrix.postRotate(45);

		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
				height, matrix, true);

		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
		return new BitmapDrawable(resizedBitmap);

	}

}
