package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

//①绘制日历网格CalendarView必须继承View类
public class CalendarView extends View{
	//定义一个Activity变量
	private Activity activity;
	//定义Calendar对象
	public Calendar calendar;
	
	//重写构造方法
	public CalendarView(Activity activity) {
		super(activity);
		//获取本界面
		this.activity = activity;
		//获取实例化对象
		calendar = new Calendar(activity,this);
	}
	//一个绘制图形的方法
	protected void onDraw(Canvas canvas) {
		//绘制日历
		calendar.draw(canvas);

	}
}
