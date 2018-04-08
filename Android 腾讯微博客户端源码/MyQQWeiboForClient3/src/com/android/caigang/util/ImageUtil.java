package com.android.caigang.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtil {

	public static InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		if (conn.getResponseCode() == 200){
			return conn.getInputStream();
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
	
	public static Drawable loadImageFromUrl(String url){
        URL m;
        InputStream i = null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }
	
	public static Drawable getDrawableFromUrl(String url) throws Exception{
		 return Drawable.createFromStream(getRequest(url),null);
	}
	
	public static Bitmap getBitmapFromUrl(String url) throws Exception{
		byte[] bytes = getBytesFromUrl(url);
		return byteToBitmap(bytes);
	}
	
	public static Bitmap getRoundBitmapFromUrl(String url,int pixels) throws Exception{
		byte[] bytes = getBytesFromUrl(url);
		Bitmap bitmap = byteToBitmap(bytes);
		return toRoundCorner(bitmap, pixels);
	} 
	
	public static Drawable geRoundDrawableFromUrl(String url,int pixels) throws Exception{
		byte[] bytes = getBytesFromUrl(url);
		BitmapDrawable bitmapDrawable = (BitmapDrawable)byteToDrawable(bytes);
		return toRoundCorner(bitmapDrawable, pixels);
	} 
	
	public static byte[] getBytesFromUrl(String url) throws Exception{
		 return readInputStream(getRequest(url));
	}
	
	public static Bitmap byteToBitmap(byte[] byteArray){
		if(byteArray.length!=0){ 
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length); 
        } 
        else { 
            return null; 
        }  
	}
	
	public static Drawable byteToDrawable(byte[] byteArray){
		ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
		return Drawable.createFromStream(ins, null);
	}
	
	public static byte[] Bitmap2Bytes(Bitmap bm){ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	 	/**
	      * ͼƬȥɫ,���ػҶ�ͼƬ
	      * @param bmpOriginal �����ͼƬ
	     * @return ȥɫ���ͼƬ
	     */
	    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
	        int width, height;
	        height = bmpOriginal.getHeight();
	        width = bmpOriginal.getWidth();    
	
	        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	        Canvas c = new Canvas(bmpGrayscale);
	        Paint paint = new Paint();
	        ColorMatrix cm = new ColorMatrix();
	        cm.setSaturation(0);
	        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	        paint.setColorFilter(f);
	        c.drawBitmap(bmpOriginal, 0, 0, paint);
	        return bmpGrayscale;
	    }
	    
	    
	    /**
	     * ȥɫͬʱ��Բ��
	     * @param bmpOriginal ԭͼ
	     * @param pixels Բ�ǻ���
	     * @return �޸ĺ��ͼƬ
	     */
	    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
	        return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	    }
	    
	    /**
	     * ��ͼƬ���Բ��
	     * @param bitmap ��Ҫ�޸ĵ�ͼƬ
	     * @param pixels Բ�ǵĻ���
	     * @return Բ��ͼƬ
	     */
	    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
	
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);
	
	        final int color = 0xff424242;
	        final Paint paint = new Paint();
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	        final RectF rectF = new RectF(rect);
	        final float roundPx = pixels;
	
	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);
	
	        return output;
	    }
	
	    
	   /**
	     * ʹԲ�ǹ���֧��BitampDrawable
	     * @param bitmapDrawable 
	     * @param pixels 
	     * @return
	     */
	    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
	        Bitmap bitmap = bitmapDrawable.getBitmap();
	        bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
	        return bitmapDrawable;
	    }
	
}
