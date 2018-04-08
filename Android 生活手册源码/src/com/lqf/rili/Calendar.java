package com.lqf.rili;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

public class Calendar extends CalendarParent{

	//elements 用于保存多功能日历中所有的日历元素
	private ArrayList<CalendarElement> elements = new ArrayList<CalendarElement>();
	
	//重写构造函数
	public Calendar(Activity activity, View view) {
		super(activity, view);
		//保存绘制的日历元素
		elements.add(new Border(activity, view));
		elements.add(new Grid(activity, view));
		elements.add(new Week(activity, view));
	}
	//调用CalendarElement的draw方法
	public void draw(Canvas canvas)
	{
	 //在draw 方法中通过扫描elements变量来获得所有日历元素的对象
	 //并调用draw 方法绘制这些日历元素
		for(CalendarElement ce: elements)
		{
			ce.draw(canvas);
		}
		super.draw(canvas);
	}
}
