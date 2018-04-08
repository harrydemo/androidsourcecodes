package com.face;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.Model.*;

public class EmpUpdDialog extends JDialog implements ActionListener
{

	JLabel jl1, jl2, jl3, jl4, jl5, jl6, jl7, jl8, jl9, jl10, jl11, jl12, jl13;
	JPanel jp1, jp2, jp3;
	JTextField jtf1, jtf2, jtf3, jtf4, jtf5, jtf6, jtf7, jtf8, jtf9, jtf10, jtf11, jtf12, jtf13;
	JButton jb1, jb2;

	// owerΪ�����ڣ�titleΪ��������modal ָ����ģ̬���ڻ��Ƿ�ģ̬�Ĵ���
	public EmpUpdDialog(JFrame owner, String title, boolean modal, EmpModel em, int rowNums)
	{

		super(owner, title, modal);
		// this.setTitle("�޸�Ա����Ϣ");
		this.setBounds(400, 100, 400, 500);
		// ����jp1���
		jp1 = new JPanel();
		jp1.setLayout(new GridLayout(13, 1));
		jl1 = new JLabel("Ա����");
		jp1.add(jl1);
		jl2 = new JLabel("����");
		jp1.add(jl2);
		jl3 = new JLabel("�Ա�");
		jp1.add(jl3);
		jl4 = new JLabel("��������");
		jp1.add(jl4);
		jl5 = new JLabel("���֤");
		jp1.add(jl5);
		jl6 = new JLabel("ѧ��");
		jp1.add(jl6);
		jl7 = new JLabel("���");
		jp1.add(jl7);
		jl8 = new JLabel("��ϵ�绰1");
		jp1.add(jl8);
		jl9 = new JLabel("��ϵ�绰2");
		jp1.add(jl9);
		jl10 = new JLabel("����");
		jp1.add(jl10);
		jl11 = new JLabel("ע��ʱ��");
		jp1.add(jl11);
		jl12 = new JLabel("��ע");
		jp1.add(jl12);
		jl13 = new JLabel("ְλ");
		jp1.add(jl13);
		this.add(jp1, BorderLayout.WEST);

		// ����jp2���
		jp2 = new JPanel();
		jp2.setLayout(new GridLayout(13, 1));

		jtf1 = new JTextField();
		jtf1.setEditable(false);
		jtf1.setText((String) em.getValueAt(rowNums, 0));
		jp2.add(jtf1);

		jtf2 = new JTextField();
		jtf2.setText((String) em.getValueAt(rowNums, 1));
		jp2.add(jtf2);

		jtf3 = new JTextField();
		jtf3.setText((String) em.getValueAt(rowNums, 2));
		jp2.add(jtf3);

		jtf4 = new JTextField();
		jtf4.setText((String) em.getValueAt(rowNums, 3));
		jp2.add(jtf4);

		jtf5 = new JTextField();
		jtf5.setText((String) em.getValueAt(rowNums, 4));
		jp2.add(jtf5);

		jtf6 = new JTextField();
		jtf6.setText((String) em.getValueAt(rowNums, 5));
		jp2.add(jtf6);

		jtf7 = new JTextField();
		jtf7.setText((String) em.getValueAt(rowNums, 6));
		jp2.add(jtf7);

		jtf8 = new JTextField();
		jtf8.setText((String) em.getValueAt(rowNums, 7));
		jp2.add(jtf8);

		jtf9 = new JTextField();
		jtf9.setText((String) em.getValueAt(rowNums, 8));
		jp2.add(jtf9);

		jtf10 = new JTextField();
		jtf10.setText((String) em.getValueAt(rowNums, 9));
		jp2.add(jtf10);

		jtf11 = new JTextField();
		jtf11.setText((String) em.getValueAt(rowNums, 10));
		jp2.add(jtf11);

		jtf12 = new JTextField();
		jtf12.setText((String) em.getValueAt(rowNums, 11));
		jp2.add(jtf12);

		jtf13 = new JTextField();
		jtf13.setText((String) em.getValueAt(rowNums, 12));
		jp2.add(jtf13);

		this.add(jp2, BorderLayout.CENTER);

		// ����jp3���
		jp3 = new JPanel();
		jp3.setLayout(new FlowLayout());
		jb1 = new JButton("ȷ��");
		jb1.addActionListener(this);
		jp3.add(jb1);
		jb2 = new JButton("ȡ��");
		jb2.addActionListener(this);
		jp3.add(jb2);
		this.add(jp3, BorderLayout.SOUTH);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent arg0)
	{

		if (arg0.getSource() == jb1)
		{
			EmpModel temp = new EmpModel();
			String sql = "update  renshiziliao set username=?,sex=?,birth=?,id=?,xueli = ?," + "married = ?,phone1 = ?,phone2 = ?,youxiang = ?,zhuceshijian =?,beizu = ?,zhiwei = ? where userid=?";

			String[] paras =
			{ jtf2.getText(), jtf3.getText(), jtf4.getText(), jtf5.getText(), jtf6.getText(), jtf7.getText(), jtf8.getText(), jtf9.getText(), jtf10.getText(), jtf11.getText(), jtf12.getText(), jtf13.getText(), jtf1.getText() };
			System.out.println("ȡ�õ�ֵΪ��");
			for (int i = 0; i < paras.length; i++)
				System.out.print(" " + " " + paras[i]);
			// if (!temp.update(sql, paras))
			if (jtf2.getText().equals("") || jtf3.getText().equals("") || jtf4.getText().equals("") || jtf5.getText().equals("") || jtf6.getText().equals("") || jtf7.getText().equals("") || jtf8.getText().equals("") || jtf9.getText().equals("") || jtf10.getText().equals("") || jtf11.getText().equals("") || jtf12.getText().equals("") || jtf13.getText().equals(""))
			{
				JOptionPane.showMessageDialog(this, "���ݲ���Ϊ�գ�����ȷ��������");
			} else if (!temp.updEmp(sql, paras))
			{
				JOptionPane.showMessageDialog(this, "���ʧ��");
			}
			this.dispose();
		}

		else if (arg0.getSource() == jb2)
		{
			this.dispose();
		}

	}

}
