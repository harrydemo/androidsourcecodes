package com.android.GeneralDesign;

public class StringsProvider
{
	public static class LanguageID
	{
		public static final int CHINESE_SIMPLE = 0;
		public static final int ENGLISH = 1;
	}
	public static class UserNotifyMsgID
	{
		public static final int DATA_SAVEING = 				0;
		public static final int DATA_SAVE_SUCCESS = 		1;
		public static final int DATA_SAVE_FAIL = 			2;
		public static final int DATA_CHECK_OK = 			3;
		public static final int ERROR_NO_MP = 				4;
		public static final int ERROR_MP_POS_WRONG = 		5;
		public static final int ERROR_NO_B0X = 				6;
		public static final int ERROR_BOX_NUM_NOT_MATCH = 	7;
		public static final int ERROR_NO_MISSION = 			8;
		public static final int LAB_GAME_STATE_NO_LEFT	= 	9;
		public static final int LAB_GAME_STATE_NO_RIGHT	= 	10;
		public static final int LAB_STEP_INDEX = 			11;
		public static final int MSG_USER_GAME = 			12;
	}
	public static String getHelpViewMsg(int languageId)
	{
		languageId %= mGameHelpViewMsg[0].length();
		return mGameHelpViewMsg[languageId];
	}
	public static String getAboutViewMsg(int languageId)
	{
		languageId %= mGameAboutViewMsg[0].length();
		return mGameAboutViewMsg[languageId];
	}
	public static String[][] getMenuItemStrings(int menuId, int languageId)
	{
		menuId %= mMenuTitleStrings[0].length;
		languageId = languageId % mMenuTitleStrings.length;
		return mMenuTitleStrings[languageId][menuId];
	}
	public static String getUserNotifyMsg(int textId, int languageId)
	{
		textId %= mUserNotifyMsg[0].length;
		languageId = languageId % mUserNotifyMsg.length;
		return mUserNotifyMsg[languageId][textId];
	}
	private static String[] mGameHelpViewMsg =
	{
		/* CHINESE_SIMPLE */
		"运行游戏：\n" +
		"        移动小乌龟将地图上的所有箱子推到指定位置后，" +
		"游戏自动切换至下一关。\n" +
		"        游戏过程中可以按数字键或轻触屏幕上方工具栏图" +
		"标进行撤消、恢复、关卡重置、关卡选择等操作。\n\n" +
		"编辑游戏：\n" +
		"        游戏编辑模式下，您可以编辑设计游戏关卡，与他人" +
		"分享自己的游戏创意，体验不一样的游戏乐趣。\n" +
		"        编辑完成后选择\"保存\"图标保存编辑，您可以从主菜单" +
		"选择\"载入自定义游戏\"命令来运行您的游戏。",

		/* ENGLISH */
		"Game Running: move main player and push all" +
		" the box to certian position"
	};
	private static String[] mGameAboutViewMsg =
	{
		/* STR_ABOUT_VIEW */
		"        联系我们:\n\n" +
		"主页:       http://www.xxxxx.com\n" +
		"Email:     mja3210@163.com\n" +
		"QQ:       31499476",
		
		/* STR_ABOUT_VIEW */
		"Contact to us: \n\n" +
		"Site:   http://www.xxxxx.com\n" +
		"Email:    mja3210@163.com\n" +
		"QQ:    31499476",	
	};
	private static String[][][][] mMenuTitleStrings =
	{
/////////////////////////////////////////////////////////
		/* CHINESE SIMPLE*/
		{
			/* MAIN_MENU */
			{
				/* Title */
				{
					""
				},
				/* Menu Item */
				{
					"开始游戏",
					"载入自定义游戏",
					"编辑自定义游戏",
					"游戏设置",
					"帮助",
					"关于",
					"退出游戏",
				},
			},
			/* GAME_SETTING */
			{
				/* Title */
				{
					"游戏设置"
				},
				/* Menu Item */
				{
					"..\\",
					"系统音效",
					"背景音设置",
					"按键音设置",
					"语言设置",
					"载入默认设置"
				},
			},
			/* SYS_SOUND_EFFECT */
			{
				/* Title */
				{
					"系统音效"
				},
				/* Menu Item */
				{
					"..\\",
					"开",
					"关",
				},
			},
			/* BG_MUSIC_SETTING */
			{
				/* Title */
				{
					"背景音乐设置"
				},
				/* Menu Item */
				{
					"..\\",
					"预设音乐1",
					"预设音乐2",
					"预设音乐3",
					"预设音乐4",
					"关"
				},
			},
			/* KEY_MUSIC_SETTING */
			{
				/* Title */
				{
					"按键音设置"
				},
				/* Menu Item */
				{
					"..\\",
					"按键音1",
					"按键音2",
					"按键音3",
					"按键音4",
					"关"
				},
			},
			/* LANGUAGE_SETTING */
			{
				/* Title */
				{
					"提示语言选择"
				},
				/* Menu Item */
				{
					"..\\",
					"简体中文",
					"English"
				}
			},
			/* LOAD_DEFAULT_SETTING */
			{
				/* Title */
				{
					"载入默认设置?"
				},
				/* Menu Item */
				{
					"否",
					"是",
				}
			},
		},
/////////////////////////////////////////////////////////
		/* ENGLISH */
		{
			/* MAIN_MENU */
			{
				/* Title */
				{
					""
				},
				/* Menu Item */
				{
					"Play",
					"Load User Game",
					"Edit User Game",
					"Option",
					"Help",
					"About",
					"Quit",
				},
			},
			/* GAME_SETTING */
			{
				/* Title */
				{
					"Game Option"
				},
				/* Menu Item */
				{
					"..\\",
					"System Sound Effect",
					"Back Ground Music Setup",
					"Key Board Music Setup",
					"Game OSD Language Setup",
					"Load Default Setting",
				},
			},
			/* SYS_SOUND_EFFECT */
			{
				/* Title */
				{
					"System Sound Effect"
				},
				/* Menu Item */
				{
					"..\\",
					"ON",
					"OFF",
				},
			},
			/* BG_MUSIC_SETTING */
			{
				/* Title */
				{
					"Back Ground Music Setting"
				},
				/* Menu Item */
				{
					"..\\",
					"Music 1",
					"Music 2",
					"Music 3",
					"Music 4",
					"OFF"
				},
			},
			/* KEY_MUSIC_SETTING */
			{
				/* Title */
				{
					"Key Board Sound Effect"
				},
				/* Menu Item */
				{
					"..\\",
					"Music 1",
					"Music 2",
					"Music 3",
					"Music 4",
					"OFF"
				},
			},
			/* LANGUAGE_SETTING */
			{
				/* Title */
				{
					"OSD Language Setting"
				},
				/* Menu Item */
				{
					"..\\",
					"简体中文",
					"English"
				}
			},
			/* LOAD_DEFAULT_SETTING */
			{
				/* Title */
				{
					"Load Default Setting ?",
				},
				/* Menu Item */
				{
					"No",
					"Yes",
				}
			},
		}
	};
	private static String[][] mUserNotifyMsg =
	{
		{
			/* DATA_SAVEING */
			"正在保存数据, 请稍候 ......",
			/* DATA_SAVE_SUCCESS */
			"保存成功",
			/* DATA_SAVE_FAIL */
			"保存失败",
			/* DATA_CHECK_OK */
			"数据较验成功",
			/* ERROR_NO_MP */
			"请为小乌龟指定初始位置",
			/* ERROR_MP_POS_WRONG */
			"小乌龟位置有误，请为小乌龟指定正确的位置",
			/* ERROR_NO_B0X */
			"未放置任何箱子，您需要为游戏指定至少一个箱子",
			/* ERROR_BOX_NUM_NOT_MATCH */
			"箱子与任务数不一致",
			/* ERROR_NO_MISSION */
			"任务数量不能为0",
			/* TAB_GAME_STATE_NO */
			"第  ", "  关",
			/* TAB_STEP_INDEX */
			"步骤: ",
			/* MSG_USER_GAME */
			"用户自定义游戏",
		},
		{
			/* DATA_SAVEING */
			"Saving data, please wait ......",
			/* DATA_SAVE_SUCCESS */
			"Success !",
			/* DATA_SAVE_FAIL */
			"Failed !",
			/* DATA_CHECK_OK */
			"OK",
			/* ERROR_NO_MP */
			"Eerror !\nNo main Player in the game, you must " +
			"place the main player in a right position",
			/* ERROR_MP_POS_WRONG */
			"Error !\nYou have place the the player a incorrect" +
			" position, please check it",
			/* ERROR_NO_B0X */
			"Eerror !\nNo box in the game editor",
			/* ERROR_BOX_NUM_NOT_MATCH */
			"Eerror !\nThe number of box didn't not match to mission",
			/* ERROR_NO_MISSION */
			"Eerror !\nThe number of mission must be more than one",
			/* TAB_GAME_STATE_NO */
			"Level: ", "",
			/* TAB_STEP_INDEX */
			"Step: ",
			/* MSG_USER_GAME */
			"User Game",
		}
	};	
}
