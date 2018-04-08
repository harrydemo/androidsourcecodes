package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

//���������߿�Border�̳�CalendarParent��
public class Border extends CalendarParent {
	// ��д���캯��
	public Border(Activity activity, View view) {
		super(activity, view);
		// ע�⣬һ��Ҫ4 ���ֽڵ���ɫֵ������һ��͸��ɫ
		paint.setColor(0xFFFFFFFF);
	}

	// ����CalendarElement��draw����
	public void draw(Canvas canvas) {
		float left = borderMargin;// ����߾�
		float top = borderMargin;// ����߾�
		float right = view.getMeasuredWidth() - left;// �ұ߾�
		float bottom = view.getMeasuredHeight() - top;// �±߾�
		// ��������
		canvas.drawLine(left, top, right, top, paint);//�����ϱ߾���
		canvas.drawLine(right, top, right, bottom, paint);//�����ұ߾���
		canvas.drawLine(right, bottom, left, bottom, paint);//�����±߾���
		canvas.drawLine(left, bottom, left, top, paint);//������߾���
	}
}
