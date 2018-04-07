package com.kerence.mine.mineGUI.dialog;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * �Զ�����Ϸģʽ�Ի���
 * 
 * @author Kerence
 * 
 */
public class SelfDefinedGameModeDialog extends JDialog implements FocusListener, Action
{
	SelfDefinedGameModeDialog dialog = this;
	private JTextField rowCountField = new JTextField(10);
	private JTextField columnCountField = new JTextField(10);
	private JTextField mineCountField = new JTextField(10);
	private JButton approveButton = new JButton("ȷ��");
	private JButton cancelButton = new JButton("ȡ��");
	private JLabel hintLabel = new JLabel();
	private int retVal = ERROR_OPTION;

	/**
	 * �õ���ѡ�ı����е����������
	 * 
	 * @return ����
	 */
	public int getRowCount()
	{
		return Integer.parseInt(rowCountField.getText());
	}

	/**
	 * �õ���ѡ�ı����е����������
	 * 
	 * @return ����
	 */
	public int getColumnCount()
	{
		return Integer.parseInt(columnCountField.getText());
	}

	/**
	 * �õ���ѡ�ı����е����������
	 * 
	 * @return ����
	 */
	public int getMineCount()
	{
		return Integer.parseInt(mineCountField.getText());
	}

	/**
	 * ����һ���Զ�����Ϸģʽ�Ի���
	 * 
	 * @param jf
	 *            ������
	 * @param rowCount
	 *            ���� ��ʾ�ڶԻ����е�Ĭ������
	 * @param columnCount
	 *            ���� ��ʾ�ڶԻ����е�Ĭ������
	 * @param mineCount
	 *            ���� ��ʾ�ڶԻ����е�Ĭ������
	 */

	public SelfDefinedGameModeDialog(JFrame jf, int rowCount, int columnCount, int mineCount)
	{
		super(jf, "�Զ�������");
		this.setLayout(new FlowLayout());
		this.rowCountField.setText(String.valueOf(rowCount));
		this.columnCountField.setText(String.valueOf(columnCount));
		this.mineCountField.setText(String.valueOf(mineCount));
		this.setPreferredSize(new Dimension(140, 170));
		this.add(new JLabel("�߶�:"));
		this.add(rowCountField);
		this.add(new JLabel("���:"));
		this.add(columnCountField);
		this.add(new JLabel("����:"));
		this.add(mineCountField);
		this.add(approveButton);
		this.add(cancelButton);
		this.add(hintLabel);
		hintLabel.setForeground(Color.red);
		this.setResizable(false);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		approveButton.addActionListener(this);
		cancelButton.addActionListener(this);
		rowCountField.addFocusListener(this);
		columnCountField.addFocusListener(this);
		mineCountField.addFocusListener(this);
		this.pack();
	}

	public static int APPROVE_OPTION = 10;
	public static int CANCEL_OPTION = 20;
	public static int ERROR_OPTION = 30;

	/**
	 * ��ʾ�Զ�����Ϸ�Ի���
	 * 
	 * ����һ���Զ�����Ϸģʽ�Ի���
	 * 
	 * @param jf
	 *            ������
	 * @param rowCount
	 *            ���� ��ʾ�ڶԻ����е�Ĭ������
	 * @param columnCount
	 *            ���� ��ʾ�ڶԻ����е�Ĭ������
	 * @param mineCount
	 *            ���� ��ʾ�ڶԻ����е�Ĭ������
	 * @return �������Ĳ���ѡ�������ERROR_OPTION,APPROVE_OPTION,CANCEL_OPTION
	 */
	public static int showSelfDefinedGameModeDialog(JFrame jf, int rowCount, int columnCount, int mineCount)
	{
		SelfDefinedGameModeDialog dialog = new SelfDefinedGameModeDialog(jf, rowCount, columnCount, mineCount);
		dialog.setVisible(true);
		dialog.pack();
		return dialog.getRetVal();

	}

	/**
	 * �õ�����ֵ
	 * 
	 * @return
	 */
	private int getRetVal()
	{
		return retVal;
	}

	/**
	 * �ж��ı����е������Ƿ���ȷ
	 * 
	 * @return ����ȷ�򷵻�true ���򷵻�false
	 */
	private boolean isInputCorrect()
	{
		String rowCountText = null;
		String columnCountText = null;
		String mineCountText = null;
		int rowCount = 0;
		int columnCount = 0;
		int mineCount = 0;

		rowCountText = rowCountField.getText();
		rowCount = Integer.parseInt(rowCountText);
		if (rowCount < 9 || rowCount > 30)
		{
			hintLabel.setText("�߶�ֻ����9-30֮��");
			return false;
		}
		columnCountText = columnCountField.getText();
		columnCount = Integer.parseInt(columnCountText);
		if (columnCount < 9 || columnCount > 30)
		{
			hintLabel.setText("���ֻ����9-30֮��");
			return false;
		}

		mineCountText = mineCountField.getText();
		mineCount = Integer.parseInt(mineCountText);
		if (mineCount < 10 || mineCount > columnCount * rowCount * 4 / 5)
		{
			hintLabel.setText("����ֻ����10��" + columnCount * rowCount * 4 / 5 + "֮��");
			return false;
		}
		return true;
	}

	/**
	 * ��Ӧ��ť�ĵ���¼�
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == approveButton)
		{
			// ����������ݽ����ж�
			try
			{
				if (!isInputCorrect())
				{
					return;
				}
			} catch (NumberFormatException exception)
			{
				hintLabel.setText("������������");
				return;
			}
			retVal = APPROVE_OPTION;

		} else if (e.getSource() == cancelButton)
		{
			retVal = CANCEL_OPTION;
		}
		this.dispose();// �رնԻ���
	}

	/**
	 * ��ʾ�Զ���Ի���
	 * 
	 * @return ���ز������
	 */
	public int showDialog()
	{
		this.pack();
		this.setVisible(true);
		return this.retVal;
	}

	/**
	 * ���ı���ѡ��ʱ��ѡ���ı����е��ı�
	 */
	@Override
	public void focusGained(FocusEvent e)
	{
		((JTextField) e.getSource()).selectAll();
	}

	@Override
	public void focusLost(FocusEvent e)
	{

	}

	/**
	 * ��д����������esc��enter��Ϣ������
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

				try
				{
					if (!isInputCorrect())
					{
						return;
					}
				} catch (NumberFormatException e)
				{
					hintLabel.setText("������������");
					return;
				}
				retVal = APPROVE_OPTION;

				SelfDefinedGameModeDialog.this.dispose();// �رնԻ���
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
	public Object getValue(String key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String key, Object value)
	{
		// TODO Auto-generated method stub

	}
}
