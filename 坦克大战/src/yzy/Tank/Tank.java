package yzy.Tank;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;


public class Tank {
	private Bitmap shadow;//阴影
	private Bitmap chassis;//地盘
	private Bitmap wheel;//轮子
	private Bitmap wheel2;//轮子
	private Bitmap tkbody;//主体
	private Bitmap gun;//炮筒
	private Bitmap xuetiaobj;//炮筒
	private Bitmap xuetiao;//血条
	private Bitmap xuetiaotk;//血条处坦克
	Rect src;//用来截取血条的
	Rect dst;//用来截取血条
	Paint paint;//画笔用来写坦克名称
	private int x;//坦克初始坐标
	private int y;//坦克初始坐标
	private int k;//旋转 初始角度
	private int gunX;
	private int gunXSpan=2;//转圈角度
	private int span = 5;//飞机走一步的像素
	GameView gameView;//GameView的引用
	int life;//生命
	Matrix matrix1;
	Matrix matrix2;
	Matrix matrix3;
	Matrix matrix4;
	Matrix matrix5;
	Matrix matrix6;
	Matrix matrix7;
	public Tank(int x,int y,int life,GameView gameView){
		this.gameView = gameView;
		this.x = x;
		this.y = y; 
		this.life = life;
		k = 0;
		gunX=0;
		shadow = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.shadow);
		chassis = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.chassis);
		wheel = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.wheel);
		wheel2 = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.wheel2);
		tkbody = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.tkbody);
		gun = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.gun);
		xuetiaobj = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.xuetiaobj);
		xuetiao = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.xuetiao);
		xuetiaotk = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.xuetiaotk);
		paint = new Paint();
		matrix1 = new Matrix();
		matrix2 = new Matrix();
		matrix3 = new Matrix();
		matrix4 = new Matrix();
		matrix5 = new Matrix();
		matrix6 = new Matrix();
		matrix7 = new Matrix();
	}
	public void drawView(Canvas canvas){
		canvas.drawBitmap(xuetiaobj, 5,5, null);
		
		paint.setColor(Color.WHITE);
		paint.setTextSize(12);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
		canvas.drawText("挑战者M2", 102, 38, paint);
		src = new Rect(0, 0, 17*life, 16);
		dst = new Rect(90, 49,90+17*life, 65);
		canvas.drawBitmap(xuetiao, src,dst, null);
		canvas.drawBitmap(xuetiaotk, 10,15, null);
		
		canvas.drawBitmap(shadow, x + 7, y + 44, null);
		canvas.drawBitmap(chassis, x + 3, y + 28, null);
		
		matrix1.setTranslate(x + 9, y + 30);
		matrix1.postRotate(k,x + 9 + 8, y + 30 + 8);
		canvas.drawBitmap(wheel, matrix1, null);
		
		matrix2.setTranslate(x + 27, y + 36);
		matrix2.postRotate(k, x + 27 + 6, y + 36 + 6);
		canvas.drawBitmap(wheel2, matrix2, null);
		
		matrix3.setTranslate(x + 40, y + 36);
		matrix3.postRotate(k, x + 40 + 6, y + 36 + 6);
		canvas.drawBitmap(wheel2, matrix3, null);
		
		matrix4.setTranslate( x + 54, y + 36);
		matrix4.postRotate(k, x + 54 + 6, y + 36 + 6);
		canvas.drawBitmap(wheel2, matrix4, null);
		
		matrix5.setTranslate(x + 69, y + 36);
		matrix5.postRotate(k, x + 69 + 6, y + 36 + 6);
		canvas.drawBitmap(wheel2, matrix5, null);
		
		matrix6.setTranslate(x + 82, y + 31);
		matrix6.postRotate(k, x + 82 + 8, y + 31 + 8);
		canvas.drawBitmap(wheel, matrix6, null);
		
		k=k+45>360? 0 :k+45;
		
		canvas.drawBitmap(tkbody, x, y, null);
		
		
		matrix7.setTranslate(x + 69, y + 5);//坐标
		matrix7.postRotate((float) (gunX+1.1), x + 69, y + 5+6);//设置旋转中心
		canvas.drawBitmap(gun, matrix7, null);//炮筒
	}
	public int getX(){
		return x;
	}
	public void setX(int x){
		this.x=x;
	}
	public int getY(){
		return y;
	}
	public void setY(int y){
		this.y=y;
	}
	public int getSpan(){
		return span;
	}
	public void setgunX(int gunX){
		this.gunX=gunX;
	}
	public int getgunX(){
		return gunX;
	}
	public int getgunXSpan(){
		return gunXSpan;
	}
	public void playSound(int sound, int loop){
		gameView.playSound(sound, loop);
	}
	public void fire(){//添加子弹的方法
		Bullet b = new Bullet(x + 69+2, y + 5+1, 1, ConstantUtil.DIR_RIGHT,gameView);
		gameView.goodBollets.add(b);
		
		gameView.playSound(2,0);//播放子弹音效
	}
	public boolean contain(EnemyTank ep){
		if(isContain(ep.x, ep.y, ep.bitmap.getWidth(), ep.bitmap.getHeight())){//检测成功
			this.life--;//自己的生命减1
			if(this.life<0){//当生命小于0时
				/*Explode e = new Explode(ep.x, ep.y, gameView,1);//创建爆炸对象
				gameView.explodeList.add(e);//添加爆炸对象
*/				gameView.status = 2;
				if(gameView.activity.isSound){
					gameView.playSound(3,0);
				}
				gameView.activity.myHandler.sendEmptyMessage(3);//向主activity发送Handler消息
				gameView.activity.mediaPlayer.stop();
			}
			return true;
		}
		return false;
	}
	public boolean contain(Bullet b){
		if(isContain(b.x, b.y, b.bitmap.getWidth(), b.bitmap.getHeight())){//检测成功
			this.life--;//自己的生命减1
			if(this.life<0){//当生命小于0时
//				Explode e = new Explode(b.x, b.y, gameView,1);//创建爆炸对象
//				gameView.explodeList.add(e);//添加爆炸对象
				gameView.status = 2;
				if(gameView.activity.isSound){
					gameView.playSound(3,0);
				}
				gameView.activity.myHandler.sendEmptyMessage(3);//向主activity发送Handler消息
				gameView.activity.mediaPlayer.stop();
			}
			return true;
		}
		return false;
	}
	public boolean contain(Life l){//检测玩家飞机是否撞血块
		if(isContain(l.x, l.y, l.bitmap.getWidth(), l.bitmap.getHeight())){//检测成功
			if(this.life<ConstantUtil.life){
				this.life++;//生命加一
			}
			return true;
		}
		return false;
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
			width = 112;//坦克宽//width = this.bitmap1.getWidth();
		}else {
			width = otherWidth;
		}
		if(yFlag == true){
			height = 54;//坦克高//width = this.bitmap1.getHeight();
		}else{
			height = otherHeight;
		}
		if(xd>=xx&&xd<=xx+width-1&&
				yd>=yx&&yd<=yx+height-1){//首先判断两个矩形有否重叠
		    double Dwidth=width-xd+xx;   //重叠区域宽度		
			double Dheight=height-yd+yx; //重叠区域高度
			if(Dwidth*Dheight/(otherWidth*otherHeight)>=0.20){//重叠面积超20%则判定为碰撞
				return true;
			}
		}
		return false;
	}	
}
