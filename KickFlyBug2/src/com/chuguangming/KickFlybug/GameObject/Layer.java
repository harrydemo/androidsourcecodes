package com.chuguangming.KickFlybug.GameObject;

import java.util.Vector;

import android.graphics.Canvas;

/**
 * 抽象层
 * 
 */
public abstract class Layer extends BaseGameObj
{

	protected Vector<BaseGameObj> objs = new Vector<BaseGameObj>();

	public Layer()
	{
		super();
	}

	public Layer(int x, int y, int w, int h)
	{
		super(x, y, w, h);
	}

	public Layer(int x, int y)
	{
		super(x, y);
	}

	@Override
	public void init()
	{

	}

	@Override
	public void logic()
	{

	}

	@Override
	public void paint(Canvas c)
	{
		synchronized (objs)
		{
			int count = objs.size();
			for (int i = 0; i < count; i++)
			{
				objs.get(i).paint(c);
			}
		}
	}

	/**
	 * 添加一个游戏对象到该层
	 * 
	 * @param obj
	 */
	public void addGameObj(BaseGameObj obj)
	{
		objs.add(obj);
	}
}
