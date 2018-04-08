package hong.specialEffects.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class Processor {
	/**
     * ��ͼƬ���Ľ�Բ��
     * @param bitmap ԭͼ
     * @param roundPixels Բ����
     * @param half �Ƿ��ȡ���
     * @return
     */
    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels,int half)
    {
    	int width=bitmap.getWidth();
    	int height=bitmap.getHeight();
    	
        //����һ����ԭʼͼƬһ����Сλͼ
        Bitmap roundConcerImage = Bitmap.createBitmap(width,height, Config.ARGB_8888);
        //��������λͼroundConcerImage�Ļ���
        Canvas canvas = new Canvas(roundConcerImage);
        //��������
        Paint paint = new Paint();
        //����һ����ԭʼͼƬһ����С�ľ���
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        // ȥ���
        paint.setAntiAlias(true);
        
        
        //��һ����ԭʼͼƬһ����С��Բ�Ǿ���
        canvas.drawRoundRect(rectF, roundPixels, roundPixels , paint);
        //�����ཻģʽ
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        //��ͼƬ��������ȥ
        canvas.drawBitmap(bitmap, null, rect, paint);
        switch(half){
        	case HalfType.LEFT:
        		return Bitmap.createBitmap(roundConcerImage, 0, 0, width/2, height);
        	case HalfType.RIGHT:
        		return Bitmap.createBitmap(roundConcerImage, width/2, 0, width/2, height);
        	case HalfType.TOP:
        		return Bitmap.createBitmap(roundConcerImage, 0, 0, width, height/2);
        	case HalfType.BOTTOM:
        		return Bitmap.createBitmap(roundConcerImage, 0, height/2, width, height/2);
        	case HalfType.NONE:
        		return roundConcerImage;
        	default:
        		return roundConcerImage;
        }
    }
    
    /**
     * ָ�����ƽ��ͼƬ
     * @param src ԭͼ
     * @param width ƽ�̺�Ŀ��
     * @return
     */
    public static Bitmap createRepeater(Bitmap src,int width){
		int count = (width + src.getWidth() - 1) / src.getWidth();
	
		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
	
		for(int idx = 0; idx < count; ++ idx){
			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
	
		return bitmap;
	}
    

    public interface HalfType{
    	public static final int LEFT=1;
    	public static final int RIGHT=2;
    	public static final int TOP=3;
    	public static final int BOTTOM=4;
    	public static final int NONE=0;
    }
}
