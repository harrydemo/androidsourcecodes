package com.kerence.mine.mineGUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.kerence.mine.data_structure.MineMatrix;
import com.kerence.mine.mine_model.Strategy.MineMatrixSettable;
import com.kerence.mine.res.image.ImageIconFactory;

/**
 * 扫雷矩阵面板
 * 
 * @author Kerence
 * 
 */
public class JMineMatrixPanel extends JPanel
{
	private JMineSweeperFrame mineSweeperFrame;

	/**
	 * 得到一个JMineSweeperFrame的引用，这样才能对statusPanel进行操作
	 * 
	 * @param mineSweeperFrame
	 *            一个JMineSweeperFrame对象的引用
	 */
	public void addMineSweeperFrame(JMineSweeperFrame mineSweeperFrame)
	{
		this.mineSweeperFrame = mineSweeperFrame;
	}

	/**
	 * 雷块鼠标监听器
	 * 
	 * @author Kerence
	 * 
	 */
	private class BlockMouseListener extends MouseAdapter
	{
		/**
		 * 鼠标移入时进行的操作
		 */
		@Override
		public void mouseEntered(MouseEvent e)
		{
			currentMineBlock = ((JMineBlock) e.getSource());
			int row = currentMineBlock.getRow();
			int column = currentMineBlock.getColumn();
			if (button1Pressed && button3Pressed)
			{
				setAutoProbeIcons(row, column);
			} else
			{
				if (button1Pressed)
				{// 在鼠标左键按下的条件下

					if (isFlagged(row, column))
					{
						return;
					}
					if (isDigged(row, column))
					{
						return;
					} else
					{// 若没被挖则 显示空白
						currentMineBlock.setBlankPressed();
						return;
					}
				}
			}

		}

		/**
		 * 鼠标移出时进行的操作
		 */
		@Override
		public void mouseExited(MouseEvent e)
		{

			if (button3Pressed && button1Pressed)
			{// 两键同时被按下的情况
				// 显示出原图
				if (currentMineBlock != null)
				{
					int row = currentMineBlock.getRow();
					int column = currentMineBlock.getColumn();
					revertAutoProbeIcons(row, column);
				}
				currentMineBlock = null;
				return;
			}
			if (button3Pressed)
			{// 右键被按下
				currentMineBlock = null;
			} else
			{// 右键未被按下
				int row = currentMineBlock.getRow();
				int column = currentMineBlock.getColumn();

				if (button1Pressed)
				{// 左键按下 显示原图
					if (isFlagged(row, column))
					{
					} else if (!isDigged(row, column))
					{

						if (isRabotOn() && isMine(row, column))
						{
							currentMineBlock.setDot();
						} else
						{
							currentMineBlock.setBlank();
						}
					}
					currentMineBlock = null;
					return;
				}

				if (isDigged(row, column))
				{
					currentMineBlock = null;
					return;
				} else
				{
					refreshMineMatrixPanel();
				}
			}
			currentMineBlock = null;
		}

		/**
		 * 鼠标单击时进行的操作
		 */
		@Override
		public void mousePressed(MouseEvent e)
		{
			// 鼠标的操作有哪些 特别是鼠标按下的操作
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				button1Pressed = true;
			} else if (e.getButton() == MouseEvent.BUTTON3)
			{
				button3Pressed = true;
			}

			// 得到被按下的块的行和列
			int row = currentMineBlock.getRow();
			int column = currentMineBlock.getColumn();
			// 左右键都按下，则探测
			if (button1Pressed && button3Pressed)
			{// 两键都被按下
				mineSweeperFrame.leftButtonPressedOnMineBlock();
				// TODO auto probe
				setAutoProbeIcons(row, column);
				// refreshMineMatrixPanel();
			} else if (button1Pressed && !button3Pressed)
			{// 左键按下，右键未按下
				mineSweeperFrame.leftButtonPressedOnMineBlock();
				if (isDigged(row, column))
				{
					return;
				} else if (isFlagged(row, column))
				{// 被置旗则也不变
					return;
				}
				jMineMatrix[row][column].setBlankPressed();
			} else if (button3Pressed && !button1Pressed)
			{// 右键被按下 左键未按下

				// 若被挖则无反应
				if (isDigged(row, column))
				{
					return;
				}
				// 未被挖则要判断是否被置旗 是否被置标记
				if (isFlagged(row, column))
				{// 若已放旗
					if (isMarkable())
					{// 可标记
						setUnFlagged(row, column);// 先取消旗
						setMarked(row, column);// 设为标记
					} else
					{// 不可标记
						setUnFlagged(row, column);// 取消旗
					}

				} else
				{// 若未放旗
					if (isMarked(row, column))
					{// 若被标记
						setUnMarked(row, column);// 设置为未标记
					} else
					{
						setFlagged(row, column);
					}

				}
				mineSweeperFrame.setLEDMineCountLeft(mineMatrix.getMineCount() - mineMatrix.getFlagCount());
				refreshMineMatrixPanel();
				return;
			}

		}

		/**
		 * 鼠标释放时进行的操作
		 */
		@Override
		public void mouseReleased(MouseEvent e)
		{// 它的源是鼠标按下时的组件。
			boolean button1Released = false, button3Released = false;
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				button1Pressed = false;
				button1Released = true;
			} else if (e.getButton() == MouseEvent.BUTTON3)
			{
				button3Pressed = false;
				button3Released = true;
			}
			// 鼠标的操作有哪些 特别是鼠标放开的操作
			// 左键放开时
			// 右键按下
			// 这时要进行的操作是自动探测并挖雷 刷新一次界面
			// 鼠标释放事件汇总 左键放开 右键放开。
			if ((button1Released && button3Pressed)// 就是进行探测了。
					|| (button1Pressed && button3Released))
			{// 右键按下时 左键放开
				// 或者左键按下时右键放开

				if (currentMineBlock == null)
				{
					return;
				}
				int row = currentMineBlock.getRow();
				int column = currentMineBlock.getColumn();

				mineSweeperFrame.leftButtonReleasedOnMineBlock();// 对状态栏进行的操作
				// 恢复图形显示
				revertAutoProbeIcons(row, column);
				if (!isDigged(row, column))
				{// 如果没被挖过就不能自动探测
					return;
				}
				mineMatrix.autoDetectMine(currentMineBlock.getRow(), currentMineBlock.getColumn());
				if (mineMatrix.isGameOver())
				{
					// 找到所有雷并
					// mineMatrix.gameOver();// 进行gameOver的雷阵设置
					gameTerminates();
					button3Pressed = false;
					button1Pressed = false;
					mineSweeperFrame.gameOver();
				} else if (mineMatrix.isGameComplete())
				{
					mineMatrix.gameComplete();
					gameTerminates();
					button3Pressed = false;
					button1Pressed = false;
					mineSweeperFrame.gameComplete();
				}
				refreshMineMatrixPanel();// 刷新图形显示
				return;
			}

			// 左键放开
			// 挖雷 若已被挖开则不挖 刷新一次界面
			else if (button1Released && !button3Pressed)
			{// 右键未按下时左键放开挖雷
				mineSweeperFrame.leftButtonReleasedOnMineBlock();
				if (currentMineBlock == null)
				{
					return;
				}

				int row = currentMineBlock.getRow();
				int column = currentMineBlock.getColumn();
				if (mineMatrix.isDigged(row, column))
				{// 判断是否已经被挖开
					return;
				} else
				{// 没挖开就挖
					// 还要判断是否是第一挖
					if (mineMatrix.isFlagged(row, column))
					{// 要先判断是否放旗。
						// 是旗则不挖
						return;
					}
					if (!mineMatrix.hasGameCommenced())
					{// 如果未开局
						generateRandomMatrix(row, column);// 并生成雷阵列。
						mineMatrix.setGameCommenced(true);// 设置成开局
						activateStatusPanel();// 通知主窗口游戏开始。只是对状态栏进行设置而已
						// 而且这里的命名好像不好吧。应该换成activatestatuspanel
					}
					mineMatrix.digBlock(row, column);// 挖雷包括自动挖开周围的雷
					// 如果是雷则自动可以挖开所有的雷。
					// 判断是否挖的是雷

					if (isMine(row, column))
					{// 如果挖的是雷则释放所有监听器
						mineMatrix.setGameOver(true);
						gameTerminates(); // 释放所有雷区按钮的监听器
						mineSweeperFrame.gameOver();// 通知扫雷框架对状态进行设置

					} else if (mineMatrix.isGameComplete())
					{
						gameTerminates();
						mineMatrix.gameComplete();
						mineSweeperFrame.gameComplete();
					}
					refreshMineMatrixPanel();// 刷新界面
				}
			}
		}
	}

	/**
	 * 设置row行column列的雷为标记
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	private void setMarked(int row, int column)
	{
		this.mineMatrix.setMarked(row, column);
	}

	/**
	 * 设置row行column列的雷为未标记
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	private void setUnMarked(int row, int column)
	{
		this.mineMatrix.setUnMarked(row, column);

	}

	/**
	 * 返回是否可以标记
	 * 
	 * @return 要否标记的状态
	 */
	private boolean isMarkable()
	{
		return mineMatrix.isMarkable();
	}

	/**
	 * 激活状态栏
	 */
	private void activateStatusPanel()
	{
		mineSweeperFrame.gameCommences();
	}

	/**
	 * 设置自动探测状态下雷块周围图标的显示
	 * 
	 * @param row
	 *            雷块的行号
	 * @param column
	 *            雷块的列号
	 */
	private void setAutoProbeIcons(int row, int column)
	{// 自动探测时的图标显示
		// 将周围被标记的置为标记按下
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				setAutoProbeIcon(row + i, column + j);
			}
		}
	}

	/**
	 * 设置自动探测状态下恢复雷块周围图标的显示
	 * 
	 * @param row
	 *            雷块的行号
	 * @param column
	 *            雷块的列号
	 */
	private void revertAutoProbeIcons(int row, int column)
	{// 自动探测时的图标显示
		// 将周围被标记的置为标记按下
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				revertAutoProbeIcon(row + i, column + j);
			}
		}
	}

	/**
	 * 设置自动探测状态下该雷块的图标显示
	 * 
	 * @param row
	 *            雷块的行号
	 * @param column
	 *            雷块的列号
	 */
	private void setAutoProbeIcon(int row, int column)
	{// 自动探测时的图标显示
		if (row < 0 || row > this.getMatrixRowCount() - 1 || column < 0 || column > this.getMatrixColumnCount() - 1)
		{
			return;
		}
		// 将周围被标记的置为标记按下
		if (isMarked(row, column))
		{
			this.jMineMatrix[row][column].setMarkPressed();
			return;
		}
		// 将周围未被挖或者未被置旗不改变图案
		if (isDigged(row, column) || isFlagged(row, column))
		{
			return;
		}
		// 否则显示为按下
		this.jMineMatrix[row][column].setBlankPressed();
	}

	/**
	 * 恢复雷块的自动探测状态下的图标
	 * 
	 * @param row
	 *            雷块的和行号
	 * @param column
	 *            雷块的列号
	 */
	private void revertAutoProbeIcon(int row, int column)
	{// 自动探测时的图标显示
		if (row < 0 || row > this.getMatrixRowCount() - 1 || column < 0 || column > this.getMatrixColumnCount() - 1)
		{
			return;
		}
		// 将周围被标记的置为标记按下
		if (isMarked(row, column))
		{
			this.jMineMatrix[row][column].setAsk();
			return;
		}
		// 将周围未被挖或者未被置旗的 置为按下
		if (isDigged(row, column) || isFlagged(row, column))
		{
			return;
		}
		// 否则显示为空白
		if (isRabotOn() && isMine(row, column))
		{
			this.jMineMatrix[row][column].setDot();
			return;
		}
		this.jMineMatrix[row][column].setBlank();
	}

	MouseListener blockMouseListener = new BlockMouseListener();
	private boolean button1Pressed;
	private boolean button3Pressed;
	private JMineBlock currentMineBlock;

	private JMineBlock[][] jMineMatrix;

	private MineMatrix mineMatrix = new MineMatrix();

	/**
	 * 构造方法，设置边界，背景色等。
	 */
	public JMineMatrixPanel()
	{

		Border b1 = BorderFactory.createCompoundBorder(new BevelBorder(BevelBorder.LOWERED), new BevelBorder(BevelBorder.LOWERED));
		Border b2 = BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), b1);
		setBorder(b2);
		setBackground(Color.LIGHT_GRAY);

	}

	/**
	 * 重置游戏
	 */
	public void resetGame()
	{// 重置游戏
		this.resetMatrixPanel();

	}

	/**
	 * 游戏结束
	 */
	private void gameTerminates()
	{
		// 要做哪些操作
		// 1.让所有的点击雷区事件都没有响应， 即释放所有监听器

		for (int row = 0; row < this.getMatrixRowCount(); row++)
		{
			for (int column = 0; column < this.getMatrixColumnCount(); column++)
			{
				jMineMatrix[row][column].removeMouseListener(blockMouseListener);

			}
		}
	}

	/**
	 * 生成随机布雷的矩阵 其中指定位置不能是雷
	 * 
	 * @param specifiedRow
	 *            指定的雷块行号
	 * @param specifiedColumn
	 *            指定的雷块列号
	 */
	public void generateRandomMatrix(int specifiedRow, int specifiedColumn)
	{
		mineMatrix.generateRandomMatrix(specifiedRow, specifiedColumn);
	}

	/**
	 * 得到当前鼠当前指向的雷块的列号
	 * 
	 * @return 列号
	 */
	public int getCurrentBlockColumn()
	{
		return currentMineBlock.getColumn();
	}

	/**
	 * 得到当前鼠当前指向的雷块的行号
	 * 
	 * @return 行号
	 */
	public int getCurrentBlockRow()
	{
		return currentMineBlock.getRow();
	}

	/**
	 * 得到矩阵列数
	 * 
	 * @return 列数
	 */
	public int getMatrixColumnCount()
	{
		return this.mineMatrix.getMatrixColumnCount();
	}

	/**
	 * 得到矩阵行数
	 * 
	 * @return 行数
	 */
	public int getMatrixRowCount()
	{
		return this.mineMatrix.getMatrixRowCount();
	}

	/**
	 * 得到矩阵中雷的个数
	 * 
	 * @return 雷个数
	 */
	public int getMineCount()
	{
		return this.mineMatrix.getMineCount();
	}

	/**
	 * 得到某个雷块周围的雷的个数
	 * 
	 * @return 周围雷的个数
	 */
	public int getMineCountAround(int row, int column)
	{
		return this.mineMatrix.getMineCountAround(row, column);
	}

	/**
	 * 返回某个位置的雷块是否被挖
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 被挖的状态
	 */
	public boolean isDigged(int row, int column)
	{
		return this.mineMatrix.isDigged(row, column);
	}

	/**
	 * 返回某个位置的雷块是否被置旗
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 被置旗的状态
	 */
	public boolean isFlagged(int row, int column)
	{
		return this.mineMatrix.isFlagged(row, column);
	}

	/**
	 * 返回某个位置的雷块是否被标记
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 被标记的态
	 */
	public boolean isMarked(int row, int column)
	{
		return mineMatrix.isMarked(row, column);
	}

	/**
	 * 返回某个位置的雷块是否是雷
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return 是否是雷的状态
	 */
	public boolean isMine(int row, int column)
	{
		return this.mineMatrix.isMine(row, column);
	}

	/**
	 * 刷新矩阵面板，即雷阵上的雷状态发生变化时调用这个方法，刷新界面
	 */
	public void refreshMineMatrixPanel()
	{
		int row = this.getMatrixRowCount();
		int column = this.getMatrixColumnCount();
		for (int r = 0; r < row; r++)
		{
			for (int c = 0; c < column; c++)
			{
				if (this.isDigged(r, c))
				{// 被挖过
					if (this.isMine(r, c))
					{// 是雷
						// 被挖是雷 则显示出红雷
						this.jMineMatrix[r][c].setRedMine();

					} else
					{// 如果不是雷则显示出数字
						// 设置正确数字 被挖是雷无标记
						int count = this.getMineCountAround(r, c);
						this.jMineMatrix[r][c].setNumber(count);
					}
				} else
				{// 没被挖过则 显示是否放置旗 和标记

					if (isFlagged(r, c) && !isMine(r, c) && mineMatrix.isGameOver())
					{// 没被挖过 被置旗 不是雷
						jMineMatrix[r][c].setWrongMine();
						continue;
					}
					if (!isFlagged(r, c) && isMine(r, c) && mineMatrix.isGameOver())
					{// 游戏结束是雷而且没被置旗
						jMineMatrix[r][c].setMine();
						continue;
					}
					if (isFlagged(r, c))
					{// 未挖过，是旗
						jMineMatrix[r][c].setFlag();
						continue;
					}
					if (isMarked(r, c))
					{// 没被挖过 被置标记
						jMineMatrix[r][c].setAsk();
						continue;

					}
					if (isRabotOn() && isMine(r, c))
					{// 开启作弊而且又是雷
						jMineMatrix[r][c].setDot();
						continue;
					}
					// 以上情况都不是，则显示出空白
					jMineMatrix[r][c].setBlank();
				}
			}
		}
	}

	/**
	 * 返回外挂是否开启
	 * 
	 * @return 外挂状态
	 */
	private boolean isRabotOn()
	{
		return this.mineMatrix.isRatbotOn();
	}

	/**
	 * 返回外挂是否开启
	 * 
	 * @return 外挂状态
	 */
	protected void setDigged(int row, int column)
	{
		this.mineMatrix.setDigged(row, column);
	}

	/**
	 * 设置某个位置的雷块被标记
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void setFlagged(int row, int column)
	{
		this.mineMatrix.setFlagged(row, column);
	}

	/**
	 * 更改游戏模式
	 * 
	 * @param mineMatrixSettable
	 *            游戏模式对象引用
	 */
	public void setMineMatrix(MineMatrixSettable mineMatrixSettable)
	{
		mineMatrixSettable.setMineMatrix(mineMatrix);
		mineMatrix.setMineMatrixSettable(mineMatrixSettable);
		int row = this.getMatrixRowCount();
		int column = this.getMatrixColumnCount();
		jMineMatrix = new JMineBlock[row][column];
		resetMatrixPanel();

	}

	/**
	 * 重置矩阵面板 当游戏模式改变时调用它生成新的雷阵列
	 */
	private void resetMatrixPanel()
	{
		resetMineMatrixLabel();// 重置雷标的数量
		mineMatrix.resetMatrix();// 重置底层雷阵
	}

	/**
	 * 得到游戏模式
	 * 
	 * @return 游戏模式描述的字符串
	 */
	public String getGameMode()
	{
		return this.mineMatrix.getGameMode();
	}

	/**
	 * 重置雷阵列图标 将之前所有的雷块图标都清除然后重新生成雷块图标对象
	 * 
	 */
	private void resetMineMatrixLabel()
	{// 重置雷阵的图标数量。
		int row = this.getMatrixRowCount();
		int column = this.getMatrixColumnCount();
		this.setLayout(new GridLayout(row, column));
		this.removeAll();
		this.mineMatrix.setGameCommenced(false);
		for (int r = 0; r < row; r++)
		{
			for (int c = 0; c < column; c++)
			{
				this.jMineMatrix[r][c] = new JMineBlock(ImageIconFactory.getBlank());
				jMineMatrix[r][c].setRow(r);
				jMineMatrix[r][c].setColumn(c);
				this.jMineMatrix[r][c].addMouseListener(this.blockMouseListener);
				this.add(jMineMatrix[r][c]);

			}
		}
	}

	/**
	 * 取消某个位置的雷块的置旗状态
	 * 
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 */
	public void setUnFlagged(int row, int column)
	{
		this.mineMatrix.setUnFlagged(row, column);

	}

	/**
	 * 得到当前模式雷阵的列数
	 * 
	 */
	public int getColumnCount()
	{
		return this.mineMatrix.getColumn();
	}

	/**
	 * 得到当前模式雷阵的行数
	 * 
	 */
	public int getRowCount()
	{
		return this.mineMatrix.getRow();
	}

	/**
	 * 设置是否可以标记
	 * 
	 * @param b
	 *            可否标记的状态
	 */
	public void setMarkable(boolean b)
	{
		this.mineMatrix.setMarkable(b);
	}

	/**
	 * 设置外挂状态
	 * 
	 * @param b
	 *            外挂状态
	 */
	public void setRabot(boolean b)
	{
		this.mineMatrix.setRabot(b);
	}

}
