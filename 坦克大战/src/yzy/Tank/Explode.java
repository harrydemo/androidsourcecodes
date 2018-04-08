package yzy.Tank;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * 
 * 该类为爆炸类
 * 在制定位置绘制爆炸
 * 其他线程通过调用nextFrame方法换帧
 *
 */

public class Explode {
	float x;//爆炸的x坐标
	float y;//爆炸的y坐标 
	Bitmap[] bitmaps;//所有爆炸的帧数组
	int k = 0;//当前帧
	
	Bitmap bitmap;//当前帧
	Paint paint;//画笔
	GameView gameView;
	public Explode(float x2, float y2, GameView gameView,int type){
		this.x = x2;
		this.y = y2;
		if(type==1)//坦克爆炸效果
			this.bitmaps = gameView.explodes;
		if(type==2)//导弹爆炸效果
			this.bitmaps = gameView.explodes2;
		paint = new Paint();//初始化画笔
		this.gameView = gameView;
	}

	public void draw(Canvas canvas) {// 绘制方法
		canvas.drawBitmap(bitmap, x, y, paint);
	}
	public boolean nextFrame(){//换帧，成功返回true。否则返回false
		if(k < bitmaps.length){
			bitmap = bitmaps[k];
			k++;
			return true;
		}
		return false;
	}
}