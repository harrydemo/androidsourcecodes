package com.hsense.drawdemo01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/*
 * 画线段
 * 
 * 伸长、缩短、移动其实都是重画直线
 */
public class Draw_Line extends MyDraw {
	
	
	private Point cenPoint;//声明线段的中间点，拖动直线需要用到次点
	private Point lPoint1,lPoint2;//声明线段的两个端点
	private Rect lPoint1Rect,lPoint2Rect;//以线段的两个端点为中心的两个矩形
	
	public Draw_Line(Context context){
		super(context);
		// TODO Auto-generated constructor stub
		  //实例化
		  cenPoint = new Point();
		  lPoint1 = new Point();
		  lPoint2 = new Point();
		  lPoint1Rect = new Rect();
		  lPoint2Rect = new Rect();
	}

	
	
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 * 根据用户所点击的点与线段的关系，来执行延长、缩短、移动、或重画
	 */
	 @Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			// 获取起点坐标
			downPoint.set((int) event.getX(), (int) event.getY());
			
			//如果用户所点的的点在 线段起点矩形范围内，则认为用户点击了起点
			if (lPoint1Rect.contains(downPoint.x, downPoint.y)) {
				downState = 1;
			//如果用户所点的的点在 线段终点矩形范围内，则认为用户点击了终点
			} else if (lPoint2Rect.contains(downPoint.x, downPoint.y)) {
				downState = 2;
			//判断用户所点击的点是否在直线上
			} else if (panduan()) {
				downState = 3;
			//在直线外
			} else {
				downState = 0;
			}
			break;

			
			
		case MotionEvent.ACTION_MOVE:
			// 获取当前拖动点坐标
			movePoint.set((int) event.getX(), (int) event.getY());
			
			switch (downState) {
			case 1:
				//如果拖动直线起点，则直线的终点不变
				lPoint1.set(movePoint.x, movePoint.y);
				moveState = 1;
				break;
			case 2:
				//如果拖动直线终点，则直线的起点不变
				lPoint2.set(movePoint.x, movePoint.y);
				break;
			case 3:
				//计算直线的中间点
				cenPoint.x = (lPoint2.x + lPoint1.x) / 2;
				cenPoint.y = (lPoint2.y + lPoint1.y) / 2;
				//移动距离
				int movedX = movePoint.x - cenPoint.x;
				int movedY = movePoint.y - cenPoint.y;
				//重新设定线段的两个端点
				lPoint1.x = lPoint1.x + movedX;
				lPoint1.y = lPoint1.y + movedY;
				lPoint2.x = lPoint2.x + movedX;
				lPoint2.y = lPoint2.y + movedY;
				break;
			default:
				lPoint1.set(downPoint.x, downPoint.y);
				lPoint2.set(movePoint.x, movePoint.y);
				break;
			}
			
			/*
			 * 拖动过程中不停的将bitmap的颜色设置为透明
			 * 不然会整个拖动过程的轨迹都画出来
			 */
			bitmap.eraseColor(Color.TRANSPARENT);
			//根据线段的两个端点画直线
			canvas.drawLine(lPoint1.x, lPoint1.y, lPoint2.x, lPoint2.y,paint);
			invalidate();
			break;

			
			
			
		case MotionEvent.ACTION_UP:
			//重新设定以线段两个端点为中心的两个矩形
			lPoint1Rect.set(lPoint1.x - 25, lPoint1.y - 25, lPoint1.x + 25,
					lPoint1.y + 25);
			lPoint2Rect.set(lPoint2.x - 25, lPoint2.y - 25, lPoint2.x + 25,
					lPoint2.y + 25);
			Log.i("ACTION_UP", "ACTION_UP``````````````break");
			break;

		default:
			break;
		}
		return true;
	}
	 
	 
	 
	 /*
	  * 判断当前所点击的点是否在直线上
	  * 
	  * 根据用户所点击的点到线段两个端点的距离之和
	  * 与线段的距离进行比较 来判断
	  */
	 public boolean panduan() {
		 
		 double lDis = Math.sqrt((lPoint1.x-lPoint2.x)*(lPoint1.x-lPoint2.x)
				 + (lPoint1.y-lPoint2.y)*(lPoint1.y-lPoint2.y));
		 
		 double lDis1 = Math.sqrt((downPoint.x-lPoint1.x)*(downPoint.x-lPoint1.x)
				 + (downPoint.y-lPoint1.y)*(downPoint.y-lPoint1.y));
		 double lDis2 = Math.sqrt((downPoint.x-lPoint2.x)*(downPoint.x-lPoint2.x)
				 + (downPoint.y-lPoint2.y)*(downPoint.y-lPoint2.y));
		 
		 
		 if (lDis1+lDis2 >= lDis+0.00f && lDis1+lDis2 <= lDis+5.00f) {
			return true;
		}else {
			return false;
		}
	}
	 
}
