package com.kerence.mine.main;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.kerence.mine.mineGUI.JMineSweeperFrame;

/**
 * 程序的入口 设置了皮肤扫雷界面的显示位置
 * 
 * @author Administrator
 * @download:http://www.codefans.net
 */
public class Main
{
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame jf = new JMineSweeperFrame();
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);
	}
}
