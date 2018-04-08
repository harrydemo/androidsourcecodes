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
 * ɨ�׾������
 * 
 * @author Kerence
 * 
 */
public class JMineMatrixPanel extends JPanel
{
	private JMineSweeperFrame mineSweeperFrame;

	/**
	 * �õ�һ��JMineSweeperFrame�����ã��������ܶ�statusPanel���в���
	 * 
	 * @param mineSweeperFrame
	 *            һ��JMineSweeperFrame���������
	 */
	public void addMineSweeperFrame(JMineSweeperFrame mineSweeperFrame)
	{
		this.mineSweeperFrame = mineSweeperFrame;
	}

	/**
	 * �׿���������
	 * 
	 * @author Kerence
	 * 
	 */
	private class BlockMouseListener extends MouseAdapter
	{
		/**
		 * �������ʱ���еĲ���
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
				{// �����������µ�������

					if (isFlagged(row, column))
					{
						return;
					}
					if (isDigged(row, column))
					{
						return;
					} else
					{// ��û������ ��ʾ�հ�
						currentMineBlock.setBlankPressed();
						return;
					}
				}
			}

		}

		/**
		 * ����Ƴ�ʱ���еĲ���
		 */
		@Override
		public void mouseExited(MouseEvent e)
		{

			if (button3Pressed && button1Pressed)
			{// ����ͬʱ�����µ����
				// ��ʾ��ԭͼ
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
			{// �Ҽ�������
				currentMineBlock = null;
			} else
			{// �Ҽ�δ������
				int row = currentMineBlock.getRow();
				int column = currentMineBlock.getColumn();

				if (button1Pressed)
				{// ������� ��ʾԭͼ
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
		 * ��굥��ʱ���еĲ���
		 */
		@Override
		public void mousePressed(MouseEvent e)
		{
			// ���Ĳ�������Щ �ر�����갴�µĲ���
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				button1Pressed = true;
			} else if (e.getButton() == MouseEvent.BUTTON3)
			{
				button3Pressed = true;
			}

			// �õ������µĿ���к���
			int row = currentMineBlock.getRow();
			int column = currentMineBlock.getColumn();
			// ���Ҽ������£���̽��
			if (button1Pressed && button3Pressed)
			{// ������������
				mineSweeperFrame.leftButtonPressedOnMineBlock();
				// TODO auto probe
				setAutoProbeIcons(row, column);
				// refreshMineMatrixPanel();
			} else if (button1Pressed && !button3Pressed)
			{// ������£��Ҽ�δ����
				mineSweeperFrame.leftButtonPressedOnMineBlock();
				if (isDigged(row, column))
				{
					return;
				} else if (isFlagged(row, column))
				{// ��������Ҳ����
					return;
				}
				jMineMatrix[row][column].setBlankPressed();
			} else if (button3Pressed && !button1Pressed)
			{// �Ҽ������� ���δ����

				// ���������޷�Ӧ
				if (isDigged(row, column))
				{
					return;
				}
				// δ������Ҫ�ж��Ƿ����� �Ƿ��ñ��
				if (isFlagged(row, column))
				{// ���ѷ���
					if (isMarkable())
					{// �ɱ��
						setUnFlagged(row, column);// ��ȡ����
						setMarked(row, column);// ��Ϊ���
					} else
					{// ���ɱ��
						setUnFlagged(row, column);// ȡ����
					}

				} else
				{// ��δ����
					if (isMarked(row, column))
					{// �������
						setUnMarked(row, column);// ����Ϊδ���
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
		 * ����ͷ�ʱ���еĲ���
		 */
		@Override
		public void mouseReleased(MouseEvent e)
		{// ����Դ����갴��ʱ�������
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
			// ���Ĳ�������Щ �ر������ſ��Ĳ���
			// ����ſ�ʱ
			// �Ҽ�����
			// ��ʱҪ���еĲ������Զ�̽�Ⲣ���� ˢ��һ�ν���
			// ����ͷ��¼����� ����ſ� �Ҽ��ſ���
			if ((button1Released && button3Pressed)// ���ǽ���̽���ˡ�
					|| (button1Pressed && button3Released))
			{// �Ҽ�����ʱ ����ſ�
				// �����������ʱ�Ҽ��ſ�

				if (currentMineBlock == null)
				{
					return;
				}
				int row = currentMineBlock.getRow();
				int column = currentMineBlock.getColumn();

				mineSweeperFrame.leftButtonReleasedOnMineBlock();// ��״̬�����еĲ���
				// �ָ�ͼ����ʾ
				revertAutoProbeIcons(row, column);
				if (!isDigged(row, column))
				{// ���û���ڹ��Ͳ����Զ�̽��
					return;
				}
				mineMatrix.autoDetectMine(currentMineBlock.getRow(), currentMineBlock.getColumn());
				if (mineMatrix.isGameOver())
				{
					// �ҵ������ײ�
					// mineMatrix.gameOver();// ����gameOver����������
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
				refreshMineMatrixPanel();// ˢ��ͼ����ʾ
				return;
			}

			// ����ſ�
			// ���� ���ѱ��ڿ����� ˢ��һ�ν���
			else if (button1Released && !button3Pressed)
			{// �Ҽ�δ����ʱ����ſ�����
				mineSweeperFrame.leftButtonReleasedOnMineBlock();
				if (currentMineBlock == null)
				{
					return;
				}

				int row = currentMineBlock.getRow();
				int column = currentMineBlock.getColumn();
				if (mineMatrix.isDigged(row, column))
				{// �ж��Ƿ��Ѿ����ڿ�
					return;
				} else
				{// û�ڿ�����
					// ��Ҫ�ж��Ƿ��ǵ�һ��
					if (mineMatrix.isFlagged(row, column))
					{// Ҫ���ж��Ƿ���졣
						// ��������
						return;
					}
					if (!mineMatrix.hasGameCommenced())
					{// ���δ����
						generateRandomMatrix(row, column);// �����������С�
						mineMatrix.setGameCommenced(true);// ���óɿ���
						activateStatusPanel();// ֪ͨ��������Ϸ��ʼ��ֻ�Ƕ�״̬���������ö���
						// ����������������񲻺ðɡ�Ӧ�û���activatestatuspanel
					}
					mineMatrix.digBlock(row, column);// ���װ����Զ��ڿ���Χ����
					// ����������Զ������ڿ����е��ס�
					// �ж��Ƿ��ڵ�����

					if (isMine(row, column))
					{// ����ڵ��������ͷ����м�����
						mineMatrix.setGameOver(true);
						gameTerminates(); // �ͷ�����������ť�ļ�����
						mineSweeperFrame.gameOver();// ֪ͨɨ�׿�ܶ�״̬��������

					} else if (mineMatrix.isGameComplete())
					{
						gameTerminates();
						mineMatrix.gameComplete();
						mineSweeperFrame.gameComplete();
					}
					refreshMineMatrixPanel();// ˢ�½���
				}
			}
		}
	}

	/**
	 * ����row��column�е���Ϊ���
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	private void setMarked(int row, int column)
	{
		this.mineMatrix.setMarked(row, column);
	}

	/**
	 * ����row��column�е���Ϊδ���
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	private void setUnMarked(int row, int column)
	{
		this.mineMatrix.setUnMarked(row, column);

	}

	/**
	 * �����Ƿ���Ա��
	 * 
	 * @return Ҫ���ǵ�״̬
	 */
	private boolean isMarkable()
	{
		return mineMatrix.isMarkable();
	}

	/**
	 * ����״̬��
	 */
	private void activateStatusPanel()
	{
		mineSweeperFrame.gameCommences();
	}

	/**
	 * �����Զ�̽��״̬���׿���Χͼ�����ʾ
	 * 
	 * @param row
	 *            �׿���к�
	 * @param column
	 *            �׿���к�
	 */
	private void setAutoProbeIcons(int row, int column)
	{// �Զ�̽��ʱ��ͼ����ʾ
		// ����Χ����ǵ���Ϊ��ǰ���
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				setAutoProbeIcon(row + i, column + j);
			}
		}
	}

	/**
	 * �����Զ�̽��״̬�»ָ��׿���Χͼ�����ʾ
	 * 
	 * @param row
	 *            �׿���к�
	 * @param column
	 *            �׿���к�
	 */
	private void revertAutoProbeIcons(int row, int column)
	{// �Զ�̽��ʱ��ͼ����ʾ
		// ����Χ����ǵ���Ϊ��ǰ���
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				revertAutoProbeIcon(row + i, column + j);
			}
		}
	}

	/**
	 * �����Զ�̽��״̬�¸��׿��ͼ����ʾ
	 * 
	 * @param row
	 *            �׿���к�
	 * @param column
	 *            �׿���к�
	 */
	private void setAutoProbeIcon(int row, int column)
	{// �Զ�̽��ʱ��ͼ����ʾ
		if (row < 0 || row > this.getMatrixRowCount() - 1 || column < 0 || column > this.getMatrixColumnCount() - 1)
		{
			return;
		}
		// ����Χ����ǵ���Ϊ��ǰ���
		if (isMarked(row, column))
		{
			this.jMineMatrix[row][column].setMarkPressed();
			return;
		}
		// ����Χδ���ڻ���δ�����첻�ı�ͼ��
		if (isDigged(row, column) || isFlagged(row, column))
		{
			return;
		}
		// ������ʾΪ����
		this.jMineMatrix[row][column].setBlankPressed();
	}

	/**
	 * �ָ��׿���Զ�̽��״̬�µ�ͼ��
	 * 
	 * @param row
	 *            �׿�ĺ��к�
	 * @param column
	 *            �׿���к�
	 */
	private void revertAutoProbeIcon(int row, int column)
	{// �Զ�̽��ʱ��ͼ����ʾ
		if (row < 0 || row > this.getMatrixRowCount() - 1 || column < 0 || column > this.getMatrixColumnCount() - 1)
		{
			return;
		}
		// ����Χ����ǵ���Ϊ��ǰ���
		if (isMarked(row, column))
		{
			this.jMineMatrix[row][column].setAsk();
			return;
		}
		// ����Χδ���ڻ���δ������� ��Ϊ����
		if (isDigged(row, column) || isFlagged(row, column))
		{
			return;
		}
		// ������ʾΪ�հ�
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
	 * ���췽�������ñ߽磬����ɫ�ȡ�
	 */
	public JMineMatrixPanel()
	{

		Border b1 = BorderFactory.createCompoundBorder(new BevelBorder(BevelBorder.LOWERED), new BevelBorder(BevelBorder.LOWERED));
		Border b2 = BorderFactory.createCompoundBorder(new EmptyBorder(5, 5, 5, 5), b1);
		setBorder(b2);
		setBackground(Color.LIGHT_GRAY);

	}

	/**
	 * ������Ϸ
	 */
	public void resetGame()
	{// ������Ϸ
		this.resetMatrixPanel();

	}

	/**
	 * ��Ϸ����
	 */
	private void gameTerminates()
	{
		// Ҫ����Щ����
		// 1.�����еĵ�������¼���û����Ӧ�� ���ͷ����м�����

		for (int row = 0; row < this.getMatrixRowCount(); row++)
		{
			for (int column = 0; column < this.getMatrixColumnCount(); column++)
			{
				jMineMatrix[row][column].removeMouseListener(blockMouseListener);

			}
		}
	}

	/**
	 * ����������׵ľ��� ����ָ��λ�ò�������
	 * 
	 * @param specifiedRow
	 *            ָ�����׿��к�
	 * @param specifiedColumn
	 *            ָ�����׿��к�
	 */
	public void generateRandomMatrix(int specifiedRow, int specifiedColumn)
	{
		mineMatrix.generateRandomMatrix(specifiedRow, specifiedColumn);
	}

	/**
	 * �õ���ǰ��ǰָ����׿���к�
	 * 
	 * @return �к�
	 */
	public int getCurrentBlockColumn()
	{
		return currentMineBlock.getColumn();
	}

	/**
	 * �õ���ǰ��ǰָ����׿���к�
	 * 
	 * @return �к�
	 */
	public int getCurrentBlockRow()
	{
		return currentMineBlock.getRow();
	}

	/**
	 * �õ���������
	 * 
	 * @return ����
	 */
	public int getMatrixColumnCount()
	{
		return this.mineMatrix.getMatrixColumnCount();
	}

	/**
	 * �õ���������
	 * 
	 * @return ����
	 */
	public int getMatrixRowCount()
	{
		return this.mineMatrix.getMatrixRowCount();
	}

	/**
	 * �õ��������׵ĸ���
	 * 
	 * @return �׸���
	 */
	public int getMineCount()
	{
		return this.mineMatrix.getMineCount();
	}

	/**
	 * �õ�ĳ���׿���Χ���׵ĸ���
	 * 
	 * @return ��Χ�׵ĸ���
	 */
	public int getMineCountAround(int row, int column)
	{
		return this.mineMatrix.getMineCountAround(row, column);
	}

	/**
	 * ����ĳ��λ�õ��׿��Ƿ���
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return ���ڵ�״̬
	 */
	public boolean isDigged(int row, int column)
	{
		return this.mineMatrix.isDigged(row, column);
	}

	/**
	 * ����ĳ��λ�õ��׿��Ƿ�����
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return �������״̬
	 */
	public boolean isFlagged(int row, int column)
	{
		return this.mineMatrix.isFlagged(row, column);
	}

	/**
	 * ����ĳ��λ�õ��׿��Ƿ񱻱��
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return ����ǵ�̬
	 */
	public boolean isMarked(int row, int column)
	{
		return mineMatrix.isMarked(row, column);
	}

	/**
	 * ����ĳ��λ�õ��׿��Ƿ�����
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 * @return �Ƿ����׵�״̬
	 */
	public boolean isMine(int row, int column)
	{
		return this.mineMatrix.isMine(row, column);
	}

	/**
	 * ˢ�¾�����壬�������ϵ���״̬�����仯ʱ�������������ˢ�½���
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
				{// ���ڹ�
					if (this.isMine(r, c))
					{// ����
						// �������� ����ʾ������
						this.jMineMatrix[r][c].setRedMine();

					} else
					{// �������������ʾ������
						// ������ȷ���� ���������ޱ��
						int count = this.getMineCountAround(r, c);
						this.jMineMatrix[r][c].setNumber(count);
					}
				} else
				{// û���ڹ��� ��ʾ�Ƿ������ �ͱ��

					if (isFlagged(r, c) && !isMine(r, c) && mineMatrix.isGameOver())
					{// û���ڹ� ������ ������
						jMineMatrix[r][c].setWrongMine();
						continue;
					}
					if (!isFlagged(r, c) && isMine(r, c) && mineMatrix.isGameOver())
					{// ��Ϸ�������׶���û������
						jMineMatrix[r][c].setMine();
						continue;
					}
					if (isFlagged(r, c))
					{// δ�ڹ�������
						jMineMatrix[r][c].setFlag();
						continue;
					}
					if (isMarked(r, c))
					{// û���ڹ� ���ñ��
						jMineMatrix[r][c].setAsk();
						continue;

					}
					if (isRabotOn() && isMine(r, c))
					{// �������׶���������
						jMineMatrix[r][c].setDot();
						continue;
					}
					// ������������ǣ�����ʾ���հ�
					jMineMatrix[r][c].setBlank();
				}
			}
		}
	}

	/**
	 * ��������Ƿ���
	 * 
	 * @return ���״̬
	 */
	private boolean isRabotOn()
	{
		return this.mineMatrix.isRatbotOn();
	}

	/**
	 * ��������Ƿ���
	 * 
	 * @return ���״̬
	 */
	protected void setDigged(int row, int column)
	{
		this.mineMatrix.setDigged(row, column);
	}

	/**
	 * ����ĳ��λ�õ��׿鱻���
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void setFlagged(int row, int column)
	{
		this.mineMatrix.setFlagged(row, column);
	}

	/**
	 * ������Ϸģʽ
	 * 
	 * @param mineMatrixSettable
	 *            ��Ϸģʽ��������
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
	 * ���þ������ ����Ϸģʽ�ı�ʱ�����������µ�������
	 */
	private void resetMatrixPanel()
	{
		resetMineMatrixLabel();// �����ױ������
		mineMatrix.resetMatrix();// ���õײ�����
	}

	/**
	 * �õ���Ϸģʽ
	 * 
	 * @return ��Ϸģʽ�������ַ���
	 */
	public String getGameMode()
	{
		return this.mineMatrix.getGameMode();
	}

	/**
	 * ����������ͼ�� ��֮ǰ���е��׿�ͼ�궼���Ȼ�����������׿�ͼ�����
	 * 
	 */
	private void resetMineMatrixLabel()
	{// ���������ͼ��������
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
	 * ȡ��ĳ��λ�õ��׿������״̬
	 * 
	 * @param row
	 *            �к�
	 * @param column
	 *            �к�
	 */
	public void setUnFlagged(int row, int column)
	{
		this.mineMatrix.setUnFlagged(row, column);

	}

	/**
	 * �õ���ǰģʽ���������
	 * 
	 */
	public int getColumnCount()
	{
		return this.mineMatrix.getColumn();
	}

	/**
	 * �õ���ǰģʽ���������
	 * 
	 */
	public int getRowCount()
	{
		return this.mineMatrix.getRow();
	}

	/**
	 * �����Ƿ���Ա��
	 * 
	 * @param b
	 *            �ɷ��ǵ�״̬
	 */
	public void setMarkable(boolean b)
	{
		this.mineMatrix.setMarkable(b);
	}

	/**
	 * �������״̬
	 * 
	 * @param b
	 *            ���״̬
	 */
	public void setRabot(boolean b)
	{
		this.mineMatrix.setRabot(b);
	}

}
