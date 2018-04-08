package com.lqf.rili;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

public class Calendar extends CalendarParent{

	//elements ���ڱ���๦�����������е�����Ԫ��
	private ArrayList<CalendarElement> elements = new ArrayList<CalendarElement>();
	
	//��д���캯��
	public Calendar(Activity activity, View view) {
		super(activity, view);
		//������Ƶ�����Ԫ��
		elements.add(new Border(activity, view));
		elements.add(new Grid(activity, view));
		elements.add(new Week(activity, view));
	}
	//����CalendarElement��draw����
	public void draw(Canvas canvas)
	{
	 //��draw ������ͨ��ɨ��elements�����������������Ԫ�صĶ���
	 //������draw ����������Щ����Ԫ��
		for(CalendarElement ce: elements)
		{
			ce.draw(canvas);
		}
		super.draw(canvas);
	}
}
