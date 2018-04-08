package yzy.Tank;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
  *�ӵ���
 */
public class Bullet {
	float x;//�ӵ�������
	float y;
	int dir;//�ӵ��ķ���,0��ֹ��1��,2���ϣ�3�ң�4���£�5�£�6���£�7��8����
	int type;//�ӵ�������
	int angle ;//�ӵ��ĽǶ�   ����Ͳ�ĽǶȾ���
	Bitmap bitmap;//��ǰ�ӵ���ͼƬ
	GameView gameView;//gameView������
	private int moveSpan = 1;//�ƶ�������
	Matrix matrix;//�ӵ���ת
	public Bullet(float x, float y, int type, int dir,GameView gameView){
		matrix = new Matrix();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.type = type;
		this.dir = dir;
		angle = gameView.tank.getgunX();////�ӵ��ĽǶ�   ����Ͳ�ĽǶȾ���
		this.initBitmap();
	}
	public void initBitmap(){
		if(type == 1){//������Ϊ1ʱ  �ҷ��ӵ�
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.zidan1);
		}
		else if(type == 2){//����Ϊ2ʱ �з�̹���ӵ�
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.zidan2);
		}
		else if(type == 3){//����Ϊ3ʱ �ɵ��ӵ�
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.zidan3);
		}
		/*else if(type == 4){//����Ϊ4ʱ
		}
		else if(type == 5){//����Ϊ5ʱ
		}*/
	}
	public void draw(Canvas canvas){//���Ƶķ���
		if(this.type==1){
			matrix.setTranslate(x, y);//����
			matrix.postRotate(angle, x, y+6);//������ת�Ƕ� �Լ���ת��
			canvas.drawBitmap(bitmap, matrix, null);//��Ͳ
		}else{
			matrix.setTranslate(x, y);//����
			matrix.postRotate(0, x, y+6);//������ת�Ƕ� �Լ���ת��
			canvas.drawBitmap(bitmap, matrix, null);//��Ͳ
		}
		
//		canvas.drawBitmap(bitmap, x, y, null);
	}
	public void move(){//�ƶ��ķ���
		if(dir == ConstantUtil.DIR_RIGHT){//�����ƶ�
			this.x = this.x + moveSpan;
			this.y = this.y - (float)Math.tan(Math.toRadians(Math.abs(angle)));
			
		}
		else if(dir == ConstantUtil.DIR_LEFT){//�����ƶ�
			this.x = this.x - moveSpan*4;
			this.y =393;
		}
		else if(dir == ConstantUtil.DIR_LEFT_DOWN){//�������ƶ�
			this.x = this.x - moveSpan*5;
			this.y = this.y + (float)Math.tan(Math.toRadians(Math.abs(36)))*2f;
		}
	}
	private boolean isContain(float otherX, float otherY, int otherWidth, int otherHeight){//�ж����������Ƿ���ײ
		float xd = 0;//���x
		float yd = 0;//���y
		float xx = 0;//С��x
		float yx = 0;//С��y
		int width = 0;
		int height = 0;
		boolean xFlag = true;//��ҷɻ�x�Ƿ���ǰ
		boolean yFlag = true;//��ҷɻ�y�Ƿ���ǰ
		if(this.x >= otherX){
			xd = this.x;
			xx = otherX;
			xFlag = false;
		}else{
			xd = otherX;
			xx = this.x;
			xFlag = true;
		}
		if(this.y >= otherY){
			yd = this.y;
			yx = otherY;
			yFlag = false;
		}else{
			yd = otherY;
			yx = this.y;
			yFlag = true;
		}
		if(xFlag == true){
			width = this.bitmap.getWidth();
		}else{
			width = otherWidth;
		}
		if(yFlag == true){
			height = this.bitmap.getHeight();
		}else{
			height = otherHeight;
		}
		if(xd>=xx&&xd<=xx+width-1&&
				yd>=yx&&yd<=yx+height-1){//�����ж����������з��ص�
		    double Dwidth=width-xd+xx;   //�ص�������		
			double Dheight=height-yd+yx; //�ص�����߶�
			if(Dwidth*Dheight/(otherWidth*otherHeight)>=0.35){//�ص������35%���ж�Ϊ��ײ
				return true;
			}
		}
		return false;
	}
	public boolean contain(Bullet b,GameView gameView){//�жϵط��ӵ��Ƿ�����ҷ��ӵ�
		if(isContain(b.x, b.y, b.bitmap.getWidth(), b.bitmap.getHeight())){
			return true;
		}
		return false;
	}
}
