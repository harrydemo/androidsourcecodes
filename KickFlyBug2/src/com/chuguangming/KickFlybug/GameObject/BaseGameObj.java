package com.chuguangming.KickFlybug.GameObject;

import android.graphics.Canvas;

/**
 * 游戏基础绘制对象
 */
public abstract class BaseGameObj
{
	//基本的left top right bottom
	public int x;
	public int y;
	public int w;
	public int h;
	//是否可见
	public boolean visiable = true;
	//基本构造函数
	public BaseGameObj(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	//基本构造函数
	public BaseGameObj(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	//基本构造函数
	public BaseGameObj()
	{

	}

	/**
	 * 绘制此对象
	 * 
	 * @param c
	 */
	public abstract void paint(Canvas c);

	/**
	 * 初始化对象数据
	 */
	public abstract void init();

	/**
	 * 逻辑处理操作
	 */
	public abstract void logic();
}
