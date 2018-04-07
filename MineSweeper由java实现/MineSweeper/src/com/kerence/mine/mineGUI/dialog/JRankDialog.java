package com.kerence.mine.mineGUI.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.kerence.mine.record.Rank;

/**
 * �����Ի���
 * 
 * @author Kerence
 * 
 */
public class JRankDialog extends JDialog
{
	/**
	 * ����һ�������Ի���
	 * 
	 * @param title
	 *            �����Ի���ı���
	 * @param list
	 *            ��ʾ�ڶԻ����е�����list
	 */
	public JRankDialog(String title, List<Rank> list)
	{
		this.setTitle(title);
		JTextArea textArea = new JTextArea(5, 25);
		JPanel jp = new JPanel();
		jp.add(new JLabel(" ����      ʱ��     ����          "), BorderLayout.NORTH);
		textArea.setEditable(false);
		// for(Rank r:i){
		JScrollPane sp = new JScrollPane(textArea);
		int rank = 1;
		Collections.sort(list);
		if (list.size() == 0)
		{
			textArea.append("���޼�¼");
		}
		
		for (Rank r : list)
		{
			textArea.append(rank + "\t");
			textArea.append(r.toString());
			textArea.append("\n");
			rank++;
		}
		
		jp.add(sp);
		this.add(jp);
		this.setPreferredSize(new Dimension(220, 170));
		// this.setResizable(false);
		this.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.pack();
	}

	/**
	 * ��ʾ�Ի���
	 * 
	 * @param jf
	 *            �����ڵ�����
	 */
	public void showDialog(JFrame jf)
	{
		this.setLocationRelativeTo(jf);
		this.setVisible(true);
		return;
	}

	/**
	 * ��д����������esc��enter�ļ�����Ϣ��Ӧ
	 */
	protected JRootPane createRootPane()
	{

		JRootPane rootPane = super.createRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action actionListener = new AbstractAction()
		{

			public void actionPerformed(ActionEvent actionEvent)
			{
				dispose();
			}
		};
		
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		inputMap.put(KeyStroke.getKeyStroke((KeyEvent.VK_ENTER), 0), "ENTER");
		rootPane.getActionMap().put("ESCAPE", actionListener);
		rootPane.getActionMap().put("ENTER", actionListener);
		super.createRootPane();
		return rootPane;
	}

	// public static void main(String[] args) throws ClassNotFoundException,
	// InstantiationException, IllegalAccessException,
	// UnsupportedLookAndFeelException {
	// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	// List<Rank> list = new LinkedList<Rank>();
	// list.add(new Rank("����", 103, "�½���"));
	// list.add(new Rank("����", 100, "�½���"));
	// list.add(new Rank("����", 101, "�½���"));
	// list.add(new Rank("����", 102, "�½���"));
	// list.add(new Rank("����", 103, "�½���"));
	// list.add(new Rank("����", 2, "�½���"));
	// list.add(new Rank("����", 1, "�½���"));
	// list.add(new Rank("����", 110, "�½���"));
	// list.add(new Rank("����", 110, "�½���"));
	// list.add(new Rank("����", 110, "�½���"));
	// list.add(new Rank("����", 110, "�½���"));
	// list.add(new Rank("����", 110, "�½���"));
	// list.add(new Rank("����", 103, "�½���"));
	// list.add(new Rank("����", 103, "�½���"));
	// list.add(new Rank("����", 107, "�½���"));
	// list.add(new Rank("����", 103, "�½���"));
	// list.add(new Rank("����", 103, "�½���"));
	// new JRankDialog("����", list).showDialog(null);
	// // new JRankDialog("����",new LinkedList<Rank>()).showDialog(null);
	//
	// }
}
