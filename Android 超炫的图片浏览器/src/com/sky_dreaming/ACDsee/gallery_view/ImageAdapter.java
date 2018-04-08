package com.sky_dreaming.ACDsee.gallery_view;

import java.util.List;

import com.sky_dreaming.ACDsee.R;

import android.content.Context;
import android.content.res.TypedArray;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
/**
 * ****************************************************************
 * 文件名称	: ImageAdapter.java
 * 创建时间	: 2010-11-2 下午05:27:23
 * 文件描述	: 自定义Gallery适配器，添加视图阴影
 *****************************************************************
 */
public class ImageAdapter extends BaseAdapter {

	private int mGalleryItemBackground;
	private Context mContext;
	/**
	 * 图像路径列表
	 */
	private List<String> lis;

	public ImageAdapter(Context c,  List<String> li) {
		mContext = c;
		lis = li;

		TypedArray typedArray = mContext.obtainStyledAttributes(R.styleable.Gallery);

		mGalleryItemBackground = typedArray.getResourceId(
				R.styleable.Gallery_android_galleryItemBackground, 0);
		typedArray.recycle();
	}
	
	public ImageView createReflectedImages(String filePath) {

		final int reflectionGap = 4;

		Bitmap originalImage = BitmapFactory.decodeFile(filePath);
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		/**
		 * 创建一个绘有bitmapWithReflection的画布，画布质量和bitmapWithReflection质量相同
		 * 也就相当于把bitmapWithReflection作为画布使用
		 * 该画布高度为 原图 + 间隔 + 倒影
		 */
		Canvas canvas = new Canvas(bitmapWithReflection);

		/**
		 * 在画布左上角（0,0）绘制原始图
		 */
		canvas.drawBitmap(originalImage, 0, 0, null);

		Paint deafaultPaint = new Paint();

		canvas.drawRect(0, height, width, height + reflectionGap,
				deafaultPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();

		LinearGradient shader = new LinearGradient(0, originalImage
				.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);

		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

			/**
			 * 在倒影图上用带阴影的画笔绘制矩形
			 */
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		ImageView imageView = new ImageView(mContext);
			
			/**
			 * BitmapDrawable bd = new BitmapDrawable(bitmapWithReflection);
			 * bd.setAntiAlias=true;
			 * imageView.setImageDrawable(bd);
			 * 替代下面：
			 * imageView.setImageBitmap(bitmapWithReflection);
			 * 可实现消除锯齿的效果
			 */
		imageView.setImageBitmap(bitmapWithReflection);
		imageView.setLayoutParams(new GalleryFlow.LayoutParams(180, 240));
		imageView.setBackgroundResource(mGalleryItemBackground);
			
			/**
			 * 设置图像缩放模式以适应ImageView大小。ScaleType.MATRIX：绘制时使用图像变换矩阵缩放。
			 * 
			 * 注意：如果执行此行代码，则原始图盛满整个ImageView，无倒影效果
			 */
		//imageView.setScaleType(ScaleType.MATRIX);
		return imageView;
	}
	@Override
	public int getCount() {
		return lis.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createReflectedImages(lis.get(position));
	}
//	public float getScale(boolean focused, int offset) {
//		/* Formula: 1 / (2 ^ offset) */
//		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
//	}
}
