package com.kerence.mine.mineGUI.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuBar;

import com.kerence.mine.mineGUI.JMineSweeperFrame;
import com.kerence.mine.mineGUI.dialog.JCopyRightDialog;
import com.kerence.mine.mineGUI.dialog.JRankDialog;
import com.kerence.mine.mineGUI.dialog.SelfDefinedGameModeDialog;
import com.kerence.mine.mine_model.Strategy.AdvancedLevel;
import com.kerence.mine.mine_model.Strategy.IntermediateLevel;
import com.kerence.mine.mine_model.Strategy.PreliminaryLevel;
import com.kerence.mine.mine_model.Strategy.SelfDefinedLevel;

/**
 * ɨ�׵Ĳ˵���
 * 
 * @author Administrator
 * 
 */
public class JMineMenuBar extends JMenuBar implements ActionListener
{
	private JGameMenu gameMenu = new JGameMenu();
	private JHelpMenu helpMenu = new JHelpMenu();
	private JMineSweeperFrame mineFrame;

	public JMineMenuBar()
	{
		this.add(gameMenu);
		this.add(helpMenu);
		gameMenu.addActionListenerOnAllItems(this);
		helpMenu.copywright.addActionListener(this);
		helpMenu.rabot.addActionListener(this);

	}

	/**
	 * ���������������
	 * 
	 * @param i
	 *            ����������
	 */
	public void setMineSweeperFrame(JMineSweeperFrame i)
	{
		this.mineFrame = i;
	}

	/**
	 * �˵������а�ť����Ϣ������
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == gameMenu.commence)
		{// ��ʼ��Ϸ
			mineFrame.resetGame();
			return;
		}
		if (e.getSource() == gameMenu.preliminaryLevel)
		{// ѡ�����
			mineFrame.setGameMode(new PreliminaryLevel());
			return;
		}
		if (e.getSource() == gameMenu.intermediateLevel)
		{// ѡ���м�
			mineFrame.setGameMode(new IntermediateLevel());
			return;
		}
		if (e.getSource() == gameMenu.advancedLevel)
		{// ѡ��߼�
			mineFrame.setGameMode(new AdvancedLevel());
			return;
		}
		if (e.getSource() == gameMenu.selfDefinedLevel)
		{// ѡ���Զ��弶
			int row = mineFrame.getRowCount();// �õ���ǰ�������Ϸģʽ����
			int column = mineFrame.getColumnCount();
			int mine = mineFrame.getMineCount();
			SelfDefinedGameModeDialog dialog = new SelfDefinedGameModeDialog(mineFrame, row, column, mine);
			dialog.setLocationRelativeTo(mineFrame);
			if (dialog.showDialog() == SelfDefinedGameModeDialog.APPROVE_OPTION)
			{
				mineFrame.setGameMode(new SelfDefinedLevel(dialog.getRowCount(), dialog.getColumnCount(), dialog.getMineCount()));
			}
			return;
		}
		if (e.getSource() == gameMenu.markable)
		{// ѡ����
			if (gameMenu.markable.isSelected())
			{// ��ѡ�к���Զ�����Ϊûѡ�С����ǵ������µ�״̬������ԭ����״̬��
				mineFrame.setMarkable(true);
			} 
			else
			{
				mineFrame.setMarkable(false);
			}

			return;
		}
		
		if (e.getSource() == helpMenu.copywright)
		{// ѡ���Ȩ
			new JCopyRightDialog(mineFrame);
			return;
		}
		
		if (e.getSource() == gameMenu.exit)
		{
			System.exit(0);
		}

		if (e.getSource() == helpMenu.rabot)
		{
			if (helpMenu.rabot.isSelected())
			{
				mineFrame.setRabot(true);
			} else
			{
				mineFrame.setRabot(false);
			}
			return;
		}
		
		if (e.getSource() == gameMenu.preliminaryRanks)
		{
			// ��ʾ�Ի���
			new JRankDialog("����Ӣ�۰�", mineFrame.preliminaryLevelRanks).showDialog(mineFrame);
			return;
		}
		
		if (e.getSource() == gameMenu.intermediateRanks)
		{
			// ��ʾ
			new JRankDialog("�м�Ӣ�۰�", mineFrame.intermediateLevelRanks).showDialog(mineFrame);
			return;
		}
		
		if (e.getSource() == gameMenu.advancedRanks)
		{
			// ����������ʾ
			new JRankDialog("�߼�Ӣ�۰�", mineFrame.advancedLevelRanks).showDialog(mineFrame);
			return;
		}
	}
}
