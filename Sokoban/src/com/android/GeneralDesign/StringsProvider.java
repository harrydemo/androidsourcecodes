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
		"������Ϸ��\n" +
		"        �ƶ�С�ڹ꽫��ͼ�ϵ����������Ƶ�ָ��λ�ú�" +
		"��Ϸ�Զ��л�����һ�ء�\n" +
		"        ��Ϸ�����п��԰����ּ����ᴥ��Ļ�Ϸ�������ͼ" +
		"����г������ָ����ؿ����á��ؿ�ѡ��Ȳ�����\n\n" +
		"�༭��Ϸ��\n" +
		"        ��Ϸ�༭ģʽ�£������Ա༭�����Ϸ�ؿ���������" +
		"�����Լ�����Ϸ���⣬���鲻һ������Ϸ��Ȥ��\n" +
		"        �༭��ɺ�ѡ��\"����\"ͼ�걣��༭�������Դ����˵�" +
		"ѡ��\"�����Զ�����Ϸ\"����������������Ϸ��",

		/* ENGLISH */
		"Game Running: move main player and push all" +
		" the box to certian position"
	};
	private static String[] mGameAboutViewMsg =
	{
		/* STR_ABOUT_VIEW */
		"        ��ϵ����:\n\n" +
		"��ҳ:       http://www.xxxxx.com\n" +
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
					"��ʼ��Ϸ",
					"�����Զ�����Ϸ",
					"�༭�Զ�����Ϸ",
					"��Ϸ����",
					"����",
					"����",
					"�˳���Ϸ",
				},
			},
			/* GAME_SETTING */
			{
				/* Title */
				{
					"��Ϸ����"
				},
				/* Menu Item */
				{
					"..\\",
					"ϵͳ��Ч",
					"����������",
					"����������",
					"��������",
					"����Ĭ������"
				},
			},
			/* SYS_SOUND_EFFECT */
			{
				/* Title */
				{
					"ϵͳ��Ч"
				},
				/* Menu Item */
				{
					"..\\",
					"��",
					"��",
				},
			},
			/* BG_MUSIC_SETTING */
			{
				/* Title */
				{
					"������������"
				},
				/* Menu Item */
				{
					"..\\",
					"Ԥ������1",
					"Ԥ������2",
					"Ԥ������3",
					"Ԥ������4",
					"��"
				},
			},
			/* KEY_MUSIC_SETTING */
			{
				/* Title */
				{
					"����������"
				},
				/* Menu Item */
				{
					"..\\",
					"������1",
					"������2",
					"������3",
					"������4",
					"��"
				},
			},
			/* LANGUAGE_SETTING */
			{
				/* Title */
				{
					"��ʾ����ѡ��"
				},
				/* Menu Item */
				{
					"..\\",
					"��������",
					"English"
				}
			},
			/* LOAD_DEFAULT_SETTING */
			{
				/* Title */
				{
					"����Ĭ������?"
				},
				/* Menu Item */
				{
					"��",
					"��",
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
					"��������",
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
			"���ڱ�������, ���Ժ� ......",
			/* DATA_SAVE_SUCCESS */
			"����ɹ�",
			/* DATA_SAVE_FAIL */
			"����ʧ��",
			/* DATA_CHECK_OK */
			"���ݽ���ɹ�",
			/* ERROR_NO_MP */
			"��ΪС�ڹ�ָ����ʼλ��",
			/* ERROR_MP_POS_WRONG */
			"С�ڹ�λ��������ΪС�ڹ�ָ����ȷ��λ��",
			/* ERROR_NO_B0X */
			"δ�����κ����ӣ�����ҪΪ��Ϸָ������һ������",
			/* ERROR_BOX_NUM_NOT_MATCH */
			"��������������һ��",
			/* ERROR_NO_MISSION */
			"������������Ϊ0",
			/* TAB_GAME_STATE_NO */
			"��  ", "  ��",
			/* TAB_STEP_INDEX */
			"����: ",
			/* MSG_USER_GAME */
			"�û��Զ�����Ϸ",
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
