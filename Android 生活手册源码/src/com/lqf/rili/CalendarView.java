package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

//�ٻ�����������CalendarView����̳�View��
public class CalendarView extends View{
	//����һ��Activity����
	private Activity activity;
	//����Calendar����
	public Calendar calendar;
	
	//��д���췽��
	public CalendarView(Activity activity) {
		super(activity);
		//��ȡ������
		this.activity = activity;
		//��ȡʵ��������
		calendar = new Calendar(activity,this);
	}
	//һ������ͼ�εķ���
	protected void onDraw(Canvas canvas) {
		//��������
		calendar.draw(canvas);

	}
}
