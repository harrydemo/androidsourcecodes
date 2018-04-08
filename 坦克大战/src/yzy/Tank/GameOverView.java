package yzy.Tank;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class GameOverView extends View{
	private MainActivity mainActivity;//�õ�activity������  ������handle��Ϣ
	private Bitmap gameover;//����  
	public GameOverView(Context context) {
		super(context);
		this.mainActivity = (MainActivity) context;
		gameover = BitmapFactory.decodeResource(this.getResources(), R.drawable.gameover);
		this.setFocusable(true);//���õ�ǰView��ӵ�п��ƽ���
	}
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(gameover, 0, 0, null);// 
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
