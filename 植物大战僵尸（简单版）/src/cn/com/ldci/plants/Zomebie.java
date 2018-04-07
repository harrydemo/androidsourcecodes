package cn.com.ldci.plants;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/***
 * 
 * @author t4 僵尸类
 */
public abstract class Zomebie {

	/**
	 * 自身线程的开关
	 */
	protected boolean avlive = true;
	/**
	 * 当前的x坐标
	 */
	protected float currentX = 480;
	/**
	 * 当前的y坐标
	 */
	protected float currentY;
	/**
	 *死亡图片的index
	 */
	protected int deadBitmapIndex;
	/**
	 * 死亡图片的数组
	 */
	protected Bitmap[] deadBitmap;
	/**
	 * 被攻击时的图片的index
	 */
	protected int hidedBitmapIndex;
	/**
	 * 正常时的图片的index
	 */
	protected int nomalBitmapIndex;
	/**
	 * 吃植物时的图片的index
	 */
	protected int eatBitmapIndex;
	/**
	 * 被攻击时的图片数组
	 */
	protected Bitmap[] hidedBitmap;
	/**
	 * 吃植物时的图片数组
	 */
	protected Bitmap[] eatPlant;
	/**
	 * 正常时的图片数组
	 */
	protected Bitmap[] nomalBitmap;
	/**
	 * 当前状态
	 */
	protected byte currentState = 0;
	/**
	 * 正常状态
	 */
	public static final byte normalState = 0;
	/**
	 * 死亡状态
	 */
	public static final byte deadState = 2;
	/**
	 * 吃植物的状态
	 */
	public static final byte eatState = 3;
	/**
	 * 被攻击状态
	 */
	public static final byte hitState = 1;
	/**
	 * 吃一次植物让植物减少的生命值
	 */
	protected int eatlifes;
	/**
	 * 僵尸当前的生命值
	 */
	protected int lifes=10;
	/**
	 * 移动的长度
	 */
	protected int moveLength;
	Paint paint = new Paint();
	Matrix martix = new Matrix();
	/***
	 * 这个方法不能确定要不要
	 */
	protected abstract void eat();

	/***
	 * 画自己
	 * 
	 * @param canvas
	 *            画布
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
	 * 被子弹击中做的事情，比如减生命值等
	 */
	protected abstract void hit();

	/***
	 * 与子弹的碰撞检测碰撞检测
	 * 
	 * @return 发生碰撞返回true ,没发生返回false
	 */
	protected boolean checkHitThing(Bullets b){
		if(isContain(b.x, b.y, b.bitmap.getWidth(), b.bitmap.getHeight())){
			this.lifes--;//自己的生命减1
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
	 * 改变xy坐标,改变图片
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
	 * 植物发炮弹的的开关,返回true植物需要发炮弹，返回false不需要发炮弹
	 */
	protected boolean fireDerail() {
		if (currentX <= 480) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 检测两个矩形是否相撞
	 * @param otherX 坐标
	 * @param otherY
	 * @param otherWidth 图片宽高
	 * @param otherHeight
	 * @return
	 */
	public boolean isContain(float otherX, float otherY, int otherWidth, int otherHeight){//判断两个矩形是否碰撞
		float xd = 0;//大的x
		float yd = 0;//大大y
		float xx = 0;//小的x
		float yx = 0;//小的y
		int width = 0;
		int height = 0;
		boolean xFlag = true;//玩家飞机x是否在前
		boolean yFlag = true;//玩家飞机y是否在前
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
