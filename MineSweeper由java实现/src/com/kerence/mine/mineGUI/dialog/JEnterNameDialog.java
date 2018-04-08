package com.kerence.mine.mineGUI.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;

/**
 * 输入姓名对话框，创建对象后调用showDialog来显示对话框。
 * 
 * @author Kerence
 * 
 */
public class JEnterNameDialog extends JDialog implements KeyListener
{
	JTextField nameField = new JTextField(10);
	int retVal = ERROR_OPTION;
	String strname;

	/**
	 * 构造方法 创建一个输入姓名对话框
	 */
	public JEnterNameDialog()
	{
		JPanel jp = new JPanel();
		jp.add(new JLabel("请输入英雄大名"));

		nameField.setText("匿名");
		nameField.selectAll();
		jp.add(nameField);
		this.add(jp);
		nameField.addKeyListener(this);
		jp.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		this.setUndecorated(true);
		this.setModal(true);
		this.setPreferredSize(new Dimension(150, 70));
		this.pack();

	}

	// public static void main(String[] args) {
	// new JEnterNameDialog().showDialog(null);
	// }
	/**
	 * 将输入姓名对话框显示出来
	 */
	public int showDialog(JFrame jf)
	{
		this.setLocationRelativeTo(jf);
		this.setVisible(true);
		String str = this.getHeroName();
		return retVal;
	}

	public static int APPROVE_OPTION = 10;
	public static int CANCEL_OPTION = 20;
	public static int ERROR_OPTION = 30;

	/**
	 * 得到文本框中的内容并且去掉左右空格
	 * 
	 * @return 文本框中的姓名
	 */
	public String getHeroName()
	{
		return nameField.getText().trim();// 去掉左右空格
	}

	/**
	 * 重写这个方法 添加esc和enter的键盘响应
	 */
	protected JRootPane createRootPane()
	{
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action escapeActionListener = new AbstractAction()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				retVal = CANCEL_OPTION;
				dispose();
			}
		};
		Action enterActionListener = new AbstractAction()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				retVal = APPROVE_OPTION;
				dispose();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		inputMap.put(KeyStroke.getKeyStroke((KeyEvent.VK_ENTER), 0), "ENTER");
		rootPane.getActionMap().put("ESCAPE", escapeActionListener);
		rootPane.getActionMap().put("ENTER", enterActionListener);
		super.createRootPane();
		return rootPane;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 当文本框中输入的字符数大于10时限制输入。敲击键盘无反应
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{// 限制最大字符数为11
		String str = nameField.getText();
		if (str.length() > 10)
		{
			try
			{
				nameField.setText(str.substring(0, 12));
			} catch (StringIndexOutOfBoundsException exception)
			{
				// TODO 不处理
			}
		}

	}

}