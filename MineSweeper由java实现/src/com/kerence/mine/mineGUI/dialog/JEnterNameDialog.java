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
 * ���������Ի��򣬴�����������showDialog����ʾ�Ի���
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
	 * ���췽�� ����һ�����������Ի���
	 */
	public JEnterNameDialog()
	{
		JPanel jp = new JPanel();
		jp.add(new JLabel("������Ӣ�۴���"));

		nameField.setText("����");
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
	 * �����������Ի�����ʾ����
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
	 * �õ��ı����е����ݲ���ȥ�����ҿո�
	 * 
	 * @return �ı����е�����
	 */
	public String getHeroName()
	{
		return nameField.getText().trim();// ȥ�����ҿո�
	}

	/**
	 * ��д������� ���esc��enter�ļ�����Ӧ
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
	 * ���ı�����������ַ�������10ʱ�������롣�û������޷�Ӧ
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{// ��������ַ���Ϊ11
		String str = nameField.getText();
		if (str.length() > 10)
		{
			try
			{
				nameField.setText(str.substring(0, 12));
			} catch (StringIndexOutOfBoundsException exception)
			{
				// TODO ������
			}
		}

	}

}