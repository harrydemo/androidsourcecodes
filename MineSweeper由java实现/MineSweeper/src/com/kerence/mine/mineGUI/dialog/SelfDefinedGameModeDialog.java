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
 * 自定义游戏模式对话框
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
	private JButton approveButton = new JButton("确定");
	private JButton cancelButton = new JButton("取消");
	private JLabel hintLabel = new JLabel();
	private int retVal = ERROR_OPTION;

	/**
	 * 得到单选文本框中的输入的行数
	 * 
	 * @return 行数
	 */
	public int getRowCount()
	{
		return Integer.parseInt(rowCountField.getText());
	}

	/**
	 * 得到单选文本框中的输入的列数
	 * 
	 * @return 列数
	 */
	public int getColumnCount()
	{
		return Integer.parseInt(columnCountField.getText());
	}

	/**
	 * 得到单选文本框中的输入的雷数
	 * 
	 * @return 雷数
	 */
	public int getMineCount()
	{
		return Integer.parseInt(mineCountField.getText());
	}

	/**
	 * 创建一个自定义游戏模式对话框
	 * 
	 * @param jf
	 *            父窗口
	 * @param rowCount
	 *            行数 显示在对话框中的默认行数
	 * @param columnCount
	 *            列数 显示在对话框中的默认列数
	 * @param mineCount
	 *            雷数 显示在对话框中的默认雷数
	 */

	public SelfDefinedGameModeDialog(JFrame jf, int rowCount, int columnCount, int mineCount)
	{
		super(jf, "自定义雷区");
		this.setLayout(new FlowLayout());
		this.rowCountField.setText(String.valueOf(rowCount));
		this.columnCountField.setText(String.valueOf(columnCount));
		this.mineCountField.setText(String.valueOf(mineCount));
		this.setPreferredSize(new Dimension(140, 170));
		this.add(new JLabel("高度:"));
		this.add(rowCountField);
		this.add(new JLabel("宽度:"));
		this.add(columnCountField);
		this.add(new JLabel("雷数:"));
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
	 * 显示自定义游戏对话框
	 * 
	 * 创建一个自定义游戏模式对话框
	 * 
	 * @param jf
	 *            父窗口
	 * @param rowCount
	 *            行数 显示在对话框中的默认行数
	 * @param columnCount
	 *            列数 显示在对话框中的默认列数
	 * @param mineCount
	 *            雷数 显示在对话框中的默认雷数
	 * @return 返回最后的操作选项可以是ERROR_OPTION,APPROVE_OPTION,CANCEL_OPTION
	 */
	public static int showSelfDefinedGameModeDialog(JFrame jf, int rowCount, int columnCount, int mineCount)
	{
		SelfDefinedGameModeDialog dialog = new SelfDefinedGameModeDialog(jf, rowCount, columnCount, mineCount);
		dialog.setVisible(true);
		dialog.pack();
		return dialog.getRetVal();

	}

	/**
	 * 得到返回值
	 * 
	 * @return
	 */
	private int getRetVal()
	{
		return retVal;
	}

	/**
	 * 判断文本框中的输入是否正确
	 * 
	 * @return 若正确则返回true 否则返回false
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
			hintLabel.setText("高度只能在9-30之间");
			return false;
		}
		columnCountText = columnCountField.getText();
		columnCount = Integer.parseInt(columnCountText);
		if (columnCount < 9 || columnCount > 30)
		{
			hintLabel.setText("宽度只能在9-30之间");
			return false;
		}

		mineCountText = mineCountField.getText();
		mineCount = Integer.parseInt(mineCountText);
		if (mineCount < 10 || mineCount > columnCount * rowCount * 4 / 5)
		{
			hintLabel.setText("雷数只能在10和" + columnCount * rowCount * 4 / 5 + "之间");
			return false;
		}
		return true;
	}

	/**
	 * 响应按钮的点击事件
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == approveButton)
		{
			// 对输入的数据进行判断
			try
			{
				if (!isInputCorrect())
				{
					return;
				}
			} catch (NumberFormatException exception)
			{
				hintLabel.setText("必须输入整数");
				return;
			}
			retVal = APPROVE_OPTION;

		} else if (e.getSource() == cancelButton)
		{
			retVal = CANCEL_OPTION;
		}
		this.dispose();// 关闭对话框。
	}

	/**
	 * 显示自定义对话框
	 * 
	 * @return 返回操作结果
	 */
	public int showDialog()
	{
		this.pack();
		this.setVisible(true);
		return this.retVal;
	}

	/**
	 * 当文本框被选中时，选中文本框中的文本
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
	 * 重写这个方法添加esc和enter消息处理方法
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
					hintLabel.setText("必须输入整数");
					return;
				}
				retVal = APPROVE_OPTION;

				SelfDefinedGameModeDialog.this.dispose();// 关闭对话框。
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
