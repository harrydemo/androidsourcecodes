package cn.m.xys;

public interface Const
{

	public static final int ALIGN_TOP = 1;
	public static final int ALIGN_VCENTER = ALIGN_TOP << 1;
	public static final int ALIGN_LEFT = ALIGN_TOP << 2;
	public static final int ALIGN_RIGHT = ALIGN_TOP << 3;
	public static final int ALIGN_HCENTER = ALIGN_TOP << 4;
	public static final int ALIGN_BOTTOM = ALIGN_TOP << 5;

	public final static int GS_WAIT = 0;
	public final static int GS_INVITING = 1;
	public final static int GS_COMFIRE = 2;
	public final static int GS_DECLINE = 3;
	public final static int GS_GAME = 4;
	public final static int GS_END = 5;
	public final static int GS_AWAY = 6;
	public final static int GS_ERROR = 7;

	public final static int MAP_SPACE = 15;

	public final static int TILE_WIDTH = 24;
	public final static int TILE_HEIGHT = 25;

	public final static int CHESS_WIDTH = 9;
	public final static int CHESS_HEIGHT = 9;

	public final static int RADIUS_SPACE = TILE_WIDTH >> 1;

	public final static int CAMP_DEFAULT = 0;
	public final static int CAMP_HERO = 1;
	public final static int CAMP_ENEMY = 2;

	public final static int CALU_ALL_COUNT = 10;
	public final static int CALU_SINGLE_COUNT = 5;
}
