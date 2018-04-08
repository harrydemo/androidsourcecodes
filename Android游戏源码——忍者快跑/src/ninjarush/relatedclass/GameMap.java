
package ninjarush.relatedclass;

import ninjarush.mainsurfaceview.NinjaRushSurfaceView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class GameMap {
	private Bitmap bam_left,bam_mid,bam_right,bam_under,fly_bam_right,fly_bam_mid,fly_bam_left,dre4;
	private Bitmap light;
	//����Ƿ�����  �Ƿ���½��
	private boolean isFly,isLand;
	//���Ƿ���ʧ
	private boolean isDead;
	//�����ǰ���Ƿ���½����
	public boolean isLandBeforeSky;
	//�ŵ�����
	private int style;	
	//�Ŷ೤�м����м�bam_min
	private	int midNum;
	//�Ƿ����� <=3  
	private int isLight;
	//�ŵ����� 
	private int leftX,leftY;
	//�ŵĽ�βX����(���Ƴ���)
	
	public int mapEndX;
	private int bamSpeed=10;
	private int x,y,x1,y1;
	//�ŵĸ߶�
		private int bamH;
		//�ӿ�
		private int holeW;
		private int yy;
	public GameMap(Bitmap map_left,Bitmap map_mid,Bitmap map_right,Bitmap map_other,Bitmap light,int bam_leftX,int bam_leftY,int style,boolean isLandBeforeSky){
		this.style=style;
		
		this.light=light; 
		this.isLandBeforeSky=isLandBeforeSky;
		if(style==Tools.STYLE_LAND){
		this.bam_left=map_left;
		this.bam_mid=map_mid;
		this.bam_right=map_right;
		this.bam_under=map_other; 
		midNum=(int)(Math.random()*4)+1;
		isLight=(int)(Math.random()*10);
		inHole();
		this.leftX=bam_leftX+holeW;
		this.leftY=bamH;
		mapEndX=midNum*bam_mid.getWidth()+leftX+bam_left.getWidth()+bam_right.getWidth();
		}
		if(style==Tools.STYLE_SKY){
			this.fly_bam_mid=map_mid;
			this.fly_bam_right=map_right;
			this.fly_bam_left=map_left;
			this.dre4=map_other;
			midNum=(int)(Math.random()*3)+3;
			this.leftX=bam_leftX+fly_bam_mid.getWidth()+dre4.getWidth();
			this.leftY=bam_leftY;
			mapEndX=midNum*fly_bam_mid.getWidth()+leftX+fly_bam_left.getWidth()+fly_bam_right.getWidth();
		}		
	}
	public GameMap(Bitmap bam_left,Bitmap bam_mid,Bitmap bam_right,Bitmap bam_under,int bam_leftX,int bam_leftY,int style){
		this.bam_left=bam_left;
		this.bam_mid=bam_mid;
		this.bam_right=bam_right;
		this.bam_under=bam_under; 
		this.leftX=bam_leftX;
		this.leftY=bam_leftY;
		this.style=style;
		mapEndX=6*bam_mid.getWidth()+leftX+bam_left.getWidth()+bam_right.getWidth();
	}
	private void inHole(){
		//////////�߶�
		int num_H=(int)(Math.random()*4);
		if(num_H==0||num_H==1){
			bamH=NinjaRushSurfaceView.screenH-bam_under.getHeight()*2/3;
		}
		if(num_H==2){
			bamH=NinjaRushSurfaceView.screenH-bam_under.getHeight()*4/5;
		}
		if(num_H==3){
			bamH=NinjaRushSurfaceView.screenH-bam_under.getHeight()/2;
		}
		//�ӿ�
		int num_W=(int)(Math.random()*4);
		if(num_W==0){
			holeW=bam_mid.getWidth();
			}
		if(num_W==1){
			holeW=bam_mid.getWidth()/2;
		}
		if(num_W==2){
			holeW=bam_mid.getWidth()*2/3;
			}
		if(num_W==3){
			holeW=bam_mid.getWidth()*4/5;
			}
		////////////
	}
	public boolean isLandBeforeSky() {
		return isLandBeforeSky;
	}
	public void setLandBeforeSky(boolean isLandBeforeSky) {
		this.isLandBeforeSky = isLandBeforeSky;
	}
	public void draw(Canvas canvas,Paint paint){
		//����style�ж���½�ػ������
		switch (style) {
		case Tools.STYLE_LAND:
			
			if(isLight<=3){
				canvas.drawBitmap(light, leftX+(mapEndX-leftX)/2,leftY-light.getHeight()+5+yy, paint);
			}
			canvas.drawBitmap(bam_left,leftX, leftY+yy, paint);	
			canvas.drawBitmap(bam_under,leftX+bam_left.getWidth(), leftY+yy, paint);
			canvas.drawBitmap(bam_under, mapEndX-bam_under.getWidth()-bam_left.getWidth(), leftY+yy, paint);
			for(int i=0;i<midNum;i++){
				canvas.drawBitmap(bam_mid, i*bam_mid.getWidth()+leftX+bam_left.getWidth(), leftY+yy, paint);
			}
			canvas.drawBitmap(bam_right, midNum*bam_mid.getWidth()+leftX+bam_left.getWidth(), leftY+yy, paint);
			break;
		case Tools.STYLE_SKY:
			
			//������ XΪ ½�ؽ�����ENDX YΪ½�ؽ���ʱ��ENDY
			if(isLandBeforeSky){
			canvas.drawBitmap(dre4,leftX-fly_bam_mid.getWidth()-dre4.getWidth(), leftY-dre4.getHeight()+5+yy, paint);
			}
			//�����������Ļ����ʾһ�� 
			canvas.drawBitmap(fly_bam_left, leftX,0- fly_bam_left.getHeight()/2+yy, paint);
			for(int i=0;i<midNum;i++){
				canvas.drawBitmap(fly_bam_mid, i*fly_bam_mid.getWidth()+leftX-fly_bam_mid.getWidth()+fly_bam_left.getWidth()+fly_bam_mid.getWidth(),0- fly_bam_left.getHeight()/2+yy, paint);
			}
			canvas.drawBitmap(fly_bam_right, midNum*fly_bam_mid.getWidth()+leftX-fly_bam_mid.getWidth()+fly_bam_mid.getWidth()+fly_bam_left.getWidth(), 0-fly_bam_left.getHeight()/2+yy, paint);
				
			break;
		case Tools.STYLE_START:
			
			canvas.drawBitmap(bam_left,leftX, leftY+yy, paint);	
			canvas.drawBitmap(bam_under,leftX+bam_left.getWidth(), leftY+yy, paint);
			canvas.drawBitmap(bam_under, mapEndX-bam_under.getWidth()-bam_left.getWidth(), leftY+yy, paint);
			for(int i=0;i<6;i++){
				canvas.drawBitmap(bam_mid, i*bam_mid.getWidth()+leftX+bam_left.getWidth(), leftY+yy, paint);
			}
			canvas.drawBitmap(bam_right, 6*bam_mid.getWidth()+leftX+bam_left.getWidth(), leftY+yy, paint);
			break;
		}	
	}
	public int getStyle() {
		return style;
	}
	public void setStyle(int style) {
		this.style = style;
	}
	public void logic(){
			leftX-=bamSpeed;
			mapEndX-=bamSpeed;
			if(mapEndX <=0){
				isDead=true;
			}
	}
	public boolean isDead(){

		return isDead;
	}
	public int getleftX() {
		return leftX;
	}
	public void setLeftX(int leftX) {
		this.leftX = leftX;
	}
	public void setSpeed(int speed){
		this.bamSpeed=speed;
	}
	public int getLeftY() {
		return leftY;
	}
	public void setLeftY(int leftY) {
		this.leftY = leftY;
	}
	public int getEndX(){
		return mapEndX;
	}
	public void setYy(int yy){
		this.yy=yy;
	}
public int getSky_y(){
	return yy;
}
	
}
