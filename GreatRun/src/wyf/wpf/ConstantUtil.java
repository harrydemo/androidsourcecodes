package wyf.wpf;				//声明包语句
/*
 * 该类提供一些列的静态成员作为各个类的常量
 */
public class ConstantUtil{
	//与线程休眠有关的常量
	public static final int DRAW_THREAD_SLEEP_SPAN = 67;	//后台重绘线程休眠时间
	public static final int KEY_THREAD_SLEEP_SPAN = 120;		//键盘线程休眠时间
	public static final int AI_THREAD_SLEEP_SPAN = 200;			//AI寻径线程的休眠时间
	//与BitmapManager类有关的常量
	public static final int SYSTEM_BITMAP_LENGTH = 1;		//系统用到的图片数组长度
	public static final int WELCOME_BITMAP_LENGTH = 11;		//欢迎界面用到的图片数组长度
	public static final int []STAGE_BITMAP_LENGTH ={7,7};		//每一关卡中用到的图片数组长度
	public static final int GAME_BITMAP_LENGTH = 37;			//游戏中用到的图片数组长度
	//与Sprite相关的常量
	public static final int SPRITE_WIDTH = 31;			//英雄图片的宽度
	public static final int SPRITE_HEIGHT = 48;			//英雄图片的高度 
	public static final int HERO_MOVING_SPAN = 4;		//英雄的移动步进
	public static final int SPAN_TO_ROLL = 4;		//每次滚屏像素数
	//与游戏后台数据相关的常量
	public static final int MAX_STAGE = 1;				//最高关卡为2
	public static final int STATUS_PASS = 0;			//过一关状态
	public static final int STATUS_WIN = 1;				//过全关状态
	public static final int STATUS_LOSE = 2;			//失败状态
	public static final int STATUS_PLAYING = 3;			//正在游戏状态
	//与地图相关的常量
	public static final int TILE_SIZE = 31;					//地图图元的大小
	public static final int MAP_ROWS = 25;		//地图行数
	public static final int MAP_COLS = 15;		//地图列数
	public static final int SCREEN_ROWS = 16;	//屏幕能显示的行数
	public static final int SCREEN_COLS = 11;	//屏幕能显示的列数
	public static final int SCREEN_WIDTH = 320;	//屏幕宽度
	public static final int SCREEN_HEIGHT = 480;	//屏幕高度
	public static final int SPACE_FOR_ROLL = 124;	//英雄与边界的距离小于该值进行滚屏
	public static final float AREA_PERCENT = 0.6f;		//重叠面积比例超过该值则判定为发生碰撞
	
}