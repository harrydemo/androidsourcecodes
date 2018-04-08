package com.kerence.mine.mine_model.Strategy;

import com.kerence.mine.data_structure.MineMatrix;

/**
 * 游戏模式中的中级
 * 
 * @author Kerence
 * 
 */
public class IntermediateLevel implements MineMatrixSettable
{
	/**
	 * 对MineMatrix的模式进行设置 行为16，列为16，雷数为40
	 */
	@Override
	public void setMineMatrix(MineMatrix m)
	{
		m.setMatrixInfo(16, 16, 40);
	}

}
