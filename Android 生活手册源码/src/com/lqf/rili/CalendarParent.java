package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
/*
 * ����һ����,�̳�CalendarElement�ӿ�
 */
public class CalendarParent implements CalendarElement{
	protected Activity activity;//����һ��Activity����
    protected View view;//����һ��View����
    protected Paint paint = new Paint();//����һ������Paint
    protected float borderMargin;//����߿����߾�
    protected float weekNameMargin;//�����������ֵ���߾�
    protected float weekNameSize;//�����������ֵ������С
    protected int sundaySaturdayColor;//����������������������ɫ
    //����CalendarElement��draw����
	public void draw(Canvas canvas) {
		
	}
	//��д���캯��
    public CalendarParent(Activity activity,View view) {
    	this.activity = activity;//��ȡ�
    	this.view = view;//��ȡ��ͼ
    	borderMargin = 10;//�����߿���߾�
    	weekNameMargin = 5;//��������������߾�
    	weekNameSize = 18;//�����������������С
    	sundaySaturdayColor = 0xFFFF0000;//����������������������ɫ
	}
}
