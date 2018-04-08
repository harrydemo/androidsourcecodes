package com.hsense.drawdemo01;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/*
 * 画三角形
 * 
 * 方法：用户点击3个点来确定三角形的3个顶点，画矩形
 */
public class Draw_triangle extends MyDraw{
	
	 private Path path;
	 private Point point1,point2,point3;
	 private Rect point1Rect,point2Rect,point3Rect;
	

	public Draw_triangle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		//实例化
		point1 = new Point();
		point2 = new Point();
		point3 = new Point();

		point1Rect = null;
		point2Rect = null;
		point3Rect = null;

		Toast.makeText(getContext(), "请开始点击，确定3个点！", Toast.LENGTH_SHORT).show();
	}

	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			downPoint.set((int)event.getX(), (int)event.getY());
			
			/*
			 * 判断point3为中心的矩形point3Rect是否为null。
			 * 如果=null，则用户还没有开始画三角形；如果！=null，则用户已经画好了三角形
			 * 
			 * 判断用户点击位置
			 */
			if (point3Rect != null) {
				if (point1Rect.contains(downPoint.x, downPoint.y)) {
					downState = 1;
				}else if (point2Rect.contains(downPoint.x, downPoint.y)) {
					downState = 2;
				}else if (point3Rect.contains(downPoint.x, downPoint.y)) {
					downState = 3;
				}else if (panduan(point1, point2, point3, downPoint)) {
					downState = 4;
				}else {
					downState = 0;
				}
			}else {
				if (point1Rect == null) {
					point1.set(downPoint.x, downPoint.y);
					point1Rect = new Rect(point1.x-20, point1.y-20, point1.x+20, point1.y+20);
					canvas.drawPoint(point1.x, point1.y, paint);
					invalidate();
				}else if (point2Rect == null) {
					point2.set(downPoint.x, downPoint.y);
					point2Rect = new Rect(point2.x-20, point2.y-20, point2.x+20, point2.y+20);
					canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
					invalidate();
				}else if (point3Rect == null) {
					point3.set(downPoint.x, downPoint.y);
					point3Rect = new Rect(point3.x-20, point3.y-20, point3.x+20, point3.y+20);
					canvas.drawPoint(point3.x, point3.y, paint);
					
					path = new Path();
					path.moveTo(point1.x, point1.y);
					path.lineTo(point2.x, point2.y);
					path.lineTo(point3.x, point3.y);
					path.close();
					canvas.drawPath(path, paint);
					invalidate();
				}
			}
			
			break;
			
		
			
			
		case MotionEvent.ACTION_MOVE:
			movePoint.set((int)event.getX(), (int)event.getY());
			
			switch (downState) {
			case 1:
				//如果用户拖动顶点point1，则point2、point3不变
				path = new Path();
				path.moveTo(point2.x, point2.y);
				path.lineTo(point3.x, point3.y);
				path.lineTo(movePoint.x, movePoint.y);
				path.close();
				bitmap.eraseColor(Color.TRANSPARENT);
				canvas.drawPath(path, paint);
				invalidate();
				moveState = 1;
				break;
			case 2:
				path = new Path();
				path.moveTo(point1.x, point1.y);
				path.lineTo(point3.x, point3.y);
				path.lineTo(movePoint.x, movePoint.y);
				path.close();
				bitmap.eraseColor(Color.TRANSPARENT);
				canvas.drawPath(path, paint);
				invalidate();
				moveState = 2;
				break;
			case 3:
				path = new Path();
				path.moveTo(point1.x, point1.y);
				path.lineTo(point2.x, point2.y);
				path.lineTo(movePoint.x, movePoint.y);
				path.close();
				bitmap.eraseColor(Color.TRANSPARENT);
				canvas.drawPath(path, paint);
				invalidate();
				moveState = 3;
				break;
			case 4:
				/*
				 * 如果点击三角形内，则开始移动三角形
				 * 
				 */
				//计算三角形的重心点coreTanriglePoint
				Point coreTanriglePoint = new Point((point1.x+point2.x+point3.x)/3,(point1.y+point2.y+point3.y)/3);
				//计算移动距离
				int movedX = movePoint.x - coreTanriglePoint.x;
				int movedY = movePoint.y - coreTanriglePoint.y;
				//重新设定三角形 的三个顶点
				point1.set(point1.x + movedX, point1.y + movedY);
				point2.set(point2.x + movedX, point2.y + movedY);
				point3.set(point3.x + movedX, point3.y + movedY);
				//重新设定绘图路径path
				path = new Path();
				path.moveTo(point1.x, point1.y);
				path.lineTo(point2.x, point2.y);
				path.lineTo(point3.x, point3.y);
				path.close();
				//画三角形
				bitmap.eraseColor(Color.TRANSPARENT);
				canvas.drawPath(path, paint);
				invalidate();
				moveState = 4;
				break;
			case 0:
				moveState = 0;
				break;
			default:
				break;
			}
			
			
			break;
			
		
			
			
		case MotionEvent.ACTION_UP:
			upPoint.set((int)event.getX(), (int)event.getY());
			
			switch (moveState) {
			case 1:
				point1.set(upPoint.x, upPoint.y);
				point1Rect = new Rect(point1.x-20, point1.y-20, point1.x+20, point1.y+20);
				Log.i("ACTION_UP", "point1:"+point1);
				break;
			case 2:
				point2.set(upPoint.x, upPoint.y);
				point2Rect = new Rect(point2.x-20, point2.y-20, point2.x+20, point2.y+20);
				Log.i("ACTION_UP", "point2:"+point2);
				break;
			case 3:
				point3.set(upPoint.x, upPoint.y);
				point3Rect = new Rect(point3.x-20, point3.y-20, point3.x+20, point3.y+20);
				Log.i("ACTION_UP", "point3:"+point3);
				break;
			case 4:
				point1Rect = new Rect(point1.x-20, point1.y-20, point1.x+20, point1.y+20);
				point2Rect = new Rect(point2.x-20, point2.y-20, point2.x+20, point2.y+20);
				point3Rect = new Rect(point3.x-20, point3.y-20, point3.x+20, point3.y+20);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
		
		return true;
	}
	
	
	
	 //比较三角形面积来判断用户点击的点是否在三角形内
	    public static boolean panduan(Point a, Point b, Point c, Point p) {

	        double abc = triangleArea(a, b, c);
	        double abp = triangleArea(a, b, p);
	        double acp = triangleArea(a, c, p);
	        double bcp = triangleArea(b, c, p);

	        if (abc == abp  + acp + bcp) {
	            return true;
	        } else {
	            return false;
	        }
	    }


	 // 返回三个点组成三角形的面积
	    private static double triangleArea(Point a, Point b, Point c) {

	        double result = Math.abs((a.x * b.y + b.x * c.y + c.x * a.y - b.x * a.y
	                - c.x * b.y - a.x * c.y) / 2.0D);
	        return result;
	    }
	    

}
