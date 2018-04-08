package com.kerence.mine.data_structure;

/**
 * �׿�������ԺͲ���
 * 
 * @download:http://www.codefans.net
 * @author Kerence
 * 
 */

public class Mine
{
	/**
	 * ���׿����ڵ���
	 */
	private int column;
	/**
	 * ��Ǹ��׿��Ƿ���
	 */
	private boolean isDigged = false;
	/**
	 * ���׿��Ƿ�����
	 */
	private boolean isFlagged = false;
	/**
	 * ���׿��Ƿ�����
	 */
	private boolean isMine = false;
	/**
	 * ���׿���Χ������
	 */
	private int mineCountAround = 0;
	/**
	 * ���׿����ڵ���
	 */
	private int row;
	/**
	 * ���׿��Ƿ��Զ��ڿ�����
	 */
	private boolean isAutoDigged = false;
	/**
	 * �Ƿ񱻱��
	 */
	private boolean isMarked = false;

	/**
	 * ���ܣ���������׿��Ƿ񱻱��
	 * 
	 * @return true��ʾ����� false��ʾδ�����
	 */
	public boolean isMarked()
	{
		return isMarked;
	}

	/**
	 * ���ø��׿鱻���
	 * 
	 * @param isMarked
	 *            Ϊtrue����false
	 */
	public void setMarked(boolean isMarked)
	{
		this.isMarked = isMarked;
	}

	/**
	 * row�����׿������У�column����������
	 * 
	 */
	public Mine(int row, int column)
	{
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * �Ƿ��Զ��ڿ�
	 * 
	 * @return true��ʾ���Զ��ڿ� false��ʾδ���Զ��ڿ�
	 */
	public boolean isAutoDigged()
	{
		return isAutoDigged;
	}

	/**
	 * �����Ƿ��Զ��ڿ�
	 * 
	 * @param isAutoDigged
	 *            true��ʾ���Զ��ڿ� false��ʾδ���Զ��ڿ�
	 */
	public void setAutoDigged(boolean isAutoDigged)
	{
		this.isAutoDigged = isAutoDigged;
	}

	/**
	 * ��д��hashCode ���ֵ��prime��������������֮�� ���׿��������������ͬ�����ǵ�hashCode����ͬ�ġ�
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	/**
	 * �Ƚ������׿���ȵ��������������ڵ�������������ȫ��ͬ
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mine other = (Mine) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	/**
	 * ������������ڵ���
	 * 
	 * @return the column
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * �õ���Χ��8���׿����ǵ����׿������
	 * 
	 * @return ��Χ��8���׿����ǵ����׿������
	 */
	public int getMineCountAround()
	{
		return mineCountAround;
	}

	/**
	 * �õ��׿����ڵ���
	 * 
	 * @return �׿����ڵ��к�
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * �����׿��Ƿ��ڹ���
	 * 
	 * @return �׿��Ƿ񱻿�Ѹ
	 */
	public boolean isDigged()
	{
		return isDigged;
	}

	/**
	 * �����Ƿ񱻱����
	 * 
	 * @return �Ƿ񱻱����
	 */
	public boolean isFlagged()
	{
		return isFlagged;
	}

	/**
	 * �����Ƿ�����
	 * 
	 * @return �Ƿ�����
	 */
	public boolean isMine()
	{
		return isMine;
	}

	/**
	 * �����׿���к�
	 * 
	 * @param column
	 *            the column to set
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * �����׿鱻�ڹ�
	 * 
	 */
	public void setDigged()
	{
		this.isDigged = true;
	}

	/**
	 * ����δ���ڹ�
	 */
	public void setUnDigged()
	{
		this.isDigged = false;
	}

	/**
	 * �����Ǳ������
	 * 
	 */
	public void setFlagged()
	{
		this.isFlagged = true;
	}

	/**
	 * ����δ�������
	 * 
	 */
	public void setUnFlagged()
	{
		this.isFlagged = false;
	}

	/**
	 * ��������
	 * 
	 */
	public void setMine()
	{
		this.isMine = true;
	}

	/**
	 * ������Χ8���׿����ǵ��׵ķ��������
	 * 
	 * @param mineCountAround
	 *            ��Χ����
	 */
	public void setMineCountAround(int mineCountAround)
	{
		this.mineCountAround = mineCountAround;
	}

	/**
	 * ���ø��׿����λ��
	 * 
	 * @param row
	 *            ��λ��
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * ��Ҫ�����ڿؼ�̨�²��������״̬����״̬����ַ�����ӡ������
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[");
		if (isDigged)
		{
			sb.append("D");
		}
		if (isFlagged)
		{
			sb.append("F");
		}
		if (isMine)
		{
			sb.append("M");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * �����׿�����Χ�����������Լ�1
	 */
	public void addMineAroundByOne()
	{
		this.mineCountAround++;
	}

	/**
	 * ����δ�����
	 */

	public void setUnMarked()
	{
		this.isMarked = false;
	}
}