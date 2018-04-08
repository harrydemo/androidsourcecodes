package wealk.android.jewels.entity;

/**
 * 精灵接口
 * @author Qingfeng
 * @since 2010-11-03
 */
public interface ISprite {
	
	public int getRow();//获取在地图中到行
	
	public int getCol();//获取在地图中到列
	
	public void setMapPosition(final int row, final int col);//移动到地图到指定位置
	
}
