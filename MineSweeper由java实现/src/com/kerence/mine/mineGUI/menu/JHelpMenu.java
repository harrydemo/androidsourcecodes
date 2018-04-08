package com.kerence.mine.mineGUI.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class JHelpMenu extends JMenu
{
	public JCheckBoxMenuItem rabot = new JCheckBoxMenuItem("外挂(R)");
	public JMenuItem copywright = new JMenuItem("版权信息(C)");

	/**
	 * 构造方法 初始化帮助菜单 设置快捷键，添加菜单项
	 */
	public JHelpMenu()
	{
		super("帮助(H)");
		copywright.setMnemonic(KeyEvent.VK_C);
		rabot.setMnemonic(KeyEvent.VK_R);
		this.setMnemonic(KeyEvent.VK_H);
		this.add(copywright);
		this.add(rabot);
	}

}
