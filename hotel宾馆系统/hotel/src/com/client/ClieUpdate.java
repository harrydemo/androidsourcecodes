/**
 * �޸�ѧ��
 */

package com.client;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import com.Model.ClieModel;

import java.sql.*;

public class ClieUpdate extends JDialog implements ActionListener
{
	// ��������Ҫ��swing���
	JLabel jl1, jl2, jl3, jl4, jl5, jl6, jl7, jl8, jl9, jl10, jl11, jl12, jl13;
	JButton jb1, jb2;
	JTextField jtf1, jtf2, jtf3, jtf4, jtf5, jtf6, jtf7, jtf8, jtf9, jtf10, jtf11, jtf12, jtf13;
	JPanel jp1, jp2, jp3;

	// owner���ĸ����� title���ڵ�����modalָ����ģ̬���ڻ��Ƿ�ģ̬����
	public ClieUpdate(Frame owner, String title, boolean modal, ClieModel sm, int rowNums)
	{
		super(owner, title, modal);
		jl1 = new JLabel("���");
		jl2 = new JLabel("����");
		jl3 = new JLabel("�Ա�");
		jl4 = new JLabel("����");
		jl5 = new JLabel("���֤��");
		jl6 = new JLabel("����");
		jl7 = new JLabel("�绰");
		jl8 = new JLabel("�����");
		jl9 = new JLabel("�ͻ�״̬");
		jl10 = new JLabel("��סʱ��");
		jl11 = new JLabel("�˷�ʱ��");
		jl12 = new JLabel("����");
		jl13 = new JLabel("����");

		jtf1 = new JTextField();
		// ��ʼ������
		jtf1.setText((String) sm.getValueAt(rowNums, 0));
		jtf2 = new JTextField();
		// ��jtf1�����޸�
		jtf1.setEditable(false);
		jtf2.setText((String) sm.getValueAt(rowNums, 1));
		jtf3 = new JTextField();
		jtf3.setText((String) sm.getValueAt(rowNums, 2));
		jtf4 = new JTextField();
		jtf4.setText(sm.getValueAt(rowNums, 3).toString());
		jtf5 = new JTextField();
		jtf5.setText((String) sm.getValueAt(rowNums, 4));
		jtf6 = new JTextField();
		jtf6.setText((String) sm.getValueAt(rowNums, 5));
		jtf7 = new JTextField();
		jtf7.setText((String) sm.getValueAt(rowNums, 6));
		jtf8 = new JTextField();
		jtf8.setText((String) sm.getValueAt(rowNums, 7));
		jtf9 = new JTextField();
		jtf9.setText((String) sm.getValueAt(rowNums, 8));
		jtf10 = new JTextField();
		jtf10.setText((String) sm.getValueAt(rowNums, 9));
		jtf11 = new JTextField();
		jtf11.setText((String) sm.getValueAt(rowNums, 10));
		jtf12 = new JTextField();
		jtf12.setText((String) sm.getValueAt(rowNums, 11));
		jtf13 = new JTextField();
		jtf13.setText((String) sm.getValueAt(rowNums, 12));

		jb1 = new JButton("�޸�");
		// ע������¼�
		jb1.addActionListener(this);
		jb2 = new JButton("ȡ��");

		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();

		// ���ò���
		jp1.setLayout(new GridLayout(13, 1));
		jp2.setLayout(new GridLayout(13, 1));

		// ������
		jp1.add(jl1);
		jp1.add(jl2);
		jp1.add(jl3);
		jp1.add(jl4);
		jp1.add(jl5);
		jp1.add(jl6);
		jp1.add(jl7);
		jp1.add(jl8);
		jp1.add(jl9);
		jp1.add(jl10);
		jp1.add(jl11);
		jp1.add(jl12);
		jp1.add(jl13);

		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(jtf3);
		jp2.add(jtf4);
		jp2.add(jtf5);
		jp2.add(jtf6);
		jp2.add(jtf7);
		jp2.add(jtf8);
		jp2.add(jtf9);
		jp2.add(jtf10);
		jp2.add(jtf11);
		jp2.add(jtf12);
		jp3.add(jtf13);

		jp3.add(jb1);
		jp3.add(jb2);

		this.add(jp1, BorderLayout.WEST);
		this.add(jp2, BorderLayout.CENTER);
		this.add(jp3, BorderLayout.SOUTH);

		// չ��
		this.setSize(1000, 600);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e)
	{

		// ���û������Ӱ�ť�����Ӧ����

		if (e.getSource() == jb1)
		{
			PreparedStatement ps = null;
			Connection ct = null;
			ResultSet rs = null;
			// Statement st=null;
			try
			{

				// �������ݿ⣬�ж��û��Ƿ�Ϸ�
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				ct = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant", "sa", "root");
				// Ԥ����������
				ClieModel temp = new ClieModel();
				String sql = "update client set cName=?,cSex=?,cAge=?," + "cCredit=?,cNation=?,cPhone=?,rNo=?,cStatus=?,startT=?,endT=?,num=?,money=? where cId=?";
				String[] paras =
				{ jtf2.getText(), jtf3.getText(), jtf4.getText(), jtf5.getText(), jtf6.getText(), jtf7.getText(), jtf8.getText(), jtf9.getText(), jtf10.getText(), jtf11.getText(), jtf12.getText(), jtf13.getText(), jtf1.getText() };
				if (!temp.updateClie(sql, paras))
				{
					// ��ʾ
					JOptionPane.showMessageDialog(this, "�޸�ʧ��");

				}
				this.dispose();// �رնԻ���
			} catch (ClassNotFoundException e1)
			{
				// TODO: handle exception
				e1.printStackTrace();
			} catch (SQLException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally
			{
				// �ͷ����������Ӷ���
				try
				{
					if (rs != null)
						rs.close();
					if (ps != null)
						ps.close();
					if (ct != null)
						ct.close();

				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
				}
			}

		}

	}

}
