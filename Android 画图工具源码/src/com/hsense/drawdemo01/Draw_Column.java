package com.hsense.drawdemo01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.MotionEvent;

/*
 * 画圆柱
 * 
 * android自己没有画圆柱的方法，所以需要自己想个办法。
 * 思路：
 * 1、先画一个椭圆，
 * 2、根据第一个椭圆画第二个椭圆。这两个椭圆平行
 * 3、将两个椭圆的左边和右边连接起来。看起来就行圆柱了
 */
public class Draw_Column extends MyDraw {
	
	//声明椭圆1、2的4个点坐标
	private Point oval1Point1,oval1Point2,oval2Point1,oval2Point2;
	private RectF rectF1,rectF2;
	 
	public Draw_Column(Context context){
		  super(context);
		// TODO Auto-generated constructor stub
		  
		 //实例化
		 oval1Point1 = new Point();
		 oval1Point2 = new Point();
		 oval2Point1 = new Point();
		 oval2Point2 = new Point();
		 rectF1 = new RectF();
		 rectF2 = new RectF();
	}
	
	

	
	 //触摸事件
	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
		 
		 switch (event.getAction()) {
		 case MotionEvent.ACTION_DOWN:
			//获取起点坐标
			downPoint.set((int) event.getX(), (int) event.getY());
			break;
			
			
		 case MotionEvent.ACTION_MOVE:
			//获取当前拖动点坐标
			movePoint.set((int) event.getX(), (int) event.getY());
			
			/*
			 * 画椭圆与画矩形类似
			 * 先确定画矩形1需要的两个点坐标点oval1Point1、oval1Point2
			 */
			oval1Point1.set(downPoint.x, downPoint.y);
			oval1Point2.set(movePoint.x, movePoint.y);
			
			/*
			 * 椭圆1 左边和右边的的坐标点LeftPoint_oval1、RightPoint_oval1
			 */
			//纵坐标
			int y1 = oval1Point1.y+(oval1Point2.y-oval1Point1.y)/2;
			Point LeftPoint_oval1 = new Point(oval1Point1.x, y1);
			Point RightPoint_oval1 = new Point(oval1Point2.x, y1);
			
			//计算一个纵坐标需要改变的距离
			int distance = (int) Math.abs(Math.sqrt((oval1Point2.x-oval1Point1.x)*(oval1Point2.x-oval1Point1.x)
					+ (oval1Point2.y-oval1Point1.y)*(oval1Point2.y-oval1Point1.y)));
			
			
			/*
			 * 根据椭圆1画椭圆2
			 * 为了是椭圆1画椭圆2上下平行，横坐标不变，改变纵坐标
			 * 确定画椭圆2需要的两个点坐标oval2Point1、oval2Point2
			 */
			oval2Point1.set(oval1Point1.x, oval1Point1.y+distance);
			oval2Point2.set(oval1Point2.x, oval1Point2.y+distance);
			
			/*
			 * 椭圆2 左边和右边的的坐标点LeftPoint_oval1、RightPoint_oval1
			 */
			//纵坐标
			int y2 = oval2Point1.y+(oval2Point2.y-oval2Point1.y)/2;
			Point LeftPoint_oval2 = new Point(oval1Point1.x, y2);
			Point RightPoint_oval2 = new Point(oval1Point2.x, y2);
			
			rectF1.set(oval1Point1.x,oval1Point1.y,oval1Point2.x,oval1Point2.y);
			rectF2.set(oval2Point1.x,oval2Point1.y,oval2Point2.x,oval2Point2.y);
			
			bitmap.eraseColor(Color.TRANSPARENT);
			
			
			canvas.drawOval(rectF1, paint);//画椭圆1
			canvas.drawOval(rectF2, paint);//画椭圆2
			//画两条线，将椭圆1和椭圆2连接起来
			canvas.drawLine(LeftPoint_oval1.x, LeftPoint_oval1.y, LeftPoint_oval2.x, LeftPoint_oval2.y, paint);
			canvas.drawLine(RightPoint_oval1.x, RightPoint_oval1.y, RightPoint_oval2.x, RightPoint_oval2.y, paint);
			invalidate();
			break;
		
		 default:
			break;
		}

	  return true;
	 }
	 
}
