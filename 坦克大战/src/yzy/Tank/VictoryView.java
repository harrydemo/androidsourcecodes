package yzy.Tank;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class VictoryView extends View{

	private MainActivity mainActivity;//得到activity的引用  用来发handle消息
	private Bitmap kcbj;//背景  
	private Bitmap callBoard;//背景  
	private Paint paint;//画笔
	
	public VictoryView(Context context) {
		super(context);
		this.mainActivity = (MainActivity) context;
		kcbj = BitmapFactory.decodeResource(this.getResources(), R.drawable.kcbj);
		callBoard = BitmapFactory.decodeResource(this.getResources(), R.drawable.callboard);
		paint = new Paint();
		this.setFocusable(true);//设置当前View先拥有控制焦点
	}
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(kcbj, 0, 0, null);
		canvas.drawBitmap(callBoard, ConstantUtil.screenWidth/2-callBoard.getWidth()/2,ConstantUtil.screenHeight/2-callBoard.getHeight()/2,  null);// 
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(30);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
		canvas.drawText("恭喜您 胜利了!",328,247,  paint);
	}
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mainActivity.myHandler.sendEmptyMessage(4);//给activity发送消息 让activity切换页面
			break;
		default:
			break;
		}
		postInvalidate();
		return true;//触摸时间不传递给activity
	}

}
