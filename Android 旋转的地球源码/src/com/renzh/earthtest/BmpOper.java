package com.renzh.earthtest;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BmpOper {
 public static Bitmap  getBmpFromRaw(Context  cx,int imgId){
		InputStream is =cx.getResources().openRawResource(imgId);
		Bitmap resBMP = null;
		try {
			resBMP = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException exception) {
			}
		}
		return resBMP;
 }
 public static Bitmap decorateBmp(Bitmap src){
	
	 int w=src.getWidth(),h=src.getHeight();
	 int colLine=24,halfRowLine=6;
	 float dCol=w/(float)colLine,dRow=h/(float)halfRowLine/2;
	 Bitmap canvasBmp=Bitmap.createBitmap(w,h,Config.RGB_565);
	 Paint paint=new Paint();
	 Canvas can=new Canvas (canvasBmp);
	 can.drawBitmap(src, 0, 0, null);
	 paint.setStrokeWidth(0.3f);
	 paint.setColor(Color.BLUE);
	 can.drawLine(0, h>>1, w, h>>1, paint);//画中间的一条赤道线。
	 paint.setStrokeWidth(0.15f);
	 paint.setColor(Color.WHITE);
	 for(int i=0;i<halfRowLine;i++){
		 can.drawLine(0, i*dRow, w, i*dRow, paint);//画赤道之上的纬线。
		 can.drawLine(0, h-i*dRow, w, h-i*dRow, paint);//画赤道下的纬线。
	 }
	 paint.setColor(Color.RED);
	 for(int i=0;i<=colLine;i++){
		 can.drawLine(i*dCol, 0,i*dCol,h, paint);//画经线。
	 }
	 return canvasBmp;
 }
 
 
}
