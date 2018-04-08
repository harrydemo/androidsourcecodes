package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Ball {
	private float x;//球的外接正方形左上角顶点坐标
	private float y;
	static final float r=Constant.BALL_SIZE/2;//球的半径
	static final float d=Constant.BALL_SIZE;//球的直径
	static final float vMax=Constant.V_MAX;//球的最大速度（规定：球的最大速度不可以超过100）
	float vx;//球的运动速度
	float vy;
	float timeSpan=Constant.TIME_SPAN;//球运动的模拟时间间隔（规定: timeSpan不可以>=Ball.d）
	float vAttenuation=Constant.V_ATTENUATION;//速度衰减比例
	float vMin=Constant.V_MIN;//速度最小值，当速度小于此值时球停止运动
	Bitmap[] bitmaps;
	private int bmpIndex=0;
	float bmpIndexf=0;//辅助索引
	Table table;//球台
	GameView gameView;
	boolean InHoleflag=false;//球是否进洞的标志
	private float rotateX;
	private float rotateY;
	private float angdeg=0;//旋转角度	
	public Ball(Bitmap[] bitmaps,GameView gameView,float vx,float vy,float[] pos)
	{
		this.bitmaps=bitmaps;
		this.gameView=gameView;
		this.vx=vx;
		this.vy=vy;
		this.x=pos[0];
		this.y=pos[1];
		this.table=gameView.table;
		rotateX=bitmaps[0].getWidth()/2;
		rotateY=bitmaps[0].getHeight()/2;
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{
		//球的实际位置和逻辑位置不一样，用大屏时可以看出
		//（碰撞检测逻辑位置是从屏幕的左上角开始的，而实际在游戏界面的左上角绘制的）
		calAngle();
		//绘制球
		Matrix m1=new Matrix();//平移矩阵
		m1.setTranslate(x+Constant.X_OFFSET,y+Constant.Y_OFFSET);
		Matrix m2=new Matrix();//旋转矩阵
		m2.setRotate(angdeg, rotateX, rotateY);
		Matrix mz=new Matrix();//综合矩阵
		mz.setConcat(m1, m2);					
		canvas.drawBitmap(bitmaps[bmpIndex], mz,paint);//绘制球
	}
	public void calAngle()
	{
		if(vx==0&&vy==0)
		{
			bmpIndex=0;
			return;
		}
		//根据球的速度方向计算球应旋转的角度
		float dirX=vx;
		float dirY=vy;		
		if(dirY>=0)
		{
			angdeg=(float) Math.toDegrees((Math.atan(-dirX/dirY)+Math.PI/2));
		}
		else if(dirY<0)
		{
			angdeg=(float) Math.toDegrees((Math.atan(-dirX/dirY)+Math.PI*3/2));
		}		
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public boolean canGo(float tempX,float tempY)
	{
		boolean canGoFlag=true;
		/*
		 * 判断球之间是否碰撞:
		 * 通过临时的位置判断两球会不会碰撞，
		 * 如果发生碰撞，只改变两球的方向，
		 * 而不改变两球的位置
		 */
		for(Ball b:gameView.alBalls)
		{
			if(b!=this && CollisionUtil.collisionCalculate(new float[]{tempX,tempY}, this, b))
			{
				if(gameView.activity.isSoundOn()){//播放球碰撞的声音
					gameView.playSound(GameView.HIT_SOUND, 0);
				}
				canGoFlag=false;
			}
		}
		if(canGoFlag==false){//如果和球碰撞了就不再考虑和球台的碰撞
			return false;
		}
		/*
		 * 判断球是否进洞
		 */
		for(int i=0;i<Table.holeCenterPoints.length;i++)//循环洞的中心坐标数组
		{
			float disWithHole=CollisionUtil.mould(//计算球心和洞中心的距离
					new float[]{tempX+Ball.r-Table.holeCenterPoints[i][0],
							tempY+Ball.r-Table.holeCenterPoints[i][1]});
			//判断是否进中洞
			if(Table.holeCenterPoints[i]==Table.N || Table.holeCenterPoints[i]==Table.Q){
				if(disWithHole<=Table.middleHoleR){//如果球心在洞内，判定为进洞
					InHoleflag=true;
					break;
				}
			}else{//判断是否进角上的洞
				if(disWithHole<=Table.cornerHoleR){//如果球心在洞内，判定为进洞
					InHoleflag=true;
					break;
				}
			}
		}
		if(InHoleflag==true){//如果球进洞，可以走，返回true，不再做其他判断
			if(gameView.activity.isSoundOn()){//播放球进洞的声音
				gameView.playSound(GameView.BALL_IN_SOUND, 0);
			}
			return true;
		}
		/*
		 * 判断是否和角有碰撞
		 */
		//球心位置
		float center[]={tempX+r,tempY+r};
		boolean collisionWithCornerFlag=false;
		for(float[] p:Table.collisionPoints)
		{
			//判断球是否和角碰撞
			if(CollisionUtil.calcuDisSquare(center, p)<=r*r)
			{
				collisionWithCornerFlag=true;
				canGoFlag=false;
				//与角碰撞，x,y速度全反向
				vx=-vx;
				vy=-vy;
				break;
			}
		}
		//如果没有与角碰撞，再判断是否与边碰撞
		if(!collisionWithCornerFlag)
		{
			//球的位置超出球台
			if(tempX<=Table.lkx||tempX+d>=Table.efx)
			{
				vx=-vx;//与竖边碰撞，x速度反向
				canGoFlag=false;
			}else if(tempY<=Table.ady||tempY+d>=Table.jgy){
				vy=-vy;//与横边碰撞，y速度反向
				canGoFlag=false;
			}
		}		
		return canGoFlag;		
	}
	public void go()
	{
		if(isStoped()||Math.sqrt(vx*vx+vy*vy)<vMin){//速度小于阈值，球停止运动，去掉开方运算可减小计算量，提升性能
			vx=0;//速度置为0
			vy=0;
			bmpIndex=0;
			return;
		}
		float tempX=x+vx*timeSpan;//球要去的下一个位置
		float tempY=y+vy*timeSpan;
		if(this.canGo(tempX, tempY))
		{
			x=tempX;
			y=tempY;			
			
			//根据速度大小计算换图频数
			float v=(float) Math.sqrt(vx*vx+vy*vy);
			//当达到最大速度的时候才每前进一步换一次帧，没有达到最大速度将值积累起来
			bmpIndexf=bmpIndexf+Constant.K*v;
			bmpIndex=(int)(bmpIndexf)%bitmaps.length;
			//每移动一步，速度衰减
			vx*=vAttenuation;
			vy*=vAttenuation;
		}		
	}
	//判断球是否停止的方法
	public boolean isStoped()
	{
		return (vx==0 && vy==0);
	}
	//根据速度和方向改变球速度的方法
	public void changeVxy(float v,float angle)
	{
		double angrad=Math.toRadians(angle);//将角度转换成弧度
		vx=(float) (v*Math.cos(angrad));//计算x,y方向的速度
		vy=(float) (v*Math.sin(angrad));
	}
	//判断球是否进洞的方法
	public boolean isInHole(){
		return InHoleflag;
	}
	//隐藏球的方法
	public void hide(){
		vx=vy=0;
		x=y=-100000;		
	}
	//判断球是否隐藏起来的方法
	public boolean isHided(){
		return y==-100000;
	}
	//母球回到原始位置的方法
	public void reset(){
		vx=vy=0;
		//判断是否有合适的位置
		for(float iy=Table.AllBallsPos[0][1]; iy>Table.ady; iy-=Ball.r)
		{
			boolean collisionflag=false;//位置是否合理的标志
			for(Ball b:gameView.alBalls)
			{//只要位置和某一个球的位置冲突，就不能复位，继续判断下一个位置是否合适
				if(b!=this && CollisionUtil.isTwoBallsCollided(
						new float[]{Table.AllBallsPos[0][0],iy}, b))
				{
					collisionflag=true;
					break;
				}
			}
			//如果某一个位置合适，将球复位
			if(!collisionflag){
				x=Table.AllBallsPos[0][0];
				y=iy;
				InHoleflag=false;//恢复进洞标志位
				return;
			}
		}
	}
}
