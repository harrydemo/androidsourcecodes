package com.chuguangming.KickFlybug.Until;

import com.chuguangming.KickFlybug.View.GameView;

/**
 * 游戏数据
 */
public final class GameObjData
{

	public static final int MODE_100S = 0;// 计时模式
	public static final int MODE_100C = 1;// 计数模式

	public static final int OVER_KILL_COUNT = 30;// 计数模式下杀死苍蝇数量大于该值则游戏结束
	public static final int OVER_USE_TIME = 30;// 计时模式下用时超过该值则游戏结束

	public static int CURRENT_GAME_MODE = 0;// 记录当前游戏的模式
	public static int CURRENT_GAME_STATE = 0;// 记录当前游戏状态

	public static int CURRENT_KILL_COUNT = 0;// 当前杀死苍蝇数
	public static int CURRENT_USE_TIME = 0;// 当前用时

	/**
	 * 检测游戏是否可以结束
	 */
	public static void checkGameOver()
	{
		switch (CURRENT_GAME_MODE)
		{
		case MODE_100C:
			if (CURRENT_KILL_COUNT >= OVER_KILL_COUNT)
			{
				CURRENT_GAME_STATE = GameView.GAME_STATE_OVER;
			} else
			{
				CURRENT_GAME_STATE = GameView.GAME_STATE_RUN;
			}
			break;
		case MODE_100S:
			if (CURRENT_USE_TIME >= OVER_USE_TIME)
			{
				CURRENT_GAME_STATE = GameView.GAME_STATE_OVER;
			} else
			{
				CURRENT_GAME_STATE = GameView.GAME_STATE_RUN;
			}
			break;
		}
	}

	/**
	 * 清除游戏数据
	 */
	public static void clear()
	{
		CURRENT_KILL_COUNT = 0;
		CURRENT_USE_TIME = 0;
	}
}
