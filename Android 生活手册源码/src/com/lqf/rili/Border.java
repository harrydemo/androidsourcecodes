package com.lqf.rili;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

//绘制日历边框Border继承CalendarParent类
public class Border extends CalendarParent {
	// 重写构造函数
	public Border(Activity activity, View view) {
		super(activity, view);
		// 注意，一定要4 个字节的颜色值，包括一个透明色
		paint.setColor(0xFFFFFFFF);
	}

	// 调用CalendarElement的draw方法
	public void draw(Canvas canvas) {
		float left = borderMargin;// 左外边距
		float top = borderMargin;// 上外边距
		float right = view.getMeasuredWidth() - left;// 右边距
		float bottom = view.getMeasuredHeight() - top;// 下边距
		// 绘制虚线
		canvas.drawLine(left, top, right, top, paint);//绘制上边距线
		canvas.drawLine(right, top, right, bottom, paint);//绘制右边距线
		canvas.drawLine(right, bottom, left, bottom, paint);//绘制下边距线
		canvas.drawLine(left, bottom, left, top, paint);//绘制左边距线
	}
}
