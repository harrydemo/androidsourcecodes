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
 * 扫雷的菜单栏
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
	 * 设置主界面的引用
	 * 
	 * @param i
	 *            主界面引用
	 */
	public void setMineSweeperFrame(JMineSweeperFrame i)
	{
		this.mineFrame = i;
	}

	/**
	 * 菜单上所有按钮的消息处理方法
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == gameMenu.commence)
		{// 开始游戏
			mineFrame.resetGame();
			return;
		}
		if (e.getSource() == gameMenu.preliminaryLevel)
		{// 选择初级
			mineFrame.setGameMode(new PreliminaryLevel());
			return;
		}
		if (e.getSource() == gameMenu.intermediateLevel)
		{// 选择中级
			mineFrame.setGameMode(new IntermediateLevel());
			return;
		}
		if (e.getSource() == gameMenu.advancedLevel)
		{// 选择高级
			mineFrame.setGameMode(new AdvancedLevel());
			return;
		}
		if (e.getSource() == gameMenu.selfDefinedLevel)
		{// 选择自定义级
			int row = mineFrame.getRowCount();// 得到当前程序的游戏模式数据
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
		{// 选择标记
			if (gameMenu.markable.isSelected())
			{// 被选中后会自动设置为没选中。这是单击后新的状态而不是原来的状态。
				mineFrame.setMarkable(true);
			} 
			else
			{
				mineFrame.setMarkable(false);
			}

			return;
		}
		
		if (e.getSource() == helpMenu.copywright)
		{// 选择版权
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
			// 显示对话框
			new JRankDialog("初级英雄榜", mineFrame.preliminaryLevelRanks).showDialog(mineFrame);
			return;
		}
		
		if (e.getSource() == gameMenu.intermediateRanks)
		{
			// 显示
			new JRankDialog("中级英雄榜", mineFrame.intermediateLevelRanks).showDialog(mineFrame);
			return;
		}
		
		if (e.getSource() == gameMenu.advancedRanks)
		{
			// 先排序再显示
			new JRankDialog("高级英雄榜", mineFrame.advancedLevelRanks).showDialog(mineFrame);
			return;
		}
	}
}
