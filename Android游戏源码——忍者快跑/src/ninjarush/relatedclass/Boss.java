package ninjarush.relatedclass;

import java.util.Vector;

import ninjarush.mainsurfaceview.NinjaRushSurfaceView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Boss {
	//BOSSѪ��
	public int hp = 20;
	//Boss��ͼƬ��Դ
		private Vector<Bitmap> boss;
		//boss�˶����ٶ�
		private int speed = 10;
		//boss ������
		public int x,y;
		//bossÿ֡�Ŀ��
		public int frameW , frameH;
		//boss��ǰ֡���±�
		private int frameIndex;
		//�ж�BOSS�Ƿ�����
		private boolean isDead;
		//գ��ʱ����
		private int eyesTime=10;
		//������
		private int bosscount;
		//boss�Ĺ��캯��
		public Boss(Vector<Bitmap> boss) {
			//boss������
			isDead=false;
			this.boss=boss;
			frameW=boss.elementAt(0).getWidth();
			frameH=boss.elementAt(0).getHeight();
			x = NinjaRushSurfaceView.screenW -frameW;
			y = NinjaRushSurfaceView.screenH /2- frameH/2;
		}
		//Boss�Ļ���
		public void draw (Canvas canvas  , Paint paint){
			if(hp>=10)
				canvas.drawBitmap(boss.get(frameIndex), x,y, paint);
			else
				canvas.drawBitmap(boss.elementAt(frameIndex), x, y, paint);
			
		}
		public void logic(){
			//����ѭ���γɶ���
			bosscount++;
			
			if (bosscount%eyesTime==0) {
				frameIndex++;
			}
			if(hp>=10){
				if (frameIndex==3) {
					frameIndex = 0;
				}
			}else{
				if (frameIndex==7) {
					frameIndex = 4;
				}
			}
				
			y-=speed;
			if (y<=0) {
				speed = -10;
			}else if (y>=NinjaRushSurfaceView.screenH*3/4) {
				speed= 10;
			}
		  }
		//Boss��Ѫ������
		
		
		public int getHp() {
			return hp;
		}
		public void setHp(int hp) {
			this.hp = hp;
		}
		public boolean isDead() {
			return isDead;
		}
		public void setDead(boolean isDead) {
			this.isDead = isDead;
		}
		
		public int[] getPosition(){
			int[] p={x,y,frameW,frameH};
			return p;
		}
		
}
