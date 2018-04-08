package yzy.Tank;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * �о�̹��
 */
public class EnemyTank {
	float x = ConstantUtil.screenWidth;//��ʱ��X������ 
	float y ;//Y������ 
	boolean status;//״̬
	long touchPoint;//������
	int type;//�ɻ�������
	int life;//����
	float spanX = 10;//�ƶ���X����
	float spanY = 5;//�ƶ���Y����
	Bitmap bitmap;
	int start;//��ǰ������
	int target;//��ǰĿ���
	int step;//��ǰ���ڵ�ǰ·��Ƭ���еڼ���
	int[][] path; //�ɻ���·��

	public EnemyTank(int start,int target,int step,int[][] path, boolean status,long touchPoint, int type, int life){
		this.start=start;
		this.target=target;
		this.step=step;
		this.path=path;
		this.status = status;
		this.touchPoint = touchPoint;
		this.type = type;
		this.life = life;
		this.x=path[0][start];
		this.y=path[1][start];
	}
	public void draw(Canvas canvas){
			canvas.drawBitmap(bitmap, x, y, new Paint());
	}
	public void move(){
		if(step==path[2][start]){//һ��·������,����һ��·����ʼ
			step=0;
			start=(start+1)%(path[0].length);
			target=(target+1)%(path[0].length);
			this.x=path[0][start];
			this.y=path[1][start]; 
		}else{//һ��·��û�����꣬������
			float xSpan=(float)(path[0][target]-path[0][start])/(float)path[2][start];
			float ySpan=(float)(path[1][target]-path[1][start])/(float)path[2][start];
			this.x=this.x+xSpan;
			this.y=this.y+ySpan;
			step++;
		}
	}
	public void fire(GameView gameView){//���ӵ��ķ���
/*		if(type == 3&&type == 2&&type == 1 && Math.random()<ConstantUtil.BooletSpan2){
			Bullet b1 = new Bullet(x, y, 2, ConstantUtil.DIR_LEFT,gameView);
//			Bullet b2 = new Bullet(x, y, 3, ConstantUtil.DIR_LEFT_DOWN,gameView);
//			Bullet b3 = new Bullet(x, y, 2, ConstantUtil.DIR_LEFT_UP,gameView);
			gameView.badBollets.add(b1);
//			gameView.badBollets.add(b2);
//			gameView.badBollets.add(b3);}*/
		 if(Math.random()<ConstantUtil.BooletSpan){
			if(this.type == 4){
				Bullet b = new Bullet(x, y, 3, ConstantUtil.DIR_LEFT_DOWN,gameView);
				gameView.badBollets.add(b);
				gameView.playSound(4,0);//������Ч
			}
			/*else{
				System.out.println(this.type);
				gameView.playSound(2,0);//�о��ڵ���Ч
				Bullet b = new Bullet(x, y, 2, ConstantUtil.DIR_LEFT,gameView);
				gameView.badBollets.add(b);
			}*/
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
	
	public boolean contain(Bullet b,GameView gameView){//�ж��ӵ��Ƿ���ел�
		if(isContain(b.x, b.y, b.bitmap.getWidth(), b.bitmap.getHeight())){
			this.life--;//�Լ���������1
			if (this.life <= 0) {// ������С��0ʱ
				if (gameView.activity.isSound) {
					gameView.playSound(3, 0);
				}
				this.status = false;//ʹ�Լ����ɼ�
				/*if(this.type == 3){//�ǹؿ�ʱ
					gameView.status = 3;//״̬����ʤ��״̬
					if(gameView.mMediaPlayer.isPlaying()){
						gameView.mMediaPlayer.stop();//����Ϸ��������ֹͣ
					}
					Message msg1 = gameView.activity.myHandler.obtainMessage(5);
					gameView.activity.myHandler.sendMessage(msg1);//����activity����Handler��Ϣ
				}*/
				if(this.type == 5){//�ؿ�BOSS
					gameView.activity.myHandler.sendEmptyMessage(5);//����activity����Handler��Ϣ
					gameView.activity.mediaPlayer.stop();//ֹͣ��������
				}
			}
			return true;
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
