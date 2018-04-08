package com.bn.d2.bill;

enum ScreenOrien
{
	HP,  //��ʾ������ö��ֵ
	SP   //��ʾ������ö��ֵ
}

//���ż���Ľ��
public class ScreenScaleResult
{
	int lucX;//���Ͻ�X����
	int lucY;//���Ͻ�y����
	float ratio;//���ű���
	ScreenOrien so;//���������	
	
	public ScreenScaleResult(int lucX,int lucY,float ratio,ScreenOrien so)
	{
		this.lucX=lucX;
		this.lucY=lucY;
		this.ratio=ratio;
		this.so=so;
	}
	
	public String toString()
	{
		return "lucX="+lucX+", lucY="+lucY+", ratio="+ratio+", "+so;
	}
}