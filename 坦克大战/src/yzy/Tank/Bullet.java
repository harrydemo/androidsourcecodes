package yzy.Tank;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
  *子弹类
 */
public class Bullet {
	float x;//子弹的坐标
	float y;
	int dir;//子弹的方向,0静止，1上,2右上，3右，4右下，5下，6左下，7左，8左上
	int type;//子弹的类型
	int angle ;//子弹的角度   由炮筒的角度决定
	Bitmap bitmap;//当前子弹的图片
	GameView gameView;//gameView的引用
	private int moveSpan = 1;//移动的像素
	Matrix matrix;//子弹旋转
	public Bullet(float x, float y, int type, int dir,GameView gameView){
		matrix = new Matrix();
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.type = type;
		this.dir = dir;
		angle = gameView.tank.getgunX();////子弹的角度   由炮筒的角度决定
		this.initBitmap();
	}
	public void initBitmap(){
		if(type == 1){//当类型为1时  我方子弹
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.zidan1);
		}
		else if(type == 2){//类型为2时 敌方坦克子弹
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.zidan2);
		}
		else if(type == 3){//类型为3时 飞碟子弹
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.zidan3);
		}
		/*else if(type == 4){//类型为4时
		}
		else if(type == 5){//类型为5时
		}*/
	}
	public void draw(Canvas canvas){//绘制的方法
		if(this.type==1){
			matrix.setTranslate(x, y);//坐标
			matrix.postRotate(angle, x, y+6);//设置旋转角度 以及旋转中
			canvas.drawBitmap(bitmap, matrix, null);//炮筒
		}else{
			matrix.setTranslate(x, y);//坐标
			matrix.postRotate(0, x, y+6);//设置旋转角度 以及旋转中
			canvas.drawBitmap(bitmap, matrix, null);//炮筒
		}
		
//		canvas.drawBitmap(bitmap, x, y, null);
	}
	public void move(){//移动的方法
		if(dir == ConstantUtil.DIR_RIGHT){//向右移动
			this.x = this.x + moveSpan;
			this.y = this.y - (float)Math.tan(Math.toRadians(Math.abs(angle)));
			
		}
		else if(dir == ConstantUtil.DIR_LEFT){//向左移动
			this.x = this.x - moveSpan*4;
			this.y =393;
		}
		else if(dir == ConstantUtil.DIR_LEFT_DOWN){//向左下移动
			this.x = this.x - moveSpan*5;
			this.y = this.y + (float)Math.tan(Math.toRadians(Math.abs(36)))*2f;
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
	public boolean contain(Bullet b,GameView gameView){//判断地方子弹是否打中我放子弹
		if(isContain(b.x, b.y, b.bitmap.getWidth(), b.bitmap.getHeight())){
			return true;
		}
		return false;
	}
}
