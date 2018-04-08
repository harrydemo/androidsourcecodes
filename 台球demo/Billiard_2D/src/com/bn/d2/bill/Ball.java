package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class Ball {
	private float x;//���������������ϽǶ�������
	private float y;
	static final float r=Constant.BALL_SIZE/2;//��İ뾶
	static final float d=Constant.BALL_SIZE;//���ֱ��
	static final float vMax=Constant.V_MAX;//�������ٶȣ��涨���������ٶȲ����Գ���100��
	float vx;//����˶��ٶ�
	float vy;
	float timeSpan=Constant.TIME_SPAN;//���˶���ģ��ʱ�������涨: timeSpan������>=Ball.d��
	float vAttenuation=Constant.V_ATTENUATION;//�ٶ�˥������
	float vMin=Constant.V_MIN;//�ٶ���Сֵ�����ٶ�С�ڴ�ֵʱ��ֹͣ�˶�
	Bitmap[] bitmaps;
	private int bmpIndex=0;
	float bmpIndexf=0;//��������
	Table table;//��̨
	GameView gameView;
	boolean InHoleflag=false;//���Ƿ�����ı�־
	private float rotateX;
	private float rotateY;
	private float angdeg=0;//��ת�Ƕ�	
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
		//���ʵ��λ�ú��߼�λ�ò�һ�����ô���ʱ���Կ���
		//����ײ����߼�λ���Ǵ���Ļ�����Ͻǿ�ʼ�ģ���ʵ������Ϸ��������Ͻǻ��Ƶģ�
		calAngle();
		//������
		Matrix m1=new Matrix();//ƽ�ƾ���
		m1.setTranslate(x+Constant.X_OFFSET,y+Constant.Y_OFFSET);
		Matrix m2=new Matrix();//��ת����
		m2.setRotate(angdeg, rotateX, rotateY);
		Matrix mz=new Matrix();//�ۺϾ���
		mz.setConcat(m1, m2);					
		canvas.drawBitmap(bitmaps[bmpIndex], mz,paint);//������
	}
	public void calAngle()
	{
		if(vx==0&&vy==0)
		{
			bmpIndex=0;
			return;
		}
		//��������ٶȷ��������Ӧ��ת�ĽǶ�
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
		 * �ж���֮���Ƿ���ײ:
		 * ͨ����ʱ��λ���ж�����᲻����ײ��
		 * ���������ײ��ֻ�ı�����ķ���
		 * �����ı������λ��
		 */
		for(Ball b:gameView.alBalls)
		{
			if(b!=this && CollisionUtil.collisionCalculate(new float[]{tempX,tempY}, this, b))
			{
				if(gameView.activity.isSoundOn()){//��������ײ������
					gameView.playSound(GameView.HIT_SOUND, 0);
				}
				canGoFlag=false;
			}
		}
		if(canGoFlag==false){//���������ײ�˾Ͳ��ٿ��Ǻ���̨����ײ
			return false;
		}
		/*
		 * �ж����Ƿ����
		 */
		for(int i=0;i<Table.holeCenterPoints.length;i++)//ѭ������������������
		{
			float disWithHole=CollisionUtil.mould(//�������ĺͶ����ĵľ���
					new float[]{tempX+Ball.r-Table.holeCenterPoints[i][0],
							tempY+Ball.r-Table.holeCenterPoints[i][1]});
			//�ж��Ƿ���ж�
			if(Table.holeCenterPoints[i]==Table.N || Table.holeCenterPoints[i]==Table.Q){
				if(disWithHole<=Table.middleHoleR){//��������ڶ��ڣ��ж�Ϊ����
					InHoleflag=true;
					break;
				}
			}else{//�ж��Ƿ�����ϵĶ�
				if(disWithHole<=Table.cornerHoleR){//��������ڶ��ڣ��ж�Ϊ����
					InHoleflag=true;
					break;
				}
			}
		}
		if(InHoleflag==true){//���������������ߣ�����true�������������ж�
			if(gameView.activity.isSoundOn()){//���������������
				gameView.playSound(GameView.BALL_IN_SOUND, 0);
			}
			return true;
		}
		/*
		 * �ж��Ƿ�ͽ�����ײ
		 */
		//����λ��
		float center[]={tempX+r,tempY+r};
		boolean collisionWithCornerFlag=false;
		for(float[] p:Table.collisionPoints)
		{
			//�ж����Ƿ�ͽ���ײ
			if(CollisionUtil.calcuDisSquare(center, p)<=r*r)
			{
				collisionWithCornerFlag=true;
				canGoFlag=false;
				//�����ײ��x,y�ٶ�ȫ����
				vx=-vx;
				vy=-vy;
				break;
			}
		}
		//���û�������ײ�����ж��Ƿ������ײ
		if(!collisionWithCornerFlag)
		{
			//���λ�ó�����̨
			if(tempX<=Table.lkx||tempX+d>=Table.efx)
			{
				vx=-vx;//��������ײ��x�ٶȷ���
				canGoFlag=false;
			}else if(tempY<=Table.ady||tempY+d>=Table.jgy){
				vy=-vy;//������ײ��y�ٶȷ���
				canGoFlag=false;
			}
		}		
		return canGoFlag;		
	}
	public void go()
	{
		if(isStoped()||Math.sqrt(vx*vx+vy*vy)<vMin){//�ٶ�С����ֵ����ֹͣ�˶���ȥ����������ɼ�С����������������
			vx=0;//�ٶ���Ϊ0
			vy=0;
			bmpIndex=0;
			return;
		}
		float tempX=x+vx*timeSpan;//��Ҫȥ����һ��λ��
		float tempY=y+vy*timeSpan;
		if(this.canGo(tempX, tempY))
		{
			x=tempX;
			y=tempY;			
			
			//�����ٶȴ�С���㻻ͼƵ��
			float v=(float) Math.sqrt(vx*vx+vy*vy);
			//���ﵽ����ٶȵ�ʱ���ÿǰ��һ����һ��֡��û�дﵽ����ٶȽ�ֵ��������
			bmpIndexf=bmpIndexf+Constant.K*v;
			bmpIndex=(int)(bmpIndexf)%bitmaps.length;
			//ÿ�ƶ�һ�����ٶ�˥��
			vx*=vAttenuation;
			vy*=vAttenuation;
		}		
	}
	//�ж����Ƿ�ֹͣ�ķ���
	public boolean isStoped()
	{
		return (vx==0 && vy==0);
	}
	//�����ٶȺͷ���ı����ٶȵķ���
	public void changeVxy(float v,float angle)
	{
		double angrad=Math.toRadians(angle);//���Ƕ�ת���ɻ���
		vx=(float) (v*Math.cos(angrad));//����x,y������ٶ�
		vy=(float) (v*Math.sin(angrad));
	}
	//�ж����Ƿ�����ķ���
	public boolean isInHole(){
		return InHoleflag;
	}
	//������ķ���
	public void hide(){
		vx=vy=0;
		x=y=-100000;		
	}
	//�ж����Ƿ����������ķ���
	public boolean isHided(){
		return y==-100000;
	}
	//ĸ��ص�ԭʼλ�õķ���
	public void reset(){
		vx=vy=0;
		//�ж��Ƿ��к��ʵ�λ��
		for(float iy=Table.AllBallsPos[0][1]; iy>Table.ady; iy-=Ball.r)
		{
			boolean collisionflag=false;//λ���Ƿ����ı�־
			for(Ball b:gameView.alBalls)
			{//ֻҪλ�ú�ĳһ�����λ�ó�ͻ���Ͳ��ܸ�λ�������ж���һ��λ���Ƿ����
				if(b!=this && CollisionUtil.isTwoBallsCollided(
						new float[]{Table.AllBallsPos[0][0],iy}, b))
				{
					collisionflag=true;
					break;
				}
			}
			//���ĳһ��λ�ú��ʣ�����λ
			if(!collisionflag){
				x=Table.AllBallsPos[0][0];
				y=iy;
				InHoleflag=false;//�ָ�������־λ
				return;
			}
		}
	}
}
