package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
//绘制星期文本Week继承CalendarParent类
public class Week extends CalendarParent{
	//定义一个星期数组
	private String[] weekNames = new String[]
			{ "日", "一", "二", "三", "四", "五", "六" };
	//定义一个星期名字颜色变量
	private int weekNameColor;
	//重写构造函数
	public Week(Activity activity, View view) {
		super(activity, view);
		weekNameColor = Color.WHITE;//得到颜色
		paint.setTextSize(weekNameSize);//得到字体大小
	}
	//调用CalendarElement的draw方法
	public void draw(Canvas canvas)
	{
		float left = borderMargin;//获取左边距的值
		float top = borderMargin;//获取上边距的值
		//获取每一个的宽度
		float everyWeekWidth = (view.getMeasuredWidth() - borderMargin * 2) / 7;
		//设置字型为默认
		paint.setFakeBoldText(true);
		//判断
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
