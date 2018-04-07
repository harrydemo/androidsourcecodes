package com.kerence.mine.mine_model.Strategy;

import com.kerence.mine.data_structure.MineMatrix;

/**
 * 游戏模式中的自定义级
 * 
 * @download:http://www.codefans.net
 * @author Kerence
 * 
 */

public class SelfDefinedLevel implements MineMatrixSettable
{
	private int row;
	private int column;
	private int mineCount;

	/**
	 * 构造方法
	 * 
	 * @param row
	 *            自定义模式的行数
	 * @param column
	 *            自定义模式的列数
	 * @param mineCount
	 *            自定义模式的雷数
	 */
	public SelfDefinedLevel(int row, int column, int mineCount)
	{
		super();
		this.row = row;
		this.column = column;
		this.mineCount = mineCount;
	}

	/**
	 * 对MineMatrix的模式进行设置 行，列，雷数都为自定义
	 */
	@Override
	public void setMineMatrix(MineMatrix m)
	{
		m.setMatrixInfo(row, column, mineCount);
	}

}
