package gl.test.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends Activity{
	 private static final float TOUCH_TOLERANCE = 4;
	private Bitmap imgBitmap;
	private Paint mPaint;
	private Canvas mCanvas;
	private Path mPath;
	private float mX;
	private float mY;
	private Context mContext;

	public PaintView (Context context ,Bitmap bmp,Paint paint){
		 super();
		 this.imgBitmap = bmp;
		 this.mPaint = paint;
		 this.mContext = context;
		 
		 mCanvas = new Canvas(bmp);
		 mPath = new Path();
	 }
	
	
	
	
	
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(imgBitmap, 0, 0, mPaint);
		canvas.drawPath(mPath, mPaint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		
	}
	
	
	public Bitmap getBitmap(){
		return imgBitmap;
	}
	
	private void touchStart(float x,float y){
		mPath.reset();
		mPath.moveTo(x, y);
		this.mX = x;
		this.mY = y;
	}
	
	private void touchUp(){
		mPath.lineTo(mX, mY);
		mCanvas.drawPath(mPath, mPaint);
		mPath.reset();
	}
	

	private void touchMove(float x,float y ){
		float dx = Math.abs(x-mX);
		float dy = Math.abs(y-mY);
		if(dx>=TOUCH_TOLERANCE || dy >=TOUCH_TOLERANCE){
			mPath.quadTo(mX, mY, (x+mX)/2,(y+mY)/2);//贝塞尔曲线，用来画曲线
			mX=x;
			mY=y;
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float x = event.getX();
		float y = event.getY();
		
		
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			touchStart(x, y);
			
			
			break;
		case MotionEvent.ACTION_MOVE:
			touchMove(x, y);
			
			break;
		case MotionEvent.ACTION_UP:
			touchUp();
			break;

		default:
			break;
		}
		return true;
	}
}
