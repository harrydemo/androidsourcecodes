package gl.test.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ZoomImage {
	
	public static Bitmap mBitmap;
	
	public static Bitmap zoomImg(Bitmap bmp, float scaleWidth){
		
		//���ͼƬ�Ŀ��
		
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		
		//�������ű�
		/*float scaleWidth = ((float)newWidth )/width;
		float scaleHeigh = ((float)newHeight)/height;*/
		float scaleHeigh = scaleWidth;
		//ȡ����Ҫ���ŵ�MARTIX����
		Matrix matix = new Matrix();
		matix.postScale(scaleWidth, scaleHeigh);
		
		
		//�õ��µ�ͼƬ
		Bitmap newBmp = Bitmap.createBitmap(bmp, 0,0,width,height,matix, true);
		
		return newBmp;
		
		
		
	}
	
	
	

}
