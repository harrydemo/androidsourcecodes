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

	private MainActivity mainActivity;//�õ�activity������  ������handle��Ϣ
	private Bitmap kcbj;//����  
	private Bitmap callBoard;//����  
	private Paint paint;//����
	
	public VictoryView(Context context) {
		super(context);
		this.mainActivity = (MainActivity) context;
		kcbj = BitmapFactory.decodeResource(this.getResources(), R.drawable.kcbj);
		callBoard = BitmapFactory.decodeResource(this.getResources(), R.drawable.callboard);
		paint = new Paint();
		this.setFocusable(true);//���õ�ǰView��ӵ�п��ƽ���
	}
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(kcbj, 0, 0, null);
		canvas.drawBitmap(callBoard, ConstantUtil.screenWidth/2-callBoard.getWidth()/2,ConstantUtil.screenHeight/2-callBoard.getHeight()/2,  null);// 
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(30);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);//�������
		canvas.drawText("��ϲ�� ʤ����!",328,247,  paint);
	}
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mainActivity.myHandler.sendEmptyMessage(4);//��activity������Ϣ ��activity�л�ҳ��
			break;
		default:
			break;
		}
		postInvalidate();
		return true;//����ʱ�䲻���ݸ�activity
	}

}
