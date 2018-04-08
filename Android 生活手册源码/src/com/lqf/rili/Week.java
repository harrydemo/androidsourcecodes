package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
//���������ı�Week�̳�CalendarParent��
public class Week extends CalendarParent{
	//����һ����������
	private String[] weekNames = new String[]
			{ "��", "һ", "��", "��", "��", "��", "��" };
	//����һ������������ɫ����
	private int weekNameColor;
	//��д���캯��
	public Week(Activity activity, View view) {
		super(activity, view);
		weekNameColor = Color.WHITE;//�õ���ɫ
		paint.setTextSize(weekNameSize);//�õ������С
	}
	//����CalendarElement��draw����
	public void draw(Canvas canvas)
	{
		float left = borderMargin;//��ȡ��߾��ֵ
		float top = borderMargin;//��ȡ�ϱ߾��ֵ
		//��ȡÿһ���Ŀ��
		float everyWeekWidth = (view.getMeasuredWidth() - borderMargin * 2) / 7;
		//��������ΪĬ��
		paint.setFakeBoldText(true);
		//�ж�
		for (int i = 0; i < weekNames.length; i++)
		{
			if (i == 0 || i == weekNames.length - 1)
			{
				paint.setColor(sundaySaturdayColor);
			}
			else
			{
				paint.setColor(weekNameColor);
			}
			left = borderMargin + everyWeekWidth * i
					+ (everyWeekWidth - paint.measureText(weekNames[i])) / 2;
			canvas.drawText(weekNames[i], left, top + paint.getTextSize()
					+ weekNameMargin, paint);
		}
		super.draw(canvas);
	}
}
