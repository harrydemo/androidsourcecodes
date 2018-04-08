package wyf.wpf;			//声明包语句

import static wyf.wpf.ConstantUtil.*;		//引入相关类
import android.graphics.Canvas;				//引入相关类

/*
 * 每一个MyDrawable类对象代表一个绘制对象，每个MyDrawable对象中包含一个图片
 * 该图片是等宽图片，，成员方法drawSelf接收传入的位置
 * （地图中的行、列数），并以此计算出左上角的坐标，将其绘制。
 */
public class MyDrawable{
	public int height;		//图片高度 
	int imgId;			//图片ID
	//构造器
	public MyDrawable(int imgId,int height){
		this.height = height;
		this.imgId = imgId;
	}
	//接收参数绘制自身
	public void drawSelf(Canvas canvas,int row,int col,int offsetX,int offsetY){
		int topLeftCornerX = col*TILE_SIZE;						//左上角x坐标就是左下角x坐标
		int topLeftCornerY = (row+1)*TILE_SIZE - height;		//左上角y坐标是左下角y坐标减去图片高度
		BitmapManager.drawCurrentStage(imgId, canvas, topLeftCornerX-offsetX, topLeftCornerY-offsetY);
	}
}