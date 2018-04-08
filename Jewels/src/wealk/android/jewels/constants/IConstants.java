package wealk.android.jewels.constants;

/**
 * @author Qingfeng
 * @time 2010-11-03
 */
public interface IConstants {
	// ===========================================================
	// Final Fields
	// ===========================================================
	
	/**格子个数**/
	public static final int CELLS_HORIZONTAL = 8;
	public static final int CELLS_VERTICAL = CELLS_HORIZONTAL;	
	
	/**格子尺寸**/
	public static final int CELL_WIDTH = 40;
	public static final int CELL_HEIGHT = CELL_WIDTH;	
	
	/**背景格子个数**/
	public static final int CELLBG_HORIZONTAL = 4;
	public static final int CELLBG_VERTICAL = CELLBG_HORIZONTAL;	
	
	/**背景格子尺寸**/
	public static final int CELLBG_WIDTH = 80;
	public static final int CELLBG_HEIGHT = CELLBG_WIDTH;
	
	/**钻石状态**/
	final int STATE_NORMAL = 0;  //正常
	final int STATE_SCALEINT = STATE_NORMAL + 1; //缩放
	final int STATE_FALL = STATE_SCALEINT + 1;   //下落中
//	final int STATE_FALL_FINISH = STATE_FALL + 1;//下落完成
	final int STATE_DEAD = STATE_FALL + 1;//死亡

	// ===========================================================
	// Methods
	// ===========================================================
}
