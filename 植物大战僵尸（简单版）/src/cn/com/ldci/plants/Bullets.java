package cn.com.ldci.plants;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
/**
 * 
 * @author coffee
 * 该类为子弹的封装类
 * 记录了子弹自身的相关参数
 * 外界通过调用move方法移动子弹
 *
 */
public class Bullets {
	/**
	 * 子弹的坐标
	 */
	float x;
	float y;
	/**
	 * 子弹的类型
	 */
	int type;
	/**
	 * 当前子弹的图片
	 */
	Bitmap bitmap;
	/**
	 * gameView的引用
	 */
	GameView gameView;
	/**
	 * 移动的像素
	 */
	private int moveSpan = 5;
	/**
	 * 子弹类的构造函数
	 * @param x  子弹坐标
	 * @param y
	 * @param type 子弹类型
	 * @param gameView gameView引用
	 */
	public Bullets(float x, float y, int type,GameView gameView){
		this.gameView = gameView;
		this.x = x;
		this.y = y;
		this.type = type;
		this.initBitmap();
	}
	
	/**
	 * 图片缩放
	 * @param bgimage 原图
	 * @param newWidth 新的宽
	 * @param newHeight 新的高
	 * @return
	 */
	public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
		// 获取这个图片的宽和高
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
		matrix, true);
		return bitmap;

		}
	/**
	 * 初始化图片
	 */
	private void initBitmap() {
		//当类型为1时
		if(type == 1){
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bullet_01);
			bitmap = zoomImage(bitmap,14,14);
		}
		//类型为2时
		else if(type == 2){
			bitmap = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.bullet_02);
		}
		
	}
	/**
	 * 子弹绘制方法
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x, y,new Paint());	
	}
	/**
	 * 子弹移动方法
	 */
	public void move(){
		this.x = this.x + moveSpan;
	}

}
