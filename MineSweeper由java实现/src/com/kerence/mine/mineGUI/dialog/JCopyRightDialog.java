package com.kerence.mine.mineGUI.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 * 这是版权信息对话框的实现类，创建一个对象后就会自动显示。阻塞父类的消息处理方法 用鼠标对话框，按esc或者enter来关闭对话框
 * 
 * @author Kerence
 * 
 */
public class JCopyRightDialog extends JDialog
{
	/**
	 * 构造方法
	 * 
	 * @param jf
	 *            该版权对话框的父窗口
	 */
	public JCopyRightDialog(JFrame jf)
	{
		super(jf, "版权信息");
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder("版权信息"));
		Component l = new JLabel("Author: Kerence");
		jp.add(l);
		jp.add(new JLabel("Version: 1.0.0.110817_beta"));
		jp.add(new JLabel("Contact me at: 850751404@qq.com "));
		jp.add(new JLabel("Aug 16 2011 CopyRightReserved "));
		jp.setPreferredSize(new Dimension(200, 120));
		this.add(jp);

		this.setModal(true);
		this.pack();
		this.setLocationRelativeTo(jf);

		this.setVisible(true);
	}

	/**
	 * 重写该方法添加快捷键
	 * 
	 * @return 根窗格
	 */
	protected JRootPane createRootPane()
	{
		JRootPane rootPane = super.createRootPane();// 创建根面板。
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");//
		Action actionListener = new AbstractAction()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				dispose();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);// rootpane还有两个重要的成员inputmap和actionmap。输入映射，行为映射
		inputMap.put(stroke, "ESCAPE");
		inputMap.put(KeyStroke.getKeyStroke((KeyEvent.VK_ENTER), 0), "ENTER");
		rootPane.getActionMap().put("ESCAPE", actionListener);
		rootPane.getActionMap().put("ENTER", actionListener);
		super.createRootPane();
		return rootPane;
	}
}
