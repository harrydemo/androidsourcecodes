package cn.com.ldci.plants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class sun {
	public float left = 200; //Í¼Æ¬×ó±ß¾àÀë
	public float top = -20;// Í¼Æ¬ÉÏ±ß¾àÀë
	public static final int ONE = 0;// Í¼Æ¬ÏÂÂä×´Ì¬
	public static final int TWO = 1;//Í¼Æ¬µã»÷·µ»Ø×´Ì¬
	public static int current = 1;//Í¼Æ¬µ±Ç°×´Ì¬
	Bitmap sun;// Ì«Ñô
	Paint paint;// »­±Ê
	Context context;
	public sun(MainActivity context) {// ¹¹ÔìÆ÷
		this.context=context;
		initBitmap();
	}
/**
 * Í¼Æ¬µã»÷ÊÂ¼þ
 * @param event
 * @return
 */
	protected boolean onTouchEvent(MotionEvent event) {
		boolean flag = true;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();

			if ( (x - left) < 55 && (x - left) > 0
					&& (y - top) < 55 && (y - top) > 0) {
				flag = true;
				current = 2;
				GameView.suncount+=25;
			} 
		}
		return true;
	}
/**
 * »­Í¼
 * @param canvas
 */
	protected void Draw(Canvas canvas) {
		paint = new Paint();
		canvas.drawBitmap(sun, left, top, paint);

	}
/**
 * ³õÊ¼»¯Í¼Æ¬×ÊÔ´
 */
	protected void initBitmap() {
		sun = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.sun);
		sun = GameView.zoomImage(sun, 40, 40);

	}
	protected void move(){
		if(current==1){
			if(this.top<260){
				this.top+=8;
			}
		}else if(current==2){
			if(top>-50){
				float yleft = left;
				float ytop = 200;
				top -= 6;
				left -= yleft / ytop * 5;
			}
			
		}
	}

}