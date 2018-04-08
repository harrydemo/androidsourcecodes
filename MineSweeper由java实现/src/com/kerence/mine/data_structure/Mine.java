package com.kerence.mine.data_structure;

/**
 * 雷块类的属性和操作
 * 
 * @download:http://www.codefans.net
 * @author Kerence
 * 
 */

public class Mine
{
	/**
	 * 该雷块所在的列
	 */
	private int column;
	/**
	 * 标记该雷块是否被挖
	 */
	private boolean isDigged = false;
	/**
	 * 该雷块是否被置旗
	 */
	private boolean isFlagged = false;
	/**
	 * 该雷块是否是雷
	 */
	private boolean isMine = false;
	/**
	 * 该雷块周围的雷数
	 */
	private int mineCountAround = 0;
	/**
	 * 该雷块所在的行
	 */
	private int row;
	/**
	 * 该雷块是否被自动挖开过。
	 */
	private boolean isAutoDigged = false;
	/**
	 * 是否被标记
	 */
	private boolean isMarked = false;

	/**
	 * 功能：返回这个雷块是否被标记
	 * 
	 * @return true表示被标记 false表示未被标记
	 */
	public boolean isMarked()
	{
		return isMarked;
	}

	/**
	 * 设置该雷块被标记
	 * 
	 * @param isMarked
	 *            为true或者false
	 */
	public void setMarked(boolean isMarked)
	{
		this.isMarked = isMarked;
	}

	/**
	 * row代表雷块所在行，column代表所在类
	 * 
	 */
	public Mine(int row, int column)
	{
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * 是否被自动挖开
	 * 
	 * @return true表示被自动挖开 false表示未被自动挖开
	 */
	public boolean isAutoDigged()
	{
		return isAutoDigged;
	}

	/**
	 * 设置是否被自动挖开
	 * 
	 * @param isAutoDigged
	 *            true表示被自动挖开 false表示未被自动挖开
	 */
	public void setAutoDigged(boolean isAutoDigged)
	{
		this.isAutoDigged = isAutoDigged;
	}

	/**
	 * 重写了hashCode 标度值是prime加上行数和列数之和 若雷块的行数和列数相同则它们的hashCode是相同的。
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
	 * 比较两个雷块相等的条件是它们所在的行数和列数完全相同
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
	 * 返回这个雷所在的列
	 * 
	 * @return the column
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * 得到周围的8个雷块中是地雷雷块的数量
	 * 
	 * @return 周围的8个雷块中是地雷雷块的数量
	 */
	public int getMineCountAround()
	{
		return mineCountAround;
	}

	/**
	 * 得到雷块所在的行
	 * 
	 * @return 雷块所在的行号
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * 返回雷块是否被挖过，
	 * 
	 * @return 雷块是否被控迅
	 */
	public boolean isDigged()
	{
		return isDigged;
	}

	/**
	 * 返回是否被标记旗
	 * 
	 * @return 是否被标记旗
	 */
	public boolean isFlagged()
	{
		return isFlagged;
	}

	/**
	 * 返回是否是雷
	 * 
	 * @return 是否是雷
	 */
	public boolean isMine()
	{
		return isMine;
	}

	/**
	 * 设置雷块的列号
	 * 
	 * @param column
	 *            the column to set
	 */
	public void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * 设置雷块被挖过
	 * 
	 */
	public void setDigged()
	{
		this.isDigged = true;
	}

	/**
	 * 设置未被挖过
	 */
	public void setUnDigged()
	{
		this.isDigged = false;
	}

	/**
	 * 设置是被标记旗
	 * 
	 */
	public void setFlagged()
	{
		this.isFlagged = true;
	}

	/**
	 * 设置未被标记旗
	 * 
	 */
	public void setUnFlagged()
	{
		this.isFlagged = false;
	}

	/**
	 * 设置是雷
	 * 
	 */
	public void setMine()
	{
		this.isMine = true;
	}

	/**
	 * 设置周围8个雷块中是地雷的方块的数量
	 * 
	 * @param mineCountAround
	 *            周围雷数
	 */
	public void setMineCountAround(int mineCountAround)
	{
		this.mineCountAround = mineCountAround;
	}

	/**
	 * 设置该雷块的行位置
	 * 
	 * @param row
	 *            行位置
	 */
	public void setRow(int row)
	{
		this.row = row;
	}

	/**
	 * 主要用于在控件台下测试雷阵的状态。将状态变成字符串打印出来。
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
	 * 将本雷块中周围的雷数的属性加1
	 */
	public void addMineAroundByOne()
	{
		this.mineCountAround++;
	}

	/**
	 * 设置未被标记
	 */

	public void setUnMarked()
	{
		this.isMarked = false;
	}
}