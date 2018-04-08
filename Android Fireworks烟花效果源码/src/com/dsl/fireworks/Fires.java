package com.dsl.fireworks;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

public class Fires {
	public int number;
	public int colorNumber;
	public int x;
	public int y;
	public int height;
	public int width;
	public int count;
	public int nowY=0;
	
	public long cTime;
	
	public boolean isEnd=false;
	
	public Paint paint;
	public int nextColor;
	
	public final static String red="#FD0303";
	public final static String orange="#FD6703";
	public final static String yellow="#FDE803";
	public final static String green="#2DFD03";
	public final static String blue="#03F1FD";
	public final static String spinning ="#0B03FD";
	public final static String purple="#CB03FD";
	
	public Fires(FireworksSurface f){
		Random r = new Random();
		number=Math.abs(r.nextInt(9)); 
		height=f.height;
		width=f.width;
		y=height*number/9;	
		nowY=height;
		colorNumber=Math.abs(r.nextInt(7)); 
		x=width*colorNumber/7;
		paint=getPaint(colorNumber);
		paint.setStyle(Paint.Style.FILL);
		//paint.set
		cTime=System.currentTimeMillis();
	}
	
	public void start(Canvas cs){
		long nTime=System.currentTimeMillis();
		if(nTime-cTime>=100){
			cTime=nTime;		
			count++;
		
		if(count>=number){
			spread(cs);
			isEnd=true;
		}
		else{
			nowY-=height/9;
			cs.drawCircle(x, nowY, 10, paint);			
		}
		}
	}
	
	public void spread(Canvas cs){
		paint.setStrokeWidth(3f);		
		LinearGradient lg=new LinearGradient(0,0,100,100,paint.getColor(),this.nextColor,Shader.TileMode.MIRROR);
		paint.setShader(lg);
		show(cs);
	}
	
	
	public Paint getPaint(int pN){
		Paint p=new Paint();
		switch(pN){
		case 1:
			p.setColor(Color.parseColor(red));
			nextColor=Color.parseColor(orange);
			break;
		case 2:
			p.setColor(Color.parseColor(orange));
			nextColor=Color.parseColor(yellow);
			break;
		case 3:
			p.setColor(Color.parseColor(yellow));
			nextColor=Color.parseColor(green);
			break;
		case 4:
			p.setColor(Color.parseColor(green));
			nextColor=Color.parseColor(blue);
			break;
		case 5:
			p.setColor(Color.parseColor(blue));
			nextColor=Color.parseColor(spinning);
			break;
		case 6:
			p.setColor(Color.parseColor(spinning));
			nextColor=Color.parseColor(purple);
			break;
		case 7:
			p.setColor(Color.parseColor(purple));
			nextColor=Color.parseColor(red);
			break;
		}
		return p;		
	}
	
	public void show(Canvas cs){
		switch(number){
		case 1:
			one(cs);
			break;
		case 2:
			tow(cs);
			break;
		case 3:
			three(cs);
			break;
		case 4:
			four(cs);
			break;
		case 5:
			five(cs);
			break;
		case 6:
			six(cs);
			break;
		case 7:
			seven(cs);
			break;
		case 8:
			eight(cs);
			break;
		case 9:
			nigh(cs);
			break;			
		}
	}
	
	public void one(Canvas cs){
		int[][] zu={
			{0,100}
		};
		cs.drawLine(x, nowY, x+zu[0][0], nowY-zu[0][1], paint);
		//cs.drawCircle(x+zu[0][0], nowY-zu[0][1], 10, paint);
	}
	
	public void tow(Canvas cs){
		int[][] zu={
				{0,100},{0,-100}
			};
		for(int i=0;i<zu.length;i++){
		cs.drawLine(x, nowY, x+zu[i][0], nowY-zu[i][1], paint);
		//cs.drawCircle(zu[i][0], nowY-zu[i][1], 10, paint);
		}
	}
	
	public void three(Canvas cs){
		int[][] zu={
				{0,100},{86,-50},{-86,-50}
			};
		for(int i=0;i<zu.length;i++){
			cs.drawLine(x, nowY, x+zu[i][0], nowY-zu[i][1], paint);
			//cs.drawCircle(zu[i][0], nowY-zu[i][1], 10, paint);
			}
	}
	
	public void four(Canvas cs){
		int[][] zu={
				{0,100},{100,0},{0,-100},{-100,0}
			};
		for(int i=0;i<zu.length;i++){
			cs.drawLine(x, nowY, x+zu[i][0], nowY-zu[i][1], paint);
			//cs.drawCircle(zu[i][0], nowY-zu[i][1], 10, paint);
			}
	}
	
	public void five(Canvas cs){
		int[][] zu={
				{0,100},{95,31},{59,-81},{-59,-81},{-95,31}
			};
		for(int i=0;i<zu.length;i++){
			cs.drawLine(x, nowY, x+zu[i][0], nowY-zu[i][1], paint);
			//cs.drawCircle(zu[i][0], nowY-zu[i][1], 10, paint);
			}
	}
	
	public void six(Canvas cs){
		int[][] zu={
				{0,100},{87,50},{87,-50},{0,-100},{-87,-50},{-87,50}
			};
		for(int i=0;i<zu.length;i++){
			cs.drawLine(x, nowY, x+zu[i][0], nowY-zu[i][1], paint);
			//cs.drawCircle(zu[i][0], nowY-zu[i][1], 10, paint);
			}
	}
	
	public void seven(Canvas cs){
		int[][] zu={
				{0,100},{78,63},{98,-20},{45,-89},{-45,-89},{-98,-20},{-78,63}
			};
		for(int i=0;i<zu.length;i++){
			cs.drawLine(x, nowY, x+zu[i][0], nowY-zu[i][1], paint);
			//cs.drawCircle(zu[i][0], nowY-zu[i][1], 10, paint);
			}
	}
	
	public void eight(Canvas cs){
		int[][] zu={
				{0,100},{71,71},{100,0},{71,-71},{0,-100},{-71,-71},{-100,0},{-71,71}
			};
		for(int i=0;i<zu.length;i++){
			cs.drawLine(x, nowY, x+zu[i][0], nowY-zu[i][1], paint);
			//cs.drawCircle(zu[i][0], nowY-zu[i][1], 10, paint);
			}
	}
	
	public void nigh(Canvas cs){
		int[][] zu={
				{0,100},{64,76},{98,17},{87,-50},{34,-93},{-34,-93},{-87,-50},{-98,17},{-64,76}
			};
		for(int i=0;i<zu.length;i++){
			cs.drawLine(x, nowY, x+zu[i][0], nowY-zu[i][1], paint);
			//cs.drawCircle(zu[i][0], nowY-zu[i][1], 10, paint);
			}
	}
	
}
