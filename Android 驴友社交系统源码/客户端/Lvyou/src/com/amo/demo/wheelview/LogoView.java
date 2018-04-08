/**
 *  Copyright 2011 ChinaSoft International Ltd. All rights reserved.
 */

package com.amo.demo.wheelview;




import com.ly.control.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>
 * Title: LogoView
 * </p>
 * <p>
 * Description: 用于显示渐变Logo的自定义视图
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */
public class LogoView extends View {
	/**
	 * 当前图片透明度，0为纯透明，255为不透明
	 * */
	private int alpha = 0;

	/**
	 * 构造方法
	 * */
	public LogoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 构造方法
	 * */
	public LogoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 应用软件的LOGO图像
	 * */
	private Bitmap logo;

	/**
	 * 重绘视图的方法
	 * 
	 * @param alpha
	 *            本次重绘时，LOGO图片的透明度取值
	 * */
	public void repaint(int alpha) {
		this.alpha = alpha;
		//System.out.println("111111111111");
		// 抛出重绘事件
		invalidate();
		
	}

	/**
	 * 构造方法
	 * */
	public LogoView(Context context) {
		super(context);
		// 加载应用软件LOGO图片
		logo = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.welcome2);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
        //System.out.println("22222222");
		// 清屏
		canvas.drawColor(Color.WHITE);
		// 创建画笔对象
		Paint p = new Paint();
		// 设置画笔对象的透明度为当前透明度取值
		p.setAlpha(alpha);
		// 在视图上利用画笔绘制Logo图像
		canvas.drawBitmap(logo, 0.0f, 0.0f, p);
	}

}
