package com.hsense.drawdemo01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

/*
 * 立方体
 * 
 * 思路：
 * 1、先画一个矩形
 * 2、画一个和第一个矩形平行的矩形
 * 3、连接两个矩形之间的一些线段
 */
public class Draw_Cube extends MyDraw {

	// 声明立方体的8个顶点。前面矩形的4个顶点为1、2、3、4；后面矩形的4个顶点为5、6、7、8
	Point cubePoint1, cubePoint2, cubePoint3, cubePoint4, cubePoint5,
			cubePoint6, cubePoint7, cubePoint8;
	Rect rect1, rect2;

	public Draw_Cube(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		cubePoint1 = new Point();
		cubePoint2 = new Point();
		cubePoint3 = new Point();
		cubePoint4 = new Point();
		cubePoint5 = new Point();
		cubePoint6 = new Point();
		cubePoint7 = new Point();
		cubePoint8 = new Point();
		rect1 = new Rect();
		rect2 = new Rect();
	}

	
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 获取起点坐标
			downPoint.set((int) event.getX(), (int) event.getY());
			break;

		case MotionEvent.ACTION_MOVE:
			// 获取当前拖动点坐标
			movePoint.set((int) event.getX(), (int) event.getY());

			//第一个矩形的4个点
			cubePoint1.set(downPoint.x, downPoint.y);
			cubePoint2.set(movePoint.x, movePoint.y);
			cubePoint3.set(cubePoint1.x, cubePoint2.y);
			cubePoint4.set(cubePoint2.x, cubePoint1.y);

			// 算出矩形的边长的一半，作为矩形rect2相对矩形rect1的移动距离
			int distance = (int) Math.abs(Math
					.sqrt((cubePoint4.x - cubePoint1.x)
							* (cubePoint4.x - cubePoint1.x)
							+ (cubePoint4.y - cubePoint1.y)
							* (cubePoint4.y - cubePoint1.y))) / 2;
			
			// 根据前面矩形的4个顶点，给后面矩形的4个顶点赋值
			cubePoint5.set(cubePoint1.x + distance, cubePoint1.y - distance);
			cubePoint6.set(cubePoint2.x + distance, cubePoint2.y - distance);
			cubePoint7.set(cubePoint3.x + distance, cubePoint3.y - distance);
			cubePoint8.set(cubePoint4.x + distance, cubePoint4.y - distance);

			//矩形rect1、rect2
			rect1.set(cubePoint1.x, cubePoint1.y, cubePoint2.x, cubePoint2.y);
			rect2.set(cubePoint5.x, cubePoint5.y, cubePoint6.x, cubePoint6.y);
			
			bitmap.eraseColor(Color.TRANSPARENT);
			//画rect1、rect2
			canvas.drawRect(rect1, paint);
			canvas.drawRect(rect2, paint);
			
			//连接矩形rect1、rect2之间的一些顶点
			canvas.drawLine(cubePoint1.x, cubePoint1.y, cubePoint5.x,
					cubePoint5.y, paint);
			canvas.drawLine(cubePoint2.x, cubePoint2.y, cubePoint6.x,
					cubePoint6.y, paint);
			canvas.drawLine(cubePoint3.x, cubePoint3.y, cubePoint7.x,
					cubePoint7.y, paint);
			canvas.drawLine(cubePoint4.x, cubePoint4.y, cubePoint8.x,
					cubePoint8.y, paint);
			
			
			invalidate();
			break;

		default:
			break;
		}

		return true;
	}

}
