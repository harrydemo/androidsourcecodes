package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

//球杆类
public class Cue {
	float x;//左上角位置	
	float y;
	float rotateX;//旋转中心点
	float rotateY;
	private float angdeg=0;//旋转角度	
	float width;//球杆宽度
	float height;//球杆高度
	float disWithBall=Constant.DIS_WITH_BALL;//球杆与母球边缘的距离
	Bitmap bitmap;//位图
	Ball mainBall;//母球
	private boolean showCueFlag=true;//是否显示球杆的标志
	private final float angleSpanSlow=0.2f;//微调角度步进
	private final float angleSpanFast=1f;//粗调角度步进
	private boolean aimFlag=true;	
	private final float lineLength=Table.tableAreaWidth;//瞄准线的长度
	//关于击球动画的量
	private final  float backSpan=3;//球杆向后退的步进
	private final float forwardSpan=10;//球杆前进时的步进	
	private final float maxDis=50;//球杆向后退的最大距离
	private float span=backSpan;//击球时球杆步进
	private boolean showingAnimFlag=false;//是否正在播放击球动画的标志	
	public Cue(Bitmap bitmap,Ball mainBall)
	{
		this.bitmap=bitmap;
		this.mainBall=mainBall;		
		this.width=bitmap.getWidth();
		this.height=bitmap.getHeight();		
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{
		if(!showCueFlag){//如果不能显示球杆，直接返回
			return;
		}	
		//计算球杆旋转中心点位置
		this.rotateX=this.width+this.disWithBall+Ball.r;
		this.rotateY=this.height/2;
		//计算球杆的位置
		x=mainBall.getX()-width-disWithBall;
		y=mainBall.getY()+Ball.r-height/2;
		//绘制球杆
		Matrix m1=new Matrix();//平移矩阵
		m1.setTranslate(x+Constant.X_OFFSET,y+Constant.Y_OFFSET);
		Matrix m2=new Matrix();//旋转矩阵
		m2.setRotate(angdeg, rotateX, rotateY);
		Matrix mz=new Matrix();//综合矩阵
		mz.setConcat(m1, m2);					
		canvas.drawBitmap(bitmap, mz,paint);//绘制球杆
		//绘制瞄准线
		canvas.save();//保存画布
		canvas.clipRect(Table.lkx+Constant.X_OFFSET, Table.ady+Constant.Y_OFFSET, Table.efx+Constant.X_OFFSET, Table.jgy+Constant.Y_OFFSET);//划定矩形区域		
		float angrad=(float) Math.toRadians(angdeg);//将角度转换成弧度
		float startX=(float) (mainBall.getX()+Constant.X_OFFSET+Ball.r+Ball.r*Math.cos(angrad));//瞄准线起点坐标
		float startY=(float) (mainBall.getY()+Constant.Y_OFFSET+Ball.r+Ball.r*Math.sin(angrad));
		float stopX=startX+(float)(lineLength*Math.cos(angrad));//瞄准线终点坐标
		float stopY=startY+(float)(lineLength*Math.sin(angrad));
		paint.setColor(Color.YELLOW);//画笔为黄色
		paint.setAlpha(240);//设置画笔不透明度
		canvas.drawLine(startX, startY, stopX, stopY, paint);//绘制瞄准线
		canvas.restore();//恢复画布
		paint.setAlpha(255);//恢复画笔透不明度
	}
	//根据触控屏幕的位置计算球杆旋转角度的方法
	public void calcuAngle(float pressX,float pressY)
	{
		//球杆的方向向量
		float dirX=pressX-(mainBall.getX()+Ball.r+Constant.X_OFFSET);
		float dirY=pressY-(mainBall.getY()+Ball.r+Constant.Y_OFFSET);
		if(!aimFlag){//如果不是按照目标确定旋转角度，将方向向量置反
			dirX = -dirX;
			dirY = -dirY;
		}
		//根据球杆的方向计算球杆应旋转的角度
		if(dirY>=0)
		{
			angdeg=(float) Math.toDegrees((Math.atan(-dirX/dirY)+Math.PI/2));
		}
		else if(dirY<0)
		{
			angdeg=(float) Math.toDegrees((Math.atan(-dirX/dirY)+Math.PI*3/2));
		}
	}
	//逆时针方向微调的方法
	public void rotateLeftSlowly(){
		angdeg+=angleSpanSlow;
	}
	//顺时针方向微调的方法
	public void rotateRightSlowly(){
		angdeg-=angleSpanSlow;
	}
	//逆时针方向粗调的方法
	public void rotateLeftFast(){
		angdeg+=angleSpanFast;
	}
	//顺时针方向粗调的方法
	public void rotateRightFast(){
		angdeg-=angleSpanFast;
	}
	//改变disWithBall的值，来播放击球动画的方法
	public float changeDisWithBall()
	{
		//如果和母球的距离超出范围，使球杆前进
		if(disWithBall>=maxDis){
			span=-forwardSpan;
		}
		disWithBall+=span;
		return disWithBall;
	}
	//恢复击球动画的初始值的方法
	public void resetAnimValues(){
		disWithBall=Constant.DIS_WITH_BALL;
		span=backSpan;
	}
	public float getAngle() {
		return angdeg;
	}	
	public boolean isShowCueFlag() {
		return showCueFlag;
	}
	public void setShowCueFlag(boolean showCueFlag) {
		this.showCueFlag = showCueFlag;
	}
	public boolean isAimFlag() {
		return aimFlag;
	}
	public void setAimFlag(boolean aimFlag) {
		this.aimFlag = aimFlag;
	}
	public boolean isShowingAnimFlag() {
		return showingAnimFlag;
	}
	public void setShowingAnimFlag(boolean showingAnimFlag) {
		this.showingAnimFlag = showingAnimFlag;
	}
}
