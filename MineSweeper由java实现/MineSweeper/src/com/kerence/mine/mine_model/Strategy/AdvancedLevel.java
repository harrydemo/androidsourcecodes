package com.kerence.mine.mine_model.Strategy;

import com.kerence.mine.data_structure.MineMatrix;

/**
 * 游戏模式中的高级
 * 
 * @author Kerence
 * @download:http://www.codefans.net
 */
public class AdvancedLevel implements MineMatrixSettable
{
	/**
	 * 对MineMatrix的模式进行设置 行为16，列为30，雷数为90
	 */
	@Override
	public void setMineMatrix(MineMatrix m)
	{
		m.setMatrixInfo(16, 30, 90);
	}

}
