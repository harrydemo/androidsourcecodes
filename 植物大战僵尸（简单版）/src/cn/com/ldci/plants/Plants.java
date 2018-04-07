package cn.com.ldci.plants;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Plants {
	float type;
	float x,y;
	Bitmap[] plantsBitmap;
	int life = 10;
	int i = 0;
	int count=0;
	public Plants(int type,float x,float y){
		this.type = type;
		this.x = x;
		this.y = y;
	}
	public void draw(Canvas canvas){
		Paint paint = new Paint();
		canvas.drawBitmap(plantsBitmap[i], x, y, paint);
		i = (i+1)%8;
	}
	public void fire(GameView gameView){
		if(type==1){
			Bullets b1 = new Bullets(x+15, y+5, 1,gameView);
			if(count==0){
				gameView.goodBollets1.add(b1);
			}
			if(count==1){
				gameView.goodBollets2.add(b1);
			}
			if(count==2){
				gameView.goodBollets3.add(b1);
			}
			count=(count+1)%3;
		}
	}
	public boolean contain(Zomebie zb){
		if(isContain(zb.currentX, zb.currentY, zb.nomalBitmap[0].getWidth(), zb.nomalBitmap[0].getHeight())){//���ɹ�
			this.life--;//�Լ���������1
			return true;
		}
		return false;
	}
	
	public boolean isContain(float otherX, float otherY, int otherWidth, int otherHeight){//�ж����������Ƿ���ײ
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
			width = this.plantsBitmap[0].getWidth();
		}else {
			width = otherWidth;
		}
		if(yFlag == true){
			height = this.plantsBitmap[0].getHeight();
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
	public float getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

}
