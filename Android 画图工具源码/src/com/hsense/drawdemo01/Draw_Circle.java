package com.hsense.drawdemo01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

/*
 * 画圆
 * 
 * 无论拖动也好，拉伸也好，其实都是重新画圆，只是不改变某些属性的情况下，看起来就行是移动的
 */
public class Draw_Circle extends MyDraw {

	private Point rDotsPoint;// 圆点
	private int radius = 0;// 半径
	private Double dtance = 0.0;// 当前点到圆心的距离

	
	
	
	
	public Draw_Circle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		rDotsPoint = new Point();
	}

	
	
	
	
	// 触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		
		
		case MotionEvent.ACTION_DOWN:
			// 获取起点坐标
			downPoint.set((int) event.getX(), (int) event.getY());

			if (radius != 0) {
				//计算当前所点击的点到圆心的距离
				dtance = Math.sqrt((downPoint.x - rDotsPoint.x)
						* (downPoint.x - rDotsPoint.x)
						+ (downPoint.y - rDotsPoint.y)
						* (downPoint.y - rDotsPoint.y));
				// 如果距离半径减20和加20个范围内，则认为用户点击在圆上
				if (dtance >= radius - 20 && dtance <= radius + 20) {
					Log.i("点击-----", "在圆上");
					downState = 1;
				//如果距离小于半径，则认为用户点击在圆内
				} else if (dtance < radius) {
					Log.i("点击-----", "在圆内");
					downState = -1;
				// 在园外
				} else if (dtance > radius) {
					Log.i("ACTION_DOWN-----", "在圆外");
					downState = 0;
				}
			} else {
				downState = 0;
			}
			break;

			
			
		case MotionEvent.ACTION_MOVE:
			// 获取当前拖动点坐标
			movePoint.set((int) event.getX(), (int) event.getY());

			switch (downState) {
			case 1:
				//在圆上，圆心不变，重新计算半径
				radius = (int) Math.sqrt((movePoint.x - rDotsPoint.x)
						* (movePoint.x - rDotsPoint.x)
						+ (movePoint.y - rDotsPoint.y)
						* (movePoint.y - rDotsPoint.y));
				break;
				
			case -1:
				//在园内。半径不变，改变其圆心坐标
				rDotsPoint.x = movePoint.x;
				rDotsPoint.y = movePoint.y;
				break;

			default:
				//在园外。重新设置圆心坐标、半径。画圆
				rDotsPoint.set(downPoint.x, downPoint.y);
				radius = (int) Math.sqrt((movePoint.x - rDotsPoint.x)
						* (movePoint.x - rDotsPoint.x)
						+ (movePoint.y - rDotsPoint.y)
						* (movePoint.y - rDotsPoint.y));
				break;
				
			}
			
			/*
			 * 拖动过程中不停的将bitmap的颜色设置为透明
			 * 不然会整个拖动过程的轨迹都画出来
			 */
			bitmap.eraseColor(Color.TRANSPARENT);
			//根据圆心和半径重新画圆
			canvas.drawCircle(rDotsPoint.x, rDotsPoint.y, radius, paint);
			invalidate();
			
			
		}
		return true;
	}

}
