package cn.com.ldci.plants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * 
 * @author coffee
 * ����Ϊ�ӵ��ķ�װ��
 * ��¼���ӵ��������ز���
 * ���ͨ������move�����ƶ��ӵ�
 *
 */
public class Bullets {
	/**
	 * �ӵ�������
	 */
	float x;
	float y;
	/**
	 * �ӵ�������
	 */
	int type;
	/**
	 * ��ǰ�ӵ���ͼƬ
	 */
	Bitmap bitmap;
	/**
	 * gameView������
	 */
	GameView gameView;
	/**
	 * �ƶ�������
	 */
	private int moveSpan = 5;
	/**
	 * �ӵ���Ĺ��캯��
	 * @param x  �ӵ�����
	 * @param y
	 * @param type �ӵ�����
	 * @param gameView gameView����
	 */
	public Bullets(float x, float y, int type,GameView gameView){
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.type = type;
		this.initBitmap();
	}
	
	/**
	 * ͼƬ����
	 * @param bgimage ԭͼ
	 * @param newWidth �µĿ�
	 * @param newHeight �µĸ�
	 * @return
	 */
	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
		// ��ȡ���ͼƬ�Ŀ�͸�
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();
		// ���������ʣ��³ߴ��ԭʼ�ߴ�
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ����ͼƬ����
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
		matrix, true);
		return bitmap;

		}
	/**
	 * ��ʼ��ͼƬ
	 */
	private void initBitmap() {
		//������Ϊ1ʱ
		if(type == 1){
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bullet_01);
			bitmap = zoomImage(bitmap,14,14);
		}
		//����Ϊ2ʱ
		else if(type == 2){
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bullet_02);
		}
		
	}
	/**
	 * �ӵ����Ʒ���
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x, y,new Paint());	
	}
	/**
	 * �ӵ��ƶ�����
	 */
	public void move(){
		this.x = this.x + moveSpan;
	}

}
