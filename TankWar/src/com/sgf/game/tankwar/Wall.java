package com.sgf.game.tankwar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class Wall {
	
	
	private Paint p;
	
	private Rect inRect;
	
	private Rect outRect;
	
	private int r;
	
	private int c;
	public Wall(){
		
	}
	public Wall(int r,int c){
		this.r=r;
		this.c=c;
		p=new Paint();
		inRect=new Rect(c*GamePanel.UNIT+1, r*GamePanel.UNIT+1, (c+1)*GamePanel.UNIT-1, (r+1)*GamePanel.UNIT-1);
		outRect=new Rect(c*GamePanel.UNIT, r*GamePanel.UNIT, (c+1)*GamePanel.UNIT, (r+1)*GamePanel.UNIT);
		//p.setStyle(Paint.Style.STROKE);
	}
	public void drawWall(Canvas can){
		p.setStyle(Paint.Style.FILL);
		p.setColor(Color.YELLOW);
		can.drawRect(inRect, p);
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.STROKE);
		can.drawRect(outRect, p);
	/*	if(GamePanel.map[r][c]==0){
			GamePanel.map[r][c]=2;
		}*/
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		Wall w=(Wall) o;
		return this.r==w.r&&this.c==w.c;
	}
	
}
