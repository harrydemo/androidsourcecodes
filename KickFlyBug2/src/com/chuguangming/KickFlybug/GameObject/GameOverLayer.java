package com.chuguangming.KickFlybug.GameObject;

import com.chuguangming.KickFly;
import com.chuguangming.KickFlybug.Until.ActivityUtil;
import com.chuguangming.KickFlybug.Until.GameObjData;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 游戏结束绘制用的层
 */
public class GameOverLayer extends Layer
{
	//结果
	private String result = "";
	private Paint p = new Paint();
	public boolean appearFull;
	private int appearX = ActivityUtil.SCREEN_WIDTH;

	public GameOverLayer(int x, int y, int w, int h)
	{
		super(x, y, w, h);
	    //初始化画笔
		init();
	}
	
	@Override
	public void init()
	{
		p.setColor(Color.BLACK);
		p.setAlpha(150);
		p.setAntiAlias(true);
		p.setTextSize(50);
	}

	@Override
	public void logic()
	{
		int gameMode = GameObjData.CURRENT_GAME_MODE;
		switch (gameMode)
		{
		case GameObjData.MODE_100C:
			result = "杀死" + GameObjData.OVER_KILL_COUNT + "只苍蝇用时"
					+ GameObjData.CURRENT_USE_TIME + "秒";
			break;
		case GameObjData.MODE_100S:
			result = GameObjData.OVER_USE_TIME + "秒内杀死了"
					+ GameObjData.CURRENT_KILL_COUNT + "只苍蝇";
			break;
		}
		if (appearX <= x)
		{
			appearFull = true;
		} else
		{
			appearX -= 50;
		}
	}

	@Override
	public void paint(Canvas c)
	{
		c.drawRect(appearX, y, appearX + w, y + h, p);
		c.drawText("Game Over", appearX, ActivityUtil.SCREEN_HEIGHT/2, p);
		c.drawText(result, appearX, y + (ActivityUtil.SCREEN_HEIGHT/2)+50, p);
	}
}
