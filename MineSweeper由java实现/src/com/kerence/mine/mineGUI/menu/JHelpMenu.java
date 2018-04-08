package com.kerence.mine.mineGUI.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class JHelpMenu extends JMenu
{
	public JCheckBoxMenuItem rabot = new JCheckBoxMenuItem("���(R)");
	public JMenuItem copywright = new JMenuItem("��Ȩ��Ϣ(C)");

	/**
	 * ���췽�� ��ʼ�������˵� ���ÿ�ݼ�����Ӳ˵���
	 */
	public JHelpMenu()
	{
		super("����(H)");
		copywright.setMnemonic(KeyEvent.VK_C);
		rabot.setMnemonic(KeyEvent.VK_R);
		this.setMnemonic(KeyEvent.VK_H);
		this.add(copywright);
		this.add(rabot);
	}

}
