package com.kerence.mine.mine_model.Strategy;

import com.kerence.mine.data_structure.MineMatrix;

/**
 * ��Ϸģʽ�еĸ߼�
 * 
 * @author Kerence
 * @download:http://www.codefans.net
 */
public class AdvancedLevel implements MineMatrixSettable
{
	/**
	 * ��MineMatrix��ģʽ�������� ��Ϊ16����Ϊ30������Ϊ90
	 */
	@Override
	public void setMineMatrix(MineMatrix m)
	{
		m.setMatrixInfo(16, 30, 90);
	}

}
