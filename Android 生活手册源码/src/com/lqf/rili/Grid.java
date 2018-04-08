package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

//绘制日历网格Grid继承CalendarParent类
public class Grid extends CalendarParent {
	// 定义左上边距变量
	private float left;
	private float top;

//	private String[] days = new String[42];
//	public int currentYear, currentMonth;
//	public int currentDay = -1, currentDay1 = -1, currentDayIndex = -1;
//	private java.util.Calendar calendar = java.util.Calendar.getInstance();
//	private int Month;
//	private int current;

	// 重写构造函数
	public Grid(Activity activity, View view) {
		super(activity, view);
//		currentYear = calendar.get(calendar.YEAR);
//		currentMonth = calendar.get(calendar.MONTH);

	}

	// 调用CalendarElement的draw方法
	public void draw(Canvas canvas) {
		left = borderMargin;// 获取左边距的值
		// 获取上边距的值
		top = borderMargin + weekNameSize + weekNameMargin * 2 + 4;
		float calendarWidth = view.getMeasuredWidth() - left * 2;
		float calendarHeight = view.getMeasuredHeight() - top - borderMargin;
		float cellWidth = calendarWidth / 7;
		float cellHeight = calendarHeight / 6;
		// 颜色取值
		paint.setColor(0xFFFFFFFF);
		// 绘制虚线
		canvas.drawLine(left, top, left + view.getMeasuredWidth()
				- borderMargin * 2, top, paint);
		// 颜色取值
		paint.setColor(0xFF666666);
		// 绘制横向直线
		for (int i = 1; i < 6; i++) {
			canvas.drawLine(left, top + cellHeight * i, left + calendarWidth,
					top + cellHeight * i, paint);
		}
		// 绘制纵向直线
		for (int i = 1; i < 7; i++) {
			canvas.drawLine(left + cellWidth * i, top, left + cellWidth * i,
					view.getMeasuredHeight() - borderMargin, paint);
		}
	}

	// 该方法用来获得指定月份的天数
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
//	//需要基础出当前月的天数,以及上一月和下一月落在本月初和本月末的天数
//	private void calculateDays()
//	{
//	// 将当前日历设为指定月份的第一天
//	calendar.set(currentYear, currentMonth, 1);
//	// 获得指定月份的第1 天是当前周的第几天
//	int week = calendar.get(calendar.DAY_OF_WEEK);
//	int monthDays = 0;
//	int prevMonthDays = 0;
//	// 获得当前月有多少天
//	monthDays = getMonthDays(currentYear, currentMonth);
//	// 如果当前月是一年中的第一个月，则获得上一年最后一月
//	//（也就是12 月）的天数
//	if (currentMonth == 0)
//	prevMonthDays = getMonthDays(currentYear - 1, 11);
//	// 否则，获得指定月上一月的天数
//	else
//	prevMonthDays = getMonthDays (currentYear, current-
//	Month - 1);
//	for (int i = week, day = prevMonthDays; i > 1; i--, day--)
//	{
//	days[i - 2] = "*" + String.valueOf(day);
//	}
//	// 设置指定月（在这里是当前月）的天数
//	for (int day = 1, i = week - 1; day <= monthDays; day++,
//	i++)
//	{
//	days[i] = String.valueOf(day);
//	if (day == currentDay)
//	{
//	// 获得当前日在days 数组中的索引
//	currentDayIndex = i;
//	}}
//	// 设置下一月显示在本月日历后面的天数
//	for (int i = week + monthDays - 1, day = 1; i < days.length;
//	i++, day++)
//	{
//	days[i] = "*" + String.valueOf(day);
//	}}

}
