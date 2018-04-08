package com.bn.d2.bill;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class PicLoadUtil 
{

   //����Դ�м���һ��ͼƬ
   public static Bitmap LoadBitmap(Resources res,int picId)
   {
	   Bitmap result=BitmapFactory.decodeResource(res, picId);
	   return result;
   }
   
   //������תͼƬ�ķ���
   public static Bitmap scaleToFit(Bitmap bm,float ratio)//����ͼƬ�ķ���
   {
   	float width = bm.getWidth(); //ͼƬ���
   	float height = bm.getHeight();//ͼƬ�߶�	
   	
   	Matrix m1 = new Matrix(); 
   	m1.postScale(ratio, ratio);   	
   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//����λͼ        	
   	return bmResult;
   }
   //������תͼƬ�ķ���,ʹͼƬȫ��,���ȱ�����
   public static Bitmap scaleToFitFullScreen(Bitmap bm,float wRatio,float hRatio)//����ͼƬ�ķ���
   {
   	float width = bm.getWidth(); //ͼƬ���
   	float height = bm.getHeight();//ͼƬ�߶�	
   	
   	Matrix m1 = new Matrix(); 
   	m1.postScale(wRatio, hRatio);   	
   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//����λͼ        	
   	return bmResult;
   }
}
