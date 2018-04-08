package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
/*
 * 绘制一个表单,继承CalendarElement接口
 */
public class CalendarParent implements CalendarElement{
	protected Activity activity;//定义一个Activity变量
    protected View view;//定义一个View变量
    protected Paint paint = new Paint();//定义一个画笔Paint
    protected float borderMargin;//定义边框的外边距
    protected float weekNameMargin;//定义星期名字的外边距
    protected float weekNameSize;//定义星期名字的字体大小
    protected int sundaySaturdayColor;//定义星期日与星期六的颜色
    //调用CalendarElement的draw方法
	public void draw(Canvas canvas) {
		
	}
	//重写构造函数
    public CalendarParent(Activity activity,View view) {
    	this.activity = activity;//获取活动
    	this.view = view;//获取视图
    	borderMargin = 10;//给定边框外边距
    	weekNameMargin = 5;//给定星期名字外边距
    	weekNameSize = 18;//给定星期名字字体大小
    	sundaySaturdayColor = 0xFFFF0000;//给定星期日与星期六的颜色
	}
}
