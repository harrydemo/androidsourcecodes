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
	// 定义我需要的swing组件
	JLabel jl1, jl2, jl3, jl4;
	JButton jb1, jb2;
	JTextField jtf1, jtf2, jtf3, jtf4;
	JPanel jp1, jp2, jp3;

	// owner他的父窗口 title窗口的名字modal指定是模态窗口还是非模态窗口
	public RoomAdd(Frame owner, String title, boolean modal)
	{
		super(owner, title, modal);
		jl1 = new JLabel("房间号");
		jl2 = new JLabel("房态");
		jl3 = new JLabel("类型");
		jl4 = new JLabel("价钱");

		jtf1 = new JTextField();
		jtf2 = new JTextField();
		jtf3 = new JTextField();
		jtf4 = new JTextField();

		jb1 = new JButton("添加");
		// 注册监听事件
		jb1.addActionListener(this);
		jb2 = new JButton("取消");

		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();

		// 设置布局
		jp1.setLayout(new GridLayout(4, 1));
		jp2.setLayout(new GridLayout(4, 1));

		// 添加组件
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

		// 展现
		this.setSize(300, 250);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e)
	{

		// 对用户点击添加按钮后的相应动作

		if (e.getSource() == jb1)
		{
			// 希望添加
			RoomModel temp = new RoomModel();
			String sql = "insert into room values(?,?,?,?)";
			String[] paras =
			{ jtf1.getText(), jtf2.getText(), jtf3.getText(), jtf4.getText() };
			if (!temp.updateRoom(sql, paras))
			{
				// 提示
				JOptionPane.showMessageDialog(this, "添加失败");

			}
			// 关闭对话框
			this.dispose();

		}

	}

}
