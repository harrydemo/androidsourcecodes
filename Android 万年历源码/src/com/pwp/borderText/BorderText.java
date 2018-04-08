package com.pwp.borderText;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;

import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 实现带下边框的textView
 * 
 * @author jack_peng
 * 
 */
public class BorderText extends TextView {

	public BorderText(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 实例化一支画笔
		Paint paint = new Paint();
		// 设置所绘制的边框颜色为黑色
		paint.setColor(android.graphics.Color.BLACK);
		// 绘制上边框
		//canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
		// 绘制左边框
		//canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
		// 绘制右边框
		//canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
		//this.getHeight() - 1, paint);
		// 绘制下边框
		canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this
				.getHeight() - 1, paint);

	}

}
