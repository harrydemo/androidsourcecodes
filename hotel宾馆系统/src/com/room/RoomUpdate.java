/**
 * 修改学生
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
	// 定义我需要的swing组件
	JLabel jl1, jl2, jl3, jl4;
	JButton jb1, jb2;
	JTextField jtf1, jtf2, jtf3, jtf4;
	JPanel jp1, jp2, jp3;

	// owner他的父窗口 title窗口的名字modal指定是模态窗口还是非模态窗口
	public RoomUpdate(Frame owner, String title, boolean modal, RoomModel sm, int rowNums)
	{
		super(owner, title, modal);
		jl1 = new JLabel("房间号");
		jl2 = new JLabel("房态");
		jl3 = new JLabel("类型");
		jl4 = new JLabel("价钱");

		jtf1 = new JTextField();
		// 初始化数据
		jtf1.setText((String) sm.getValueAt(rowNums, 0));
		jtf2 = new JTextField();
		// 让jtf1不能修改
		jtf1.setEditable(false);
		jtf2.setText((String) sm.getValueAt(rowNums, 1));
		jtf3 = new JTextField();
		jtf3.setText((String) sm.getValueAt(rowNums, 2));
		jtf4 = new JTextField();
		jtf4.setText((String) sm.getValueAt(rowNums, 3));

		jb1 = new JButton("修改");
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
		this.setSize(600, 250);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e)
	{

		// 对用户点击添加按钮后的相应动作

		if (e.getSource() == jb1)
		{
			PreparedStatement ps = null;
			Connection ct = null;
			ResultSet rs = null;
			// Statement st=null;
			try
			{

				// 连接数据库，判断用户是否合法
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				ct = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=restaurant", "sa", "881221");
				// 预编译语句对象
				RoomModel temp = new RoomModel();
				String sql = "update room set status=?,type=?," + "price=? where roomNo=?";
				String[] paras =
				{ jtf2.getText(), jtf3.getText(), jtf4.getText(), jtf1.getText() };
				if (!temp.updateRoom(sql, paras))
				{
					// 提示
					JOptionPane.showMessageDialog(this, "修改失败");

				}
				this.dispose();// 关闭对话框
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
				// 释放语句对象，连接对象
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
