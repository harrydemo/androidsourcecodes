package com.sgf.game.tankwar;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public class Tank {
	
	private Point centerPoint;
	
	
	private Point pp=new Point();
	
	private int speed=GamePanel.UNIT;
	
	private int  flag=0;//̹�˵��ұ�־
	
	private int screenHeight;
	
	private int screenWidth;
	
	private Bitmap tankBmp ;

	
	private int direction=GamePanel.UP;
	
	public Matrix m=new Matrix();
	
	private long lastTrunTime;
	
	private int column;//̹����ռ����
	private int row;//̹����ռ����
	
	
	public Tank(){
		
	}
	public Tank(Point p){
		this.centerPoint=p;
	}
	public Tank(BitmapDrawable tankDrawable,int flag){
		centerPoint=new Point(200, 350);
		this.flag=flag;
		this.tankBmp=tankDrawable.getBitmap();
		pp.setY(centerPoint.getY());
		pp.setX(centerPoint.getX()+tankBmp.getWidth()/2);
		column=tankBmp.getWidth()/GamePanel.UNIT;
		row=tankBmp.getHeight()/GamePanel.UNIT;
	}
	public Tank(BitmapDrawable tankDrawable,Point p,int flag,int direction,int h,int w){
		this.flag=flag;
		this.tankBmp=tankDrawable.getBitmap();
		m.postRotate((direction-GamePanel.UP)*90);
		tankBmp=Bitmap.createBitmap(tankBmp, 0, 0, tankBmp.getWidth(), tankBmp.getHeight(), m, true);
		this.direction=direction;
		this.centerPoint=p;
		pp.setY(centerPoint.getY());
		pp.setX(centerPoint.getX()+tankBmp.getWidth()/2);
		this.screenHeight=h;
		this.screenWidth=w;
		column=tankBmp.getWidth()/GamePanel.UNIT;
		row=tankBmp.getHeight()/GamePanel.UNIT;
	}
	
	public void setScreenHeight(int h){
		screenHeight=h;
	}
	public void setScreenWidth(int w){
		screenWidth=w;
	}
	/**
	 * ���³�ʼ����Ͳ�������
	 */
	private void resetFrontPoint(){
		if(direction==GamePanel.UP){
			pp.setY(centerPoint.getY());
			pp.setX(centerPoint.getX()+tankBmp.getWidth()/2);
		}else if(direction==GamePanel.DOWN){
			pp.setY(centerPoint.getY()+tankBmp.getHeight());
			pp.setX(centerPoint.getX()+tankBmp.getWidth()/2);
		}else if(direction==GamePanel.LEFT){
			pp.setY(centerPoint.getY()+tankBmp.getHeight()/2);
			pp.setX(centerPoint.getX());
		}else if(direction==GamePanel.RIGHT){
			pp.setY(centerPoint.getY()+tankBmp.getHeight()/2);
			pp.setX(centerPoint.getX()+tankBmp.getWidth());
		}
	}
	private void resetTankPoint(){
		modifyMapStatus(0);//�ͷ�ԭ�е�ͼռλ
		if(direction==GamePanel.UP){
			this.centerPoint.setY(centerPoint.getY()-speed);
		}else if(direction==GamePanel.DOWN){
			this.centerPoint.setY(centerPoint.getY()+speed);
		}else if(direction==GamePanel.LEFT){
			this.centerPoint.setX(centerPoint.getX()-speed);
		}else if(direction==GamePanel.RIGHT){
			this.centerPoint.setX(centerPoint.getX()+speed);
		}
		resetFrontPoint();
		modifyMapStatus(1);//���½��е�ͼռλ
	}
	/** 
	 * �޸ĵ�ͼ״̬
	 */
	private void modifyMapStatus(int status){
		int bC=centerPoint.getX()/GamePanel.UNIT;
		int bR=centerPoint.getY()/GamePanel.UNIT;
	
		for(int i=bR;i<bR+row;i++){
			for(int j=bC;j<bC+column;j++){
				GamePanel.map[i][j]=status;
			}
		}
	}
	
	public int getDirection(){
		return this.direction;
	}
	public void drawTank(Canvas canvas){
		canvas.drawBitmap(tankBmp, centerPoint.x, centerPoint.y, null);
	}
	public Point getFrontPoint(){
		return this.pp;
	}
	
	public void moveUp(){
		m.reset();
		m.postRotate((GamePanel.UP-direction)*90);
		tankBmp=Bitmap.createBitmap(tankBmp, 0, 0, tankBmp.getWidth(), tankBmp.getHeight(), m, true);
		direction=GamePanel.UP;
		if(!canMove()){
			resetFrontPoint();//��Ȼ�����ƶ�������Ҫ����Ͳλ�����¼���
			return ;
		}
		resetTankPoint();
	}
	public void moveDown(){
		m.reset();
		m.postRotate((GamePanel.DOWN-direction)*90);
		tankBmp=Bitmap.createBitmap(tankBmp, 0, 0, tankBmp.getWidth(), tankBmp.getHeight(), m, true);
		direction=GamePanel.DOWN;
		if(!canMove()){
			resetFrontPoint();//��Ȼ�����ƶ�������Ҫ����Ͳλ�����¼���
			return ;
		}
		resetTankPoint();
	}
	public void moveLeft(){
		m.reset();
		
		m.postRotate((GamePanel.LEFT-direction)*90);
		tankBmp=Bitmap.createBitmap(tankBmp, 0, 0, tankBmp.getWidth(), tankBmp.getHeight(), m, true);
		direction=GamePanel.LEFT;
		if(!canMove()){
			resetFrontPoint();//��Ȼ�����ƶ�������Ҫ����Ͳλ�����¼���
			return ;
		}
		resetTankPoint();
	}
	public void moveRight(){
		m.reset();
		m.postRotate((GamePanel.RIGHT-direction)*90);
		tankBmp=Bitmap.createBitmap(tankBmp, 0, 0, tankBmp.getWidth(), tankBmp.getHeight(), m, true);
		direction=GamePanel.RIGHT;
		if(!canMove()){
			resetFrontPoint();//��Ȼ�����ƶ�������Ҫ����Ͳλ�����¼���
			return ;
		}
		resetTankPoint();
	}
	
	public void move(){
		if(direction==GamePanel.UP){
			moveUp();
		}else if(direction==GamePanel.DOWN){
			moveDown();
		}else if(direction==GamePanel.LEFT){
			moveLeft();
		}else if(direction==GamePanel.RIGHT){
			moveRight();
		}
	}
	public Shells fire(){
		Shells s=GameFactory.createShells();
		s.setCenterPoint(pp);
		s.direction=direction;
		s.flag=flag;
		return s;
	}
	//���̹����ռ�ľ�������
	public Rect getFillRect(){
		return new Rect(centerPoint.getX(), centerPoint.getY(), centerPoint.getX()+tankBmp.getWidth()-1, centerPoint.getY()+tankBmp.getHeight()-1);
	}
	//�ж��ӵ��Ƿ����̹�ˣ��ж��ӵ���������̹����ռ�����Ƿ��ཻ
	public boolean hit(Shells s){
		if(s.getFlag()==flag){
			return false;
		}
		if(getFillRect().intersect(s.getFillRect())){//�н�����̹�˱�����
			modifyMapStatus(0);//�ͷŵ�ͼռλ
			return true;
		}
		return false;
	}
	 /**
	  * �ж�̹���Ƿ����ƶ���
	  * 1����ʱ����̫Сʱ�������ƶ�
	  * 2����̹�������Ķ�ǽʱ�������ƶ�
	  * 3����̹�˵���һ���˶�������������ʱ�������ƶ�,̹�����˶����Ƶ��ж����ж�̹����ǰ������������Ƿ��ڵ�ͼ�ϱ�ռλ
	  * @return
	  */
	private boolean canMove(){
		if(System.currentTimeMillis()-lastTrunTime<300){//�˴��ƶ������ϴ��ƶ���Ҫ����300����
			return false;
		}
		//resetFrontPoint();
		boolean f=false;
		
		if(direction==GamePanel.UP){//��̹�������ƶ�ʱ
			if(centerPoint.getY()-speed>=0){
				if(GamePanel.map[(centerPoint.getY()-speed)/GamePanel.UNIT][centerPoint.getX()/GamePanel.UNIT]==0
					&&	GamePanel.map[(centerPoint.getY()-speed)/GamePanel.UNIT][(centerPoint.getX()+GamePanel.UNIT)/GamePanel.UNIT]==0
					&&	GamePanel.map[(centerPoint.getY()-speed)/GamePanel.UNIT][(centerPoint.getX()+2*GamePanel.UNIT)/GamePanel.UNIT]==0){
					f=true;
				}
			}
		}else if(direction==GamePanel.DOWN){
			if( centerPoint.getY()+tankBmp.getHeight()+speed<screenHeight){
				if(GamePanel.map[(centerPoint.getY()+2*GamePanel.UNIT+speed)/GamePanel.UNIT][centerPoint.getX()/GamePanel.UNIT]==0
					&& GamePanel.map[(centerPoint.getY()+2*GamePanel.UNIT+speed)/GamePanel.UNIT][(centerPoint.getX()+GamePanel.UNIT)/GamePanel.UNIT]==0	
					&& GamePanel.map[(centerPoint.getY()+2*GamePanel.UNIT+speed)/GamePanel.UNIT][(centerPoint.getX()+2*GamePanel.UNIT)/GamePanel.UNIT]==0){
					f=true;
				}
			}
		}else if(direction==GamePanel.LEFT){
			
			if( centerPoint.getX()-speed>=0){
				if(GamePanel.map[centerPoint.getY()/GamePanel.UNIT][(centerPoint.getX()-speed)/GamePanel.UNIT]==0
					&& GamePanel.map[(centerPoint.getY()+GamePanel.UNIT)/GamePanel.UNIT][(centerPoint.getX()-speed)/GamePanel.UNIT]==0
					&& GamePanel.map[(centerPoint.getY()+2*GamePanel.UNIT)/GamePanel.UNIT][(centerPoint.getX()-speed)/GamePanel.UNIT]==0){
					f=true;
				}
			}
		}else if(direction==GamePanel.RIGHT){
			if(  centerPoint.getX()+tankBmp.getWidth()+speed<screenWidth){
				if(GamePanel.map[centerPoint.getY()/GamePanel.UNIT][(centerPoint.getX()+2*GamePanel.UNIT+speed)/GamePanel.UNIT]==0
					&& GamePanel.map[(centerPoint.getY()+GamePanel.UNIT)/GamePanel.UNIT][(centerPoint.getX()+2*GamePanel.UNIT+speed)/GamePanel.UNIT]==0
					&& GamePanel.map[(centerPoint.getY()+2*GamePanel.UNIT)/GamePanel.UNIT][(centerPoint.getX()+2*GamePanel.UNIT+speed)/GamePanel.UNIT]==0){
					f=true;
				}
			}
		}
		if(f)
			lastTrunTime=System.currentTimeMillis();
		return f;
	}
}
