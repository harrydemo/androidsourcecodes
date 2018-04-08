package com.client;

import javax.swing.*;

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

public class ClieAdd extends JDialog implements ActionListener
{
	// 定义我需要的swing组件
	JLabel jl1, jl2, jl3, jl4, jl5, jl6, jl7, jl8, jl9, jl10, jl11, jl12, jl13;
	JButton jb1, jb2;
	JTextField jtf1, jtf2, jtf3, jtf4, jtf5, jtf6, jtf7, jtf8, jtf9, jtf10, jtf11, jtf12, jtf13;
	JPanel jp1, jp2, jp3;

	// owner他的父窗口 title窗口的名字modal指定是模态窗口还是非模态窗口
	public ClieAdd(Frame owner, String title, boolean modal)
	{
		super(owner, title, modal);
		jl1 = new JLabel("编号");
		jl2 = new JLabel("姓名");
		jl3 = new JLabel("性别");
		jl4 = new JLabel("年龄");
		jl5 = new JLabel("身份证号");
		jl6 = new JLabel("民族");
		jl7 = new JLabel("电话");
		jl8 = new JLabel("房间号");
		jl9 = new JLabel("客户状态");
		jl10 = new JLabel("入住时间");
		jl11 = new JLabel("退房时间");
		jl12 = new JLabel("天数");
		jl13 = new JLabel("结算");

		jtf1 = new JTextField();
		jtf2 = new JTextField();
		jtf3 = new JTextField();
		jtf4 = new JTextField();
		jtf5 = new JTextField();
		jtf6 = new JTextField();
		jtf7 = new JTextField();
		jtf8 = new JTextField();
		jtf9 = new JTextField();
		jtf10 = new JTextField();
		jtf11 = new JTextField();
		jtf12 = new JTextField();
		jtf13 = new JTextField();

		jb1 = new JButton("添加");
		// 注册监听事件
		jb1.addActionListener(this);
		jb2 = new JButton("取消");

		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();

		// 设置布局
		jp1.setLayout(new GridLayout(13, 1));
		jp2.setLayout(new GridLayout(13, 1));

		// 添加组件
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
		jp2.add(jtf13);
		jp3.add(jb1);
		jp3.add(jb2);

		this.add(jp1, BorderLayout.WEST);
		this.add(jp2, BorderLayout.CENTER);
		this.add(jp3, BorderLayout.SOUTH);

		// 展现
		this.setSize(1000, 600);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e)
	{

		// 对用户点击添加按钮后的相应动作

		if (e.getSource() == jb1)
		{
			// 希望添加
			ClieModel temp = new ClieModel();
			String sql = "insert into client values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String[] paras =
			{ jtf1.getText(), jtf2.getText(), jtf3.getText(), jtf4.getText(), jtf5.getText(), jtf6.getText(), jtf7.getText(), jtf8.getText(), jtf9.getText(), jtf10.getText(), jtf11.getText(), jtf12.getText(), jtf13.getText() };
			if (!temp.updateClie(sql, paras))
			{
				// 提示
				JOptionPane.showMessageDialog(this, "添加失败");

			}
			// 关闭对话框
			this.dispose();

		}

	}

}
