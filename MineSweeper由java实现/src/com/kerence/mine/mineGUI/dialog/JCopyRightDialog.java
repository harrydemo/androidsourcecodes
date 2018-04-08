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
 * ���ǰ�Ȩ��Ϣ�Ի����ʵ���࣬����һ�������ͻ��Զ���ʾ�������������Ϣ������ �����Ի��򣬰�esc����enter���رնԻ���
 * 
 * @author Kerence
 * 
 */
public class JCopyRightDialog extends JDialog
{
	/**
	 * ���췽��
	 * 
	 * @param jf
	 *            �ð�Ȩ�Ի���ĸ�����
	 */
	public JCopyRightDialog(JFrame jf)
	{
		super(jf, "��Ȩ��Ϣ");
		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder("��Ȩ��Ϣ"));
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
	 * ��д�÷�����ӿ�ݼ�
	 * 
	 * @return ������
	 */
	protected JRootPane createRootPane()
	{
		JRootPane rootPane = super.createRootPane();// ��������塣
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");//
		Action actionListener = new AbstractAction()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				dispose();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);// rootpane����������Ҫ�ĳ�Աinputmap��actionmap������ӳ�䣬��Ϊӳ��
		inputMap.put(stroke, "ESCAPE");
		inputMap.put(KeyStroke.getKeyStroke((KeyEvent.VK_ENTER), 0), "ENTER");
		rootPane.getActionMap().put("ESCAPE", actionListener);
		rootPane.getActionMap().put("ENTER", actionListener);
		super.createRootPane();
		return rootPane;
	}
}
