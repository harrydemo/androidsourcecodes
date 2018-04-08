package com.bn.d2.bill;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class PicLoadUtil 
{

   //从资源中加载一幅图片
   public static Bitmap LoadBitmap(Resources res,int picId)
   {
	   Bitmap result=BitmapFactory.decodeResource(res, picId);
	   return result;
   }
   
   //缩放旋转图片的方法
   public static Bitmap scaleToFit(Bitmap bm,float ratio)//缩放图片的方法
   {
   	float width = bm.getWidth(); //图片宽度
   	float height = bm.getHeight();//图片高度	
   	
   	Matrix m1 = new Matrix(); 
   	m1.postScale(ratio, ratio);   	
   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//声明位图        	
   	return bmResult;
   }
   //缩放旋转图片的方法,使图片全屏,不等比缩放
   public static Bitmap scaleToFitFullScreen(Bitmap bm,float wRatio,float hRatio)//缩放图片的方法
   {
   	float width = bm.getWidth(); //图片宽度
   	float height = bm.getHeight();//图片高度	
   	
   	Matrix m1 = new Matrix(); 
   	m1.postScale(wRatio, hRatio);   	
   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//声明位图        	
   	return bmResult;
   }
}
