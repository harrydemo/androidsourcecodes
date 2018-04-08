package com.lqf.rili;

import android.graphics.Canvas;



/*②
 * 要绘制的日历分成若干块,而每一块都需要有同样的接口,以便统一绘制它们.
 * 因此,这些块都要实现一个CalendarElement接口。
 * 在CalendarElement 接口中有一个draw方法
 * public void draw(Canvas canvas);
 */
public interface CalendarElement {
	//公开的绘画方法
	public void draw(Canvas canvas);
}
