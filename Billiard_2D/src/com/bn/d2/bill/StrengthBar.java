package com.bn.d2.bill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class StrengthBar {
	//����������ͼ����
	private Bitmap downBmp;//λͼ
	private Bitmap pointerBmp;
	final float x=Constant.BAR_X+Constant.X_OFFSET;//��������ʵ��λ��
	final float y=Constant.BAR_Y+Constant.Y_OFFSET;	
	private float width;//���������
	private float height;//�������ܸ߶�		
	private float currHeight;//��������ǰ�߶�
	private int extendX=50;//���ⰴť�����������ĳߴ�
	private int extendY=10;//���ⰴť�����������ĳߴ�
	//���ڲʺ�������
	private float rainbowWidth=Constant.RAINBOW_WIDTH;//�ʺ������
	private float rainbowHeight=Constant.RAINBOW_HEIGHT;//�ʺ����߶�
	private float rainbowGap=Constant.RAINBOW_GAP;//�ʺ�����϶
	private float rainbowX;//��һ���ʺ�����λ��
	private float rainbowY;
	//����ָʾ�����
	private float pointerWidth;
	private float pointerHeight;
	public StrengthBar(Bitmap downBmp,Bitmap pointerBmp)
	{
		this.downBmp=downBmp;
		this.pointerBmp=pointerBmp;
		this.width=downBmp.getWidth();
		this.height=downBmp.getHeight();
		currHeight=downBmp.getHeight();
		rainbowX=Constant.RAINBOW_X+Constant.X_OFFSET;;
		rainbowY=height+Constant.RAINBOW_Y+Constant.Y_OFFSET;
		pointerWidth=pointerBmp.getWidth();
		pointerHeight=pointerBmp.getHeight();
	}
	//�����������ķ���
	public void drawSelf(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(downBmp, x, y,paint);//���Ƶ��µ���
		
		int n=(int) (currHeight/(rainbowHeight+rainbowGap));//��ʺ�������
		n=(n>ColorUtil.result.length)? ColorUtil.result.length :n;//ѡn�����鳤������С��ֵΪn��ֵ
		//���Ʋʺ���
		for(int i=0;i<n && i<ColorUtil.result.length;i++)
		{
			int[] c=ColorUtil.getColor(i);
			paint.setARGB(255, c[0], c[1], c[2]);
			float yTemp=rainbowY-(i*(rainbowHeight+rainbowGap));
			canvas.drawRect(rainbowX, yTemp, rainbowX+rainbowWidth, yTemp+rainbowHeight, paint);
		}
		//��������ָʾ��
		canvas.drawBitmap(pointerBmp, x-pointerWidth, rainbowY-((n-1)*(rainbowHeight+rainbowGap))-pointerHeight/2,paint);//���Ƶ��µ���
	}
	//�������ȵķ���
	public void changeCurrHeight(float pressX, float pressY)
	{
		if(pressY<=y){//���µ�λ��������������
			currHeight=this.height;
		}
		else if(pressY>=y+this.height){//���µ�λ��������������
			currHeight=0;
		}
		else{//���µ�λ�����������м�
			currHeight=this.height-(pressY-y);
		}
	}
	//�жϰ�ť�Ƿ��д����¼��ķ���
	public boolean isActionOnBar(float pressX,float pressY)
	{
		return Constant.isPointInRect(pressX, pressY, 
				x-extendX, y-extendY, width+2*extendX, height+2*extendY);
	}
	public float getCurrHeight() {
		return currHeight;
	}
	public float getHeight() {
		return height;
	}
	
}
