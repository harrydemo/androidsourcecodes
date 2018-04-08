/**
 * �޸�ѧ��
 */

package com.room;

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

import com.Model.RoomModel;

import java.sql.*;

public class RoomUpdate extends JDialog implements ActionListener
{
	// ��������Ҫ��swing���
	JLabel jl1, jl2, jl3, jl4;
	JButton jb1, jb2;
	JTextField jtf1, jtf2, jtf3, jtf4;
	JPanel jp1, jp2, jp3;

	// owner���ĸ����� title���ڵ�����modalָ����ģ̬���ڻ��Ƿ�ģ̬����
	public RoomUpdate(Frame owner, String title, boolean modal, RoomModel sm, int rowNums)
	{
		super(owner, title, modal);
		jl1 = new JLabel("�����");
		jl2 = new JLabel("��̬");
		jl3 = new JLabel("����");
		jl4 = new JLabel("��Ǯ");

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
		jtf4.setText((String) sm.getValueAt(rowNums, 3));

		jb1 = new JButton("�޸�");
		// ע������¼�
		jb1.addActionListener(this);
		jb2 = new JButton("ȡ��");

		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();

		// ���ò���
		jp1.setLayout(new GridLayout(4, 1));
		jp2.setLayout(new GridLayout(4, 1));

		// ������
		jp1.add(jl1);
		jp1.add(jl2);
		jp1.add(jl3);
		jp1.add(jl4);

		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(jtf3);
		jp2.add(jtf4);

		jp3.add(jb1);
		jp3.add(jb2);

		this.add(jp1, BorderLayout.WEST);
		this.add(jp2, BorderLayout.CENTER);
		this.add(jp3, BorderLayout.SOUTH);

		// չ��
		this.setSize(600, 250);
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
				ct = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant", "sa", "881221");
				// Ԥ����������
				RoomModel temp = new RoomModel();
				String sql = "update room set status=?,type=?," + "price=? where roomNo=?";
				String[] paras =
				{ jtf2.getText(), jtf3.getText(), jtf4.getText(), jtf1.getText() };
				if (!temp.updateRoom(sql, paras))
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
