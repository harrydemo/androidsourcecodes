package com.kerence.mine.data_structure;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.kerence.mine.mine_model.Strategy.MineMatrixSettable;

/**
 * �׿��������ԺͲ���
 * 
 * @download:http://www.codefans.net
 * @author Kerence
 * 
 */
public class MineMatrix
{
	/**
	 * ���������
	 */
	private int column;
	/**
	 * �׾��������
	 */
	private int flagCount;
	/**
	 * ��Ϸ�Ƿ�ʼ�ı��
	 */
	private boolean gameCommenced;
	/**
	 * ��Ϸ�Ƿ�����ı��
	 */
	private boolean isGameOver = false;
	/**
	 * ����Ƿ�ʼ�ı��
	 */
	private boolean isRabotOn;
	/**
	 * �Ƿ�������mark�ı��
	 */
	private boolean markable;
	/**
	 * ����
	 */
	private int mineCount;
	/**
	 * ���ڴ洢���������
	 */
	private Mine[][] mineMatrix = null;
	/**
	 * ��Ϸģʽ ����ʹ�ò���ģʽ�Ծ���������
	 */
	MineMatrixSettable mineMode;
	/**
	 * ���������
	 */
	private int row;

	/**
	 * ���캯����ʲôҲ��ִ��
	 */
	public MineMatrix()
	{
	}

	/**
	 * ���캯��
	 * 
	 * @param row
	 *            ���������
	 * @param column
	 *            ���������
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
	 * ��row��columnλ�õ���Χ�������Լ�1
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
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
	 * ��row��columnλ�õ��׿��Զ�̽��
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void autoDetectMine(int row, int column)
	{// �Զ�̽��
		if (row < 0 || column < 0 || row > this.row - 1 || column > this.column - 1)
		{
		}
		// �ж���Χ������ ������ ��������������ͬ���Զ��ڿ�δ�ڿ�����
		if (!isDigged(row, column))
		{
			return;
		}
		if (getMineCountAround(row, column) == getFlagCountAround(row, column))
		{// �Զ�����
			// ɨ�踽��û�б��ڹ����ף�Ȼ�����ס�
			for (int i = -1; i < 2; i++)
			{
				for (int j = -1; j < 2; j++)
				{
					if (!isDiggedAutoCorrect(row + i, column + j)// δ���ڹ�
							&& !isFlaggedAutoCorrect(row + i, column + j))
					{// δ���ù���
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
	 * ��row��columnλ�õ��׿��Զ����ף�����ʹ���˵ݹ��㷨��
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	protected void autoDigMine(int row, int column)
	{// �Զ����׵�������ʲô��1.����ǹ���2.δ���Զ�̽�����

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
	 * ���׿�row��columnλ�õ��� ���е�����autoDigMine����
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void digBlock(int row, int column)
	{
		if (isMine(row, column))
		{// ���������gameOver����Ϊtrue
			setDigged(row, column);
			setGameOver(true);
			return;
		}
		autoDigMine(row, column);// ���������Զ��ڿ�

	}

	/**
	 * �ڿ��׿���Χ����
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
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
	 * ���з��׵��׿鶼���ڳ�ʱ��ִ�и÷��������е����׵��׿鶼��Ϊ����״̬��
	 */
	public void gameComplete()
	{
		// �����е��׿鶼�ڳ��� �����Ϊflag
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
	 * �������һ���ֲ���minCount�����ײ��ҵ��ײ���row��column��λ�õ�����
	 * 
	 * @param clickedRow
	 *            �����ѡ�е��׿���к�
	 * @param clickedColumn
	 *            �����ѡ�е��׿���к�
	 */
	public void generateRandomMatrix(int clickedRow, int clickedColumn)
	{
		/*
		 * �㷨����ô�����أ� Ҫ���������1..������ɵ��׿���к����ظ���ôҪ�����ж� ���ų�������ɵ�λ��
		 * 2.���ɵ��׿鲻����row��column����ʶ��λ���ϡ�
		 * �������;ʹ��һ��set����������ÿһ���׿鶼��ӽ�set����ظ����Զ�����ˡ�
		 */
		// �����������ʱҲ�ѱ����Ϊfalse
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
				this.mineMatrix[temp.getRow()][temp.getColumn()].setMine();// ���
				set.add(temp);
			}
		}
		// ����ÿ���׿�˽ǵ�Mine����д��һ��Mine��.
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
					// this.addMineCountByOne(i, j); �Լ��ǲ��ü�1��Ŷ.
					this.addMineCountByOne(i, j + 1);

					this.addMineCountByOne(i + 1, j - 1);
					this.addMineCountByOne(i + 1, j);
					this.addMineCountByOne(i + 1, j + 1);
				}
			}
		}
	}

	/**
	 * �õ���ǰ�����е�����
	 * 
	 * @return ��ǰ�����е�����
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * �õ���ǰ��������е�����
	 * 
	 * @return ��ǰ��������е�����
	 */
	public int getFlagCount()
	{
		return flagCount;
	}

	/**
	 * �õ�ĳ���׿���Χ����
	 * 
	 * @param row
	 *            �׿��к�
	 * @param column
	 *            �׿��к�
	 * @return �׿���Χ����
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
	 * �õ���ǰ��Ϸģʽ
	 * 
	 * @return ��ǰ��Ϸģʽ���ַ�����ʾ
	 */
	public String getGameMode()
	{
		return mineMode.getClass().toString();
	}

	/**
	 * �õ���������
	 * 
	 */
	public int getMatrixColumnCount()
	{
		return this.column;
	}

	/**
	 * �õ���������
	 * 
	 */
	public int getMatrixRowCount()
	{
		return this.row;
	}

	/**
	 * �õ�ĳ���׿������
	 * 
	 * @param row
	 *            �׿���к�
	 * @param column
	 *            �׿���к�
	 * @return �׿������
	 */
	private Mine getMine(int row, int column)
	{
		return this.getMineMatrix()[row][column];
	}

	/**
	 * �õ���ǰ��Ϸģʽ�е�����
	 * 
	 * @return ��ǰ��Ϸģʽ�е�����
	 */
	public int getMineCount()
	{
		return this.mineCount;
	}

	/**
	 * �õ�row��column�е��׿���Χ������
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return �׿���Χ������
	 */
	public int getMineCountAround(int row, int column)
	{
		return this.mineMatrix[row][column].getMineCountAround();
	}

	/**
	 * �õ���ǰ��Ϸ״̬�еı��ڵ��׿�ĸ���
	 * 
	 * @return ��ǰ��Ϸ״̬�еı��ڵ��׿�ĸ���
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
	 * ���ؾ�������
	 * 
	 * @return ��������
	 */
	public Mine[][] getMineMatrix()
	{
		return mineMatrix;
	}

	/**
	 * �õ���ǰ��Ϸģʽ������
	 * 
	 * @return ��ǰ��Ϸģʽ������
	 */
	public int getRow()
	{
		return row;
	}

	/**
	 * �ж���Ϸ�Ƿ��Ѿ���ʼ
	 * 
	 * @return ��Ϸ�Ƿ�ʼ
	 */
	public boolean hasGameCommenced()
	{
		return gameCommenced;
	}

	/**
	 * ĳ���׿��Ƿ��Զ��ڹ�
	 * 
	 * @param row
	 *            �׿���к�
	 * @param column
	 *            �׿���к�
	 * @return �׿��Ƿ��Զ��ڹ�
	 */
	public boolean isAutoDigged(int row, int column)
	{
		return mineMatrix[row][column].isAutoDigged();
	}

	/**
	 * �õ�row��column�е��׿��Ƿ��ڿ���
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return �׿��Ƿ��ڹ�
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
	 * �õ�row��column�е��׿��Ƿ񱻱�ǹ�
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return �Ƿ񱻱�ǹ�
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
	 * �ж���Ϸ�Ƿ���ɣ� ��ɵ��������жϱ��ڵ���ͨ�׿����Ƿ������������ȥ�����������׵��׿�ĸ���
	 * 
	 * @return ��Ϸ�Ƿ����
	 */
	public boolean isGameComplete()
	{
		return (column * row) - mineCount == getMineCountDigged();
	}

	/**
	 * ��Ϸ�Ƿ����
	 * 
	 * @return ������Ϸ�Ƿ����
	 */
	public boolean isGameOver()
	{
		return isGameOver;
	}

	/**
	 * ���ص�ǰ��Ϸ�ɷ���
	 * 
	 * @return �ɷ��ǵ�״̬
	 */
	public boolean isMarkable()
	{
		return markable;
	}

	/**
	 * ĳ���׿��Ƿ񱻱��
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return �Ƿ񱻱��
	 */
	public boolean isMarked(int row, int column)
	{
		return mineMatrix[row][column].isMarked();
	}

	/**
	 * �õ�row��column�е��׿��Ƿ�����
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return �Ƿ�����
	 */
	public boolean isMine(int row, int column)
	{
		return this.mineMatrix[row][column].isMine();
	}

	/**
	 * ʹ�õݹ�����Զ�����
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
	 * ����Ƿ��
	 * 
	 * @return ����Ƿ��
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
	 * ����������
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
	 * ����ĳ���ױ��Զ��ڹ�
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @param isAutoDigged
	 *            �Ƿ��Զ��ڹ�
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
	 * ���þ��������
	 * 
	 * @param column
	 */
	protected void setColumn(int column)
	{
		this.column = column;
	}

	/**
	 * ����ĳ���׿鱻ȥ���
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void setDeMarked(int row, int column)
	{
		mineMatrix[row][column].setMarked(false);
	}

	/**
	 * ��row,columnλ�õ��׿�����ΪisDigged
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
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
	 * ��row,columnλ�õ��׿�����ΪisFlagged
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
	 * ������Ϸ�Ƿ�ʼ
	 * 
	 * @param b
	 *            �Ƿ�ʼ
	 */
	public void setGameCommenced(boolean b)
	{
		gameCommenced = b;
	}

	/**
	 * ������Ϸʧ��
	 * 
	 * @param b
	 *            ��Ϸʧ��
	 */
	public void setGameOver(boolean b)
	{
		isGameOver = b;
	}

	/**
	 * �����Ƿ���Ա��
	 * 
	 * @param b
	 *            �Ƿ���Ա��
	 */
	public void setMarkable(boolean b)
	{
		this.markable = b;
	}

	/**
	 * ����ĳ���׿�Ϊ���
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void setMarked(int row, int column)
	{
		mineMatrix[row][column].setMarked(true);
	}

	/**
	 * ������Ϸģʽ
	 * 
	 * @param row
	 *            ���������
	 * @param column
	 *            ���������
	 * @param mineCount
	 *            ����
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
	 * ��row,columnλ�õ��׿�����ΪisMine
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
	 * ��row,columnλ�õ��׿����Χ��������ΪmineCount
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
	 * ������Ϸģʽ
	 * 
	 * @param mineMatrixSettable
	 *            ��Ϸģʽ���������
	 */
	public void setMineMatrixSettable(MineMatrixSettable mineMatrixSettable)
	{
		mineMode = mineMatrixSettable;
	}

	/**
	 * ��������Ƿ���
	 * 
	 * @param b
	 *            ����Ƿ���
	 */
	public void setRabot(boolean b)
	{
		isRabotOn = b;
	}

	/**
	 * ��������
	 * 
	 * @param row
	 *            ����
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
	 * ����ĳ���׿�Ϊδ����
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void setUnFlagged(int row, int column)
	{
		this.mineMatrix[row][column].setUnFlagged();
		this.flagCount--;
	}

	/**
	 * ����ĳ���׿�Ϊδ���
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void setUnMarked(int row, int column)
	{
		this.mineMatrix[row][column].setUnMarked();
	}

}
