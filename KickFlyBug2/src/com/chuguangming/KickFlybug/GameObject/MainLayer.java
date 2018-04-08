package com.chuguangming.KickFlybug.GameObject;

import com.chuguangming.R;
import com.chuguangming.KickFlybug.Until.ActivityUtil;
import com.chuguangming.KickFlybug.Until.AudioUtil;
import com.chuguangming.KickFlybug.Until.GameObjData;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class MainLayer extends Layer
{

	private static final int LIVE_FLY_COUNT = 50; // 屏幕中最多存活的苍蝇数量

	private long lastUpdate;

	private Paint p = new Paint();

	public MainLayer(int x, int y, int w, int h)
	{
		super(x, y, w, h);
		init();
	}

	@Override
	public void init()
	{
		//生成苍蝇
		for (int i = 0; i < LIVE_FLY_COUNT; i++)
		{
			addGameObj(new Fly());
		}
		p.setColor(Color.BLACK);
		p.setAntiAlias(true);
		p.setTextSize(25);
		p.setAlpha(50);
	}

	@Override
	public void logic()
	{
		synchronized (objs)
		{
			for (BaseGameObj obj : objs)
			{
				obj.logic();
				for (BaseGameObj obj2 : objs)
				{
					Fly f = (Fly) obj;
					if (!f.equals(obj2) && !f.dead)
					{
						f.collisionTo((Fly) obj2);
					}
				}
			}
		}
		//如果当前时间与上次更新时间超过1秒那么累加
		long now = System.currentTimeMillis();
		if (now - lastUpdate >= 1000)
		{
			GameObjData.CURRENT_USE_TIME++;
			lastUpdate = now;
		}
	}

	@Override
	public void paint(Canvas c)
	{
		super.paint(c);
		c.drawText("杀死数量: " + GameObjData.CURRENT_KILL_COUNT, 0, 25, p);
		c.drawText("用时: " + GameObjData.CURRENT_USE_TIME, 0, 60, p);
	}

	public void onTouch(MotionEvent event)
	{
		int x = (int) event.getX();
		int y = (int) event.getY();

		// 播放声音
		AudioUtil.PlayMusic(ActivityUtil.myContext, R.raw.musickill);
		//ActivityUtil.PlaySoundPool(ActivityUtil.myContext,  R.raw.music3);
		// 震动一下
		// ActivityUtil.getVibrator(ActivityUtil.myContext);
		synchronized (objs)
		{
			for (BaseGameObj obj : objs)
			{
				Fly f = (Fly) obj;
				if (!f.dead && f.contains(x, y))
				{
					f.dead = true;
					GameObjData.CURRENT_KILL_COUNT++;
					if (objs.size() < GameObjData.OVER_KILL_COUNT)
					{
						objs.add(new Fly());
					}
					return;
				}
			}
		}
	}
}
