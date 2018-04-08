package com.android.GeneralDesign;

import android.content.pm.ActivityInfo;

public class LayoutDesign
{
	public static final class ScreenResulution
	{
		public static final int UNKNOWN_RESOLUTION = 0;
		public static final int WVGA_800X480 = 1;
		public static final int VGA_640X480 = 2;
		public static final int HVGA_480X320 = 3;
		public static final int QVGA_320X240 = 4;
	}
	public static final class DisplayItemID
	{
		public static final int DEFAULT = 0;
		public static final int TOOL_BAR = 1;
		public static final int GAME_CONTAINER = 2;
		public static final int HELP_BAR = 3;
		public static final int MAIN_MENU_LOGO = 4;
		public static final int MAIN_MENU_ITEMS = 5;
		public static final int GAME_TEXT_VIEW = 6;
		public static final int MAX_VALUE = 7;
	}
	public static final class DispalyParamType
	{
		public static final int DIS_WIN_X = 		0;
		public static final int DIS_WIN_Y = 		1;
		public static final int DIS_WIN_WIDTH = 	2;
		public static final int DIS_WID_HEIGHT = 	3;
		public static final int STYLE = 			4;
		public static final int ALIGEN_MODE = 		5;
	}
	public static final class AlignMode
	{
		public static final int CENTER = 1;
		public static final int LEFT_TOP = 2;
		public static final int TOP_CENTER = 3;
		public static final int BUTTOM_CENTER = 4;
		public static final int LEFT_MIDDLE = 5;
		public static final int RIGHT_MIDDLE = 6;
	}
	public static final class DispalyStyle
	{
		public static final int HORIZONTAL = 0;
		public static final int VERTICAL = 1;
	}
	public static final class DisplayOrienTation
	{
		public static final int HORIZONTAL = 0;
		public static final int VERTICAL = 1;
		public static final int DEFAULT_ORIENTATION = HORIZONTAL;
	}
	public static int getTextSize(
			int screenResolutionType,
			int screenOrientation,
			int displayItemId)
	{
		int textSize;
		switch(screenResolutionType)
		{
		case ScreenResulution.WVGA_800X480:
		case ScreenResulution.VGA_640X480:
		case ScreenResulution.HVGA_480X320:
		case ScreenResulution.QVGA_320X240:
		default:
			if(DisplayItemID.HELP_BAR == displayItemId)
				textSize = 14;
			else
				textSize = 16;
			break;
		}
		return textSize;
	}
	public static int[] getDisplayConfigParam(int displayItemId, int screenResolutionType, int screenOrientation)
	{
		if(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == screenOrientation)
			return getVerticalDisConfigParamTable(displayItemId, screenResolutionType);
		else
			return getHorizontalDisConfigParamTable(displayItemId, screenResolutionType);
	}
	private static int[] getHorizontalDisConfigParamTable(int displayItemId, int screenResolutionType)
	{
		int[] configTabel;
		displayItemId = displayItemId % DisplayItemID.MAX_VALUE;
		
		switch(screenResolutionType)
		{
		case ScreenResulution.WVGA_800X480:
			configTabel = mDisConfigTable_HVGA_480X320_H[displayItemId];
			break;
		case ScreenResulution.VGA_640X480:
			configTabel = mDisConfigTable_HVGA_480X320_H[displayItemId];
			break;
		case ScreenResulution.HVGA_480X320:
			configTabel = mDisConfigTable_HVGA_480X320_H[displayItemId];
			break;
		case ScreenResulution.QVGA_320X240:
			configTabel = mDisConfigTable_HVGA_480X320_H[displayItemId];
			break;
		default:
			configTabel = mDisConfigTable_HVGA_480X320_H[displayItemId];
			break;
		}
		return configTabel;
	}
	private static int[] getVerticalDisConfigParamTable(int displayItemID, int screenResolutionType)
	{
		int[] configTabel;
		displayItemID = displayItemID % DisplayItemID.MAX_VALUE;
		switch(screenResolutionType)
		{
		case ScreenResulution.WVGA_800X480:
			configTabel = mDisConfigTable_HVGA_480X320_V[displayItemID];
			break;
		case ScreenResulution.VGA_640X480:
			configTabel = mDisConfigTable_HVGA_480X320_V[displayItemID];
			break;
		case ScreenResulution.HVGA_480X320:
			configTabel = mDisConfigTable_HVGA_480X320_V[displayItemID];
			break;
		case ScreenResulution.QVGA_320X240:
			configTabel = mDisConfigTable_HVGA_480X320_V[displayItemID];
			break;
		default:
			configTabel = mDisConfigTable_HVGA_480X320_V[displayItemID];
			break;
		}
		return configTabel;
	}

	/*HVGA_480X320_VERTICAL*/
	private static int[][] mDisConfigTable_HVGA_480X320_V = 
	{
		/*UNKOWN_DISPLAY_ITEM*/
		{
			0,							/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			320,						/*DIS_WIN_WIDTH*/
			480,						/*DIS_WID_HEIGHT*/
			DispalyStyle.VERTICAL,		/*STYLE*/
			AlignMode.LEFT_TOP,			/*ALIGEN_MODE*/
		},
		/*TOOL_BAR*/
		{
			0,							/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			320,						/*DIS_WIN_WIDTH*/
			50,							/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.TOP_CENTER,		/*ALIGEN_MODE*/
		},
		/*GAME_CONTAINER*/
		{
			0,							/*DIS_WIN_X*/
			65,							/*DIS_WIN_Y*/
			320,						/*DIS_WIN_WIDTH*/
			320,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
		/*HELP_BAR*/
		{
			0,						/*DIS_WIN_X*/
			400,							/*DIS_WIN_Y*/
			320,						/*DIS_WIN_WIDTH*/
			80,							/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
		/*MAIN_MENU_LOGO*/
		{
			0,							/*DIS_WIN_X*/
			50,							/*DIS_WIN_Y*/
			320,						/*DIS_WIN_WIDTH*/
			130,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
		/*MAIN_MENU_ITEMS*/
		{
			0,							/*DIS_WIN_X*/
			180,						/*DIS_WIN_Y*/
			320,						/*DIS_WIN_WIDTH*/
			300,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
		/*GAME_TEXT_VIEW*/
		{
			0,							/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			320,						/*DIS_WIN_WIDTH*/
			480,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
	};
	/*HVGA_480X320_HORIZONTAL*/
	private static int[][] mDisConfigTable_HVGA_480X320_H = 
	{
		/*UNKOWN_DISPLAY_ITEM*/
		{
			0,							/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			480,						/*DIS_WIN_WIDTH*/
			320,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.LEFT_TOP,			/*ALIGEN_MODE*/
		},
		/*TOOL_BAR*/
		{
			0,							/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			50,							/*DIS_WIN_WIDTH*/
			320,						/*DIS_WID_HEIGHT*/
			DispalyStyle.VERTICAL,		/*STYLE*/
			AlignMode.LEFT_MIDDLE,		/*ALIGEN_MODE*/
		},
		/*GAME_CONTAINER*/
		{
			65,							/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			320,						/*DIS_WIN_WIDTH*/
			320,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
		/*HELP_BAR*/
		{
			385,						/*DIS_WIN_X*/
			0,						/*DIS_WIN_Y*/
			95,							/*DIS_WIN_WIDTH*/
			320,							/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
		/*MAIN_MENU_LOGO*/
		{
			20,							/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			200,						/*DIS_WIN_WIDTH*/
			320,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
		/*MAIN_MENU_ITEMS*/
		{
			240,						/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			240,						/*DIS_WIN_WIDTH*/
			320,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
		/*GAME_TEXT_VIEW*/
		{
			0,							/*DIS_WIN_X*/
			0,							/*DIS_WIN_Y*/
			480,						/*DIS_WIN_WIDTH*/
			320,						/*DIS_WID_HEIGHT*/
			DispalyStyle.HORIZONTAL,	/*STYLE*/
			AlignMode.CENTER,			/*ALIGEN_MODE*/
		},
	};
}
