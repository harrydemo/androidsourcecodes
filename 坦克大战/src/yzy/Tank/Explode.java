package yzy.Tank;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 
 * ����Ϊ��ը��
 * ���ƶ�λ�û��Ʊ�ը
 * �����߳�ͨ������nextFrame������֡
 *
 */

public class Explode {
	float x;//��ը��x����
	float y;//��ը��y���� 
	Bitmap[] bitmaps;//���б�ը��֡����
	int k = 0;//��ǰ֡
	
	Bitmap bitmap;//��ǰ֡
	Paint paint;//����
	GameView gameView;
	public Explode(float x2, float y2, GameView gameView,int type){
		this.x = x2;
		this.y = y2;
		if(type==1)//̹�˱�ըЧ��
			this.bitmaps = gameView.explodes;
		if(type==2)//������ըЧ��
			this.bitmaps = gameView.explodes2;
		paint = new Paint();//��ʼ������
		this.gameView = gameView;
	}

	public void draw(Canvas canvas) {// ���Ʒ���
		canvas.drawBitmap(bitmap, x, y, paint);
	}
	public boolean nextFrame(){//��֡���ɹ�����true�����򷵻�false
		if(k < bitmaps.length){
			bitmap = bitmaps[k];
			k++;
			return true;
		}
		return false;
	}
}