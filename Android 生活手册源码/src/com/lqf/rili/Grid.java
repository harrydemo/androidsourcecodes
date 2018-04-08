package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

//������������Grid�̳�CalendarParent��
public class Grid extends CalendarParent {
	// �������ϱ߾����
	private float left;
	private float top;

//	private String[] days = new String[42];
//	public int currentYear, currentMonth;
//	public int currentDay = -1, currentDay1 = -1, currentDayIndex = -1;
//	private java.util.Calendar calendar = java.util.Calendar.getInstance();
//	private int Month;
//	private int current;

	// ��д���캯��
	public Grid(Activity activity, View view) {
		super(activity, view);
//		currentYear = calendar.get(calendar.YEAR);
//		currentMonth = calendar.get(calendar.MONTH);

	}

	// ����CalendarElement��draw����
	public void draw(Canvas canvas) {
		left = borderMargin;// ��ȡ��߾��ֵ
		// ��ȡ�ϱ߾��ֵ
		top = borderMargin + weekNameSize + weekNameMargin * 2 + 4;
		float calendarWidth = view.getMeasuredWidth() - left * 2;
		float calendarHeight = view.getMeasuredHeight() - top - borderMargin;
		float cellWidth = calendarWidth / 7;
		float cellHeight = calendarHeight / 6;
		// ��ɫȡֵ
		paint.setColor(0xFFFFFFFF);
		// ��������
		canvas.drawLine(left, top, left + view.getMeasuredWidth()
				- borderMargin * 2, top, paint);
		// ��ɫȡֵ
		paint.setColor(0xFF666666);
		// ���ƺ���ֱ��
		for (int i = 1; i < 6; i++) {
			canvas.drawLine(left, top + cellHeight * i, left + calendarWidth,
					top + cellHeight * i, paint);
		}
		// ��������ֱ��
		for (int i = 1; i < 7; i++) {
			canvas.drawLine(left + cellWidth * i, top, left + cellWidth * i,
					view.getMeasuredHeight() - borderMargin, paint);
		}
	}

	// �÷����������ָ���·ݵ�����
//	private int getMonthDays(int year, int month) {
//		month++;
//		switch (month) {
//		case 1:
//		case 3:
//		case 5:
//		case 7:
//		case 8:
//		case 10:
//		case 12: {
//			return 31;
//		}
//		case 4:
//		case 6:
//		case 9:
//		case 11: {
//			return 30;
//		}
//		case 2: {
//			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
//				return 29;
//			else
//				return 28;
//		}
//		}
//		return 0;
//	}
//	//��Ҫ��������ǰ�µ�����,�Լ���һ�º���һ�����ڱ��³��ͱ���ĩ������
//	private void calculateDays()
//	{
//	// ����ǰ������Ϊָ���·ݵĵ�һ��
//	calendar.set(currentYear, currentMonth, 1);
//	// ���ָ���·ݵĵ�1 ���ǵ�ǰ�ܵĵڼ���
//	int week = calendar.get(calendar.DAY_OF_WEEK);
//	int monthDays = 0;
//	int prevMonthDays = 0;
//	// ��õ�ǰ���ж�����
//	monthDays = getMonthDays(currentYear, currentMonth);
//	// �����ǰ����һ���еĵ�һ���£�������һ�����һ��
//	//��Ҳ����12 �£�������
//	if (currentMonth == 0)
//	prevMonthDays = getMonthDays(currentYear - 1, 11);
//	// ���򣬻��ָ������һ�µ�����
//	else
//	prevMonthDays = getMonthDays (currentYear, current-
//	Month - 1);
//	for (int i = week, day = prevMonthDays; i > 1; i--, day--)
//	{
//	days[i - 2] = "*" + String.valueOf(day);
//	}
//	// ����ָ���£��������ǵ�ǰ�£�������
//	for (int day = 1, i = week - 1; day <= monthDays; day++,
//	i++)
//	{
//	days[i] = String.valueOf(day);
//	if (day == currentDay)
//	{
//	// ��õ�ǰ����days �����е�����
//	currentDayIndex = i;
//	}}
//	// ������һ����ʾ�ڱ����������������
//	for (int i = week + monthDays - 1, day = 1; i < days.length;
//	i++, day++)
//	{
//	days[i] = "*" + String.valueOf(day);
//	}}

}
