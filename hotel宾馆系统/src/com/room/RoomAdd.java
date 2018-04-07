package com.room;

import javax.swing.*;
//Download by http://www.codefans.net
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

public class RoomAdd extends JDialog implements ActionListener
{
	// ��������Ҫ��swing���
	JLabel jl1, jl2, jl3, jl4;
	JButton jb1, jb2;
	JTextField jtf1, jtf2, jtf3, jtf4;
	JPanel jp1, jp2, jp3;

	// owner���ĸ����� title���ڵ�����modalָ����ģ̬���ڻ��Ƿ�ģ̬����
	public RoomAdd(Frame owner, String title, boolean modal)
	{
		super(owner, title, modal);
		jl1 = new JLabel("�����");
		jl2 = new JLabel("��̬");
		jl3 = new JLabel("����");
		jl4 = new JLabel("��Ǯ");

		jtf1 = new JTextField();
		jtf2 = new JTextField();
		jtf3 = new JTextField();
		jtf4 = new JTextField();

		jb1 = new JButton("���");
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
		this.setSize(300, 250);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e)
	{

		// ���û������Ӱ�ť�����Ӧ����

		if (e.getSource() == jb1)
		{
			// ϣ�����
			RoomModel temp = new RoomModel();
			String sql = "insert into room values(?,?,?,?)";
			String[] paras =
			{ jtf1.getText(), jtf2.getText(), jtf3.getText(), jtf4.getText() };
			if (!temp.updateRoom(sql, paras))
			{
				// ��ʾ
				JOptionPane.showMessageDialog(this, "���ʧ��");

			}
			// �رնԻ���
			this.dispose();

		}

	}

}
