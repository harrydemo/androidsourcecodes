package com.sgf.game.tankwar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Shells {
	
	public  int radius=2;
	
	public Point centerPoint;
	
	private Paint paint;
	
	public int direction;
	
	private int speed=GamePanel.UNIT;
	
	public int flag=0;//炮弹属性，与坦克属性一致
	
	public  Shells(){
		paint=new Paint();
		paint.setColor(Color.GRAY);
	}
	public void setCenterPoint(Point p){
		centerPoint=new Point(p.getX(), p.getY());
	}
	/*public  Shells(Point p,int direction,int flag){
		this.centerPoint=new Point(p.getX(), p.getY());
		this.direction=direction;
		paint=new Paint();
		paint.setColor(Color.GRAY);
		this.flag=flag;
	}*/
	public void drawShells(Canvas canvas){
		canvas.drawCircle(centerPoint.x, centerPoint.y, radius, paint);
	}
	public void move(){
		switch (direction) {
		case GamePanel.LEFT:
			centerPoint.setX(centerPoint.getX()-speed);
			break;
		case GamePanel.UP:
			centerPoint.setY(centerPoint.getY()-speed);
			break;
		case GamePanel.RIGHT:
			centerPoint.setX(centerPoint.getX()+speed);
			break;
		case GamePanel.DOWN:
			centerPoint.setY(centerPoint.getY()+speed);
			break;
		default:
			break;
		}
	}
	public Rect getFillRect(){
		return new Rect(centerPoint.getX()-radius,centerPoint.getY()-radius,centerPoint.getX()+radius,centerPoint.getY()+radius);
	}
	public int getFlag(){
		return flag;
	}
	public Point getPoint(){
		return centerPoint;
	}
}
