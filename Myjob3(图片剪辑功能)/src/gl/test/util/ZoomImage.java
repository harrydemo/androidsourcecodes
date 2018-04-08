package gl.test.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ZoomImage {
	
	public static Bitmap mBitmap;
	
	public static Bitmap zoomImg(Bitmap bmp, float scaleWidth){
		
		//获得图片的宽高
		
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		
		//计算缩放比
		/*float scaleWidth = ((float)newWidth )/width;
		float scaleHeigh = ((float)newHeight)/height;*/
		float scaleHeigh = scaleWidth;
		//取得想要缩放的MARTIX参数
		Matrix matix = new Matrix();
		matix.postScale(scaleWidth, scaleHeigh);
		
		
		//得到新的图片
		Bitmap newBmp = Bitmap.createBitmap(bmp, 0,0,width,height,matix, true);
		
		return newBmp;
		
		
		
	}
	
	
	

}
