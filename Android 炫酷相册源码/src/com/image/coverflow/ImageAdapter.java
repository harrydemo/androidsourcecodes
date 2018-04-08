package com.image.coverflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    private int[] mImageIds;
    private ImageView[] mImages;
    public ImageAdapter(Context c,int[] ImageIds){
    	mContext=c;
    	mImageIds=ImageIds;
    	mImages=new ImageView[mImageIds.length];
    }
    public void createReflectedImages() {
		// TODO Auto-generated method stub
     final int reflectionGap=4;
     int index=0;
     for (int imageId : mImageIds) {
		Bitmap originalImage=BitmapFactory.decodeResource(mContext.getResources(),
				imageId);
		int width=originalImage.getWidth();
		int height=originalImage.getHeight();
		Matrix matrix=new Matrix();
	    matrix.preScale(1, -1);
	    Bitmap reflectionImage=Bitmap.createBitmap(originalImage, 0,
	    		height/2, width, height/2,matrix,false);
	    Bitmap bitmapWithReflection=Bitmap.createBitmap(width,
	    		(height+height/2), Config.ARGB_8888);
	    Canvas canvas=new Canvas(bitmapWithReflection);
	    canvas.drawBitmap(originalImage, 0, 0,null);
	    Paint defaultPaint=new Paint();
	    canvas.drawRect(0, height,width,height+reflectionGap,defaultPaint);
	    canvas.drawBitmap(reflectionImage, 0, height+reflectionGap,null);
	    Paint paint=new Paint();
	    LinearGradient shader=new LinearGradient(0, originalImage.getHeight(),
	    		0, bitmapWithReflection.getHeight()+reflectionGap, 0x70ffffff,0x00ffffff , TileMode.CLAMP);
	    paint.setShader(shader);
	    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	    canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()+reflectionGap, paint);
	    ImageView imageView=new ImageView(mContext);
	    imageView.setImageBitmap(bitmapWithReflection);
	    imageView.setLayoutParams(new GalleryFlow.LayoutParams(160,240));
	    mImages[index++]=imageView;
     }
   
	}
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageIds.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return mImages[position];
	}
	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
}
