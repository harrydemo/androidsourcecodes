package com.kerence.mine.mine_model.Strategy;

import com.kerence.mine.data_structure.MineMatrix;

/**
 * ��Ϸģʽ�еĳ���
 * 
 * @author Kerence
 * 
 */
public class PreliminaryLevel implements MineMatrixSettable
{
	/**
	 * ��MineMatrix��ģʽ�������� ��Ϊ9����Ϊ9������Ϊ10
	 */
	@Override
	public void setMineMatrix(MineMatrix m)
	{
		m.setMatrixInfo(9, 9, 10);
	}

}
