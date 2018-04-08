package yzy.Tank;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 敌军坦克
 */
public class EnemyTank {
	float x = ConstantUtil.screenWidth;//出时的X轴坐标 
	float y ;//Y轴坐标 
	boolean status;//状态
	long touchPoint;//触发点
	int type;//飞机的类型
	int life;//生命
	float spanX = 10;//移动的X像素
	float spanY = 5;//移动的Y像素
	Bitmap bitmap;
	int start;//当前出发点
	int target;//当前目标点
	int step;//当前处于当前路径片段中第几步
	int[][] path; //飞机的路径

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
		if(step==path[2][start]){//一段路径走完,到下一段路径开始
			step=0;
			start=(start+1)%(path[0].length);
			target=(target+1)%(path[0].length);
			this.x=path[0][start];
			this.y=path[1][start]; 
		}else{//一段路径没有走完，继续走
			float xSpan=(float)(path[0][target]-path[0][start])/(float)path[2][start];
			float ySpan=(float)(path[1][target]-path[1][start])/(float)path[2][start];
			this.x=this.x+xSpan;
			this.y=this.y+ySpan;
			step++;
		}
	}
	public void fire(GameView gameView){//打子弹的方法
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
				gameView.playSound(4,0);//导弹音效
			}
			/*else{
				System.out.println(this.type);
				gameView.playSound(2,0);//敌军炮弹音效
				Bullet b = new Bullet(x, y, 2, ConstantUtil.DIR_LEFT,gameView);
				gameView.badBollets.add(b);
			}*/
		}
	}
	private boolean isContain(float otherX, float otherY, int otherWidth, int otherHeight){//判断两个矩形是否碰撞
		float xd = 0;//大的x
		float yd = 0;//大大y
		float xx = 0;//小的x
		float yx = 0;//小的y
		int width = 0;
		int height = 0;
		boolean xFlag = true;//玩家飞机x是否在前
		boolean yFlag = true;//玩家飞机y是否在前
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
				yd>=yx&&yd<=yx+height-1){//首先判断两个矩形有否重叠
		    double Dwidth=width-xd+xx;   //重叠区域宽度		
			double Dheight=height-yd+yx; //重叠区域高度
			if(Dwidth*Dheight/(otherWidth*otherHeight)>=0.35){//重叠面积超35%则判定为碰撞
				return true;
			}
		}
		return false;
	}
	
	public boolean contain(Bullet b,GameView gameView){//判断子弹是否打中敌机
		if(isContain(b.x, b.y, b.bitmap.getWidth(), b.bitmap.getHeight())){
			this.life--;//自己的生命减1
			if (this.life <= 0) {// 当生命小于0时
				if (gameView.activity.isSound) {
					gameView.playSound(3, 0);
				}
				this.status = false;//使自己不可见
				/*if(this.type == 3){//是关口时
					gameView.status = 3;//状态换成胜利状态
					if(gameView.mMediaPlayer.isPlaying()){
						gameView.mMediaPlayer.stop();//将游戏背景音乐停止
					}
					Message msg1 = gameView.activity.myHandler.obtainMessage(5);
					gameView.activity.myHandler.sendMessage(msg1);//向主activity发送Handler消息
				}*/
				if(this.type == 5){//关口BOSS
					gameView.activity.myHandler.sendEmptyMessage(5);//向主activity发送Handler消息
					gameView.activity.mediaPlayer.stop();//停止背景音乐
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
