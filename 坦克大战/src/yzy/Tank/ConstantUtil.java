package yzy.Tank;

public class ConstantUtil {
	public static final int pictureWidth = 36;//单元图的宽度
	public static final int pictureCount = 50;//背景图片数量
	public static final int screenWidth = 854;
	public static final int screenHeight = 480;
	
	/**
	 * 下面是游戏中所用到的方向常量
	 * 0静止，1上, 2右上，3右，4右下，5下，6左下，7左，8左上
	 */
	public static final int DIR_STOP = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_RIGHT_UP = 2;
	public static final int DIR_RIGHT = 3;
	public static final int DIR_RIGHT_DOWN = 4;
	public static final int DIR_DOWN = 5;
	public static final int DIR_LEFT_DOWN = 6;
	public static final int DIR_LEFT = 7;
	public static final int DIR_LEFT_UP = 8;
	public static final double BooletSpan = 0.02;//敌机发子弹的概率
	public static final double BooletSpan2 = 0.1;//关口发子弹的概率
	public static final int life = 8;//玩家飞机的生命

}
/*canvas.drawBitmap(gdbj, bjX, 0, null);
bjX=bjX-10;
if(bjX<=-960)
	canvas.drawBitmap(gdbj, 1804+bjX, 0, null);
if(bjX==-1804)
	bjX=0;*/