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
 * ��Ϸ�˵�
 * 
 * @author Kerence
 * 
 */
public class JGameMenu extends JMenu
{
	public JMenuItem commence = new JMenuItem("����(N)");// ���ڹ��캯����������Щ������ԭ���ǹ������Ժ���Щ����������Ŷ��
	public JCheckBoxMenuItem preliminaryLevel = new JCheckBoxMenuItem("����(B)");
	public JCheckBoxMenuItem intermediateLevel = new JCheckBoxMenuItem("�м�(I)");
	public JCheckBoxMenuItem advancedLevel = new JCheckBoxMenuItem("�߼�(E)");
	public JCheckBoxMenuItem selfDefinedLevel = new JCheckBoxMenuItem("�Զ���(C)...");
	public ButtonGroup gameModeButtonGroup = new ButtonGroup();
	public MineMatrixSettable mineMatrixSettable = new PreliminaryLevel();
	public JCheckBoxMenuItem markable = new JCheckBoxMenuItem("���(M)");
	// private JMenuItem setFont = new JMenuItem("��������");
	public JMenu rank = new JMenu("ɨ��Ӣ�۰�(T)");
	public JMenuItem preliminaryRanks = new JMenuItem("����Ӣ�۰�");
	public JMenuItem intermediateRanks = new JMenuItem("�м�Ӣ�۰�");
	public JMenuItem advancedRanks = new JMenuItem("�߼�Ӣ�۰�");

	public JMenuItem exit = new JMenuItem("�˳�(X)");

	// private Font currentFont = new Font("����", Font.PLAIN, 12);//
	// �������ִ�з�����ֻ����ִ�й��췽����
	/**
	 * ��ʼ����Ϸģʽ�ĵ�ѡ��ť�˵���
	 */
	private void initializeGameModeRadioButtonMenuItem()
	{
		gameModeButtonGroup.add(preliminaryLevel);
		gameModeButtonGroup.add(intermediateLevel);
		gameModeButtonGroup.add(advancedLevel);
		gameModeButtonGroup.add(selfDefinedLevel);
	}

	/**
	 * ��ʼ����ݼ�
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
	 * ���췽�� ��ʼ����Ϸ�˵�
	 */
	public JGameMenu()
	{
		super("��Ϸ(G)");

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
	 * Ϊ���еĲ˵���Ӽ�����
	 * 
	 * @param l
	 *            ����������
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
