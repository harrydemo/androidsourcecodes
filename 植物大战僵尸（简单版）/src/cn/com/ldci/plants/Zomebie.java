package cn.com.ldci.plants;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/***
 * 
 * @author t4 ��ʬ��
 */
public abstract class Zomebie {

	/**
	 * �����̵߳Ŀ���
	 */
	protected boolean avlive = true;
	/**
	 * ��ǰ��x����
	 */
	protected float currentX = 480;
	/**
	 * ��ǰ��y����
	 */
	protected float currentY;
	/**
	 *����ͼƬ��index
	 */
	protected int deadBitmapIndex;
	/**
	 * ����ͼƬ������
	 */
	protected Bitmap[] deadBitmap;
	/**
	 * ������ʱ��ͼƬ��index
	 */
	protected int hidedBitmapIndex;
	/**
	 * ����ʱ��ͼƬ��index
	 */
	protected int nomalBitmapIndex;
	/**
	 * ��ֲ��ʱ��ͼƬ��index
	 */
	protected int eatBitmapIndex;
	/**
	 * ������ʱ��ͼƬ����
	 */
	protected Bitmap[] hidedBitmap;
	/**
	 * ��ֲ��ʱ��ͼƬ����
	 */
	protected Bitmap[] eatPlant;
	/**
	 * ����ʱ��ͼƬ����
	 */
	protected Bitmap[] nomalBitmap;
	/**
	 * ��ǰ״̬
	 */
	protected byte currentState = 0;
	/**
	 * ����״̬
	 */
	public static final byte normalState = 0;
	/**
	 * ����״̬
	 */
	public static final byte deadState = 2;
	/**
	 * ��ֲ���״̬
	 */
	public static final byte eatState = 3;
	/**
	 * ������״̬
	 */
	public static final byte hitState = 1;
	/**
	 * ��һ��ֲ����ֲ����ٵ�����ֵ
	 */
	protected int eatlifes;
	/**
	 * ��ʬ��ǰ������ֵ
	 */
	protected int lifes=10;
	/**
	 * �ƶ��ĳ���
	 */
	protected int moveLength;
	Paint paint = new Paint();
	Matrix martix = new Matrix();
	/***
	 * �����������ȷ��Ҫ��Ҫ
	 */
	protected abstract void eat();

	/***
	 * ���Լ�
	 * 
	 * @param canvas
	 *            ����
	 */
	protected void draw(Canvas canvas) {
		switch (currentState) {
		case normalState:
			canvas.drawBitmap(nomalBitmap[nomalBitmapIndex], currentX,
					currentY, paint);
			
			break;
		case hitState:
			canvas.drawBitmap(hidedBitmap[hidedBitmapIndex], currentX, currentY, paint);
			break;

		case eatState:
			canvas.drawBitmap(eatPlant[eatBitmapIndex], currentX, currentY,
					paint);
			break;
		case deadState:
			canvas.drawBitmap(deadBitmap[deadBitmapIndex], currentX, currentY, paint);
			break;
		}

	}

	/***
	 * ���ӵ������������飬���������ֵ��
	 */
	protected abstract void hit();

	/***
	 * ���ӵ�����ײ�����ײ���
	 * 
	 * @return ������ײ����true ,û��������false
	 */
	protected boolean checkHitThing(Bullets b){
		if(isContain(b.x, b.y, b.bitmap.getWidth(), b.bitmap.getHeight())){
			this.lifes--;//�Լ���������1
			return true;
		}
		return false;
	};
	
	protected boolean checkHitThing(Plants ps){
		if(isContain(ps.x, ps.y, ps.plantsBitmap[0].getWidth(), ps.plantsBitmap[0].getHeight())){
			moveLength = 0;
			currentState = 3;
			return true;
		}
		return false;
	};
	

	/***
	 * �ı�xy����,�ı�ͼƬ
	 */
	protected void move(){
		switch (currentState) {
		case deadState:
			deadBitmapIndex = (deadBitmapIndex + 1) % deadBitmap.length;
			moveLength = 0;
			break;
		case hitState:
			hidedBitmapIndex = (hidedBitmapIndex + 1)
					% hidedBitmap.length;
			break;
		case eatState:
			eatBitmapIndex = (eatBitmapIndex + 1) % eatPlant.length;
			moveLength = 0;
			break;
		case normalState:
			nomalBitmapIndex = (nomalBitmapIndex + 1)
			% nomalBitmap.length;
			currentX = currentX - moveLength;
			break;
		}
	}

	/**
	 * ֲ�﷢�ڵ��ĵĿ���,����trueֲ����Ҫ���ڵ�������false����Ҫ���ڵ�
	 */
	protected boolean fireDerail() {
		if (currentX <= 480) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ������������Ƿ���ײ
	 * @param otherX ����
	 * @param otherY
	 * @param otherWidth ͼƬ���
	 * @param otherHeight
	 * @return
	 */
	public boolean isContain(float otherX, float otherY, int otherWidth, int otherHeight){//�ж����������Ƿ���ײ
		float xd = 0;//���x
		float yd = 0;//���y
		float xx = 0;//С��x
		float yx = 0;//С��y
		int width = 0;
		int height = 0;
		boolean xFlag = true;//��ҷɻ�x�Ƿ���ǰ
		boolean yFlag = true;//��ҷɻ�y�Ƿ���ǰ
		if(this.currentX >= otherX){
			xd = this.currentX;
			xx = otherX;
			xFlag = false;
		}else{
			xd = otherX;
			xx = this.currentX;
			xFlag = true;
		}
		if(this.currentY >= otherY){
			yd = this.currentY;
			yx = otherY;
			yFlag = false;
		}else{
			yd = otherY;
			yx = this.currentY;
			yFlag = true;
		}
		if(xFlag == true){
			width = this.nomalBitmap[0].getWidth();
		}else {
			width = otherWidth;
		}
		if(yFlag == true){
			height = this.nomalBitmap[0].getHeight();
		}else{
			height = otherHeight;
		}
		if(xd>=xx&&xd<=xx+width-1&&
				yd>=yx&&yd<=yx+height-1){//�����ж����������з��ص�
		    double Dwidth=width-xd+xx;   //�ص�������		
			double Dheight=height-yd+yx; //�ص�����߶�
			if(Dwidth*Dheight/(otherWidth*otherHeight)>=0.20){//�ص������20%���ж�Ϊ��ײ
				return true;
			}
		}
		return false;
	}	
}
