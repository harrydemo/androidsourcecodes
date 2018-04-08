package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

//�����
public class Cue {
	float x;//���Ͻ�λ��	
	float y;
	float rotateX;//��ת���ĵ�
	float rotateY;
	private float angdeg=0;//��ת�Ƕ�	
	float width;//��˿��
	float height;//��˸߶�
	float disWithBall=Constant.DIS_WITH_BALL;//�����ĸ���Ե�ľ���
	Bitmap bitmap;//λͼ
	Ball mainBall;//ĸ��
	private boolean showCueFlag=true;//�Ƿ���ʾ��˵ı�־
	private final float angleSpanSlow=0.2f;//΢���ǶȲ���
	private final float angleSpanFast=1f;//�ֵ��ǶȲ���
	private boolean aimFlag=true;	
	private final float lineLength=Table.tableAreaWidth;//��׼�ߵĳ���
	//���ڻ��򶯻�����
	private final  float backSpan=3;//�������˵Ĳ���
	private final float forwardSpan=10;//���ǰ��ʱ�Ĳ���	
	private final float maxDis=50;//�������˵�������
	private float span=backSpan;//����ʱ��˲���
	private boolean showingAnimFlag=false;//�Ƿ����ڲ��Ż��򶯻��ı�־	
	public Cue(Bitmap bitmap,Ball mainBall)
	{
		this.bitmap=bitmap;
		this.mainBall=mainBall;		
		this.width=bitmap.getWidth();
		this.height=bitmap.getHeight();		
	}
	public void drawSelf(Canvas canvas,Paint paint)
	{
		if(!showCueFlag){//���������ʾ��ˣ�ֱ�ӷ���
			return;
		}	
		//���������ת���ĵ�λ��
		this.rotateX=this.width+this.disWithBall+Ball.r;
		this.rotateY=this.height/2;
		//������˵�λ��
		x=mainBall.getX()-width-disWithBall;
		y=mainBall.getY()+Ball.r-height/2;
		//�������
		Matrix m1=new Matrix();//ƽ�ƾ���
		m1.setTranslate(x+Constant.X_OFFSET,y+Constant.Y_OFFSET);
		Matrix m2=new Matrix();//��ת����
		m2.setRotate(angdeg, rotateX, rotateY);
		Matrix mz=new Matrix();//�ۺϾ���
		mz.setConcat(m1, m2);					
		canvas.drawBitmap(bitmap, mz,paint);//�������
		//������׼��
		canvas.save();//���滭��
		canvas.clipRect(Table.lkx+Constant.X_OFFSET, Table.ady+Constant.Y_OFFSET, Table.efx+Constant.X_OFFSET, Table.jgy+Constant.Y_OFFSET);//������������		
		float angrad=(float) Math.toRadians(angdeg);//���Ƕ�ת���ɻ���
		float startX=(float) (mainBall.getX()+Constant.X_OFFSET+Ball.r+Ball.r*Math.cos(angrad));//��׼���������
		float startY=(float) (mainBall.getY()+Constant.Y_OFFSET+Ball.r+Ball.r*Math.sin(angrad));
		float stopX=startX+(float)(lineLength*Math.cos(angrad));//��׼���յ�����
		float stopY=startY+(float)(lineLength*Math.sin(angrad));
		paint.setColor(Color.YELLOW);//����Ϊ��ɫ
		paint.setAlpha(240);//���û��ʲ�͸����
		canvas.drawLine(startX, startY, stopX, stopY, paint);//������׼��
		canvas.restore();//�ָ�����
		paint.setAlpha(255);//�ָ�����͸������
	}
	//���ݴ�����Ļ��λ�ü��������ת�Ƕȵķ���
	public void calcuAngle(float pressX,float pressY)
	{
		//��˵ķ�������
		float dirX=pressX-(mainBall.getX()+Ball.r+Constant.X_OFFSET);
		float dirY=pressY-(mainBall.getY()+Ball.r+Constant.Y_OFFSET);
		if(!aimFlag){//������ǰ���Ŀ��ȷ����ת�Ƕȣ������������÷�
			dirX = -dirX;
			dirY = -dirY;
		}
		//������˵ķ���������Ӧ��ת�ĽǶ�
		if(dirY>=0)
		{
			angdeg=(float) Math.toDegrees((Math.atan(-dirX/dirY)+Math.PI/2));
		}
		else if(dirY<0)
		{
			angdeg=(float) Math.toDegrees((Math.atan(-dirX/dirY)+Math.PI*3/2));
		}
	}
	//��ʱ�뷽��΢���ķ���
	public void rotateLeftSlowly(){
		angdeg+=angleSpanSlow;
	}
	//˳ʱ�뷽��΢���ķ���
	public void rotateRightSlowly(){
		angdeg-=angleSpanSlow;
	}
	//��ʱ�뷽��ֵ��ķ���
	public void rotateLeftFast(){
		angdeg+=angleSpanFast;
	}
	//˳ʱ�뷽��ֵ��ķ���
	public void rotateRightFast(){
		angdeg-=angleSpanFast;
	}
	//�ı�disWithBall��ֵ�������Ż��򶯻��ķ���
	public float changeDisWithBall()
	{
		//�����ĸ��ľ��볬����Χ��ʹ���ǰ��
		if(disWithBall>=maxDis){
			span=-forwardSpan;
		}
		disWithBall+=span;
		return disWithBall;
	}
	//�ָ����򶯻��ĳ�ʼֵ�ķ���
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
