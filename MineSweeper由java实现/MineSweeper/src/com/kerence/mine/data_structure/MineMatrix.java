package com.kerence.mine.data_structure;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.kerence.mine.mine_model.Strategy.MineMatrixSettable;

/**
 * 雷块矩阵的属性和操作
 * 
 * @download:http://www.codefans.net
 * @author Kerence
 * 
 */
public class MineMatrix
{
	/**
	 * 雷阵的列数
	 */
	private int column;
	/**
	 * 雷矩阵的旗数
	 */
	private int flagCount;
	/**
	 * 游戏是否开始的标记
	 */
	private boolean gameCommenced;
	/**
	 * 游戏是否结束的标记
	 */
	private boolean isGameOver = false;
	/**
	 * 外挂是否开始的标记
	 */
	private boolean isRabotOn;
	/**
	 * 是否可以添加mark的标记
	 */
	private boolean markable;
	/**
	 * 雷数
	 */
	private int mineCount;
	/**
	 * 用于存储矩阵的数组
	 */
	private Mine[][] mineMatrix = null;
	/**
	 * 游戏模式 这里使用策略模式对矩阵进行设计
	 */
	MineMatrixSettable mineMode;
	/**
	 * 矩阵的列数
	 */
	private int row;

	/**
	 * 构造函数，什么也不执行
	 */
	public MineMatrix()
	{
	}

	/**
	 * 构造函数
	 * 
	 * @param row
	 *            矩阵的行数
	 * @param column
	 *            矩阵的列数
	 */
	public MineMatrix(int row, int column)
	{
		this.row = row;
		this.column = column;
		mineMatrix = new Mine[row][column];
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < column; j++)
			{
				mineMatrix[i][j] = new Mine(i, j);
			}
		}
	}

	/**
	 * 将row和column位置的周围雷数属性加1
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	private void addMineCountByOne(int row, int column)
	{
		if (row < 0 || row > this.row - 1 || column < 0 || column > this.column - 1)
		{
			return;
		}
		Mine m = this.getMine(row, column);
		m.addMineAroundByOne();
	}

	/**
	 * 对row和column位置的雷块自动探雷
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void autoDetectMine(int row, int column)
	{// 自动探测
		if (row < 0 || column < 0 || row > this.row - 1 || column > this.column - 1)
		{
		}
		// 判断周围的雷数 和旗数 若雷数和旗数相同则自动挖开未挖开的雷
		if (!isDigged(row, column))
		{
			return;
		}
		if (getMineCountAround(row, column) == getFlagCountAround(row, column))
		{// 自动挖雷
			// 扫描附近没有被挖过的雷，然后挖雷。
			for (int i = -1; i < 2; i++)
			{
				for (int j = -1; j < 2; j++)
				{
					if (!isDiggedAutoCorrect(row + i, column + j)// 未被挖过
							&& !isFlaggedAutoCorrect(row + i, column + j))
					{// 未被置过旗
						autoDigMine(row + i, column + j);
						if (isMineAutoCorrect(row + i, column + j))
						{
							setGameOver(true);
						}

					}
				}
			}

		}
	}

	/**
	 * 对row和column位置的雷块自动挖雷，其中使用了递归算法。
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	protected void autoDigMine(int row, int column)
	{// 自动挖雷的条件是什么？1.被标记过。2.未被自动探测过。

		if (row < 0 || column < 0 || row > this.row - 1 || column > this.column - 1)
		{
			return;
		}
		if (isFlagged(row, column))
		{
			return;
		}
		if (getMineCountAround(row, column) == 0 && !isAutoDigged(row, column))
		{
			this.setAutoDigged(row, column, true);
			this.digBlocksAround(row, column);

			this.autoDigMine(row - 1, column - 1);
			this.autoDigMine(row - 1, column);
			this.autoDigMine(row - 1, column + 1);

			this.autoDigMine(row, column - 1);
			this.autoDigMine(row, column + 1);

			this.autoDigMine(row + 1, column - 1);
			this.autoDigMine(row + 1, column);
			this.autoDigMine(row + 1, column + 1);
		}
		this.setDigged(row, column);

	}

	/**
	 * 挖雷开row，column位置的雷 其中调用了autoDigMine方法
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void digBlock(int row, int column)
	{
		if (isMine(row, column))
		{// 如果是雷则将gameOver设置为true
			setDigged(row, column);
			setGameOver(true);
			return;
		}
		autoDigMine(row, column);// 不是雷则自动挖开

	}

	/**
	 * 挖开雷块周围的雷
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	private void digBlocksAround(int row, int column)
	{
		if (row < 0 || column < 0 || row > this.row - 1 || column > this.column - 1)
		{
			return;
		}
		this.setDiggedAutoCorrect(row - 1, column - 1);
		this.setDiggedAutoCorrect(row - 1, column);
		this.setDiggedAutoCorrect(row - 1, column + 1);

		this.setDiggedAutoCorrect(row, column - 1);
		this.setDiggedAutoCorrect(row, column + 1);

		this.setDiggedAutoCorrect(row + 1, column - 1);
		this.setDiggedAutoCorrect(row + 1, column);
		this.setDiggedAutoCorrect(row + 1, column + 1);
	}

	/**
	 * 所有非雷的雷块都被挖出时，执行该方法把所有的是雷的雷块都置为被挖状态。
	 */
	public void gameComplete()
	{
		// 把所有的雷块都挖出来 并标记为flag
		for (int row = 0; row < getMatrixRowCount(); row++)
		{
			for (int column = 0; column < getMatrixColumnCount(); column++)
			{
				if (isMine(row, column))
				{
					setFlagged(row, column);
				}
			}
		}
	}

	/**
	 * 随机生成一个分布有minCount个地雷并且地雷不在row行column列位置的雷阵
	 * 
	 * @param clickedRow
	 *            被鼠标选中的雷块的行号
	 * @param clickedColumn
	 *            被鼠标选中的雷块的列号
	 */
	public void generateRandomMatrix(int clickedRow, int clickedColumn)
	{
		/*
		 * 算法是怎么样的呢？ 要解决的问题1..如果生成的雷块的行和列重复那么要进行判断 并排除这个生成的位置
		 * 2.生成的雷块不能在row和column所标识的位置上。
		 * 解决方法;使用一个set，将产生的每一个雷块都添加进set里，有重复的自动会过滤。
		 */
		// 生成随机雷阵时也把标记置为false
		gameCommenced = false;
		if (clickedRow < 0 || clickedColumn < 0 || clickedRow > this.row - 1 || clickedColumn > this.column - 1)
		{
			throw new IllegalArgumentException();
		}
		Set<Mine> set = new HashSet<Mine>();
		Random r = new Random();
		Mine temp = new Mine(clickedRow, clickedColumn);
		set.add(temp);

		while (set.size() <= mineCount)
		{
			temp = new Mine(r.nextInt(this.row), r.nextInt(this.column));
			if (!set.contains(temp))
			{
				this.mineMatrix[temp.getRow()][temp.getColumn()].setMine();// 这里。
				set.add(temp);
			}
		}
		// 计算每个雷块八角的Mine数并写入一个Mine中.
		for (int i = 0; i < this.row; i++)
		{
			for (int j = 0; j < this.column; j++)
			{
				if (this.isMine(i, j))
				{
					this.addMineCountByOne(i - 1, j - 1);
					this.addMineCountByOne(i - 1, j);
					this.addMineCountByOne(i - 1, j + 1);

					this.addMineCountByOne(i, j - 1);
					// this.addMineCountByOne(i, j); 自己是不用加1滴哦.
					this.addMineCountByOne(i, j + 1);

					this.addMineCountByOne(i + 1, j - 1);
					this.addMineCountByOne(i + 1, j);
					this.addMineCountByOne(i + 1, j + 1);
				}
			}
		}
	}

	/**
	 * 得到当前雷阵列的行数
	 * 
	 * @return 当前雷阵列的行数
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * 得到当前雷阵界面中的旗数
	 * 
	 * @return 当前雷阵界面中的旗数
	 */
	public int getFlagCount()
	{
		return flagCount;
	}

	/**
	 * 得到某个雷块周围旗数
	 * 
	 * @param row
	 *            雷块行号
	 * @param column
	 *            雷块列号
	 * @return 雷块周围旗数
	 */
	public int getFlagCountAround(int row, int column)
	{
		int flagCount = 0;
		if (isFlaggedAutoCorrect(row - 1, column - 1))
		{
			flagCount++;
		}
		if (isFlaggedAutoCorrect(row - 1, column))
		{
			flagCount++;
		}
		if (isFlaggedAutoCorrect(row - 1, column + 1))
		{
			flagCount++;
		}
		if (isFlaggedAutoCorrect(row, column - 1))
		{
			flagCount++;
		}
		if (isFlaggedAutoCorrect(row, column + 1))
		{
			flagCount++;
		}
		if (isFlaggedAutoCorrect(row + 1, column - 1))
		{
			flagCount++;
		}
		if (isFlaggedAutoCorrect(row + 1, column))
		{
			flagCount++;
		}
		if (isFlaggedAutoCorrect(row + 1, column + 1))
		{
			flagCount++;
		}
		return flagCount;
	}

	// public void setMineMatrix(Mine[][] mineMatrix) {
	// this.mineMatrix = mineMatrix;
	// }
	/**
	 * 得到当前游戏模式
	 * 
	 * @return 当前游戏模式的字符串表示
	 */
	public String getGameMode()
	{
		return mineMode.getClass().toString();
	}

	/**
	 * 得到雷阵列数
	 * 
	 */
	public int getMatrixColumnCount()
	{
		return this.column;
	}

	/**
	 * 得到雷阵行数
	 * 
	 */
	public int getMatrixRowCount()
	{
		return this.row;
	}

	/**
	 * 得到某个雷块的引用
	 * 
	 * @param row
	 *            雷块的行号
	 * @param column
	 *            雷块的列号
	 * @return 雷块的引用
	 */
	private Mine getMine(int row, int column)
	{
		return this.getMineMatrix()[row][column];
	}

	/**
	 * 得到当前游戏模式中的雷数
	 * 
	 * @return 当前游戏模式中的雷数
	 */
	public int getMineCount()
	{
		return this.mineCount;
	}

	/**
	 * 得到row行column列的雷块周围的雷数
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 雷块周围的雷数
	 */
	public int getMineCountAround(int row, int column)
	{
		return this.mineMatrix[row][column].getMineCountAround();
	}

	/**
	 * 得到当前游戏状态中的被挖的雷块的个数
	 * 
	 * @return 当前游戏状态中的被挖的雷块的个数
	 */
	private int getMineCountDigged()
	{
		int count = 0;
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < column; j++)
			{
				if (mineMatrix[i][j].isDigged())
				{
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 返回矩阵数组
	 * 
	 * @return 矩阵数组
	 */
	public Mine[][] getMineMatrix()
	{
		return mineMatrix;
	}

	/**
	 * 得到当前游戏模式的行数
	 * 
	 * @return 当前游戏模式的行数
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * 判断游戏是否已经开始
	 * 
	 * @return 游戏是否开始
	 */
	public boolean hasGameCommenced()
	{
		return gameCommenced;
	}

	/**
	 * 某个雷块是否被自动挖过
	 * 
	 * @param row
	 *            雷块的行号
	 * @param column
	 *            雷块的列号
	 * @return 雷块是否被自动挖过
	 */
	public boolean isAutoDigged(int row, int column)
	{
		return mineMatrix[row][column].isAutoDigged();
	}

	/**
	 * 得到row行column列的雷块是否被挖开过
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 雷块是否被挖过
	 */
	public boolean isDigged(int row, int column)
	{
		return this.mineMatrix[row][column].isDigged();
	}

	private boolean isDiggedAutoCorrect(int row, int column)
	{
		if (row < 0 || column > this.column - 1 || column < 0 || row > this.row - 1)
		{
			return false;
		}
		return isDigged(row, column);
	}

	/**
	 * 得到row行column列的雷块是否被标记过
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 是否被标记过
	 */
	public boolean isFlagged(int row, int column)
	{
		return this.mineMatrix[row][column].isFlagged();
	}

	private boolean isFlaggedAutoCorrect(int row, int column)
	{
		if (row < 0 || column > this.column - 1 || column < 0 || row > this.row - 1)
		{
			return false;
		}
		return this.mineMatrix[row][column].isFlagged();
	}

	/**
	 * 判断游戏是否完成， 完成的条件是判断被挖的普通雷块数是否等于总雷数减去雷阵列中是雷的雷块的个数
	 * 
	 * @return 游戏是否完成
	 */
	public boolean isGameComplete()
	{
		return (column * row) - mineCount == getMineCountDigged();
	}

	/**
	 * 游戏是否结束
	 * 
	 * @return 返回游戏是否结束
	 */
	public boolean isGameOver()
	{
		return isGameOver;
	}

	/**
	 * 返回当前游戏可否标记
	 * 
	 * @return 可否标记的状态
	 */
	public boolean isMarkable()
	{
		return markable;
	}

	/**
	 * 某个雷块是否被标记
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 是否被标记
	 */
	public boolean isMarked(int row, int column)
	{
		return mineMatrix[row][column].isMarked();
	}

	/**
	 * 得到row行column列的雷块是否是雷
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 是否是雷
	 */
	public boolean isMine(int row, int column)
	{
		return this.mineMatrix[row][column].isMine();
	}

	/**
	 * 使用递归进行自动挖雷
	 * 
	 * @param row
	 * @param column
	 */
	private boolean isMineAutoCorrect(int row, int column)
	{
		if (row < 0 || column > this.column - 1 || column < 0 || row > this.row - 1)
		{
			return false;
		}
		return isMine(row, column);
	}

	/**
	 * 外挂是否打开
	 * 
	 * @return 外挂是否打开
	 */
	public boolean isRatbotOn()
	{
		return isRabotOn;
	}

	protected void printMatrixToConsole()
	{
		System.out.print("     ");
		for (int j = 0; j < this.column; j++)
		{
			System.out.print(j + "     ");
		}
		System.out.println();
		for (int i = 0; i < this.row; i++)
		{
			System.out.print(i + "   ");
			for (int j = 0; j < this.column; j++)
			{
				System.out.print(this.mineMatrix[i][j] + "    ");

			}
			System.out.println();
		}
	}

	protected void printMineCountAroundMatrix()
	{
		System.out.print("     ");
		for (int j = 0; j < this.column; j++)
		{
			System.out.print(j + "     ");
		}
		System.out.println();
		for (int i = 0; i < this.row; i++)
		{
			System.out.print(i + "    ");
			for (int j = 0; j < this.column; j++)
			{
				System.out.print(this.mineMatrix[i][j].getMineCountAround() + "     ");

			}
			System.out.println();
		}
	}

	/**
	 * 重置雷阵列
	 */
	public void resetMatrix()
	{
		flagCount = 0;
		setGameOver(false);
		mineMatrix = new Mine[row][column];
		for (int i = 0; i < row; i++)
		{
			for (int j = 0; j < column; j++)
			{
				mineMatrix[i][j] = new Mine(i, j);
			}
		}
	}

	/**
	 * 设置某个雷被自动挖过
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @param isAutoDigged
	 *            是否被自动挖过
	 */
	public void setAutoDigged(int row, int column, boolean isAutoDigged)
	{
		if (row < 0 || row > this.row - 1 || column < 0 || column > this.column - 1)
		{
			return;
		}
		mineMatrix[row][column].setAutoDigged(true);
	}

	/**
	 * 设置矩阵的行数
	 * 
	 * @param column
	 */
	protected void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * 设置某个雷块被去标记
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void setDeMarked(int row, int column)
	{
		mineMatrix[row][column].setMarked(false);
	}

	/**
	 * 将row,column位置的雷块设置为isDigged
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void setDigged(int row, int column)
	{
		this.mineMatrix[row][column].setDigged();
		if (isMine(row, column))
		{
			setGameOver(true);
			return;
		}
		if (isGameComplete())
		{
			setGameCommenced(true);
			return;
		}
	}

	private void setDiggedAutoCorrect(int row, int column)
	{
		if (row < 0 || column < 0 || row > this.row - 1 || column > this.column - 1)
		{
			return;
		}
		if (this.mineMatrix[row][column].isFlagged())
		{
			return;
		}
		this.mineMatrix[row][column].setDigged();
	}

	/**
	 * 将row,column位置的雷块设置为isFlagged
	 * 
	 * @param row
	 * @param column
	 */
	public void setFlagged(int row, int column)
	{
		this.mineMatrix[row][column].setFlagged();
		this.flagCount++;
	}

	/**
	 * 设置游戏是否开始
	 * 
	 * @param b
	 *            是否开始
	 */
	public void setGameCommenced(boolean b)
	{
		gameCommenced = b;
	}

	/**
	 * 设置游戏失败
	 * 
	 * @param b
	 *            游戏失败
	 */
	public void setGameOver(boolean b)
	{
		isGameOver = b;
	}

	/**
	 * 设置是否可以标记
	 * 
	 * @param b
	 *            是否可以标记
	 */
	public void setMarkable(boolean b)
	{
		this.markable = b;
	}

	/**
	 * 设置某个雷块为标记
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void setMarked(int row, int column)
	{
		mineMatrix[row][column].setMarked(true);
	}

	/**
	 * 设置游戏模式
	 * 
	 * @param row
	 *            矩阵的行数
	 * @param column
	 *            矩阵的列数
	 * @param mineCount
	 *            雷数
	 */
	public void setMatrixInfo(int row, int column, int mineCount)
	{
		this.setRowAndColumn(row, column);
		if (mineCount < 0)
		{
			throw new IllegalArgumentException();
		}
		this.setMineCount(mineCount);
		this.resetMatrix();
	}

	/**
	 * 将row,column位置的雷块设置为isMine
	 * 
	 * @param row
	 * @param column
	 * @param isMine
	 */
	private void setMine(int row, int column)
	{
		this.mineMatrix[row][column].setMine();
	}

	protected void setMineCount(int mineCount)
	{
		this.mineCount = mineCount;
	}

	/**
	 * 将row,column位置的雷块的周围雷数设置为mineCount
	 * 
	 * @param row
	 * @param column
	 * @param n
	 */
	private void setMineCountAround(int row, int column, int mineCount)
	{
		this.mineMatrix[row][column].setMineCountAround(mineCount);
	}

	/**
	 * 设置游戏模式
	 * 
	 * @param mineMatrixSettable
	 *            游戏模式对象的引用
	 */
	public void setMineMatrixSettable(MineMatrixSettable mineMatrixSettable)
	{
		mineMode = mineMatrixSettable;
	}

	/**
	 * 设置外挂是否开启
	 * 
	 * @param b
	 *            外挂是否开启
	 */
	public void setRabot(boolean b)
	{
		isRabotOn = b;
	}

	/**
	 * 设置列数
	 * 
	 * @param row
	 *            列数
	 */
	protected void setRow(int row)
	{
		this.row = row;
	}

	private void setRowAndColumn(int row, int column)
	{
		if (row < 0 || column < 0)
		{
			throw new IllegalArgumentException();
		}
		this.column = column;
		this.row = row;
		mineMatrix = new Mine[this.row][this.column];
		for (int r = 0; r < row; r++)
		{
			for (int c = 0; c < column; c++)
			{
				mineMatrix[r][c] = new Mine(r, c);
			}
		}
	}

	/**
	 * 设置某个雷块为未置旗
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void setUnFlagged(int row, int column)
	{
		this.mineMatrix[row][column].setUnFlagged();
		this.flagCount--;
	}

	/**
	 * 设置某个雷块为未标记
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void setUnMarked(int row, int column)
	{
		this.mineMatrix[row][column].setUnMarked();
	}

}
