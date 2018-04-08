package com.kerence.mine.mineGUI.menu;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.kerence.mine.mine_model.Strategy.MineMatrixSettable;
import com.kerence.mine.mine_model.Strategy.PreliminaryLevel;

/**
 * 游戏菜单
 * 
 * @author Kerence
 * 
 */
public class JGameMenu extends JMenu
{
	public JMenuItem commence = new JMenuItem("开局(N)");// 不在构造函数中声明这些变量的原因是构造完以后这些变量还有用哦。
	public JCheckBoxMenuItem preliminaryLevel = new JCheckBoxMenuItem("初级(B)");
	public JCheckBoxMenuItem intermediateLevel = new JCheckBoxMenuItem("中级(I)");
	public JCheckBoxMenuItem advancedLevel = new JCheckBoxMenuItem("高级(E)");
	public JCheckBoxMenuItem selfDefinedLevel = new JCheckBoxMenuItem("自定义(C)...");
	public ButtonGroup gameModeButtonGroup = new ButtonGroup();
	public MineMatrixSettable mineMatrixSettable = new PreliminaryLevel();
	public JCheckBoxMenuItem markable = new JCheckBoxMenuItem("标记(M)");
	// private JMenuItem setFont = new JMenuItem("设置字体");
	public JMenu rank = new JMenu("扫雷英雄榜(T)");
	public JMenuItem preliminaryRanks = new JMenuItem("初级英雄榜");
	public JMenuItem intermediateRanks = new JMenuItem("中级英雄榜");
	public JMenuItem advancedRanks = new JMenuItem("高级英雄榜");

	public JMenuItem exit = new JMenuItem("退出(X)");

	// private Font currentFont = new Font("宋体", Font.PLAIN, 12);//
	// 这里可以执行方法吗？只可以执行构造方法？
	/**
	 * 初始化游戏模式的单选按钮菜单项
	 */
	private void initializeGameModeRadioButtonMenuItem()
	{
		gameModeButtonGroup.add(preliminaryLevel);
		gameModeButtonGroup.add(intermediateLevel);
		gameModeButtonGroup.add(advancedLevel);
		gameModeButtonGroup.add(selfDefinedLevel);
	}

	/**
	 * 初始化快捷键
	 */
	private void initShortCut()
	{
		commence.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		commence.setMnemonic(KeyEvent.VK_N);
		this.setMnemonic(KeyEvent.VK_G); // doesn't
		preliminaryLevel.setMnemonic(KeyEvent.VK_B);
		intermediateLevel.setMnemonic(KeyEvent.VK_I);
		advancedLevel.setMnemonic(KeyEvent.VK_E);
		selfDefinedLevel.setMnemonic(KeyEvent.VK_C);
		markable.setMnemonic(KeyEvent.VK_M);
		rank.setMnemonic(KeyEvent.VK_T);
		exit.setMnemonic(KeyEvent.VK_X);
	}

	/**
	 * 构造方法 初始化游戏菜单
	 */
	public JGameMenu()
	{
		super("游戏(G)");

		this.add(commence);
		this.addSeparator();
		this.add(preliminaryLevel);
		this.add(intermediateLevel);
		this.add(advancedLevel);
		this.add(selfDefinedLevel);
		this.addSeparator();
		this.add(markable);
		// this.add(setFont);
		this.addSeparator();
		this.add(rank);
		this.addSeparator();
		this.add(exit);
		this.rank.add(preliminaryRanks);
		this.rank.add(intermediateRanks);
		this.rank.add(advancedRanks);
		initializeGameModeRadioButtonMenuItem();
		initShortCut();
		this.preliminaryLevel.setSelected(true);
	}

	/**
	 * 为所有的菜单添加监听器
	 * 
	 * @param l
	 *            监听器引用
	 */
	public void addActionListenerOnAllItems(ActionListener l)
	{
		// setFont.addActionListener(new SelectFontListener());
		commence.addActionListener(l);
		advancedLevel.addActionListener(l);
		exit.addActionListener(l);
		intermediateLevel.addActionListener(l);
		markable.addActionListener(l);
		preliminaryLevel.addActionListener(l);
		rank.addActionListener(l);
		selfDefinedLevel.addActionListener(l);
		preliminaryRanks.addActionListener(l);
		intermediateRanks.addActionListener(l);
		advancedRanks.addActionListener(l);
	}

}
